package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TCharIntIterator;
import gnu.trove.procedure.TCharIntProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public abstract interface TCharIntMap
{
  public abstract char getNoEntryKey();
  
  public abstract int getNoEntryValue();
  
  public abstract int put(char paramChar, int paramInt);
  
  public abstract int putIfAbsent(char paramChar, int paramInt);
  
  public abstract void putAll(Map<? extends Character, ? extends Integer> paramMap);
  
  public abstract void putAll(TCharIntMap paramTCharIntMap);
  
  public abstract int get(char paramChar);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract int remove(char paramChar);
  
  public abstract int size();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract TCharIntIterator iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TCharIntProcedure paramTCharIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TCharIntProcedure paramTCharIntProcedure);
  
  public abstract boolean increment(char paramChar);
  
  public abstract boolean adjustValue(char paramChar, int paramInt);
  
  public abstract int adjustOrPutValue(char paramChar, int paramInt1, int paramInt2);
}
