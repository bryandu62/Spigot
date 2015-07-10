package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Sets;
import java.util.Set;

public class ItemAxe
  extends ItemTool
{
  private static final Set<Block> c = Sets.newHashSet(new Block[] { Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER });
  
  protected ItemAxe(Item.EnumToolMaterial ☃)
  {
    super(3.0F, ☃, c);
  }
  
  public float getDestroySpeed(ItemStack ☃, Block ☃)
  {
    if ((☃.getMaterial() == Material.WOOD) || (☃.getMaterial() == Material.PLANT) || (☃.getMaterial() == Material.REPLACEABLE_PLANT)) {
      return this.a;
    }
    return super.getDestroySpeed(☃, ☃);
  }
}
