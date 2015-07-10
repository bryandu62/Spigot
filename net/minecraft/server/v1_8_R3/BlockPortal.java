package net.minecraft.server.v1_8_R3;

import com.google.common.cache.LoadingCache;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.SpigotWorldConfig;

public class BlockPortal
  extends BlockHalfTransparent
{
  public static final BlockStateEnum<EnumDirection.EnumAxis> AXIS = BlockStateEnum.of("axis", EnumDirection.EnumAxis.class, new EnumDirection.EnumAxis[] { EnumDirection.EnumAxis.X, EnumDirection.EnumAxis.Z });
  
  public BlockPortal()
  {
    super(Material.PORTAL, false);
    j(this.blockStateList.getBlockData().set(AXIS, EnumDirection.EnumAxis.X));
    a(true);
  }
  
  public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random)
  {
    super.b(world, blockposition, iblockdata, random);
    if ((world.spigotConfig.enableZombiePigmenPortalSpawns) && (world.worldProvider.d()) && (world.getGameRules().getBoolean("doMobSpawning")) && (random.nextInt(2000) < world.getDifficulty().a()))
    {
      int i = blockposition.getY();
      for (BlockPosition blockposition1 = blockposition; (!World.a(world, blockposition1)) && (blockposition1.getY() > 0); blockposition1 = blockposition1.down()) {}
      if ((i > 0) && (!world.getType(blockposition1.up()).getBlock().isOccluding()))
      {
        Entity entity = ItemMonsterEgg.spawnCreature(world, 57, blockposition1.getX() + 0.5D, blockposition1.getY() + 1.1D, blockposition1.getZ() + 0.5D, CreatureSpawnEvent.SpawnReason.NETHER_PORTAL);
        if (entity != null) {
          entity.portalCooldown = entity.aq();
        }
      }
    }
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    EnumDirection.EnumAxis enumdirection_enumaxis = (EnumDirection.EnumAxis)iblockaccess.getType(blockposition).get(AXIS);
    float f = 0.125F;
    float f1 = 0.125F;
    if (enumdirection_enumaxis == EnumDirection.EnumAxis.X) {
      f = 0.5F;
    }
    if (enumdirection_enumaxis == EnumDirection.EnumAxis.Z) {
      f1 = 0.5F;
    }
    a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
  }
  
  public static int a(EnumDirection.EnumAxis enumdirection_enumaxis)
  {
    return enumdirection_enumaxis == EnumDirection.EnumAxis.Z ? 2 : enumdirection_enumaxis == EnumDirection.EnumAxis.X ? 1 : 0;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean e(World world, BlockPosition blockposition)
  {
    Shape blockportal_shape = new Shape(world, blockposition, EnumDirection.EnumAxis.X);
    if ((blockportal_shape.d()) && (blockportal_shape.e == 0)) {
      return blockportal_shape.e();
    }
    Shape blockportal_shape1 = new Shape(world, blockposition, EnumDirection.EnumAxis.Z);
    if ((blockportal_shape1.d()) && (blockportal_shape1.e == 0)) {
      return blockportal_shape1.e();
    }
    return false;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    EnumDirection.EnumAxis enumdirection_enumaxis = (EnumDirection.EnumAxis)iblockdata.get(AXIS);
    if (enumdirection_enumaxis == EnumDirection.EnumAxis.X)
    {
      Shape blockportal_shape = new Shape(world, blockposition, EnumDirection.EnumAxis.X);
      if ((!blockportal_shape.d()) || (blockportal_shape.e < blockportal_shape.h * blockportal_shape.g)) {
        world.setTypeUpdate(blockposition, Blocks.AIR.getBlockData());
      }
    }
    else if (enumdirection_enumaxis == EnumDirection.EnumAxis.Z)
    {
      Shape blockportal_shape = new Shape(world, blockposition, EnumDirection.EnumAxis.Z);
      if ((!blockportal_shape.d()) || (blockportal_shape.e < blockportal_shape.h * blockportal_shape.g)) {
        world.setTypeUpdate(blockposition, Blocks.AIR.getBlockData());
      }
    }
  }
  
  public int a(Random random)
  {
    return 0;
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity)
  {
    if ((entity.vehicle == null) && (entity.passenger == null))
    {
      EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ()));
      world.getServer().getPluginManager().callEvent(event);
      
      entity.d(blockposition);
    }
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(AXIS, (i & 0x3) == 2 ? EnumDirection.EnumAxis.Z : EnumDirection.EnumAxis.X);
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return a((EnumDirection.EnumAxis)iblockdata.get(AXIS));
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { AXIS });
  }
  
  public ShapeDetector.ShapeDetectorCollection f(World world, BlockPosition blockposition)
  {
    EnumDirection.EnumAxis enumdirection_enumaxis = EnumDirection.EnumAxis.Z;
    Shape blockportal_shape = new Shape(world, blockposition, EnumDirection.EnumAxis.X);
    LoadingCache loadingcache = ShapeDetector.a(world, true);
    if (!blockportal_shape.d())
    {
      enumdirection_enumaxis = EnumDirection.EnumAxis.X;
      blockportal_shape = new Shape(world, blockposition, EnumDirection.EnumAxis.Z);
    }
    if (!blockportal_shape.d()) {
      return new ShapeDetector.ShapeDetectorCollection(blockposition, EnumDirection.NORTH, EnumDirection.UP, loadingcache, 1, 1, 1);
    }
    int[] aint = new int[EnumDirection.EnumAxisDirection.values().length];
    EnumDirection enumdirection = blockportal_shape.c.f();
    BlockPosition blockposition1 = blockportal_shape.f.up(blockportal_shape.a() - 1);
    EnumDirection.EnumAxisDirection[] aenumdirection_enumaxisdirection = EnumDirection.EnumAxisDirection.values();
    int i = aenumdirection_enumaxisdirection.length;
    for (int j = 0; j < i; j++)
    {
      EnumDirection.EnumAxisDirection enumdirection_enumaxisdirection = aenumdirection_enumaxisdirection[j];
      ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = new ShapeDetector.ShapeDetectorCollection(enumdirection.c() == enumdirection_enumaxisdirection ? blockposition1 : blockposition1.shift(blockportal_shape.c, blockportal_shape.b() - 1), EnumDirection.a(enumdirection_enumaxisdirection, enumdirection_enumaxis), EnumDirection.UP, loadingcache, blockportal_shape.b(), blockportal_shape.a(), 1);
      for (int k = 0; k < blockportal_shape.b(); k++) {
        for (int l = 0; l < blockportal_shape.a(); l++)
        {
          ShapeDetectorBlock shapedetectorblock = shapedetector_shapedetectorcollection.a(k, l, 1);
          if ((shapedetectorblock.a() != null) && (shapedetectorblock.a().getBlock().getMaterial() != Material.AIR)) {
            aint[enumdirection_enumaxisdirection.ordinal()] += 1;
          }
        }
      }
    }
    EnumDirection.EnumAxisDirection enumdirection_enumaxisdirection1 = EnumDirection.EnumAxisDirection.POSITIVE;
    EnumDirection.EnumAxisDirection[] aenumdirection_enumaxisdirection1 = EnumDirection.EnumAxisDirection.values();
    
    j = aenumdirection_enumaxisdirection1.length;
    for (int i1 = 0; i1 < j; i1++)
    {
      EnumDirection.EnumAxisDirection enumdirection_enumaxisdirection2 = aenumdirection_enumaxisdirection1[i1];
      if (aint[enumdirection_enumaxisdirection2.ordinal()] < aint[enumdirection_enumaxisdirection1.ordinal()]) {
        enumdirection_enumaxisdirection1 = enumdirection_enumaxisdirection2;
      }
    }
    return new ShapeDetector.ShapeDetectorCollection(enumdirection.c() == enumdirection_enumaxisdirection1 ? blockposition1 : blockposition1.shift(blockportal_shape.c, blockportal_shape.b() - 1), EnumDirection.a(enumdirection_enumaxisdirection1, enumdirection_enumaxis), EnumDirection.UP, loadingcache, blockportal_shape.b(), blockportal_shape.a(), 1);
  }
  
  public static class Shape
  {
    private final World a;
    private final EnumDirection.EnumAxis b;
    private final EnumDirection c;
    private final EnumDirection d;
    private int e = 0;
    private BlockPosition f;
    private int g;
    private int h;
    Collection<org.bukkit.block.Block> blocks = new HashSet();
    
    public Shape(World world, BlockPosition blockposition, EnumDirection.EnumAxis enumdirection_enumaxis)
    {
      this.a = world;
      this.b = enumdirection_enumaxis;
      if (enumdirection_enumaxis == EnumDirection.EnumAxis.X)
      {
        this.d = EnumDirection.EAST;
        this.c = EnumDirection.WEST;
      }
      else
      {
        this.d = EnumDirection.NORTH;
        this.c = EnumDirection.SOUTH;
      }
      for (BlockPosition blockposition1 = blockposition; (blockposition.getY() > blockposition1.getY() - 21) && (blockposition.getY() > 0) && (a(world.getType(blockposition.down()).getBlock())); blockposition = blockposition.down()) {}
      int i = a(blockposition, this.d) - 1;
      if (i >= 0)
      {
        this.f = blockposition.shift(this.d, i);
        this.h = a(this.f, this.c);
        if ((this.h < 2) || (this.h > 21))
        {
          this.f = null;
          this.h = 0;
        }
      }
      if (this.f != null) {
        this.g = c();
      }
    }
    
    protected int a(BlockPosition blockposition, EnumDirection enumdirection)
    {
      for (int i = 0; i < 22; i++)
      {
        BlockPosition blockposition1 = blockposition.shift(enumdirection, i);
        if ((!a(this.a.getType(blockposition1).getBlock())) || (this.a.getType(blockposition1.down()).getBlock() != Blocks.OBSIDIAN)) {
          break;
        }
      }
      Block block = this.a.getType(blockposition.shift(enumdirection, i)).getBlock();
      
      return block == Blocks.OBSIDIAN ? i : 0;
    }
    
    public int a()
    {
      return this.g;
    }
    
    public int b()
    {
      return this.h;
    }
    
    protected int c()
    {
      this.blocks.clear();
      org.bukkit.World bworld = this.a.getWorld();
      for (this.g = 0; this.g < 21; this.g += 1) {
        for (int i = 0; i < this.h; i++)
        {
          BlockPosition blockposition = this.f.shift(this.c, i).up(this.g);
          Block block = this.a.getType(blockposition).getBlock();
          if (!a(block)) {
            break;
          }
          if (block == Blocks.PORTAL) {
            this.e += 1;
          }
          if (i == 0)
          {
            block = this.a.getType(blockposition.shift(this.d)).getBlock();
            if (block != Blocks.OBSIDIAN) {
              break;
            }
            BlockPosition pos = blockposition.shift(this.d);
            this.blocks.add(bworld.getBlockAt(pos.getX(), pos.getY(), pos.getZ()));
          }
          else if (i == this.h - 1)
          {
            block = this.a.getType(blockposition.shift(this.c)).getBlock();
            if (block != Blocks.OBSIDIAN) {
              break;
            }
            BlockPosition pos = blockposition.shift(this.c);
            this.blocks.add(bworld.getBlockAt(pos.getX(), pos.getY(), pos.getZ()));
          }
        }
      }
      for (int i = 0; i < this.h; i++)
      {
        if (this.a.getType(this.f.shift(this.c, i).up(this.g)).getBlock() != Blocks.OBSIDIAN)
        {
          this.g = 0;
          break;
        }
        BlockPosition pos = this.f.shift(this.c, i).up(this.g);
        this.blocks.add(bworld.getBlockAt(pos.getX(), pos.getY(), pos.getZ()));
      }
      if ((this.g <= 21) && (this.g >= 3)) {
        return this.g;
      }
      this.f = null;
      this.h = 0;
      this.g = 0;
      return 0;
    }
    
    protected boolean a(Block block)
    {
      return (block.material == Material.AIR) || (block == Blocks.FIRE) || (block == Blocks.PORTAL);
    }
    
    public boolean d()
    {
      return (this.f != null) && (this.h >= 2) && (this.h <= 21) && (this.g >= 3) && (this.g <= 21);
    }
    
    public boolean e()
    {
      org.bukkit.World bworld = this.a.getWorld();
      for (int i = 0; i < this.h; i++)
      {
        BlockPosition blockposition = this.f.shift(this.c, i);
        for (int j = 0; j < this.g; j++)
        {
          BlockPosition pos = blockposition.up(j);
          this.blocks.add(bworld.getBlockAt(pos.getX(), pos.getY(), pos.getZ()));
        }
      }
      PortalCreateEvent event = new PortalCreateEvent(this.blocks, bworld, PortalCreateEvent.CreateReason.FIRE);
      this.a.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        return false;
      }
      for (int i = 0; i < this.h; i++)
      {
        BlockPosition blockposition = this.f.shift(this.c, i);
        for (int j = 0; j < this.g; j++) {
          this.a.setTypeAndData(blockposition.up(j), Blocks.PORTAL.getBlockData().set(BlockPortal.AXIS, this.b), 2);
        }
      }
      return true;
    }
  }
}
