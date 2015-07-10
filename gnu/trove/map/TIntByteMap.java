package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TIntByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public abstract interface TIntByteMap
{
  public abstract int getNoEntryKey();
  
  public abstract byte getNoEntryValue();
  
  public abstract byte put(int paramInt, byte paramByte);
  
  public abstract byte putIfAbsent(int paramInt, byte paramByte);
  
  public abstract void putAll(Map<? extends Integer, ? extends Byte> paramMap);
  
  public abstract void putAll(TIntByteMap paramTIntByteMap);
  
  public abstract byte get(int paramInt);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract byte remove(int paramInt);
  
  public abstract int size();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract TIntByteIterator iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TIntByteProcedure paramTIntByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TIntByteProcedure paramTIntByteProcedure);
  
  public abstract boolean increment(int paramInt);
  
  public abstract boolean adjustValue(int paramInt, byte paramByte);
  
  public abstract byte adjustOrPutValue(int paramInt, byte paramByte1, byte paramByte2);
}
