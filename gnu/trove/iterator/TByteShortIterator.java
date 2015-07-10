package gnu.trove.iterator;

public abstract interface TByteShortIterator
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract short value();
  
  public abstract short setValue(short paramShort);
}
