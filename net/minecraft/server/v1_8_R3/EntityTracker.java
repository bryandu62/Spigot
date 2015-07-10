package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.TrackingRange;

public class EntityTracker
{
  private static final Logger a = ;
  private final WorldServer world;
  private Set<EntityTrackerEntry> c = Sets.newHashSet();
  public IntHashMap<EntityTrackerEntry> trackedEntities = new IntHashMap();
  private int e;
  
  public EntityTracker(WorldServer worldserver)
  {
    this.world = worldserver;
    this.e = worldserver.getMinecraftServer().getPlayerList().d();
  }
  
  public void track(Entity entity)
  {
    if ((entity instanceof EntityPlayer))
    {
      addEntity(entity, 512, 2);
      EntityPlayer entityplayer = (EntityPlayer)entity;
      Iterator iterator = this.c.iterator();
      while (iterator.hasNext())
      {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
        if (entitytrackerentry.tracker != entityplayer) {
          entitytrackerentry.updatePlayer(entityplayer);
        }
      }
    }
    else if ((entity instanceof EntityFishingHook))
    {
      addEntity(entity, 64, 5, true);
    }
    else if ((entity instanceof EntityArrow))
    {
      addEntity(entity, 64, 20, false);
    }
    else if ((entity instanceof EntitySmallFireball))
    {
      addEntity(entity, 64, 10, false);
    }
    else if ((entity instanceof EntityFireball))
    {
      addEntity(entity, 64, 10, false);
    }
    else if ((entity instanceof EntitySnowball))
    {
      addEntity(entity, 64, 10, true);
    }
    else if ((entity instanceof EntityEnderPearl))
    {
      addEntity(entity, 64, 10, true);
    }
    else if ((entity instanceof EntityEnderSignal))
    {
      addEntity(entity, 64, 4, true);
    }
    else if ((entity instanceof EntityEgg))
    {
      addEntity(entity, 64, 10, true);
    }
    else if ((entity instanceof EntityPotion))
    {
      addEntity(entity, 64, 10, true);
    }
    else if ((entity instanceof EntityThrownExpBottle))
    {
      addEntity(entity, 64, 10, true);
    }
    else if ((entity instanceof EntityFireworks))
    {
      addEntity(entity, 64, 10, true);
    }
    else if ((entity instanceof EntityItem))
    {
      addEntity(entity, 64, 20, true);
    }
    else if ((entity instanceof EntityMinecartAbstract))
    {
      addEntity(entity, 80, 3, true);
    }
    else if ((entity instanceof EntityBoat))
    {
      addEntity(entity, 80, 3, true);
    }
    else if ((entity instanceof EntitySquid))
    {
      addEntity(entity, 64, 3, true);
    }
    else if ((entity instanceof EntityWither))
    {
      addEntity(entity, 80, 3, false);
    }
    else if ((entity instanceof EntityBat))
    {
      addEntity(entity, 80, 3, false);
    }
    else if ((entity instanceof EntityEnderDragon))
    {
      addEntity(entity, 160, 3, true);
    }
    else if ((entity instanceof IAnimal))
    {
      addEntity(entity, 80, 3, true);
    }
    else if ((entity instanceof EntityTNTPrimed))
    {
      addEntity(entity, 160, 10, true);
    }
    else if ((entity instanceof EntityFallingBlock))
    {
      addEntity(entity, 160, 20, true);
    }
    else if ((entity instanceof EntityHanging))
    {
      addEntity(entity, 160, Integer.MAX_VALUE, false);
    }
    else if ((entity instanceof EntityArmorStand))
    {
      addEntity(entity, 160, 3, true);
    }
    else if ((entity instanceof EntityExperienceOrb))
    {
      addEntity(entity, 160, 20, true);
    }
    else if ((entity instanceof EntityEnderCrystal))
    {
      addEntity(entity, 256, Integer.MAX_VALUE, false);
    }
  }
  
  public void addEntity(Entity entity, int i, int j)
  {
    addEntity(entity, i, j, false);
  }
  
