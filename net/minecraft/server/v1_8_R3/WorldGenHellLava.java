package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenHellLava
  extends WorldGenerator
{
  private final Block a;
  private final boolean b;
  
  public WorldGenHellLava(Block ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    if (☃.getType(☃.up()).getBlock() != Blocks.NETHERRACK) {
      return false;
    }
    if ((☃.getType(☃).getBlock().getMaterial() != Material.AIR) && (☃.getType(☃).getBlock() != Blocks.NETHERRACK)) {
      return false;
    }
    int ☃ = 0;
    if (☃.getType(☃.west()).getBlock() == Blocks.NETHERRACK) {
      ☃++;
    }
    if (☃.getType(☃.east()).getBlock() == Blocks.NETHERRACK) {
      ☃++;
    }
    if (☃.getType(☃.north()).getBlock() == Blocks.NETHERRACK) {
      ☃++;
    }
    if (☃.getType(☃.south()).getBlock() == Blocks.NETHERRACK) {
      ☃++;
    }
    if (☃.getType(☃.down()).getBlock() == Blocks.NETHERRACK) {
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
    if (☃.isEmpty(☃.down())) {
      ☃++;
    }
    if (((!this.b) && (☃ == 4) && (☃ == 1)) || (☃ == 5))
    {
      ☃.setTypeAndData(☃, this.a.getBlockData(), 2);
      ☃.a(this.a, ☃, ☃);
    }
    return true;
  }
}
