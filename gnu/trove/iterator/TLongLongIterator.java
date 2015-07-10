package gnu.trove.iterator;

public abstract interface TLongLongIterator
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
