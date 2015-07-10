package gnu.trove.iterator;

public abstract interface TDoubleShortIterator
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
