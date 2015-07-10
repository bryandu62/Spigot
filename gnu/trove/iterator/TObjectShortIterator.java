package gnu.trove.iterator;

public abstract interface TObjectShortIterator<K>
  extends TAdvancingIterator
{
  public abstract K key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
