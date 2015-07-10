package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.PluginManager;

public class BlockDropper
  extends BlockDispenser
{
  private final IDispenseBehavior P = new DispenseBehaviorItem();
  
  protected IDispenseBehavior a(ItemStack itemstack)
  {
    return this.P;
  }
  
  public TileEntity a(World world, int i)
  {
    return new TileEntityDropper();
  }
  
  public void dispense(World world, BlockPosition blockposition)
  {
    SourceBlock sourceblock = new SourceBlock(world, blockposition);
    TileEntityDispenser tileentitydispenser = (TileEntityDispenser)sourceblock.getTileEntity();
    if (tileentitydispenser != null)
    {
      int i = tileentitydispenser.m();
      if (i < 0)
      {
        world.triggerEffect(1001, blockposition, 0);
      }
      else
      {
        ItemStack itemstack = tileentitydispenser.getItem(i);
        if (itemstack != null)
        {
          EnumDirection enumdirection = (EnumDirection)world.getType(blockposition).get(FACING);
          BlockPosition blockposition1 = blockposition.shift(enumdirection);
          IInventory iinventory = TileEntityHopper.b(world, blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
          ItemStack itemstack1;
          if (iinventory == null)
          {
            ItemStack itemstack1 = this.P.a(sourceblock, itemstack);
            if ((itemstack1 != null) && (itemstack1.count <= 0)) {
              itemstack1 = null;
            }
          }
          else
          {
            CraftItemStack oitemstack = CraftItemStack.asCraftMirror(itemstack.cloneItemStack().a(1));
            Inventory destinationInventory;
            Inventory destinationInventory;
            if ((iinventory instanceof InventoryLargeChest)) {
              destinationInventory = new CraftInventoryDoubleChest((InventoryLargeChest)iinventory);
            } else {
              destinationInventory = iinventory.getOwner().getInventory();
            }
            InventoryMoveItemEvent event = new InventoryMoveItemEvent(tileentitydispenser.getOwner().getInventory(), oitemstack.clone(), destinationInventory, true);
            world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
              return;
            }
            itemstack1 = TileEntityHopper.addItem(iinventory, CraftItemStack.asNMSCopy(event.getItem()), enumdirection.opposite());
            if ((event.getItem().equals(oitemstack)) && (itemstack1 == null))
            {
              itemstack1 = itemstack.cloneItemStack();
              if (--itemstack1.count <= 0) {
                itemstack1 = null;
              }
            }
            else
            {
              itemstack1 = itemstack.cloneItemStack();
            }
          }
          tileentitydispenser.setItem(i, itemstack1);
        }
      }
    }
  }
}
