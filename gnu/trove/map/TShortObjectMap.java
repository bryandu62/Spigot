package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TShortObjectIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TShortObjectProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Collection;
import java.util.Map;

public abstract interface TShortObjectMap<V>
{
  public abstract short getNoEntryKey();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract V get(short paramShort);
  
  public abstract V put(short paramShort, V paramV);
  
  public abstract V putIfAbsent(short paramShort, V paramV);
  
  public abstract V remove(short paramShort);
  
  public abstract void putAll(Map<? extends Short, ? extends V> paramMap);
  
  public abstract void putAll(TShortObjectMap<? extends V> paramTShortObjectMap);
  
  public abstract void clear();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract Collection<V> valueCollection();
  
  public abstract Object[] values();
  
  public abstract V[] values(V[] paramArrayOfV);
  
  public abstract TShortObjectIterator<V> iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TObjectProcedure<? super V> paramTObjectProcedure);
  
  public abstract boolean forEachEntry(TShortObjectProcedure<? super V> paramTShortObjectProcedure);
  
  public abstract void transformValues(TObjectFunction<V, V> paramTObjectFunction);
  
  public abstract boolean retainEntries(TShortObjectProcedure<? super V> paramTShortObjectProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
