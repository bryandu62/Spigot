package gnu.trove.queue;

import gnu.trove.TLongCollection;

public abstract interface TLongQueue
  extends TLongCollection
{
  public abstract long element();
  
  public abstract boolean offer(long paramLong);
  
  public abstract long peek();
  
  public abstract long poll();
}
