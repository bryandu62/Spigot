package net.minecraft.server.v1_8_R3;

import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.server.MapInitializeEvent;

public class ItemMapEmpty
  extends ItemWorldMapBase
{
  protected ItemMapEmpty()
  {
    a(CreativeModeTab.f);
  }
  
  public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
  {
    World worldMain = (World)world.getServer().getServer().worlds.get(0);
    ItemStack itemstack1 = new ItemStack(Items.FILLED_MAP, 1, worldMain.b("map"));
    String s = "map_" + itemstack1.getData();
    WorldMap worldmap = new WorldMap(s);
    
    worldMain.a(s, worldmap);
    worldmap.scale = 0;
    worldmap.a(entityhuman.locX, entityhuman.locZ, worldmap.scale);
    worldmap.map = ((byte)((WorldServer)world).dimension);
    worldmap.c();
    
    CraftEventFactory.callEvent(new MapInitializeEvent(worldmap.mapView));
    
    itemstack.count -= 1;
    if (itemstack.count <= 0) {
      return itemstack1;
    }
    if (!entityhuman.inventory.pickup(itemstack1.cloneItemStack())) {
      entityhuman.drop(itemstack1, false);
    }
    entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    return itemstack;
  }
}
