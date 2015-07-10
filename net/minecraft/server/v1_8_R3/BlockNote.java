package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class BlockNote
  extends BlockContainer
{
  private static final List<String> a = Lists.newArrayList(new String[] { "harp", "bd", "snare", "hat", "bassattack" });
  
  public BlockNote()
  {
    super(Material.WOOD);
    a(CreativeModeTab.d);
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    boolean ☃ = ☃.isBlockIndirectlyPowered(☃);
    
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityNote))
    {
      TileEntityNote ☃ = (TileEntityNote)☃;
      if (☃.f != ☃)
      {
        if (☃) {
          ☃.play(☃, ☃);
        }
        ☃.f = ☃;
      }
    }
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityNote))
    {
      TileEntityNote ☃ = (TileEntityNote)☃;
      
      ☃.b();
      ☃.play(☃, ☃);
      ☃.b(StatisticList.S);
    }
    return true;
  }
  
  public void attack(World ☃, BlockPosition ☃, EntityHuman ☃)
  {
    if (☃.isClientSide) {
      return;
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityNote))
    {
      ((TileEntityNote)☃).play(☃, ☃);
      ☃.b(StatisticList.R);
    }
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityNote();
  }
  
  private String b(int ☃)
  {
    if ((☃ < 0) || (☃ >= a.size())) {
      ☃ = 0;
    }
    return (String)a.get(☃);
  }
  
  public boolean a(World ☃, BlockPosition ☃, IBlockData ☃, int ☃, int ☃)
  {
    float ☃ = (float)Math.pow(2.0D, (☃ - 12) / 12.0D);
    
    ☃.makeSound(☃.getX() + 0.5D, ☃.getY() + 0.5D, ☃.getZ() + 0.5D, "note." + b(☃), 3.0F, ☃);
    ☃.addParticle(EnumParticle.NOTE, ☃.getX() + 0.5D, ☃.getY() + 1.2D, ☃.getZ() + 0.5D, ☃ / 24.0D, 0.0D, 0.0D, new int[0]);
    return true;
  }
  
  public int b()
  {
    return 3;
  }
}
