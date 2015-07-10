package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;

public abstract class BlockPistonEvent
  extends BlockEvent
  implements Cancellable
{
  private boolean cancelled;
  private final BlockFace direction;
  
  public BlockPistonEvent(Block block, BlockFace direction)
  {
    super(block);
    this.direction = direction;
  }
  
  public boolean isCancelled()
  {
    return this.cancelled;
  }
  
  public void setCancelled(boolean cancelled)
  {
    this.cancelled = cancelled;
  }
  
  public boolean isSticky()
  {
    return (this.block.getType() == Material.PISTON_STICKY_BASE) || (this.block.getType() == Material.PISTON_MOVING_PIECE);
  }
  
  public BlockFace getDirection()
  {
    return this.direction;
  }
}
