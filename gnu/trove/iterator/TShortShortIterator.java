package gnu.trove.iterator;

public abstract interface TShortShortIterator
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
