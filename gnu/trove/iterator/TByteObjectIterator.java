package gnu.trove.iterator;

public abstract interface TByteObjectIterator<V>
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract V value();
  
  public abstract V setValue(V paramV);
}
