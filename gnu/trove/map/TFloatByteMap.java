package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TFloatByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public abstract interface TFloatByteMap
{
  public abstract float getNoEntryKey();
  
  public abstract byte getNoEntryValue();
  
  public abstract byte put(float paramFloat, byte paramByte);
  
  public abstract byte putIfAbsent(float paramFloat, byte paramByte);
  
  public abstract void putAll(Map<? extends Float, ? extends Byte> paramMap);
  
  public abstract void putAll(TFloatByteMap paramTFloatByteMap);
  
  public abstract byte get(float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract byte remove(float paramFloat);
  
  public abstract int size();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract TFloatByteIterator iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TFloatByteProcedure paramTFloatByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TFloatByteProcedure paramTFloatByteProcedure);
  
  public abstract boolean increment(float paramFloat);
  
  public abstract boolean adjustValue(float paramFloat, byte paramByte);
  
  public abstract byte adjustOrPutValue(float paramFloat, byte paramByte1, byte paramByte2);
}
