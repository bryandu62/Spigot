package gnu.trove.iterator;

public abstract interface TCharLongIterator
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
