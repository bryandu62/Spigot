package gnu.trove.iterator;

public abstract interface TDoubleIntIterator
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
