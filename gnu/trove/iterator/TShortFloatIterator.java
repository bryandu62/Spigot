package gnu.trove.iterator;

public abstract interface TShortFloatIterator
  extends TAdvancingIterator
{
  public abstract short key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
