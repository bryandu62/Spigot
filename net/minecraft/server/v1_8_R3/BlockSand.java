package net.minecraft.server.v1_8_R3;

public class BlockSand
  extends BlockFalling
{
  public static final BlockStateEnum<EnumSandVariant> VARIANT = BlockStateEnum.of("variant", EnumSandVariant.class);
  
  public BlockSand()
  {
    j(this.blockStateList.getBlockData().set(VARIANT, EnumSandVariant.SAND));
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumSandVariant)☃.get(VARIANT)).a();
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumSandVariant)☃.get(VARIANT)).c();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumSandVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumSandVariant)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  public static enum EnumSandVariant
    implements INamable
  {
    private static final EnumSandVariant[] c;
    private final int d;
    private final String e;
    private final MaterialMapColor f;
    private final String g;
    
    static
    {
      c = new EnumSandVariant[values().length];
      for (EnumSandVariant ☃ : values()) {
        c[☃.a()] = ☃;
      }
    }
    
    private EnumSandVariant(int ☃, String ☃, String ☃, MaterialMapColor ☃)
    {
      this.d = ☃;
      this.e = ☃;
      this.f = ☃;
      this.g = ☃;
    }
    
    public int a()
    {
      return this.d;
    }
    
    public String toString()
    {
      return this.e;
    }
    
    public MaterialMapColor c()
    {
      return this.f;
    }
    
    public static EnumSandVariant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= c.length)) {
        ☃ = 0;
      }
      return c[☃];
    }
    
    public String getName()
    {
      return this.e;
    }
    
    public String d()
    {
      return this.g;
    }
  }
}
