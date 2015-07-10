package gnu.trove.strategy;

import java.io.Serializable;

public abstract interface HashingStrategy<T>
  extends Serializable
{
  public static final long serialVersionUID = 5674097166776615540L;
  
  public abstract int computeHashCode(T paramT);
  
  public abstract boolean equals(T paramT1, T paramT2);
}
