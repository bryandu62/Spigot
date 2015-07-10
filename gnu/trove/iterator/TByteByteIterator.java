package gnu.trove.iterator;

public abstract interface TByteByteIterator
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
