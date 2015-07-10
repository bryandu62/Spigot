package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockGravel
  extends BlockFalling
{
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    if (☃ > 3) {
      ☃ = 3;
    }
    if (☃.nextInt(10 - ☃ * 3) == 0) {
      return Items.FLINT;
    }
    return Item.getItemOf(this);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return MaterialMapColor.m;
  }
}
