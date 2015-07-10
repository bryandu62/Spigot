package gnu.trove.iterator;

public abstract interface TFloatDoubleIterator
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
