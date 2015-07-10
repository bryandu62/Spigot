package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockFalling
  extends Block
{
  public static boolean instaFall;
  
  public BlockFalling()
  {
    super(Material.SAND);
    a(CreativeModeTab.b);
  }
  
  public BlockFalling(Material ☃)
  {
    super(☃);
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    ☃.a(☃, this, a(☃));
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    ☃.a(☃, this, a(☃));
  }
  
  public void b(World ☃, BlockPosition ☃, IBlockData ☃, Random ☃)
  {
    if (!☃.isClientSide) {
      f(☃, ☃);
    }
  }
  
  private void f(World ☃, BlockPosition ☃)
  {
    if ((!canFall(☃, ☃.down())) || (☃.getY() < 0)) {
      return;
    }
    int ☃ = 32;
    if ((instaFall) || (!☃.areChunksLoadedBetween(☃.a(-☃, -☃, -☃), ☃.a(☃, ☃, ☃))))
    {
      ☃.setAir(☃);
      
      BlockPosition ☃ = ☃.down();
      while ((canFall(☃, ☃)) && (☃.getY() > 0)) {
        ☃ = ☃.down();
      }
      if (☃.getY() > 0) {
        ☃.setTypeUpdate(☃.up(), getBlockData());
      }
    }
    else if (!☃.isClientSide)
    {
      EntityFallingBlock ☃ = new EntityFallingBlock(☃, ☃.getX() + 0.5D, ☃.getY(), ☃.getZ() + 0.5D, ☃.getType(☃));
      a(☃);
      ☃.addEntity(☃);
    }
  }
  
  protected void a(EntityFallingBlock ☃) {}
  
  public int a(World ☃)
  {
    return 2;
  }
  
  public static boolean canFall(World ☃, BlockPosition ☃)
  {
    Block ☃ = ☃.getType(☃).getBlock();
    Material ☃ = ☃.material;
    return (☃ == Blocks.FIRE) || (☃ == Material.AIR) || (☃ == Material.WATER) || (☃ == Material.LAVA);
  }
  
  public void a_(World ☃, BlockPosition ☃) {}
}
