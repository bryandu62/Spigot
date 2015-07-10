package net.minecraft.server.v1_8_R3;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.plugin.PluginManager;

public class SlotFurnaceResult
  extends Slot
{
  private EntityHuman a;
  private int b;
  
  public SlotFurnaceResult(EntityHuman entityhuman, IInventory iinventory, int i, int j, int k)
  {
    super(iinventory, i, j, k);
    this.a = entityhuman;
  }
  
  public boolean isAllowed(ItemStack itemstack)
  {
    return false;
  }
  
  public ItemStack a(int i)
  {
    if (hasItem()) {
      this.b += Math.min(i, getItem().count);
    }
    return super.a(i);
  }
  
  public void a(EntityHuman entityhuman, ItemStack itemstack)
  {
    c(itemstack);
    super.a(entityhuman, itemstack);
  }
  
  protected void a(ItemStack itemstack, int i)
  {
    this.b += i;
    c(itemstack);
  }
  
  protected void c(ItemStack itemstack)
  {
    itemstack.a(this.a.world, this.a, this.b);
    if (!this.a.world.isClientSide)
    {
      int i = this.b;
      float f = RecipesFurnace.getInstance().b(itemstack);
      if (f == 0.0F)
      {
        i = 0;
      }
      else if (f < 1.0F)
      {
        int j = MathHelper.d(i * f);
        if ((j < MathHelper.f(i * f)) && (Math.random() < i * f - j)) {
          j++;
        }
        i = j;
      }
      Player player = (Player)this.a.getBukkitEntity();
      TileEntityFurnace furnace = (TileEntityFurnace)this.inventory;
      Block block = this.a.world.getWorld().getBlockAt(furnace.position.getX(), furnace.position.getY(), furnace.position.getZ());
      
      FurnaceExtractEvent event = new FurnaceExtractEvent(player, block, CraftMagicNumbers.getMaterial(itemstack.getItem()), itemstack.count, i);
      this.a.world.getServer().getPluginManager().callEvent(event);
      
      i = event.getExpToDrop();
      while (i > 0)
      {
        int j = EntityExperienceOrb.getOrbValue(i);
        i -= j;
        this.a.world.addEntity(new EntityExperienceOrb(this.a.world, this.a.locX, this.a.locY + 0.5D, this.a.locZ + 0.5D, j));
      }
    }
    this.b = 0;
    if (itemstack.getItem() == Items.IRON_INGOT) {
      this.a.b(AchievementList.k);
    }
    if (itemstack.getItem() == Items.COOKED_FISH) {
      this.a.b(AchievementList.p);
    }
  }
}
