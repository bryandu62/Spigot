package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public class BlockLog2
  extends BlockLogAbstract
{
  public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.a("variant", BlockWood.EnumLogVariant.class, new Predicate()
  {
    public boolean a(BlockWood.EnumLogVariant ☃)
    {
      return ☃.a() >= 4;
    }
  });
  
  public BlockLog2()
  {
    j(this.blockStateList.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.ACACIA).set(AXIS, BlockLogAbstract.EnumLogRotation.Y));
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    BlockWood.EnumLogVariant ☃ = (BlockWood.EnumLogVariant)☃.get(VARIANT);
    switch (2.b[((BlockLogAbstract.EnumLogRotation)☃.get(AXIS)).ordinal()])
    {
    case 1: 
    case 2: 
    case 3: 
    default: 
      switch (2.a[☃.ordinal()])
      {
      case 1: 
      default: 
        return MaterialMapColor.m;
      }
      return BlockWood.EnumLogVariant.DARK_OAK.c();
    }
    return ☃.c();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    IBlockData ☃ = getBlockData().set(VARIANT, BlockWood.EnumLogVariant.a((☃ & 0x3) + 4));
    switch (☃ & 0xC)
    {
    case 0: 
      ☃ = ☃.set(AXIS, BlockLogAbstract.EnumLogRotation.Y);
      break;
    case 4: 
      ☃ = ☃.set(AXIS, BlockLogAbstract.EnumLogRotation.X);
      break;
    case 8: 
      ☃ = ☃.set(AXIS, BlockLogAbstract.EnumLogRotation.Z);
      break;
    default: 
      ☃ = ☃.set(AXIS, BlockLogAbstract.EnumLogRotation.NONE);
    }
    return ☃;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a() - 4;
    switch (2.b[((BlockLogAbstract.EnumLogRotation)☃.get(AXIS)).ordinal()])
    {
    case 1: 
      ☃ |= 0x4;
      break;
    case 2: 
      ☃ |= 0x8;
      break;
    case 3: 
      ☃ |= 0xC;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT, AXIS });
  }
  
  protected ItemStack i(IBlockData ☃)
  {
    return new ItemStack(Item.getItemOf(this), 1, ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a() - 4);
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a() - 4;
  }
}
