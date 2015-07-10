package gnu.trove.iterator;

public abstract interface TIntIntIterator
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
