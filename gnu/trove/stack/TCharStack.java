package gnu.trove.stack;

public abstract interface TCharStack
{
  public abstract char getNoEntryValue();
  
  public abstract void push(char paramChar);
  
  public abstract char pop();
  
  public abstract char peek();
  
  public abstract int size();
  
  public abstract void clear();
  
  public abstract char[] toArray();
  
  public abstract void toArray(char[] paramArrayOfChar);
}
