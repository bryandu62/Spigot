package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TObjectShortIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TObjectShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import java.util.Map;
import java.util.Set;

public abstract interface TObjectShortMap<K>
{
  public abstract short getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract short get(Object paramObject);
  
  public abstract short put(K paramK, short paramShort);
  
  public abstract short putIfAbsent(K paramK, short paramShort);
  
  public abstract short remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends Short> paramMap);
  
  public abstract void putAll(TObjectShortMap<? extends K> paramTObjectShortMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Object[] keys();
  
  public abstract K[] keys(K[] paramArrayOfK);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract TObjectShortIterator<K> iterator();
  
  public abstract boolean increment(K paramK);
  
  public abstract boolean adjustValue(K paramK, short paramShort);
  
  public abstract short adjustOrPutValue(K paramK, short paramShort1, short paramShort2);
  
  public abstract boolean forEachKey(TObjectProcedure<? super K> paramTObjectProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TObjectShortProcedure<? super K> paramTObjectShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TObjectShortProcedure<? super K> paramTObjectShortProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
