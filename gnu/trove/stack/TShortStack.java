package gnu.trove.stack;

public abstract interface TShortStack
{
  public abstract short getNoEntryValue();
  
  public abstract void push(short paramShort);
  
  public abstract short pop();
  
  public abstract short peek();
  
  public abstract int size();
  
  public abstract void clear();
  
  public abstract short[] toArray();
  
  public abstract void toArray(short[] paramArrayOfShort);
}
