package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TLongCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public abstract interface TLongCharMap
{
  public abstract long getNoEntryKey();
  
  public abstract char getNoEntryValue();
  
  public abstract char put(long paramLong, char paramChar);
  
  public abstract char putIfAbsent(long paramLong, char paramChar);
  
  public abstract void putAll(Map<? extends Long, ? extends Character> paramMap);
  
  public abstract void putAll(TLongCharMap paramTLongCharMap);
  
  public abstract char get(long paramLong);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract char remove(long paramLong);
  
  public abstract int size();
  
  public abstract TLongSet keySet();
  
  public abstract long[] keys();
  
  public abstract long[] keys(long[] paramArrayOfLong);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract boolean containsKey(long paramLong);
  
  public abstract TLongCharIterator iterator();
  
  public abstract boolean forEachKey(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TLongCharProcedure paramTLongCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TLongCharProcedure paramTLongCharProcedure);
  
  public abstract boolean increment(long paramLong);
  
  public abstract boolean adjustValue(long paramLong, char paramChar);
  
  public abstract char adjustOrPutValue(long paramLong, char paramChar1, char paramChar2);
}
