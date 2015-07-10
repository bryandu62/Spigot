package gnu.trove.iterator;

public abstract interface TLongObjectIterator<V>
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract V value();
  
  public abstract V setValue(V paramV);
}
