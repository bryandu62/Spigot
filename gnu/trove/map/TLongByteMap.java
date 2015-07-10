package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TLongByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public abstract interface TLongByteMap
{
  public abstract long getNoEntryKey();
  
  public abstract byte getNoEntryValue();
  
  public abstract byte put(long paramLong, byte paramByte);
  
  public abstract byte putIfAbsent(long paramLong, byte paramByte);
  
  public abstract void putAll(Map<? extends Long, ? extends Byte> paramMap);
  
  public abstract void putAll(TLongByteMap paramTLongByteMap);
  
  public abstract byte get(long paramLong);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract byte remove(long paramLong);
  
  public abstract int size();
  
  public abstract TLongSet keySet();
  
  public abstract long[] keys();
  
  public abstract long[] keys(long[] paramArrayOfLong);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract boolean containsKey(long paramLong);
  
  public abstract TLongByteIterator iterator();
  
  public abstract boolean forEachKey(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TLongByteProcedure paramTLongByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TLongByteProcedure paramTLongByteProcedure);
  
  public abstract boolean increment(long paramLong);
  
  public abstract boolean adjustValue(long paramLong, byte paramByte);
  
  public abstract byte adjustOrPutValue(long paramLong, byte paramByte1, byte paramByte2);
}
