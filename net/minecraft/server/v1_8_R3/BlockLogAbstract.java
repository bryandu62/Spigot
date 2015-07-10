package net.minecraft.server.v1_8_R3;

public abstract class BlockLogAbstract
  extends BlockRotatable
{
  public static final BlockStateEnum<EnumLogRotation> AXIS = BlockStateEnum.of("axis", EnumLogRotation.class);
  
  public BlockLogAbstract()
  {
    super(Material.WOOD);
    a(CreativeModeTab.b);
    c(2.0F);
    a(f);
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    int ☃ = 4;
    int ☃ = ☃ + 1;
    if (!☃.areChunksLoadedBetween(☃.a(-☃, -☃, -☃), ☃.a(☃, ☃, ☃))) {
      return;
    }
    for (BlockPosition ☃ : BlockPosition.a(☃.a(-☃, -☃, -☃), ☃.a(☃, ☃, ☃)))
    {
      IBlockData ☃ = ☃.getType(☃);
      if ((☃.getBlock().getMaterial() == Material.LEAVES) && 
        (!((Boolean)☃.get(BlockLeaves.CHECK_DECAY)).booleanValue())) {
        ☃.setTypeAndData(☃, ☃.set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4);
      }
    }
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return super.getPlacedState(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃).set(AXIS, EnumLogRotation.a(☃.k()));
  }
  
  public static enum EnumLogRotation
    implements INamable
  {
    private final String e;
    
    private EnumLogRotation(String ☃)
    {
      this.e = ☃;
    }
    
    public String toString()
    {
      return this.e;
    }
    
    public static EnumLogRotation a(EnumDirection.EnumAxis ☃)
    {
      switch (BlockLogAbstract.1.a[☃.ordinal()])
      {
      case 1: 
        return X;
      case 2: 
        return Y;
      case 3: 
        return Z;
      }
      return NONE;
    }
    
    public String getName()
    {
      return this.e;
    }
  }
}
