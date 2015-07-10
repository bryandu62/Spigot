package org.bukkit.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public enum CreatureType
{
  CREEPER(
    "Creeper", Creeper.class, 50),  SKELETON("Skeleton", Skeleton.class, 51),  SPIDER("Spider", Spider.class, 52),  GIANT("Giant", Giant.class, 53),  ZOMBIE("Zombie", Zombie.class, 54),  SLIME("Slime", Slime.class, 55),  GHAST("Ghast", Ghast.class, 56),  PIG_ZOMBIE("PigZombie", PigZombie.class, 57),  ENDERMAN("Enderman", Enderman.class, 58),  CAVE_SPIDER("CaveSpider", CaveSpider.class, 59),  SILVERFISH("Silverfish", Silverfish.class, 60),  BLAZE("Blaze", Blaze.class, 61),  MAGMA_CUBE("LavaSlime", MagmaCube.class, 62),  ENDER_DRAGON("EnderDragon", EnderDragon.class, 63),  ENDERMITE("Endermite", Endermite.class, 67),  GUARDIAN("Guardian", Guardian.class, 68),  PIG("Pig", Pig.class, 90),  SHEEP("Sheep", Sheep.class, 91),  COW("Cow", Cow.class, 92),  CHICKEN("Chicken", Chicken.class, 93),  SQUID("Squid", Squid.class, 94),  WOLF("Wolf", Wolf.class, 95),  MUSHROOM_COW("MushroomCow", MushroomCow.class, 96),  SNOWMAN("SnowMan", Snowman.class, 97),  RABBIT("Rabbit", Rabbit.class, 101),  VILLAGER("Villager", Villager.class, 120);
  
  private String name;
  private Class<? extends Entity> clazz;
  private short typeId;
  private static final Map<String, CreatureType> NAME_MAP;
  private static final Map<Short, CreatureType> ID_MAP;
  
  static
  {
    NAME_MAP = new HashMap();
    ID_MAP = new HashMap();
    for (CreatureType type : EnumSet.allOf(CreatureType.class))
    {
      NAME_MAP.put(type.name, type);
      if (type.typeId != 0) {
        ID_MAP.put(Short.valueOf(type.typeId), type);
      }
    }
  }
  
  private CreatureType(String name, Class<? extends Entity> clazz, int typeId)
  {
    this.name = name;
    this.clazz = clazz;
    this.typeId = ((short)typeId);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Class<? extends Entity> getEntityClass()
  {
    return this.clazz;
  }
  
  @Deprecated
  public short getTypeId()
  {
    return this.typeId;
  }
  
  public static CreatureType fromName(String name)
  {
    return (CreatureType)NAME_MAP.get(name);
  }
  
  @Deprecated
  public static CreatureType fromId(int id)
  {
    if (id > 32767) {
      return null;
    }
    return (CreatureType)ID_MAP.get(Short.valueOf((short)id));
  }
  
  @Deprecated
  public EntityType toEntityType()
  {
    return EntityType.fromName(getName());
  }
  
  public static CreatureType fromEntityType(EntityType creatureType)
  {
    return fromName(creatureType.getName());
  }
}
