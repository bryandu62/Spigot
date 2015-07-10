package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.base.Ticker;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

@GwtCompatible(emulated=true)
class LocalCache<K, V>
  extends AbstractMap<K, V>
  implements ConcurrentMap<K, V>
{
  static final int MAXIMUM_CAPACITY = 1073741824;
  static final int MAX_SEGMENTS = 65536;
  static final int CONTAINS_VALUE_RETRIES = 3;
  static final int DRAIN_THRESHOLD = 63;
  static final int DRAIN_MAX = 16;
  static final Logger logger = Logger.getLogger(LocalCache.class.getName());
  static final ListeningExecutorService sameThreadExecutor = MoreExecutors.sameThreadExecutor();
  final int segmentMask;
  final int segmentShift;
  final Segment<K, V>[] segments;
  final int concurrencyLevel;
  final Equivalence<Object> keyEquivalence;
  final Equivalence<Object> valueEquivalence;
  final Strength keyStrength;
  final Strength valueStrength;
  final long maxWeight;
  final Weigher<K, V> weigher;
  final long expireAfterAccessNanos;
  final long expireAfterWriteNanos;
  final long refreshNanos;
  final Queue<RemovalNotification<K, V>> removalNotificationQueue;
  final RemovalListener<K, V> removalListener;
  final Ticker ticker;
  final EntryFactory entryFactory;
  final AbstractCache.StatsCounter globalStatsCounter;
  @Nullable
  final CacheLoader<? super K, V> defaultLoader;
  
  LocalCache(CacheBuilder<? super K, ? super V> builder, @Nullable CacheLoader<? super K, V> loader)
  {
    this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
    
    this.keyStrength = builder.getKeyStrength();
    this.valueStrength = builder.getValueStrength();
    
    this.keyEquivalence = builder.getKeyEquivalence();
    this.valueEquivalence = builder.getValueEquivalence();
    
    this.maxWeight = builder.getMaximumWeight();
    this.weigher = builder.getWeigher();
    this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
    this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
    this.refreshNanos = builder.getRefreshNanos();
    
    this.removalListener = builder.getRemovalListener();
    this.removalNotificationQueue = (this.removalListener == CacheBuilder.NullListener.INSTANCE ? discardingQueue() : new ConcurrentLinkedQueue());
    
    this.ticker = builder.getTicker(recordsTime());
    this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
    this.globalStatsCounter = ((AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
    this.defaultLoader = loader;
    
    int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
    if ((evictsBySize()) && (!customWeigher())) {
      initialCapacity = Math.min(initialCapacity, (int)this.maxWeight);
    }
    int segmentShift = 0;
    int segmentCount = 1;
    while ((segmentCount < this.concurrencyLevel) && ((!evictsBySize()) || (segmentCount * 20 <= this.maxWeight)))
    {
      segmentShift++;
      segmentCount <<= 1;
    }
    this.segmentShift = (32 - segmentShift);
    this.segmentMask = (segmentCount - 1);
    
    this.segments = newSegmentArray(segmentCount);
    
    int segmentCapacity = initialCapacity / segmentCount;
    if (segmentCapacity * segmentCount < initialCapacity) {
      segmentCapacity++;
    }
    int segmentSize = 1;
    while (segmentSize < segmentCapacity) {
      segmentSize <<= 1;
    }
    if (evictsBySize())
    {
      long maxSegmentWeight = this.maxWeight / segmentCount + 1L;
      long remainder = this.maxWeight % segmentCount;
      for (int i = 0; i < this.segments.length; i++)
      {
        if (i == remainder) {
          maxSegmentWeight -= 1L;
        }
        this.segments[i] = createSegment(segmentSize, maxSegmentWeight, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
      }
    }
    else
    {
      for (int i = 0; i < this.segments.length; i++) {
        this.segments[i] = createSegment(segmentSize, -1L, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
      }
    }
  }
  
  boolean evictsBySize()
  {
    return this.maxWeight >= 0L;
  }
  
  boolean customWeigher()
  {
    return this.weigher != CacheBuilder.OneWeigher.INSTANCE;
  }
  
  boolean expires()
  {
    return (expiresAfterWrite()) || (expiresAfterAccess());
  }
  
  boolean expiresAfterWrite()
  {
    return this.expireAfterWriteNanos > 0L;
  }
  
  boolean expiresAfterAccess()
  {
    return this.expireAfterAccessNanos > 0L;
  }
  
  boolean refreshes()
  {
    return this.refreshNanos > 0L;
  }
  
  boolean usesAccessQueue()
  {
    return (expiresAfterAccess()) || (evictsBySize());
  }
  
  boolean usesWriteQueue()
  {
    return expiresAfterWrite();
  }
  
  boolean recordsWrite()
  {
    return (expiresAfterWrite()) || (refreshes());
  }
  
  boolean recordsAccess()
  {
    return expiresAfterAccess();
  }
  
  boolean recordsTime()
  {
    return (recordsWrite()) || (recordsAccess());
  }
  
  boolean usesWriteEntries()
  {
    return (usesWriteQueue()) || (recordsWrite());
  }
  
  boolean usesAccessEntries()
  {
    return (usesAccessQueue()) || (recordsAccess());
  }
  
  boolean usesKeyReferences()
  {
    return this.keyStrength != Strength.STRONG;
  }
  
  boolean usesValueReferences()
  {
    return this.valueStrength != Strength.STRONG;
  }
  
  static abstract enum Strength
  {
    STRONG,  SOFT,  WEAK;
    
    private Strength() {}
    
    abstract <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> paramSegment, LocalCache.ReferenceEntry<K, V> paramReferenceEntry, V paramV, int paramInt);
    
    abstract Equivalence<Object> defaultEquivalence();
  }
  
  static abstract enum EntryFactory
  {
    STRONG,  STRONG_ACCESS,  STRONG_WRITE,  STRONG_ACCESS_WRITE,  WEAK,  WEAK_ACCESS,  WEAK_WRITE,  WEAK_ACCESS_WRITE;
    
    static final int ACCESS_MASK = 1;
    static final int WRITE_MASK = 2;
    static final int WEAK_MASK = 4;
    static final EntryFactory[] factories = { STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE };
    
    private EntryFactory() {}
    
    static EntryFactory getFactory(LocalCache.Strength keyStrength, boolean usesAccessQueue, boolean usesWriteQueue)
    {
      int flags = (keyStrength == LocalCache.Strength.WEAK ? 4 : 0) | (usesAccessQueue ? 1 : 0) | (usesWriteQueue ? 2 : 0);
      
      return factories[flags];
    }
    
    abstract <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> paramSegment, K paramK, int paramInt, @Nullable LocalCache.ReferenceEntry<K, V> paramReferenceEntry);
    
    @GuardedBy("Segment.this")
    <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext)
    {
      return newEntry(segment, original.getKey(), original.getHash(), newNext);
    }
    
    @GuardedBy("Segment.this")
    <K, V> void copyAccessEntry(LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newEntry)
    {
      newEntry.setAccessTime(original.getAccessTime());
      
      LocalCache.connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
      LocalCache.connectAccessOrder(newEntry, original.getNextInAccessQueue());
      
      LocalCache.nullifyAccessOrder(original);
    }
    
    @GuardedBy("Segment.this")
    <K, V> void copyWriteEntry(LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newEntry)
    {
      newEntry.setWriteTime(original.getWriteTime());
      
      LocalCache.connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
      LocalCache.connectWriteOrder(newEntry, original.getNextInWriteQueue());
      
      LocalCache.nullifyWriteOrder(original);
    }
  }
  
  static final ValueReference<Object, Object> UNSET = new ValueReference()
  {
    public Object get()
    {
      return null;
    }
    
    public int getWeight()
    {
      return 0;
    }
    
    public LocalCache.ReferenceEntry<Object, Object> getEntry()
    {
      return null;
    }
    
    public LocalCache.ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, @Nullable Object value, LocalCache.ReferenceEntry<Object, Object> entry)
    {
      return this;
    }
    
    public boolean isLoading()
    {
      return false;
    }
    
    public boolean isActive()
    {
      return false;
    }
    
    public Object waitForValue()
    {
      return null;
    }
    
    public void notifyNewValue(Object newValue) {}
  };
  
  static <K, V> ValueReference<K, V> unset()
  {
    return UNSET;
  }
  
  static abstract interface ValueReference<K, V>
  {
    @Nullable
    public abstract V get();
    
    public abstract V waitForValue()
      throws ExecutionException;
    
    public abstract int getWeight();
    
    @Nullable
    public abstract LocalCache.ReferenceEntry<K, V> getEntry();
    
    public abstract ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, @Nullable V paramV, LocalCache.ReferenceEntry<K, V> paramReferenceEntry);
    
    public abstract void notifyNewValue(@Nullable V paramV);
    
    public abstract boolean isLoading();
    
    public abstract boolean isActive();
  }
  
  static abstract interface ReferenceEntry<K, V>
  {
    public abstract LocalCache.ValueReference<K, V> getValueReference();
    
    public abstract void setValueReference(LocalCache.ValueReference<K, V> paramValueReference);
    
    @Nullable
    public abstract ReferenceEntry<K, V> getNext();
    
    public abstract int getHash();
    
    @Nullable
    public abstract K getKey();
    
    public abstract long getAccessTime();
    
    public abstract void setAccessTime(long paramLong);
    
    public abstract ReferenceEntry<K, V> getNextInAccessQueue();
    
    public abstract void setNextInAccessQueue(ReferenceEntry<K, V> paramReferenceEntry);
    
    public abstract ReferenceEntry<K, V> getPreviousInAccessQueue();
    
    public abstract void setPreviousInAccessQueue(ReferenceEntry<K, V> paramReferenceEntry);
    
    public abstract long getWriteTime();
    
    public abstract void setWriteTime(long paramLong);
    
    public abstract ReferenceEntry<K, V> getNextInWriteQueue();
    
    public abstract void setNextInWriteQueue(ReferenceEntry<K, V> paramReferenceEntry);
    
    public abstract ReferenceEntry<K, V> getPreviousInWriteQueue();
    
    public abstract void setPreviousInWriteQueue(ReferenceEntry<K, V> paramReferenceEntry);
  }
  
  private static enum NullEntry
    implements LocalCache.ReferenceEntry<Object, Object>
  {
    INSTANCE;
    
    private NullEntry() {}
    
    public LocalCache.ValueReference<Object, Object> getValueReference()
    {
      return null;
    }
    
    public void setValueReference(LocalCache.ValueReference<Object, Object> valueReference) {}
    
    public LocalCache.ReferenceEntry<Object, Object> getNext()
    {
      return null;
    }
    
    public int getHash()
    {
      return 0;
    }
    
    public Object getKey()
    {
      return null;
    }
    
    public long getAccessTime()
    {
      return 0L;
    }
    
    public void setAccessTime(long time) {}
    
    public LocalCache.ReferenceEntry<Object, Object> getNextInAccessQueue()
    {
      return this;
    }
    
    public void setNextInAccessQueue(LocalCache.ReferenceEntry<Object, Object> next) {}
    
    public LocalCache.ReferenceEntry<Object, Object> getPreviousInAccessQueue()
    {
      return this;
    }
    
    public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<Object, Object> previous) {}
    
    public long getWriteTime()
    {
      return 0L;
    }
    
    public void setWriteTime(long time) {}
    
    public LocalCache.ReferenceEntry<Object, Object> getNextInWriteQueue()
    {
      return this;
    }
    
    public void setNextInWriteQueue(LocalCache.ReferenceEntry<Object, Object> next) {}
    
    public LocalCache.ReferenceEntry<Object, Object> getPreviousInWriteQueue()
    {
      return this;
    }
    
    public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<Object, Object> previous) {}
  }
  
  static abstract class AbstractReferenceEntry<K, V>
    implements LocalCache.ReferenceEntry<K, V>
  {
    public LocalCache.ValueReference<K, V> getValueReference()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setValueReference(LocalCache.ValueReference<K, V> valueReference)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getNext()
    {
      throw new UnsupportedOperationException();
    }
    
    public int getHash()
    {
      throw new UnsupportedOperationException();
    }
    
    public K getKey()
    {
      throw new UnsupportedOperationException();
    }
    
    public long getAccessTime()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setAccessTime(long time)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      throw new UnsupportedOperationException();
    }
    
    public long getWriteTime()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setWriteTime(long time)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      throw new UnsupportedOperationException();
    }
  }
  
  static <K, V> ReferenceEntry<K, V> nullEntry()
  {
    return NullEntry.INSTANCE;
  }
  
  static final Queue<? extends Object> DISCARDING_QUEUE = new AbstractQueue()
  {
    public boolean offer(Object o)
    {
      return true;
    }
    
    public Object peek()
    {
      return null;
    }
    
    public Object poll()
    {
      return null;
    }
    
    public int size()
    {
      return 0;
    }
    
    public Iterator<Object> iterator()
    {
      return Iterators.emptyIterator();
    }
  };
  Set<K> keySet;
  Collection<V> values;
  Set<Map.Entry<K, V>> entrySet;
  
  static <E> Queue<E> discardingQueue()
  {
    return DISCARDING_QUEUE;
  }
  
  static class StrongEntry<K, V>
    extends LocalCache.AbstractReferenceEntry<K, V>
  {
    final K key;
    final int hash;
    final LocalCache.ReferenceEntry<K, V> next;
    
    StrongEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      this.key = key;
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey()
    {
      return (K)this.key;
    }
    
    volatile LocalCache.ValueReference<K, V> valueReference = LocalCache.unset();
    
    public LocalCache.ValueReference<K, V> getValueReference()
    {
      return this.valueReference;
    }
    
    public void setValueReference(LocalCache.ValueReference<K, V> valueReference)
    {
      this.valueReference = valueReference;
    }
    
    public int getHash()
    {
      return this.hash;
    }
    
    public LocalCache.ReferenceEntry<K, V> getNext()
    {
      return this.next;
    }
  }
  
  static final class StrongAccessEntry<K, V>
    extends LocalCache.StrongEntry<K, V>
  {
    StrongAccessEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      super(hash, next);
    }
    
    volatile long accessTime = Long.MAX_VALUE;
    
    public long getAccessTime()
    {
      return this.accessTime;
    }
    
    public void setAccessTime(long time)
    {
      this.accessTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue()
    {
      return this.nextAccess;
    }
    
    public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextAccess = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue()
    {
      return this.previousAccess;
    }
    
    public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousAccess = previous;
    }
  }
  
  static final class StrongWriteEntry<K, V>
    extends LocalCache.StrongEntry<K, V>
  {
    StrongWriteEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      super(hash, next);
    }
    
    volatile long writeTime = Long.MAX_VALUE;
    
    public long getWriteTime()
    {
      return this.writeTime;
    }
    
    public void setWriteTime(long time)
    {
      this.writeTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue()
    {
      return this.nextWrite;
    }
    
    public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextWrite = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue()
    {
      return this.previousWrite;
    }
    
    public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousWrite = previous;
    }
  }
  
  static final class StrongAccessWriteEntry<K, V>
    extends LocalCache.StrongEntry<K, V>
  {
    StrongAccessWriteEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      super(hash, next);
    }
    
    volatile long accessTime = Long.MAX_VALUE;
    
    public long getAccessTime()
    {
      return this.accessTime;
    }
    
    public void setAccessTime(long time)
    {
      this.accessTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue()
    {
      return this.nextAccess;
    }
    
    public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextAccess = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue()
    {
      return this.previousAccess;
    }
    
    public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousAccess = previous;
    }
    
    volatile long writeTime = Long.MAX_VALUE;
    
    public long getWriteTime()
    {
      return this.writeTime;
    }
    
    public void setWriteTime(long time)
    {
      this.writeTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue()
    {
      return this.nextWrite;
    }
    
    public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextWrite = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue()
    {
      return this.previousWrite;
    }
    
    public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousWrite = previous;
    }
  }
  
  static class WeakEntry<K, V>
    extends WeakReference<K>
    implements LocalCache.ReferenceEntry<K, V>
  {
    final int hash;
    final LocalCache.ReferenceEntry<K, V> next;
    
    WeakEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      super(queue);
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey()
    {
      return (K)get();
    }
    
    public long getAccessTime()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setAccessTime(long time)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      throw new UnsupportedOperationException();
    }
    
    public long getWriteTime()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setWriteTime(long time)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      throw new UnsupportedOperationException();
    }
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue()
    {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      throw new UnsupportedOperationException();
    }
    
    volatile LocalCache.ValueReference<K, V> valueReference = LocalCache.unset();
    
    public LocalCache.ValueReference<K, V> getValueReference()
    {
      return this.valueReference;
    }
    
    public void setValueReference(LocalCache.ValueReference<K, V> valueReference)
    {
      this.valueReference = valueReference;
    }
    
    public int getHash()
    {
      return this.hash;
    }
    
    public LocalCache.ReferenceEntry<K, V> getNext()
    {
      return this.next;
    }
  }
  
  static final class WeakAccessEntry<K, V>
    extends LocalCache.WeakEntry<K, V>
  {
    WeakAccessEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      super(key, hash, next);
    }
    
    volatile long accessTime = Long.MAX_VALUE;
    
    public long getAccessTime()
    {
      return this.accessTime;
    }
    
    public void setAccessTime(long time)
    {
      this.accessTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue()
    {
      return this.nextAccess;
    }
    
    public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextAccess = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue()
    {
      return this.previousAccess;
    }
    
    public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousAccess = previous;
    }
  }
  
  static final class WeakWriteEntry<K, V>
    extends LocalCache.WeakEntry<K, V>
  {
    WeakWriteEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      super(key, hash, next);
    }
    
    volatile long writeTime = Long.MAX_VALUE;
    
    public long getWriteTime()
    {
      return this.writeTime;
    }
    
    public void setWriteTime(long time)
    {
      this.writeTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue()
    {
      return this.nextWrite;
    }
    
    public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextWrite = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue()
    {
      return this.previousWrite;
    }
    
    public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousWrite = previous;
    }
  }
  
  static final class WeakAccessWriteEntry<K, V>
    extends LocalCache.WeakEntry<K, V>
  {
    WeakAccessWriteEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      super(key, hash, next);
    }
    
    volatile long accessTime = Long.MAX_VALUE;
    
    public long getAccessTime()
    {
      return this.accessTime;
    }
    
    public void setAccessTime(long time)
    {
      this.accessTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue()
    {
      return this.nextAccess;
    }
    
    public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextAccess = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousAccess = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue()
    {
      return this.previousAccess;
    }
    
    public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousAccess = previous;
    }
    
    volatile long writeTime = Long.MAX_VALUE;
    
    public long getWriteTime()
    {
      return this.writeTime;
    }
    
    public void setWriteTime(long time)
    {
      this.writeTime = time;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> nextWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue()
    {
      return this.nextWrite;
    }
    
    public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next)
    {
      this.nextWrite = next;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> previousWrite = LocalCache.nullEntry();
    
    public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue()
    {
      return this.previousWrite;
    }
    
    public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous)
    {
      this.previousWrite = previous;
    }
  }
  
  static class WeakValueReference<K, V>
    extends WeakReference<V>
    implements LocalCache.ValueReference<K, V>
  {
    final LocalCache.ReferenceEntry<K, V> entry;
    
    WeakValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry)
    {
      super(queue);
      this.entry = entry;
    }
    
    public int getWeight()
    {
      return 1;
    }
    
    public LocalCache.ReferenceEntry<K, V> getEntry()
    {
      return this.entry;
    }
    
    public void notifyNewValue(V newValue) {}
    
    public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry)
    {
      return new WeakValueReference(queue, value, entry);
    }
    
    public boolean isLoading()
    {
      return false;
    }
    
    public boolean isActive()
    {
      return true;
    }
    
    public V waitForValue()
    {
      return (V)get();
    }
  }
  
  static class SoftValueReference<K, V>
    extends SoftReference<V>
    implements LocalCache.ValueReference<K, V>
  {
    final LocalCache.ReferenceEntry<K, V> entry;
    
    SoftValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry)
    {
      super(queue);
      this.entry = entry;
    }
    
    public int getWeight()
    {
      return 1;
    }
    
    public LocalCache.ReferenceEntry<K, V> getEntry()
    {
      return this.entry;
    }
    
    public void notifyNewValue(V newValue) {}
    
    public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry)
    {
      return new SoftValueReference(queue, value, entry);
    }
    
    public boolean isLoading()
    {
      return false;
    }
    
    public boolean isActive()
    {
      return true;
    }
    
    public V waitForValue()
    {
      return (V)get();
    }
  }
  
  static class StrongValueReference<K, V>
    implements LocalCache.ValueReference<K, V>
  {
    final V referent;
    
    StrongValueReference(V referent)
    {
      this.referent = referent;
    }
    
    public V get()
    {
      return (V)this.referent;
    }
    
    public int getWeight()
    {
      return 1;
    }
    
    public LocalCache.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }
    
    public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry)
    {
      return this;
    }
    
    public boolean isLoading()
    {
      return false;
    }
    
    public boolean isActive()
    {
      return true;
    }
    
    public V waitForValue()
    {
      return (V)get();
    }
    
    public void notifyNewValue(V newValue) {}
  }
  
  static final class WeightedWeakValueReference<K, V>
    extends LocalCache.WeakValueReference<K, V>
  {
    final int weight;
    
    WeightedWeakValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry, int weight)
    {
      super(referent, entry);
      this.weight = weight;
    }
    
    public int getWeight()
    {
      return this.weight;
    }
    
    public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry)
    {
      return new WeightedWeakValueReference(queue, value, entry, this.weight);
    }
  }
  
  static final class WeightedSoftValueReference<K, V>
    extends LocalCache.SoftValueReference<K, V>
  {
    final int weight;
    
    WeightedSoftValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry, int weight)
    {
      super(referent, entry);
      this.weight = weight;
    }
    
    public int getWeight()
    {
      return this.weight;
    }
    
    public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry)
    {
      return new WeightedSoftValueReference(queue, value, entry, this.weight);
    }
  }
  
  static final class WeightedStrongValueReference<K, V>
    extends LocalCache.StrongValueReference<K, V>
  {
    final int weight;
    
    WeightedStrongValueReference(V referent, int weight)
    {
      super();
      this.weight = weight;
    }
    
    public int getWeight()
    {
      return this.weight;
    }
  }
  
  static int rehash(int h)
  {
    h += (h << 15 ^ 0xCD7D);
    h ^= h >>> 10;
    h += (h << 3);
    h ^= h >>> 6;
    h += (h << 2) + (h << 14);
    return h ^ h >>> 16;
  }
  
  @GuardedBy("Segment.this")
  @VisibleForTesting
  ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next)
  {
    return segmentFor(hash).newEntry(key, hash, next);
  }
  
  @GuardedBy("Segment.this")
  @VisibleForTesting
  ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext)
  {
    int hash = original.getHash();
    return segmentFor(hash).copyEntry(original, newNext);
  }
  
  @GuardedBy("Segment.this")
  @VisibleForTesting
  ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value, int weight)
  {
    int hash = entry.getHash();
    return this.valueStrength.referenceValue(segmentFor(hash), entry, Preconditions.checkNotNull(value), weight);
  }
  
  int hash(@Nullable Object key)
  {
    int h = this.keyEquivalence.hash(key);
    return rehash(h);
  }
  
  void reclaimValue(ValueReference<K, V> valueReference)
  {
    ReferenceEntry<K, V> entry = valueReference.getEntry();
    int hash = entry.getHash();
    segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
  }
  
  void reclaimKey(ReferenceEntry<K, V> entry)
  {
    int hash = entry.getHash();
    segmentFor(hash).reclaimKey(entry, hash);
  }
  
  @VisibleForTesting
  boolean isLive(ReferenceEntry<K, V> entry, long now)
  {
    return segmentFor(entry.getHash()).getLiveValue(entry, now) != null;
  }
  
  Segment<K, V> segmentFor(int hash)
  {
    return this.segments[(hash >>> this.segmentShift & this.segmentMask)];
  }
  
  Segment<K, V> createSegment(int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter)
  {
    return new Segment(this, initialCapacity, maxSegmentWeight, statsCounter);
  }
  
  @Nullable
  V getLiveValue(ReferenceEntry<K, V> entry, long now)
  {
    if (entry.getKey() == null) {
      return null;
    }
    V value = entry.getValueReference().get();
    if (value == null) {
      return null;
    }
    if (isExpired(entry, now)) {
      return null;
    }
    return value;
  }
  
  boolean isExpired(ReferenceEntry<K, V> entry, long now)
  {
    Preconditions.checkNotNull(entry);
    if ((expiresAfterAccess()) && (now - entry.getAccessTime() >= this.expireAfterAccessNanos)) {
      return true;
    }
    if ((expiresAfterWrite()) && (now - entry.getWriteTime() >= this.expireAfterWriteNanos)) {
      return true;
    }
    return false;
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void connectAccessOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next)
  {
    previous.setNextInAccessQueue(next);
    next.setPreviousInAccessQueue(previous);
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> nulled)
  {
    ReferenceEntry<K, V> nullEntry = nullEntry();
    nulled.setNextInAccessQueue(nullEntry);
    nulled.setPreviousInAccessQueue(nullEntry);
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void connectWriteOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next)
  {
    previous.setNextInWriteQueue(next);
    next.setPreviousInWriteQueue(previous);
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> nulled)
  {
    ReferenceEntry<K, V> nullEntry = nullEntry();
    nulled.setNextInWriteQueue(nullEntry);
    nulled.setPreviousInWriteQueue(nullEntry);
  }
  
  void processPendingNotifications()
  {
    RemovalNotification<K, V> notification;
    while ((notification = (RemovalNotification)this.removalNotificationQueue.poll()) != null) {
      try
      {
        this.removalListener.onRemoval(notification);
      }
      catch (Throwable e)
      {
        logger.log(Level.WARNING, "Exception thrown by removal listener", e);
      }
    }
  }
  
  final Segment<K, V>[] newSegmentArray(int ssize)
  {
    return new Segment[ssize];
  }
  
  static class Segment<K, V>
    extends ReentrantLock
  {
    final LocalCache<K, V> map;
    volatile int count;
    @GuardedBy("Segment.this")
    int totalWeight;
    int modCount;
    int threshold;
    volatile AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table;
    final long maxSegmentWeight;
    final ReferenceQueue<K> keyReferenceQueue;
    final ReferenceQueue<V> valueReferenceQueue;
    final Queue<LocalCache.ReferenceEntry<K, V>> recencyQueue;
    final AtomicInteger readCount = new AtomicInteger();
    @GuardedBy("Segment.this")
    final Queue<LocalCache.ReferenceEntry<K, V>> writeQueue;
    @GuardedBy("Segment.this")
    final Queue<LocalCache.ReferenceEntry<K, V>> accessQueue;
    final AbstractCache.StatsCounter statsCounter;
    
    Segment(LocalCache<K, V> map, int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter)
    {
      this.map = map;
      this.maxSegmentWeight = maxSegmentWeight;
      this.statsCounter = ((AbstractCache.StatsCounter)Preconditions.checkNotNull(statsCounter));
      initTable(newEntryArray(initialCapacity));
      
      this.keyReferenceQueue = (map.usesKeyReferences() ? new ReferenceQueue() : null);
      
      this.valueReferenceQueue = (map.usesValueReferences() ? new ReferenceQueue() : null);
      
      this.recencyQueue = (map.usesAccessQueue() ? new ConcurrentLinkedQueue() : LocalCache.discardingQueue());
      
      this.writeQueue = (map.usesWriteQueue() ? new LocalCache.WriteQueue() : LocalCache.discardingQueue());
      
      this.accessQueue = (map.usesAccessQueue() ? new LocalCache.AccessQueue() : LocalCache.discardingQueue());
    }
    
    AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> newEntryArray(int size)
    {
      return new AtomicReferenceArray(size);
    }
    
    void initTable(AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> newTable)
    {
      this.threshold = (newTable.length() * 3 / 4);
      if ((!this.map.customWeigher()) && (this.threshold == this.maxSegmentWeight)) {
        this.threshold += 1;
      }
      this.table = newTable;
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
    {
      return this.map.entryFactory.newEntry(this, Preconditions.checkNotNull(key), hash, next);
    }
    
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext)
    {
      if (original.getKey() == null) {
        return null;
      }
      LocalCache.ValueReference<K, V> valueReference = original.getValueReference();
      V value = valueReference.get();
      if ((value == null) && (valueReference.isActive())) {
        return null;
      }
      LocalCache.ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
      newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
      return newEntry;
    }
    
    @GuardedBy("Segment.this")
    void setValue(LocalCache.ReferenceEntry<K, V> entry, K key, V value, long now)
    {
      LocalCache.ValueReference<K, V> previous = entry.getValueReference();
      int weight = this.map.weigher.weigh(key, value);
      Preconditions.checkState(weight >= 0, "Weights must be non-negative");
      
      LocalCache.ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value, weight);
      
      entry.setValueReference(valueReference);
      recordWrite(entry, weight, now);
      previous.notifyNewValue(value);
    }
    
    V get(K key, int hash, CacheLoader<? super K, V> loader)
      throws ExecutionException
    {
      Preconditions.checkNotNull(key);
      Preconditions.checkNotNull(loader);
      try
      {
        LocalCache.ReferenceEntry<K, V> e;
        if (this.count != 0)
        {
          e = getEntry(key, hash);
          if (e != null)
          {
            long now = this.map.ticker.read();
            V value = getLiveValue(e, now);
            if (value != null)
            {
              recordRead(e, now);
              this.statsCounter.recordHits(1);
              return (V)scheduleRefresh(e, key, hash, value, now, loader);
            }
            Object valueReference = e.getValueReference();
            if (((LocalCache.ValueReference)valueReference).isLoading()) {
              return (V)waitForLoadingValue(e, key, (LocalCache.ValueReference)valueReference);
            }
          }
        }
        return (V)lockedGetOrLoad(key, hash, loader);
      }
      catch (ExecutionException ee)
      {
        Throwable cause = ee.getCause();
        if ((cause instanceof Error)) {
          throw new ExecutionError((Error)cause);
        }
        if ((cause instanceof RuntimeException)) {
          throw new UncheckedExecutionException(cause);
        }
        throw ee;
      }
      finally
      {
        postReadCleanup();
      }
    }
    
    V lockedGetOrLoad(K key, int hash, CacheLoader<? super K, V> loader)
      throws ExecutionException
    {
      valueReference = null;
      LocalCache.LoadingValueReference<K, V> loadingValueReference = null;
      boolean createNewEntry = true;
      
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        int newCount = this.count - 1;
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            valueReference = e.getValueReference();
            if (valueReference.isLoading())
            {
              createNewEntry = false; break;
            }
            V value = valueReference.get();
            if (value == null)
            {
              enqueueNotification(entryKey, hash, valueReference, RemovalCause.COLLECTED);
            }
            else if (this.map.isExpired(e, now))
            {
              enqueueNotification(entryKey, hash, valueReference, RemovalCause.EXPIRED);
            }
            else
            {
              recordLockedRead(e, now);
              this.statsCounter.recordHits(1);
              
              return value;
            }
            this.writeQueue.remove(e);
            this.accessQueue.remove(e);
            this.count = newCount;
            
            break;
          }
        }
        if (createNewEntry)
        {
          loadingValueReference = new LocalCache.LoadingValueReference();
          if (e == null)
          {
            e = newEntry(key, hash, first);
            e.setValueReference(loadingValueReference);
            table.set(index, e);
          }
          else
          {
            e.setValueReference(loadingValueReference);
          }
        }
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
      if (createNewEntry) {
        try
        {
          synchronized (e)
          {
            return (V)loadSync(key, hash, loadingValueReference, loader);
          }
          return (V)waitForLoadingValue(e, key, valueReference);
        }
        finally
        {
          this.statsCounter.recordMisses(1);
        }
      }
    }
    
    V waitForLoadingValue(LocalCache.ReferenceEntry<K, V> e, K key, LocalCache.ValueReference<K, V> valueReference)
      throws ExecutionException
    {
      if (!valueReference.isLoading()) {
        throw new AssertionError();
      }
      Preconditions.checkState(!Thread.holdsLock(e), "Recursive load of: %s", new Object[] { key });
      try
      {
        V value = valueReference.waitForValue();
        if (value == null) {
          throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
        }
        long now = this.map.ticker.read();
        recordRead(e, now);
        return value;
      }
      finally
      {
        this.statsCounter.recordMisses(1);
      }
    }
    
    V loadSync(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader)
      throws ExecutionException
    {
      ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
      return (V)getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
    }
    
    ListenableFuture<V> loadAsync(final K key, final int hash, final LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader)
    {
      final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
      loadingFuture.addListener(new Runnable()
      {
        public void run()
        {
          try
          {
            newValue = LocalCache.Segment.this.getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
          }
          catch (Throwable t)
          {
            V newValue;
            LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", t);
            loadingValueReference.setException(t);
          }
        }
      }, LocalCache.sameThreadExecutor);
      
      return loadingFuture;
    }
    
    V getAndRecordStats(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> newValue)
      throws ExecutionException
    {
      V value = null;
      try
      {
        value = Uninterruptibles.getUninterruptibly(newValue);
        if (value == null) {
          throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
        }
        this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
        storeLoadedValue(key, hash, loadingValueReference, value);
        return value;
      }
      finally
      {
        if (value == null)
        {
          this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
          removeLoadingValue(key, hash, loadingValueReference);
        }
      }
    }
    
    V scheduleRefresh(LocalCache.ReferenceEntry<K, V> entry, K key, int hash, V oldValue, long now, CacheLoader<? super K, V> loader)
    {
      if ((this.map.refreshes()) && (now - entry.getWriteTime() > this.map.refreshNanos) && (!entry.getValueReference().isLoading()))
      {
        V newValue = refresh(key, hash, loader, true);
        if (newValue != null) {
          return newValue;
        }
      }
      return oldValue;
    }
    
    @Nullable
    V refresh(K key, int hash, CacheLoader<? super K, V> loader, boolean checkTime)
    {
      LocalCache.LoadingValueReference<K, V> loadingValueReference = insertLoadingValueReference(key, hash, checkTime);
      if (loadingValueReference == null) {
        return null;
      }
      ListenableFuture<V> result = loadAsync(key, hash, loadingValueReference, loader);
      if (result.isDone()) {
        try
        {
          return (V)Uninterruptibles.getUninterruptibly(result);
        }
        catch (Throwable t) {}
      }
      return null;
    }
    
    @Nullable
    LocalCache.LoadingValueReference<K, V> insertLoadingValueReference(K key, int hash, boolean checkTime)
    {
      LocalCache.ReferenceEntry<K, V> e = null;
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        LocalCache.ValueReference<K, V> valueReference;
        for (e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            valueReference = e.getValueReference();
            if ((valueReference.isLoading()) || ((checkTime) && (now - e.getWriteTime() < this.map.refreshNanos))) {
              return null;
            }
            this.modCount += 1;
            Object loadingValueReference = new LocalCache.LoadingValueReference(valueReference);
            
            e.setValueReference((LocalCache.ValueReference)loadingValueReference);
            return (LocalCache.LoadingValueReference<K, V>)loadingValueReference;
          }
        }
        this.modCount += 1;
        LocalCache.LoadingValueReference<K, V> loadingValueReference = new LocalCache.LoadingValueReference();
        e = newEntry(key, hash, first);
        e.setValueReference(loadingValueReference);
        table.set(index, e);
        return loadingValueReference;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    void tryDrainReferenceQueues()
    {
      if (tryLock()) {
        try
        {
          drainReferenceQueues();
        }
        finally
        {
          unlock();
        }
      }
    }
    
    @GuardedBy("Segment.this")
    void drainReferenceQueues()
    {
      if (this.map.usesKeyReferences()) {
        drainKeyReferenceQueue();
      }
      if (this.map.usesValueReferences()) {
        drainValueReferenceQueue();
      }
    }
    
    @GuardedBy("Segment.this")
    void drainKeyReferenceQueue()
    {
      int i = 0;
      Reference<? extends K> ref;
      for (; (ref = this.keyReferenceQueue.poll()) != null; i == 16)
      {
        LocalCache.ReferenceEntry<K, V> entry = (LocalCache.ReferenceEntry)ref;
        this.map.reclaimKey(entry);
        i++;
      }
    }
    
    @GuardedBy("Segment.this")
    void drainValueReferenceQueue()
    {
      int i = 0;
      Reference<? extends V> ref;
      for (; (ref = this.valueReferenceQueue.poll()) != null; i == 16)
      {
        LocalCache.ValueReference<K, V> valueReference = (LocalCache.ValueReference)ref;
        this.map.reclaimValue(valueReference);
        i++;
      }
    }
    
    void clearReferenceQueues()
    {
      if (this.map.usesKeyReferences()) {
        clearKeyReferenceQueue();
      }
      if (this.map.usesValueReferences()) {
        clearValueReferenceQueue();
      }
    }
    
    void clearKeyReferenceQueue()
    {
      while (this.keyReferenceQueue.poll() != null) {}
    }
    
    void clearValueReferenceQueue()
    {
      while (this.valueReferenceQueue.poll() != null) {}
    }
    
    void recordRead(LocalCache.ReferenceEntry<K, V> entry, long now)
    {
      if (this.map.recordsAccess()) {
        entry.setAccessTime(now);
      }
      this.recencyQueue.add(entry);
    }
    
    @GuardedBy("Segment.this")
    void recordLockedRead(LocalCache.ReferenceEntry<K, V> entry, long now)
    {
      if (this.map.recordsAccess()) {
        entry.setAccessTime(now);
      }
      this.accessQueue.add(entry);
    }
    
    @GuardedBy("Segment.this")
    void recordWrite(LocalCache.ReferenceEntry<K, V> entry, int weight, long now)
    {
      drainRecencyQueue();
      this.totalWeight += weight;
      if (this.map.recordsAccess()) {
        entry.setAccessTime(now);
      }
      if (this.map.recordsWrite()) {
        entry.setWriteTime(now);
      }
      this.accessQueue.add(entry);
      this.writeQueue.add(entry);
    }
    
    @GuardedBy("Segment.this")
    void drainRecencyQueue()
    {
      LocalCache.ReferenceEntry<K, V> e;
      while ((e = (LocalCache.ReferenceEntry)this.recencyQueue.poll()) != null) {
        if (this.accessQueue.contains(e)) {
          this.accessQueue.add(e);
        }
      }
    }
    
    void tryExpireEntries(long now)
    {
      if (tryLock()) {
        try
        {
          expireEntries(now);
        }
        finally
        {
          unlock();
        }
      }
    }
    
    @GuardedBy("Segment.this")
    void expireEntries(long now)
    {
      drainRecencyQueue();
      LocalCache.ReferenceEntry<K, V> e;
      while (((e = (LocalCache.ReferenceEntry)this.writeQueue.peek()) != null) && (this.map.isExpired(e, now))) {
        if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
          throw new AssertionError();
        }
      }
      while (((e = (LocalCache.ReferenceEntry)this.accessQueue.peek()) != null) && (this.map.isExpired(e, now))) {
        if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
          throw new AssertionError();
        }
      }
    }
    
    @GuardedBy("Segment.this")
    void enqueueNotification(LocalCache.ReferenceEntry<K, V> entry, RemovalCause cause)
    {
      enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference(), cause);
    }
    
    @GuardedBy("Segment.this")
    void enqueueNotification(@Nullable K key, int hash, LocalCache.ValueReference<K, V> valueReference, RemovalCause cause)
    {
      this.totalWeight -= valueReference.getWeight();
      if (cause.wasEvicted()) {
        this.statsCounter.recordEviction();
      }
      if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE)
      {
        V value = valueReference.get();
        RemovalNotification<K, V> notification = new RemovalNotification(key, value, cause);
        this.map.removalNotificationQueue.offer(notification);
      }
    }
    
    @GuardedBy("Segment.this")
    void evictEntries()
    {
      if (!this.map.evictsBySize()) {
        return;
      }
      drainRecencyQueue();
      while (this.totalWeight > this.maxSegmentWeight)
      {
        LocalCache.ReferenceEntry<K, V> e = getNextEvictable();
        if (!removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
          throw new AssertionError();
        }
      }
    }
    
    LocalCache.ReferenceEntry<K, V> getNextEvictable()
    {
      for (LocalCache.ReferenceEntry<K, V> e : this.accessQueue)
      {
        int weight = e.getValueReference().getWeight();
        if (weight > 0) {
          return e;
        }
      }
      throw new AssertionError();
    }
    
    LocalCache.ReferenceEntry<K, V> getFirst(int hash)
    {
      AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
      return (LocalCache.ReferenceEntry)table.get(hash & table.length() - 1);
    }
    
    @Nullable
    LocalCache.ReferenceEntry<K, V> getEntry(Object key, int hash)
    {
      for (LocalCache.ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
        if (e.getHash() == hash)
        {
          K entryKey = e.getKey();
          if (entryKey == null) {
            tryDrainReferenceQueues();
          } else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
            return e;
          }
        }
      }
      return null;
    }
    
    @Nullable
    LocalCache.ReferenceEntry<K, V> getLiveEntry(Object key, int hash, long now)
    {
      LocalCache.ReferenceEntry<K, V> e = getEntry(key, hash);
      if (e == null) {
        return null;
      }
      if (this.map.isExpired(e, now))
      {
        tryExpireEntries(now);
        return null;
      }
      return e;
    }
    
    V getLiveValue(LocalCache.ReferenceEntry<K, V> entry, long now)
    {
      if (entry.getKey() == null)
      {
        tryDrainReferenceQueues();
        return null;
      }
      V value = entry.getValueReference().get();
      if (value == null)
      {
        tryDrainReferenceQueues();
        return null;
      }
      if (this.map.isExpired(entry, now))
      {
        tryExpireEntries(now);
        return null;
      }
      return value;
    }
    
    @Nullable
    V get(Object key, int hash)
    {
      try
      {
        if (this.count != 0)
        {
          long now = this.map.ticker.read();
          LocalCache.ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
          if (e == null) {
            return null;
          }
          Object value = e.getValueReference().get();
          if (value != null)
          {
            recordRead(e, now);
            return (V)scheduleRefresh(e, e.getKey(), hash, value, now, this.map.defaultLoader);
          }
          tryDrainReferenceQueues();
        }
        return null;
      }
      finally
      {
        postReadCleanup();
      }
    }
    
    boolean containsKey(Object key, int hash)
    {
      try
      {
        if (this.count != 0)
        {
          long now = this.map.ticker.read();
          LocalCache.ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
          boolean bool1;
          if (e == null) {
            return false;
          }
          return e.getValueReference().get() != null;
        }
        return false;
      }
      finally
      {
        postReadCleanup();
      }
    }
    
    @VisibleForTesting
    boolean containsValue(Object value)
    {
      try
      {
        if (this.count != 0)
        {
          long now = this.map.ticker.read();
          AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
          int length = table.length();
          for (int i = 0; i < length; i++) {
            for (LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry)table.get(i); e != null; e = e.getNext())
            {
              V entryValue = getLiveValue(e, now);
              if (entryValue != null) {
                if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                  return true;
                }
              }
            }
          }
        }
        return false;
      }
      finally
      {
        postReadCleanup();
      }
    }
    
    @Nullable
    V put(K key, int hash, V value, boolean onlyIfAbsent)
    {
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        int newCount = this.count + 1;
        if (newCount > this.threshold)
        {
          expand();
          newCount = this.count + 1;
        }
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        K entryKey;
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            V ?;
            if (entryValue == null)
            {
              this.modCount += 1;
              if (valueReference.isActive())
              {
                enqueueNotification(key, hash, valueReference, RemovalCause.COLLECTED);
                setValue(e, key, value, now);
                newCount = this.count;
              }
              else
              {
                setValue(e, key, value, now);
                newCount = this.count + 1;
              }
              this.count = newCount;
              evictEntries();
              return null;
            }
            if (onlyIfAbsent)
            {
              recordLockedRead(e, now);
              return entryValue;
            }
            this.modCount += 1;
            enqueueNotification(key, hash, valueReference, RemovalCause.REPLACED);
            setValue(e, key, value, now);
            evictEntries();
            return entryValue;
          }
        }
        this.modCount += 1;
        LocalCache.ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
        setValue(newEntry, key, value, now);
        table.set(index, newEntry);
        newCount = this.count + 1;
        this.count = newCount;
        evictEntries();
        return null;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    @GuardedBy("Segment.this")
    void expand()
    {
      AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> oldTable = this.table;
      int oldCapacity = oldTable.length();
      if (oldCapacity >= 1073741824) {
        return;
      }
      int newCount = this.count;
      AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
      this.threshold = (newTable.length() * 3 / 4);
      int newMask = newTable.length() - 1;
      for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++)
      {
        LocalCache.ReferenceEntry<K, V> head = (LocalCache.ReferenceEntry)oldTable.get(oldIndex);
        if (head != null)
        {
          LocalCache.ReferenceEntry<K, V> next = head.getNext();
          int headIndex = head.getHash() & newMask;
          if (next == null)
          {
            newTable.set(headIndex, head);
          }
          else
          {
            LocalCache.ReferenceEntry<K, V> tail = head;
            int tailIndex = headIndex;
            for (LocalCache.ReferenceEntry<K, V> e = next; e != null; e = e.getNext())
            {
              int newIndex = e.getHash() & newMask;
              if (newIndex != tailIndex)
              {
                tailIndex = newIndex;
                tail = e;
              }
            }
            newTable.set(tailIndex, tail);
            for (LocalCache.ReferenceEntry<K, V> e = head; e != tail; e = e.getNext())
            {
              int newIndex = e.getHash() & newMask;
              LocalCache.ReferenceEntry<K, V> newNext = (LocalCache.ReferenceEntry)newTable.get(newIndex);
              LocalCache.ReferenceEntry<K, V> newFirst = copyEntry(e, newNext);
              if (newFirst != null)
              {
                newTable.set(newIndex, newFirst);
              }
              else
              {
                removeCollectedEntry(e);
                newCount--;
              }
            }
          }
        }
      }
      this.table = newTable;
      this.count = newCount;
    }
    
    boolean replace(K key, int hash, V oldValue, V newValue)
    {
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            int newCount;
            if (entryValue == null)
            {
              if (valueReference.isActive())
              {
                newCount = this.count - 1;
                this.modCount += 1;
                LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference, RemovalCause.COLLECTED);
                
                newCount = this.count - 1;
                table.set(index, newFirst);
                this.count = newCount;
              }
              return 0;
            }
            if (this.map.valueEquivalence.equivalent(oldValue, entryValue))
            {
              this.modCount += 1;
              enqueueNotification(key, hash, valueReference, RemovalCause.REPLACED);
              setValue(e, key, newValue, now);
              evictEntries();
              return 1;
            }
            recordLockedRead(e, now);
            return 0;
          }
        }
        return 0;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    @Nullable
    V replace(K key, int hash, V newValue)
    {
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            int newCount;
            if (entryValue == null)
            {
              if (valueReference.isActive())
              {
                newCount = this.count - 1;
                this.modCount += 1;
                LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference, RemovalCause.COLLECTED);
                
                newCount = this.count - 1;
                table.set(index, newFirst);
                this.count = newCount;
              }
              return null;
            }
            this.modCount += 1;
            enqueueNotification(key, hash, valueReference, RemovalCause.REPLACED);
            setValue(e, key, newValue, now);
            evictEntries();
            return entryValue;
          }
        }
        return null;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    @Nullable
    V remove(Object key, int hash)
    {
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        int newCount = this.count - 1;
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            RemovalCause cause;
            if (entryValue != null)
            {
              cause = RemovalCause.EXPLICIT;
            }
            else
            {
              RemovalCause cause;
              if (valueReference.isActive()) {
                cause = RemovalCause.COLLECTED;
              } else {
                return null;
              }
            }
            RemovalCause cause;
            this.modCount += 1;
            Object newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference, cause);
            
            newCount = this.count - 1;
            table.set(index, newFirst);
            this.count = newCount;
            return entryValue;
          }
        }
        return null;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    boolean storeLoadedValue(K key, int hash, LocalCache.LoadingValueReference<K, V> oldValueReference, V newValue)
    {
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        int newCount = this.count + 1;
        if (newCount > this.threshold)
        {
          expand();
          newCount = this.count + 1;
        }
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        K entryKey;
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            RemovalCause cause;
            if ((oldValueReference == valueReference) || ((entryValue == null) && (valueReference != LocalCache.UNSET)))
            {
              this.modCount += 1;
              if (oldValueReference.isActive())
              {
                cause = entryValue == null ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
                
                enqueueNotification(key, hash, oldValueReference, cause);
                newCount--;
              }
              setValue(e, key, newValue, now);
              this.count = newCount;
              evictEntries();
              return 1;
            }
            valueReference = new LocalCache.WeightedStrongValueReference(newValue, 0);
            enqueueNotification(key, hash, valueReference, RemovalCause.REPLACED);
            return 0;
          }
        }
        this.modCount += 1;
        LocalCache.ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
        setValue(newEntry, key, newValue, now);
        table.set(index, newEntry);
        this.count = newCount;
        evictEntries();
        return 1;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    boolean remove(Object key, int hash, Object value)
    {
      lock();
      try
      {
        long now = this.map.ticker.read();
        preWriteCleanup(now);
        
        int newCount = this.count - 1;
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            RemovalCause cause;
            if (this.map.valueEquivalence.equivalent(value, entryValue))
            {
              cause = RemovalCause.EXPLICIT;
            }
            else
            {
              RemovalCause cause;
              if ((entryValue == null) && (valueReference.isActive())) {
                cause = RemovalCause.COLLECTED;
              } else {
                return false;
              }
            }
            RemovalCause cause;
            this.modCount += 1;
            Object newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference, cause);
            
            newCount = this.count - 1;
            table.set(index, newFirst);
            this.count = newCount;
            return cause == RemovalCause.EXPLICIT;
          }
        }
        return 0;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    void clear()
    {
      if (this.count != 0)
      {
        lock();
        try
        {
          AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
          for (int i = 0; i < table.length(); i++) {
            for (LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry)table.get(i); e != null; e = e.getNext()) {
              if (e.getValueReference().isActive()) {
                enqueueNotification(e, RemovalCause.EXPLICIT);
              }
            }
          }
          for (int i = 0; i < table.length(); i++) {
            table.set(i, null);
          }
          clearReferenceQueues();
          this.writeQueue.clear();
          this.accessQueue.clear();
          this.readCount.set(0);
          
          this.modCount += 1;
          this.count = 0;
        }
        finally
        {
          unlock();
          postWriteCleanup();
        }
      }
    }
    
    @Nullable
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> removeValueFromChain(LocalCache.ReferenceEntry<K, V> first, LocalCache.ReferenceEntry<K, V> entry, @Nullable K key, int hash, LocalCache.ValueReference<K, V> valueReference, RemovalCause cause)
    {
      enqueueNotification(key, hash, valueReference, cause);
      this.writeQueue.remove(entry);
      this.accessQueue.remove(entry);
      if (valueReference.isLoading())
      {
        valueReference.notifyNewValue(null);
        return first;
      }
      return removeEntryFromChain(first, entry);
    }
    
    @Nullable
    @GuardedBy("Segment.this")
    LocalCache.ReferenceEntry<K, V> removeEntryFromChain(LocalCache.ReferenceEntry<K, V> first, LocalCache.ReferenceEntry<K, V> entry)
    {
      int newCount = this.count;
      LocalCache.ReferenceEntry<K, V> newFirst = entry.getNext();
      for (LocalCache.ReferenceEntry<K, V> e = first; e != entry; e = e.getNext())
      {
        LocalCache.ReferenceEntry<K, V> next = copyEntry(e, newFirst);
        if (next != null)
        {
          newFirst = next;
        }
        else
        {
          removeCollectedEntry(e);
          newCount--;
        }
      }
      this.count = newCount;
      return newFirst;
    }
    
    @GuardedBy("Segment.this")
    void removeCollectedEntry(LocalCache.ReferenceEntry<K, V> entry)
    {
      enqueueNotification(entry, RemovalCause.COLLECTED);
      this.writeQueue.remove(entry);
      this.accessQueue.remove(entry);
    }
    
    boolean reclaimKey(LocalCache.ReferenceEntry<K, V> entry, int hash)
    {
      lock();
      try
      {
        int newCount = this.count - 1;
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          if (e == entry)
          {
            this.modCount += 1;
            LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference(), RemovalCause.COLLECTED);
            
            newCount = this.count - 1;
            table.set(index, newFirst);
            this.count = newCount;
            return true;
          }
        }
        return 0;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    boolean reclaimValue(K key, int hash, LocalCache.ValueReference<K, V> valueReference)
    {
      lock();
      try
      {
        int newCount = this.count - 1;
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> v = e.getValueReference();
            LocalCache.ReferenceEntry<K, V> newFirst;
            if (v == valueReference)
            {
              this.modCount += 1;
              newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference, RemovalCause.COLLECTED);
              
              newCount = this.count - 1;
              table.set(index, newFirst);
              this.count = newCount;
              return true;
            }
            return 0;
          }
        }
        return 0;
      }
      finally
      {
        unlock();
        if (!isHeldByCurrentThread()) {
          postWriteCleanup();
        }
      }
    }
    
    boolean removeLoadingValue(K key, int hash, LocalCache.LoadingValueReference<K, V> valueReference)
    {
      lock();
      try
      {
        AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
        for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
        {
          K entryKey = e.getKey();
          if ((e.getHash() == hash) && (entryKey != null) && (this.map.keyEquivalence.equivalent(key, entryKey)))
          {
            LocalCache.ValueReference<K, V> v = e.getValueReference();
            LocalCache.ReferenceEntry<K, V> newFirst;
            if (v == valueReference)
            {
              if (valueReference.isActive())
              {
                e.setValueReference(valueReference.getOldValue());
              }
              else
              {
                newFirst = removeEntryFromChain(first, e);
                table.set(index, newFirst);
              }
              return 1;
            }
            return 0;
          }
        }
        return 0;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
    }
    
    @GuardedBy("Segment.this")
    boolean removeEntry(LocalCache.ReferenceEntry<K, V> entry, int hash, RemovalCause cause)
    {
      int newCount = this.count - 1;
      AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
      int index = hash & table.length() - 1;
      LocalCache.ReferenceEntry<K, V> first = (LocalCache.ReferenceEntry)table.get(index);
      for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
        if (e == entry)
        {
          this.modCount += 1;
          LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference(), cause);
          
          newCount = this.count - 1;
          table.set(index, newFirst);
          this.count = newCount;
          return true;
        }
      }
      return false;
    }
    
    void postReadCleanup()
    {
      if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
        cleanUp();
      }
    }
    
    @GuardedBy("Segment.this")
    void preWriteCleanup(long now)
    {
      runLockedCleanup(now);
    }
    
    void postWriteCleanup()
    {
      runUnlockedCleanup();
    }
    
    void cleanUp()
    {
      long now = this.map.ticker.read();
      runLockedCleanup(now);
      runUnlockedCleanup();
    }
    
    void runLockedCleanup(long now)
    {
      if (tryLock()) {
        try
        {
          drainReferenceQueues();
          expireEntries(now);
          this.readCount.set(0);
        }
        finally
        {
          unlock();
        }
      }
    }
    
    void runUnlockedCleanup()
    {
      if (!isHeldByCurrentThread()) {
        this.map.processPendingNotifications();
      }
    }
  }
  
  static class LoadingValueReference<K, V>
    implements LocalCache.ValueReference<K, V>
  {
    volatile LocalCache.ValueReference<K, V> oldValue;
    final SettableFuture<V> futureValue = SettableFuture.create();
    final Stopwatch stopwatch = Stopwatch.createUnstarted();
    
    public LoadingValueReference()
    {
      this(LocalCache.unset());
    }
    
    public LoadingValueReference(LocalCache.ValueReference<K, V> oldValue)
    {
      this.oldValue = oldValue;
    }
    
    public boolean isLoading()
    {
      return true;
    }
    
    public boolean isActive()
    {
      return this.oldValue.isActive();
    }
    
    public int getWeight()
    {
      return this.oldValue.getWeight();
    }
    
    public boolean set(@Nullable V newValue)
    {
      return this.futureValue.set(newValue);
    }
    
    public boolean setException(Throwable t)
    {
      return this.futureValue.setException(t);
    }
    
    private ListenableFuture<V> fullyFailedFuture(Throwable t)
    {
      return Futures.immediateFailedFuture(t);
    }
    
    public void notifyNewValue(@Nullable V newValue)
    {
      if (newValue != null) {
        set(newValue);
      } else {
        this.oldValue = LocalCache.unset();
      }
    }
    
    public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader)
    {
      this.stopwatch.start();
      V previousValue = this.oldValue.get();
      try
      {
        if (previousValue == null)
        {
          V newValue = loader.load(key);
          return set(newValue) ? this.futureValue : Futures.immediateFuture(newValue);
        }
        ListenableFuture<V> newValue = loader.reload(key, previousValue);
        if (newValue == null) {
          return Futures.immediateFuture(null);
        }
        Futures.transform(newValue, new Function()
        {
          public V apply(V newValue)
          {
            LocalCache.LoadingValueReference.this.set(newValue);
            return newValue;
          }
        });
      }
      catch (Throwable t)
      {
        if ((t instanceof InterruptedException)) {
          Thread.currentThread().interrupt();
        }
        return setException(t) ? this.futureValue : fullyFailedFuture(t);
      }
    }
    
    public long elapsedNanos()
    {
      return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
    }
    
    public V waitForValue()
      throws ExecutionException
    {
      return (V)Uninterruptibles.getUninterruptibly(this.futureValue);
    }
    
    public V get()
    {
      return (V)this.oldValue.get();
    }
    
    public LocalCache.ValueReference<K, V> getOldValue()
    {
      return this.oldValue;
    }
    
    public LocalCache.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }
    
    public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @Nullable V value, LocalCache.ReferenceEntry<K, V> entry)
    {
      return this;
    }
  }
  
  static final class WriteQueue<K, V>
    extends AbstractQueue<LocalCache.ReferenceEntry<K, V>>
  {
    final LocalCache.ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry()
    {
      public long getWriteTime()
      {
        return Long.MAX_VALUE;
      }
      
      LocalCache.ReferenceEntry<K, V> nextWrite = this;
      
      public void setWriteTime(long time) {}
      
      public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue()
      {
        return this.nextWrite;
      }
      
      public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next)
      {
        this.nextWrite = next;
      }
      
      LocalCache.ReferenceEntry<K, V> previousWrite = this;
      
      public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue()
      {
        return this.previousWrite;
      }
      
      public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous)
      {
        this.previousWrite = previous;
      }
    };
    
    public boolean offer(LocalCache.ReferenceEntry<K, V> entry)
    {
      LocalCache.connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());
      
      LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), entry);
      LocalCache.connectWriteOrder(entry, this.head);
      
      return true;
    }
    
    public LocalCache.ReferenceEntry<K, V> peek()
    {
      LocalCache.ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
      return next == this.head ? null : next;
    }
    
    public LocalCache.ReferenceEntry<K, V> poll()
    {
      LocalCache.ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
      if (next == this.head) {
        return null;
      }
      remove(next);
      return next;
    }
    
    public boolean remove(Object o)
    {
      LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry)o;
      LocalCache.ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
      LocalCache.ReferenceEntry<K, V> next = e.getNextInWriteQueue();
      LocalCache.connectWriteOrder(previous, next);
      LocalCache.nullifyWriteOrder(e);
      
      return next != LocalCache.NullEntry.INSTANCE;
    }
    
    public boolean contains(Object o)
    {
      LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry)o;
      return e.getNextInWriteQueue() != LocalCache.NullEntry.INSTANCE;
    }
    
    public boolean isEmpty()
    {
      return this.head.getNextInWriteQueue() == this.head;
    }
    
    public int size()
    {
      int size = 0;
      for (LocalCache.ReferenceEntry<K, V> e = this.head.getNextInWriteQueue(); e != this.head; e = e.getNextInWriteQueue()) {
        size++;
      }
      return size;
    }
    
    public void clear()
    {
      LocalCache.ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
      while (e != this.head)
      {
        LocalCache.ReferenceEntry<K, V> next = e.getNextInWriteQueue();
        LocalCache.nullifyWriteOrder(e);
        e = next;
      }
      this.head.setNextInWriteQueue(this.head);
      this.head.setPreviousInWriteQueue(this.head);
    }
    
    public Iterator<LocalCache.ReferenceEntry<K, V>> iterator()
    {
      new AbstractSequentialIterator(peek())
      {
        protected LocalCache.ReferenceEntry<K, V> computeNext(LocalCache.ReferenceEntry<K, V> previous)
        {
          LocalCache.ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
          return next == LocalCache.WriteQueue.this.head ? null : next;
        }
      };
    }
  }
  
  static final class AccessQueue<K, V>
    extends AbstractQueue<LocalCache.ReferenceEntry<K, V>>
  {
    final LocalCache.ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry()
    {
      public long getAccessTime()
      {
        return Long.MAX_VALUE;
      }
      
      LocalCache.ReferenceEntry<K, V> nextAccess = this;
      
      public void setAccessTime(long time) {}
      
      public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue()
      {
        return this.nextAccess;
      }
      
      public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next)
      {
        this.nextAccess = next;
      }
      
      LocalCache.ReferenceEntry<K, V> previousAccess = this;
      
      public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue()
      {
        return this.previousAccess;
      }
      
      public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous)
      {
        this.previousAccess = previous;
      }
    };
    
    public boolean offer(LocalCache.ReferenceEntry<K, V> entry)
    {
      LocalCache.connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());
      
      LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), entry);
      LocalCache.connectAccessOrder(entry, this.head);
      
      return true;
    }
    
    public LocalCache.ReferenceEntry<K, V> peek()
    {
      LocalCache.ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
      return next == this.head ? null : next;
    }
    
    public LocalCache.ReferenceEntry<K, V> poll()
    {
      LocalCache.ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
      if (next == this.head) {
        return null;
      }
      remove(next);
      return next;
    }
    
    public boolean remove(Object o)
    {
      LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry)o;
      LocalCache.ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
      LocalCache.ReferenceEntry<K, V> next = e.getNextInAccessQueue();
      LocalCache.connectAccessOrder(previous, next);
      LocalCache.nullifyAccessOrder(e);
      
      return next != LocalCache.NullEntry.INSTANCE;
    }
    
    public boolean contains(Object o)
    {
      LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry)o;
      return e.getNextInAccessQueue() != LocalCache.NullEntry.INSTANCE;
    }
    
    public boolean isEmpty()
    {
      return this.head.getNextInAccessQueue() == this.head;
    }
    
    public int size()
    {
      int size = 0;
      for (LocalCache.ReferenceEntry<K, V> e = this.head.getNextInAccessQueue(); e != this.head; e = e.getNextInAccessQueue()) {
        size++;
      }
      return size;
    }
    
    public void clear()
    {
      LocalCache.ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
      while (e != this.head)
      {
        LocalCache.ReferenceEntry<K, V> next = e.getNextInAccessQueue();
        LocalCache.nullifyAccessOrder(e);
        e = next;
      }
      this.head.setNextInAccessQueue(this.head);
      this.head.setPreviousInAccessQueue(this.head);
    }
    
    public Iterator<LocalCache.ReferenceEntry<K, V>> iterator()
    {
      new AbstractSequentialIterator(peek())
      {
        protected LocalCache.ReferenceEntry<K, V> computeNext(LocalCache.ReferenceEntry<K, V> previous)
        {
          LocalCache.ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
          return next == LocalCache.AccessQueue.this.head ? null : next;
        }
      };
    }
  }
  
  public void cleanUp()
  {
    for (Segment<?, ?> segment : this.segments) {
      segment.cleanUp();
    }
  }
  
  public boolean isEmpty()
  {
    long sum = 0L;
    Segment<K, V>[] segments = this.segments;
    for (int i = 0; i < segments.length; i++)
    {
      if (segments[i].count != 0) {
        return false;
      }
      sum += segments[i].modCount;
    }
    if (sum != 0L)
    {
      for (int i = 0; i < segments.length; i++)
      {
        if (segments[i].count != 0) {
          return false;
        }
        sum -= segments[i].modCount;
      }
      if (sum != 0L) {
        return false;
      }
    }
    return true;
  }
  
  long longSize()
  {
    Segment<K, V>[] segments = this.segments;
    long sum = 0L;
    for (int i = 0; i < segments.length; i++) {
      sum += segments[i].count;
    }
    return sum;
  }
  
  public int size()
  {
    return Ints.saturatedCast(longSize());
  }
  
  @Nullable
  public V get(@Nullable Object key)
  {
    if (key == null) {
      return null;
    }
    int hash = hash(key);
    return (V)segmentFor(hash).get(key, hash);
  }
  
  @Nullable
  public V getIfPresent(Object key)
  {
    int hash = hash(Preconditions.checkNotNull(key));
    V value = segmentFor(hash).get(key, hash);
    if (value == null) {
      this.globalStatsCounter.recordMisses(1);
    } else {
      this.globalStatsCounter.recordHits(1);
    }
    return value;
  }
  
  V get(K key, CacheLoader<? super K, V> loader)
    throws ExecutionException
  {
    int hash = hash(Preconditions.checkNotNull(key));
    return (V)segmentFor(hash).get(key, hash, loader);
  }
  
  V getOrLoad(K key)
    throws ExecutionException
  {
    return (V)get(key, this.defaultLoader);
  }
  
  ImmutableMap<K, V> getAllPresent(Iterable<?> keys)
  {
    int hits = 0;
    int misses = 0;
    
    Map<K, V> result = Maps.newLinkedHashMap();
    for (Object key : keys)
    {
      V value = get(key);
      if (value == null)
      {
        misses++;
      }
      else
      {
        K castKey = (K)key;
        result.put(castKey, value);
        hits++;
      }
    }
    this.globalStatsCounter.recordHits(hits);
    this.globalStatsCounter.recordMisses(misses);
    return ImmutableMap.copyOf(result);
  }
  
  ImmutableMap<K, V> getAll(Iterable<? extends K> keys)
    throws ExecutionException
  {
    int hits = 0;
    int misses = 0;
    
    Map<K, V> result = Maps.newLinkedHashMap();
    Set<K> keysToLoad = Sets.newLinkedHashSet();
    for (K key : keys)
    {
      V value = get(key);
      if (!result.containsKey(key))
      {
        result.put(key, value);
        if (value == null)
        {
          misses++;
          keysToLoad.add(key);
        }
        else
        {
          hits++;
        }
      }
    }
    try
    {
      if (!keysToLoad.isEmpty())
      {
        Iterator i$;
        try
        {
          newEntries = loadAll(keysToLoad, this.defaultLoader);
          for (K key : keysToLoad)
          {
            V value = newEntries.get(key);
            if (value == null) {
              throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + key);
            }
            result.put(key, value);
          }
        }
        catch (CacheLoader.UnsupportedLoadingOperationException e)
        {
          Map<K, V> newEntries;
          i$ = keysToLoad.iterator();
        }
        while (i$.hasNext())
        {
          K key = i$.next();
          misses--;
          result.put(key, get(key, this.defaultLoader));
        }
      }
      return ImmutableMap.copyOf(result);
    }
    finally
    {
      this.globalStatsCounter.recordHits(hits);
      this.globalStatsCounter.recordMisses(misses);
    }
  }
  
  @Nullable
  Map<K, V> loadAll(Set<? extends K> keys, CacheLoader<? super K, V> loader)
    throws ExecutionException
  {
    Preconditions.checkNotNull(loader);
    Preconditions.checkNotNull(keys);
    Stopwatch stopwatch = Stopwatch.createStarted();
    
    boolean success = false;
    Map<K, V> result;
    try
    {
      Map<K, V> map = loader.loadAll(keys);
      result = map;
      success = true;
    }
    catch (CacheLoader.UnsupportedLoadingOperationException e)
    {
      success = true;
      throw e;
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      throw new ExecutionException(e);
    }
    catch (RuntimeException e)
    {
      throw new UncheckedExecutionException(e);
    }
    catch (Exception e)
    {
      throw new ExecutionException(e);
    }
    catch (Error e)
    {
      throw new ExecutionError(e);
    }
    finally
    {
      if (!success) {
        this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
      }
    }
    if (result == null)
    {
      this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
      throw new CacheLoader.InvalidCacheLoadException(loader + " returned null map from loadAll");
    }
    stopwatch.stop();
    
    boolean nullsPresent = false;
    for (Map.Entry<K, V> entry : result.entrySet())
    {
      K key = entry.getKey();
      V value = entry.getValue();
      if ((key == null) || (value == null)) {
        nullsPresent = true;
      } else {
        put(key, value);
      }
    }
    if (nullsPresent)
    {
      this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
      throw new CacheLoader.InvalidCacheLoadException(loader + " returned null keys or values from loadAll");
    }
    this.globalStatsCounter.recordLoadSuccess(stopwatch.elapsed(TimeUnit.NANOSECONDS));
    return result;
  }
  
  ReferenceEntry<K, V> getEntry(@Nullable Object key)
  {
    if (key == null) {
      return null;
    }
    int hash = hash(key);
    return segmentFor(hash).getEntry(key, hash);
  }
  
  void refresh(K key)
  {
    int hash = hash(Preconditions.checkNotNull(key));
    segmentFor(hash).refresh(key, hash, this.defaultLoader, false);
  }
  
  public boolean containsKey(@Nullable Object key)
  {
    if (key == null) {
      return false;
    }
    int hash = hash(key);
    return segmentFor(hash).containsKey(key, hash);
  }
  
  public boolean containsValue(@Nullable Object value)
  {
    if (value == null) {
      return false;
    }
    long now = this.ticker.read();
    Segment<K, V>[] segments = this.segments;
    long last = -1L;
    for (int i = 0; i < 3; i++)
    {
      long sum = 0L;
      for (Segment<K, V> segment : segments)
      {
        int c = segment.count;
        
        AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
        for (int j = 0; j < table.length(); j++) {
          for (ReferenceEntry<K, V> e = (ReferenceEntry)table.get(j); e != null; e = e.getNext())
          {
            V v = segment.getLiveValue(e, now);
            if ((v != null) && (this.valueEquivalence.equivalent(value, v))) {
              return true;
            }
          }
        }
        sum += segment.modCount;
      }
      if (sum == last) {
        break;
      }
      last = sum;
    }
    return false;
  }
  
  public V put(K key, V value)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return (V)segmentFor(hash).put(key, hash, value, false);
  }
  
  public V putIfAbsent(K key, V value)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return (V)segmentFor(hash).put(key, hash, value, true);
  }
  
  public void putAll(Map<? extends K, ? extends V> m)
  {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  
  public V remove(@Nullable Object key)
  {
    if (key == null) {
      return null;
    }
    int hash = hash(key);
    return (V)segmentFor(hash).remove(key, hash);
  }
  
  public boolean remove(@Nullable Object key, @Nullable Object value)
  {
    if ((key == null) || (value == null)) {
      return false;
    }
    int hash = hash(key);
    return segmentFor(hash).remove(key, hash, value);
  }
  
  public boolean replace(K key, @Nullable V oldValue, V newValue)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(newValue);
    if (oldValue == null) {
      return false;
    }
    int hash = hash(key);
    return segmentFor(hash).replace(key, hash, oldValue, newValue);
  }
  
  public V replace(K key, V value)
  {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return (V)segmentFor(hash).replace(key, hash, value);
  }
  
  public void clear()
  {
    for (Segment<K, V> segment : this.segments) {
      segment.clear();
    }
  }
  
  void invalidateAll(Iterable<?> keys)
  {
    for (Object key : keys) {
      remove(key);
    }
  }
  
  public Set<K> keySet()
  {
    Set<K> ks = this.keySet;
    return ks != null ? ks : (this.keySet = new KeySet(this));
  }
  
  public Collection<V> values()
  {
    Collection<V> vs = this.values;
    return vs != null ? vs : (this.values = new Values(this));
  }
  
  @GwtIncompatible("Not supported.")
  public Set<Map.Entry<K, V>> entrySet()
  {
    Set<Map.Entry<K, V>> es = this.entrySet;
    return es != null ? es : (this.entrySet = new EntrySet(this));
  }
  
  abstract class HashIterator<T>
    implements Iterator<T>
  {
    int nextSegmentIndex;
    int nextTableIndex;
    LocalCache.Segment<K, V> currentSegment;
    AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> currentTable;
    LocalCache.ReferenceEntry<K, V> nextEntry;
    LocalCache<K, V>.WriteThroughEntry nextExternal;
    LocalCache<K, V>.WriteThroughEntry lastReturned;
    
    HashIterator()
    {
      this.nextSegmentIndex = (LocalCache.this.segments.length - 1);
      this.nextTableIndex = -1;
      advance();
    }
    
    public abstract T next();
    
    final void advance()
    {
      this.nextExternal = null;
      if (nextInChain()) {
        return;
      }
      if (nextInTable()) {
        return;
      }
      while (this.nextSegmentIndex >= 0)
      {
        this.currentSegment = LocalCache.this.segments[(this.nextSegmentIndex--)];
        if (this.currentSegment.count != 0)
        {
          this.currentTable = this.currentSegment.table;
          this.nextTableIndex = (this.currentTable.length() - 1);
          if (nextInTable()) {}
        }
      }
    }
    
    boolean nextInChain()
    {
      if (this.nextEntry != null) {
        for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
          if (advanceTo(this.nextEntry)) {
            return true;
          }
        }
      }
      return false;
    }
    
    boolean nextInTable()
    {
      while (this.nextTableIndex >= 0) {
        if (((this.nextEntry = (LocalCache.ReferenceEntry)this.currentTable.get(this.nextTableIndex--)) != null) && (
          (advanceTo(this.nextEntry)) || (nextInChain()))) {
          return true;
        }
      }
      return false;
    }
    
    boolean advanceTo(LocalCache.ReferenceEntry<K, V> entry)
    {
      try
      {
        long now = LocalCache.this.ticker.read();
        K key = entry.getKey();
        V value = LocalCache.this.getLiveValue(entry, now);
        boolean bool;
        if (value != null)
        {
          this.nextExternal = new LocalCache.WriteThroughEntry(LocalCache.this, key, value);
          return true;
        }
        return false;
      }
      finally
      {
        this.currentSegment.postReadCleanup();
      }
    }
    
    public boolean hasNext()
    {
      return this.nextExternal != null;
    }
    
    LocalCache<K, V>.WriteThroughEntry nextEntry()
    {
      if (this.nextExternal == null) {
        throw new NoSuchElementException();
      }
      this.lastReturned = this.nextExternal;
      advance();
      return this.lastReturned;
    }
    
    public void remove()
    {
      Preconditions.checkState(this.lastReturned != null);
      LocalCache.this.remove(this.lastReturned.getKey());
      this.lastReturned = null;
    }
  }
  
  final class KeyIterator
    extends LocalCache<K, V>.HashIterator<K>
  {
    KeyIterator()
    {
      super();
    }
    
    public K next()
    {
      return (K)nextEntry().getKey();
    }
  }
  
  final class ValueIterator
    extends LocalCache<K, V>.HashIterator<V>
  {
    ValueIterator()
    {
      super();
    }
    
    public V next()
    {
      return (V)nextEntry().getValue();
    }
  }
  
  final class WriteThroughEntry
    implements Map.Entry<K, V>
  {
    final K key;
    V value;
    
    WriteThroughEntry(V key)
    {
      this.key = key;
      this.value = value;
    }
    
    public K getKey()
    {
      return (K)this.key;
    }
    
    public V getValue()
    {
      return (V)this.value;
    }
    
    public boolean equals(@Nullable Object object)
    {
      if ((object instanceof Map.Entry))
      {
        Map.Entry<?, ?> that = (Map.Entry)object;
        return (this.key.equals(that.getKey())) && (this.value.equals(that.getValue()));
      }
      return false;
    }
    
    public int hashCode()
    {
      return this.key.hashCode() ^ this.value.hashCode();
    }
    
    public V setValue(V newValue)
    {
      throw new UnsupportedOperationException();
    }
    
    public String toString()
    {
      return getKey() + "=" + getValue();
    }
  }
  
  final class EntryIterator
    extends LocalCache<K, V>.HashIterator<Map.Entry<K, V>>
  {
    EntryIterator()
    {
      super();
    }
    
    public Map.Entry<K, V> next()
    {
      return nextEntry();
    }
  }
  
  abstract class AbstractCacheSet<T>
    extends AbstractSet<T>
  {
    final ConcurrentMap<?, ?> map;
    
    AbstractCacheSet()
    {
      this.map = map;
    }
    
    public int size()
    {
      return this.map.size();
    }
    
    public boolean isEmpty()
    {
      return this.map.isEmpty();
    }
    
    public void clear()
    {
      this.map.clear();
    }
  }
  
  final class KeySet
    extends LocalCache<K, V>.AbstractCacheSet<K>
  {
    KeySet()
    {
      super(map);
    }
    
    public Iterator<K> iterator()
    {
      return new LocalCache.KeyIterator(LocalCache.this);
    }
    
    public boolean contains(Object o)
    {
      return this.map.containsKey(o);
    }
    
    public boolean remove(Object o)
    {
      return this.map.remove(o) != null;
    }
  }
  
  final class Values
    extends AbstractCollection<V>
  {
    private final ConcurrentMap<?, ?> map;
    
    Values()
    {
      this.map = map;
    }
    
    public int size()
    {
      return this.map.size();
    }
    
    public boolean isEmpty()
    {
      return this.map.isEmpty();
    }
    
    public void clear()
    {
      this.map.clear();
    }
    
    public Iterator<V> iterator()
    {
      return new LocalCache.ValueIterator(LocalCache.this);
    }
    
    public boolean contains(Object o)
    {
      return this.map.containsValue(o);
    }
  }
  
  final class EntrySet
    extends LocalCache<K, V>.AbstractCacheSet<Map.Entry<K, V>>
  {
    EntrySet()
    {
      super(map);
    }
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new LocalCache.EntryIterator(LocalCache.this);
    }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      Object key = e.getKey();
      if (key == null) {
        return false;
      }
      V v = LocalCache.this.get(key);
      
      return (v != null) && (LocalCache.this.valueEquivalence.equivalent(e.getValue(), v));
    }
    
    public boolean remove(Object o)
    {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry<?, ?> e = (Map.Entry)o;
      Object key = e.getKey();
      return (key != null) && (LocalCache.this.remove(key, e.getValue()));
    }
  }
  
  static class ManualSerializationProxy<K, V>
    extends ForwardingCache<K, V>
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    final LocalCache.Strength keyStrength;
    final LocalCache.Strength valueStrength;
    final Equivalence<Object> keyEquivalence;
    final Equivalence<Object> valueEquivalence;
    final long expireAfterWriteNanos;
    final long expireAfterAccessNanos;
    final long maxWeight;
    final Weigher<K, V> weigher;
    final int concurrencyLevel;
    final RemovalListener<? super K, ? super V> removalListener;
    final Ticker ticker;
    final CacheLoader<? super K, V> loader;
    transient Cache<K, V> delegate;
    
    ManualSerializationProxy(LocalCache<K, V> cache)
    {
      this(cache.keyStrength, cache.valueStrength, cache.keyEquivalence, cache.valueEquivalence, cache.expireAfterWriteNanos, cache.expireAfterAccessNanos, cache.maxWeight, cache.weigher, cache.concurrencyLevel, cache.removalListener, cache.ticker, cache.defaultLoader);
    }
    
    private ManualSerializationProxy(LocalCache.Strength keyStrength, LocalCache.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, long maxWeight, Weigher<K, V> weigher, int concurrencyLevel, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> loader)
    {
      this.keyStrength = keyStrength;
      this.valueStrength = valueStrength;
      this.keyEquivalence = keyEquivalence;
      this.valueEquivalence = valueEquivalence;
      this.expireAfterWriteNanos = expireAfterWriteNanos;
      this.expireAfterAccessNanos = expireAfterAccessNanos;
      this.maxWeight = maxWeight;
      this.weigher = weigher;
      this.concurrencyLevel = concurrencyLevel;
      this.removalListener = removalListener;
      this.ticker = ((ticker == Ticker.systemTicker()) || (ticker == CacheBuilder.NULL_TICKER) ? null : ticker);
      
      this.loader = loader;
    }
    
    CacheBuilder<K, V> recreateCacheBuilder()
    {
      CacheBuilder<K, V> builder = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
      
      builder.strictParsing = false;
      if (this.expireAfterWriteNanos > 0L) {
        builder.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
      }
      if (this.expireAfterAccessNanos > 0L) {
        builder.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
      }
      if (this.weigher != CacheBuilder.OneWeigher.INSTANCE)
      {
        builder.weigher(this.weigher);
        if (this.maxWeight != -1L) {
          builder.maximumWeight(this.maxWeight);
        }
      }
      else if (this.maxWeight != -1L)
      {
        builder.maximumSize(this.maxWeight);
      }
      if (this.ticker != null) {
        builder.ticker(this.ticker);
      }
      return builder;
    }
    
    private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException
    {
      in.defaultReadObject();
      CacheBuilder<K, V> builder = recreateCacheBuilder();
      this.delegate = builder.build();
    }
    
    private Object readResolve()
    {
      return this.delegate;
    }
    
    protected Cache<K, V> delegate()
    {
      return this.delegate;
    }
  }
  
  static final class LoadingSerializationProxy<K, V>
    extends LocalCache.ManualSerializationProxy<K, V>
    implements LoadingCache<K, V>, Serializable
  {
    private static final long serialVersionUID = 1L;
    transient LoadingCache<K, V> autoDelegate;
    
    LoadingSerializationProxy(LocalCache<K, V> cache)
    {
      super();
    }
    
    private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException
    {
      in.defaultReadObject();
      CacheBuilder<K, V> builder = recreateCacheBuilder();
      this.autoDelegate = builder.build(this.loader);
    }
    
    public V get(K key)
      throws ExecutionException
    {
      return (V)this.autoDelegate.get(key);
    }
    
    public V getUnchecked(K key)
    {
      return (V)this.autoDelegate.getUnchecked(key);
    }
    
    public ImmutableMap<K, V> getAll(Iterable<? extends K> keys)
      throws ExecutionException
    {
      return this.autoDelegate.getAll(keys);
    }
    
    public final V apply(K key)
    {
      return (V)this.autoDelegate.apply(key);
    }
    
    public void refresh(K key)
    {
      this.autoDelegate.refresh(key);
    }
    
    private Object readResolve()
    {
      return this.autoDelegate;
    }
  }
  
  static class LocalManualCache<K, V>
    implements Cache<K, V>, Serializable
  {
    final LocalCache<K, V> localCache;
    private static final long serialVersionUID = 1L;
    
    LocalManualCache(CacheBuilder<? super K, ? super V> builder)
    {
      this(new LocalCache(builder, null));
    }
    
    private LocalManualCache(LocalCache<K, V> localCache)
    {
      this.localCache = localCache;
    }
    
    @Nullable
    public V getIfPresent(Object key)
    {
      return (V)this.localCache.getIfPresent(key);
    }
    
    public V get(K key, final Callable<? extends V> valueLoader)
      throws ExecutionException
    {
      Preconditions.checkNotNull(valueLoader);
      (V)this.localCache.get(key, new CacheLoader()
      {
        public V load(Object key)
          throws Exception
        {
          return (V)valueLoader.call();
        }
      });
    }
    
    public ImmutableMap<K, V> getAllPresent(Iterable<?> keys)
    {
      return this.localCache.getAllPresent(keys);
    }
    
    public void put(K key, V value)
    {
      this.localCache.put(key, value);
    }
    
    public void putAll(Map<? extends K, ? extends V> m)
    {
      this.localCache.putAll(m);
    }
    
    public void invalidate(Object key)
    {
      Preconditions.checkNotNull(key);
      this.localCache.remove(key);
    }
    
    public void invalidateAll(Iterable<?> keys)
    {
      this.localCache.invalidateAll(keys);
    }
    
    public void invalidateAll()
    {
      this.localCache.clear();
    }
    
    public long size()
    {
      return this.localCache.longSize();
    }
    
    public ConcurrentMap<K, V> asMap()
    {
      return this.localCache;
    }
    
    public CacheStats stats()
    {
      AbstractCache.SimpleStatsCounter aggregator = new AbstractCache.SimpleStatsCounter();
      aggregator.incrementBy(this.localCache.globalStatsCounter);
      for (LocalCache.Segment<K, V> segment : this.localCache.segments) {
        aggregator.incrementBy(segment.statsCounter);
      }
      return aggregator.snapshot();
    }
    
    public void cleanUp()
    {
      this.localCache.cleanUp();
    }
    
    Object writeReplace()
    {
      return new LocalCache.ManualSerializationProxy(this.localCache);
    }
  }
  
  static class LocalLoadingCache<K, V>
    extends LocalCache.LocalManualCache<K, V>
    implements LoadingCache<K, V>
  {
    private static final long serialVersionUID = 1L;
    
    LocalLoadingCache(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader)
    {
      super(null);
    }
    
    public V get(K key)
      throws ExecutionException
    {
      return (V)this.localCache.getOrLoad(key);
    }
    
    public V getUnchecked(K key)
    {
      try
      {
        return (V)get(key);
      }
      catch (ExecutionException e)
      {
        throw new UncheckedExecutionException(e.getCause());
      }
    }
    
    public ImmutableMap<K, V> getAll(Iterable<? extends K> keys)
      throws ExecutionException
    {
      return this.localCache.getAll(keys);
    }
    
    public void refresh(K key)
    {
      this.localCache.refresh(key);
    }
    
    public final V apply(K key)
    {
      return (V)getUnchecked(key);
    }
    
    Object writeReplace()
    {
      return new LocalCache.LoadingSerializationProxy(this.localCache);
    }
  }
}
