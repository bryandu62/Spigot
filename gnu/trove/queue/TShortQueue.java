package gnu.trove.queue;

import gnu.trove.TShortCollection;

public abstract interface TShortQueue
  extends TShortCollection
{
  public abstract short element();
  
  public abstract boolean offer(short paramShort);
  
  public abstract short peek();
  
  public abstract short poll();
}
