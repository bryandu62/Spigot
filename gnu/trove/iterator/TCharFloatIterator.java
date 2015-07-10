package gnu.trove.iterator;

public abstract interface TCharFloatIterator
  extends TAdvancingIterator
{
  public abstract char key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
