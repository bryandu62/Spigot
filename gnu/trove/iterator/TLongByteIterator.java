package gnu.trove.iterator;

public abstract interface TLongByteIterator
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
