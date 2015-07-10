package gnu.trove.iterator;

public abstract interface TFloatByteIterator
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
