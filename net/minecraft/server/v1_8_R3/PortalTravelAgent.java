package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

public class PortalTravelAgent
{
  private final WorldServer a;
  private final Random b;
  private final LongHashMap<ChunkCoordinatesPortal> c = new LongHashMap();
  private final List<Long> d = Lists.newArrayList();
  
  public PortalTravelAgent(WorldServer worldserver)
  {
    this.a = worldserver;
    this.b = new Random(worldserver.getSeed());
  }
  
  public void a(Entity entity, float f)
  {
    if (this.a.worldProvider.getDimension() != 1)
    {
      if (!b(entity, f))
      {
        a(entity);
        b(entity, f);
      }
    }
    else
    {
      MathHelper.floor(entity.locX);
      MathHelper.floor(entity.locY);
      MathHelper.floor(entity.locZ);
      
      BlockPosition created = createEndPortal(entity.locX, entity.locY, entity.locZ);
      entity.setPositionRotation(created.getX(), created.getY(), created.getZ(), entity.yaw, 0.0F);
      entity.motX = (entity.motY = entity.motZ = 0.0D);
    }
  }
  
  private BlockPosition createEndPortal(double x, double y, double z)
  {
    int i = MathHelper.floor(x);
    int j = MathHelper.floor(y) - 1;
    int k = MathHelper.floor(z);
    
    byte b0 = 1;
    byte b1 = 0;
    for (int l = -2; l <= 2; l++) {
      for (int i1 = -2; i1 <= 2; i1++) {
        for (int j1 = -1; j1 < 3; j1++)
        {
          int k1 = i + i1 * b0 + l * b1;
          int l1 = j + j1;
          int i2 = k + i1 * b1 - l * b0;
          boolean flag = j1 < 0;
          
          this.a.setTypeUpdate(new BlockPosition(k1, l1, i2), flag ? Blocks.OBSIDIAN.getBlockData() : Blocks.AIR.getBlockData());
        }
      }
    }
    return new BlockPosition(i, k, k);
  }
  
  private BlockPosition findEndPortal(BlockPosition portal)
  {
    int i = portal.getX();
    int j = portal.getY() - 1;
    int k = portal.getZ();
    byte b0 = 1;
    byte b1 = 0;
    for (int l = -2; l <= 2; l++) {
      for (int i1 = -2; i1 <= 2; i1++) {
        for (int j1 = -1; j1 < 3; j1++)
        {
          int k1 = i + i1 * b0 + l * b1;
          int l1 = j + j1;
          int i2 = k + i1 * b1 - l * b0;
          boolean flag = j1 < 0;
          if (this.a.getType(new BlockPosition(k1, l1, i2)).getBlock() != (flag ? Blocks.OBSIDIAN : Blocks.AIR)) {
            return null;
          }
        }
      }
    }
    return new BlockPosition(i, j, k);
  }
  
