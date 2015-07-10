package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTypes
{
  private static final Logger b = ;
  private static final Map<String, Class<? extends Entity>> c = Maps.newHashMap();
  private static final Map<Class<? extends Entity>, String> d = Maps.newHashMap();
  private static final Map<Integer, Class<? extends Entity>> e = Maps.newHashMap();
  private static final Map<Class<? extends Entity>, Integer> f = Maps.newHashMap();
  private static final Map<String, Integer> g = Maps.newHashMap();
  public static final Map<Integer, MonsterEggInfo> eggInfo = Maps.newLinkedHashMap();
  
  private static void a(Class<? extends Entity> ☃, String ☃, int ☃)
  {
    if (c.containsKey(☃)) {
      throw new IllegalArgumentException("ID is already registered: " + ☃);
    }
    if (e.containsKey(Integer.valueOf(☃))) {
      throw new IllegalArgumentException("ID is already registered: " + ☃);
    }
    if (☃ == 0) {
      throw new IllegalArgumentException("Cannot register to reserved id: " + ☃);
    }
    if (☃ == null) {
      throw new IllegalArgumentException("Cannot register null clazz for id: " + ☃);
    }
    c.put(☃, ☃);
    d.put(☃, ☃);
    e.put(Integer.valueOf(☃), ☃);
    f.put(☃, Integer.valueOf(☃));
    g.put(☃, Integer.valueOf(☃));
  }
  
  private static void a(Class<? extends Entity> ☃, String ☃, int ☃, int ☃, int ☃)
  {
    a(☃, ☃, ☃);
    
    eggInfo.put(Integer.valueOf(☃), new MonsterEggInfo(☃, ☃, ☃));
  }
  
  static
  {
    a(EntityItem.class, "Item", 1);
    a(EntityExperienceOrb.class, "XPOrb", 2);
    
    a(EntityEgg.class, "ThrownEgg", 7);
    a(EntityLeash.class, "LeashKnot", 8);
    a(EntityPainting.class, "Painting", 9);
    a(EntityArrow.class, "Arrow", 10);
    a(EntitySnowball.class, "Snowball", 11);
    a(EntityLargeFireball.class, "Fireball", 12);
    a(EntitySmallFireball.class, "SmallFireball", 13);
    a(EntityEnderPearl.class, "ThrownEnderpearl", 14);
    a(EntityEnderSignal.class, "EyeOfEnderSignal", 15);
    a(EntityPotion.class, "ThrownPotion", 16);
    a(EntityThrownExpBottle.class, "ThrownExpBottle", 17);
    a(EntityItemFrame.class, "ItemFrame", 18);
    a(EntityWitherSkull.class, "WitherSkull", 19);
    
    a(EntityTNTPrimed.class, "PrimedTnt", 20);
    a(EntityFallingBlock.class, "FallingSand", 21);
    
    a(EntityFireworks.class, "FireworksRocketEntity", 22);
    
    a(EntityArmorStand.class, "ArmorStand", 30);
    
    a(EntityBoat.class, "Boat", 41);
    a(EntityMinecartRideable.class, EntityMinecartAbstract.EnumMinecartType.RIDEABLE.b(), 42);
    a(EntityMinecartChest.class, EntityMinecartAbstract.EnumMinecartType.CHEST.b(), 43);
    a(EntityMinecartFurnace.class, EntityMinecartAbstract.EnumMinecartType.FURNACE.b(), 44);
    a(EntityMinecartTNT.class, EntityMinecartAbstract.EnumMinecartType.TNT.b(), 45);
    a(EntityMinecartHopper.class, EntityMinecartAbstract.EnumMinecartType.HOPPER.b(), 46);
    a(EntityMinecartMobSpawner.class, EntityMinecartAbstract.EnumMinecartType.SPAWNER.b(), 47);
    a(EntityMinecartCommandBlock.class, EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK.b(), 40);
    
    a(EntityInsentient.class, "Mob", 48);
    a(EntityMonster.class, "Monster", 49);
    
    a(EntityCreeper.class, "Creeper", 50, 894731, 0);
    a(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
    a(EntitySpider.class, "Spider", 52, 3419431, 11013646);
    a(EntityGiantZombie.class, "Giant", 53);
    a(EntityZombie.class, "Zombie", 54, 44975, 7969893);
    a(EntitySlime.class, "Slime", 55, 5349438, 8306542);
    a(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
    a(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
    a(EntityEnderman.class, "Enderman", 58, 1447446, 0);
    a(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
    a(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
    a(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
    a(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
    a(EntityEnderDragon.class, "EnderDragon", 63);
    a(EntityWither.class, "WitherBoss", 64);
    a(EntityBat.class, "Bat", 65, 4996656, 986895);
    a(EntityWitch.class, "Witch", 66, 3407872, 5349438);
    a(EntityEndermite.class, "Endermite", 67, 1447446, 7237230);
    a(EntityGuardian.class, "Guardian", 68, 5931634, 15826224);
    
    a(EntityPig.class, "Pig", 90, 15771042, 14377823);
    a(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
    a(EntityCow.class, "Cow", 92, 4470310, 10592673);
    a(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
    a(EntitySquid.class, "Squid", 94, 2243405, 7375001);
    a(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
    a(EntityMushroomCow.class, "MushroomCow", 96, 10489616, 12040119);
    a(EntitySnowman.class, "SnowMan", 97);
    a(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
    a(EntityIronGolem.class, "VillagerGolem", 99);
    a(EntityHorse.class, "EntityHorse", 100, 12623485, 15656192);
    a(EntityRabbit.class, "Rabbit", 101, 10051392, 7555121);
    
    a(EntityVillager.class, "Villager", 120, 5651507, 12422002);
    
    a(EntityEnderCrystal.class, "EnderCrystal", 200);
  }
  
  public static Entity createEntityByName(String ☃, World ☃)
  {
    Entity ☃ = null;
    try
    {
      Class<? extends Entity> ☃ = (Class)c.get(☃);
      if (☃ != null) {
        ☃ = (Entity)☃.getConstructor(new Class[] { World.class }).newInstance(new Object[] { ☃ });
      }
    }
    catch (Exception ☃)
    {
      ☃.printStackTrace();
    }
    return ☃;
  }
  
  public static Entity a(NBTTagCompound ☃, World ☃)
  {
    Entity ☃ = null;
    if ("Minecart".equals(☃.getString("id")))
    {
      ☃.setString("id", EntityMinecartAbstract.EnumMinecartType.a(☃.getInt("Type")).b());
      ☃.remove("Type");
    }
    try
    {
      Class<? extends Entity> ☃ = (Class)c.get(☃.getString("id"));
      if (☃ != null) {
        ☃ = (Entity)☃.getConstructor(new Class[] { World.class }).newInstance(new Object[] { ☃ });
      }
    }
    catch (Exception ☃)
    {
      ☃.printStackTrace();
    }
    if (☃ != null) {
      ☃.f(☃);
    } else {
      b.warn("Skipping Entity with id " + ☃.getString("id"));
    }
    return ☃;
  }
  
  public static Entity a(int ☃, World ☃)
  {
    Entity ☃ = null;
    try
    {
      Class<? extends Entity> ☃ = a(☃);
      if (☃ != null) {
        ☃ = (Entity)☃.getConstructor(new Class[] { World.class }).newInstance(new Object[] { ☃ });
      }
    }
    catch (Exception ☃)
    {
      ☃.printStackTrace();
    }
    if (☃ == null) {
      b.warn("Skipping Entity with id " + ☃);
    }
    return ☃;
  }
  
  public static int a(Entity ☃)
  {
    Integer ☃ = (Integer)f.get(☃.getClass());
    return ☃ == null ? 0 : ☃.intValue();
  }
  
  public static Class<? extends Entity> a(int ☃)
  {
    return (Class)e.get(Integer.valueOf(☃));
  }
  
  public static String b(Entity ☃)
  {
    return (String)d.get(☃.getClass());
  }
  
  public static String b(int ☃)
  {
    return (String)d.get(a(☃));
  }
  
  public static List<String> b()
  {
    Set<String> ☃ = c.keySet();
    List<String> ☃ = Lists.newArrayList();
    for (String ☃ : ☃)
    {
      Class<? extends Entity> ☃ = (Class)c.get(☃);
      if ((☃.getModifiers() & 0x400) != 1024) {
        ☃.add(☃);
      }
    }
    ☃.add("LightningBolt");
    return ☃;
  }
  
  public static boolean a(Entity ☃, String ☃)
  {
    String ☃ = b(☃);
    if ((☃ == null) && ((☃ instanceof EntityHuman))) {
      ☃ = "Player";
    } else if ((☃ == null) && ((☃ instanceof EntityLightning))) {
      ☃ = "LightningBolt";
    }
    return ☃.equals(☃);
  }
  
  public static boolean b(String ☃)
  {
    return ("Player".equals(☃)) || (b().contains(☃));
  }
  
  public static void a() {}
  
  public static class MonsterEggInfo
  {
    public final int a;
    public final int b;
    public final int c;
    public final Statistic killEntityStatistic;
    public final Statistic e;
    
    public MonsterEggInfo(int ☃, int ☃, int ☃)
    {
      this.a = ☃;
      this.b = ☃;
      this.c = ☃;
      this.killEntityStatistic = StatisticList.a(this);
      this.e = StatisticList.b(this);
    }
  }
}
