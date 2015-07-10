package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TShortByteProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public abstract interface TShortByteMap
{
  public abstract short getNoEntryKey();
  
  public abstract byte getNoEntryValue();
  
  public abstract byte put(short paramShort, byte paramByte);
  
  public abstract byte putIfAbsent(short paramShort, byte paramByte);
  
  public abstract void putAll(Map<? extends Short, ? extends Byte> paramMap);
  
  public abstract void putAll(TShortByteMap paramTShortByteMap);
  
  public abstract byte get(short paramShort);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract byte remove(short paramShort);
  
  public abstract int size();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract TShortByteIterator iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TShortByteProcedure paramTShortByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TShortByteProcedure paramTShortByteProcedure);
  
  public abstract boolean increment(short paramShort);
  
  public abstract boolean adjustValue(short paramShort, byte paramByte);
  
  public abstract byte adjustOrPutValue(short paramShort, byte paramByte1, byte paramByte2);
}
