package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.plugin.PluginManager;

public class ItemFishingRod
  extends Item
{
  public ItemFishingRod()
  {
    setMaxDurability(64);
    c(1);
    a(CreativeModeTab.i);
  }
  
  public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
  {
    if (entityhuman.hookedFish != null)
    {
      int i = entityhuman.hookedFish.l();
      
      itemstack.damage(i, entityhuman);
      entityhuman.bw();
    }
    else
    {
      EntityFishingHook hook = new EntityFishingHook(world, entityhuman);
      PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player)entityhuman.getBukkitEntity(), null, (Fish)hook.getBukkitEntity(), PlayerFishEvent.State.FISHING);
      world.getServer().getPluginManager().callEvent(playerFishEvent);
      if (playerFishEvent.isCancelled())
      {
        entityhuman.hookedFish = null;
        return itemstack;
      }
      world.makeSound(entityhuman, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
      if (!world.isClientSide) {
        world.addEntity(hook);
      }
      entityhuman.bw();
      entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    }
    return itemstack;
  }
  
  public boolean f_(ItemStack itemstack)
  {
    return super.f_(itemstack);
  }
  
  public int b()
  {
    return 1;
  }
}
