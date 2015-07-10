package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.BlockDispenser;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.TileEntityDispenser;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.projectiles.CraftBlockProjectileSource;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

public class CraftDispenser
  extends CraftBlockState
  implements Dispenser
{
  private final CraftWorld world;
  private final TileEntityDispenser dispenser;
  
  public CraftDispenser(Block block)
  {
    super(block);
    
    this.world = ((CraftWorld)block.getWorld());
    this.dispenser = ((TileEntityDispenser)this.world.getTileEntityAt(getX(), getY(), getZ()));
  }
  
  public CraftDispenser(Material material, TileEntityDispenser te)
  {
    super(material);
    this.world = null;
    this.dispenser = te;
  }
  
  public Inventory getInventory()
  {
    return new CraftInventory(this.dispenser);
  }
  
  public BlockProjectileSource getBlockProjectileSource()
  {
    Block block = getBlock();
    if (block.getType() != Material.DISPENSER) {
      return null;
    }
    return new CraftBlockProjectileSource(this.dispenser);
  }
  
  public boolean dispense()
  {
    Block block = getBlock();
    if (block.getType() == Material.DISPENSER)
    {
      BlockDispenser dispense = (BlockDispenser)Blocks.DISPENSER;
      
      dispense.dispense(this.world.getHandle(), new BlockPosition(getX(), getY(), getZ()));
      return true;
    }
    return false;
  }
  
  public boolean update(boolean force, boolean applyPhysics)
  {
    boolean result = super.update(force, applyPhysics);
    if (result) {
      this.dispenser.update();
    }
    return result;
  }
  
  public TileEntityDispenser getTileEntity()
  {
    return this.dispenser;
  }
}
