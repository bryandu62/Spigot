package net.minecraft.server.v1_8_R3;

public class MaterialLiquid
  extends Material
{
  public MaterialLiquid(MaterialMapColor ☃)
  {
    super(☃);
    i();
    n();
  }
  
  public boolean isLiquid()
  {
    return true;
  }
  
  public boolean isSolid()
  {
    return false;
  }
  
  public boolean isBuildable()
  {
    return false;
  }
}
