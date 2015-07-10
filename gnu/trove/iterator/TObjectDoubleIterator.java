package gnu.trove.iterator;

public abstract interface TObjectDoubleIterator<K>
  extends TAdvancingIterator
{
  public abstract K key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
