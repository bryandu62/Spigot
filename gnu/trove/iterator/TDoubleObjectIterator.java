package gnu.trove.iterator;

public abstract interface TDoubleObjectIterator<V>
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract V value();
  
  public abstract V setValue(V paramV);
}
