package gnu.trove.iterator;

public abstract interface TObjectByteIterator<K>
  extends TAdvancingIterator
{
  public abstract K key();
  
  public abstract byte value();
  
  public abstract byte setValue(byte paramByte);
}
