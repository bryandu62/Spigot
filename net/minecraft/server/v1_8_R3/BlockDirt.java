package net.minecraft.server.v1_8_R3;

public class BlockDirt
  extends Block
{
  public static final BlockStateEnum<EnumDirtVariant> VARIANT = BlockStateEnum.of("variant", EnumDirtVariant.class);
  public static final BlockStateBoolean SNOWY = BlockStateBoolean.of("snowy");
  
  protected BlockDirt()
  {
    super(Material.EARTH);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumDirtVariant.DIRT).set(SNOWY, Boolean.valueOf(false)));
    a(CreativeModeTab.b);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumDirtVariant)☃.get(VARIANT)).d();
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    if (☃.get(VARIANT) == EnumDirtVariant.PODZOL)
    {
      Block ☃ = ☃.getType(☃.up()).getBlock();
      ☃ = ☃.set(SNOWY, Boolean.valueOf((☃ == Blocks.SNOW) || (☃ == Blocks.SNOW_LAYER)));
    }
    return ☃;
  }
  
  public int getDropData(World ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    if (☃.getBlock() != this) {
      return 0;
    }
    return ((EnumDirtVariant)☃.get(VARIANT)).a();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumDirtVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumDirtVariant)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT, SNOWY });
  }
  
  public int getDropData(IBlockData ☃)
  {
    EnumDirtVariant ☃ = (EnumDirtVariant)☃.get(VARIANT);
    if (☃ == EnumDirtVariant.PODZOL) {
      ☃ = EnumDirtVariant.DIRT;
    }
    return ☃.a();
  }
  
  public static enum EnumDirtVariant
    implements INamable
  {
    private static final EnumDirtVariant[] d;
    private final int e;
    private final String f;
    private final String g;
    private final MaterialMapColor h;
    
    static
    {
      d = new EnumDirtVariant[values().length];
      for (EnumDirtVariant ☃ : values()) {
        d[☃.a()] = ☃;
      }
    }
    
    private EnumDirtVariant(int ☃, String ☃, MaterialMapColor ☃)
    {
      this(☃, ☃, ☃, ☃);
    }
    
    private EnumDirtVariant(int ☃, String ☃, String ☃, MaterialMapColor ☃)
    {
      this.e = ☃;
      this.f = ☃;
      this.g = ☃;
      this.h = ☃;
    }
    
    public int a()
    {
      return this.e;
    }
    
    public String c()
    {
      return this.g;
    }
    
    public MaterialMapColor d()
    {
      return this.h;
    }
    
    public String toString()
    {
      return this.f;
    }
    
    public static EnumDirtVariant a(int ☃)
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
  }
}
