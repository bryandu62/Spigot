package net.minecraft.server.v1_8_R3;

public class BlockSlime
  extends BlockHalfTransparent
{
  public BlockSlime()
  {
    super(Material.CLAY, false, MaterialMapColor.c);
    a(CreativeModeTab.c);
    this.frictionFactor = 0.8F;
  }
  
  public void a(World ☃, BlockPosition ☃, Entity ☃, float ☃)
  {
    if (☃.isSneaking()) {
      super.a(☃, ☃, ☃, ☃);
    } else {
      ☃.e(☃, 0.0F);
    }
  }
  
  public void a(World ☃, Entity ☃)
  {
    if (☃.isSneaking()) {
      super.a(☃, ☃);
    } else if (☃.motY < 0.0D) {
      ☃.motY = (-☃.motY);
    }
  }
  
  public void a(World ☃, BlockPosition ☃, Entity ☃)
  {
    if ((Math.abs(☃.motY) < 0.1D) && (!☃.isSneaking()))
    {
      double ☃ = 0.4D + Math.abs(☃.motY) * 0.2D;
      ☃.motX *= ☃;
      ☃.motZ *= ☃;
    }
    super.a(☃, ☃, ☃);
  }
}
