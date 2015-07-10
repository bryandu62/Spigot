package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

public class ItemMinecart
  extends Item
{
  private static final IDispenseBehavior a = new DispenseBehaviorItem()
  {
    private final DispenseBehaviorItem b = new DispenseBehaviorItem();
    
    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack)
    {
      EnumDirection enumdirection = BlockDispenser.b(isourceblock.f());
      World world = isourceblock.i();
      double d0 = isourceblock.getX() + enumdirection.getAdjacentX() * 1.125D;
      double d1 = Math.floor(isourceblock.getY()) + enumdirection.getAdjacentY();
      double d2 = isourceblock.getZ() + enumdirection.getAdjacentZ() * 1.125D;
      BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
      IBlockData iblockdata = world.getType(blockposition);
      BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = (iblockdata.getBlock() instanceof BlockMinecartTrackAbstract) ? (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(((BlockMinecartTrackAbstract)iblockdata.getBlock()).n()) : BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
      double d3;
      double d3;
      if (BlockMinecartTrackAbstract.d(iblockdata))
      {
        double d3;
        if (blockminecarttrackabstract_enumtrackposition.c()) {
          d3 = 0.6D;
        } else {
          d3 = 0.1D;
        }
      }
      else
      {
        if ((iblockdata.getBlock().getMaterial() != Material.AIR) || (!BlockMinecartTrackAbstract.d(world.getType(blockposition.down())))) {
          return this.b.a(isourceblock, itemstack);
        }
        IBlockData iblockdata1 = world.getType(blockposition.down());
        BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition1 = (iblockdata1.getBlock() instanceof BlockMinecartTrackAbstract) ? (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata1.get(((BlockMinecartTrackAbstract)iblockdata1.getBlock()).n()) : BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
        double d3;
        if ((enumdirection != EnumDirection.DOWN) && (blockminecarttrackabstract_enumtrackposition1.c())) {
          d3 = -0.4D;
        } else {
          d3 = -0.9D;
        }
      }
      ItemStack itemstack1 = itemstack.a(1);
      org.bukkit.block.Block block2 = world.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
      CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
      
      BlockDispenseEvent event = new BlockDispenseEvent(block2, craftItem.clone(), new Vector(d0, d1 + d3, d2));
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
      itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
      EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(world, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), ((ItemMinecart)itemstack1.getItem()).b);
      if (itemstack.hasName()) {
        entityminecartabstract.setCustomName(itemstack.getName());
      }
      world.addEntity(entityminecartabstract);
      
      return itemstack;
    }
    
    protected void a(ISourceBlock isourceblock)
    {
      isourceblock.i().triggerEffect(1000, isourceblock.getBlockPosition(), 0);
    }
  };
  private final EntityMinecartAbstract.EnumMinecartType b;
  
  public ItemMinecart(EntityMinecartAbstract.EnumMinecartType entityminecartabstract_enumminecarttype)
  {
    this.maxStackSize = 1;
    this.b = entityminecartabstract_enumminecarttype;
    a(CreativeModeTab.e);
    BlockDispenser.N.a(this, a);
  }
  
  public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2)
  {
    IBlockData iblockdata = world.getType(blockposition);
    if (BlockMinecartTrackAbstract.d(iblockdata))
    {
      if (!world.isClientSide)
      {
        BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = (iblockdata.getBlock() instanceof BlockMinecartTrackAbstract) ? (BlockMinecartTrackAbstract.EnumTrackPosition)iblockdata.get(((BlockMinecartTrackAbstract)iblockdata.getBlock()).n()) : BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
        double d0 = 0.0D;
        if (blockminecarttrackabstract_enumtrackposition.c()) {
          d0 = 0.5D;
        }
        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, blockposition, enumdirection, itemstack);
        if (event.isCancelled()) {
          return false;
        }
        EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(world, blockposition.getX() + 0.5D, blockposition.getY() + 0.0625D + d0, blockposition.getZ() + 0.5D, this.b);
        if (itemstack.hasName()) {
          entityminecartabstract.setCustomName(itemstack.getName());
        }
        world.addEntity(entityminecartabstract);
      }
      itemstack.count -= 1;
      return true;
    }
    return false;
  }
}
