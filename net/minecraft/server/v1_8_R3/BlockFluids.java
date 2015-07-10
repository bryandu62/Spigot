package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class BlockFluids
  extends Block
{
  public static final BlockStateInteger LEVEL = BlockStateInteger.of("level", 0, 15);
  
  protected BlockFluids(Material ☃)
  {
    super(☃);
    j(this.blockStateList.getBlockData().set(LEVEL, Integer.valueOf(0)));
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    a(true);
  }
  
  public boolean b(IBlockAccess ☃, BlockPosition ☃)
  {
    return this.material != Material.LAVA;
  }
  
  public static float b(int ☃)
  {
    if (☃ >= 8) {
      ☃ = 0;
    }
    return (☃ + 1) / 9.0F;
  }
  
  protected int e(IBlockAccess ☃, BlockPosition ☃)
  {
    if (☃.getType(☃).getBlock().getMaterial() == this.material) {
      return ((Integer)☃.getType(☃).get(LEVEL)).intValue();
    }
    return -1;
  }
  
  protected int f(IBlockAccess ☃, BlockPosition ☃)
  {
    int ☃ = e(☃, ☃);
    
    return ☃ >= 8 ? 0 : ☃;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean a(IBlockData ☃, boolean ☃)
  {
    return (☃) && (((Integer)☃.get(LEVEL)).intValue() == 0);
  }
  
  public boolean b(IBlockAccess ☃, BlockPosition ☃, EnumDirection ☃)
  {
    Material ☃ = ☃.getType(☃).getBlock().getMaterial();
    if (☃ == this.material) {
      return false;
    }
    if (☃ == EnumDirection.UP) {
      return true;
    }
    if (☃ == Material.ICE) {
      return false;
    }
    return super.b(☃, ☃, ☃);
  }
  
  public AxisAlignedBB a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    return null;
  }
  
  public int b()
  {
    return 1;
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return null;
  }
  
  public int a(Random ☃)
  {
    return 0;
  }
  
  protected Vec3D h(IBlockAccess ☃, BlockPosition ☃)
  {
    Vec3D ☃ = new Vec3D(0.0D, 0.0D, 0.0D);
    int ☃ = f(☃, ☃);
    for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL)
    {
      BlockPosition ☃ = ☃.shift(☃);
      
      int ☃ = f(☃, ☃);
      if (☃ < 0)
      {
        if (!☃.getType(☃).getBlock().getMaterial().isSolid())
        {
          ☃ = f(☃, ☃.down());
          if (☃ >= 0)
          {
            int ☃ = ☃ - (☃ - 8);
            ☃ = ☃.add((☃.getX() - ☃.getX()) * ☃, (☃.getY() - ☃.getY()) * ☃, (☃.getZ() - ☃.getZ()) * ☃);
          }
        }
      }
      else if (☃ >= 0)
      {
        int ☃ = ☃ - ☃;
        ☃ = ☃.add((☃.getX() - ☃.getX()) * ☃, (☃.getY() - ☃.getY()) * ☃, (☃.getZ() - ☃.getZ()) * ☃);
      }
    }
    if (((Integer)☃.getType(☃).get(LEVEL)).intValue() >= 8) {
      for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL)
      {
        BlockPosition ☃ = ☃.shift(☃);
        if ((b(☃, ☃, ☃)) || (b(☃, ☃.up(), ☃)))
        {
          ☃ = ☃.a().add(0.0D, -6.0D, 0.0D);
          break;
        }
      }
    }
    return ☃.a();
  }
  
  public Vec3D a(World ☃, BlockPosition ☃, Entity ☃, Vec3D ☃)
  {
    return ☃.e(h(☃, ☃));
  }
  
  public int a(World ☃)
  {
    if (this.material == Material.WATER) {
      return 5;
    }
    if (this.material == Material.LAVA) {
      return ☃.worldProvider.o() ? 10 : 30;
    }
    return 0;
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    e(☃, ☃, ☃);
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    e(☃, ☃, ☃);
  }
  
  public boolean e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (this.material == Material.LAVA)
    {
      boolean ☃ = false;
      for (EnumDirection ☃ : EnumDirection.values()) {
        if ((☃ != EnumDirection.DOWN) && (☃.getType(☃.shift(☃)).getBlock().getMaterial() == Material.WATER))
        {
          ☃ = true;
          break;
        }
      }
      if (☃)
      {
        Integer ☃ = (Integer)☃.get(LEVEL);
        if (☃.intValue() == 0)
        {
          ☃.setTypeUpdate(☃, Blocks.OBSIDIAN.getBlockData());
          fizz(☃, ☃);
          return true;
        }
        if (☃.intValue() <= 4)
        {
          ☃.setTypeUpdate(☃, Blocks.COBBLESTONE.getBlockData());
          fizz(☃, ☃);
          return true;
        }
      }
    }
    return false;
  }
  
  protected void fizz(World ☃, BlockPosition ☃)
  {
    double ☃ = ☃.getX();
    double ☃ = ☃.getY();
    double ☃ = ☃.getZ();
    
    ☃.makeSound(☃ + 0.5D, ☃ + 0.5D, ☃ + 0.5D, "random.fizz", 0.5F, 2.6F + (☃.random.nextFloat() - ☃.random.nextFloat()) * 0.8F);
    for (int ☃ = 0; ☃ < 8; ☃++) {
      ☃.addParticle(EnumParticle.SMOKE_LARGE, ☃ + Math.random(), ☃ + 1.2D, ☃ + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
    }
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(LEVEL, Integer.valueOf(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((Integer)☃.get(LEVEL)).intValue();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { LEVEL });
  }
  
  public static BlockFlowing a(Material ☃)
  {
    if (☃ == Material.WATER) {
      return Blocks.FLOWING_WATER;
    }
    if (☃ == Material.LAVA) {
      return Blocks.FLOWING_LAVA;
    }
    throw new IllegalArgumentException("Invalid material");
  }
  
  public static BlockStationary b(Material ☃)
  {
    if (☃ == Material.WATER) {
      return Blocks.WATER;
    }
    if (☃ == Material.LAVA) {
      return Blocks.LAVA;
    }
    throw new IllegalArgumentException("Invalid material");
  }
}
