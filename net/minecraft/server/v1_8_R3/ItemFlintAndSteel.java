package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;

public class ItemFlintAndSteel
  extends Item
{
  public ItemFlintAndSteel()
  {
    this.maxStackSize = 1;
    setMaxDurability(64);
    a(CreativeModeTab.i);
  }
  
  public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2)
  {
    BlockPosition clicked = blockposition;
    blockposition = blockposition.shift(enumdirection);
    if (!entityhuman.a(blockposition, enumdirection, itemstack)) {
      return false;
    }
    if (world.getType(blockposition).getBlock().getMaterial() == Material.AIR)
    {
      if (CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, entityhuman).isCancelled())
      {
        itemstack.damage(1, entityhuman);
        return false;
      }
      CraftBlockState blockState = CraftBlockState.getBlockState(world, blockposition.getX(), blockposition.getY(), blockposition.getZ());
      
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "fire.ignite", 1.0F, g.nextFloat() * 0.4F + 0.8F);
      world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
      
      BlockPlaceEvent placeEvent = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clicked.getX(), clicked.getY(), clicked.getZ());
      if ((placeEvent.isCancelled()) || (!placeEvent.canBuild()))
      {
        placeEvent.getBlockPlaced().setTypeIdAndData(0, (byte)0, false);
        return false;
      }
    }
    itemstack.damage(1, entityhuman);
    return true;
  }
}
