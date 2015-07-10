package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest
  extends CraftBlockState
  implements Chest
{
  private final CraftWorld world;
  private final TileEntityChest chest;
  
  public CraftChest(Block block)
  {
    super(block);
    
    this.world = ((CraftWorld)block.getWorld());
    this.chest = ((TileEntityChest)this.world.getTileEntityAt(getX(), getY(), getZ()));
  }
  
  public CraftChest(Material material, TileEntityChest te)
  {
    super(material);
    this.chest = te;
    this.world = null;
  }
  
  public Inventory getBlockInventory()
  {
    return new CraftInventory(this.chest);
  }
  
  public Inventory getInventory()
  {
    int x = getX();
    int y = getY();
    int z = getZ();
    
    CraftInventory inventory = new CraftInventory(this.chest);
    if (!isPlaced()) {
      return inventory;
    }
    int id;
    if (this.world.getBlockTypeIdAt(x, y, z) == Material.CHEST.getId())
    {
      id = Material.CHEST.getId();
    }
    else
    {
      int id;
      if (this.world.getBlockTypeIdAt(x, y, z) == Material.TRAPPED_CHEST.getId()) {
        id = Material.TRAPPED_CHEST.getId();
      } else {
        throw new IllegalStateException("CraftChest is not a chest but is instead " + this.world.getBlockAt(x, y, z));
      }
    }
    int id;
    if (this.world.getBlockTypeIdAt(x - 1, y, z) == id)
    {
      CraftInventory left = new CraftInventory((TileEntityChest)this.world.getHandle().getTileEntity(new BlockPosition(x - 1, y, z)));
      inventory = new CraftInventoryDoubleChest(left, inventory);
    }
    if (this.world.getBlockTypeIdAt(x + 1, y, z) == id)
    {
      CraftInventory right = new CraftInventory((TileEntityChest)this.world.getHandle().getTileEntity(new BlockPosition(x + 1, y, z)));
      inventory = new CraftInventoryDoubleChest(inventory, right);
    }
    if (this.world.getBlockTypeIdAt(x, y, z - 1) == id)
    {
      CraftInventory left = new CraftInventory((TileEntityChest)this.world.getHandle().getTileEntity(new BlockPosition(x, y, z - 1)));
      inventory = new CraftInventoryDoubleChest(left, inventory);
    }
    if (this.world.getBlockTypeIdAt(x, y, z + 1) == id)
    {
      CraftInventory right = new CraftInventory((TileEntityChest)this.world.getHandle().getTileEntity(new BlockPosition(x, y, z + 1)));
      inventory = new CraftInventoryDoubleChest(inventory, right);
    }
    return inventory;
  }
  
  public boolean update(boolean force, boolean applyPhysics)
  {
    boolean result = super.update(force, applyPhysics);
    if (result) {
      this.chest.update();
    }
    return result;
  }
  
  public TileEntityChest getTileEntity()
  {
    return this.chest;
  }
}
