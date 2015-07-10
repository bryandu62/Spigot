package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockPotatoes
  extends BlockCrops
{
  protected Item l()
  {
    return Items.POTATO;
  }
  
  protected Item n()
  {
    return Items.POTATO;
  }
  
  public void dropNaturally(World ☃, BlockPosition ☃, IBlockData ☃, float ☃, int ☃)
  {
    super.dropNaturally(☃, ☃, ☃, ☃, ☃);
    if (☃.isClientSide) {
      return;
    }
    if ((((Integer)☃.get(AGE)).intValue() >= 7) && (☃.random.nextInt(50) == 0)) {
      a(☃, ☃, new ItemStack(Items.POISONOUS_POTATO));
    }
  }
}
