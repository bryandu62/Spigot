package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicates;
import java.util.Random;

public class WorldGenDesertWell
  extends WorldGenerator
{
  private static final BlockStatePredicate a = BlockStatePredicate.a(Blocks.SAND).a(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumSandVariant.SAND));
  private final IBlockData b = Blocks.STONE_SLAB.getBlockData().set(BlockDoubleStepAbstract.VARIANT, BlockDoubleStepAbstract.EnumStoneSlabVariant.SAND).set(BlockStepAbstract.HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
  private final IBlockData c = Blocks.SANDSTONE.getBlockData();
  private final IBlockData d = Blocks.FLOWING_WATER.getBlockData();
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    while ((☃.isEmpty(☃)) && (☃.getY() > 2)) {
      ☃ = ☃.down();
    }
    if (!a.a(☃.getType(☃))) {
      return false;
    }
    for (int ☃ = -2; ☃ <= 2; ☃++) {
      for (int ☃ = -2; ☃ <= 2; ☃++) {
        if ((☃.isEmpty(☃.a(☃, -1, ☃))) && (☃.isEmpty(☃.a(☃, -2, ☃)))) {
          return false;
        }
      }
    }
    for (int ☃ = -1; ☃ <= 0; ☃++) {
      for (int ☃ = -2; ☃ <= 2; ☃++) {
        for (int ☃ = -2; ☃ <= 2; ☃++) {
          ☃.setTypeAndData(☃.a(☃, ☃, ☃), this.c, 2);
        }
      }
    }
    ☃.setTypeAndData(☃, this.d, 2);
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
      ☃.setTypeAndData(☃.shift(☃), this.d, 2);
    }
    for (int ☃ = -2; ☃ <= 2; ☃++) {
      for (int ☃ = -2; ☃ <= 2; ☃++) {
        if ((☃ == -2) || (☃ == 2) || (☃ == -2) || (☃ == 2)) {
          ☃.setTypeAndData(☃.a(☃, 1, ☃), this.c, 2);
        }
      }
    }
    ☃.setTypeAndData(☃.a(2, 1, 0), this.b, 2);
    ☃.setTypeAndData(☃.a(-2, 1, 0), this.b, 2);
    ☃.setTypeAndData(☃.a(0, 1, 2), this.b, 2);
    ☃.setTypeAndData(☃.a(0, 1, -2), this.b, 2);
    for (int ☃ = -1; ☃ <= 1; ☃++) {
      for (int ☃ = -1; ☃ <= 1; ☃++) {
        if ((☃ == 0) && (☃ == 0)) {
          ☃.setTypeAndData(☃.a(☃, 4, ☃), this.c, 2);
        } else {
          ☃.setTypeAndData(☃.a(☃, 4, ☃), this.b, 2);
        }
      }
    }
    for (int ☃ = 1; ☃ <= 3; ☃++)
    {
      ☃.setTypeAndData(☃.a(-1, ☃, -1), this.c, 2);
      ☃.setTypeAndData(☃.a(-1, ☃, 1), this.c, 2);
      ☃.setTypeAndData(☃.a(1, ☃, -1), this.c, 2);
      ☃.setTypeAndData(☃.a(1, ☃, 1), this.c, 2);
    }
    return true;
  }
}
