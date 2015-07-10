package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockWeb
  extends Block
{
  public BlockWeb()
  {
    super(Material.WEB);
    a(CreativeModeTab.c);
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, Entity ☃)
  {
    ☃.aA();
  }
  
  public boolean c()
  {
    return false;
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return null;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.STRING;
  }
  
  protected boolean I()
  {
    return true;
  }
}
