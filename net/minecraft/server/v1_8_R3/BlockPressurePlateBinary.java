package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockPressurePlateBinary
  extends BlockPressurePlateAbstract
{
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  private final EnumMobType b;
  
  protected BlockPressurePlateBinary(Material material, EnumMobType blockpressureplatebinary_enummobtype)
  {
    super(material);
    j(this.blockStateList.getBlockData().set(POWERED, Boolean.valueOf(false)));
    this.b = blockpressureplatebinary_enummobtype;
  }
  
  protected int e(IBlockData iblockdata)
  {
    return ((Boolean)iblockdata.get(POWERED)).booleanValue() ? 15 : 0;
  }
  
  protected IBlockData a(IBlockData iblockdata, int i)
  {
    return iblockdata.set(POWERED, Boolean.valueOf(i > 0));
  }
  
  protected int f(World world, BlockPosition blockposition)
  {
    AxisAlignedBB axisalignedbb = a(blockposition);
    List list;
    List list;
    switch (SyntheticClass_1.a[this.b.ordinal()])
    {
    case 1: 
      list = world.getEntities(null, axisalignedbb);
      break;
    case 2: 
      list = world.a(EntityLiving.class, axisalignedbb);
      break;
    default: 
      return 0;
    }
    List list;
    if (!list.isEmpty())
    {
      Iterator iterator = list.iterator();
      while (iterator.hasNext())
      {
        Entity entity = (Entity)iterator.next();
        if (e(world.getType(blockposition)) == 0)
        {
          org.bukkit.World bworld = world.getWorld();
          PluginManager manager = world.getServer().getPluginManager();
          Cancellable cancellable;
          Cancellable cancellable;
          if ((entity instanceof EntityHuman))
          {
            cancellable = CraftEventFactory.callPlayerInteractEvent((EntityHuman)entity, Action.PHYSICAL, blockposition, null, null);
          }
          else
          {
            cancellable = new EntityInteractEvent(entity.getBukkitEntity(), bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
            manager.callEvent((EntityInteractEvent)cancellable);
          }
          if (cancellable.isCancelled()) {}
        }
        else if (!entity.aI())
        {
          return 15;
        }
      }
    }
    return 0;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(POWERED, Boolean.valueOf(i == 1));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Boolean)iblockdata.get(POWERED)).booleanValue() ? 1 : 0;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { POWERED });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[BlockPressurePlateBinary.EnumMobType.values().length];
    
    static
    {
      try
      {
        a[BlockPressurePlateBinary.EnumMobType.EVERYTHING.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[BlockPressurePlateBinary.EnumMobType.MOBS.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
    }
  }
  
  public static enum EnumMobType
  {
    EVERYTHING,  MOBS;
  }
}
