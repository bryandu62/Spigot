package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class MonsterEggs
  extends TexturedMaterial
{
  private static final List<Material> textures = new ArrayList();
  
  static
  {
    textures.add(Material.STONE);
    textures.add(Material.COBBLESTONE);
    textures.add(Material.SMOOTH_BRICK);
  }
  
  public MonsterEggs()
  {
    super(Material.MONSTER_EGGS);
  }
  
  @Deprecated
  public MonsterEggs(int type)
  {
    super(type);
  }
  
  public MonsterEggs(Material type)
  {
    super(textures.contains(type) ? Material.MONSTER_EGGS : type);
    if (textures.contains(type)) {
      setMaterial(type);
    }
  }
  
  @Deprecated
  public MonsterEggs(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public MonsterEggs(Material type, byte data)
  {
    super(type, data);
  }
  
  public List<Material> getTextures()
  {
    return textures;
  }
  
  public MonsterEggs clone()
  {
    return (MonsterEggs)super.clone();
  }
}
