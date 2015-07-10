package gnu.trove.iterator;

public abstract interface TObjectCharIterator<K>
  extends TAdvancingIterator
{
  public abstract K key();
  
  public abstract char value();
  
  public abstract char setValue(char paramChar);
}
