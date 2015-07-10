package gnu.trove.iterator;

public abstract interface TLongShortIterator
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
