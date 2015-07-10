package gnu.trove.iterator;

public abstract interface TFloatFloatIterator
  extends TAdvancingIterator
{
  public abstract float key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
