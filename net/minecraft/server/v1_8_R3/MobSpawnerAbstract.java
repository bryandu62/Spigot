package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.spigotmc.SpigotWorldConfig;

public abstract class MobSpawnerAbstract
{
  public int spawnDelay = 20;
  private String mobName = "Pig";
  private final List<a> mobs = Lists.newArrayList();
  private a spawnData;
  private double e;
  private double f;
  private int minSpawnDelay = 200;
  private int maxSpawnDelay = 800;
  private int spawnCount = 4;
  private Entity j;
  private int maxNearbyEntities = 6;
  private int requiredPlayerRange = 16;
  private int spawnRange = 4;
  
  public String getMobName()
  {
    if (i() == null)
    {
      if (this.mobName == null) {
        this.mobName = "Pig";
      }
      if ((this.mobName != null) && (this.mobName.equals("Minecart"))) {
        this.mobName = "MinecartRideable";
      }
      return this.mobName;
    }
    return i().d;
  }
  
  public void setMobName(String s)
  {
    this.mobName = s;
  }
  
  private boolean g()
  {
    BlockPosition blockposition = b();
    
    return a().isPlayerNearby(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, this.requiredPlayerRange);
  }
  
  public void c()
  {
    if (g())
    {
      BlockPosition blockposition = b();
      if (a().isClientSide)
      {
        double d1 = blockposition.getX() + a().random.nextFloat();
        double d2 = blockposition.getY() + a().random.nextFloat();
        
        double d0 = blockposition.getZ() + a().random.nextFloat();
        a().addParticle(EnumParticle.SMOKE_NORMAL, d1, d2, d0, 0.0D, 0.0D, 0.0D, new int[0]);
        a().addParticle(EnumParticle.FLAME, d1, d2, d0, 0.0D, 0.0D, 0.0D, new int[0]);
        if (this.spawnDelay > 0) {
          this.spawnDelay -= 1;
        }
        this.f = this.e;
        this.e = ((this.e + 1000.0F / (this.spawnDelay + 200.0F)) % 360.0D);
      }
      else
      {
        if (this.spawnDelay == -1) {
          h();
        }
        if (this.spawnDelay > 0)
        {
          this.spawnDelay -= 1;
          return;
        }
        boolean flag = false;
        for (int i = 0; i < this.spawnCount; i++)
        {
          Entity entity = EntityTypes.createEntityByName(getMobName(), a());
          if (entity == null) {
            return;
          }
          int j = a().a(entity.getClass(), new AxisAlignedBB(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition.getX() + 1, blockposition.getY() + 1, blockposition.getZ() + 1).grow(this.spawnRange, this.spawnRange, this.spawnRange)).size();
          if (j >= this.maxNearbyEntities)
          {
            h();
            return;
          }
          double d0 = blockposition.getX() + (a().random.nextDouble() - a().random.nextDouble()) * this.spawnRange + 0.5D;
          double d3 = blockposition.getY() + a().random.nextInt(3) - 1;
          double d4 = blockposition.getZ() + (a().random.nextDouble() - a().random.nextDouble()) * this.spawnRange + 0.5D;
          EntityInsentient entityinsentient = (entity instanceof EntityInsentient) ? (EntityInsentient)entity : null;
          
          entity.setPositionRotation(d0, d3, d4, a().random.nextFloat() * 360.0F, 0.0F);
          if ((entityinsentient == null) || ((entityinsentient.bR()) && (entityinsentient.canSpawn())))
          {
            a(entity, true);
            a().triggerEffect(2004, blockposition, 0);
            if (entityinsentient != null) {
              entityinsentient.y();
            }
            flag = true;
          }
        }
        if (flag) {
          h();
        }
      }
    }
  }
  
