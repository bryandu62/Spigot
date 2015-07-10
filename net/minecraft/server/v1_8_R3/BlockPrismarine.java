package net.minecraft.server.v1_8_R3;

public class BlockPrismarine
  extends Block
{
  public static final BlockStateEnum<EnumPrismarineVariant> VARIANT = BlockStateEnum.of("variant", EnumPrismarineVariant.class);
  public static final int b = EnumPrismarineVariant.ROUGH.a();
  public static final int N = EnumPrismarineVariant.BRICKS.a();
  public static final int O = EnumPrismarineVariant.DARK.a();
  
  public BlockPrismarine()
  {
    super(Material.STONE);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumPrismarineVariant.ROUGH));
    a(CreativeModeTab.b);
  }
  
  public String getName()
  {
    return LocaleI18n.get(a() + "." + EnumPrismarineVariant.ROUGH.c() + ".name");
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    if (☃.get(VARIANT) == EnumPrismarineVariant.ROUGH) {
      return MaterialMapColor.y;
    }
    return MaterialMapColor.G;
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumPrismarineVariant)☃.get(VARIANT)).a();
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumPrismarineVariant)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumPrismarineVariant.a(☃));
  }
  
  public static enum EnumPrismarineVariant
    implements INamable
  {
    private static final EnumPrismarineVariant[] d;
    private final int e;
    private final String f;
    private final String g;
    
    static
    {
      d = new EnumPrismarineVariant[values().length];
      for (EnumPrismarineVariant ☃ : values()) {
        d[☃.a()] = ☃;
      }
    }
    
    private EnumPrismarineVariant(int ☃, String ☃, String ☃)
    {
      this.e = ☃;
      this.f = ☃;
      this.g = ☃;
    }
    
    public int a()
    {
      return this.e;
    }
    
    public String toString()
    {
      return this.f;
    }
    
    public static EnumPrismarineVariant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= d.length)) {
        ☃ = 0;
      }
      return d[☃];
    }
    
    public String getName()
    {
      return this.f;
    }
    
    public String c()
    {
      return this.g;
    }
  }
}
