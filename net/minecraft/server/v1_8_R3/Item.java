package net.minecraft.server.v1_8_R3;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Item
{
  public static final RegistryMaterials<MinecraftKey, Item> REGISTRY = new RegistryMaterials();
  private static final Map<Block, Item> a = Maps.newHashMap();
  protected static final UUID f = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
  private CreativeModeTab b;
  
  public static int getId(Item ☃)
  {
    return ☃ == null ? 0 : REGISTRY.b(☃);
  }
  
  public static Item getById(int ☃)
  {
    return (Item)REGISTRY.a(☃);
  }
  
  public static Item getItemOf(Block ☃)
  {
    return (Item)a.get(☃);
  }
  
  public static Item d(String ☃)
  {
    Item ☃ = (Item)REGISTRY.get(new MinecraftKey(☃));
    if (☃ == null) {
      try
      {
        return getById(Integer.parseInt(☃));
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    return ☃;
  }
  
  public boolean a(NBTTagCompound ☃)
  {
    return false;
  }
  
  public static enum EnumToolMaterial
  {
    private final int f;
    private final int g;
    private final float h;
    private final float i;
    private final int j;
    
    private EnumToolMaterial(int ☃, int ☃, float ☃, float ☃, int ☃)
    {
      this.f = ☃;
      this.g = ☃;
      this.h = ☃;
      this.i = ☃;
      this.j = ☃;
    }
    
    public int a()
    {
      return this.g;
    }
    
    public float b()
    {
      return this.h;
    }
    
    public float c()
    {
      return this.i;
    }
    
    public int d()
    {
      return this.f;
    }
    
    public int e()
    {
      return this.j;
    }
    
    public Item f()
    {
      if (this == WOOD) {
        return Item.getItemOf(Blocks.PLANKS);
      }
      if (this == STONE) {
        return Item.getItemOf(Blocks.COBBLESTONE);
      }
      if (this == GOLD) {
        return Items.GOLD_INGOT;
      }
      if (this == IRON) {
        return Items.IRON_INGOT;
      }
      if (this == EMERALD) {
        return Items.DIAMOND;
      }
      return null;
    }
  }
  
  protected static Random g = new Random();
  protected int maxStackSize = 64;
  private int durability;
  protected boolean i;
  protected boolean j;
  private Item craftingResult;
  private String k;
  private String name;
  
  public Item c(int ☃)
  {
    this.maxStackSize = ☃;
    return this;
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    return false;
  }
  
  public float getDestroySpeed(ItemStack ☃, Block ☃)
  {
    return 1.0F;
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    return ☃;
  }
  
  public ItemStack b(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    return ☃;
  }
  
  public int getMaxStackSize()
  {
    return this.maxStackSize;
  }
  
  public int filterData(int ☃)
  {
    return 0;
  }
  
  public boolean k()
  {
    return this.j;
  }
  
  protected Item a(boolean ☃)
  {
    this.j = ☃;
    return this;
  }
  
  public int getMaxDurability()
  {
    return this.durability;
  }
  
  protected Item setMaxDurability(int ☃)
  {
    this.durability = ☃;
    return this;
  }
  
  public boolean usesDurability()
  {
    return (this.durability > 0) && (!this.j);
  }
  
  public boolean a(ItemStack ☃, EntityLiving ☃, EntityLiving ☃)
  {
    return false;
  }
  
  public boolean a(ItemStack ☃, World ☃, Block ☃, BlockPosition ☃, EntityLiving ☃)
  {
    return false;
  }
  
  public boolean canDestroySpecialBlock(Block ☃)
  {
    return false;
  }
  
  public boolean a(ItemStack ☃, EntityHuman ☃, EntityLiving ☃)
  {
    return false;
  }
  
  public Item n()
  {
    this.i = true;
    return this;
  }
  
  public Item c(String ☃)
  {
    this.name = ☃;
    return this;
  }
  
  public String k(ItemStack ☃)
  {
    String ☃ = e_(☃);
    if (☃ == null) {
      return "";
    }
    return LocaleI18n.get(☃);
  }
  
  public String getName()
  {
    return "item." + this.name;
  }
  
  public String e_(ItemStack ☃)
  {
    return "item." + this.name;
  }
  
  public Item c(Item ☃)
  {
    this.craftingResult = ☃;
    return this;
  }
  
  public boolean p()
  {
    return true;
  }
  
  public Item q()
  {
    return this.craftingResult;
  }
  
  public boolean r()
  {
    return this.craftingResult != null;
  }
  
  public void a(ItemStack ☃, World ☃, Entity ☃, int ☃, boolean ☃) {}
  
  public void d(ItemStack ☃, World ☃, EntityHuman ☃) {}
  
  public boolean f()
  {
    return false;
  }
  
  public EnumAnimation e(ItemStack ☃)
  {
    return EnumAnimation.NONE;
  }
  
  public int d(ItemStack ☃)
  {
    return 0;
  }
  
  public void a(ItemStack ☃, World ☃, EntityHuman ☃, int ☃) {}
  
  protected Item e(String ☃)
  {
    this.k = ☃;
    return this;
  }
  
  public String j(ItemStack ☃)
  {
    return this.k;
  }
  
  public boolean l(ItemStack ☃)
  {
    return j(☃) != null;
  }
  
  public String a(ItemStack ☃)
  {
    return ("" + LocaleI18n.get(new StringBuilder().append(k(☃)).append(".name").toString())).trim();
  }
  
  public EnumItemRarity g(ItemStack ☃)
  {
    if (☃.hasEnchantments()) {
      return EnumItemRarity.RARE;
    }
    return EnumItemRarity.COMMON;
  }
  
  public boolean f_(ItemStack ☃)
  {
    return (getMaxStackSize() == 1) && (usesDurability());
  }
  
  protected MovingObjectPosition a(World ☃, EntityHuman ☃, boolean ☃)
  {
    float ☃ = ☃.pitch;
    float ☃ = ☃.yaw;
    
    double ☃ = ☃.locX;
    double ☃ = ☃.locY + ☃.getHeadHeight();
    double ☃ = ☃.locZ;
    
    Vec3D ☃ = new Vec3D(☃, ☃, ☃);
    
    float ☃ = MathHelper.cos(-☃ * 0.017453292F - 3.1415927F);
    float ☃ = MathHelper.sin(-☃ * 0.017453292F - 3.1415927F);
    float ☃ = -MathHelper.cos(-☃ * 0.017453292F);
    float ☃ = MathHelper.sin(-☃ * 0.017453292F);
    
    float ☃ = ☃ * ☃;
    float ☃ = ☃;
    float ☃ = ☃ * ☃;
    
    double ☃ = 5.0D;
    Vec3D ☃ = ☃.add(☃ * ☃, ☃ * ☃, ☃ * ☃);
    
    return ☃.rayTrace(☃, ☃, ☃, !☃, false);
  }
  
  public int b()
  {
    return 0;
  }
  
  public Item a(CreativeModeTab ☃)
  {
    this.b = ☃;
    return this;
  }
  
  public boolean s()
  {
    return false;
  }
  
  public boolean a(ItemStack ☃, ItemStack ☃)
  {
    return false;
  }
  
  public Multimap<String, AttributeModifier> i()
  {
    return HashMultimap.create();
  }
  
  public static void t()
  {
    a(Blocks.STONE, new ItemMultiTexture(Blocks.STONE, Blocks.STONE, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockStone.EnumStoneVariant.a(☃.getData()).d();
      }
    }).b("stone"));
    
    a(Blocks.GRASS, new ItemWithAuxData(Blocks.GRASS, false));
    a(Blocks.DIRT, new ItemMultiTexture(Blocks.DIRT, Blocks.DIRT, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockDirt.EnumDirtVariant.a(☃.getData()).c();
      }
    }).b("dirt"));
    
    c(Blocks.COBBLESTONE);
    a(Blocks.PLANKS, new ItemMultiTexture(Blocks.PLANKS, Blocks.PLANKS, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockWood.EnumLogVariant.a(☃.getData()).d();
      }
    }).b("wood"));
    
    a(Blocks.SAPLING, new ItemMultiTexture(Blocks.SAPLING, Blocks.SAPLING, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockWood.EnumLogVariant.a(☃.getData()).d();
      }
    }).b("sapling"));
    
    c(Blocks.BEDROCK);
    a(Blocks.SAND, new ItemMultiTexture(Blocks.SAND, Blocks.SAND, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockSand.EnumSandVariant.a(☃.getData()).d();
      }
    }).b("sand"));
    
    c(Blocks.GRAVEL);
    c(Blocks.GOLD_ORE);
    c(Blocks.IRON_ORE);
    c(Blocks.COAL_ORE);
    a(Blocks.LOG, new ItemMultiTexture(Blocks.LOG, Blocks.LOG, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockWood.EnumLogVariant.a(☃.getData()).d();
      }
    }).b("log"));
    
    a(Blocks.LOG2, new ItemMultiTexture(Blocks.LOG2, Blocks.LOG2, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockWood.EnumLogVariant.a(☃.getData() + 4).d();
      }
    }).b("log"));
    
    a(Blocks.LEAVES, new ItemLeaves(Blocks.LEAVES).b("leaves"));
    a(Blocks.LEAVES2, new ItemLeaves(Blocks.LEAVES2).b("leaves"));
    a(Blocks.SPONGE, new ItemMultiTexture(Blocks.SPONGE, Blocks.SPONGE, new Function()
    {
      public String a(ItemStack ☃)
      {
        return (☃.getData() & 0x1) == 1 ? "wet" : "dry";
      }
    }).b("sponge"));
    
    c(Blocks.GLASS);
    c(Blocks.LAPIS_ORE);
    c(Blocks.LAPIS_BLOCK);
    c(Blocks.DISPENSER);
    a(Blocks.SANDSTONE, new ItemMultiTexture(Blocks.SANDSTONE, Blocks.SANDSTONE, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockSandStone.EnumSandstoneVariant.a(☃.getData()).c();
      }
    }).b("sandStone"));
    
    c(Blocks.NOTEBLOCK);
    c(Blocks.GOLDEN_RAIL);
    c(Blocks.DETECTOR_RAIL);
    a(Blocks.STICKY_PISTON, new ItemPiston(Blocks.STICKY_PISTON));
    c(Blocks.WEB);
    a(Blocks.TALLGRASS, new ItemWithAuxData(Blocks.TALLGRASS, true).a(new String[] { "shrub", "grass", "fern" }));
    c(Blocks.DEADBUSH);
    a(Blocks.PISTON, new ItemPiston(Blocks.PISTON));
    a(Blocks.WOOL, new ItemCloth(Blocks.WOOL).b("cloth"));
    a(Blocks.YELLOW_FLOWER, new ItemMultiTexture(Blocks.YELLOW_FLOWER, Blocks.YELLOW_FLOWER, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockFlowers.EnumFlowerVarient.a(BlockFlowers.EnumFlowerType.YELLOW, ☃.getData()).d();
      }
    }).b("flower"));
    
    a(Blocks.RED_FLOWER, new ItemMultiTexture(Blocks.RED_FLOWER, Blocks.RED_FLOWER, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockFlowers.EnumFlowerVarient.a(BlockFlowers.EnumFlowerType.RED, ☃.getData()).d();
      }
    }).b("rose"));
    
    c(Blocks.BROWN_MUSHROOM);
    c(Blocks.RED_MUSHROOM);
    c(Blocks.GOLD_BLOCK);
    c(Blocks.IRON_BLOCK);
    a(Blocks.STONE_SLAB, new ItemStep(Blocks.STONE_SLAB, Blocks.STONE_SLAB, Blocks.DOUBLE_STONE_SLAB).b("stoneSlab"));
    c(Blocks.BRICK_BLOCK);
    c(Blocks.TNT);
    c(Blocks.BOOKSHELF);
    c(Blocks.MOSSY_COBBLESTONE);
    c(Blocks.OBSIDIAN);
    c(Blocks.TORCH);
    c(Blocks.MOB_SPAWNER);
    c(Blocks.OAK_STAIRS);
    c(Blocks.CHEST);
    c(Blocks.DIAMOND_ORE);
    c(Blocks.DIAMOND_BLOCK);
    c(Blocks.CRAFTING_TABLE);
    c(Blocks.FARMLAND);
    c(Blocks.FURNACE);
    c(Blocks.LIT_FURNACE);
    c(Blocks.LADDER);
    c(Blocks.RAIL);
    c(Blocks.STONE_STAIRS);
    c(Blocks.LEVER);
    c(Blocks.STONE_PRESSURE_PLATE);
    c(Blocks.WOODEN_PRESSURE_PLATE);
    c(Blocks.REDSTONE_ORE);
    c(Blocks.REDSTONE_TORCH);
    c(Blocks.STONE_BUTTON);
    a(Blocks.SNOW_LAYER, new ItemSnow(Blocks.SNOW_LAYER));
    c(Blocks.ICE);
    c(Blocks.SNOW);
    c(Blocks.CACTUS);
    c(Blocks.CLAY);
    c(Blocks.JUKEBOX);
    c(Blocks.FENCE);
    c(Blocks.SPRUCE_FENCE);
    c(Blocks.BIRCH_FENCE);
    c(Blocks.JUNGLE_FENCE);
    c(Blocks.DARK_OAK_FENCE);
    c(Blocks.ACACIA_FENCE);
    c(Blocks.PUMPKIN);
    c(Blocks.NETHERRACK);
    c(Blocks.SOUL_SAND);
    c(Blocks.GLOWSTONE);
    c(Blocks.LIT_PUMPKIN);
    c(Blocks.TRAPDOOR);
    a(Blocks.MONSTER_EGG, new ItemMultiTexture(Blocks.MONSTER_EGG, Blocks.MONSTER_EGG, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockMonsterEggs.EnumMonsterEggVarient.a(☃.getData()).c();
      }
    }).b("monsterStoneEgg"));
    
    a(Blocks.STONEBRICK, new ItemMultiTexture(Blocks.STONEBRICK, Blocks.STONEBRICK, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockSmoothBrick.EnumStonebrickType.a(☃.getData()).c();
      }
    }).b("stonebricksmooth"));
    
    c(Blocks.BROWN_MUSHROOM_BLOCK);
    c(Blocks.RED_MUSHROOM_BLOCK);
    c(Blocks.IRON_BARS);
    c(Blocks.GLASS_PANE);
    c(Blocks.MELON_BLOCK);
    a(Blocks.VINE, new ItemWithAuxData(Blocks.VINE, false));
    c(Blocks.FENCE_GATE);
    c(Blocks.SPRUCE_FENCE_GATE);
    c(Blocks.BIRCH_FENCE_GATE);
    c(Blocks.JUNGLE_FENCE_GATE);
    c(Blocks.DARK_OAK_FENCE_GATE);
    c(Blocks.ACACIA_FENCE_GATE);
    c(Blocks.BRICK_STAIRS);
    c(Blocks.STONE_BRICK_STAIRS);
    c(Blocks.MYCELIUM);
    a(Blocks.WATERLILY, new ItemWaterLily(Blocks.WATERLILY));
    c(Blocks.NETHER_BRICK);
    c(Blocks.NETHER_BRICK_FENCE);
    c(Blocks.NETHER_BRICK_STAIRS);
    c(Blocks.ENCHANTING_TABLE);
    c(Blocks.END_PORTAL_FRAME);
    c(Blocks.END_STONE);
    c(Blocks.DRAGON_EGG);
    c(Blocks.REDSTONE_LAMP);
    a(Blocks.WOODEN_SLAB, new ItemStep(Blocks.WOODEN_SLAB, Blocks.WOODEN_SLAB, Blocks.DOUBLE_WOODEN_SLAB).b("woodSlab"));
    c(Blocks.SANDSTONE_STAIRS);
    c(Blocks.EMERALD_ORE);
    c(Blocks.ENDER_CHEST);
    c(Blocks.TRIPWIRE_HOOK);
    c(Blocks.EMERALD_BLOCK);
    c(Blocks.SPRUCE_STAIRS);
    c(Blocks.BIRCH_STAIRS);
    c(Blocks.JUNGLE_STAIRS);
    c(Blocks.COMMAND_BLOCK);
    c(Blocks.BEACON);
    a(Blocks.COBBLESTONE_WALL, new ItemMultiTexture(Blocks.COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockCobbleWall.EnumCobbleVariant.a(☃.getData()).c();
      }
    }).b("cobbleWall"));
    
    c(Blocks.WOODEN_BUTTON);
    a(Blocks.ANVIL, new ItemAnvil(Blocks.ANVIL).b("anvil"));
    c(Blocks.TRAPPED_CHEST);
    c(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
    c(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
    c(Blocks.DAYLIGHT_DETECTOR);
    c(Blocks.REDSTONE_BLOCK);
    c(Blocks.QUARTZ_ORE);
    c(Blocks.HOPPER);
    a(Blocks.QUARTZ_BLOCK, new ItemMultiTexture(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, new String[] { "default", "chiseled", "lines" }).b("quartzBlock"));
    c(Blocks.QUARTZ_STAIRS);
    c(Blocks.ACTIVATOR_RAIL);
    c(Blocks.DROPPER);
    a(Blocks.STAINED_HARDENED_CLAY, new ItemCloth(Blocks.STAINED_HARDENED_CLAY).b("clayHardenedStained"));
    c(Blocks.BARRIER);
    c(Blocks.IRON_TRAPDOOR);
    c(Blocks.HAY_BLOCK);
    a(Blocks.CARPET, new ItemCloth(Blocks.CARPET).b("woolCarpet"));
    c(Blocks.HARDENED_CLAY);
    c(Blocks.COAL_BLOCK);
    c(Blocks.PACKED_ICE);
    c(Blocks.ACACIA_STAIRS);
    c(Blocks.DARK_OAK_STAIRS);
    c(Blocks.SLIME);
    a(Blocks.DOUBLE_PLANT, new ItemTallPlant(Blocks.DOUBLE_PLANT, Blocks.DOUBLE_PLANT, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockTallPlant.EnumTallFlowerVariants.a(☃.getData()).c();
      }
    }).b("doublePlant"));
    
    a(Blocks.STAINED_GLASS, new ItemCloth(Blocks.STAINED_GLASS).b("stainedGlass"));
    a(Blocks.STAINED_GLASS_PANE, new ItemCloth(Blocks.STAINED_GLASS_PANE).b("stainedGlassPane"));
    a(Blocks.PRISMARINE, new ItemMultiTexture(Blocks.PRISMARINE, Blocks.PRISMARINE, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockPrismarine.EnumPrismarineVariant.a(☃.getData()).c();
      }
    }).b("prismarine"));
    
    c(Blocks.SEA_LANTERN);
    a(Blocks.RED_SANDSTONE, new ItemMultiTexture(Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE, new Function()
    {
      public String a(ItemStack ☃)
      {
        return BlockRedSandstone.EnumRedSandstoneVariant.a(☃.getData()).c();
      }
    }).b("redSandStone"));
    
    c(Blocks.RED_SANDSTONE_STAIRS);
    a(Blocks.STONE_SLAB2, new ItemStep(Blocks.STONE_SLAB2, Blocks.STONE_SLAB2, Blocks.DOUBLE_STONE_SLAB2).b("stoneSlab2"));
    
    a(256, "iron_shovel", new ItemSpade(EnumToolMaterial.IRON).c("shovelIron"));
    a(257, "iron_pickaxe", new ItemPickaxe(EnumToolMaterial.IRON).c("pickaxeIron"));
    a(258, "iron_axe", new ItemAxe(EnumToolMaterial.IRON).c("hatchetIron"));
    a(259, "flint_and_steel", new ItemFlintAndSteel().c("flintAndSteel"));
    a(260, "apple", new ItemFood(4, 0.3F, false).c("apple"));
    a(261, "bow", new ItemBow().c("bow"));
    a(262, "arrow", new Item().c("arrow").a(CreativeModeTab.j));
    a(263, "coal", new ItemCoal().c("coal"));
    a(264, "diamond", new Item().c("diamond").a(CreativeModeTab.l));
    a(265, "iron_ingot", new Item().c("ingotIron").a(CreativeModeTab.l));
    a(266, "gold_ingot", new Item().c("ingotGold").a(CreativeModeTab.l));
    a(267, "iron_sword", new ItemSword(EnumToolMaterial.IRON).c("swordIron"));
    a(268, "wooden_sword", new ItemSword(EnumToolMaterial.WOOD).c("swordWood"));
    a(269, "wooden_shovel", new ItemSpade(EnumToolMaterial.WOOD).c("shovelWood"));
    a(270, "wooden_pickaxe", new ItemPickaxe(EnumToolMaterial.WOOD).c("pickaxeWood"));
    a(271, "wooden_axe", new ItemAxe(EnumToolMaterial.WOOD).c("hatchetWood"));
    a(272, "stone_sword", new ItemSword(EnumToolMaterial.STONE).c("swordStone"));
    a(273, "stone_shovel", new ItemSpade(EnumToolMaterial.STONE).c("shovelStone"));
    a(274, "stone_pickaxe", new ItemPickaxe(EnumToolMaterial.STONE).c("pickaxeStone"));
    a(275, "stone_axe", new ItemAxe(EnumToolMaterial.STONE).c("hatchetStone"));
    a(276, "diamond_sword", new ItemSword(EnumToolMaterial.EMERALD).c("swordDiamond"));
    a(277, "diamond_shovel", new ItemSpade(EnumToolMaterial.EMERALD).c("shovelDiamond"));
    a(278, "diamond_pickaxe", new ItemPickaxe(EnumToolMaterial.EMERALD).c("pickaxeDiamond"));
    a(279, "diamond_axe", new ItemAxe(EnumToolMaterial.EMERALD).c("hatchetDiamond"));
    a(280, "stick", new Item().n().c("stick").a(CreativeModeTab.l));
    a(281, "bowl", new Item().c("bowl").a(CreativeModeTab.l));
    a(282, "mushroom_stew", new ItemSoup(6).c("mushroomStew"));
    a(283, "golden_sword", new ItemSword(EnumToolMaterial.GOLD).c("swordGold"));
    a(284, "golden_shovel", new ItemSpade(EnumToolMaterial.GOLD).c("shovelGold"));
    a(285, "golden_pickaxe", new ItemPickaxe(EnumToolMaterial.GOLD).c("pickaxeGold"));
    a(286, "golden_axe", new ItemAxe(EnumToolMaterial.GOLD).c("hatchetGold"));
    a(287, "string", new ItemReed(Blocks.TRIPWIRE).c("string").a(CreativeModeTab.l));
    a(288, "feather", new Item().c("feather").a(CreativeModeTab.l));
    a(289, "gunpowder", new Item().c("sulphur").e(PotionBrewer.k).a(CreativeModeTab.l));
    a(290, "wooden_hoe", new ItemHoe(EnumToolMaterial.WOOD).c("hoeWood"));
    a(291, "stone_hoe", new ItemHoe(EnumToolMaterial.STONE).c("hoeStone"));
    a(292, "iron_hoe", new ItemHoe(EnumToolMaterial.IRON).c("hoeIron"));
    a(293, "diamond_hoe", new ItemHoe(EnumToolMaterial.EMERALD).c("hoeDiamond"));
    a(294, "golden_hoe", new ItemHoe(EnumToolMaterial.GOLD).c("hoeGold"));
    a(295, "wheat_seeds", new ItemSeeds(Blocks.WHEAT, Blocks.FARMLAND).c("seeds"));
    a(296, "wheat", new Item().c("wheat").a(CreativeModeTab.l));
    a(297, "bread", new ItemFood(5, 0.6F, false).c("bread"));
    a(298, "leather_helmet", new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 0).c("helmetCloth"));
    a(299, "leather_chestplate", new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 1).c("chestplateCloth"));
    a(300, "leather_leggings", new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 2).c("leggingsCloth"));
    a(301, "leather_boots", new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 3).c("bootsCloth"));
    a(302, "chainmail_helmet", new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 0).c("helmetChain"));
    a(303, "chainmail_chestplate", new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 1).c("chestplateChain"));
    a(304, "chainmail_leggings", new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 2).c("leggingsChain"));
    a(305, "chainmail_boots", new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 3).c("bootsChain"));
    a(306, "iron_helmet", new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 0).c("helmetIron"));
    a(307, "iron_chestplate", new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 1).c("chestplateIron"));
    a(308, "iron_leggings", new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 2).c("leggingsIron"));
    a(309, "iron_boots", new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 3).c("bootsIron"));
    a(310, "diamond_helmet", new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 0).c("helmetDiamond"));
    a(311, "diamond_chestplate", new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 1).c("chestplateDiamond"));
    a(312, "diamond_leggings", new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 2).c("leggingsDiamond"));
    a(313, "diamond_boots", new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 3).c("bootsDiamond"));
    a(314, "golden_helmet", new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 0).c("helmetGold"));
    a(315, "golden_chestplate", new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 1).c("chestplateGold"));
    a(316, "golden_leggings", new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 2).c("leggingsGold"));
    a(317, "golden_boots", new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 3).c("bootsGold"));
    a(318, "flint", new Item().c("flint").a(CreativeModeTab.l));
    a(319, "porkchop", new ItemFood(3, 0.3F, true).c("porkchopRaw"));
    a(320, "cooked_porkchop", new ItemFood(8, 0.8F, true).c("porkchopCooked"));
    a(321, "painting", new ItemHanging(EntityPainting.class).c("painting"));
    a(322, "golden_apple", new ItemGoldenApple(4, 1.2F, false).h().a(MobEffectList.REGENERATION.id, 5, 1, 1.0F).c("appleGold"));
    a(323, "sign", new ItemSign().c("sign"));
    a(324, "wooden_door", new ItemDoor(Blocks.WOODEN_DOOR).c("doorOak"));
    Item ☃ = new ItemBucket(Blocks.AIR).c("bucket").c(16);
    a(325, "bucket", ☃);
    a(326, "water_bucket", new ItemBucket(Blocks.FLOWING_WATER).c("bucketWater").c(☃));
    a(327, "lava_bucket", new ItemBucket(Blocks.FLOWING_LAVA).c("bucketLava").c(☃));
    a(328, "minecart", new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.RIDEABLE).c("minecart"));
    a(329, "saddle", new ItemSaddle().c("saddle"));
    a(330, "iron_door", new ItemDoor(Blocks.IRON_DOOR).c("doorIron"));
    a(331, "redstone", new ItemRedstone().c("redstone").e(PotionBrewer.i));
    a(332, "snowball", new ItemSnowball().c("snowball"));
    a(333, "boat", new ItemBoat().c("boat"));
    a(334, "leather", new Item().c("leather").a(CreativeModeTab.l));
    a(335, "milk_bucket", new ItemMilkBucket().c("milk").c(☃));
    a(336, "brick", new Item().c("brick").a(CreativeModeTab.l));
    a(337, "clay_ball", new Item().c("clay").a(CreativeModeTab.l));
    a(338, "reeds", new ItemReed(Blocks.REEDS).c("reeds").a(CreativeModeTab.l));
    a(339, "paper", new Item().c("paper").a(CreativeModeTab.f));
    a(340, "book", new ItemBook().c("book").a(CreativeModeTab.f));
    a(341, "slime_ball", new Item().c("slimeball").a(CreativeModeTab.f));
    a(342, "chest_minecart", new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.CHEST).c("minecartChest"));
    a(343, "furnace_minecart", new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.FURNACE).c("minecartFurnace"));
    a(344, "egg", new ItemEgg().c("egg"));
    a(345, "compass", new Item().c("compass").a(CreativeModeTab.i));
    a(346, "fishing_rod", new ItemFishingRod().c("fishingRod"));
    a(347, "clock", new Item().c("clock").a(CreativeModeTab.i));
    a(348, "glowstone_dust", new Item().c("yellowDust").e(PotionBrewer.j).a(CreativeModeTab.l));
    a(349, "fish", new ItemFish(false).c("fish").a(true));
    a(350, "cooked_fish", new ItemFish(true).c("fish").a(true));
    a(351, "dye", new ItemDye().c("dyePowder"));
    a(352, "bone", new Item().c("bone").n().a(CreativeModeTab.f));
    a(353, "sugar", new Item().c("sugar").e(PotionBrewer.b).a(CreativeModeTab.l));
    a(354, "cake", new ItemReed(Blocks.CAKE).c(1).c("cake").a(CreativeModeTab.h));
    a(355, "bed", new ItemBed().c(1).c("bed"));
    a(356, "repeater", new ItemReed(Blocks.UNPOWERED_REPEATER).c("diode").a(CreativeModeTab.d));
    a(357, "cookie", new ItemFood(2, 0.1F, false).c("cookie"));
    a(358, "filled_map", new ItemWorldMap().c("map"));
    a(359, "shears", new ItemShears().c("shears"));
    a(360, "melon", new ItemFood(2, 0.3F, false).c("melon"));
    a(361, "pumpkin_seeds", new ItemSeeds(Blocks.PUMPKIN_STEM, Blocks.FARMLAND).c("seeds_pumpkin"));
    a(362, "melon_seeds", new ItemSeeds(Blocks.MELON_STEM, Blocks.FARMLAND).c("seeds_melon"));
    a(363, "beef", new ItemFood(3, 0.3F, true).c("beefRaw"));
    a(364, "cooked_beef", new ItemFood(8, 0.8F, true).c("beefCooked"));
    a(365, "chicken", new ItemFood(2, 0.3F, true).a(MobEffectList.HUNGER.id, 30, 0, 0.3F).c("chickenRaw"));
    a(366, "cooked_chicken", new ItemFood(6, 0.6F, true).c("chickenCooked"));
    a(367, "rotten_flesh", new ItemFood(4, 0.1F, true).a(MobEffectList.HUNGER.id, 30, 0, 0.8F).c("rottenFlesh"));
    a(368, "ender_pearl", new ItemEnderPearl().c("enderPearl"));
    a(369, "blaze_rod", new Item().c("blazeRod").a(CreativeModeTab.l).n());
    a(370, "ghast_tear", new Item().c("ghastTear").e(PotionBrewer.c).a(CreativeModeTab.k));
    a(371, "gold_nugget", new Item().c("goldNugget").a(CreativeModeTab.l));
    a(372, "nether_wart", new ItemSeeds(Blocks.NETHER_WART, Blocks.SOUL_SAND).c("netherStalkSeeds").e("+4"));
    a(373, "potion", new ItemPotion().c("potion"));
    a(374, "glass_bottle", new ItemGlassBottle().c("glassBottle"));
    a(375, "spider_eye", new ItemFood(2, 0.8F, false).a(MobEffectList.POISON.id, 5, 0, 1.0F).c("spiderEye").e(PotionBrewer.d));
    a(376, "fermented_spider_eye", new Item().c("fermentedSpiderEye").e(PotionBrewer.e).a(CreativeModeTab.k));
    a(377, "blaze_powder", new Item().c("blazePowder").e(PotionBrewer.g).a(CreativeModeTab.k));
    a(378, "magma_cream", new Item().c("magmaCream").e(PotionBrewer.h).a(CreativeModeTab.k));
    a(379, "brewing_stand", new ItemReed(Blocks.BREWING_STAND).c("brewingStand").a(CreativeModeTab.k));
    a(380, "cauldron", new ItemReed(Blocks.cauldron).c("cauldron").a(CreativeModeTab.k));
    a(381, "ender_eye", new ItemEnderEye().c("eyeOfEnder"));
    a(382, "speckled_melon", new Item().c("speckledMelon").e(PotionBrewer.f).a(CreativeModeTab.k));
    a(383, "spawn_egg", new ItemMonsterEgg().c("monsterPlacer"));
    a(384, "experience_bottle", new ItemExpBottle().c("expBottle"));
    a(385, "fire_charge", new ItemFireball().c("fireball"));
    a(386, "writable_book", new ItemBookAndQuill().c("writingBook").a(CreativeModeTab.f));
    a(387, "written_book", new ItemWrittenBook().c("writtenBook").c(16));
    a(388, "emerald", new Item().c("emerald").a(CreativeModeTab.l));
    a(389, "item_frame", new ItemHanging(EntityItemFrame.class).c("frame"));
    a(390, "flower_pot", new ItemReed(Blocks.FLOWER_POT).c("flowerPot").a(CreativeModeTab.c));
    a(391, "carrot", new ItemSeedFood(3, 0.6F, Blocks.CARROTS, Blocks.FARMLAND).c("carrots"));
    a(392, "potato", new ItemSeedFood(1, 0.3F, Blocks.POTATOES, Blocks.FARMLAND).c("potato"));
    a(393, "baked_potato", new ItemFood(5, 0.6F, false).c("potatoBaked"));
    a(394, "poisonous_potato", new ItemFood(2, 0.3F, false).a(MobEffectList.POISON.id, 5, 0, 0.6F).c("potatoPoisonous"));
    a(395, "map", new ItemMapEmpty().c("emptyMap"));
    a(396, "golden_carrot", new ItemFood(6, 1.2F, false).c("carrotGolden").e(PotionBrewer.l).a(CreativeModeTab.k));
    a(397, "skull", new ItemSkull().c("skull"));
    a(398, "carrot_on_a_stick", new ItemCarrotStick().c("carrotOnAStick"));
    a(399, "nether_star", new ItemNetherStar().c("netherStar").a(CreativeModeTab.l));
    a(400, "pumpkin_pie", new ItemFood(8, 0.3F, false).c("pumpkinPie").a(CreativeModeTab.h));
    a(401, "fireworks", new ItemFireworks().c("fireworks"));
    a(402, "firework_charge", new ItemFireworksCharge().c("fireworksCharge").a(CreativeModeTab.f));
    a(403, "enchanted_book", new ItemEnchantedBook().c(1).c("enchantedBook"));
    a(404, "comparator", new ItemReed(Blocks.UNPOWERED_COMPARATOR).c("comparator").a(CreativeModeTab.d));
    a(405, "netherbrick", new Item().c("netherbrick").a(CreativeModeTab.l));
    a(406, "quartz", new Item().c("netherquartz").a(CreativeModeTab.l));
    a(407, "tnt_minecart", new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.TNT).c("minecartTnt"));
    a(408, "hopper_minecart", new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.HOPPER).c("minecartHopper"));
    a(409, "prismarine_shard", new Item().c("prismarineShard").a(CreativeModeTab.l));
    a(410, "prismarine_crystals", new Item().c("prismarineCrystals").a(CreativeModeTab.l));
    a(411, "rabbit", new ItemFood(3, 0.3F, true).c("rabbitRaw"));
    a(412, "cooked_rabbit", new ItemFood(5, 0.6F, true).c("rabbitCooked"));
    a(413, "rabbit_stew", new ItemSoup(10).c("rabbitStew"));
    a(414, "rabbit_foot", new Item().c("rabbitFoot").e(PotionBrewer.n).a(CreativeModeTab.k));
    a(415, "rabbit_hide", new Item().c("rabbitHide").a(CreativeModeTab.l));
    a(416, "armor_stand", new ItemArmorStand().c("armorStand").c(16));
    a(417, "iron_horse_armor", new Item().c("horsearmormetal").c(1).a(CreativeModeTab.f));
    a(418, "golden_horse_armor", new Item().c("horsearmorgold").c(1).a(CreativeModeTab.f));
    a(419, "diamond_horse_armor", new Item().c("horsearmordiamond").c(1).a(CreativeModeTab.f));
    a(420, "lead", new ItemLeash().c("leash"));
    a(421, "name_tag", new ItemNameTag().c("nameTag"));
    a(422, "command_block_minecart", new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK).c("minecartCommandBlock").a(null));
    a(423, "mutton", new ItemFood(2, 0.3F, true).c("muttonRaw"));
    a(424, "cooked_mutton", new ItemFood(6, 0.8F, true).c("muttonCooked"));
    a(425, "banner", new ItemBanner().b("banner"));
    a(427, "spruce_door", new ItemDoor(Blocks.SPRUCE_DOOR).c("doorSpruce"));
    a(428, "birch_door", new ItemDoor(Blocks.BIRCH_DOOR).c("doorBirch"));
    a(429, "jungle_door", new ItemDoor(Blocks.JUNGLE_DOOR).c("doorJungle"));
    a(430, "acacia_door", new ItemDoor(Blocks.ACACIA_DOOR).c("doorAcacia"));
    a(431, "dark_oak_door", new ItemDoor(Blocks.DARK_OAK_DOOR).c("doorDarkOak"));
    
    a(2256, "record_13", new ItemRecord("13").c("record"));
    a(2257, "record_cat", new ItemRecord("cat").c("record"));
    a(2258, "record_blocks", new ItemRecord("blocks").c("record"));
    a(2259, "record_chirp", new ItemRecord("chirp").c("record"));
    a(2260, "record_far", new ItemRecord("far").c("record"));
    a(2261, "record_mall", new ItemRecord("mall").c("record"));
    a(2262, "record_mellohi", new ItemRecord("mellohi").c("record"));
    a(2263, "record_stal", new ItemRecord("stal").c("record"));
    a(2264, "record_strad", new ItemRecord("strad").c("record"));
    a(2265, "record_ward", new ItemRecord("ward").c("record"));
    a(2266, "record_11", new ItemRecord("11").c("record"));
    a(2267, "record_wait", new ItemRecord("wait").c("record"));
  }
  
  private static void c(Block ☃)
  {
    a(☃, new ItemBlock(☃));
  }
  
  protected static void a(Block ☃, Item ☃)
  {
    a(Block.getId(☃), (MinecraftKey)Block.REGISTRY.c(☃), ☃);
    a.put(☃, ☃);
  }
  
  private static void a(int ☃, String ☃, Item ☃)
  {
    a(☃, new MinecraftKey(☃), ☃);
  }
  
  private static void a(int ☃, MinecraftKey ☃, Item ☃)
  {
    REGISTRY.a(☃, ☃, ☃);
  }
}
