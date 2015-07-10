package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BiomeBase
{
  private static final Logger aD = ;
  
  public static enum EnumTemperature
  {
    private EnumTemperature() {}
  }
  
  public static class BiomeTemperature
  {
    public float a;
    public float b;
    
    public BiomeTemperature(float ☃, float ☃)
    {
      this.a = ☃;
      this.b = ☃;
    }
    
    public BiomeTemperature a()
    {
      return new BiomeTemperature(this.a * 0.8F, this.b * 0.6F);
    }
  }
  
  protected static final BiomeTemperature a = new BiomeTemperature(0.1F, 0.2F);
  protected static final BiomeTemperature b = new BiomeTemperature(-0.5F, 0.0F);
  protected static final BiomeTemperature c = new BiomeTemperature(-1.0F, 0.1F);
  protected static final BiomeTemperature d = new BiomeTemperature(-1.8F, 0.1F);
  protected static final BiomeTemperature e = new BiomeTemperature(0.125F, 0.05F);
  protected static final BiomeTemperature f = new BiomeTemperature(0.2F, 0.2F);
  protected static final BiomeTemperature g = new BiomeTemperature(0.45F, 0.3F);
  protected static final BiomeTemperature h = new BiomeTemperature(1.5F, 0.025F);
  protected static final BiomeTemperature i = new BiomeTemperature(1.0F, 0.5F);
  protected static final BiomeTemperature j = new BiomeTemperature(0.0F, 0.025F);
  protected static final BiomeTemperature k = new BiomeTemperature(0.1F, 0.8F);
  protected static final BiomeTemperature l = new BiomeTemperature(0.2F, 0.3F);
  protected static final BiomeTemperature m = new BiomeTemperature(-0.2F, 0.1F);
  private static final BiomeBase[] biomes = new BiomeBase['Ā'];
  public static final Set<BiomeBase> n = Sets.newHashSet();
  public static final Map<String, BiomeBase> o = Maps.newHashMap();
  public static final BiomeBase OCEAN = new BiomeOcean(0).b(112).a("Ocean").a(c);
  public static final BiomeBase PLAINS = new BiomePlains(1).b(9286496).a("Plains");
  public static final BiomeBase DESERT = new BiomeDesert(2).b(16421912).a("Desert").b().a(2.0F, 0.0F).a(e);
  public static final BiomeBase EXTREME_HILLS = new BiomeBigHills(3, false).b(6316128).a("Extreme Hills").a(i).a(0.2F, 0.3F);
  public static final BiomeBase FOREST = new BiomeForest(4, 0).b(353825).a("Forest");
  public static final BiomeBase TAIGA = new BiomeTaiga(5, 0).b(747097).a("Taiga").a(5159473).a(0.25F, 0.8F).a(f);
  public static final BiomeBase SWAMPLAND = new BiomeSwamp(6).b(522674).a("Swampland").a(9154376).a(m).a(0.8F, 0.9F);
  public static final BiomeBase RIVER = new BiomeRiver(7).b(255).a("River").a(b);
  public static final BiomeBase HELL = new BiomeHell(8).b(16711680).a("Hell").b().a(2.0F, 0.0F);
  public static final BiomeBase SKY = new BiomeTheEnd(9).b(8421631).a("The End").b();
  public static final BiomeBase FROZEN_OCEAN = new BiomeOcean(10).b(9474208).a("FrozenOcean").c().a(c).a(0.0F, 0.5F);
  public static final BiomeBase FROZEN_RIVER = new BiomeRiver(11).b(10526975).a("FrozenRiver").c().a(b).a(0.0F, 0.5F);
  public static final BiomeBase ICE_PLAINS = new BiomeIcePlains(12, false).b(16777215).a("Ice Plains").c().a(0.0F, 0.5F).a(e);
  public static final BiomeBase ICE_MOUNTAINS = new BiomeIcePlains(13, false).b(10526880).a("Ice Mountains").c().a(g).a(0.0F, 0.5F);
  public static final BiomeBase MUSHROOM_ISLAND = new BiomeMushrooms(14).b(16711935).a("MushroomIsland").a(0.9F, 1.0F).a(l);
  public static final BiomeBase MUSHROOM_SHORE = new BiomeMushrooms(15).b(10486015).a("MushroomIslandShore").a(0.9F, 1.0F).a(j);
  public static final BiomeBase BEACH = new BiomeBeach(16).b(16440917).a("Beach").a(0.8F, 0.4F).a(j);
  public static final BiomeBase DESERT_HILLS = new BiomeDesert(17).b(13786898).a("DesertHills").b().a(2.0F, 0.0F).a(g);
  public static final BiomeBase FOREST_HILLS = new BiomeForest(18, 0).b(2250012).a("ForestHills").a(g);
  public static final BiomeBase TAIGA_HILLS = new BiomeTaiga(19, 0).b(1456435).a("TaigaHills").a(5159473).a(0.25F, 0.8F).a(g);
  public static final BiomeBase SMALL_MOUNTAINS = new BiomeBigHills(20, true).b(7501978).a("Extreme Hills Edge").a(i.a()).a(0.2F, 0.3F);
  public static final BiomeBase JUNGLE = new BiomeJungle(21, false).b(5470985).a("Jungle").a(5470985).a(0.95F, 0.9F);
  public static final BiomeBase JUNGLE_HILLS = new BiomeJungle(22, false).b(2900485).a("JungleHills").a(5470985).a(0.95F, 0.9F).a(g);
  public static final BiomeBase JUNGLE_EDGE = new BiomeJungle(23, true).b(6458135).a("JungleEdge").a(5470985).a(0.95F, 0.8F);
  public static final BiomeBase DEEP_OCEAN = new BiomeOcean(24).b(48).a("Deep Ocean").a(d);
  public static final BiomeBase STONE_BEACH = new BiomeStoneBeach(25).b(10658436).a("Stone Beach").a(0.2F, 0.3F).a(k);
  public static final BiomeBase COLD_BEACH = new BiomeBeach(26).b(16445632).a("Cold Beach").a(0.05F, 0.3F).a(j).c();
  public static final BiomeBase BIRCH_FOREST = new BiomeForest(27, 2).a("Birch Forest").b(3175492);
  public static final BiomeBase BIRCH_FOREST_HILLS = new BiomeForest(28, 2).a("Birch Forest Hills").b(2055986).a(g);
  public static final BiomeBase ROOFED_FOREST = new BiomeForest(29, 3).b(4215066).a("Roofed Forest");
  public static final BiomeBase COLD_TAIGA = new BiomeTaiga(30, 0).b(3233098).a("Cold Taiga").a(5159473).c().a(-0.5F, 0.4F).a(f).c(16777215);
  public static final BiomeBase COLD_TAIGA_HILLS = new BiomeTaiga(31, 0).b(2375478).a("Cold Taiga Hills").a(5159473).c().a(-0.5F, 0.4F).a(g).c(16777215);
  public static final BiomeBase MEGA_TAIGA = new BiomeTaiga(32, 1).b(5858897).a("Mega Taiga").a(5159473).a(0.3F, 0.8F).a(f);
  public static final BiomeBase MEGA_TAIGA_HILLS = new BiomeTaiga(33, 1).b(4542270).a("Mega Taiga Hills").a(5159473).a(0.3F, 0.8F).a(g);
  public static final BiomeBase EXTREME_HILLS_PLUS = new BiomeBigHills(34, true).b(5271632).a("Extreme Hills+").a(i).a(0.2F, 0.3F);
  public static final BiomeBase SAVANNA = new BiomeSavanna(35).b(12431967).a("Savanna").a(1.2F, 0.0F).b().a(e);
  public static final BiomeBase SAVANNA_PLATEAU = new BiomeSavanna(36).b(10984804).a("Savanna Plateau").a(1.0F, 0.0F).b().a(h);
  public static final BiomeBase MESA = new BiomeMesa(37, false, false).b(14238997).a("Mesa");
  public static final BiomeBase MESA_PLATEAU_F = new BiomeMesa(38, false, true).b(11573093).a("Mesa Plateau F").a(h);
  public static final BiomeBase MESA_PLATEAU = new BiomeMesa(39, false, false).b(13274213).a("Mesa Plateau").a(h);
  public static final BiomeBase ad = OCEAN;
  
  static
  {
    PLAINS.k();
    DESERT.k();
    FOREST.k();
    TAIGA.k();
    SWAMPLAND.k();
    ICE_PLAINS.k();
    JUNGLE.k();
    JUNGLE_EDGE.k();
    COLD_TAIGA.k();
    SAVANNA.k();
    SAVANNA_PLATEAU.k();
    MESA.k();
    MESA_PLATEAU_F.k();
    MESA_PLATEAU.k();
    BIRCH_FOREST.k();
    BIRCH_FOREST_HILLS.k();
    ROOFED_FOREST.k();
    MEGA_TAIGA.k();
    EXTREME_HILLS.k();
    EXTREME_HILLS_PLUS.k();
    
    MEGA_TAIGA.d(MEGA_TAIGA_HILLS.id + 128).a("Redwood Taiga Hills M");
    for (BiomeBase ☃ : biomes) {
      if (☃ != null)
      {
        if (o.containsKey(☃.ah)) {
          throw new Error("Biome \"" + ☃.ah + "\" is defined as both ID " + ((BiomeBase)o.get(☃.ah)).id + " and " + ☃.id);
        }
        o.put(☃.ah, ☃);
        if (☃.id < 128) {
          n.add(☃);
        }
      }
    }
    n.remove(HELL);
    n.remove(SKY);
    n.remove(FROZEN_OCEAN);
    n.remove(SMALL_MOUNTAINS);
  }
  
  protected static final NoiseGenerator3 ae = new NoiseGenerator3(new Random(1234L), 1);
  protected static final NoiseGenerator3 af = new NoiseGenerator3(new Random(2345L), 1);
  protected static final WorldGenTallPlant ag = new WorldGenTallPlant();
  public String ah;
  public int ai;
  public int aj;
  public IBlockData ak = Blocks.GRASS.getBlockData();
  public IBlockData al = Blocks.DIRT.getBlockData();
  public int am = 5169201;
  public float an = a.a;
  public float ao = a.b;
  public float temperature = 0.5F;
  public float humidity = 0.5F;
  public int ar = 16777215;
  public BiomeDecorator as;
  protected List<BiomeMeta> at = Lists.newArrayList();
  protected List<BiomeMeta> au = Lists.newArrayList();
  protected List<BiomeMeta> av = Lists.newArrayList();
  protected List<BiomeMeta> aw = Lists.newArrayList();
  protected boolean ax;
  protected boolean ay = true;
  public final int id;
  
  protected BiomeBase(int ☃)
  {
    this.id = ☃;
    biomes[☃] = this;
    this.as = a();
    
    this.au.add(new BiomeMeta(EntitySheep.class, 12, 4, 4));
    this.au.add(new BiomeMeta(EntityRabbit.class, 10, 3, 3));
    this.au.add(new BiomeMeta(EntityPig.class, 10, 4, 4));
    this.au.add(new BiomeMeta(EntityChicken.class, 10, 4, 4));
    this.au.add(new BiomeMeta(EntityCow.class, 8, 4, 4));
    
    this.at.add(new BiomeMeta(EntitySpider.class, 100, 4, 4));
    this.at.add(new BiomeMeta(EntityZombie.class, 100, 4, 4));
    this.at.add(new BiomeMeta(EntitySkeleton.class, 100, 4, 4));
    this.at.add(new BiomeMeta(EntityCreeper.class, 100, 4, 4));
    this.at.add(new BiomeMeta(EntitySlime.class, 100, 4, 4));
    this.at.add(new BiomeMeta(EntityEnderman.class, 10, 1, 4));
    this.at.add(new BiomeMeta(EntityWitch.class, 5, 1, 1));
    
    this.av.add(new BiomeMeta(EntitySquid.class, 10, 4, 4));
    
    this.aw.add(new BiomeMeta(EntityBat.class, 10, 8, 8));
  }
  
  protected BiomeDecorator a()
  {
    return new BiomeDecorator();
  }
  
  protected BiomeBase a(float ☃, float ☃)
  {
    if ((☃ > 0.1F) && (☃ < 0.2F)) {
      throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
    }
    this.temperature = ☃;
    this.humidity = ☃;
    return this;
  }
  
  protected final BiomeBase a(BiomeTemperature ☃)
  {
    this.an = ☃.a;
    this.ao = ☃.b;
    return this;
  }
  
  protected BiomeBase b()
  {
    this.ay = false;
    return this;
  }
  
  protected WorldGenTrees aA = new WorldGenTrees(false);
  protected WorldGenBigTree aB = new WorldGenBigTree(false);
  protected WorldGenSwampTree aC = new WorldGenSwampTree();
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    if (☃.nextInt(10) == 0) {
      return this.aB;
    }
    return this.aA;
  }
  
  public WorldGenerator b(Random ☃)
  {
    return new WorldGenGrass(BlockLongGrass.EnumTallGrassType.GRASS);
  }
  
  public BlockFlowers.EnumFlowerVarient a(Random ☃, BlockPosition ☃)
  {
    if (☃.nextInt(3) > 0) {
      return BlockFlowers.EnumFlowerVarient.DANDELION;
    }
    return BlockFlowers.EnumFlowerVarient.POPPY;
  }
  
  protected BiomeBase c()
  {
    this.ax = true;
    return this;
  }
  
  protected BiomeBase a(String ☃)
  {
    this.ah = ☃;
    return this;
  }
  
  protected BiomeBase a(int ☃)
  {
    this.am = ☃;
    return this;
  }
  
  protected BiomeBase b(int ☃)
  {
    a(☃, false);
    return this;
  }
  
  protected BiomeBase c(int ☃)
  {
    this.aj = ☃;
    return this;
  }
  
  protected BiomeBase a(int ☃, boolean ☃)
  {
    this.ai = ☃;
    if (☃) {
      this.aj = ((☃ & 0xFEFEFE) >> 1);
    } else {
      this.aj = ☃;
    }
    return this;
  }
  
  public List<BiomeMeta> getMobs(EnumCreatureType ☃)
  {
    switch (1.switchMap[☃.ordinal()])
    {
    case 1: 
      return this.at;
    case 2: 
      return this.au;
    case 3: 
      return this.av;
    case 4: 
      return this.aw;
    }
    return Collections.emptyList();
  }
  
  public static class BiomeMeta
    extends WeightedRandom.WeightedRandomChoice
  {
    public Class<? extends EntityInsentient> b;
    public int c;
    public int d;
    
    public BiomeMeta(Class<? extends EntityInsentient> ☃, int ☃, int ☃, int ☃)
    {
      super();
      this.b = ☃;
      this.c = ☃;
      this.d = ☃;
    }
    
    public String toString()
    {
      return this.b.getSimpleName() + "*(" + this.c + "-" + this.d + "):" + this.a;
    }
  }
  
  public boolean d()
  {
    return j();
  }
  
  public boolean e()
  {
    if (j()) {
      return false;
    }
    return this.ay;
  }
  
  public boolean f()
  {
    return this.humidity > 0.85F;
  }
  
  public float g()
  {
    return 0.1F;
  }
  
  public final int h()
  {
    return (int)(this.humidity * 65536.0F);
  }
  
  public final float a(BlockPosition ☃)
  {
    if (☃.getY() > 64)
    {
      float ☃ = (float)(ae.a(☃.getX() * 1.0D / 8.0D, ☃.getZ() * 1.0D / 8.0D) * 4.0D);
      return this.temperature - (☃ + ☃.getY() - 64.0F) * 0.05F / 30.0F;
    }
    return this.temperature;
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    this.as.a(☃, ☃, this, ☃);
  }
  
  public boolean j()
  {
    return this.ax;
  }
  
  public void a(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
  {
    b(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public final void b(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
  {
    int ☃ = ☃.F();
    IBlockData ☃ = this.ak;
    IBlockData ☃ = this.al;
    int ☃ = -1;
    int ☃ = (int)(☃ / 3.0D + 3.0D + ☃.nextDouble() * 0.25D);
    
    int ☃ = ☃ & 0xF;
    int ☃ = ☃ & 0xF;
    BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
    for (int ☃ = 255; ☃ >= 0; ☃--) {
      if (☃ <= ☃.nextInt(5))
      {
        ☃.a(☃, ☃, ☃, Blocks.BEDROCK.getBlockData());
      }
      else
      {
        IBlockData ☃ = ☃.a(☃, ☃, ☃);
        if (☃.getBlock().getMaterial() == Material.AIR) {
          ☃ = -1;
        } else if (☃.getBlock() == Blocks.STONE) {
          if (☃ == -1)
          {
            if (☃ <= 0)
            {
              ☃ = null;
              ☃ = Blocks.STONE.getBlockData();
            }
            else if ((☃ >= ☃ - 4) && (☃ <= ☃ + 1))
            {
              ☃ = this.ak;
              ☃ = this.al;
            }
            if ((☃ < ☃) && ((☃ == null) || (☃.getBlock().getMaterial() == Material.AIR))) {
              if (a(☃.c(☃, ☃, ☃)) < 0.15F) {
                ☃ = Blocks.ICE.getBlockData();
              } else {
                ☃ = Blocks.WATER.getBlockData();
              }
            }
            ☃ = ☃;
            if (☃ >= ☃ - 1)
            {
              ☃.a(☃, ☃, ☃, ☃);
            }
            else if (☃ < ☃ - 7 - ☃)
            {
              ☃ = null;
              ☃ = Blocks.STONE.getBlockData();
              ☃.a(☃, ☃, ☃, Blocks.GRAVEL.getBlockData());
            }
            else
            {
              ☃.a(☃, ☃, ☃, ☃);
            }
          }
          else if (☃ > 0)
          {
            ☃--;
            ☃.a(☃, ☃, ☃, ☃);
            if ((☃ == 0) && (☃.getBlock() == Blocks.SAND))
            {
              ☃ = ☃.nextInt(4) + Math.max(0, ☃ - 63);
              ☃ = ☃.get(BlockSand.VARIANT) == BlockSand.EnumSandVariant.RED_SAND ? Blocks.RED_SANDSTONE.getBlockData() : Blocks.SANDSTONE.getBlockData();
            }
          }
        }
      }
    }
  }
  
  protected BiomeBase k()
  {
    return d(this.id + 128);
  }
  
  protected BiomeBase d(int ☃)
  {
    return new BiomeBaseSub(☃, this);
  }
  
  public Class<? extends BiomeBase> l()
  {
    return getClass();
  }
  
  public boolean a(BiomeBase ☃)
  {
    if (☃ == this) {
      return true;
    }
    if (☃ == null) {
      return false;
    }
    return l() == ☃.l();
  }
  
  public EnumTemperature m()
  {
    if (this.temperature < 0.2D) {
      return EnumTemperature.COLD;
    }
    if (this.temperature < 1.0D) {
      return EnumTemperature.MEDIUM;
    }
    return EnumTemperature.WARM;
  }
  
  public static BiomeBase[] getBiomes()
  {
    return biomes;
  }
  
  public static BiomeBase getBiome(int ☃)
  {
    return getBiome(☃, null);
  }
  
  public static BiomeBase getBiome(int ☃, BiomeBase ☃)
  {
    if ((☃ < 0) || (☃ > biomes.length))
    {
      aD.warn("Biome ID is out of bounds: " + ☃ + ", defaulting to 0 (Ocean)");
      return OCEAN;
    }
    BiomeBase ☃ = biomes[☃];
    if (☃ == null) {
      return ☃;
    }
    return ☃;
  }
}
