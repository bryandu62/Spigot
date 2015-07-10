package gnu.trove.iterator;

public abstract interface TFloatCharIterator
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
