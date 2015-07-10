package gnu.trove.iterator;

public abstract interface TCharObjectIterator<V>
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract V value();
  
  public abstract V setValue(V paramV);
}
