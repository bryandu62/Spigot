package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TByteObjectIterator;
import gnu.trove.procedure.TByteObjectProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TByteSet;
import java.util.Collection;
import java.util.Map;

public abstract interface TByteObjectMap<V>
{
  public abstract byte getNoEntryKey();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract V get(byte paramByte);
  
  public abstract V put(byte paramByte, V paramV);
  
  public abstract V putIfAbsent(byte paramByte, V paramV);
  
  public abstract V remove(byte paramByte);
  
  public abstract void putAll(Map<? extends Byte, ? extends V> paramMap);
  
  public abstract void putAll(TByteObjectMap<? extends V> paramTByteObjectMap);
  
  public abstract void clear();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract Collection<V> valueCollection();
  
  public abstract Object[] values();
  
  public abstract V[] values(V[] paramArrayOfV);
  
  public abstract TByteObjectIterator<V> iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TObjectProcedure<? super V> paramTObjectProcedure);
  
  public abstract boolean forEachEntry(TByteObjectProcedure<? super V> paramTByteObjectProcedure);
  
  public abstract void transformValues(TObjectFunction<V, V> paramTObjectFunction);
  
  public abstract boolean retainEntries(TByteObjectProcedure<? super V> paramTByteObjectProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
