package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TCharShortIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TCharShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public abstract interface TCharShortMap
{
  public abstract char getNoEntryKey();
  
  public abstract short getNoEntryValue();
  
  public abstract short put(char paramChar, short paramShort);
  
  public abstract short putIfAbsent(char paramChar, short paramShort);
  
  public abstract void putAll(Map<? extends Character, ? extends Short> paramMap);
  
  public abstract void putAll(TCharShortMap paramTCharShortMap);
  
  public abstract short get(char paramChar);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract short remove(char paramChar);
  
  public abstract int size();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract TCharShortIterator iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TCharShortProcedure paramTCharShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TCharShortProcedure paramTCharShortProcedure);
  
  public abstract boolean increment(char paramChar);
  
  public abstract boolean adjustValue(char paramChar, short paramShort);
  
  public abstract short adjustOrPutValue(char paramChar, short paramShort1, short paramShort2);
}
