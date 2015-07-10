package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

public class ItemFireball
  extends Item
{
  public ItemFireball()
  {
    a(CreativeModeTab.f);
  }
  
  public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (world.isClientSide) {
      return true;
    }
    blockposition = blockposition.shift(enumdirection);
    if (!entityhuman.a(blockposition, enumdirection, itemstack)) {
      return false;
    }
    if (world.getType(blockposition).getBlock().getMaterial() == Material.AIR)
    {
      if (CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), BlockIgniteEvent.IgniteCause.FIREBALL, entityhuman).isCancelled())
      {
        if (!entityhuman.abilities.canInstantlyBuild) {
          itemstack.count -= 1;
        }
        return false;
      }
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "item.fireCharge.use", 1.0F, (g.nextFloat() - g.nextFloat()) * 0.2F + 1.0F);
      world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
    }
    if (!entityhuman.abilities.canInstantlyBuild) {
      itemstack.count -= 1;
    }
    return true;
  }
}
