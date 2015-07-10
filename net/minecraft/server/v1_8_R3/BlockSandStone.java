package net.minecraft.server.v1_8_R3;

public class BlockSandStone
  extends Block
{
  public static final BlockStateEnum<EnumSandstoneVariant> TYPE = BlockStateEnum.of("type", EnumSandstoneVariant.class);
  
  public BlockSandStone()
  {
    super(Material.STONE);
    j(this.blockStateList.getBlockData().set(TYPE, EnumSandstoneVariant.DEFAULT));
    a(CreativeModeTab.b);
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumSandstoneVariant)☃.get(TYPE)).a();
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return MaterialMapColor.d;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(TYPE, EnumSandstoneVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumSandstoneVariant)☃.get(TYPE)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { TYPE });
  }
  
  public static enum EnumSandstoneVariant
    implements INamable
  {
    private static final EnumSandstoneVariant[] d;
    private final int e;
    private final String f;
    private final String g;
    
    static
    {
      d = new EnumSandstoneVariant[values().length];
      for (EnumSandstoneVariant ☃ : values()) {
        d[☃.a()] = ☃;
      }
    }
    
    private EnumSandstoneVariant(int ☃, String ☃, String ☃)
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
    
    public static EnumSandstoneVariant a(int ☃)
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
