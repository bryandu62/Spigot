package gnu.trove.iterator;

public abstract interface TIntObjectIterator<V>
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract V value();
  
  public abstract V setValue(V paramV);
}
