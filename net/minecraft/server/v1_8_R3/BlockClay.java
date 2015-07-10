package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockClay
  extends Block
{
  public BlockClay()
  {
    super(Material.CLAY);
    a(CreativeModeTab.b);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.CLAY_BALL;
  }
  
  public int a(Random ☃)
  {
    return 4;
  }
}
