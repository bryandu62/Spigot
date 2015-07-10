package gnu.trove.iterator;

public abstract interface TShortObjectIterator<V>
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract V value();
  
  public abstract V setValue(V paramV);
}
