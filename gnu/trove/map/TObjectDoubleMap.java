package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public abstract interface TObjectDoubleMap<K>
{
  public abstract double getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract double get(Object paramObject);
  
  public abstract double put(K paramK, double paramDouble);
  
  public abstract double putIfAbsent(K paramK, double paramDouble);
  
  public abstract double remove(Object paramObject);
  
  public abstract void putAll(Map<? extends K, ? extends Double> paramMap);
  
  public abstract void putAll(TObjectDoubleMap<? extends K> paramTObjectDoubleMap);
  
  public abstract void clear();
  
  public abstract Set<K> keySet();
  
  public abstract Object[] keys();
  
  public abstract K[] keys(K[] paramArrayOfK);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract TObjectDoubleIterator<K> iterator();
  
  public abstract boolean increment(K paramK);
  
  public abstract boolean adjustValue(K paramK, double paramDouble);
  
  public abstract double adjustOrPutValue(K paramK, double paramDouble1, double paramDouble2);
  
  public abstract boolean forEachKey(TObjectProcedure<? super K> paramTObjectProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TObjectDoubleProcedure<? super K> paramTObjectDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TObjectDoubleProcedure<? super K> paramTObjectDoubleProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
