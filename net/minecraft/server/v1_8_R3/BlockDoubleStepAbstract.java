package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class BlockDoubleStepAbstract
  extends BlockStepAbstract
{
  public static final BlockStateBoolean SEAMLESS = BlockStateBoolean.of("seamless");
  public static final BlockStateEnum<EnumStoneSlabVariant> VARIANT = BlockStateEnum.of("variant", EnumStoneSlabVariant.class);
  
  public BlockDoubleStepAbstract()
  {
    super(Material.STONE);
    IBlockData ☃ = this.blockStateList.getBlockData();
    if (l()) {
      ☃ = ☃.set(SEAMLESS, Boolean.valueOf(false));
    } else {
      ☃ = ☃.set(HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
    }
    j(☃.set(VARIANT, EnumStoneSlabVariant.STONE));
    a(CreativeModeTab.b);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Item.getItemOf(Blocks.STONE_SLAB);
  }
  
  public String b(int ☃)
  {
    return super.a() + "." + EnumStoneSlabVariant.a(☃).d();
  }
  
  public IBlockState<?> n()
  {
    return VARIANT;
  }
  
  public Object a(ItemStack ☃)
  {
    return EnumStoneSlabVariant.a(☃.getData() & 0x7);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    IBlockData ☃ = getBlockData().set(VARIANT, EnumStoneSlabVariant.a(☃ & 0x7));
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
    
    ☃ |= ((EnumStoneSlabVariant)☃.get(VARIANT)).a();
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
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumStoneSlabVariant)☃.get(VARIANT)).a();
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumStoneSlabVariant)☃.get(VARIANT)).c();
  }
  
  public static enum EnumStoneSlabVariant
    implements INamable
  {
    private static final EnumStoneSlabVariant[] i;
    private final int j;
    private final MaterialMapColor k;
    private final String l;
    private final String m;
    
    static
    {
      i = new EnumStoneSlabVariant[values().length];
      for (EnumStoneSlabVariant ☃ : values()) {
        i[☃.a()] = ☃;
      }
    }
    
    private EnumStoneSlabVariant(int ☃, MaterialMapColor ☃, String ☃)
    {
      this(☃, ☃, ☃, ☃);
    }
    
    private EnumStoneSlabVariant(int ☃, MaterialMapColor ☃, String ☃, String ☃)
    {
      this.j = ☃;
      this.k = ☃;
      this.l = ☃;
      this.m = ☃;
    }
    
    public int a()
    {
      return this.j;
    }
    
    public MaterialMapColor c()
    {
      return this.k;
    }
    
    public String toString()
    {
      return this.l;
    }
    
    public static EnumStoneSlabVariant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= i.length)) {
        ☃ = 0;
      }
      return i[☃];
    }
    
    public String getName()
    {
      return this.l;
    }
    
    public String d()
    {
      return this.m;
    }
  }
}
