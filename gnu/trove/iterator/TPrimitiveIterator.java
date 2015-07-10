package gnu.trove.iterator;

public abstract interface TPrimitiveIterator
  extends TIterator
{
  public abstract boolean hasNext();
  
  public abstract void remove();
}
