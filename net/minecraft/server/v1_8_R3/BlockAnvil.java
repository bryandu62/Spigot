package net.minecraft.server.v1_8_R3;

public class BlockAnvil
  extends BlockFalling
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  public static final BlockStateInteger DAMAGE = BlockStateInteger.of("damage", 0, 2);
  
  protected BlockAnvil()
  {
    super(Material.HEAVY);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(DAMAGE, Integer.valueOf(0)));
    e(0);
    a(CreativeModeTab.c);
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    EnumDirection enumdirection1 = entityliving.getDirection().e();
    
    return super.getPlacedState(world, blockposition, enumdirection, f, f1, f2, i, entityliving).set(FACING, enumdirection1).set(DAMAGE, Integer.valueOf(i >> 2));
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (!world.isClientSide) {
      entityhuman.openTileEntity(new TileEntityContainerAnvil(world, blockposition));
    }
    return true;
  }
  
  public int getDropData(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(DAMAGE)).intValue();
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    EnumDirection enumdirection = (EnumDirection)iblockaccess.getType(blockposition).get(FACING);
    if (enumdirection.k() == EnumDirection.EnumAxis.X) {
      a(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
    } else {
      a(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
    }
  }
  
  protected void a(EntityFallingBlock entityfallingblock)
  {
    entityfallingblock.a(true);
  }
  
  public void a_(World world, BlockPosition blockposition)
  {
    world.triggerEffect(1022, blockposition, 0);
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(FACING, EnumDirection.fromType2(i & 0x3)).set(DAMAGE, Integer.valueOf((i & 0xF) >> 2));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | ((EnumDirection)iblockdata.get(FACING)).b();
    
    i |= ((Integer)iblockdata.get(DAMAGE)).intValue() << 2;
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, DAMAGE });
  }
  
  public static class TileEntityContainerAnvil
    implements ITileEntityContainer
  {
    private final World a;
    private final BlockPosition b;
    
    public TileEntityContainerAnvil(World world, BlockPosition blockposition)
    {
      this.a = world;
      this.b = blockposition;
    }
    
    public String getName()
    {
      return "anvil";
    }
    
    public boolean hasCustomName()
    {
      return false;
    }
    
    public IChatBaseComponent getScoreboardDisplayName()
    {
      return new ChatMessage(Blocks.ANVIL.a() + ".name", new Object[0]);
    }
    
    public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman)
    {
      return new ContainerAnvil(playerinventory, this.a, this.b, entityhuman);
    }
    
    public String getContainerName()
    {
      return "minecraft:anvil";
    }
  }
}
