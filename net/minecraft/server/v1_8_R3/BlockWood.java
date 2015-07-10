package net.minecraft.server.v1_8_R3;

public class BlockWood
  extends Block
{
  public static final BlockStateEnum<EnumLogVariant> VARIANT = BlockStateEnum.of("variant", EnumLogVariant.class);
  
  public BlockWood()
  {
    super(Material.WOOD);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumLogVariant.OAK));
    a(CreativeModeTab.b);
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumLogVariant)☃.get(VARIANT)).a();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumLogVariant.a(☃));
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumLogVariant)☃.get(VARIANT)).c();
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumLogVariant)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  public static enum EnumLogVariant
    implements INamable
  {
    private static final EnumLogVariant[] g;
    private final int h;
    private final String i;
    private final String j;
    private final MaterialMapColor k;
    
    static
    {
      g = new EnumLogVariant[values().length];
      for (EnumLogVariant ☃ : values()) {
        g[☃.a()] = ☃;
      }
    }
    
    private EnumLogVariant(int ☃, String ☃, MaterialMapColor ☃)
    {
      this(☃, ☃, ☃, ☃);
    }
    
    private EnumLogVariant(int ☃, String ☃, String ☃, MaterialMapColor ☃)
    {
      this.h = ☃;
      this.i = ☃;
      this.j = ☃;
      this.k = ☃;
    }
    
    public int a()
    {
      return this.h;
    }
    
    public MaterialMapColor c()
    {
      return this.k;
    }
    
    public String toString()
    {
      return this.i;
    }
    
    public static EnumLogVariant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= g.length)) {
        ☃ = 0;
      }
      return g[☃];
    }
    
    public String getName()
    {
      return this.i;
    }
    
    public String d()
    {
      return this.j;
    }
  }
}
