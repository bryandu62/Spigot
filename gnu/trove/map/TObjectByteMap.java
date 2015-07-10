package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public abstract interface TObjectByteMap<K>
{
  public abstract byte getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract byte get(Object paramObject);
  
  public abstract byte put(K paramK, byte paramByte);
  
  public abstract byte putIfAbsent(K paramK, byte paramByte);
  
  public abstract byte remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends Byte> paramMap);
  
  public abstract void putAll(TObjectByteMap<? extends K> paramTObjectByteMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Object[] keys();
  
  public abstract K[] keys(K[] paramArrayOfK);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract TObjectByteIterator<K> iterator();
  
  public abstract boolean increment(K paramK);
  
  public abstract boolean adjustValue(K paramK, byte paramByte);
  
  public abstract byte adjustOrPutValue(K paramK, byte paramByte1, byte paramByte2);
  
  public abstract boolean forEachKey(TObjectProcedure<? super K> paramTObjectProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TObjectByteProcedure<? super K> paramTObjectByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TObjectByteProcedure<? super K> paramTObjectByteProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
