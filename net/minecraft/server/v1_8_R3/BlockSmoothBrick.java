package net.minecraft.server.v1_8_R3;

public class BlockSmoothBrick
  extends Block
{
  public static final BlockStateEnum<EnumStonebrickType> VARIANT = BlockStateEnum.of("variant", EnumStonebrickType.class);
  public static final int b = EnumStonebrickType.DEFAULT.a();
  public static final int N = EnumStonebrickType.MOSSY.a();
  public static final int O = EnumStonebrickType.CRACKED.a();
  public static final int P = EnumStonebrickType.CHISELED.a();
  
  public BlockSmoothBrick()
  {
    super(Material.STONE);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumStonebrickType.DEFAULT));
    a(CreativeModeTab.b);
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumStonebrickType)☃.get(VARIANT)).a();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumStonebrickType.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumStonebrickType)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  public static enum EnumStonebrickType
    implements INamable
  {
    private static final EnumStonebrickType[] e;
    private final int f;
    private final String g;
    private final String h;
    
    static
    {
      e = new EnumStonebrickType[values().length];
      for (EnumStonebrickType ☃ : values()) {
        e[☃.a()] = ☃;
      }
    }
    
    private EnumStonebrickType(int ☃, String ☃, String ☃)
    {
      this.f = ☃;
      this.g = ☃;
      this.h = ☃;
    }
    
    public int a()
    {
      return this.f;
    }
    
    public String toString()
    {
      return this.g;
    }
    
    public static EnumStonebrickType a(int ☃)
    {
      if ((☃ < 0) || (☃ >= e.length)) {
        ☃ = 0;
      }
      return e[☃];
    }
    
    public String getName()
    {
      return this.g;
    }
    
    public String c()
    {
      return this.h;
    }
  }
}
