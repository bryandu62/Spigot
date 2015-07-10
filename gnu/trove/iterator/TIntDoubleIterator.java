package gnu.trove.iterator;

public abstract interface TIntDoubleIterator
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
