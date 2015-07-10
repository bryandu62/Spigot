package gnu.trove.iterator;

public abstract interface TIntByteIterator
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
