package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TByteShortIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TByteShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public abstract interface TByteShortMap
{
  public abstract byte getNoEntryKey();
  
  public abstract short getNoEntryValue();
  
  public abstract short put(byte paramByte, short paramShort);
  
  public abstract short putIfAbsent(byte paramByte, short paramShort);
  
  public abstract void putAll(Map<? extends Byte, ? extends Short> paramMap);
  
  public abstract void putAll(TByteShortMap paramTByteShortMap);
  
  public abstract short get(byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract short remove(byte paramByte);
  
  public abstract int size();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract TByteShortIterator iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TByteShortProcedure paramTByteShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TByteShortProcedure paramTByteShortProcedure);
  
  public abstract boolean increment(byte paramByte);
  
  public abstract boolean adjustValue(byte paramByte, short paramShort);
  
  public abstract short adjustOrPutValue(byte paramByte, short paramShort1, short paramShort2);
}
