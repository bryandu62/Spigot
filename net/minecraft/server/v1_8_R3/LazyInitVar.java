package net.minecraft.server.v1_8_R3;

public abstract class LazyInitVar<T>
{
  private T a;
  private boolean b = false;
  
  public T c()
  {
    if (!this.b)
    {
      this.b = true;
      this.a = init();
    }
    return (T)this.a;
  }
  
  protected abstract T init();
}
