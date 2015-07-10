package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TByteIntIterator;
import gnu.trove.procedure.TByteIntProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public abstract interface TByteIntMap
{
  public abstract byte getNoEntryKey();
  
  public abstract int getNoEntryValue();
  
  public abstract int put(byte paramByte, int paramInt);
  
  public abstract int putIfAbsent(byte paramByte, int paramInt);
  
  public abstract void putAll(Map<? extends Byte, ? extends Integer> paramMap);
  
  public abstract void putAll(TByteIntMap paramTByteIntMap);
  
  public abstract int get(byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract int remove(byte paramByte);
  
  public abstract int size();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract TByteIntIterator iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TByteIntProcedure paramTByteIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TByteIntProcedure paramTByteIntProcedure);
  
  public abstract boolean increment(byte paramByte);
  
  public abstract boolean adjustValue(byte paramByte, int paramInt);
  
  public abstract int adjustOrPutValue(byte paramByte, int paramInt1, int paramInt2);
}
