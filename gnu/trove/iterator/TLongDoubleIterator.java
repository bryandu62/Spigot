package gnu.trove.iterator;

public abstract interface TLongDoubleIterator
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
