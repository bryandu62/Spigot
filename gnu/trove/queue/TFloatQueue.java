package gnu.trove.queue;

import gnu.trove.TFloatCollection;

public abstract interface TFloatQueue
  extends TFloatCollection
{
  public abstract float element();
  
  public abstract boolean offer(float paramFloat);
  
  public abstract float peek();
  
  public abstract float poll();
}
