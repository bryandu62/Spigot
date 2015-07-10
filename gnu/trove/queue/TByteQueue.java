package gnu.trove.queue;

import gnu.trove.TByteCollection;

public abstract interface TByteQueue
  extends TByteCollection
{
  public abstract byte element();
  
  public abstract boolean offer(byte paramByte);
  
  public abstract byte peek();
  
  public abstract byte poll();
}
