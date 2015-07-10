package gnu.trove.iterator;

public abstract interface TLongIntIterator
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
