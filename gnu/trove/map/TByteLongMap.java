package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TByteLongIterator;
import gnu.trove.procedure.TByteLongProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public abstract interface TByteLongMap
{
  public abstract byte getNoEntryKey();
  
  public abstract long getNoEntryValue();
  
  public abstract long put(byte paramByte, long paramLong);
  
  public abstract long putIfAbsent(byte paramByte, long paramLong);
  
  public abstract void putAll(Map<? extends Byte, ? extends Long> paramMap);
  
  public abstract void putAll(TByteLongMap paramTByteLongMap);
  
  public abstract long get(byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract long remove(byte paramByte);
  
  public abstract int size();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract TByteLongIterator iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TByteLongProcedure paramTByteLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TByteLongProcedure paramTByteLongProcedure);
  
  public abstract boolean increment(byte paramByte);
  
  public abstract boolean adjustValue(byte paramByte, long paramLong);
  
  public abstract long adjustOrPutValue(byte paramByte, long paramLong1, long paramLong2);
}
