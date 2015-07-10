package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TObjectFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public abstract interface TObjectFloatMap<K>
{
  public abstract float getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract float get(Object paramObject);
  
  public abstract float put(K paramK, float paramFloat);
  
  public abstract float putIfAbsent(K paramK, float paramFloat);
  
  public abstract float remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends Float> paramMap);
  
  public abstract void putAll(TObjectFloatMap<? extends K> paramTObjectFloatMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Object[] keys();
  
  public abstract K[] keys(K[] paramArrayOfK);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract TObjectFloatIterator<K> iterator();
  
  public abstract boolean increment(K paramK);
  
  public abstract boolean adjustValue(K paramK, float paramFloat);
  
  public abstract float adjustOrPutValue(K paramK, float paramFloat1, float paramFloat2);
  
  public abstract boolean forEachKey(TObjectProcedure<? super K> paramTObjectProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TObjectFloatProcedure<? super K> paramTObjectFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TObjectFloatProcedure<? super K> paramTObjectFloatProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
