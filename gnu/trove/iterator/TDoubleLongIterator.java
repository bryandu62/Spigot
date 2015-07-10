package gnu.trove.iterator;

public abstract interface TDoubleLongIterator
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
