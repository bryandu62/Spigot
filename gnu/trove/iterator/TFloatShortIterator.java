package gnu.trove.iterator;

public abstract interface TFloatShortIterator
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
