package gnu.trove.iterator;

public abstract interface TFloatObjectIterator<V>
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract V value();
  
  public abstract V setValue(V paramV);
}
