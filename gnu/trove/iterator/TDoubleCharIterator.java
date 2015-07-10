package gnu.trove.iterator;

public abstract interface TDoubleCharIterator
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
