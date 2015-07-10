package gnu.trove.iterator;

public abstract interface TObjectIntIterator<K>
  extends TAdvancingIterator
{
  public abstract K key();
  
  public abstract int value();
  
  public abstract int setValue(int paramInt);
}
