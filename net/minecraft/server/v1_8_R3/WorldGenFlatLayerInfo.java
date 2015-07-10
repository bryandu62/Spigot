package net.minecraft.server.v1_8_R3;

public class WorldGenFlatLayerInfo
{
  private final int a;
  private IBlockData b;
  private int c = 1;
  private int d;
  
  public WorldGenFlatLayerInfo(int ☃, Block ☃)
  {
    this(3, ☃, ☃);
  }
  
  public WorldGenFlatLayerInfo(int ☃, int ☃, Block ☃)
  {
    this.a = ☃;
    this.c = ☃;
    this.b = ☃.getBlockData();
  }
  
  public WorldGenFlatLayerInfo(int ☃, int ☃, Block ☃, int ☃)
  {
    this(☃, ☃, ☃);
    this.b = ☃.fromLegacyData(☃);
  }
  
  public int b()
  {
    return this.c;
  }
  
  public IBlockData c()
  {
    return this.b;
  }
  
  private Block e()
  {
    return this.b.getBlock();
  }
  
  private int f()
  {
    return this.b.getBlock().toLegacyData(this.b);
  }
  
  public int d()
  {
    return this.d;
  }
  
  public void b(int ☃)
  {
    this.d = ☃;
  }
  
  public String toString()
  {
    String ☃;
    if (this.a >= 3)
    {
      MinecraftKey ☃ = (MinecraftKey)Block.REGISTRY.c(e());
      String ☃ = ☃ == null ? "null" : ☃.toString();
      if (this.c > 1) {
        ☃ = this.c + "*" + ☃;
      }
    }
    else
    {
      ☃ = Integer.toString(Block.getId(e()));
      if (this.c > 1) {
        ☃ = this.c + "x" + ☃;
      }
    }
    int ☃ = f();
    if (☃ > 0) {
      ☃ = ☃ + ":" + ☃;
    }
    return ☃;
  }
}
