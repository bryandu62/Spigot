package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.HashMap;

public class EntityPositionTypes
{
  private static final HashMap<Class, EntityInsentient.EnumEntityPositionType> a = ;
  
  public static EntityInsentient.EnumEntityPositionType a(Class ☃)
  {
    return (EntityInsentient.EnumEntityPositionType)a.get(☃);
  }
  
  static
  {
    a.put(EntityBat.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityChicken.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityCow.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityHorse.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityMushroomCow.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityOcelot.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityPig.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityRabbit.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntitySheep.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntitySnowman.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntitySquid.class, EntityInsentient.EnumEntityPositionType.IN_WATER);
    a.put(EntityIronGolem.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityWolf.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    
    a.put(EntityVillager.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    
    a.put(EntityEnderDragon.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityWither.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    
    a.put(EntityBlaze.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityCaveSpider.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityCreeper.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityEnderman.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityEndermite.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityGhast.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityGiantZombie.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityGuardian.class, EntityInsentient.EnumEntityPositionType.IN_WATER);
    a.put(EntityMagmaCube.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityPigZombie.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntitySilverfish.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntitySkeleton.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntitySlime.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntitySpider.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityWitch.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
    a.put(EntityZombie.class, EntityInsentient.EnumEntityPositionType.ON_GROUND);
  }
}