  public void addEntity(Entity entity, int i, int j, boolean flag)
  {
    AsyncCatcher.catchOp("entity track");
    i = TrackingRange.getEntityTrackingRange(entity, i);
    if (i > this.e) {
      i = this.e;
    }
    try
    {
      if (this.trackedEntities.b(entity.getId())) {
        throw new IllegalStateException("Entity is already tracked!");
      }
      EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, j, flag);
      
      this.c.add(entitytrackerentry);
      this.trackedEntities.a(entity.getId(), entitytrackerentry);
      entitytrackerentry.scanPlayers(this.world.players);
    }
    catch (Throwable throwable)
    {
      CrashReport crashreport = CrashReport.a(throwable, "Adding entity to track");
      CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity To Track");
      
      crashreportsystemdetails.a("Tracking range", i + " blocks");
      final int finalI = i;
      crashreportsystemdetails.a("Update interval", new Callable()
      {
        public String a()
          throws Exception
        {
          String s = "Once per " + finalI + " ticks";
          if (finalI == Integer.MAX_VALUE) {
            s = "Maximum (" + s + ")";
          }
          return s;
        }
        
        public Object call()
          throws Exception
        {
          return a();
        }
      });
      entity.appendEntityCrashDetails(crashreportsystemdetails);
      CrashReportSystemDetails crashreportsystemdetails1 = crashreport.a("Entity That Is Already Tracked");
      
      ((EntityTrackerEntry)this.trackedEntities.get(entity.getId())).tracker.appendEntityCrashDetails(crashreportsystemdetails1);
      try
      {
        throw new ReportedException(crashreport);
      }
      catch (ReportedException reportedexception)
      {
        a.error("\"Silently\" catching entity tracking error.", reportedexception);
      }
    }
  }
  
  public void untrackEntity(Entity entity)
  {
    AsyncCatcher.catchOp("entity untrack");
    if ((entity instanceof EntityPlayer))
    {
      EntityPlayer entityplayer = (EntityPlayer)entity;
      Iterator iterator = this.c.iterator();
      while (iterator.hasNext())
      {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
        
        entitytrackerentry.a(entityplayer);
      }
    }
    EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry)this.trackedEntities.d(entity.getId());
    if (entitytrackerentry1 != null)
    {
      this.c.remove(entitytrackerentry1);
      entitytrackerentry1.a();
    }
  }
  
  public void updatePlayers()
  {
    ArrayList arraylist = Lists.newArrayList();
    Iterator iterator = this.c.iterator();
    while (iterator.hasNext())
    {
      EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
      
      entitytrackerentry.track(this.world.players);
      if ((entitytrackerentry.n) && ((entitytrackerentry.tracker instanceof EntityPlayer))) {
        arraylist.add((EntityPlayer)entitytrackerentry.tracker);
      }
    }
    for (int i = 0; i < arraylist.size(); i++)
    {
      EntityPlayer entityplayer = (EntityPlayer)arraylist.get(i);
      Iterator iterator1 = this.c.iterator();
      while (iterator1.hasNext())
      {
        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry)iterator1.next();
        if (entitytrackerentry1.tracker != entityplayer) {
          entitytrackerentry1.updatePlayer(entityplayer);
        }
      }
    }
  }
  
  public void a(EntityPlayer entityplayer)
  {
    Iterator iterator = this.c.iterator();
    while (iterator.hasNext())
    {
      EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
      if (entitytrackerentry.tracker == entityplayer) {
        entitytrackerentry.scanPlayers(this.world.players);
      } else {
        entitytrackerentry.updatePlayer(entityplayer);
      }
    }
  }
  
  public void a(Entity entity, Packet packet)
  {
    EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntities.get(entity.getId());
    if (entitytrackerentry != null) {
      entitytrackerentry.broadcast(packet);
    }
  }
  
  public void sendPacketToEntity(Entity entity, Packet packet)
  {
    EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntities.get(entity.getId());
    if (entitytrackerentry != null) {
      entitytrackerentry.broadcastIncludingSelf(packet);
    }
  }
  
  public void untrackPlayer(EntityPlayer entityplayer)
  {
    Iterator iterator = this.c.iterator();
    while (iterator.hasNext())
    {
      EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
      
      entitytrackerentry.clear(entityplayer);
    }
  }
  
  public void a(EntityPlayer entityplayer, Chunk chunk)
  {
    Iterator iterator = this.c.iterator();
    while (iterator.hasNext())
    {
      EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)iterator.next();
      if ((entitytrackerentry.tracker != entityplayer) && (entitytrackerentry.tracker.ae == chunk.locX) && (entitytrackerentry.tracker.ag == chunk.locZ)) {
        entitytrackerentry.updatePlayer(entityplayer);
      }
    }
  }
}
