package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TByteDoubleIterator;
import gnu.trove.procedure.TByteDoubleProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public abstract interface TByteDoubleMap
{
  public abstract byte getNoEntryKey();
  
  public abstract double getNoEntryValue();
  
  public abstract double put(byte paramByte, double paramDouble);
  
  public abstract double putIfAbsent(byte paramByte, double paramDouble);
  
  public abstract void putAll(Map<? extends Byte, ? extends Double> paramMap);
  
  public abstract void putAll(TByteDoubleMap paramTByteDoubleMap);
  
  public abstract double get(byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract double remove(byte paramByte);
  
  public abstract int size();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract TByteDoubleIterator iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TByteDoubleProcedure paramTByteDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TByteDoubleProcedure paramTByteDoubleProcedure);
  
  public abstract boolean increment(byte paramByte);
  
  public abstract boolean adjustValue(byte paramByte, double paramDouble);
  
  public abstract double adjustOrPutValue(byte paramByte, double paramDouble1, double paramDouble2);
}
