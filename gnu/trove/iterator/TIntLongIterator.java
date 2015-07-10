package gnu.trove.iterator;

public abstract interface TIntLongIterator
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
