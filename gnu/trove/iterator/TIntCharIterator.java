package gnu.trove.iterator;

public abstract interface TIntCharIterator
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
