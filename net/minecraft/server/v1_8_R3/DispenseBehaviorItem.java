package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

public class DispenseBehaviorItem
  implements IDispenseBehavior
{
  public final ItemStack a(ISourceBlock isourceblock, ItemStack itemstack)
  {
    ItemStack itemstack1 = b(isourceblock, itemstack);
    
    a(isourceblock);
    a(isourceblock, BlockDispenser.b(isourceblock.f()));
    return itemstack1;
  }
  
  protected ItemStack b(ISourceBlock isourceblock, ItemStack itemstack)
  {
    EnumDirection enumdirection = BlockDispenser.b(isourceblock.f());
    BlockDispenser.a(isourceblock);
    ItemStack itemstack1 = itemstack.a(1);
    if (!a(isourceblock.i(), itemstack1, 6, enumdirection, isourceblock)) {
      itemstack.count += 1;
    }
    return itemstack;
  }
  
  public static boolean a(World world, ItemStack itemstack, int i, EnumDirection enumdirection, ISourceBlock isourceblock)
  {
    IPosition iposition = BlockDispenser.a(isourceblock);
    
    double d0 = iposition.getX();
    double d1 = iposition.getY();
    double d2 = iposition.getZ();
    if (enumdirection.k() == EnumDirection.EnumAxis.Y) {
      d1 -= 0.125D;
    } else {
      d1 -= 0.15625D;
    }
    EntityItem entityitem = new EntityItem(world, d0, d1, d2, itemstack);
    double d3 = world.random.nextDouble() * 0.1D + 0.2D;
    
    entityitem.motX = (enumdirection.getAdjacentX() * d3);
    entityitem.motY = 0.20000000298023224D;
    entityitem.motZ = (enumdirection.getAdjacentZ() * d3);
    entityitem.motX += world.random.nextGaussian() * 0.007499999832361937D * i;
    entityitem.motY += world.random.nextGaussian() * 0.007499999832361937D * i;
    entityitem.motZ += world.random.nextGaussian() * 0.007499999832361937D * i;
    
    Block block = world.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
    CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);
    
    BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new Vector(entityitem.motX, entityitem.motY, entityitem.motZ));
    if (!BlockDispenser.eventFired) {
      world.getServer().getPluginManager().callEvent(event);
    }
    if (event.isCancelled()) {
      return false;
    }
    entityitem.setItemStack(CraftItemStack.asNMSCopy(event.getItem()));
    entityitem.motX = event.getVelocity().getX();
    entityitem.motY = event.getVelocity().getY();
    entityitem.motZ = event.getVelocity().getZ();
    if (!event.getItem().getType().equals(craftItem.getType()))
    {
      ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
      IDispenseBehavior idispensebehavior = (IDispenseBehavior)BlockDispenser.N.get(eventStack.getItem());
      if ((idispensebehavior != IDispenseBehavior.a) && (idispensebehavior.getClass() != DispenseBehaviorItem.class)) {
        idispensebehavior.a(isourceblock, eventStack);
      } else {
        world.addEntity(entityitem);
      }
      return false;
    }
    world.addEntity(entityitem);
    
    return true;
  }
  
  protected void a(ISourceBlock isourceblock)
  {
    isourceblock.i().triggerEffect(1000, isourceblock.getBlockPosition(), 0);
  }
  
  protected void a(ISourceBlock isourceblock, EnumDirection enumdirection)
  {
    isourceblock.i().triggerEffect(2000, isourceblock.getBlockPosition(), a(enumdirection));
  }
  
  private int a(EnumDirection enumdirection)
  {
    return enumdirection.getAdjacentX() + 1 + (enumdirection.getAdjacentZ() + 1) * 3;
  }
}
