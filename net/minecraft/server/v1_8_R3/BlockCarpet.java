package net.minecraft.server.v1_8_R3;

public class BlockCarpet
  extends Block
{
  public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);
  
  protected BlockCarpet()
  {
    super(Material.WOOL);
    j(this.blockStateList.getBlockData().set(COLOR, EnumColor.WHITE));
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    a(true);
    a(CreativeModeTab.c);
    b(0);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumColor)☃.get(COLOR)).e();
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public void j()
  {
    b(0);
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    b(0);
  }
  
  protected void b(int ☃)
  {
    int ☃ = 0;
    float ☃ = 1 * (1 + ☃) / 16.0F;
    a(0.0F, 0.0F, 0.0F, 1.0F, ☃, 1.0F);
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    return (super.canPlace(☃, ☃)) && (e(☃, ☃));
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    e(☃, ☃, ☃);
  }
  
  private boolean e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (!e(☃, ☃))
    {
      b(☃, ☃, ☃, 0);
      ☃.setAir(☃);
      return false;
    }
    return true;
  }
  
  private boolean e(World ☃, BlockPosition ☃)
  {
    return !☃.isEmpty(☃.down());
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumColor)☃.get(COLOR)).getColorIndex();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(COLOR, EnumColor.fromColorIndex(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumColor)☃.get(COLOR)).getColorIndex();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { COLOR });
  }
}