  private Entity a(Entity entity, boolean flag)
  {
    if (i() != null)
    {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      
      entity.d(nbttagcompound);
      Iterator iterator = i().c.c().iterator();
      while (iterator.hasNext())
      {
        String s = (String)iterator.next();
        NBTBase nbtbase = i().c.get(s);
        
        nbttagcompound.set(s, nbtbase.clone());
      }
      entity.f(nbttagcompound);
      if ((entity.world != null) && (flag))
      {
        SpawnerSpawnEvent event = CraftEventFactory.callSpawnerSpawnEvent(entity, b().getX(), b().getY(), b().getZ());
        if (!event.isCancelled())
        {
          entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.SPAWNER);
          if (entity.world.spigotConfig.nerfSpawnerMobs) {
            entity.fromMobSpawner = true;
          }
        }
      }
      NBTTagCompound nbttagcompound1;
      for (Entity entity1 = entity; nbttagcompound.hasKeyOfType("Riding", 10); nbttagcompound = nbttagcompound1)
      {
        nbttagcompound1 = nbttagcompound.getCompound("Riding");
        Entity entity2 = EntityTypes.createEntityByName(nbttagcompound1.getString("id"), entity.world);
        if (entity2 != null)
        {
          NBTTagCompound nbttagcompound2 = new NBTTagCompound();
          
          entity2.d(nbttagcompound2);
          Iterator iterator1 = nbttagcompound1.c().iterator();
          while (iterator1.hasNext())
          {
            String s1 = (String)iterator1.next();
            NBTBase nbtbase1 = nbttagcompound1.get(s1);
            
            nbttagcompound2.set(s1, nbtbase1.clone());
          }
          entity2.f(nbttagcompound2);
          entity2.setPositionRotation(entity1.locX, entity1.locY, entity1.locZ, entity1.yaw, entity1.pitch);
          
          SpawnerSpawnEvent event = CraftEventFactory.callSpawnerSpawnEvent(entity2, b().getX(), b().getY(), b().getZ());
          if (!event.isCancelled())
          {
            if ((entity.world != null) && (flag)) {
              entity.world.addEntity(entity2, CreatureSpawnEvent.SpawnReason.SPAWNER);
            }
            entity1.mount(entity2);
          }
        }
        else
        {
          entity1 = entity2;
        }
      }
    }
    else if (((entity instanceof EntityLiving)) && (entity.world != null) && (flag))
    {
      if ((entity instanceof EntityInsentient)) {
        ((EntityInsentient)entity).prepare(entity.world.E(new BlockPosition(entity)), null);
      }
      SpawnerSpawnEvent event = CraftEventFactory.callSpawnerSpawnEvent(entity, b().getX(), b().getY(), b().getZ());
      if (!event.isCancelled())
      {
        entity.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.SPAWNER);
        if (entity.world.spigotConfig.nerfSpawnerMobs) {
          entity.fromMobSpawner = true;
        }
      }
    }
    return entity;
  }
  
  private void h()
  {
    if (this.maxSpawnDelay <= this.minSpawnDelay)
    {
      this.spawnDelay = this.minSpawnDelay;
    }
    else
    {
      int i = this.maxSpawnDelay - this.minSpawnDelay;
      
      this.spawnDelay = (this.minSpawnDelay + a().random.nextInt(i));
    }
    if (this.mobs.size() > 0) {
      a((a)WeightedRandom.a(a().random, this.mobs));
    }
    a(1);
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    this.mobName = nbttagcompound.getString("EntityId");
    this.spawnDelay = nbttagcompound.getShort("Delay");
    this.mobs.clear();
    if (nbttagcompound.hasKeyOfType("SpawnPotentials", 9))
    {
      NBTTagList nbttaglist = nbttagcompound.getList("SpawnPotentials", 10);
      for (int i = 0; i < nbttaglist.size(); i++) {
        this.mobs.add(new a(nbttaglist.get(i)));
      }
    }
    if (nbttagcompound.hasKeyOfType("SpawnData", 10)) {
      a(new a(nbttagcompound.getCompound("SpawnData"), this.mobName));
    } else {
      a(null);
    }
    if (nbttagcompound.hasKeyOfType("MinSpawnDelay", 99))
    {
      this.minSpawnDelay = nbttagcompound.getShort("MinSpawnDelay");
      this.maxSpawnDelay = nbttagcompound.getShort("MaxSpawnDelay");
      this.spawnCount = nbttagcompound.getShort("SpawnCount");
    }
    if (nbttagcompound.hasKeyOfType("MaxNearbyEntities", 99))
    {
      this.maxNearbyEntities = nbttagcompound.getShort("MaxNearbyEntities");
      this.requiredPlayerRange = nbttagcompound.getShort("RequiredPlayerRange");
    }
    if (nbttagcompound.hasKeyOfType("SpawnRange", 99)) {
      this.spawnRange = nbttagcompound.getShort("SpawnRange");
    }
    if (a() != null) {
      this.j = null;
    }
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    String s = getMobName();
    if (!UtilColor.b(s))
    {
      nbttagcompound.setString("EntityId", s);
      nbttagcompound.setShort("Delay", (short)this.spawnDelay);
      nbttagcompound.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
      nbttagcompound.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
      nbttagcompound.setShort("SpawnCount", (short)this.spawnCount);
      nbttagcompound.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
      nbttagcompound.setShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
      nbttagcompound.setShort("SpawnRange", (short)this.spawnRange);
      if (i() != null) {
        nbttagcompound.set("SpawnData", i().c.clone());
      }
      if ((i() != null) || (this.mobs.size() > 0))
      {
        NBTTagList nbttaglist = new NBTTagList();
        if (this.mobs.size() > 0)
        {
          Iterator iterator = this.mobs.iterator();
          while (iterator.hasNext())
          {
            a mobspawnerabstract_a = (a)iterator.next();
            
            nbttaglist.add(mobspawnerabstract_a.a());
          }
        }
        else
        {
          nbttaglist.add(i().a());
        }
        nbttagcompound.set("SpawnPotentials", nbttaglist);
      }
    }
  }
  
  public boolean b(int i)
  {
    if ((i == 1) && (a().isClientSide))
    {
      this.spawnDelay = this.minSpawnDelay;
      return true;
    }
    return false;
  }
  
  private a i()
  {
    return this.spawnData;
  }
  
  public void a(a mobspawnerabstract_a)
  {
    this.spawnData = mobspawnerabstract_a;
  }
  
  public abstract void a(int paramInt);
  
  public abstract World a();
  
  public abstract BlockPosition b();
  
  public class a
    extends WeightedRandom.WeightedRandomChoice
  {
    private final NBTTagCompound c;
    private final String d;
    
    public a(NBTTagCompound nbttagcompound)
    {
      this(nbttagcompound.getCompound("Properties"), nbttagcompound.getString("Type"), nbttagcompound.getInt("Weight"));
    }
    
    public a(NBTTagCompound nbttagcompound, String s)
    {
      this(nbttagcompound, s, 1);
    }
    
    private a(NBTTagCompound nbttagcompound, String s, int i)
    {
      super();
      if (s.equals("Minecart")) {
        if (nbttagcompound != null) {
          s = EntityMinecartAbstract.EnumMinecartType.a(nbttagcompound.getInt("Type")).b();
        } else {
          s = "MinecartRideable";
        }
      }
      this.c = nbttagcompound;
      this.d = s;
    }
    
    public NBTTagCompound a()
    {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      
      nbttagcompound.set("Properties", this.c);
      nbttagcompound.setString("Type", this.d);
      nbttagcompound.setInt("Weight", this.a);
      return nbttagcompound;
    }
  }
}
