package net.minecraft.server.v1_8_R3;

public class MaterialPortal
  extends Material
{
  public MaterialPortal(MaterialMapColor ☃)
  {
    super(☃);
  }
  
  public boolean isBuildable()
  {
    return false;
  }
  
  public boolean blocksLight()
  {
    return false;
  }
  
  public boolean isSolid()
  {
    return false;
  }
}
