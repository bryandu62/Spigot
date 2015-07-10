package gnu.trove.iterator;

public abstract interface TShortByteIterator
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
