package net.minecraft.server.v1_8_R3;

public class BlockCobbleWall
  extends Block
{
  public static final BlockStateBoolean UP = BlockStateBoolean.of("up");
  public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
  public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
  public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
  public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
  public static final BlockStateEnum<EnumCobbleVariant> VARIANT = BlockStateEnum.of("variant", EnumCobbleVariant.class);
  
  public BlockCobbleWall(Block ☃)
  {
    super(☃.material);
    j(this.blockStateList.getBlockData().set(UP, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)).set(VARIANT, EnumCobbleVariant.NORMAL));
    c(☃.strength);
    b(☃.durability / 3.0F);
    a(☃.stepSound);
    a(CreativeModeTab.b);
  }
  
  public String getName()
  {
    return LocaleI18n.get(a() + "." + EnumCobbleVariant.NORMAL.c() + ".name");
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean b(IBlockAccess ☃, BlockPosition ☃)
  {
    return false;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    boolean ☃ = e(☃, ☃.north());
    boolean ☃ = e(☃, ☃.south());
    boolean ☃ = e(☃, ☃.west());
    boolean ☃ = e(☃, ☃.east());
    
    float ☃ = 0.25F;
    float ☃ = 0.75F;
    float ☃ = 0.25F;
    float ☃ = 0.75F;
    float ☃ = 1.0F;
    if (☃) {
      ☃ = 0.0F;
    }
    if (☃) {
      ☃ = 1.0F;
    }
    if (☃) {
      ☃ = 0.0F;
    }
    if (☃) {
      ☃ = 1.0F;
    }
    if ((☃) && (☃) && (!☃) && (!☃))
    {
      ☃ = 0.8125F;
      ☃ = 0.3125F;
      ☃ = 0.6875F;
    }
    else if ((!☃) && (!☃) && (☃) && (☃))
    {
      ☃ = 0.8125F;
      ☃ = 0.3125F;
      ☃ = 0.6875F;
    }
    a(☃, 0.0F, ☃, ☃, ☃, ☃);
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    updateShape(☃, ☃);
    this.maxY = 1.5D;
    return super.a(☃, ☃, ☃);
  }
  
  public boolean e(IBlockAccess ☃, BlockPosition ☃)
  {
    Block ☃ = ☃.getType(☃).getBlock();
    if (☃ == Blocks.BARRIER) {
      return false;
    }
    if ((☃ == this) || ((☃ instanceof BlockFenceGate))) {
      return true;
    }
    if ((☃.material.k()) && (☃.d())) {
      return ☃.material != Material.PUMPKIN;
    }
    return false;
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumCobbleVariant)☃.get(VARIANT)).a();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumCobbleVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumCobbleVariant)☃.get(VARIANT)).a();
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    return ☃.set(UP, Boolean.valueOf(!☃.isEmpty(☃.up()))).set(NORTH, Boolean.valueOf(e(☃, ☃.north()))).set(EAST, Boolean.valueOf(e(☃, ☃.east()))).set(SOUTH, Boolean.valueOf(e(☃, ☃.south()))).set(WEST, Boolean.valueOf(e(☃, ☃.west())));
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { UP, NORTH, EAST, WEST, SOUTH, VARIANT });
  }
  
  public static enum EnumCobbleVariant
    implements INamable
  {
    private static final EnumCobbleVariant[] c;
    private final int d;
    private final String e;
    private String f;
    
    static
    {
      c = new EnumCobbleVariant[values().length];
      for (EnumCobbleVariant ☃ : values()) {
        c[☃.a()] = ☃;
      }
    }
    
    private EnumCobbleVariant(int ☃, String ☃, String ☃)
    {
      this.d = ☃;
      this.e = ☃;
      this.f = ☃;
    }
    
    public int a()
    {
      return this.d;
    }
    
    public String toString()
    {
      return this.e;
    }
    
    public static EnumCobbleVariant a(int ☃)
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
    
    public String c()
    {
      return this.f;
    }
  }
}
