package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TByteCharIterator;
import gnu.trove.procedure.TByteCharProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public abstract interface TByteCharMap
{
  public abstract byte getNoEntryKey();
  
  public abstract char getNoEntryValue();
  
  public abstract char put(byte paramByte, char paramChar);
  
  public abstract char putIfAbsent(byte paramByte, char paramChar);
  
  public abstract void putAll(Map<? extends Byte, ? extends Character> paramMap);
  
  public abstract void putAll(TByteCharMap paramTByteCharMap);
  
  public abstract char get(byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract char remove(byte paramByte);
  
  public abstract int size();
  
  public abstract TByteSet keySet();
  
  public abstract byte[] keys();
  
  public abstract byte[] keys(byte[] paramArrayOfByte);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract boolean containsKey(byte paramByte);
  
  public abstract TByteCharIterator iterator();
  
  public abstract boolean forEachKey(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TByteCharProcedure paramTByteCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TByteCharProcedure paramTByteCharProcedure);
  
  public abstract boolean increment(byte paramByte);
  
  public abstract boolean adjustValue(byte paramByte, char paramChar);
  
  public abstract char adjustOrPutValue(byte paramByte, char paramChar1, char paramChar2);
}
