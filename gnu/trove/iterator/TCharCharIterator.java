package gnu.trove.iterator;

public abstract interface TCharCharIterator
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
