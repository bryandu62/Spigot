package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TByteFloatIterator;
import gnu.trove.procedure.TByteFloatProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public abstract interface TByteFloatMap
{
  public abstract byte getNoEntryKey();
  
  public abstract float getNoEntryValue();
  
  public abstract float put(byte paramByte, float paramFloat);
  
  public abstract float putIfAbsent(byte paramByte, float paramFloat);
  
  public abstract void putAll(Map<? extends Byte, ? extends Float> paramMap);
  
  public abstract void putAll(TByteFloatMap paramTByteFloatMap);
  
  public abstract float get(byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract float remove(byte paramByte);
  
  public abstract int size();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract TByteFloatIterator iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TByteFloatProcedure paramTByteFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TByteFloatProcedure paramTByteFloatProcedure);
  
  public abstract boolean increment(byte paramByte);
  
  public abstract boolean adjustValue(byte paramByte, float paramFloat);
  
  public abstract float adjustOrPutValue(byte paramByte, float paramFloat1, float paramFloat2);
}
