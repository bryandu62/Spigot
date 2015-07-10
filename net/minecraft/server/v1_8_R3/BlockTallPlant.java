package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockTallPlant
  extends BlockPlant
  implements IBlockFragilePlantElement
{
  public static final BlockStateEnum<EnumTallFlowerVariants> VARIANT = BlockStateEnum.of("variant", EnumTallFlowerVariants.class);
  public static final BlockStateEnum<EnumTallPlantHalf> HALF = BlockStateEnum.of("half", EnumTallPlantHalf.class);
  public static final BlockStateEnum<EnumDirection> N = BlockDirectional.FACING;
  
  public BlockTallPlant()
  {
    super(Material.REPLACEABLE_PLANT);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumTallFlowerVariants.SUNFLOWER).set(HALF, EnumTallPlantHalf.LOWER).set(N, EnumDirection.NORTH));
    c(0.0F);
    a(h);
    c("doublePlant");
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public EnumTallFlowerVariants e(IBlockAccess ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    if (☃.getBlock() == this)
    {
      ☃ = updateState(☃, ☃, ☃);
      
      return (EnumTallFlowerVariants)☃.get(VARIANT);
    }
    return EnumTallFlowerVariants.FERN;
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    return (super.canPlace(☃, ☃)) && (☃.isEmpty(☃.up()));
  }
  
  public boolean a(World ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    if (☃.getBlock() == this)
    {
      EnumTallFlowerVariants ☃ = (EnumTallFlowerVariants)updateState(☃, ☃, ☃).get(VARIANT);
      return (☃ == EnumTallFlowerVariants.FERN) || (☃ == EnumTallFlowerVariants.GRASS);
    }
    return true;
  }
  
  protected void e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (f(☃, ☃, ☃)) {
      return;
    }
    boolean ☃ = ☃.get(HALF) == EnumTallPlantHalf.UPPER;
    BlockPosition ☃ = ☃ ? ☃ : ☃.up();
    BlockPosition ☃ = ☃ ? ☃.down() : ☃;
    
    Block ☃ = ☃ ? this : ☃.getType(☃).getBlock();
    Block ☃ = ☃ ? ☃.getType(☃).getBlock() : this;
    if (☃ == this) {
      ☃.setTypeAndData(☃, Blocks.AIR.getBlockData(), 2);
    }
    if (☃ == this)
    {
      ☃.setTypeAndData(☃, Blocks.AIR.getBlockData(), 3);
      if (!☃) {
        b(☃, ☃, ☃, 0);
      }
    }
  }
  
  public boolean f(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (☃.get(HALF) == EnumTallPlantHalf.UPPER) {
      return ☃.getType(☃.down()).getBlock() == this;
    }
    IBlockData ☃ = ☃.getType(☃.up());
    return (☃.getBlock() == this) && (super.f(☃, ☃, ☃));
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    if (☃.get(HALF) == EnumTallPlantHalf.UPPER) {
      return null;
    }
    EnumTallFlowerVariants ☃ = (EnumTallFlowerVariants)☃.get(VARIANT);
    if (☃ == EnumTallFlowerVariants.FERN) {
      return null;
    }
    if (☃ == EnumTallFlowerVariants.GRASS)
    {
      if (☃.nextInt(8) == 0) {
        return Items.WHEAT_SEEDS;
      }
      return null;
    }
    return Item.getItemOf(this);
  }
  
  public int getDropData(IBlockData ☃)
  {
    if ((☃.get(HALF) == EnumTallPlantHalf.UPPER) || (☃.get(VARIANT) == EnumTallFlowerVariants.GRASS)) {
      return 0;
    }
    return ((EnumTallFlowerVariants)☃.get(VARIANT)).a();
  }
  
  public void a(World ☃, BlockPosition ☃, EnumTallFlowerVariants ☃, int ☃)
  {
    ☃.setTypeAndData(☃, getBlockData().set(HALF, EnumTallPlantHalf.LOWER).set(VARIANT, ☃), ☃);
    ☃.setTypeAndData(☃.up(), getBlockData().set(HALF, EnumTallPlantHalf.UPPER), ☃);
  }
  
  public void postPlace(World ☃, BlockPosition ☃, IBlockData ☃, EntityLiving ☃, ItemStack ☃)
  {
    ☃.setTypeAndData(☃.up(), getBlockData().set(HALF, EnumTallPlantHalf.UPPER), 2);
  }
  
  public void a(World ☃, EntityHuman ☃, BlockPosition ☃, IBlockData ☃, TileEntity ☃)
  {
    if ((!☃.isClientSide) && (☃.bZ() != null) && (☃.bZ().getItem() == Items.SHEARS)) {
      if ((☃.get(HALF) == EnumTallPlantHalf.LOWER) && 
        (b(☃, ☃, ☃, ☃))) {
        return;
      }
    }
    super.a(☃, ☃, ☃, ☃, ☃);
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃)
  {
    if (☃.get(HALF) == EnumTallPlantHalf.UPPER)
    {
      if (☃.getType(☃.down()).getBlock() == this) {
        if (!☃.abilities.canInstantlyBuild)
        {
          IBlockData ☃ = ☃.getType(☃.down());
          EnumTallFlowerVariants ☃ = (EnumTallFlowerVariants)☃.get(VARIANT);
          if ((☃ == EnumTallFlowerVariants.FERN) || (☃ == EnumTallFlowerVariants.GRASS))
          {
            if (!☃.isClientSide)
            {
              if ((☃.bZ() != null) && (☃.bZ().getItem() == Items.SHEARS))
              {
                b(☃, ☃, ☃, ☃);
                ☃.setAir(☃.down());
              }
              else
              {
                ☃.setAir(☃.down(), true);
              }
            }
            else {
              ☃.setAir(☃.down());
            }
          }
          else {
            ☃.setAir(☃.down(), true);
          }
        }
        else
        {
          ☃.setAir(☃.down());
        }
      }
    }
    else if ((☃.abilities.canInstantlyBuild) && (☃.getType(☃.up()).getBlock() == this)) {
      ☃.setTypeAndData(☃.up(), Blocks.AIR.getBlockData(), 2);
    }
    super.a(☃, ☃, ☃, ☃);
  }
  
  private boolean b(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃)
  {
    EnumTallFlowerVariants ☃ = (EnumTallFlowerVariants)☃.get(VARIANT);
    if ((☃ == EnumTallFlowerVariants.FERN) || (☃ == EnumTallFlowerVariants.GRASS))
    {
      ☃.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
      
      int ☃ = (☃ == EnumTallFlowerVariants.GRASS ? BlockLongGrass.EnumTallGrassType.GRASS : BlockLongGrass.EnumTallGrassType.FERN).a();
      a(☃, ☃, new ItemStack(Blocks.TALLGRASS, 2, ☃));
      return true;
    }
    return false;
  }
  
  public int getDropData(World ☃, BlockPosition ☃)
  {
    return e(☃, ☃).a();
  }
  
  public boolean a(World ☃, BlockPosition ☃, IBlockData ☃, boolean ☃)
  {
    EnumTallFlowerVariants ☃ = e(☃, ☃);
    
    return (☃ != EnumTallFlowerVariants.GRASS) && (☃ != EnumTallFlowerVariants.FERN);
  }
  
  public boolean a(World ☃, Random ☃, BlockPosition ☃, IBlockData ☃)
  {
    return true;
  }
  
  public void b(World ☃, Random ☃, BlockPosition ☃, IBlockData ☃)
  {
    a(☃, ☃, new ItemStack(this, 1, e(☃, ☃).a()));
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    if ((☃ & 0x8) > 0) {
      return getBlockData().set(HALF, EnumTallPlantHalf.UPPER);
    }
    return getBlockData().set(HALF, EnumTallPlantHalf.LOWER).set(VARIANT, EnumTallFlowerVariants.a(☃ & 0x7));
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    if (☃.get(HALF) == EnumTallPlantHalf.UPPER)
    {
      IBlockData ☃ = ☃.getType(☃.down());
      if (☃.getBlock() == this) {
        ☃ = ☃.set(VARIANT, ☃.get(VARIANT));
      }
    }
    return ☃;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    if (☃.get(HALF) == EnumTallPlantHalf.UPPER) {
      return 0x8 | ((EnumDirection)☃.get(N)).b();
    }
    return ((EnumTallFlowerVariants)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { HALF, VARIANT, N });
  }
  
  public static enum EnumTallFlowerVariants
    implements INamable
  {
    private static final EnumTallFlowerVariants[] g;
    private final int h;
    private final String i;
    private final String j;
    
    static
    {
      g = new EnumTallFlowerVariants[values().length];
      for (EnumTallFlowerVariants ☃ : values()) {
        g[☃.a()] = ☃;
      }
    }
    
    private EnumTallFlowerVariants(int ☃, String ☃)
    {
      this(☃, ☃, ☃);
    }
    
    private EnumTallFlowerVariants(int ☃, String ☃, String ☃)
    {
      this.h = ☃;
      this.i = ☃;
      this.j = ☃;
    }
    
    public int a()
    {
      return this.h;
    }
    
    public String toString()
    {
      return this.i;
    }
    
    public static EnumTallFlowerVariants a(int ☃)
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
    
    public String c()
    {
      return this.j;
    }
  }
  
  public static enum EnumTallPlantHalf
    implements INamable
  {
    private EnumTallPlantHalf() {}
    
    public String toString()
    {
      return getName();
    }
    
    public String getName()
    {
      return this == UPPER ? "upper" : "lower";
    }
  }
}
