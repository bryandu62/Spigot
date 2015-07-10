package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Sets;
import java.util.Set;

public class ItemSpade
  extends ItemTool
{
  private static final Set<Block> c = Sets.newHashSet(new Block[] { Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND });
  
  public ItemSpade(Item.EnumToolMaterial ☃)
  {
    super(1.0F, ☃, c);
  }
  
  public boolean canDestroySpecialBlock(Block ☃)
  {
    if (☃ == Blocks.SNOW_LAYER) {
      return true;
    }
    if (☃ == Blocks.SNOW) {
      return true;
    }
    return false;
  }
}
