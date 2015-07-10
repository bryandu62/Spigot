package gnu.trove.iterator;

public abstract interface TShortIntIterator
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
