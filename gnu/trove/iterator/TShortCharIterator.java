package gnu.trove.iterator;

public abstract interface TShortCharIterator
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
