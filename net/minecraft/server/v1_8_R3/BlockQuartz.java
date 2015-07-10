package net.minecraft.server.v1_8_R3;

public class BlockQuartz
  extends Block
{
  public static final BlockStateEnum<EnumQuartzVariant> VARIANT = BlockStateEnum.of("variant", EnumQuartzVariant.class);
  
  public BlockQuartz()
  {
    super(Material.STONE);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumQuartzVariant.DEFAULT));
    a(CreativeModeTab.b);
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    if (☃ == EnumQuartzVariant.LINES_Y.a())
    {
      switch (1.a[☃.k().ordinal()])
      {
      case 1: 
        return getBlockData().set(VARIANT, EnumQuartzVariant.LINES_Z);
      case 2: 
        return getBlockData().set(VARIANT, EnumQuartzVariant.LINES_X);
      }
      return getBlockData().set(VARIANT, EnumQuartzVariant.LINES_Y);
    }
    if (☃ == EnumQuartzVariant.CHISELED.a()) {
      return getBlockData().set(VARIANT, EnumQuartzVariant.CHISELED);
    }
    return getBlockData().set(VARIANT, EnumQuartzVariant.DEFAULT);
  }
  
  public int getDropData(IBlockData ☃)
  {
    EnumQuartzVariant ☃ = (EnumQuartzVariant)☃.get(VARIANT);
    if ((☃ == EnumQuartzVariant.LINES_X) || (☃ == EnumQuartzVariant.LINES_Z)) {
      return EnumQuartzVariant.LINES_Y.a();
    }
    return ☃.a();
  }
  
  protected ItemStack i(IBlockData ☃)
  {
    EnumQuartzVariant ☃ = (EnumQuartzVariant)☃.get(VARIANT);
    if ((☃ == EnumQuartzVariant.LINES_X) || (☃ == EnumQuartzVariant.LINES_Z)) {
      return new ItemStack(Item.getItemOf(this), 1, EnumQuartzVariant.LINES_Y.a());
    }
    return super.i(☃);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return MaterialMapColor.p;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumQuartzVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumQuartzVariant)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  public static enum EnumQuartzVariant
    implements INamable
  {
    private static final EnumQuartzVariant[] f;
    private final int g;
    private final String h;
    private final String i;
    
    static
    {
      f = new EnumQuartzVariant[values().length];
      for (EnumQuartzVariant ☃ : values()) {
        f[☃.a()] = ☃;
      }
    }
    
    private EnumQuartzVariant(int ☃, String ☃, String ☃)
    {
      this.g = ☃;
      this.h = ☃;
      this.i = ☃;
    }
    
    public int a()
    {
      return this.g;
    }
    
    public String toString()
    {
      return this.i;
    }
    
    public static EnumQuartzVariant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= f.length)) {
        ☃ = 0;
      }
      return f[☃];
    }
    
    public String getName()
    {
      return this.h;
    }
  }
}
