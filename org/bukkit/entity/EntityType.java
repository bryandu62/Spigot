package org.bukkit.entity;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;

public enum EntityType
{
  DROPPED_ITEM(
  
    "Item", Item.class, 1, false),  EXPERIENCE_ORB(
  
    "XPOrb", ExperienceOrb.class, 2),  LEASH_HITCH(
  
    "LeashKnot", LeashHitch.class, 8),  PAINTING(
  
    "Painting", Painting.class, 9),  ARROW(
  
    "Arrow", Arrow.class, 10),  SNOWBALL(
  
    "Snowball", Snowball.class, 11),  FIREBALL(
  
    "Fireball", LargeFireball.class, 12),  SMALL_FIREBALL(
  
    "SmallFireball", SmallFireball.class, 13),  ENDER_PEARL(
  
    "ThrownEnderpearl", EnderPearl.class, 14),  ENDER_SIGNAL(
  
    "EyeOfEnderSignal", EnderSignal.class, 15),  THROWN_EXP_BOTTLE(
  
    "ThrownExpBottle", ThrownExpBottle.class, 17),  ITEM_FRAME(
  
    "ItemFrame", ItemFrame.class, 18),  WITHER_SKULL(
  
    "WitherSkull", WitherSkull.class, 19),  PRIMED_TNT(
  
    "PrimedTnt", TNTPrimed.class, 20),  FALLING_BLOCK(
  
    "FallingSand", FallingBlock.class, 21, false),  FIREWORK("FireworksRocketEntity", Firework.class, 22, false),  ARMOR_STAND("ArmorStand", ArmorStand.class, 30, false),  MINECART_COMMAND(
  
    "MinecartCommandBlock", CommandMinecart.class, 40),  BOAT(
  
    "Boat", Boat.class, 41),  MINECART(
  
    "MinecartRideable", RideableMinecart.class, 42),  MINECART_CHEST(
  
    "MinecartChest", StorageMinecart.class, 43),  MINECART_FURNACE(
  
    "MinecartFurnace", PoweredMinecart.class, 44),  MINECART_TNT(
  
    "MinecartTNT", ExplosiveMinecart.class, 45),  MINECART_HOPPER(
  
    "MinecartHopper", HopperMinecart.class, 46),  MINECART_MOB_SPAWNER(
  
    "MinecartMobSpawner", SpawnerMinecart.class, 47),  CREEPER("Creeper", Creeper.class, 50),  SKELETON("Skeleton", Skeleton.class, 51),  SPIDER("Spider", Spider.class, 52),  GIANT("Giant", Giant.class, 53),  ZOMBIE("Zombie", Zombie.class, 54),  SLIME("Slime", Slime.class, 55),  GHAST("Ghast", Ghast.class, 56),  PIG_ZOMBIE("PigZombie", PigZombie.class, 57),  ENDERMAN("Enderman", Enderman.class, 58),  CAVE_SPIDER("CaveSpider", CaveSpider.class, 59),  SILVERFISH("Silverfish", Silverfish.class, 60),  BLAZE("Blaze", Blaze.class, 61),  MAGMA_CUBE("LavaSlime", MagmaCube.class, 62),  ENDER_DRAGON("EnderDragon", EnderDragon.class, 63),  WITHER("WitherBoss", Wither.class, 64),  BAT("Bat", Bat.class, 65),  WITCH("Witch", Witch.class, 66),  ENDERMITE("Endermite", Endermite.class, 67),  GUARDIAN("Guardian", Guardian.class, 68),  PIG("Pig", Pig.class, 90),  SHEEP("Sheep", Sheep.class, 91),  COW("Cow", Cow.class, 92),  CHICKEN("Chicken", Chicken.class, 93),  SQUID("Squid", Squid.class, 94),  WOLF("Wolf", Wolf.class, 95),  MUSHROOM_COW("MushroomCow", MushroomCow.class, 96),  SNOWMAN("SnowMan", Snowman.class, 97),  OCELOT("Ozelot", Ocelot.class, 98),  IRON_GOLEM("VillagerGolem", IronGolem.class, 99),  HORSE("EntityHorse", Horse.class, 100),  RABBIT("Rabbit", Rabbit.class, 101),  VILLAGER("Villager", Villager.class, 120),  ENDER_CRYSTAL("EnderCrystal", EnderCrystal.class, 200),  SPLASH_POTION(
  
    null, ThrownPotion.class, -1, false),  EGG(
  
    null, Egg.class, -1, false),  FISHING_HOOK(
  
    null, Fish.class, -1, false),  LIGHTNING(
  
    null, LightningStrike.class, -1, false),  WEATHER(null, Weather.class, -1, false),  PLAYER(null, Player.class, -1, false),  COMPLEX_PART(null, ComplexEntityPart.class, -1, false),  UNKNOWN(
  
    null, null, -1, false);
  
  private String name;
  private Class<? extends Entity> clazz;
  private short typeId;
  private boolean independent;
  private boolean living;
  private static final Map<String, EntityType> NAME_MAP;
  private static final Map<Short, EntityType> ID_MAP;
  
  static
  {
    NAME_MAP = new HashMap();
    ID_MAP = new HashMap();
    EntityType[] arrayOfEntityType;
    int i = (arrayOfEntityType = values()).length;
    for (int j = 0; j < i; j++)
    {
      EntityType type = arrayOfEntityType[j];
      if (type.name != null) {
        NAME_MAP.put(type.name.toLowerCase(), type);
      }
      if (type.typeId > 0) {
        ID_MAP.put(Short.valueOf(type.typeId), type);
      }
    }
  }
  
  private EntityType(String name, Class<? extends Entity> clazz, int typeId)
  {
    this(name, clazz, typeId, true);
  }
  
  private EntityType(String name, Class<? extends Entity> clazz, int typeId, boolean independent)
  {
    this.name = name;
    this.clazz = clazz;
    this.typeId = ((short)typeId);
    this.independent = independent;
    if (clazz != null) {
      this.living = LivingEntity.class.isAssignableFrom(clazz);
    }
  }
  
  @Deprecated
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
  
  @Deprecated
  public static EntityType fromName(String name)
  {
    if (name == null) {
      return null;
    }
    return (EntityType)NAME_MAP.get(name.toLowerCase());
  }
  
  @Deprecated
  public static EntityType fromId(int id)
  {
    if (id > 32767) {
      return null;
    }
    return (EntityType)ID_MAP.get(Short.valueOf((short)id));
  }
  
  public boolean isSpawnable()
  {
    return this.independent;
  }
  
  public boolean isAlive()
  {
    return this.living;
  }
}
