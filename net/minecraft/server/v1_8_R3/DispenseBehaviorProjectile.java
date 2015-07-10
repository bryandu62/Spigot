package net.minecraft.server.v1_8_R3;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.projectiles.CraftBlockProjectileSource;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

public abstract class DispenseBehaviorProjectile
  extends DispenseBehaviorItem
{
  public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack)
  {
    World world = isourceblock.i();
    IPosition iposition = BlockDispenser.a(isourceblock);
    EnumDirection enumdirection = BlockDispenser.b(isourceblock.f());
    IProjectile iprojectile = a(world, iposition);
    
    ItemStack itemstack1 = itemstack.a(1);
    Block block = world.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
    CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
    
    BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new Vector(enumdirection.getAdjacentX(), enumdirection.getAdjacentY() + 0.1F, enumdirection.getAdjacentZ()));
    if (!BlockDispenser.eventFired) {
      world.getServer().getPluginManager().callEvent(event);
    }
    if (event.isCancelled())
    {
      itemstack.count += 1;
      return itemstack;
    }
    if (!event.getItem().equals(craftItem))
    {
      itemstack.count += 1;
      
      ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
      IDispenseBehavior idispensebehavior = (IDispenseBehavior)BlockDispenser.N.get(eventStack.getItem());
      if ((idispensebehavior != IDispenseBehavior.a) && (idispensebehavior != this))
      {
        idispensebehavior.a(isourceblock, eventStack);
        return itemstack;
      }
    }
    iprojectile.shoot(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), b(), a());
    ((Entity)iprojectile).projectileSource = new CraftBlockProjectileSource((TileEntityDispenser)isourceblock.getTileEntity());
    
    world.addEntity((Entity)iprojectile);
    
    return itemstack;
  }
  
  protected void a(ISourceBlock isourceblock)
  {
    isourceblock.i().triggerEffect(1002, isourceblock.getBlockPosition(), 0);
  }
  
  protected abstract IProjectile a(World paramWorld, IPosition paramIPosition);
  
  protected float a()
  {
    return 6.0F;
  }
  
  protected float b()
  {
    return 1.1F;
  }
}
