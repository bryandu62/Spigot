package net.minecraft.server.v1_8_R3;

public class BlockChest
  extends BlockContainer
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  public final int b;
  
  protected BlockChest(int ☃)
  {
    super(Material.WOOD);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
    this.b = ☃;
    a(CreativeModeTab.c);
    a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public int b()
  {
    return 2;
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    if (☃.getType(☃.north()).getBlock() == this) {
      a(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
    } else if (☃.getType(☃.south()).getBlock() == this) {
      a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
    } else if (☃.getType(☃.west()).getBlock() == this) {
      a(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    } else if (☃.getType(☃.east()).getBlock() == this) {
      a(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
    } else {
      a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    e(☃, ☃, ☃);
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL)
    {
      BlockPosition ☃ = ☃.shift(☃);
      IBlockData ☃ = ☃.getType(☃);
      if (☃.getBlock() == this) {
        e(☃, ☃, ☃);
      }
    }
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return getBlockData().set(FACING, ☃.getDirection());
  }
  
  public void postPlace(World ☃, BlockPosition ☃, IBlockData ☃, EntityLiving ☃, ItemStack ☃)
  {
    EnumDirection ☃ = EnumDirection.fromType2(MathHelper.floor(☃.yaw * 4.0F / 360.0F + 0.5D) & 0x3).opposite();
    ☃ = ☃.set(FACING, ☃);
    
    BlockPosition ☃ = ☃.north();
    BlockPosition ☃ = ☃.south();
    BlockPosition ☃ = ☃.west();
    BlockPosition ☃ = ☃.east();
    
    boolean ☃ = this == ☃.getType(☃).getBlock();
    boolean ☃ = this == ☃.getType(☃).getBlock();
    boolean ☃ = this == ☃.getType(☃).getBlock();
    boolean ☃ = this == ☃.getType(☃).getBlock();
    if ((☃) || (☃) || (☃) || (☃))
    {
      if ((☃.k() == EnumDirection.EnumAxis.X) && ((☃) || (☃)))
      {
        if (☃) {
          ☃.setTypeAndData(☃, ☃, 3);
        } else {
          ☃.setTypeAndData(☃, ☃, 3);
        }
        ☃.setTypeAndData(☃, ☃, 3);
      }
      else if ((☃.k() == EnumDirection.EnumAxis.Z) && ((☃) || (☃)))
      {
        if (☃) {
          ☃.setTypeAndData(☃, ☃, 3);
        } else {
          ☃.setTypeAndData(☃, ☃, 3);
        }
        ☃.setTypeAndData(☃, ☃, 3);
      }
    }
    else {
      ☃.setTypeAndData(☃, ☃, 3);
    }
    if (☃.hasName())
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ instanceof TileEntityChest)) {
        ((TileEntityChest)☃).a(☃.getName());
      }
    }
  }
  
  public IBlockData e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (☃.isClientSide) {
      return ☃;
    }
    IBlockData ☃ = ☃.getType(☃.north());
    IBlockData ☃ = ☃.getType(☃.south());
    IBlockData ☃ = ☃.getType(☃.west());
    IBlockData ☃ = ☃.getType(☃.east());
    
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    
    Block ☃ = ☃.getBlock();
    Block ☃ = ☃.getBlock();
    Block ☃ = ☃.getBlock();
    Block ☃ = ☃.getBlock();
    if ((☃ == this) || (☃ == this))
    {
      BlockPosition ☃ = ☃ == this ? ☃.north() : ☃.south();
      IBlockData ☃ = ☃.getType(☃.west());
      IBlockData ☃ = ☃.getType(☃.east());
      
      ☃ = EnumDirection.EAST;
      EnumDirection ☃;
      EnumDirection ☃;
      if (☃ == this) {
        ☃ = (EnumDirection)☃.get(FACING);
      } else {
        ☃ = (EnumDirection)☃.get(FACING);
      }
      if (☃ == EnumDirection.WEST) {
        ☃ = EnumDirection.WEST;
      }
      Block ☃ = ☃.getBlock();
      Block ☃ = ☃.getBlock();
      if (((☃.o()) || (☃.o())) && (!☃.o()) && (!☃.o())) {
        ☃ = EnumDirection.EAST;
      }
      if (((☃.o()) || (☃.o())) && (!☃.o()) && (!☃.o())) {
        ☃ = EnumDirection.WEST;
      }
    }
    else
    {
      boolean ☃ = ☃.o();
      boolean ☃ = ☃.o();
      if ((☃ == this) || (☃ == this))
      {
        BlockPosition ☃ = ☃ == this ? ☃.west() : ☃.east();
        IBlockData ☃ = ☃.getType(☃.north());
        IBlockData ☃ = ☃.getType(☃.south());
        
        ☃ = EnumDirection.SOUTH;
        EnumDirection ☃;
        EnumDirection ☃;
        if (☃ == this) {
          ☃ = (EnumDirection)☃.get(FACING);
        } else {
          ☃ = (EnumDirection)☃.get(FACING);
        }
        if (☃ == EnumDirection.NORTH) {
          ☃ = EnumDirection.NORTH;
        }
        Block ☃ = ☃.getBlock();
        Block ☃ = ☃.getBlock();
        if (((☃) || (☃.o())) && (!☃) && (!☃.o())) {
          ☃ = EnumDirection.SOUTH;
        }
        if (((☃) || (☃.o())) && (!☃) && (!☃.o())) {
          ☃ = EnumDirection.NORTH;
        }
      }
    }
    ☃ = ☃.set(FACING, ☃);
    ☃.setTypeAndData(☃, ☃, 3);
    return ☃;
  }
  
  public IBlockData f(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    EnumDirection ☃ = null;
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL)
    {
      IBlockData ☃ = ☃.getType(☃.shift(☃));
      if (☃.getBlock() == this) {
        return ☃;
      }
      if (☃.getBlock().o()) {
        if (☃ == null)
        {
          ☃ = ☃;
        }
        else
        {
          ☃ = null;
          break;
        }
      }
    }
    if (☃ != null) {
      return ☃.set(FACING, ☃.opposite());
    }
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    if (☃.getType(☃.shift(☃)).getBlock().o()) {
      ☃ = ☃.opposite();
    }
    if (☃.getType(☃.shift(☃)).getBlock().o()) {
      ☃ = ☃.e();
    }
    if (☃.getType(☃.shift(☃)).getBlock().o()) {
      ☃ = ☃.opposite();
    }
    return ☃.set(FACING, ☃);
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    int ☃ = 0;
    
    BlockPosition ☃ = ☃.west();
    BlockPosition ☃ = ☃.east();
    BlockPosition ☃ = ☃.north();
    BlockPosition ☃ = ☃.south();
    if (☃.getType(☃).getBlock() == this)
    {
      if (m(☃, ☃)) {
        return false;
      }
      ☃++;
    }
    if (☃.getType(☃).getBlock() == this)
    {
      if (m(☃, ☃)) {
        return false;
      }
      ☃++;
    }
    if (☃.getType(☃).getBlock() == this)
    {
      if (m(☃, ☃)) {
        return false;
      }
      ☃++;
    }
    if (☃.getType(☃).getBlock() == this)
    {
      if (m(☃, ☃)) {
        return false;
      }
      ☃++;
    }
    if (☃ > 1) {
      return false;
    }
    return true;
  }
  
  private boolean m(World ☃, BlockPosition ☃)
  {
    if (☃.getType(☃).getBlock() != this) {
      return false;
    }
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
      if (☃.getType(☃.shift(☃)).getBlock() == this) {
        return true;
      }
    }
    return false;
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    super.doPhysics(☃, ☃, ☃, ☃);
    
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityChest)) {
      ☃.E();
    }
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof IInventory))
    {
      InventoryUtils.dropInventory(☃, ☃, (IInventory)☃);
      
      ☃.updateAdjacentComparators(☃, this);
    }
    super.remove(☃, ☃, ☃);
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    ITileInventory ☃ = f(☃, ☃);
    if (☃ != null)
    {
      ☃.openContainer(☃);
      if (this.b == 0) {
        ☃.b(StatisticList.aa);
      } else if (this.b == 1) {
        ☃.b(StatisticList.U);
      }
    }
    return true;
  }
  
  public ITileInventory f(World ☃, BlockPosition ☃)
  {
    TileEntity ☃ = ☃.getTileEntity(☃);
    if (!(☃ instanceof TileEntityChest)) {
      return null;
    }
    ITileInventory ☃ = (TileEntityChest)☃;
    if (n(☃, ☃)) {
      return null;
    }
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL)
    {
      BlockPosition ☃ = ☃.shift(☃);
      Block ☃ = ☃.getType(☃).getBlock();
      if (☃ == this)
      {
        if (n(☃, ☃)) {
          return null;
        }
        TileEntity ☃ = ☃.getTileEntity(☃);
        if ((☃ instanceof TileEntityChest)) {
          if ((☃ == EnumDirection.WEST) || (☃ == EnumDirection.NORTH)) {
            ☃ = new InventoryLargeChest("container.chestDouble", (TileEntityChest)☃, ☃);
          } else {
            ☃ = new InventoryLargeChest("container.chestDouble", ☃, (TileEntityChest)☃);
          }
        }
      }
    }
    return ☃;
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityChest();
  }
  
  public boolean isPowerSource()
  {
    return this.b == 1;
  }
  
  public int a(IBlockAccess ☃, BlockPosition ☃, IBlockData ☃, EnumDirection ☃)
  {
    if (!isPowerSource()) {
      return 0;
    }
    int ☃ = 0;
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityChest)) {
      ☃ = ((TileEntityChest)☃).l;
    }
    return MathHelper.clamp(☃, 0, 15);
  }
  
  public int b(IBlockAccess ☃, BlockPosition ☃, IBlockData ☃, EnumDirection ☃)
  {
    if (☃ == EnumDirection.UP) {
      return a(☃, ☃, ☃, ☃);
    }
    return 0;
  }
  
  private boolean n(World ☃, BlockPosition ☃)
  {
    return (o(☃, ☃)) || (p(☃, ☃));
  }
  
  private boolean o(World ☃, BlockPosition ☃)
  {
    return ☃.getType(☃.up()).getBlock().isOccluding();
  }
  
  private boolean p(World ☃, BlockPosition ☃)
  {
    for (Entity ☃ : ☃.a(EntityOcelot.class, new AxisAlignedBB(☃.getX(), ☃.getY() + 1, ☃.getZ(), ☃.getX() + 1, ☃.getY() + 2, ☃.getZ() + 1)))
    {
      EntityOcelot ☃ = (EntityOcelot)☃;
      if (☃.isSitting()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isComplexRedstone()
  {
    return true;
  }
  
  public int l(World ☃, BlockPosition ☃)
  {
    return Container.b(f(☃, ☃));
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    EnumDirection ☃ = EnumDirection.fromType1(☃);
    if (☃.k() == EnumDirection.EnumAxis.Y) {
      ☃ = EnumDirection.NORTH;
    }
    return getBlockData().set(FACING, ☃);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumDirection)☃.get(FACING)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING });
  }
}
