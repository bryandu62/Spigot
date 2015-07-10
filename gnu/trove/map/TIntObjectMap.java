package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TIntSet;
import java.util.Collection;
import java.util.Map;

public abstract interface TIntObjectMap<V>
{
  public abstract int getNoEntryKey();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract V get(int paramInt);
  
  public abstract V put(int paramInt, V paramV);
  
  public abstract V putIfAbsent(int paramInt, V paramV);
  
  public abstract V remove(int paramInt);
  
  public abstract void putAll(Map<? extends Integer, ? extends V> paramMap);
  
  public abstract void putAll(TIntObjectMap<? extends V> paramTIntObjectMap);
  
  public abstract void clear();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract Collection<V> valueCollection();
  
  public abstract Object[] values();
  
  public abstract V[] values(V[] paramArrayOfV);
  
  public abstract TIntObjectIterator<V> iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TObjectProcedure<? super V> paramTObjectProcedure);
  
  public abstract boolean forEachEntry(TIntObjectProcedure<? super V> paramTIntObjectProcedure);
  
  public abstract void transformValues(TObjectFunction<V, V> paramTObjectFunction);
  
  public abstract boolean retainEntries(TIntObjectProcedure<? super V> paramTIntObjectProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
