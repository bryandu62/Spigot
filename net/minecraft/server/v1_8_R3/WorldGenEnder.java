package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenEnder
  extends WorldGenerator
{
  private Block a;
  
  public WorldGenEnder(Block ☃)
  {
    this.a = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    if ((!☃.isEmpty(☃)) || (☃.getType(☃.down()).getBlock() != this.a)) {
      return false;
    }
    int ☃ = ☃.nextInt(32) + 6;
    int ☃ = ☃.nextInt(4) + 1;
    BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
    for (int ☃ = ☃.getX() - ☃; ☃ <= ☃.getX() + ☃; ☃++) {
      for (int ☃ = ☃.getZ() - ☃; ☃ <= ☃.getZ() + ☃; ☃++)
      {
        int ☃ = ☃ - ☃.getX();
        int ☃ = ☃ - ☃.getZ();
        if ((☃ * ☃ + ☃ * ☃ <= ☃ * ☃ + 1) && 
          (☃.getType(☃.c(☃, ☃.getY() - 1, ☃)).getBlock() != this.a)) {
          return false;
        }
      }
    }
    for (int ☃ = ☃.getY(); ☃ < ☃.getY() + ☃; ☃++)
    {
      if (☃ >= 256) {
        break;
      }
      for (int ☃ = ☃.getX() - ☃; ☃ <= ☃.getX() + ☃; ☃++) {
        for (int ☃ = ☃.getZ() - ☃; ☃ <= ☃.getZ() + ☃; ☃++)
        {
          int ☃ = ☃ - ☃.getX();
          int ☃ = ☃ - ☃.getZ();
          if (☃ * ☃ + ☃ * ☃ <= ☃ * ☃ + 1) {
            ☃.setTypeAndData(new BlockPosition(☃, ☃, ☃), Blocks.OBSIDIAN.getBlockData(), 2);
          }
        }
      }
    }
    Entity ☃ = new EntityEnderCrystal(☃);
    ☃.setPositionRotation(☃.getX() + 0.5F, ☃.getY() + ☃, ☃.getZ() + 0.5F, ☃.nextFloat() * 360.0F, 0.0F);
    ☃.addEntity(☃);
    ☃.setTypeAndData(☃.up(☃), Blocks.BEDROCK.getBlockData(), 2);
    
    return true;
  }
}
