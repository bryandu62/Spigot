package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public abstract class BlockPressurePlateAbstract
  extends Block
{
  protected BlockPressurePlateAbstract(Material material)
  {
    this(material, material.r());
  }
  
  protected BlockPressurePlateAbstract(Material material, MaterialMapColor materialmapcolor)
  {
    super(material, materialmapcolor);
    a(CreativeModeTab.d);
    a(true);
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    d(iblockaccess.getType(blockposition));
  }
  
  protected void d(IBlockData iblockdata)
  {
    boolean flag = e(iblockdata) > 0;
    if (flag) {
      a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.03125F, 0.9375F);
    } else {
      a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
    }
  }
  
  public int a(World world)
  {
    return 20;
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return true;
  }
  
  public boolean g()
  {
    return true;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return m(world, blockposition.down());
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!m(world, blockposition.down()))
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
    }
  }
  
  private boolean m(World world, BlockPosition blockposition)
  {
    return (World.a(world, blockposition)) || ((world.getType(blockposition).getBlock() instanceof BlockFence));
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (!world.isClientSide)
    {
      int i = e(iblockdata);
      if (i > 0) {
        a(world, blockposition, iblockdata, i);
      }
    }
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity)
  {
    if (!world.isClientSide)
    {
      int i = e(iblockdata);
      if (i == 0) {
        a(world, blockposition, iblockdata, i);
      }
    }
  }
  
  protected void a(World world, BlockPosition blockposition, IBlockData iblockdata, int i)
  {
    int j = f(world, blockposition);
    boolean flag = i > 0;
    boolean flag1 = j > 0;
    
    org.bukkit.World bworld = world.getWorld();
    PluginManager manager = world.getServer().getPluginManager();
    if (flag != flag1)
    {
      BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), i, j);
      manager.callEvent(eventRedstone);
      
      flag1 = eventRedstone.getNewCurrent() > 0;
      j = eventRedstone.getNewCurrent();
    }
    if (i != j)
    {
      iblockdata = a(iblockdata, j);
      world.setTypeAndData(blockposition, iblockdata, 2);
      e(world, blockposition);
      world.b(blockposition, blockposition);
    }
    if ((!flag1) && (flag)) {
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.1D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
    } else if ((flag1) && (!flag)) {
      world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.1D, blockposition.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
    }
    if (flag1) {
      world.a(blockposition, this, a(world));
    }
  }
  
  protected AxisAlignedBB a(BlockPosition blockposition)
  {
    return new AxisAlignedBB(blockposition.getX() + 0.125F, blockposition.getY(), blockposition.getZ() + 0.125F, blockposition.getX() + 1 - 0.125F, blockposition.getY() + 0.25D, blockposition.getZ() + 1 - 0.125F);
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (e(iblockdata) > 0) {
      e(world, blockposition);
    }
    super.remove(world, blockposition, iblockdata);
  }
  
  protected void e(World world, BlockPosition blockposition)
  {
    world.applyPhysics(blockposition, this);
    world.applyPhysics(blockposition.down(), this);
  }
  
  public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return e(iblockdata);
  }
  
  public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return enumdirection == EnumDirection.UP ? e(iblockdata) : 0;
  }
  
  public boolean isPowerSource()
  {
    return true;
  }
  
  public void j()
  {
    a(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
  }
  
  public int k()
  {
    return 1;
  }
  
  protected abstract int f(World paramWorld, BlockPosition paramBlockPosition);
  
  protected abstract int e(IBlockData paramIBlockData);
  
  protected abstract IBlockData a(IBlockData paramIBlockData, int paramInt);
}
