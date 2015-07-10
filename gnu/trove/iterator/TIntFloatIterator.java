package gnu.trove.iterator;

public abstract interface TIntFloatIterator
  extends TAdvancingIterator
{
  public abstract int key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
