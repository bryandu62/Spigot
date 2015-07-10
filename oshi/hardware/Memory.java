package oshi.hardware;

public abstract interface Memory
{
  public abstract long getTotal();
  
  public abstract long getAvailable();
}
