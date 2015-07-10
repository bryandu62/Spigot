package net.minecraft.server.v1_8_R3;

import gnu.trove.map.hash.TObjectIntHashMap;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHashSet;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.spigotmc.SpigotWorldConfig;

public final class SpawnerCreature
{
  private static final int a = (int)Math.pow(17.0D, 2.0D);
  private final LongHashSet b = new LongHashSet();
  
  private int getEntityCount(WorldServer server, Class oClass)
  {
    int i = 0;
    Iterator<Long> it = this.b.iterator();
    while (it.hasNext())
    {
      Long coord = (Long)it.next();
      int x = LongHash.msw(coord.longValue());
      int z = LongHash.lsw(coord.longValue());
      if ((!server.chunkProviderServer.unloadQueue.contains(coord.longValue())) && (server.isChunkLoaded(x, z, true))) {
        i += server.getChunkAt(x, z).entityCount.get(oClass);
      }
    }
    return i;
  }
  
  public int a(WorldServer worldserver, boolean flag, boolean flag1, boolean flag2)
  {
    if ((!flag) && (!flag1)) {
      return 0;
    }
    this.b.clear();
    int i = 0;
    Iterator iterator = worldserver.players.iterator();
    while (iterator.hasNext())
    {
      EntityHuman entityhuman = (EntityHuman)iterator.next();
      if (!entityhuman.isSpectator())
      {
        int l = MathHelper.floor(entityhuman.locX / 16.0D);
        
        int j = MathHelper.floor(entityhuman.locZ / 16.0D);
        byte b0 = 8;
        
        b0 = worldserver.spigotConfig.mobSpawnRange;
        b0 = b0 > worldserver.spigotConfig.viewDistance ? (byte)worldserver.spigotConfig.viewDistance : b0;
        b0 = b0 > 8 ? 8 : b0;
        for (int i1 = -b0; i1 <= b0; i1++) {
          for (int k = -b0; k <= b0; k++)
          {
            boolean flag3 = (i1 == -b0) || (i1 == b0) || (k == -b0) || (k == b0);
            
            long chunkCoords = LongHash.toLong(i1 + l, k + j);
            if (!this.b.contains(chunkCoords))
            {
              i++;
              if ((!flag3) && (worldserver.getWorldBorder().isInBounds(i1 + l, k + j))) {
                this.b.add(chunkCoords);
              }
            }
          }
        }
      }
    }
    int j1 = 0;
    BlockPosition blockposition = worldserver.getSpawn();
    EnumCreatureType[] aenumcreaturetype = EnumCreatureType.values();
    
    int j = aenumcreaturetype.length;
    for (int k1 = 0; k1 < j; k1++)
    {
      EnumCreatureType enumcreaturetype = aenumcreaturetype[k1];
      
      int limit = enumcreaturetype.b();
      switch (enumcreaturetype)
      {
      case AMBIENT: 
        limit = worldserver.getWorld().getMonsterSpawnLimit();
        break;
      case CREATURE: 
        limit = worldserver.getWorld().getAnimalSpawnLimit();
        break;
      case WATER_CREATURE: 
        limit = worldserver.getWorld().getWaterAnimalSpawnLimit();
        break;
      case MONSTER: 
        limit = worldserver.getWorld().getAmbientSpawnLimit();
      }
      if (limit != 0)
      {
        int mobcnt = 0;
        if (((!enumcreaturetype.d()) || (flag1)) && ((enumcreaturetype.d()) || (flag)) && ((!enumcreaturetype.e()) || (flag2)))
        {
          int k = worldserver.a(enumcreaturetype.a());
          (limit * i / a);
          if ((mobcnt = getEntityCount(worldserver, enumcreaturetype.a())) <= limit * i / 256)
          {
            Iterator iterator1 = this.b.iterator();
            
            int moblimit = limit * i / 256 - mobcnt + 1;
            label1018:
            while ((iterator1.hasNext()) && (moblimit > 0))
            {
              long key = ((Long)iterator1.next()).longValue();
              BlockPosition blockposition1 = getRandomPosition(worldserver, LongHash.msw(key), LongHash.lsw(key));
              
              int i2 = blockposition1.getX();
              int j2 = blockposition1.getY();
              int k2 = blockposition1.getZ();
              Block block = worldserver.getType(blockposition1).getBlock();
              if (!block.isOccluding())
              {
                int l2 = 0;
                int i3 = 0;
                while (i3 < 3)
                {
                  int j3 = i2;
                  int k3 = j2;
                  int l3 = k2;
                  byte b1 = 6;
                  BiomeBase.BiomeMeta biomebase_biomemeta = null;
                  GroupDataEntity groupdataentity = null;
                  int i4 = 0;
                  while (i4 < 4)
                  {
                    j3 += worldserver.random.nextInt(b1) - worldserver.random.nextInt(b1);
                    k3 += worldserver.random.nextInt(1) - worldserver.random.nextInt(1);
                    l3 += worldserver.random.nextInt(b1) - worldserver.random.nextInt(b1);
                    BlockPosition blockposition2 = new BlockPosition(j3, k3, l3);
                    float f = j3 + 0.5F;
                    float f1 = l3 + 0.5F;
                    if ((!worldserver.isPlayerNearby(f, k3, f1, 24.0D)) && (blockposition.c(f, k3, f1) >= 576.0D))
                    {
                      if (biomebase_biomemeta == null)
                      {
                        biomebase_biomemeta = worldserver.a(enumcreaturetype, blockposition2);
                        if (biomebase_biomemeta == null) {
                          break;
                        }
                      }
                      if ((worldserver.a(enumcreaturetype, biomebase_biomemeta, blockposition2)) && (a(EntityPositionTypes.a(biomebase_biomemeta.b), worldserver, blockposition2)))
                      {
                        try
                        {
                          entityinsentient = (EntityInsentient)biomebase_biomemeta.b.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldserver });
                        }
                        catch (Exception exception)
                        {
                          EntityInsentient entityinsentient;
                          exception.printStackTrace();
                          return j1;
                        }
                        EntityInsentient entityinsentient;
                        entityinsentient.setPositionRotation(f, k3, f1, worldserver.random.nextFloat() * 360.0F, 0.0F);
                        if ((entityinsentient.bR()) && (entityinsentient.canSpawn()))
                        {
                          groupdataentity = entityinsentient.prepare(worldserver.E(new BlockPosition(entityinsentient)), groupdataentity);
                          if (entityinsentient.canSpawn())
                          {
                            l2++;
                            worldserver.addEntity(entityinsentient, CreatureSpawnEvent.SpawnReason.NATURAL);
                          }
                          moblimit--;
                          if (moblimit <= 0) {
                            break label1018;
                          }
                          if (l2 >= entityinsentient.bV()) {
                            break label1018;
                          }
                        }
                        j1 += l2;
                      }
                    }
                    i4++;
                  }
                  i3++;
                }
              }
            }
          }
        }
      }
    }
    return j1;
  }
  
  protected static BlockPosition getRandomPosition(World world, int i, int j)
  {
    Chunk chunk = world.getChunkAt(i, j);
    int k = i * 16 + world.random.nextInt(16);
    int l = j * 16 + world.random.nextInt(16);
    int i1 = MathHelper.c(chunk.f(new BlockPosition(k, 0, l)) + 1, 16);
    int j1 = world.random.nextInt(i1 > 0 ? i1 : chunk.g() + 16 - 1);
    
    return new BlockPosition(k, j1, l);
  }
  
  public static boolean a(EntityInsentient.EnumEntityPositionType entityinsentient_enumentitypositiontype, World world, BlockPosition blockposition)
  {
    if (!world.getWorldBorder().a(blockposition)) {
      return false;
    }
    Block block = world.getType(blockposition).getBlock();
    if (entityinsentient_enumentitypositiontype == EntityInsentient.EnumEntityPositionType.IN_WATER) {
      return (block.getMaterial().isLiquid()) && (world.getType(blockposition.down()).getBlock().getMaterial().isLiquid()) && (!world.getType(blockposition.up()).getBlock().isOccluding());
    }
    BlockPosition blockposition1 = blockposition.down();
    if (!World.a(world, blockposition1)) {
      return false;
    }
    Block block1 = world.getType(blockposition1).getBlock();
    boolean flag = (block1 != Blocks.BEDROCK) && (block1 != Blocks.BARRIER);
    
    return (flag) && (!block.isOccluding()) && (!block.getMaterial().isLiquid()) && (!world.getType(blockposition.up()).getBlock().isOccluding());
  }
  
  public static void a(World world, BiomeBase biomebase, int i, int j, int k, int l, Random random)
  {
    List list = biomebase.getMobs(EnumCreatureType.CREATURE);
    if (!list.isEmpty())
    {
      int i1;
      int j2;
      for (; random.nextFloat() < biomebase.g(); j2 < i1)
      {
        BiomeBase.BiomeMeta biomebase_biomemeta = (BiomeBase.BiomeMeta)WeightedRandom.a(world.random, list);
        i1 = biomebase_biomemeta.c + random.nextInt(1 + biomebase_biomemeta.d - biomebase_biomemeta.c);
        GroupDataEntity groupdataentity = null;
        int j1 = i + random.nextInt(k);
        int k1 = j + random.nextInt(l);
        int l1 = j1;
        int i2 = k1;
        
        j2 = 0; continue;
        boolean flag = false;
        for (int k2 = 0; (!flag) && (k2 < 4); k2++)
        {
          BlockPosition blockposition = world.r(new BlockPosition(j1, 0, k1));
          if (a(EntityInsentient.EnumEntityPositionType.ON_GROUND, world, blockposition))
          {
            try
            {
              entityinsentient = (EntityInsentient)biomebase_biomemeta.b.getConstructor(new Class[] { World.class }).newInstance(new Object[] { world });
            }
            catch (Exception exception)
            {
              EntityInsentient entityinsentient;
              exception.printStackTrace();
              continue;
            }
            EntityInsentient entityinsentient;
            entityinsentient.setPositionRotation(j1 + 0.5F, blockposition.getY(), k1 + 0.5F, random.nextFloat() * 360.0F, 0.0F);
            
            groupdataentity = entityinsentient.prepare(world.E(new BlockPosition(entityinsentient)), groupdataentity);
            world.addEntity(entityinsentient, CreatureSpawnEvent.SpawnReason.CHUNK_GEN);
            
            flag = true;
          }
          j1 += random.nextInt(5) - random.nextInt(5);
          for (k1 += random.nextInt(5) - random.nextInt(5); (j1 < i) || (j1 >= i + k) || (k1 < j) || (k1 >= j + k); k1 = i2 + random.nextInt(5) - random.nextInt(5)) {
            j1 = l1 + random.nextInt(5) - random.nextInt(5);
          }
        }
        j2++;
      }
    }
  }
}
