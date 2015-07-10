package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockMelon
  extends Block
{
  protected BlockMelon()
  {
    super(Material.PUMPKIN, MaterialMapColor.u);
    a(CreativeModeTab.b);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.MELON;
  }
  
  public int a(Random ☃)
  {
    return 3 + ☃.nextInt(5);
  }
  
  public int getDropCount(int ☃, Random ☃)
  {
    return Math.min(9, a(☃) + ☃.nextInt(1 + ☃));
  }
}
