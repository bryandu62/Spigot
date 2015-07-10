package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;

public class BlockRedstoneComparator
  extends BlockDiodeAbstract
  implements IContainer
{
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  public static final BlockStateEnum<EnumComparatorMode> MODE = BlockStateEnum.of("mode", EnumComparatorMode.class);
  
  public BlockRedstoneComparator(boolean ☃)
  {
    super(☃);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(POWERED, Boolean.valueOf(false)).set(MODE, EnumComparatorMode.COMPARE));
    this.isTileEntity = true;
  }
  
  public String getName()
  {
    return LocaleI18n.get("item.comparator.name");
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.COMPARATOR;
  }
  
  protected int d(IBlockData ☃)
  {
    return 2;
  }
  
  protected IBlockData e(IBlockData ☃)
  {
    Boolean ☃ = (Boolean)☃.get(POWERED);
    EnumComparatorMode ☃ = (EnumComparatorMode)☃.get(MODE);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    
    return Blocks.POWERED_COMPARATOR.getBlockData().set(FACING, ☃).set(POWERED, ☃).set(MODE, ☃);
  }
  
  protected IBlockData k(IBlockData ☃)
  {
    Boolean ☃ = (Boolean)☃.get(POWERED);
    EnumComparatorMode ☃ = (EnumComparatorMode)☃.get(MODE);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    
    return Blocks.UNPOWERED_COMPARATOR.getBlockData().set(FACING, ☃).set(POWERED, ☃).set(MODE, ☃);
  }
  
  protected boolean l(IBlockData ☃)
  {
    return (this.N) || (((Boolean)☃.get(POWERED)).booleanValue());
  }
  
  protected int a(IBlockAccess ☃, BlockPosition ☃, IBlockData ☃)
  {
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityComparator)) {
      return ((TileEntityComparator)☃).b();
    }
    return 0;
  }
  
  private int j(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (☃.get(MODE) == EnumComparatorMode.SUBTRACT) {
      return Math.max(f(☃, ☃, ☃) - c(☃, ☃, ☃), 0);
    }
    return f(☃, ☃, ☃);
  }
  
  protected boolean e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    int ☃ = f(☃, ☃, ☃);
    if (☃ >= 15) {
      return true;
    }
    if (☃ == 0) {
      return false;
    }
    int ☃ = c(☃, ☃, ☃);
    if (☃ == 0) {
      return true;
    }
    return ☃ >= ☃;
  }
  
  protected int f(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    int ☃ = super.f(☃, ☃, ☃);
    
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    BlockPosition ☃ = ☃.shift(☃);
    Block ☃ = ☃.getType(☃).getBlock();
    if (☃.isComplexRedstone())
    {
      ☃ = ☃.l(☃, ☃);
    }
    else if ((☃ < 15) && (☃.isOccluding()))
    {
      ☃ = ☃.shift(☃);
      ☃ = ☃.getType(☃).getBlock();
      if (☃.isComplexRedstone())
      {
        ☃ = ☃.l(☃, ☃);
      }
      else if (☃.getMaterial() == Material.AIR)
      {
        EntityItemFrame ☃ = a(☃, ☃, ☃);
        if (☃ != null) {
          ☃ = ☃.q();
        }
      }
    }
    return ☃;
  }
  
  private EntityItemFrame a(World ☃, final EnumDirection ☃, BlockPosition ☃)
  {
    List<EntityItemFrame> ☃ = ☃.a(EntityItemFrame.class, new AxisAlignedBB(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX() + 1, ☃.getY() + 1, ☃.getZ() + 1), new Predicate()
    {
      public boolean a(Entity ☃)
      {
        return (☃ != null) && (☃.getDirection() == ☃);
      }
    });
    if (☃.size() == 1) {
      return (EntityItemFrame)☃.get(0);
    }
    return null;
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (!☃.abilities.mayBuild) {
      return false;
    }
    ☃ = ☃.a(MODE);
    ☃.makeSound(☃.getX() + 0.5D, ☃.getY() + 0.5D, ☃.getZ() + 0.5D, "random.click", 0.3F, ☃.get(MODE) == EnumComparatorMode.SUBTRACT ? 0.55F : 0.5F);
    
    ☃.setTypeAndData(☃, ☃, 2);
    k(☃, ☃, ☃);
    return true;
  }
  
  protected void g(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (☃.a(☃, this)) {
      return;
    }
    int ☃ = j(☃, ☃, ☃);
    TileEntity ☃ = ☃.getTileEntity(☃);
    int ☃ = (☃ instanceof TileEntityComparator) ? ((TileEntityComparator)☃).b() : 0;
    if ((☃ != ☃) || (l(☃) != e(☃, ☃, ☃))) {
      if (i(☃, ☃, ☃)) {
        ☃.a(☃, this, 2, -1);
      } else {
        ☃.a(☃, this, 2, 0);
      }
    }
  }
  
  private void k(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    int ☃ = j(☃, ☃, ☃);
    
    TileEntity ☃ = ☃.getTileEntity(☃);
    int ☃ = 0;
    if ((☃ instanceof TileEntityComparator))
    {
      TileEntityComparator ☃ = (TileEntityComparator)☃;
      
      ☃ = ☃.b();
      ☃.a(☃);
    }
    if ((☃ != ☃) || (☃.get(MODE) == EnumComparatorMode.COMPARE))
    {
      boolean ☃ = e(☃, ☃, ☃);
      boolean ☃ = l(☃);
      if ((☃) && (!☃)) {
        ☃.setTypeAndData(☃, ☃.set(POWERED, Boolean.valueOf(false)), 2);
      } else if ((!☃) && (☃)) {
        ☃.setTypeAndData(☃, ☃.set(POWERED, Boolean.valueOf(true)), 2);
      }
      h(☃, ☃, ☃);
    }
  }
  
  public void b(World ☃, BlockPosition ☃, IBlockData ☃, Random ☃)
  {
    if (this.N) {
      ☃.setTypeAndData(☃, k(☃).set(POWERED, Boolean.valueOf(true)), 4);
    }
    k(☃, ☃, ☃);
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    super.onPlace(☃, ☃, ☃);
    ☃.setTileEntity(☃, a(☃, 0));
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    super.remove(☃, ☃, ☃);
    ☃.t(☃);
    
    h(☃, ☃, ☃);
  }
  
  public boolean a(World ☃, BlockPosition ☃, IBlockData ☃, int ☃, int ☃)
  {
    super.a(☃, ☃, ☃, ☃, ☃);
    
    TileEntity ☃ = ☃.getTileEntity(☃);
    if (☃ == null) {
      return false;
    }
    return ☃.c(☃, ☃);
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityComparator();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(FACING, EnumDirection.fromType2(☃)).set(POWERED, Boolean.valueOf((☃ & 0x8) > 0)).set(MODE, (☃ & 0x4) > 0 ? EnumComparatorMode.SUBTRACT : EnumComparatorMode.COMPARE);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumDirection)☃.get(FACING)).b();
    if (((Boolean)☃.get(POWERED)).booleanValue()) {
      ☃ |= 0x8;
    }
    if (☃.get(MODE) == EnumComparatorMode.SUBTRACT) {
      ☃ |= 0x4;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, MODE, POWERED });
  }
  
  public static enum EnumComparatorMode
    implements INamable
  {
    private final String c;
    
    private EnumComparatorMode(String ☃)
    {
      this.c = ☃;
    }
    
    public String toString()
    {
      return this.c;
    }
    
    public String getName()
    {
      return this.c;
    }
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return getBlockData().set(FACING, ☃.getDirection().opposite()).set(POWERED, Boolean.valueOf(false)).set(MODE, EnumComparatorMode.COMPARE);
  }
}
