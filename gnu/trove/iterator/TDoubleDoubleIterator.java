package gnu.trove.iterator;

public abstract interface TDoubleDoubleIterator
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
