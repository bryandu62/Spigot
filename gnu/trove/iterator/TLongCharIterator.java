package gnu.trove.iterator;

public abstract interface TLongCharIterator
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
