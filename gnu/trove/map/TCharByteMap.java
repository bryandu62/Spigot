package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TCharByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public abstract interface TCharByteMap
{
  public abstract char getNoEntryKey();
  
  public abstract byte getNoEntryValue();
  
  public abstract byte put(char paramChar, byte paramByte);
  
  public abstract byte putIfAbsent(char paramChar, byte paramByte);
  
  public abstract void putAll(Map<? extends Character, ? extends Byte> paramMap);
  
  public abstract void putAll(TCharByteMap paramTCharByteMap);
  
  public abstract byte get(char paramChar);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract byte remove(char paramChar);
  
  public abstract int size();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract TByteCollection valueCollection();
  
  public abstract byte[] values();
  
  public abstract byte[] values(byte[] paramArrayOfByte);
  
  public abstract boolean containsValue(byte paramByte);
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract TCharByteIterator iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachEntry(TCharByteProcedure paramTCharByteProcedure);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract boolean retainEntries(TCharByteProcedure paramTCharByteProcedure);
  
  public abstract boolean increment(char paramChar);
  
  public abstract boolean adjustValue(char paramChar, byte paramByte);
  
  public abstract byte adjustOrPutValue(char paramChar, byte paramByte1, byte paramByte2);
}
