package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockLongGrass
  extends BlockPlant
  implements IBlockFragilePlantElement
{
  public static final BlockStateEnum<EnumTallGrassType> TYPE = BlockStateEnum.of("type", EnumTallGrassType.class);
  
  protected BlockLongGrass()
  {
    super(Material.REPLACEABLE_PLANT);
    j(this.blockStateList.getBlockData().set(TYPE, EnumTallGrassType.DEAD_BUSH));
    float ☃ = 0.4F;
    a(0.5F - ☃, 0.0F, 0.5F - ☃, 0.5F + ☃, 0.8F, 0.5F + ☃);
  }
  
  public boolean f(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return c(☃.getType(☃.down()).getBlock());
  }
  
  public boolean a(World ☃, BlockPosition ☃)
  {
    return true;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    if (☃.nextInt(8) == 0) {
      return Items.WHEAT_SEEDS;
    }
    return null;
  }
  
  public int getDropCount(int ☃, Random ☃)
  {
    return 1 + ☃.nextInt(☃ * 2 + 1);
  }
  
  public void a(World ☃, EntityHuman ☃, BlockPosition ☃, IBlockData ☃, TileEntity ☃)
  {
    if ((!☃.isClientSide) && (☃.bZ() != null) && (☃.bZ().getItem() == Items.SHEARS))
    {
      ☃.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
      
      a(☃, ☃, new ItemStack(Blocks.TALLGRASS, 1, ((EnumTallGrassType)☃.get(TYPE)).a()));
    }
    else
    {
      super.a(☃, ☃, ☃, ☃, ☃);
    }
  }
  
  public int getDropData(World ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    return ☃.getBlock().toLegacyData(☃);
  }
  
  public boolean a(World ☃, BlockPosition ☃, IBlockData ☃, boolean ☃)
  {
    return ☃.get(TYPE) != EnumTallGrassType.DEAD_BUSH;
  }
  
  public boolean a(World ☃, Random ☃, BlockPosition ☃, IBlockData ☃)
  {
    return true;
  }
  
  public void b(World ☃, Random ☃, BlockPosition ☃, IBlockData ☃)
  {
    BlockTallPlant.EnumTallFlowerVariants ☃ = BlockTallPlant.EnumTallFlowerVariants.GRASS;
    if (☃.get(TYPE) == EnumTallGrassType.FERN) {
      ☃ = BlockTallPlant.EnumTallFlowerVariants.FERN;
    }
    if (Blocks.DOUBLE_PLANT.canPlace(☃, ☃)) {
      Blocks.DOUBLE_PLANT.a(☃, ☃, ☃, 2);
    }
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(TYPE, EnumTallGrassType.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumTallGrassType)☃.get(TYPE)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { TYPE });
  }
  
  public static enum EnumTallGrassType
    implements INamable
  {
    private static final EnumTallGrassType[] d;
    private final int e;
    private final String f;
    
    static
    {
      d = new EnumTallGrassType[values().length];
      for (EnumTallGrassType ☃ : values()) {
        d[☃.a()] = ☃;
      }
    }
    
    private EnumTallGrassType(int ☃, String ☃)
    {
      this.e = ☃;
      this.f = ☃;
    }
    
    public int a()
    {
      return this.e;
    }
    
    public String toString()
    {
      return this.f;
    }
    
    public static EnumTallGrassType a(int ☃)
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
