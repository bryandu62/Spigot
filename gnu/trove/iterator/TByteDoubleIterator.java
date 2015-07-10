package gnu.trove.iterator;

public abstract interface TByteDoubleIterator
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract double value();
  
  public abstract double setValue(double paramDouble);
}
