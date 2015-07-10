package gnu.trove.iterator;

public abstract interface TCharShortIterator
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
