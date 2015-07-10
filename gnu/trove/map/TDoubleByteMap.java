package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TDoubleByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public abstract interface TDoubleByteMap
{
  public abstract double getNoEntryKey();
  
  public abstract byte getNoEntryValue();
  
  public abstract byte put(double paramDouble, byte paramByte);
  
  public abstract byte putIfAbsent(double paramDouble, byte paramByte);
  
  public abstract void putAll(Map<? extends Double, ? extends Byte> paramMap);
  
  public abstract void putAll(TDoubleByteMap paramTDoubleByteMap);
  
  public abstract byte get(double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract byte remove(double paramDouble);
  
  public abstract int size();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract TDoubleByteIterator iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TDoubleByteProcedure paramTDoubleByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TDoubleByteProcedure paramTDoubleByteProcedure);
  
  public abstract boolean increment(double paramDouble);
  
  public abstract boolean adjustValue(double paramDouble, byte paramByte);
  
  public abstract byte adjustOrPutValue(double paramDouble, byte paramByte1, byte paramByte2);
}
