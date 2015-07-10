package gnu.trove.iterator;

public abstract interface TShortDoubleIterator
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
