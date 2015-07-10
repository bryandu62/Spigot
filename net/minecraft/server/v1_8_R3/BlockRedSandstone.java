package net.minecraft.server.v1_8_R3;

public class BlockRedSandstone
  extends Block
{
  public static final BlockStateEnum<EnumRedSandstoneVariant> TYPE = BlockStateEnum.of("type", EnumRedSandstoneVariant.class);
  
  public BlockRedSandstone()
  {
    super(Material.STONE, BlockSand.EnumSandVariant.RED_SAND.c());
    j(this.blockStateList.getBlockData().set(TYPE, EnumRedSandstoneVariant.DEFAULT));
    a(CreativeModeTab.b);
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumRedSandstoneVariant)☃.get(TYPE)).a();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(TYPE, EnumRedSandstoneVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumRedSandstoneVariant)☃.get(TYPE)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { TYPE });
  }
  
  public static enum EnumRedSandstoneVariant
    implements INamable
  {
    private static final EnumRedSandstoneVariant[] d;
    private final int e;
    private final String f;
    private final String g;
    
    static
    {
      d = new EnumRedSandstoneVariant[values().length];
      for (EnumRedSandstoneVariant ☃ : values()) {
        d[☃.a()] = ☃;
      }
    }
    
    private EnumRedSandstoneVariant(int ☃, String ☃, String ☃)
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
    
    public static EnumRedSandstoneVariant a(int ☃)
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
