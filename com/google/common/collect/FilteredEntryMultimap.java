package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class FilteredEntryMultimap<K, V>
  extends AbstractMultimap<K, V>
  implements FilteredMultimap<K, V>
{
  final Multimap<K, V> unfiltered;
  final Predicate<? super Map.Entry<K, V>> predicate;
  
  FilteredEntryMultimap(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate)
  {
    this.unfiltered = ((Multimap)Preconditions.checkNotNull(unfiltered));
    this.predicate = ((Predicate)Preconditions.checkNotNull(predicate));
  }
  
  public Multimap<K, V> unfiltered()
  {
    return this.unfiltered;
  }
  
  public Predicate<? super Map.Entry<K, V>> entryPredicate()
  {
    return this.predicate;
  }
  
  public int size()
  {
    return entries().size();
  }
  
  private boolean satisfies(K key, V value)
  {
    return this.predicate.apply(Maps.immutableEntry(key, value));
  }
  
  final class ValuePredicate
    implements Predicate<V>
  {
    private final K key;
    
    ValuePredicate()
    {
      this.key = key;
    }
    
    public boolean apply(@Nullable V value)
    {
      return FilteredEntryMultimap.this.satisfies(this.key, value);
    }
  }
  
  static <E> Collection<E> filterCollection(Collection<E> collection, Predicate<? super E> predicate)
  {
    if ((collection instanceof Set)) {
      return Sets.filter((Set)collection, predicate);
    }
    return Collections2.filter(collection, predicate);
  }
  
  public boolean containsKey(@Nullable Object key)
  {
    return asMap().get(key) != null;
  }
  
  public Collection<V> removeAll(@Nullable Object key)
  {
    return (Collection)Objects.firstNonNull(asMap().remove(key), unmodifiableEmptyCollection());
  }
  
  Collection<V> unmodifiableEmptyCollection()
  {
    return (this.unfiltered instanceof SetMultimap) ? Collections.emptySet() : Collections.emptyList();
  }
  
  public void clear()
  {
    entries().clear();
  }
  
  public Collection<V> get(K key)
  {
    return filterCollection(this.unfiltered.get(key), new ValuePredicate(key));
  }
  
  Collection<Map.Entry<K, V>> createEntries()
  {
    return filterCollection(this.unfiltered.entries(), this.predicate);
  }
  
  Collection<V> createValues()
  {
    return new FilteredMultimapValues(this);
  }
  
  Iterator<Map.Entry<K, V>> entryIterator()
  {
    throw new AssertionError("should never be called");
  }
  
  Map<K, Collection<V>> createAsMap()
  {
    return new AsMap();
  }
  
  public Set<K> keySet()
  {
    return asMap().keySet();
  }
  
  boolean removeEntriesIf(Predicate<? super Map.Entry<K, Collection<V>>> predicate)
  {
    Iterator<Map.Entry<K, Collection<V>>> entryIterator = this.unfiltered.asMap().entrySet().iterator();
    boolean changed = false;
    while (entryIterator.hasNext())
    {
      Map.Entry<K, Collection<V>> entry = (Map.Entry)entryIterator.next();
      K key = entry.getKey();
      Collection<V> collection = filterCollection((Collection)entry.getValue(), new ValuePredicate(key));
      if ((!collection.isEmpty()) && (predicate.apply(Maps.immutableEntry(key, collection))))
      {
        if (collection.size() == ((Collection)entry.getValue()).size()) {
          entryIterator.remove();
        } else {
          collection.clear();
        }
        changed = true;
      }
    }
    return changed;
  }
  
  class AsMap
    extends Maps.ImprovedAbstractMap<K, Collection<V>>
  {
    AsMap() {}
    
    public boolean containsKey(@Nullable Object key)
    {
      return get(key) != null;
    }
    
    public void clear()
    {
      FilteredEntryMultimap.this.clear();
    }
    
    public Collection<V> get(@Nullable Object key)
    {
      Collection<V> result = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
      if (result == null) {
        return null;
      }
      K k = (K)key;
      result = FilteredEntryMultimap.filterCollection(result, new FilteredEntryMultimap.ValuePredicate(FilteredEntryMultimap.this, k));
      return result.isEmpty() ? null : result;
    }
    
    public Collection<V> remove(@Nullable Object key)
    {
      Collection<V> collection = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
      if (collection == null) {
        return null;
      }
      K k = (K)key;
      List<V> result = Lists.newArrayList();
      Iterator<V> itr = collection.iterator();
      while (itr.hasNext())
      {
        V v = itr.next();
        if (FilteredEntryMultimap.this.satisfies(k, v))
        {
          itr.remove();
          result.add(v);
        }
      }
      if (result.isEmpty()) {
        return null;
      }
      if ((FilteredEntryMultimap.this.unfiltered instanceof SetMultimap)) {
        return Collections.unmodifiableSet(Sets.newLinkedHashSet(result));
      }
      return Collections.unmodifiableList(result);
    }
    
    Set<K> createKeySet()
    {
      new Maps.KeySet(this)
      {
        public boolean removeAll(Collection<?> c)
        {
          return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
        }
        
        public boolean retainAll(Collection<?> c)
        {
          return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
        }
        
        public boolean remove(@Nullable Object o)
        {
          return FilteredEntryMultimap.AsMap.this.remove(o) != null;
        }
      };
    }
    
    Set<Map.Entry<K, Collection<V>>> createEntrySet()
    {
      new Maps.EntrySet()
      {
        Map<K, Collection<V>> map()
        {
          return FilteredEntryMultimap.AsMap.this;
        }
        
        public Iterator<Map.Entry<K, Collection<V>>> iterator()
        {
          new AbstractIterator()
          {
            final Iterator<Map.Entry<K, Collection<V>>> backingIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
            
            protected Map.Entry<K, Collection<V>> computeNext()
            {
              while (this.backingIterator.hasNext())
              {
                Map.Entry<K, Collection<V>> entry = (Map.Entry)this.backingIterator.next();
                K key = entry.getKey();
                Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), new FilteredEntryMultimap.ValuePredicate(FilteredEntryMultimap.this, key));
                if (!collection.isEmpty()) {
                  return Maps.immutableEntry(key, collection);
                }
              }
              return (Map.Entry)endOfData();
            }
          };
        }
        
        public boolean removeAll(Collection<?> c)
        {
          return FilteredEntryMultimap.this.removeEntriesIf(Predicates.in(c));
        }
        
        public boolean retainAll(Collection<?> c)
        {
          return FilteredEntryMultimap.this.removeEntriesIf(Predicates.not(Predicates.in(c)));
        }
        
        public int size()
        {
          return Iterators.size(iterator());
        }
      };
    }
    
    Collection<Collection<V>> createValues()
    {
      new Maps.Values(this)
      {
        public boolean remove(@Nullable Object o)
        {
          if ((o instanceof Collection))
          {
            Collection<?> c = (Collection)o;
            Iterator<Map.Entry<K, Collection<V>>> entryIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
            while (entryIterator.hasNext())
            {
              Map.Entry<K, Collection<V>> entry = (Map.Entry)entryIterator.next();
              K key = entry.getKey();
              Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), new FilteredEntryMultimap.ValuePredicate(FilteredEntryMultimap.this, key));
              if ((!collection.isEmpty()) && (c.equals(collection)))
              {
                if (collection.size() == ((Collection)entry.getValue()).size()) {
                  entryIterator.remove();
                } else {
                  collection.clear();
                }
                return true;
              }
            }
          }
          return false;
        }
        
        public boolean removeAll(Collection<?> c)
        {
          return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
        }
        
        public boolean retainAll(Collection<?> c)
        {
          return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
        }
      };
    }
  }
  
  Multiset<K> createKeys()
  {
    return new Keys();
  }
  
  class Keys
    extends Multimaps.Keys<K, V>
  {
    Keys()
    {
      super();
    }
    
    public int remove(@Nullable Object key, int occurrences)
    {
      CollectPreconditions.checkNonnegative(occurrences, "occurrences");
      if (occurrences == 0) {
        return count(key);
      }
      Collection<V> collection = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
      if (collection == null) {
        return 0;
      }
      K k = (K)key;
      int oldCount = 0;
      Iterator<V> itr = collection.iterator();
      while (itr.hasNext())
      {
        V v = itr.next();
        if (FilteredEntryMultimap.this.satisfies(k, v))
        {
          oldCount++;
          if (oldCount <= occurrences) {
            itr.remove();
          }
        }
      }
      return oldCount;
    }
    
    public Set<Multiset.Entry<K>> entrySet()
    {
      new Multisets.EntrySet()
      {
        Multiset<K> multiset()
        {
          return FilteredEntryMultimap.Keys.this;
        }
        
        public Iterator<Multiset.Entry<K>> iterator()
        {
          return FilteredEntryMultimap.Keys.this.entryIterator();
        }
        
        public int size()
        {
          return FilteredEntryMultimap.this.keySet().size();
        }
        
        private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> predicate)
        {
          FilteredEntryMultimap.this.removeEntriesIf(new Predicate()
          {
            public boolean apply(Map.Entry<K, Collection<V>> entry)
            {
              return predicate.apply(Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size()));
            }
          });
        }
        
        public boolean removeAll(Collection<?> c)
        {
          return removeEntriesIf(Predicates.in(c));
        }
        
        public boolean retainAll(Collection<?> c)
        {
          return removeEntriesIf(Predicates.not(Predicates.in(c)));
        }
      };
    }
  }
}
