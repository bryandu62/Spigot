package gnu.trove.iterator;

public abstract interface TByteCharIterator
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
