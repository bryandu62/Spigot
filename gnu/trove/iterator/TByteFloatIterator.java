package gnu.trove.iterator;

public abstract interface TByteFloatIterator
  extends TAdvancingIterator
{
  public abstract byte key();
  
  public abstract float value();
  
  public abstract float setValue(float paramFloat);
}
