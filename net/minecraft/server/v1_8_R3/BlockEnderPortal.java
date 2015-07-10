package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.plugin.PluginManager;

public class BlockEnderPortal
  extends BlockContainer
{
  protected BlockEnderPortal(Material material)
  {
    super(material);
    a(1.0F);
  }
  
  public TileEntity a(World world, int i)
  {
    return new TileEntityEnderPortal();
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    float f = 0.0625F;
    
    a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity) {}
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public int a(Random random)
  {
    return 0;
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity)
  {
    if ((entity.vehicle == null) && (entity.passenger == null) && (!world.isClientSide))
    {
      EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ()));
      world.getServer().getPluginManager().callEvent(event);
      
      entity.c(1);
    }
  }
  
  public MaterialMapColor g(IBlockData iblockdata)
  {
    return MaterialMapColor.E;
  }
}
