package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class Step
  extends TexturedMaterial
{
  private static final List<Material> textures = new ArrayList();
  
  static
  {
    textures.add(Material.STONE);
    textures.add(Material.SANDSTONE);
    textures.add(Material.WOOD);
    textures.add(Material.COBBLESTONE);
    textures.add(Material.BRICK);
    textures.add(Material.SMOOTH_BRICK);
    textures.add(Material.NETHER_BRICK);
    textures.add(Material.QUARTZ_BLOCK);
  }
  
  public Step()
  {
    super(Material.STEP);
  }
  
  @Deprecated
  public Step(int type)
  {
    super(type);
  }
  
  public Step(Material type)
  {
    super(textures.contains(type) ? Material.STEP : type);
    if (textures.contains(type)) {
      setMaterial(type);
    }
  }
  
  @Deprecated
  public Step(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Step(Material type, byte data)
  {
    super(type, data);
  }
  
  public List<Material> getTextures()
  {
    return textures;
  }
  
  public boolean isInverted()
  {
    return (getData() & 0x8) != 0;
  }
  
  public void setInverted(boolean inv)
  {
    int dat = getData() & 0x7;
    if (inv) {
      dat |= 0x8;
    }
    setData((byte)dat);
  }
  
  @Deprecated
  protected int getTextureIndex()
  {
    return getData() & 0x7;
  }
  
  @Deprecated
  protected void setTextureIndex(int idx)
  {
    setData((byte)(getData() & 0x8 | idx));
  }
  
  public Step clone()
  {
    return (Step)super.clone();
  }
  
  public String toString()
  {
    return super.toString() + (isInverted() ? "inverted" : "");
  }
}
