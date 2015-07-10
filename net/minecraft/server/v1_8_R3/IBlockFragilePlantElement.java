package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract interface IBlockFragilePlantElement
{
  public abstract boolean a(World paramWorld, BlockPosition paramBlockPosition, IBlockData paramIBlockData, boolean paramBoolean);
  
  public abstract boolean a(World paramWorld, Random paramRandom, BlockPosition paramBlockPosition, IBlockData paramIBlockData);
  
  public abstract void b(World paramWorld, Random paramRandom, BlockPosition paramBlockPosition, IBlockData paramIBlockData);
}
