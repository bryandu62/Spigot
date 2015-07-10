package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class BlockWoodenStep
  extends BlockStepAbstract
{
  public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.of("variant", BlockWood.EnumLogVariant.class);
  
  public BlockWoodenStep()
  {
    super(Material.WOOD);
    IBlockData ☃ = this.blockStateList.getBlockData();
    if (!l()) {
      ☃ = ☃.set(HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
    }
    j(☃.set(VARIANT, BlockWood.EnumLogVariant.OAK));
    a(CreativeModeTab.b);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((BlockWood.EnumLogVariant)☃.get(VARIANT)).c();
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Item.getItemOf(Blocks.WOODEN_SLAB);
  }
  
  public String b(int ☃)
  {
    return super.a() + "." + BlockWood.EnumLogVariant.a(☃).d();
  }
  
  public IBlockState<?> n()
  {
    return VARIANT;
  }
  
  public Object a(ItemStack ☃)
  {
    return BlockWood.EnumLogVariant.a(☃.getData() & 0x7);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    IBlockData ☃ = getBlockData().set(VARIANT, BlockWood.EnumLogVariant.a(☃ & 0x7));
    if (!l()) {
      ☃ = ☃.set(HALF, (☃ & 0x8) == 0 ? BlockStepAbstract.EnumSlabHalf.BOTTOM : BlockStepAbstract.EnumSlabHalf.TOP);
    }
    return ☃;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a();
    if ((!l()) && (☃.get(HALF) == BlockStepAbstract.EnumSlabHalf.TOP)) {
      ☃ |= 0x8;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    if (l()) {
      return new BlockStateList(this, new IBlockState[] { VARIANT });
    }
    return new BlockStateList(this, new IBlockState[] { HALF, VARIANT });
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a();
  }
}
