package net.minecraft.server.v1_8_R3;

public abstract interface IWorldBorderListener
{
  public abstract void a(WorldBorder paramWorldBorder, double paramDouble);
  
  public abstract void a(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2, long paramLong);
  
  public abstract void a(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2);
  
  public abstract void a(WorldBorder paramWorldBorder, int paramInt);
  
  public abstract void b(WorldBorder paramWorldBorder, int paramInt);
  
  public abstract void b(WorldBorder paramWorldBorder, double paramDouble);
  
  public abstract void c(WorldBorder paramWorldBorder, double paramDouble);
}
