package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TByteByteIterator;
import gnu.trove.procedure.TByteByteProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public abstract interface TByteByteMap
{
  public abstract byte getNoEntryKey();
  
  public abstract byte getNoEntryValue();
  
  public abstract byte put(byte paramByte1, byte paramByte2);
  
  public abstract byte putIfAbsent(byte paramByte1, byte paramByte2);
  
  public abstract void putAll(Map<? extends Byte, ? extends Byte> paramMap);
  
  public abstract void putAll(TByteByteMap paramTByteByteMap);
  
  public abstract byte get(byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract byte remove(byte paramByte);
  
  public abstract int size();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract TByteByteIterator iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TByteByteProcedure paramTByteByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TByteByteProcedure paramTByteByteProcedure);
  
  public abstract boolean increment(byte paramByte);
  
  public abstract boolean adjustValue(byte paramByte1, byte paramByte2);
  
  public abstract byte adjustOrPutValue(byte paramByte1, byte paramByte2, byte paramByte3);
}
