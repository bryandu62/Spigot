package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenSand
  extends WorldGenerator
{
  private Block a;
  private int b;
  
  public WorldGenSand(Block ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    if (☃.getType(☃).getBlock().getMaterial() != Material.WATER) {
      return false;
    }
    int ☃ = ☃.nextInt(this.b - 2) + 2;
    int ☃ = 2;
    for (int ☃ = ☃.getX() - ☃; ☃ <= ☃.getX() + ☃; ☃++) {
      for (int ☃ = ☃.getZ() - ☃; ☃ <= ☃.getZ() + ☃; ☃++)
      {
        int ☃ = ☃ - ☃.getX();
        int ☃ = ☃ - ☃.getZ();
        if (☃ * ☃ + ☃ * ☃ <= ☃ * ☃) {
          for (int ☃ = ☃.getY() - ☃; ☃ <= ☃.getY() + ☃; ☃++)
          {
            BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
            Block ☃ = ☃.getType(☃).getBlock();
            if ((☃ == Blocks.DIRT) || (☃ == Blocks.GRASS)) {
              ☃.setTypeAndData(☃, this.a.getBlockData(), 2);
            }
          }
        }
      }
    }
    return true;
  }
}