  public boolean b(Entity entity, float f)
  {
    BlockPosition found = findPortal(entity.locX, entity.locY, entity.locZ, 128);
    if (found == null) {
      return false;
    }
    Location exit = new Location(this.a.getWorld(), found.getX(), found.getY(), found.getZ(), f, entity.pitch);
    Vector velocity = entity.getBukkitEntity().getVelocity();
    adjustExit(entity, exit, velocity);
    entity.setPositionRotation(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
    if ((entity.motX != velocity.getX()) || (entity.motY != velocity.getY()) || (entity.motZ != velocity.getZ())) {
      entity.getBukkitEntity().setVelocity(velocity);
    }
    return true;
  }
  
  public BlockPosition findPortal(double x, double y, double z, int short1)
  {
    if (this.a.getWorld().getEnvironment() == World.Environment.THE_END) {
      return findEndPortal(this.a.worldProvider.h());
    }
    double d0 = -1.0D;
    
    int i = MathHelper.floor(x);
    int j = MathHelper.floor(z);
    
    boolean flag1 = true;
    Object object = BlockPosition.ZERO;
    long k = ChunkCoordIntPair.a(i, j);
    if (this.c.contains(k))
    {
      ChunkCoordinatesPortal portaltravelagent_chunkcoordinatesportal = (ChunkCoordinatesPortal)this.c.getEntry(k);
      
      d0 = 0.0D;
      object = portaltravelagent_chunkcoordinatesportal;
      portaltravelagent_chunkcoordinatesportal.c = this.a.getTime();
      flag1 = false;
    }
    else
    {
      BlockPosition blockposition = new BlockPosition(x, y, z);
      for (int l = -128; l <= 128; l++) {
        for (int i1 = -128; i1 <= 128; i1++)
        {
          BlockPosition blockposition1;
          for (BlockPosition blockposition2 = blockposition.a(l, this.a.V() - 1 - blockposition.getY(), i1); blockposition2.getY() >= 0; blockposition2 = blockposition1)
          {
            blockposition1 = blockposition2.down();
            if (this.a.getType(blockposition2).getBlock() == Blocks.PORTAL)
            {
              while (this.a.getType(blockposition1 = blockposition2.down()).getBlock() == Blocks.PORTAL) {
                blockposition2 = blockposition1;
              }
              double d1 = blockposition2.i(blockposition);
              if ((d0 < 0.0D) || (d1 < d0))
              {
                d0 = d1;
                object = blockposition2;
              }
            }
          }
        }
      }
    }
    if (d0 >= 0.0D)
    {
      if (flag1)
      {
        this.c.put(k, new ChunkCoordinatesPortal((BlockPosition)object, this.a.getTime()));
        this.d.add(Long.valueOf(k));
      }
      return (BlockPosition)object;
    }
    return null;
  }
  
  public void adjustExit(Entity entity, Location position, Vector velocity)
  {
    Location from = position.clone();
    Vector before = velocity.clone();
    BlockPosition object = new BlockPosition(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    float f = position.getYaw();
    if ((this.a.getWorld().getEnvironment() == World.Environment.THE_END) || (entity.getBukkitEntity().getWorld().getEnvironment() == World.Environment.THE_END) || (entity.aG() == null))
    {
      position.setPitch(0.0F);
      velocity.setX(0);
      velocity.setY(0);
      velocity.setZ(0);
    }
    else
    {
      double d2 = object.getX() + 0.5D;
      double d3 = object.getY() + 0.5D;
      double d4 = object.getZ() + 0.5D;
      ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = Blocks.PORTAL.f(this.a, object);
      boolean flag2 = shapedetector_shapedetectorcollection.b().e().c() == EnumDirection.EnumAxisDirection.NEGATIVE;
      double d5 = shapedetector_shapedetectorcollection.b().k() == EnumDirection.EnumAxis.X ? shapedetector_shapedetectorcollection.a().getZ() : shapedetector_shapedetectorcollection.a().getX();
      
      d3 = shapedetector_shapedetectorcollection.a().getY() + 1 - entity.aG().b * shapedetector_shapedetectorcollection.e();
      if (flag2) {
        d5 += 1.0D;
      }
      if (shapedetector_shapedetectorcollection.b().k() == EnumDirection.EnumAxis.X) {
        d4 = d5 + (1.0D - entity.aG().a) * shapedetector_shapedetectorcollection.d() * shapedetector_shapedetectorcollection.b().e().c().a();
      } else {
        d2 = d5 + (1.0D - entity.aG().a) * shapedetector_shapedetectorcollection.d() * shapedetector_shapedetectorcollection.b().e().c().a();
      }
      float f1 = 0.0F;
      float f2 = 0.0F;
      float f3 = 0.0F;
      float f4 = 0.0F;
      if (shapedetector_shapedetectorcollection.b().opposite() == entity.aH())
      {
        f1 = 1.0F;
        f2 = 1.0F;
      }
      else if (shapedetector_shapedetectorcollection.b().opposite() == entity.aH().opposite())
      {
        f1 = -1.0F;
        f2 = -1.0F;
      }
      else if (shapedetector_shapedetectorcollection.b().opposite() == entity.aH().e())
      {
        f3 = 1.0F;
        f4 = -1.0F;
      }
      else
      {
        f3 = -1.0F;
        f4 = 1.0F;
      }
      double d6 = velocity.getX();
      double d7 = velocity.getZ();
      
      velocity.setX(d6 * f1 + d7 * f4);
      velocity.setZ(d6 * f3 + d7 * f2);
      f = f - entity.aH().opposite().b() * 90 + shapedetector_shapedetectorcollection.b().b() * 90;
      
      position.setX(d2);
      position.setY(d3);
      position.setZ(d4);
      position.setYaw(f);
    }
    EntityPortalExitEvent event = new EntityPortalExitEvent(entity.getBukkitEntity(), from, position, before, velocity);
    this.a.getServer().getPluginManager().callEvent(event);
    Location to = event.getTo();
    if ((event.isCancelled()) || (to == null) || (!entity.isAlive()))
    {
      position.setX(from.getX());
      position.setY(from.getY());
      position.setZ(from.getZ());
      position.setYaw(from.getYaw());
      position.setPitch(from.getPitch());
      velocity.copy(before);
    }
    else
    {
      position.setX(to.getX());
      position.setY(to.getY());
      position.setZ(to.getZ());
      position.setYaw(to.getYaw());
      position.setPitch(to.getPitch());
      velocity.copy(event.getAfter());
    }
  }
  
  public boolean a(Entity entity)
  {
    return createPortal(entity.locX, entity.locY, entity.locZ, 16);
  }
  
  public boolean createPortal(double x, double y, double z, int b0)
  {
    if (this.a.getWorld().getEnvironment() == World.Environment.THE_END)
    {
      createEndPortal(x, y, z);
      return true;
    }
    double d0 = -1.0D;
    
    int i = MathHelper.floor(x);
    int j = MathHelper.floor(y);
    int k = MathHelper.floor(z);
    
    int l = i;
    int i1 = j;
    int j1 = k;
    int k1 = 0;
    int l1 = this.b.nextInt(4);
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int i2 = i - b0; i2 <= i + b0; i2++)
    {
      double d1 = i2 + 0.5D - x;
      for (int j2 = k - b0; j2 <= k + b0; j2++)
      {
        double d2 = j2 + 0.5D - z;
        for (int k2 = this.a.V() - 1; k2 >= 0; k2--) {
          if (this.a.isEmpty(blockposition_mutableblockposition.c(i2, k2, j2)))
          {
            while ((k2 > 0) && (this.a.isEmpty(blockposition_mutableblockposition.c(i2, k2 - 1, j2)))) {
              k2--;
            }
            for (int l2 = l1; l2 < l1 + 4; l2++)
            {
              int i3 = l2 % 2;
              int j3 = 1 - i3;
              if (l2 % 4 >= 2)
              {
                i3 = -i3;
                j3 = -j3;
              }
              for (int k3 = 0; k3 < 3; k3++) {
                for (int l3 = 0; l3 < 4; l3++) {
                  for (int i4 = -1; i4 < 4; i4++)
                  {
                    int j4 = i2 + (l3 - 1) * i3 + k3 * j3;
                    int k4 = k2 + i4;
                    int l4 = j2 + (l3 - 1) * j3 - k3 * i3;
                    
                    blockposition_mutableblockposition.c(j4, k4, l4);
                    if (((i4 < 0) && (!this.a.getType(blockposition_mutableblockposition).getBlock().getMaterial().isBuildable())) || ((i4 >= 0) && (!this.a.isEmpty(blockposition_mutableblockposition)))) {
                      break;
                    }
                  }
                }
              }
              double d3 = k2 + 0.5D - y;
              double d4 = d1 * d1 + d3 * d3 + d2 * d2;
              if ((d0 < 0.0D) || (d4 < d0))
              {
                d0 = d4;
                l = i2;
                i1 = k2;
                j1 = j2;
                k1 = l2 % 4;
              }
            }
          }
        }
      }
    }
    if (d0 < 0.0D) {
      for (i2 = i - b0; i2 <= i + b0; i2++)
      {
        double d1 = i2 + 0.5D - x;
        for (int j2 = k - b0; j2 <= k + b0; j2++)
        {
          double d2 = j2 + 0.5D - z;
          for (int k2 = this.a.V() - 1; k2 >= 0; k2--) {
            if (this.a.isEmpty(blockposition_mutableblockposition.c(i2, k2, j2)))
            {
              while ((k2 > 0) && (this.a.isEmpty(blockposition_mutableblockposition.c(i2, k2 - 1, j2)))) {
                k2--;
              }
              for (int l2 = l1; l2 < l1 + 2; l2++)
              {
                int i3 = l2 % 2;
                int j3 = 1 - i3;
                for (int k3 = 0; k3 < 4; k3++) {
                  for (int l3 = -1; l3 < 4; l3++)
                  {
                    int i4 = i2 + (k3 - 1) * i3;
                    int j4 = k2 + l3;
                    int k4 = j2 + (k3 - 1) * j3;
                    blockposition_mutableblockposition.c(i4, j4, k4);
                    if (((l3 < 0) && (!this.a.getType(blockposition_mutableblockposition).getBlock().getMaterial().isBuildable())) || ((l3 >= 0) && (!this.a.isEmpty(blockposition_mutableblockposition)))) {
                      break;
                    }
                  }
                }
                double d3 = k2 + 0.5D - y;
                double d4 = d1 * d1 + d3 * d3 + d2 * d2;
                if ((d0 < 0.0D) || (d4 < d0))
                {
                  d0 = d4;
                  l = i2;
                  i1 = k2;
                  j1 = j2;
                  k1 = l2 % 2;
                }
              }
            }
          }
        }
      }
    }
    int i5 = l;
    int j5 = i1;
    
    int j2 = j1;
    int k5 = k1 % 2;
    int l5 = 1 - k5;
    if (k1 % 4 >= 2)
    {
      k5 = -k5;
      l5 = -l5;
    }
    if (d0 < 0.0D)
    {
      i1 = MathHelper.clamp(i1, 70, this.a.V() - 10);
      j5 = i1;
      for (int k2 = -1; k2 <= 1; k2++) {
        for (int l2 = 1; l2 < 3; l2++) {
          for (int i3 = -1; i3 < 3; i3++)
          {
            int j3 = i5 + (l2 - 1) * k5 + k2 * l5;
            int k3 = j5 + i3;
            int l3 = j2 + (l2 - 1) * l5 - k2 * k5;
            boolean flag = i3 < 0;
            
            this.a.setTypeUpdate(new BlockPosition(j3, k3, l3), flag ? Blocks.OBSIDIAN.getBlockData() : Blocks.AIR.getBlockData());
          }
        }
      }
    }
    IBlockData iblockdata = Blocks.PORTAL.getBlockData().set(BlockPortal.AXIS, k5 != 0 ? EnumDirection.EnumAxis.X : EnumDirection.EnumAxis.Z);
    for (int l2 = 0; l2 < 4; l2++)
    {
      for (int i3 = 0; i3 < 4; i3++) {
        for (int j3 = -1; j3 < 4; j3++)
        {
          int k3 = i5 + (i3 - 1) * k5;
          int l3 = j5 + j3;
          int i4 = j2 + (i3 - 1) * l5;
          boolean flag1 = (i3 == 0) || (i3 == 3) || (j3 == -1) || (j3 == 3);
          
          this.a.setTypeAndData(new BlockPosition(k3, l3, i4), flag1 ? Blocks.OBSIDIAN.getBlockData() : iblockdata, 2);
        }
      }
      for (i3 = 0; i3 < 4; i3++) {
        for (int j3 = -1; j3 < 4; j3++)
        {
          int k3 = i5 + (i3 - 1) * k5;
          int l3 = j5 + j3;
          int i4 = j2 + (i3 - 1) * l5;
          BlockPosition blockposition = new BlockPosition(k3, l3, i4);
          
          this.a.applyPhysics(blockposition, this.a.getType(blockposition).getBlock());
        }
      }
    }
    return true;
  }
  
  public void a(long i)
  {
    if (i % 100L == 0L)
    {
      Iterator iterator = this.d.iterator();
      long j = i - 300L;
      while (iterator.hasNext())
      {
        Long olong = (Long)iterator.next();
        ChunkCoordinatesPortal portaltravelagent_chunkcoordinatesportal = (ChunkCoordinatesPortal)this.c.getEntry(olong.longValue());
        if ((portaltravelagent_chunkcoordinatesportal == null) || (portaltravelagent_chunkcoordinatesportal.c < j))
        {
          iterator.remove();
          this.c.remove(olong.longValue());
        }
      }
    }
  }
  
  public class ChunkCoordinatesPortal
    extends BlockPosition
  {
    public long c;
    
    public ChunkCoordinatesPortal(BlockPosition blockposition, long i)
    {
      super(blockposition.getY(), blockposition.getZ());
      this.c = i;
    }
  }
}
