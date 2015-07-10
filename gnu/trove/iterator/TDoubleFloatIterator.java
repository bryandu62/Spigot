package gnu.trove.iterator;

public abstract interface TDoubleFloatIterator
  extends TAdvancingIterator
{
  public abstract double key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
