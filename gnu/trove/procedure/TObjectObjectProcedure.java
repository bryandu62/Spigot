package gnu.trove.procedure;

public abstract interface TObjectObjectProcedure<K, V>
{
  public abstract boolean execute(K paramK, V paramV);
}
