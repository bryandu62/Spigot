package gnu.trove.procedure;

public abstract interface TLongObjectProcedure<T>
{
  public abstract boolean execute(long paramLong, T paramT);
}
