package gnu.trove.iterator;

public abstract interface TByteLongIterator
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
