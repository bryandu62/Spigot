package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockIce
  extends BlockHalfTransparent
{
  public BlockIce()
  {
    super(Material.ICE, false);
    this.frictionFactor = 0.98F;
    a(true);
    a(CreativeModeTab.b);
  }
  
  public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, TileEntity tileentity)
  {
    entityhuman.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
    entityhuman.applyExhaustion(0.025F);
    if ((I()) && (EnchantmentManager.hasSilkTouchEnchantment(entityhuman)))
    {
      ItemStack itemstack = i(iblockdata);
      if (itemstack != null) {
        a(world, blockposition, itemstack);
      }
    }
    else
    {
      if (world.worldProvider.n())
      {
        world.setAir(blockposition);
        return;
      }
      int i = EnchantmentManager.getBonusBlockLootEnchantmentLevel(entityhuman);
      
      b(world, blockposition, iblockdata, i);
      Material material = world.getType(blockposition.down()).getBlock().getMaterial();
      if ((material.isSolid()) || (material.isLiquid())) {
        world.setTypeUpdate(blockposition, Blocks.FLOWING_WATER.getBlockData());
      }
    }
  }
  
  public int a(Random random)
  {
    return 0;
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (world.b(EnumSkyBlock.BLOCK, blockposition) > 11 - p())
    {
      if (CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), world.worldProvider.n() ? Blocks.AIR : Blocks.WATER).isCancelled()) {
        return;
      }
      if (world.worldProvider.n())
      {
        world.setAir(blockposition);
      }
      else
      {
        b(world, blockposition, world.getType(blockposition), 0);
        world.setTypeUpdate(blockposition, Blocks.WATER.getBlockData());
      }
    }
  }
  
  public int k()
  {
    return 0;
  }
}
