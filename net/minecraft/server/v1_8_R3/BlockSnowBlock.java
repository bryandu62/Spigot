package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockSnowBlock
  extends Block
{
  protected BlockSnowBlock()
  {
    super(Material.SNOW_BLOCK);
    a(true);
    a(CreativeModeTab.b);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.SNOWBALL;
  }
  
  public int a(Random ☃)
  {
    return 4;
  }
  
  public void b(World ☃, BlockPosition ☃, IBlockData ☃, Random ☃)
  {
    if (☃.b(EnumSkyBlock.BLOCK, ☃) > 11)
    {
      b(☃, ☃, ☃.getType(☃), 0);
      ☃.setAir(☃);
    }
  }
}
