package gnu.trove.iterator;

public abstract interface TByteIntIterator
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
