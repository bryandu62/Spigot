package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenHugeMushroom
  extends WorldGenerator
{
  private Block a;
  
  public WorldGenHugeMushroom(Block ☃)
  {
    super(true);
    this.a = ☃;
  }
  
  public WorldGenHugeMushroom()
  {
    super(false);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    if (this.a == null) {
      this.a = (☃.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK);
    }
    int ☃ = ☃.nextInt(3) + 4;
    
    boolean ☃ = true;
    if ((☃.getY() < 1) || (☃.getY() + ☃ + 1 >= 256)) {
      return false;
    }
    for (int ☃ = ☃.getY(); ☃ <= ☃.getY() + 1 + ☃; ☃++)
    {
      int ☃ = 3;
      if (☃ <= ☃.getY() + 3) {
        ☃ = 0;
      }
      BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
      for (int ☃ = ☃.getX() - ☃; (☃ <= ☃.getX() + ☃) && (☃); ☃++) {
        for (int ☃ = ☃.getZ() - ☃; (☃ <= ☃.getZ() + ☃) && (☃); ☃++) {
          if ((☃ >= 0) && (☃ < 256))
          {
            Block ☃ = ☃.getType(☃.c(☃, ☃, ☃)).getBlock();
            if ((☃.getMaterial() != Material.AIR) && (☃.getMaterial() != Material.LEAVES)) {
              ☃ = false;
            }
          }
          else
          {
            ☃ = false;
          }
        }
      }
    }
    if (!☃) {
      return false;
    }
    Block ☃ = ☃.getType(☃.down()).getBlock();
    if ((☃ != Blocks.DIRT) && (☃ != Blocks.GRASS) && (☃ != Blocks.MYCELIUM)) {
      return false;
    }
    int ☃ = ☃.getY() + ☃;
    if (this.a == Blocks.RED_MUSHROOM_BLOCK) {
      ☃ = ☃.getY() + ☃ - 3;
    }
    for (int ☃ = ☃; ☃ <= ☃.getY() + ☃; ☃++)
    {
      int ☃ = 1;
      if (☃ < ☃.getY() + ☃) {
        ☃++;
      }
      if (this.a == Blocks.BROWN_MUSHROOM_BLOCK) {
        ☃ = 3;
      }
      int ☃ = ☃.getX() - ☃;
      int ☃ = ☃.getX() + ☃;
      int ☃ = ☃.getZ() - ☃;
      int ☃ = ☃.getZ() + ☃;
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++)
        {
          int ☃ = 5;
          if (☃ == ☃) {
            ☃--;
          } else if (☃ == ☃) {
            ☃++;
          }
          if (☃ == ☃) {
            ☃ -= 3;
          } else if (☃ == ☃) {
            ☃ += 3;
          }
          BlockHugeMushroom.EnumHugeMushroomVariant ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.a(☃);
          if ((this.a == Blocks.BROWN_MUSHROOM_BLOCK) || (☃ < ☃.getY() + ☃))
          {
            if (((☃ == ☃) || (☃ == ☃)) && ((☃ == ☃) || (☃ == ☃))) {
              continue;
            }
            if ((☃ == ☃.getX() - (☃ - 1)) && (☃ == ☃)) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_WEST;
            }
            if ((☃ == ☃) && (☃ == ☃.getZ() - (☃ - 1))) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_WEST;
            }
            if ((☃ == ☃.getX() + (☃ - 1)) && (☃ == ☃)) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_EAST;
            }
            if ((☃ == ☃) && (☃ == ☃.getZ() - (☃ - 1))) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_EAST;
            }
            if ((☃ == ☃.getX() - (☃ - 1)) && (☃ == ☃)) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_WEST;
            }
            if ((☃ == ☃) && (☃ == ☃.getZ() + (☃ - 1))) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_WEST;
            }
            if ((☃ == ☃.getX() + (☃ - 1)) && (☃ == ☃)) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_EAST;
            }
            if ((☃ == ☃) && (☃ == ☃.getZ() + (☃ - 1))) {
              ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_EAST;
            }
          }
          if ((☃ == BlockHugeMushroom.EnumHugeMushroomVariant.CENTER) && (☃ < ☃.getY() + ☃)) {
            ☃ = BlockHugeMushroom.EnumHugeMushroomVariant.ALL_INSIDE;
          }
          if ((☃.getY() >= ☃.getY() + ☃ - 1) || (☃ != BlockHugeMushroom.EnumHugeMushroomVariant.ALL_INSIDE))
          {
            BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
            if (!☃.getType(☃).getBlock().o()) {
              a(☃, ☃, this.a.getBlockData().set(BlockHugeMushroom.VARIANT, ☃));
            }
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      Block ☃ = ☃.getType(☃.up(☃)).getBlock();
      if (!☃.o()) {
        a(☃, ☃.up(☃), this.a.getBlockData().set(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumHugeMushroomVariant.STEM));
      }
    }
    return true;
  }
}
