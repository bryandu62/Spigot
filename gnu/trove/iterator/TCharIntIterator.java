package gnu.trove.iterator;

public abstract interface TCharIntIterator
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
