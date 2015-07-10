package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TFloatObjectIterator;
import gnu.trove.procedure.TFloatObjectProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Collection;
import java.util.Map;

public abstract interface TFloatObjectMap<V>
{
  public abstract float getNoEntryKey();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract V get(float paramFloat);
  
  public abstract V put(float paramFloat, V paramV);
  
  public abstract V putIfAbsent(float paramFloat, V paramV);
  
  public abstract V remove(float paramFloat);
  
  public abstract void putAll(Map<? extends Float, ? extends V> paramMap);
  
  public abstract void putAll(TFloatObjectMap<? extends V> paramTFloatObjectMap);
  
  public abstract void clear();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract Collection<V> valueCollection();
  
  public abstract Object[] values();
  
  public abstract V[] values(V[] paramArrayOfV);
  
  public abstract TFloatObjectIterator<V> iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TObjectProcedure<? super V> paramTObjectProcedure);
  
  public abstract boolean forEachEntry(TFloatObjectProcedure<? super V> paramTFloatObjectProcedure);
  
  public abstract void transformValues(TObjectFunction<V, V> paramTObjectFunction);
  
  public abstract boolean retainEntries(TFloatObjectProcedure<? super V> paramTFloatObjectProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
