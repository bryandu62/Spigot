package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenPackedIce1
  extends WorldGenerator
{
  private Block a;
  private int b;
  
  public WorldGenPackedIce1(int ☃)
  {
    this.a = Blocks.PACKED_ICE;
    this.b = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    while ((☃.isEmpty(☃)) && (☃.getY() > 2)) {
      ☃ = ☃.down();
    }
    if (☃.getType(☃).getBlock() != Blocks.SNOW) {
      return false;
    }
    int ☃ = ☃.nextInt(this.b - 2) + 2;
    int ☃ = 1;
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
            if ((☃ == Blocks.DIRT) || (☃ == Blocks.SNOW) || (☃ == Blocks.ICE)) {
              ☃.setTypeAndData(☃, this.a.getBlockData(), 2);
            }
          }
        }
      }
    }
    return true;
  }
}
