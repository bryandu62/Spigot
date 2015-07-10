package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public abstract interface TObjectLongMap<K>
{
  public abstract long getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract long get(Object paramObject);
  
  public abstract long put(K paramK, long paramLong);
  
  public abstract long putIfAbsent(K paramK, long paramLong);
  
  public abstract long remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends Long> paramMap);
  
  public abstract void putAll(TObjectLongMap<? extends K> paramTObjectLongMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Object[] keys();
  
  public abstract K[] keys(K[] paramArrayOfK);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract TObjectLongIterator<K> iterator();
  
  public abstract boolean increment(K paramK);
  
  public abstract boolean adjustValue(K paramK, long paramLong);
  
  public abstract long adjustOrPutValue(K paramK, long paramLong1, long paramLong2);
  
  public abstract boolean forEachKey(TObjectProcedure<? super K> paramTObjectProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TObjectLongProcedure<? super K> paramTObjectLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TObjectLongProcedure<? super K> paramTObjectLongProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
