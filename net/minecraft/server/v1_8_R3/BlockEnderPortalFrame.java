package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BlockEnderPortalFrame
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  public static final BlockStateBoolean EYE = BlockStateBoolean.of("eye");
  
  public BlockEnderPortalFrame()
  {
    super(Material.STONE, MaterialMapColor.C);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(EYE, Boolean.valueOf(false)));
  }
  
  public boolean c()
  {
    return false;
  }
  
  public void j()
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    if (((Boolean)☃.getType(☃).get(EYE)).booleanValue())
    {
      a(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    j();
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return null;
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return getBlockData().set(FACING, ☃.getDirection().opposite()).set(EYE, Boolean.valueOf(false));
  }
  
  public boolean isComplexRedstone()
  {
    return true;
  }
  
  public int l(World ☃, BlockPosition ☃)
  {
    if (((Boolean)☃.getType(☃).get(EYE)).booleanValue()) {
      return 15;
    }
    return 0;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(EYE, Boolean.valueOf((☃ & 0x4) != 0)).set(FACING, EnumDirection.fromType2(☃ & 0x3));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumDirection)☃.get(FACING)).b();
    if (((Boolean)☃.get(EYE)).booleanValue()) {
      ☃ |= 0x4;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, EYE });
  }
}
