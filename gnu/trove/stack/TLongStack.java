package gnu.trove.stack;

public abstract interface TLongStack
{
  public abstract long getNoEntryValue();
  
  public abstract void push(long paramLong);
  
  public abstract long pop();
  
  public abstract long peek();
  
  public abstract int size();
  
  public abstract void clear();
  
  public abstract long[] toArray();
  
  public abstract void toArray(long[] paramArrayOfLong);
}
