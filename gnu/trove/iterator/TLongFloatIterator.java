package gnu.trove.iterator;

public abstract interface TLongFloatIterator
  extends TAdvancingIterator
{
  public abstract long key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
