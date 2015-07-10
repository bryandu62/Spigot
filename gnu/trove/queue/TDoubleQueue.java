package gnu.trove.queue;

import gnu.trove.TDoubleCollection;

public abstract interface TDoubleQueue
  extends TDoubleCollection
{
  public abstract double element();
  
  public abstract boolean offer(double paramDouble);
  
  public abstract double peek();
  
  public abstract double poll();
}
