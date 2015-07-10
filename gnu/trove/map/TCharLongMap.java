package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TCharLongIterator;
import gnu.trove.procedure.TCharLongProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public abstract interface TCharLongMap
{
  public abstract char getNoEntryKey();
  
  public abstract long getNoEntryValue();
  
  public abstract long put(char paramChar, long paramLong);
  
  public abstract long putIfAbsent(char paramChar, long paramLong);
  
  public abstract void putAll(Map<? extends Character, ? extends Long> paramMap);
  
  public abstract void putAll(TCharLongMap paramTCharLongMap);
  
  public abstract long get(char paramChar);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract long remove(char paramChar);
  
  public abstract int size();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract TCharLongIterator iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TCharLongProcedure paramTCharLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TCharLongProcedure paramTCharLongProcedure);
  
  public abstract boolean increment(char paramChar);
  
  public abstract boolean adjustValue(char paramChar, long paramLong);
  
  public abstract long adjustOrPutValue(char paramChar, long paramLong1, long paramLong2);
}
