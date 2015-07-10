package gnu.trove.iterator;

public abstract interface TDoubleByteIterator
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
