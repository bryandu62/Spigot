package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class SmoothBrick
  extends TexturedMaterial
{
  private static final List<Material> textures = new ArrayList();
  
  static
  {
    textures.add(Material.STONE);
    textures.add(Material.MOSSY_COBBLESTONE);
    textures.add(Material.COBBLESTONE);
    textures.add(Material.SMOOTH_BRICK);
  }
  
  public SmoothBrick()
  {
    super(Material.SMOOTH_BRICK);
  }
  
  @Deprecated
  public SmoothBrick(int type)
  {
    super(type);
  }
  
  public SmoothBrick(Material type)
  {
    super(textures.contains(type) ? Material.SMOOTH_BRICK : type);
    if (textures.contains(type)) {
      setMaterial(type);
    }
  }
  
  @Deprecated
  public SmoothBrick(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public SmoothBrick(Material type, byte data)
  {
    super(type, data);
  }
  
  public List<Material> getTextures()
  {
    return textures;
  }
  
  public SmoothBrick clone()
  {
    return (SmoothBrick)super.clone();
  }
}
