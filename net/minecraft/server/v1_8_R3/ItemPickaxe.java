package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Sets;
import java.util.Set;

public class ItemPickaxe
  extends ItemTool
{
  private static final Set<Block> c = Sets.newHashSet(new Block[] { Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB });
  
  protected ItemPickaxe(Item.EnumToolMaterial ☃)
  {
    super(2.0F, ☃, c);
  }
  
  public boolean canDestroySpecialBlock(Block ☃)
  {
    if (☃ == Blocks.OBSIDIAN) {
      return this.b.d() == 3;
    }
    if ((☃ == Blocks.DIAMOND_BLOCK) || (☃ == Blocks.DIAMOND_ORE)) {
      return this.b.d() >= 2;
    }
    if ((☃ == Blocks.EMERALD_ORE) || (☃ == Blocks.EMERALD_BLOCK)) {
      return this.b.d() >= 2;
    }
    if ((☃ == Blocks.GOLD_BLOCK) || (☃ == Blocks.GOLD_ORE)) {
      return this.b.d() >= 2;
    }
    if ((☃ == Blocks.IRON_BLOCK) || (☃ == Blocks.IRON_ORE)) {
      return this.b.d() >= 1;
    }
    if ((☃ == Blocks.LAPIS_BLOCK) || (☃ == Blocks.LAPIS_ORE)) {
      return this.b.d() >= 1;
    }
    if ((☃ == Blocks.REDSTONE_ORE) || (☃ == Blocks.LIT_REDSTONE_ORE)) {
      return this.b.d() >= 2;
    }
    if (☃.getMaterial() == Material.STONE) {
      return true;
    }
    if (☃.getMaterial() == Material.ORE) {
      return true;
    }
    if (☃.getMaterial() == Material.HEAVY) {
      return true;
    }
    return false;
  }
  
  public float getDestroySpeed(ItemStack ☃, Block ☃)
  {
    if ((☃.getMaterial() == Material.ORE) || (☃.getMaterial() == Material.HEAVY) || (☃.getMaterial() == Material.STONE)) {
      return this.a;
    }
    return super.getDestroySpeed(☃, ☃);
  }
}
