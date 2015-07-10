package gnu.trove.stack;

public abstract interface TByteStack
{
  public abstract byte getNoEntryValue();
  
  public abstract void push(byte paramByte);
  
  public abstract byte pop();
  
  public abstract byte peek();
  
  public abstract int size();
  
  public abstract void clear();
  
  public abstract byte[] toArray();
  
  public abstract void toArray(byte[] paramArrayOfByte);
}
