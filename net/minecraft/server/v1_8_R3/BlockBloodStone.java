package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockBloodStone
  extends Block
{
  public BlockBloodStone()
  {
    super(Material.STONE);
    a(CreativeModeTab.b);
  }
  
  public MaterialMapColor g(IBlockData iblockdata)
  {
    return MaterialMapColor.K;
  }
  
  public void doPhysics(World world, BlockPosition position, IBlockData iblockdata, Block block)
  {
    if ((block != null) && (block.isPowerSource()))
    {
      org.bukkit.block.Block bl = world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
      int power = bl.getBlockPower();
      
      BlockRedstoneEvent event = new BlockRedstoneEvent(bl, power, power);
      world.getServer().getPluginManager().callEvent(event);
    }
  }
}
