package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TShortCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TShortCharProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public abstract interface TShortCharMap
{
  public abstract short getNoEntryKey();
  
  public abstract char getNoEntryValue();
  
  public abstract char put(short paramShort, char paramChar);
  
  public abstract char putIfAbsent(short paramShort, char paramChar);
  
  public abstract void putAll(Map<? extends Short, ? extends Character> paramMap);
  
  public abstract void putAll(TShortCharMap paramTShortCharMap);
  
  public abstract char get(short paramShort);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract char remove(short paramShort);
  
  public abstract int size();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract TShortCharIterator iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TShortCharProcedure paramTShortCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TShortCharProcedure paramTShortCharProcedure);
  
  public abstract boolean increment(short paramShort);
  
  public abstract boolean adjustValue(short paramShort, char paramChar);
  
  public abstract char adjustOrPutValue(short paramShort, char paramChar1, char paramChar2);
}
