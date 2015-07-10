package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockPressurePlateWeighted
  extends BlockPressurePlateAbstract
{
  public static final BlockStateInteger POWER = BlockStateInteger.of("power", 0, 15);
  private final int b;
  
  protected BlockPressurePlateWeighted(Material material, int i)
  {
    this(material, i, material.r());
  }
  
  protected BlockPressurePlateWeighted(Material material, int i, MaterialMapColor materialmapcolor)
  {
    super(material, materialmapcolor);
    j(this.blockStateList.getBlockData().set(POWER, Integer.valueOf(0)));
    this.b = i;
  }
  
  protected int f(World world, BlockPosition blockposition)
  {
    int i = 0;
    Iterator iterator = world.a(Entity.class, a(blockposition)).iterator();
    while (iterator.hasNext())
    {
      Entity entity = (Entity)iterator.next();
      Cancellable cancellable;
      Cancellable cancellable;
      if ((entity instanceof EntityHuman))
      {
        cancellable = CraftEventFactory.callPlayerInteractEvent((EntityHuman)entity, Action.PHYSICAL, blockposition, null, null);
      }
      else
      {
        cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
        world.getServer().getPluginManager().callEvent((EntityInteractEvent)cancellable);
      }
      if (!cancellable.isCancelled()) {
        i++;
      }
    }
    i = Math.min(i, this.b);
    if (i > 0)
    {
      float f = Math.min(this.b, i) / this.b;
      
      return MathHelper.f(f * 15.0F);
    }
    return 0;
  }
  
  protected int e(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(POWER)).intValue();
  }
  
  protected IBlockData a(IBlockData iblockdata, int i)
  {
    return iblockdata.set(POWER, Integer.valueOf(i));
  }
  
  public int a(World world)
  {
    return 10;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(POWER, Integer.valueOf(i));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(POWER)).intValue();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { POWER });
  }
}
