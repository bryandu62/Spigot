package gnu.trove.iterator;

public abstract interface TCharByteIterator
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
