package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TObjectCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public abstract interface TObjectCharMap<K>
{
  public abstract char getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract char get(Object paramObject);
  
  public abstract char put(K paramK, char paramChar);
  
  public abstract char putIfAbsent(K paramK, char paramChar);
  
  public abstract char remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends Character> paramMap);
  
  public abstract void putAll(TObjectCharMap<? extends K> paramTObjectCharMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Object[] keys();
  
  public abstract K[] keys(K[] paramArrayOfK);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract TObjectCharIterator<K> iterator();
  
  public abstract boolean increment(K paramK);
  
  public abstract boolean adjustValue(K paramK, char paramChar);
  
  public abstract char adjustOrPutValue(K paramK, char paramChar1, char paramChar2);
  
  public abstract boolean forEachKey(TObjectProcedure<? super K> paramTObjectProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TObjectCharProcedure<? super K> paramTObjectCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TObjectCharProcedure<? super K> paramTObjectCharProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
