package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockLightStone
  extends Block
{
  public BlockLightStone(Material ☃)
  {
    super(☃);
    a(CreativeModeTab.b);
  }
  
  public int getDropCount(int ☃, Random ☃)
  {
    return MathHelper.clamp(a(☃) + ☃.nextInt(☃ + 1), 1, 4);
  }
  
  public int a(Random ☃)
  {
    return 2 + ☃.nextInt(3);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.GLOWSTONE_DUST;
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return MaterialMapColor.d;
  }
}
