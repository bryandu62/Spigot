package net.minecraft.server.v1_8_R3;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.plugin.PluginManager;

public class ItemHanging
  extends Item
{
  private final Class<? extends EntityHanging> a;
  
  public ItemHanging(Class<? extends EntityHanging> oclass)
  {
    this.a = oclass;
    a(CreativeModeTab.c);
  }
  
  public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (enumdirection == EnumDirection.DOWN) {
      return false;
    }
    if (enumdirection == EnumDirection.UP) {
      return false;
    }
    BlockPosition blockposition1 = blockposition.shift(enumdirection);
    if (!entityhuman.a(blockposition1, enumdirection, itemstack)) {
      return false;
    }
    EntityHanging entityhanging = a(world, blockposition1, enumdirection);
    if ((entityhanging != null) && (entityhanging.survives()))
    {
      if (!world.isClientSide)
      {
        Player who = entityhuman == null ? null : (Player)entityhuman.getBukkitEntity();
        Block blockClicked = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        BlockFace blockFace = CraftBlock.notchToBlockFace(enumdirection);
        
        HangingPlaceEvent event = new HangingPlaceEvent((Hanging)entityhanging.getBukkitEntity(), who, blockClicked, blockFace);
        world.getServer().getPluginManager().callEvent(event);
        
        PaintingPlaceEvent paintingEvent = null;
        if ((entityhanging instanceof EntityPainting))
        {
          paintingEvent = new PaintingPlaceEvent((Painting)entityhanging.getBukkitEntity(), who, blockClicked, blockFace);
          paintingEvent.setCancelled(event.isCancelled());
          world.getServer().getPluginManager().callEvent(paintingEvent);
        }
        if ((event.isCancelled()) || ((paintingEvent != null) && (paintingEvent.isCancelled()))) {
          return false;
        }
        world.addEntity(entityhanging);
      }
      itemstack.count -= 1;
    }
    return true;
  }
  
  private EntityHanging a(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    return this.a == EntityItemFrame.class ? new EntityItemFrame(world, blockposition, enumdirection) : this.a == EntityPainting.class ? new EntityPainting(world, blockposition, enumdirection) : null;
  }
}
