package gnu.trove.iterator;

public abstract interface TObjectLongIterator<K>
  extends TAdvancingIterator
{
  public abstract K key();
  
  public abstract long value();
  
  public abstract long setValue(long paramLong);
}
