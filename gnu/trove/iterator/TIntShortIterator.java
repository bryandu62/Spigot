package gnu.trove.iterator;

public abstract interface TIntShortIterator
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
