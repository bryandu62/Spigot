package gnu.trove.iterator;

public abstract interface TObjectFloatIterator<K>
  extends TAdvancingIterator
{
  public abstract K key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
