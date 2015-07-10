package org.bukkit.event.block;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockMultiPlaceEvent
  extends BlockPlaceEvent
{
  private final List<BlockState> states;
  
  public BlockMultiPlaceEvent(List<BlockState> states, Block clicked, ItemStack itemInHand, Player thePlayer, boolean canBuild)
  {
    super(((BlockState)states.get(0)).getBlock(), (BlockState)states.get(0), clicked, itemInHand, thePlayer, canBuild);
    this.states = ImmutableList.copyOf(states);
  }
  
  public List<BlockState> getReplacedBlockStates()
  {
    return this.states;
  }
}
