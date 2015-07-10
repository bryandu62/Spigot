package gnu.trove.queue;

import gnu.trove.TIntCollection;

public abstract interface TIntQueue
  extends TIntCollection
{
  public abstract int element();
  
  public abstract boolean offer(int paramInt);
  
  public abstract int peek();
  
  public abstract int poll();
}
