package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockObsidian
  extends Block
{
  public BlockObsidian()
  {
    super(Material.STONE);
    a(CreativeModeTab.b);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Item.getItemOf(Blocks.OBSIDIAN);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return MaterialMapColor.E;
  }
}
