package gnu.trove.queue;

import gnu.trove.TCharCollection;

public abstract interface TCharQueue
  extends TCharCollection
{
  public abstract char element();
  
  public abstract boolean offer(char paramChar);
  
  public abstract char peek();
  
  public abstract char poll();
}
