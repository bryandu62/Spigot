package gnu.trove.iterator;

public abstract interface TFloatLongIterator
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
