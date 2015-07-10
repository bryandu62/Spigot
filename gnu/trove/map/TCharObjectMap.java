package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TCharObjectIterator;
import gnu.trove.procedure.TCharObjectProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TCharSet;
import java.util.Collection;
import java.util.Map;

public abstract interface TCharObjectMap<V>
{
  public abstract char getNoEntryKey();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract V get(char paramChar);
  
  public abstract V put(char paramChar, V paramV);
  
  public abstract V putIfAbsent(char paramChar, V paramV);
  
  public abstract V remove(char paramChar);
  
  public abstract void putAll(Map<? extends Character, ? extends V> paramMap);
  
  public abstract void putAll(TCharObjectMap<? extends V> paramTCharObjectMap);
  
  public abstract void clear();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract Collection<V> valueCollection();
  
  public abstract Object[] values();
  
  public abstract V[] values(V[] paramArrayOfV);
  
  public abstract TCharObjectIterator<V> iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TObjectProcedure<? super V> paramTObjectProcedure);
  
  public abstract boolean forEachEntry(TCharObjectProcedure<? super V> paramTCharObjectProcedure);
  
  public abstract void transformValues(TObjectFunction<V, V> paramTObjectFunction);
  
  public abstract boolean retainEntries(TCharObjectProcedure<? super V> paramTCharObjectProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
