package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockSnow
  extends Block
{
  public static final BlockStateInteger LAYERS = BlockStateInteger.of("layers", 1, 8);
  
  protected BlockSnow()
  {
    super(Material.PACKED_ICE);
    j(this.blockStateList.getBlockData().set(LAYERS, Integer.valueOf(1)));
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    a(true);
    a(CreativeModeTab.c);
    j();
  }
  
  public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    return ((Integer)iblockaccess.getType(blockposition).get(LAYERS)).intValue() < 5;
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    int i = ((Integer)iblockdata.get(LAYERS)).intValue() - 1;
    float f = 0.125F;
    
    return new AxisAlignedBB(blockposition.getX() + this.minX, blockposition.getY() + this.minY, blockposition.getZ() + this.minZ, blockposition.getX() + this.maxX, blockposition.getY() + i * f, blockposition.getZ() + this.maxZ);
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
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    
    b(((Integer)iblockdata.get(LAYERS)).intValue());
  }
  
  protected void b(int i)
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, i / 8.0F, 1.0F);
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    IBlockData iblockdata = world.getType(blockposition.down());
    Block block = iblockdata.getBlock();
    
    return block.getMaterial() == Material.LEAVES;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    e(world, blockposition, iblockdata);
  }
  
  private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (!canPlace(world, blockposition))
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
      return false;
    }
    return true;
  }
  
  public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, TileEntity tileentity)
  {
    a(world, blockposition, new ItemStack(Items.SNOWBALL, ((Integer)iblockdata.get(LAYERS)).intValue() + 1, 0));
    world.setAir(blockposition);
    entityhuman.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Items.SNOWBALL;
  }
  
  public int a(Random random)
  {
    return 0;
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    if (world.b(EnumSkyBlock.BLOCK, blockposition) > 11)
    {
      if (CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), Blocks.AIR).isCancelled()) {
        return;
      }
      b(world, blockposition, world.getType(blockposition), 0);
      world.setAir(blockposition);
    }
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(LAYERS, Integer.valueOf((i & 0x7) + 1));
  }
  
  public boolean a(World world, BlockPosition blockposition)
  {
    return ((Integer)world.getType(blockposition).get(LAYERS)).intValue() == 1;
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(LAYERS)).intValue() - 1;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { LAYERS });
  }
}
