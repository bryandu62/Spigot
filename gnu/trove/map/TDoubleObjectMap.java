package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.procedure.TDoubleObjectProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Collection;
import java.util.Map;

public abstract interface TDoubleObjectMap<V>
{
  public abstract double getNoEntryKey();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract V get(double paramDouble);
  
  public abstract V put(double paramDouble, V paramV);
  
  public abstract V putIfAbsent(double paramDouble, V paramV);
  
  public abstract V remove(double paramDouble);
  
  public abstract void putAll(Map<? extends Double, ? extends V> paramMap);
  
  public abstract void putAll(TDoubleObjectMap<? extends V> paramTDoubleObjectMap);
  
  public abstract void clear();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract Collection<V> valueCollection();
  
  public abstract Object[] values();
  
  public abstract V[] values(V[] paramArrayOfV);
  
  public abstract TDoubleObjectIterator<V> iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TObjectProcedure<? super V> paramTObjectProcedure);
  
  public abstract boolean forEachEntry(TDoubleObjectProcedure<? super V> paramTDoubleObjectProcedure);
  
  public abstract void transformValues(TObjectFunction<V, V> paramTObjectFunction);
  
  public abstract boolean retainEntries(TDoubleObjectProcedure<? super V> paramTDoubleObjectProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
