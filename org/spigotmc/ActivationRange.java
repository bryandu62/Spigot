package org.spigotmc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityAmbient;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityComplexPart;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityProjectile;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWeather;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings;

public class ActivationRange
{
  static AxisAlignedBB maxBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
  static AxisAlignedBB miscBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
  static AxisAlignedBB animalBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
  static AxisAlignedBB monsterBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
  
  public static byte initializeEntityActivationType(Entity entity)
  {
    if (((entity instanceof EntityMonster)) || ((entity instanceof EntitySlime))) {
      return 1;
    }
    if (((entity instanceof EntityCreature)) || ((entity instanceof EntityAmbient))) {
      return 2;
    }
    return 3;
  }
  
  public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config)
  {
    if (((entity.activationType == 3) && (config.miscActivationRange == 0)) || 
      ((entity.activationType == 2) && (config.animalActivationRange == 0)) || 
      ((entity.activationType == 1) && (config.monsterActivationRange == 0)) || 
      ((entity instanceof EntityHuman)) || 
      ((entity instanceof EntityProjectile)) || 
      ((entity instanceof EntityEnderDragon)) || 
      ((entity instanceof EntityComplexPart)) || 
      ((entity instanceof EntityWither)) || 
      ((entity instanceof EntityFireball)) || 
      ((entity instanceof EntityWeather)) || 
      ((entity instanceof EntityTNTPrimed)) || 
      ((entity instanceof EntityEnderCrystal)) || 
      ((entity instanceof EntityFireworks))) {
      return true;
    }
    return false;
  }
  
  public static void activateEntities(World world)
  {
    SpigotTimings.entityActivationCheckTimer.startTiming();
    int miscActivationRange = world.spigotConfig.miscActivationRange;
    int animalActivationRange = world.spigotConfig.animalActivationRange;
    int monsterActivationRange = world.spigotConfig.monsterActivationRange;
    
    int maxRange = Math.max(monsterActivationRange, animalActivationRange);
    maxRange = Math.max(maxRange, miscActivationRange);
    maxRange = Math.min((world.spigotConfig.viewDistance << 4) - 8, maxRange);
    int j;
    int i1;
    for (Iterator localIterator = world.players.iterator(); localIterator.hasNext(); i1 <= j)
    {
      Entity player = (Entity)localIterator.next();
      
      player.activatedTick = MinecraftServer.currentTick;
      maxBB = player.getBoundingBox().grow(maxRange, 256.0D, maxRange);
      miscBB = player.getBoundingBox().grow(miscActivationRange, 256.0D, miscActivationRange);
      animalBB = player.getBoundingBox().grow(animalActivationRange, 256.0D, animalActivationRange);
      monsterBB = player.getBoundingBox().grow(monsterActivationRange, 256.0D, monsterActivationRange);
      
      int i = MathHelper.floor(maxBB.a / 16.0D);
      j = MathHelper.floor(maxBB.d / 16.0D);
      int k = MathHelper.floor(maxBB.c / 16.0D);
      int l = MathHelper.floor(maxBB.f / 16.0D);
      
      i1 = i; continue;
      for (int j1 = k; j1 <= l; j1++) {
        if (world.getWorld().isChunkLoaded(i1, j1)) {
          activateChunkEntities(world.getChunkAt(i1, j1));
        }
      }
      i1++;
    }
    SpigotTimings.entityActivationCheckTimer.stopTiming();
  }
  
  private static void activateChunkEntities(Chunk chunk)
  {
    List[] arrayOfList;
    int i = (arrayOfList = chunk.entitySlices).length;
    for (int j = 0; j < i; j++)
    {
      List<Entity> slice = arrayOfList[j];
      for (Entity entity : slice) {
        if (MinecraftServer.currentTick > entity.activatedTick) {
          if (entity.defaultActivationState) {
            entity.activatedTick = MinecraftServer.currentTick;
          } else {
            switch (entity.activationType)
            {
            case 1: 
              if (monsterBB.b(entity.getBoundingBox())) {
                entity.activatedTick = MinecraftServer.currentTick;
              }
              break;
            case 2: 
              if (animalBB.b(entity.getBoundingBox())) {
                entity.activatedTick = MinecraftServer.currentTick;
              }
              break;
            case 3: 
            default: 
              if (miscBB.b(entity.getBoundingBox())) {
                entity.activatedTick = MinecraftServer.currentTick;
              }
              break;
            }
          }
        }
      }
    }
  }
  
  public static boolean checkEntityImmunities(Entity entity)
  {
    if ((entity.inWater) || (entity.fireTicks > 0)) {
      return true;
    }
    if (!(entity instanceof EntityArrow))
    {
      if ((!entity.onGround) || (entity.passenger != null) || 
        (entity.vehicle != null)) {
        return true;
      }
    }
    else if (!((EntityArrow)entity).inGround) {
      return true;
    }
    if ((entity instanceof EntityLiving))
    {
      EntityLiving living = (EntityLiving)entity;
      if ((living.hurtTicks > 0) || (living.effects.size() > 0)) {
        return true;
      }
      if (((entity instanceof EntityCreature)) && (((EntityCreature)entity).getGoalTarget() != null)) {
        return true;
      }
      if (((entity instanceof EntityVillager)) && (((EntityVillager)entity).cm())) {
        return true;
      }
      if ((entity instanceof EntityAnimal))
      {
        EntityAnimal animal = (EntityAnimal)entity;
        if ((animal.isBaby()) || (animal.isInLove())) {
          return true;
        }
        if (((entity instanceof EntitySheep)) && (((EntitySheep)entity).isSheared())) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static boolean checkIfActive(Entity entity)
  {
    SpigotTimings.checkIfActiveTimer.startTiming();
    if ((!entity.isAddedToChunk()) || ((entity instanceof EntityFireworks)))
    {
      SpigotTimings.checkIfActiveTimer.stopTiming();
      return true;
    }
    boolean isActive = (entity.activatedTick >= MinecraftServer.currentTick) || (entity.defaultActivationState);
    if (!isActive)
    {
      if ((MinecraftServer.currentTick - entity.activatedTick - 1L) % 20L == 0L)
      {
        if (checkEntityImmunities(entity)) {
          entity.activatedTick = (MinecraftServer.currentTick + 20);
        }
        isActive = true;
      }
    }
    else if ((!entity.defaultActivationState) && (entity.ticksLived % 4 == 0) && (!checkEntityImmunities(entity))) {
      isActive = false;
    }
    int x = MathHelper.floor(entity.locX);
    int z = MathHelper.floor(entity.locZ);
    
    Chunk chunk = entity.world.getChunkIfLoaded(x >> 4, z >> 4);
    if ((isActive) && ((chunk == null) || (!chunk.areNeighborsLoaded(1)))) {
      isActive = false;
    }
    SpigotTimings.checkIfActiveTimer.stopTiming();
    return isActive;
  }
}
