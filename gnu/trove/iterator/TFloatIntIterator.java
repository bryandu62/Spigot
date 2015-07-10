package gnu.trove.iterator;

public abstract interface TFloatIntIterator
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
