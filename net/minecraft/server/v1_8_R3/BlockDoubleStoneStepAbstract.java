package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class BlockDoubleStoneStepAbstract
  extends BlockStepAbstract
{
  public static final BlockStateBoolean SEAMLESS = BlockStateBoolean.of("seamless");
  public static final BlockStateEnum<EnumStoneSlab2Variant> VARIANT = BlockStateEnum.of("variant", EnumStoneSlab2Variant.class);
  
  public BlockDoubleStoneStepAbstract()
  {
    super(Material.STONE);
    IBlockData ☃ = this.blockStateList.getBlockData();
    if (l()) {
      ☃ = ☃.set(SEAMLESS, Boolean.valueOf(false));
    } else {
      ☃ = ☃.set(HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
    }
    j(☃.set(VARIANT, EnumStoneSlab2Variant.RED_SANDSTONE));
    a(CreativeModeTab.b);
  }
  
  public String getName()
  {
    return LocaleI18n.get(a() + ".red_sandstone.name");
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Item.getItemOf(Blocks.STONE_SLAB2);
  }
  
  public String b(int ☃)
  {
    return super.a() + "." + EnumStoneSlab2Variant.a(☃).d();
  }
  
  public IBlockState<?> n()
  {
    return VARIANT;
  }
  
  public Object a(ItemStack ☃)
  {
    return EnumStoneSlab2Variant.a(☃.getData() & 0x7);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    IBlockData ☃ = getBlockData().set(VARIANT, EnumStoneSlab2Variant.a(☃ & 0x7));
    if (l()) {
      ☃ = ☃.set(SEAMLESS, Boolean.valueOf((☃ & 0x8) != 0));
    } else {
      ☃ = ☃.set(HALF, (☃ & 0x8) == 0 ? BlockStepAbstract.EnumSlabHalf.BOTTOM : BlockStepAbstract.EnumSlabHalf.TOP);
    }
    return ☃;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumStoneSlab2Variant)☃.get(VARIANT)).a();
    if (l())
    {
      if (((Boolean)☃.get(SEAMLESS)).booleanValue()) {
        ☃ |= 0x8;
      }
    }
    else if (☃.get(HALF) == BlockStepAbstract.EnumSlabHalf.TOP) {
      ☃ |= 0x8;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    if (l()) {
      return new BlockStateList(this, new IBlockState[] { SEAMLESS, VARIANT });
    }
    return new BlockStateList(this, new IBlockState[] { HALF, VARIANT });
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumStoneSlab2Variant)☃.get(VARIANT)).c();
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumStoneSlab2Variant)☃.get(VARIANT)).a();
  }
  
  public static enum EnumStoneSlab2Variant
    implements INamable
  {
    private static final EnumStoneSlab2Variant[] b;
    private final int c;
    private final String d;
    private final MaterialMapColor e;
    
    static
    {
      b = new EnumStoneSlab2Variant[values().length];
      for (EnumStoneSlab2Variant ☃ : values()) {
        b[☃.a()] = ☃;
      }
    }
    
    private EnumStoneSlab2Variant(int ☃, String ☃, MaterialMapColor ☃)
    {
      this.c = ☃;
      this.d = ☃;
      this.e = ☃;
    }
    
    public int a()
    {
      return this.c;
    }
    
    public MaterialMapColor c()
    {
      return this.e;
    }
    
    public String toString()
    {
      return this.d;
    }
    
    public static EnumStoneSlab2Variant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= b.length)) {
        ☃ = 0;
      }
      return b[☃];
    }
    
    public String getName()
    {
      return this.d;
    }
    
    public String d()
    {
      return this.d;
    }
  }
}
