package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatisticList
{
  protected static Map<String, Statistic> a = ;
  public static List<Statistic> stats = Lists.newArrayList();
  public static List<Statistic> c = Lists.newArrayList();
  public static List<CraftingStatistic> d = Lists.newArrayList();
  public static List<CraftingStatistic> e = Lists.newArrayList();
  public static Statistic f = new CounterStatistic("stat.leaveGame", new ChatMessage("stat.leaveGame", new Object[0])).i().h();
  public static Statistic g = new CounterStatistic("stat.playOneMinute", new ChatMessage("stat.playOneMinute", new Object[0]), Statistic.h).i().h();
  public static Statistic h = new CounterStatistic("stat.timeSinceDeath", new ChatMessage("stat.timeSinceDeath", new Object[0]), Statistic.h).i().h();
  public static Statistic i = new CounterStatistic("stat.walkOneCm", new ChatMessage("stat.walkOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic j = new CounterStatistic("stat.crouchOneCm", new ChatMessage("stat.crouchOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic k = new CounterStatistic("stat.sprintOneCm", new ChatMessage("stat.sprintOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic l = new CounterStatistic("stat.swimOneCm", new ChatMessage("stat.swimOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic m = new CounterStatistic("stat.fallOneCm", new ChatMessage("stat.fallOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic n = new CounterStatistic("stat.climbOneCm", new ChatMessage("stat.climbOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic o = new CounterStatistic("stat.flyOneCm", new ChatMessage("stat.flyOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic p = new CounterStatistic("stat.diveOneCm", new ChatMessage("stat.diveOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic q = new CounterStatistic("stat.minecartOneCm", new ChatMessage("stat.minecartOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic r = new CounterStatistic("stat.boatOneCm", new ChatMessage("stat.boatOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic s = new CounterStatistic("stat.pigOneCm", new ChatMessage("stat.pigOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic t = new CounterStatistic("stat.horseOneCm", new ChatMessage("stat.horseOneCm", new Object[0]), Statistic.i).i().h();
  public static Statistic u = new CounterStatistic("stat.jump", new ChatMessage("stat.jump", new Object[0])).i().h();
  public static Statistic v = new CounterStatistic("stat.drop", new ChatMessage("stat.drop", new Object[0])).i().h();
  public static Statistic w = new CounterStatistic("stat.damageDealt", new ChatMessage("stat.damageDealt", new Object[0]), Statistic.j).h();
  public static Statistic x = new CounterStatistic("stat.damageTaken", new ChatMessage("stat.damageTaken", new Object[0]), Statistic.j).h();
  public static Statistic y = new CounterStatistic("stat.deaths", new ChatMessage("stat.deaths", new Object[0])).h();
  public static Statistic z = new CounterStatistic("stat.mobKills", new ChatMessage("stat.mobKills", new Object[0])).h();
  public static Statistic A = new CounterStatistic("stat.animalsBred", new ChatMessage("stat.animalsBred", new Object[0])).h();
  public static Statistic B = new CounterStatistic("stat.playerKills", new ChatMessage("stat.playerKills", new Object[0])).h();
  public static Statistic C = new CounterStatistic("stat.fishCaught", new ChatMessage("stat.fishCaught", new Object[0])).h();
  public static Statistic D = new CounterStatistic("stat.junkFished", new ChatMessage("stat.junkFished", new Object[0])).h();
  public static Statistic E = new CounterStatistic("stat.treasureFished", new ChatMessage("stat.treasureFished", new Object[0])).h();
  public static Statistic F = new CounterStatistic("stat.talkedToVillager", new ChatMessage("stat.talkedToVillager", new Object[0])).h();
  public static Statistic G = new CounterStatistic("stat.tradedWithVillager", new ChatMessage("stat.tradedWithVillager", new Object[0])).h();
  public static Statistic H = new CounterStatistic("stat.cakeSlicesEaten", new ChatMessage("stat.cakeSlicesEaten", new Object[0])).h();
  public static Statistic I = new CounterStatistic("stat.cauldronFilled", new ChatMessage("stat.cauldronFilled", new Object[0])).h();
  public static Statistic J = new CounterStatistic("stat.cauldronUsed", new ChatMessage("stat.cauldronUsed", new Object[0])).h();
  public static Statistic K = new CounterStatistic("stat.armorCleaned", new ChatMessage("stat.armorCleaned", new Object[0])).h();
  public static Statistic L = new CounterStatistic("stat.bannerCleaned", new ChatMessage("stat.bannerCleaned", new Object[0])).h();
  public static Statistic M = new CounterStatistic("stat.brewingstandInteraction", new ChatMessage("stat.brewingstandInteraction", new Object[0])).h();
  public static Statistic N = new CounterStatistic("stat.beaconInteraction", new ChatMessage("stat.beaconInteraction", new Object[0])).h();
  public static Statistic O = new CounterStatistic("stat.dropperInspected", new ChatMessage("stat.dropperInspected", new Object[0])).h();
  public static Statistic P = new CounterStatistic("stat.hopperInspected", new ChatMessage("stat.hopperInspected", new Object[0])).h();
  public static Statistic Q = new CounterStatistic("stat.dispenserInspected", new ChatMessage("stat.dispenserInspected", new Object[0])).h();
  public static Statistic R = new CounterStatistic("stat.noteblockPlayed", new ChatMessage("stat.noteblockPlayed", new Object[0])).h();
  public static Statistic S = new CounterStatistic("stat.noteblockTuned", new ChatMessage("stat.noteblockTuned", new Object[0])).h();
  public static Statistic T = new CounterStatistic("stat.flowerPotted", new ChatMessage("stat.flowerPotted", new Object[0])).h();
  public static Statistic U = new CounterStatistic("stat.trappedChestTriggered", new ChatMessage("stat.trappedChestTriggered", new Object[0])).h();
  public static Statistic V = new CounterStatistic("stat.enderchestOpened", new ChatMessage("stat.enderchestOpened", new Object[0])).h();
  public static Statistic W = new CounterStatistic("stat.itemEnchanted", new ChatMessage("stat.itemEnchanted", new Object[0])).h();
  public static Statistic X = new CounterStatistic("stat.recordPlayed", new ChatMessage("stat.recordPlayed", new Object[0])).h();
  public static Statistic Y = new CounterStatistic("stat.furnaceInteraction", new ChatMessage("stat.furnaceInteraction", new Object[0])).h();
  public static Statistic Z = new CounterStatistic("stat.craftingTableInteraction", new ChatMessage("stat.workbenchInteraction", new Object[0])).h();
  public static Statistic aa = new CounterStatistic("stat.chestOpened", new ChatMessage("stat.chestOpened", new Object[0])).h();
  public static final Statistic[] MINE_BLOCK_COUNT = new Statistic['က'];
  public static final Statistic[] CRAFT_BLOCK_COUNT = new Statistic['紀'];
  public static final Statistic[] USE_ITEM_COUNT = new Statistic['紀'];
  public static final Statistic[] BREAK_ITEM_COUNT = new Statistic['紀'];
  
  public static void a()
  {
    c();
    d();
    e();
    b();
    
    AchievementList.a();
    EntityTypes.a();
  }
  
  private static void b()
  {
    Set<Item> ☃ = Sets.newHashSet();
    for (IRecipe ☃ : CraftingManager.getInstance().getRecipes()) {
      if (☃.b() != null) {
        ☃.add(☃.b().getItem());
      }
    }
    for (ItemStack ☃ : RecipesFurnace.getInstance().getRecipes().values()) {
      ☃.add(☃.getItem());
    }
    for (Item ☃ : ☃) {
      if (☃ != null)
      {
        int ☃ = Item.getId(☃);
        String ☃ = a(☃);
        if (☃ != null) {
          CRAFT_BLOCK_COUNT[☃] = new CraftingStatistic("stat.craftItem.", ☃, new ChatMessage("stat.craftItem", new Object[] { new ItemStack(☃).C() }), ☃).h();
        }
      }
    }
    a(CRAFT_BLOCK_COUNT);
  }
  
  private static void c()
  {
    for (Block ☃ : Block.REGISTRY)
    {
      Item ☃ = Item.getItemOf(☃);
      if (☃ != null)
      {
        int ☃ = Block.getId(☃);
        String ☃ = a(☃);
        if ((☃ != null) && (☃.J()))
        {
          MINE_BLOCK_COUNT[☃] = new CraftingStatistic("stat.mineBlock.", ☃, new ChatMessage("stat.mineBlock", new Object[] { new ItemStack(☃).C() }), ☃).h();
          e.add((CraftingStatistic)MINE_BLOCK_COUNT[☃]);
        }
      }
    }
    a(MINE_BLOCK_COUNT);
  }
  
  private static void d()
  {
    for (Item ☃ : Item.REGISTRY) {
      if (☃ != null)
      {
        int ☃ = Item.getId(☃);
        String ☃ = a(☃);
        if (☃ != null)
        {
          USE_ITEM_COUNT[☃] = new CraftingStatistic("stat.useItem.", ☃, new ChatMessage("stat.useItem", new Object[] { new ItemStack(☃).C() }), ☃).h();
          if (!(☃ instanceof ItemBlock)) {
            d.add((CraftingStatistic)USE_ITEM_COUNT[☃]);
          }
        }
      }
    }
    a(USE_ITEM_COUNT);
  }
  
  private static void e()
  {
    for (Item ☃ : Item.REGISTRY) {
      if (☃ != null)
      {
        int ☃ = Item.getId(☃);
        String ☃ = a(☃);
        if ((☃ != null) && (☃.usesDurability())) {
          BREAK_ITEM_COUNT[☃] = new CraftingStatistic("stat.breakItem.", ☃, new ChatMessage("stat.breakItem", new Object[] { new ItemStack(☃).C() }), ☃).h();
        }
      }
    }
    a(BREAK_ITEM_COUNT);
  }
  
  private static String a(Item ☃)
  {
    MinecraftKey ☃ = (MinecraftKey)Item.REGISTRY.c(☃);
    if (☃ != null) {
      return ☃.toString().replace(':', '.');
    }
    return null;
  }
  
  private static void a(Statistic[] ☃)
  {
    a(☃, Blocks.WATER, Blocks.FLOWING_WATER);
    a(☃, Blocks.LAVA, Blocks.FLOWING_LAVA);
    
    a(☃, Blocks.LIT_PUMPKIN, Blocks.PUMPKIN);
    a(☃, Blocks.LIT_FURNACE, Blocks.FURNACE);
    a(☃, Blocks.LIT_REDSTONE_ORE, Blocks.REDSTONE_ORE);
    
    a(☃, Blocks.POWERED_REPEATER, Blocks.UNPOWERED_REPEATER);
    a(☃, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_COMPARATOR);
    a(☃, Blocks.REDSTONE_TORCH, Blocks.UNLIT_REDSTONE_TORCH);
    a(☃, Blocks.LIT_REDSTONE_LAMP, Blocks.REDSTONE_LAMP);
    
    a(☃, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB);
    a(☃, Blocks.DOUBLE_WOODEN_SLAB, Blocks.WOODEN_SLAB);
    a(☃, Blocks.DOUBLE_STONE_SLAB2, Blocks.STONE_SLAB2);
    
    a(☃, Blocks.GRASS, Blocks.DIRT);
    a(☃, Blocks.FARMLAND, Blocks.DIRT);
  }
  
  private static void a(Statistic[] ☃, Block ☃, Block ☃)
  {
    int ☃ = Block.getId(☃);
    int ☃ = Block.getId(☃);
    if ((☃[☃] != null) && (☃[☃] == null))
    {
      ☃[☃] = ☃[☃];
      return;
    }
    stats.remove(☃[☃]);
    e.remove(☃[☃]);
    c.remove(☃[☃]);
    ☃[☃] = ☃[☃];
  }
  
  public static Statistic a(EntityTypes.MonsterEggInfo ☃)
  {
    String ☃ = EntityTypes.b(☃.a);
    if (☃ == null) {
      return null;
    }
    return new Statistic("stat.killEntity." + ☃, new ChatMessage("stat.entityKill", new Object[] { new ChatMessage("entity." + ☃ + ".name", new Object[0]) })).h();
  }
  
  public static Statistic b(EntityTypes.MonsterEggInfo ☃)
  {
    String ☃ = EntityTypes.b(☃.a);
    if (☃ == null) {
      return null;
    }
    return new Statistic("stat.entityKilledBy." + ☃, new ChatMessage("stat.entityKilledBy", new Object[] { new ChatMessage("entity." + ☃ + ".name", new Object[0]) })).h();
  }
  
  public static Statistic getStatistic(String ☃)
  {
    return (Statistic)a.get(☃);
  }
}
