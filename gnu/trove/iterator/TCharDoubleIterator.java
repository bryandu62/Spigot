package gnu.trove.iterator;

public abstract interface TCharDoubleIterator
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
