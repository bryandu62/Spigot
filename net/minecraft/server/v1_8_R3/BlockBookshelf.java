package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockBookshelf
  extends Block
{
  public BlockBookshelf()
  {
    super(Material.WOOD);
    a(CreativeModeTab.b);
  }
  
  public int a(Random ☃)
  {
    return 3;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.BOOK;
  }
}
