package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockBanner
  extends BlockContainer
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  public static final BlockStateInteger ROTATION = BlockStateInteger.of("rotation", 0, 15);
  
  protected BlockBanner()
  {
    super(Material.WOOD);
    float ☃ = 0.25F;
    float ☃ = 1.0F;
    a(0.5F - ☃, 0.0F, 0.5F - ☃, 0.5F + ☃, ☃, 0.5F + ☃);
  }
  
  public String getName()
  {
    return LocaleI18n.get("item.banner.white.name");
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return null;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean b(IBlockAccess ☃, BlockPosition ☃)
  {
    return true;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean g()
  {
    return true;
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityBanner();
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.BANNER;
  }
  
  public void dropNaturally(World ☃, BlockPosition ☃, IBlockData ☃, float ☃, int ☃)
  {
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityBanner))
    {
      ItemStack ☃ = new ItemStack(Items.BANNER, 1, ((TileEntityBanner)☃).b());
      
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.b(☃);
      ☃.remove("x");
      ☃.remove("y");
      ☃.remove("z");
      ☃.remove("id");
      ☃.a("BlockEntityTag", ☃);
      
      a(☃, ☃, ☃);
    }
    else
    {
      super.dropNaturally(☃, ☃, ☃, ☃, ☃);
    }
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    return (!e(☃, ☃)) && (super.canPlace(☃, ☃));
  }
  
  public void a(World ☃, EntityHuman ☃, BlockPosition ☃, IBlockData ☃, TileEntity ☃)
  {
    if ((☃ instanceof TileEntityBanner))
    {
      TileEntityBanner ☃ = (TileEntityBanner)☃;
      ItemStack ☃ = new ItemStack(Items.BANNER, 1, ((TileEntityBanner)☃).b());
      
      NBTTagCompound ☃ = new NBTTagCompound();
      TileEntityBanner.a(☃, ☃.b(), ☃.d());
      ☃.a("BlockEntityTag", ☃);
      
      a(☃, ☃, ☃);
    }
    else
    {
      super.a(☃, ☃, ☃, ☃, null);
    }
  }
  
  public static class BlockWallBanner
    extends BlockBanner
  {
    public BlockWallBanner()
    {
      j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
    }
    
    public void updateShape(IBlockAccess ☃, BlockPosition ☃)
    {
      EnumDirection ☃ = (EnumDirection)☃.getType(☃).get(FACING);
      
      float ☃ = 0.0F;
      float ☃ = 0.78125F;
      float ☃ = 0.0F;
      float ☃ = 1.0F;
      
      float ☃ = 0.125F;
      
      a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      switch (BlockBanner.1.a[☃.ordinal()])
      {
      case 1: 
      default: 
        a(☃, ☃, 1.0F - ☃, ☃, ☃, 1.0F);
        break;
      case 2: 
        a(☃, ☃, 0.0F, ☃, ☃, ☃);
        break;
      case 3: 
        a(1.0F - ☃, ☃, ☃, 1.0F, ☃, ☃);
        break;
      case 4: 
        a(0.0F, ☃, ☃, ☃, ☃, ☃);
      }
    }
    
    public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
    {
      EnumDirection ☃ = (EnumDirection)☃.get(FACING);
      if (!☃.getType(☃.shift(☃.opposite())).getBlock().getMaterial().isBuildable())
      {
        b(☃, ☃, ☃, 0);
        ☃.setAir(☃);
      }
      super.doPhysics(☃, ☃, ☃, ☃);
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
  
  public static class BlockStandingBanner
    extends BlockBanner
  {
    public BlockStandingBanner()
    {
      j(this.blockStateList.getBlockData().set(ROTATION, Integer.valueOf(0)));
    }
    
    public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
    {
      if (!☃.getType(☃.down()).getBlock().getMaterial().isBuildable())
      {
        b(☃, ☃, ☃, 0);
        ☃.setAir(☃);
      }
      super.doPhysics(☃, ☃, ☃, ☃);
    }
    
    public IBlockData fromLegacyData(int ☃)
    {
      return getBlockData().set(ROTATION, Integer.valueOf(☃));
    }
    
    public int toLegacyData(IBlockData ☃)
    {
      return ((Integer)☃.get(ROTATION)).intValue();
    }
    
    protected BlockStateList getStateList()
    {
      return new BlockStateList(this, new IBlockState[] { ROTATION });
    }
  }
}
