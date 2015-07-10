package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public abstract interface TObjectIntMap<K>
{
  public abstract int getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract int get(Object paramObject);
  
  public abstract int put(K paramK, int paramInt);
  
  public abstract int putIfAbsent(K paramK, int paramInt);
  
  public abstract int remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends Integer> paramMap);
  
  public abstract void putAll(TObjectIntMap<? extends K> paramTObjectIntMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Object[] keys();
  
  public abstract K[] keys(K[] paramArrayOfK);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract TObjectIntIterator<K> iterator();
  
  public abstract boolean increment(K paramK);
  
  public abstract boolean adjustValue(K paramK, int paramInt);
  
  public abstract int adjustOrPutValue(K paramK, int paramInt1, int paramInt2);
  
  public abstract boolean forEachKey(TObjectProcedure<? super K> paramTObjectProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TObjectIntProcedure<? super K> paramTObjectIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TObjectIntProcedure<? super K> paramTObjectIntProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
