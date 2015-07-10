package gnu.trove.iterator;

public abstract interface TShortLongIterator
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
