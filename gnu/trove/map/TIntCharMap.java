package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TIntCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public abstract interface TIntCharMap
{
  public abstract int getNoEntryKey();
  
  public abstract char getNoEntryValue();
  
  public abstract char put(int paramInt, char paramChar);
  
  public abstract char putIfAbsent(int paramInt, char paramChar);
  
  public abstract void putAll(Map<? extends Integer, ? extends Character> paramMap);
  
  public abstract void putAll(TIntCharMap paramTIntCharMap);
  
  public abstract char get(int paramInt);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract char remove(int paramInt);
  
  public abstract int size();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract TIntCharIterator iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TIntCharProcedure paramTIntCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TIntCharProcedure paramTIntCharProcedure);
  
  public abstract boolean increment(int paramInt);
  
  public abstract boolean adjustValue(int paramInt, char paramChar);
  
  public abstract char adjustOrPutValue(int paramInt, char paramChar1, char paramChar2);
}
