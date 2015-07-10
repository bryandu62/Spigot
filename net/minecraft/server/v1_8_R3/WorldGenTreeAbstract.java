package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class WorldGenTreeAbstract
  extends WorldGenerator
{
  public WorldGenTreeAbstract(boolean ☃)
  {
    super(☃);
  }
  
  protected boolean a(Block ☃)
  {
    Material ☃ = ☃.getMaterial();
    return (☃ == Material.AIR) || (☃ == Material.LEAVES) || (☃ == Blocks.GRASS) || (☃ == Blocks.DIRT) || (☃ == Blocks.LOG) || (☃ == Blocks.LOG2) || (☃ == Blocks.SAPLING) || (☃ == Blocks.VINE);
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃) {}
  
  protected void a(World ☃, BlockPosition ☃)
  {
    if (☃.getType(☃).getBlock() != Blocks.DIRT) {
      a(☃, ☃, Blocks.DIRT.getBlockData());
    }
  }
}
