package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TCharCharIterator;
import gnu.trove.procedure.TCharCharProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public abstract interface TCharCharMap
{
  public abstract char getNoEntryKey();
  
  public abstract char getNoEntryValue();
  
  public abstract char put(char paramChar1, char paramChar2);
  
  public abstract char putIfAbsent(char paramChar1, char paramChar2);
  
  public abstract void putAll(Map<? extends Character, ? extends Character> paramMap);
  
  public abstract void putAll(TCharCharMap paramTCharCharMap);
  
  public abstract char get(char paramChar);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract char remove(char paramChar);
  
  public abstract int size();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract TCharCharIterator iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TCharCharProcedure paramTCharCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TCharCharProcedure paramTCharCharProcedure);
  
  public abstract boolean increment(char paramChar);
  
  public abstract boolean adjustValue(char paramChar1, char paramChar2);
  
  public abstract char adjustOrPutValue(char paramChar1, char paramChar2, char paramChar3);
}
