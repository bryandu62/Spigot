package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockSeaLantern
  extends Block
{
  public BlockSeaLantern(Material ☃)
  {
    super(☃);
    a(CreativeModeTab.b);
  }
  
  public int a(Random ☃)
  {
    return 2 + ☃.nextInt(2);
  }
  
  public int getDropCount(int ☃, Random ☃)
  {
    return MathHelper.clamp(a(☃) + ☃.nextInt(☃ + 1), 1, 5);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.PRISMARINE_CRYSTALS;
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return MaterialMapColor.p;
  }
  
  protected boolean I()
  {
    return true;
  }
}
