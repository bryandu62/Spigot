package net.minecraft.server.v1_8_R3;

public abstract interface IWorldAccess
{
  public abstract void a(BlockPosition paramBlockPosition);
  
  public abstract void b(BlockPosition paramBlockPosition);
  
  public abstract void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public abstract void a(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);
  
  public abstract void a(EntityHuman paramEntityHuman, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);
  
  public abstract void a(int paramInt, boolean paramBoolean, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, int... paramVarArgs);
  
  public abstract void a(Entity paramEntity);
  
  public abstract void b(Entity paramEntity);
  
  public abstract void a(String paramString, BlockPosition paramBlockPosition);
  
  public abstract void a(int paramInt1, BlockPosition paramBlockPosition, int paramInt2);
  
  public abstract void a(EntityHuman paramEntityHuman, int paramInt1, BlockPosition paramBlockPosition, int paramInt2);
  
  public abstract void b(int paramInt1, BlockPosition paramBlockPosition, int paramInt2);
}
