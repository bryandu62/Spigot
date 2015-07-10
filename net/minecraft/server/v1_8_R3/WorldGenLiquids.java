package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenLiquids
  extends WorldGenerator
{
  private Block a;
  
  public WorldGenLiquids(Block ☃)
  {
    this.a = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    if (☃.getType(☃.up()).getBlock() != Blocks.STONE) {
      return false;
    }
    if (☃.getType(☃.down()).getBlock() != Blocks.STONE) {
      return false;
    }
    if ((☃.getType(☃).getBlock().getMaterial() != Material.AIR) && (☃.getType(☃).getBlock() != Blocks.STONE)) {
      return false;
    }
    int ☃ = 0;
    if (☃.getType(☃.west()).getBlock() == Blocks.STONE) {
      ☃++;
    }
    if (☃.getType(☃.east()).getBlock() == Blocks.STONE) {
      ☃++;
    }
    if (☃.getType(☃.north()).getBlock() == Blocks.STONE) {
      ☃++;
    }
    if (☃.getType(☃.south()).getBlock() == Blocks.STONE) {
      ☃++;
    }
    int ☃ = 0;
    if (☃.isEmpty(☃.west())) {
      ☃++;
    }
    if (☃.isEmpty(☃.east())) {
      ☃++;
    }
    if (☃.isEmpty(☃.north())) {
      ☃++;
    }
    if (☃.isEmpty(☃.south())) {
      ☃++;
    }
    if ((☃ == 3) && (☃ == 1))
    {
      ☃.setTypeAndData(☃, this.a.getBlockData(), 2);
      ☃.a(this.a, ☃, ☃);
    }
    return true;
  }
}
