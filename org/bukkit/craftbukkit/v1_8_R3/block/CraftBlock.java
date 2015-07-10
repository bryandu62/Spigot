package org.bukkit.craftbukkit.v1_8_R3.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockCocoa;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockRedstoneWire;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.EnumSkyBlock;
import net.minecraft.server.v1_8_R3.GameProfileSerializer;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;

public class CraftBlock
  implements org.bukkit.block.Block
{
  private final CraftChunk chunk;
  private final int x;
  private final int y;
  private final int z;
  
  public CraftBlock(CraftChunk chunk, int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.chunk = chunk;
  }
  
  private net.minecraft.server.v1_8_R3.Block getNMSBlock()
  {
    return CraftMagicNumbers.getBlock(this);
  }
  
  private static net.minecraft.server.v1_8_R3.Block getNMSBlock(int type)
  {
    return CraftMagicNumbers.getBlock(type);
  }
  
  public org.bukkit.World getWorld()
  {
    return this.chunk.getWorld();
  }
  
  public Location getLocation()
  {
    return new Location(getWorld(), this.x, this.y, this.z);
  }
  
  public Location getLocation(Location loc)
  {
    if (loc != null)
    {
      loc.setWorld(getWorld());
      loc.setX(this.x);
      loc.setY(this.y);
      loc.setZ(this.z);
      loc.setYaw(0.0F);
      loc.setPitch(0.0F);
    }
    return loc;
  }
  
  public BlockVector getVector()
  {
    return new BlockVector(this.x, this.y, this.z);
  }
  
  public int getX()
  {
    return this.x;
  }
  
  public int getY()
  {
    return this.y;
  }
  
  public int getZ()
  {
    return this.z;
  }
  
  public org.bukkit.Chunk getChunk()
  {
    return this.chunk;
  }
  
  public void setData(byte data)
  {
    setData(data, 3);
  }
  
  public void setData(byte data, boolean applyPhysics)
  {
    if (applyPhysics) {
      setData(data, 3);
    } else {
      setData(data, 2);
    }
  }
  
  private void setData(byte data, int flag)
  {
    net.minecraft.server.v1_8_R3.World world = this.chunk.getHandle().getWorld();
    BlockPosition position = new BlockPosition(this.x, this.y, this.z);
    IBlockData blockData = world.getType(position);
    world.setTypeAndData(position, blockData.getBlock().fromLegacyData(data), flag);
  }
  
  public byte getData()
  {
    IBlockData blockData = this.chunk.getHandle().getBlockData(new BlockPosition(this.x, this.y, this.z));
    return (byte)blockData.getBlock().toLegacyData(blockData);
  }
  
  public void setType(org.bukkit.Material type)
  {
    setType(type, true);
  }
  
  public void setType(org.bukkit.Material type, boolean applyPhysics)
  {
    setTypeId(type.getId(), applyPhysics);
  }
  
  public boolean setTypeId(int type)
  {
    return setTypeId(type, true);
  }
  
  public boolean setTypeId(int type, boolean applyPhysics)
  {
    net.minecraft.server.v1_8_R3.Block block = getNMSBlock(type);
    return setTypeIdAndData(type, (byte)block.toLegacyData(block.getBlockData()), applyPhysics);
  }
  
  public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics)
  {
    IBlockData blockData = getNMSBlock(type).fromLegacyData(data);
    BlockPosition position = new BlockPosition(this.x, this.y, this.z);
    if (applyPhysics) {
      return this.chunk.getHandle().getWorld().setTypeAndData(position, blockData, 3);
    }
    boolean success = this.chunk.getHandle().getWorld().setTypeAndData(position, blockData, 2);
    if (success) {
      this.chunk.getHandle().getWorld().notify(position);
    }
    return success;
  }
  
  public org.bukkit.Material getType()
  {
    return org.bukkit.Material.getMaterial(getTypeId());
  }
  
  @Deprecated
  public int getTypeId()
  {
    return CraftMagicNumbers.getId(this.chunk.getHandle().getType(new BlockPosition(this.x, this.y, this.z)));
  }
  
  public byte getLightLevel()
  {
    return (byte)this.chunk.getHandle().getWorld().getLightLevel(new BlockPosition(this.x, this.y, this.z));
  }
  
  public byte getLightFromSky()
  {
    return (byte)this.chunk.getHandle().getBrightness(EnumSkyBlock.SKY, new BlockPosition(this.x, this.y, this.z));
  }
  
  public byte getLightFromBlocks()
  {
    return (byte)this.chunk.getHandle().getBrightness(EnumSkyBlock.BLOCK, new BlockPosition(this.x, this.y, this.z));
  }
  
  public org.bukkit.block.Block getFace(BlockFace face)
  {
    return getRelative(face, 1);
  }
  
  public org.bukkit.block.Block getFace(BlockFace face, int distance)
  {
    return getRelative(face, distance);
  }
  
  public org.bukkit.block.Block getRelative(int modX, int modY, int modZ)
  {
    return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
  }
  
  public org.bukkit.block.Block getRelative(BlockFace face)
  {
    return getRelative(face, 1);
  }
  
  public org.bukkit.block.Block getRelative(BlockFace face, int distance)
  {
    return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
  }
  
  public BlockFace getFace(org.bukkit.block.Block block)
  {
    BlockFace[] values = BlockFace.values();
    BlockFace[] arrayOfBlockFace1;
    int i = (arrayOfBlockFace1 = values).length;
    for (int j = 0; j < i; j++)
    {
      BlockFace face = arrayOfBlockFace1[j];
      if ((getX() + face.getModX() == block.getX()) && 
        (getY() + face.getModY() == block.getY()) && 
        (getZ() + face.getModZ() == block.getZ())) {
        return face;
      }
    }
    return null;
  }
  
  public String toString()
  {
    return "CraftBlock{chunk=" + this.chunk + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",type=" + getType() + ",data=" + getData() + '}';
  }
  
  public static BlockFace notchToBlockFace(EnumDirection notch)
  {
    if (notch == null) {
      return BlockFace.SELF;
    }
    switch (notch)
    {
    case DOWN: 
      return BlockFace.DOWN;
    case EAST: 
      return BlockFace.UP;
    case NORTH: 
      return BlockFace.NORTH;
    case SOUTH: 
      return BlockFace.SOUTH;
    case UP: 
      return BlockFace.WEST;
    case WEST: 
      return BlockFace.EAST;
    }
    return BlockFace.SELF;
  }
  
  public static EnumDirection blockFaceToNotch(BlockFace face)
  {
    switch (face)
    {
    case NORTH_EAST: 
      return EnumDirection.DOWN;
    case NORTH: 
      return EnumDirection.UP;
    case DOWN: 
      return EnumDirection.NORTH;
    case EAST_NORTH_EAST: 
      return EnumDirection.SOUTH;
    case EAST_SOUTH_EAST: 
      return EnumDirection.WEST;
    case EAST: 
      return EnumDirection.EAST;
    }
    return null;
  }
  
  public BlockState getState()
  {
    org.bukkit.Material material = getType();
    switch (material)
    {
    case COCOA: 
    case COOKED_CHICKEN: 
    case QUARTZ_STAIRS: 
      return new CraftSign(this);
    case CLAY: 
    case GOLD_BOOTS: 
      return new CraftChest(this);
    case COBBLESTONE_STAIRS: 
    case COBBLE_WALL: 
      return new CraftFurnace(this);
    case BLAZE_POWDER: 
      return new CraftDispenser(this);
    case GOLD_SWORD: 
      return new CraftDropper(this);
    case GOLD_PICKAXE: 
      return new CraftHopper(this);
    case CHAINMAIL_LEGGINGS: 
      return new CraftCreatureSpawner(this);
    case BOAT: 
      return new CraftNoteBlock(this);
    case DIAMOND_AXE: 
      return new CraftJukebox(this);
    case ENDER_STONE: 
      return new CraftBrewingStand(this);
    case GOLD_BARDING: 
      return new CraftSkull(this);
    case GLASS_BOTTLE: 
      return new CraftCommandBlock(this);
    case GLOWING_REDSTONE_ORE: 
      return new CraftBeacon(this);
    case IRON_DOOR: 
    case IRON_DOOR_BLOCK: 
    case WHEAT: 
      return new CraftBanner(this);
    }
    return new CraftBlockState(this);
  }
  
  public Biome getBiome()
  {
    return getWorld().getBiome(this.x, this.z);
  }
  
  public void setBiome(Biome bio)
  {
    getWorld().setBiome(this.x, this.z, bio);
  }
  
  public static Biome biomeBaseToBiome(BiomeBase base)
  {
    if (base == null) {
      return null;
    }
    return BIOME_MAPPING[base.id];
  }
  
  public static BiomeBase biomeToBiomeBase(Biome bio)
  {
    if (bio == null) {
      return null;
    }
    return BIOMEBASE_MAPPING[bio.ordinal()];
  }
  
  public double getTemperature()
  {
    return getWorld().getTemperature(this.x, this.z);
  }
  
  public double getHumidity()
  {
    return getWorld().getHumidity(this.x, this.z);
  }
  
  public boolean isBlockPowered()
  {
    return this.chunk.getHandle().getWorld().getBlockPower(new BlockPosition(this.x, this.y, this.z)) > 0;
  }
  
  public boolean isBlockIndirectlyPowered()
  {
    return this.chunk.getHandle().getWorld().isBlockIndirectlyPowered(new BlockPosition(this.x, this.y, this.z));
  }
  
  public boolean equals(Object o)
  {
    if (o == this) {
      return true;
    }
    if (!(o instanceof CraftBlock)) {
      return false;
    }
    CraftBlock other = (CraftBlock)o;
    
    return (this.x == other.x) && (this.y == other.y) && (this.z == other.z) && (getWorld().equals(other.getWorld()));
  }
  
  public int hashCode()
  {
    return this.y << 24 ^ this.x ^ this.z ^ getWorld().hashCode();
  }
  
  public boolean isBlockFacePowered(BlockFace face)
  {
    return this.chunk.getHandle().getWorld().isBlockFacePowered(new BlockPosition(this.x, this.y, this.z), blockFaceToNotch(face));
  }
  
  public boolean isBlockFaceIndirectlyPowered(BlockFace face)
  {
    int power = this.chunk.getHandle().getWorld().getBlockFacePower(new BlockPosition(this.x, this.y, this.z), blockFaceToNotch(face));
    
    org.bukkit.block.Block relative = getRelative(face);
    if (relative.getType() == org.bukkit.Material.REDSTONE_WIRE) {
      return Math.max(power, relative.getData()) > 0;
    }
    return power > 0;
  }
  
  public int getBlockPower(BlockFace face)
  {
    int power = 0;
    BlockRedstoneWire wire = Blocks.REDSTONE_WIRE;
    net.minecraft.server.v1_8_R3.World world = this.chunk.getHandle().getWorld();
    if (((face == BlockFace.DOWN) || (face == BlockFace.SELF)) && (world.isBlockFacePowered(new BlockPosition(this.x, this.y - 1, this.z), EnumDirection.DOWN))) {
      power = wire.getPower(world, new BlockPosition(this.x, this.y - 1, this.z), power);
    }
    if (((face == BlockFace.UP) || (face == BlockFace.SELF)) && (world.isBlockFacePowered(new BlockPosition(this.x, this.y + 1, this.z), EnumDirection.UP))) {
      power = wire.getPower(world, new BlockPosition(this.x, this.y + 1, this.z), power);
    }
    if (((face == BlockFace.EAST) || (face == BlockFace.SELF)) && (world.isBlockFacePowered(new BlockPosition(this.x + 1, this.y, this.z), EnumDirection.EAST))) {
      power = wire.getPower(world, new BlockPosition(this.x + 1, this.y, this.z), power);
    }
    if (((face == BlockFace.WEST) || (face == BlockFace.SELF)) && (world.isBlockFacePowered(new BlockPosition(this.x - 1, this.y, this.z), EnumDirection.WEST))) {
      power = wire.getPower(world, new BlockPosition(this.x - 1, this.y, this.z), power);
    }
    if (((face == BlockFace.NORTH) || (face == BlockFace.SELF)) && (world.isBlockFacePowered(new BlockPosition(this.x, this.y, this.z - 1), EnumDirection.NORTH))) {
      power = wire.getPower(world, new BlockPosition(this.x, this.y, this.z - 1), power);
    }
    if (((face == BlockFace.SOUTH) || (face == BlockFace.SELF)) && (world.isBlockFacePowered(new BlockPosition(this.x, this.y, this.z + 1), EnumDirection.SOUTH))) {
      power = wire.getPower(world, new BlockPosition(this.x, this.y, this.z - 1), power);
    }
    return face == BlockFace.SELF ? isBlockIndirectlyPowered() : isBlockFaceIndirectlyPowered(face) ? 15 : power > 0 ? power : 0;
  }
  
  public int getBlockPower()
  {
    return getBlockPower(BlockFace.SELF);
  }
  
  public boolean isEmpty()
  {
    return getType() == org.bukkit.Material.AIR;
  }
  
  public boolean isLiquid()
  {
    return (getType() == org.bukkit.Material.WATER) || (getType() == org.bukkit.Material.STATIONARY_WATER) || (getType() == org.bukkit.Material.LAVA) || (getType() == org.bukkit.Material.STATIONARY_LAVA);
  }
  
  public PistonMoveReaction getPistonMoveReaction()
  {
    return PistonMoveReaction.getById(getNMSBlock().getMaterial().getPushReaction());
  }
  
  private boolean itemCausesDrops(org.bukkit.inventory.ItemStack item)
  {
    net.minecraft.server.v1_8_R3.Block block = getNMSBlock();
    Item itemType = item != null ? Item.getById(item.getTypeId()) : null;
    return (block != null) && ((block.getMaterial().isAlwaysDestroyable()) || ((itemType != null) && (itemType.canDestroySpecialBlock(block))));
  }
  
  public boolean breakNaturally()
  {
    net.minecraft.server.v1_8_R3.Block block = getNMSBlock();
    byte data = getData();
    boolean result = false;
    if ((block != null) && (block != Blocks.AIR))
    {
      block.dropNaturally(this.chunk.getHandle().getWorld(), new BlockPosition(this.x, this.y, this.z), block.fromLegacyData(data), 1.0F, 0);
      result = true;
    }
    setTypeId(org.bukkit.Material.AIR.getId());
    return result;
  }
  
  public boolean breakNaturally(org.bukkit.inventory.ItemStack item)
  {
    if (itemCausesDrops(item)) {
      return breakNaturally();
    }
    return setTypeId(org.bukkit.Material.AIR.getId());
  }
  
  public Collection<org.bukkit.inventory.ItemStack> getDrops()
  {
    List<org.bukkit.inventory.ItemStack> drops = new ArrayList();
    
    net.minecraft.server.v1_8_R3.Block block = getNMSBlock();
    if (block != Blocks.AIR)
    {
      byte data = getData();
      
      int count = block.getDropCount(0, this.chunk.getHandle().getWorld().random);
      for (int i = 0; i < count; i++)
      {
        Item item = block.getDropType(block.fromLegacyData(data), this.chunk.getHandle().getWorld().random, 0);
        if (item != null) {
          if (Blocks.SKULL == block)
          {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = new net.minecraft.server.v1_8_R3.ItemStack(item, 1, block.getDropData(this.chunk.getHandle().getWorld(), new BlockPosition(this.x, this.y, this.z)));
            TileEntitySkull tileentityskull = (TileEntitySkull)this.chunk.getHandle().getWorld().getTileEntity(new BlockPosition(this.x, this.y, this.z));
            if ((tileentityskull.getSkullType() == 3) && (tileentityskull.getGameProfile() != null))
            {
              nmsStack.setTag(new NBTTagCompound());
              NBTTagCompound nbttagcompound = new NBTTagCompound();
              
              GameProfileSerializer.serialize(nbttagcompound, tileentityskull.getGameProfile());
              nmsStack.getTag().set("SkullOwner", nbttagcompound);
            }
            drops.add(CraftItemStack.asBukkitCopy(nmsStack));
          }
          else if (Blocks.COCOA == block)
          {
            int age = ((Integer)block.fromLegacyData(data).get(BlockCocoa.AGE)).intValue();
            int dropAmount = age >= 2 ? 3 : 1;
            for (int j = 0; j < dropAmount; j++) {
              drops.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.INK_SACK, 1, (short)3));
            }
          }
          else
          {
            drops.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(item), 1, (short)block.getDropData(block.fromLegacyData(data))));
          }
        }
      }
    }
    return drops;
  }
  
  public Collection<org.bukkit.inventory.ItemStack> getDrops(org.bukkit.inventory.ItemStack item)
  {
    if (itemCausesDrops(item)) {
      return getDrops();
    }
    return Collections.emptyList();
  }
  
  private static final Biome[] BIOME_MAPPING = new Biome[BiomeBase.getBiomes().length];
  private static final BiomeBase[] BIOMEBASE_MAPPING = new BiomeBase[Biome.values().length];
  
  static
  {
    BIOME_MAPPING[BiomeBase.OCEAN.id] = Biome.OCEAN;
    BIOME_MAPPING[BiomeBase.PLAINS.id] = Biome.PLAINS;
    BIOME_MAPPING[BiomeBase.DESERT.id] = Biome.DESERT;
    BIOME_MAPPING[BiomeBase.EXTREME_HILLS.id] = Biome.EXTREME_HILLS;
    BIOME_MAPPING[BiomeBase.FOREST.id] = Biome.FOREST;
    BIOME_MAPPING[BiomeBase.TAIGA.id] = Biome.TAIGA;
    BIOME_MAPPING[BiomeBase.SWAMPLAND.id] = Biome.SWAMPLAND;
    BIOME_MAPPING[BiomeBase.RIVER.id] = Biome.RIVER;
    BIOME_MAPPING[BiomeBase.HELL.id] = Biome.HELL;
    BIOME_MAPPING[BiomeBase.SKY.id] = Biome.SKY;
    BIOME_MAPPING[BiomeBase.FROZEN_OCEAN.id] = Biome.FROZEN_OCEAN;
    BIOME_MAPPING[BiomeBase.FROZEN_RIVER.id] = Biome.FROZEN_RIVER;
    BIOME_MAPPING[BiomeBase.ICE_PLAINS.id] = Biome.ICE_PLAINS;
    BIOME_MAPPING[BiomeBase.ICE_MOUNTAINS.id] = Biome.ICE_MOUNTAINS;
    BIOME_MAPPING[BiomeBase.MUSHROOM_ISLAND.id] = Biome.MUSHROOM_ISLAND;
    BIOME_MAPPING[BiomeBase.MUSHROOM_SHORE.id] = Biome.MUSHROOM_SHORE;
    BIOME_MAPPING[BiomeBase.BEACH.id] = Biome.BEACH;
    BIOME_MAPPING[BiomeBase.DESERT_HILLS.id] = Biome.DESERT_HILLS;
    BIOME_MAPPING[BiomeBase.FOREST_HILLS.id] = Biome.FOREST_HILLS;
    BIOME_MAPPING[BiomeBase.TAIGA_HILLS.id] = Biome.TAIGA_HILLS;
    BIOME_MAPPING[BiomeBase.SMALL_MOUNTAINS.id] = Biome.SMALL_MOUNTAINS;
    BIOME_MAPPING[BiomeBase.JUNGLE.id] = Biome.JUNGLE;
    BIOME_MAPPING[BiomeBase.JUNGLE_HILLS.id] = Biome.JUNGLE_HILLS;
    BIOME_MAPPING[BiomeBase.JUNGLE_EDGE.id] = Biome.JUNGLE_EDGE;
    BIOME_MAPPING[BiomeBase.DEEP_OCEAN.id] = Biome.DEEP_OCEAN;
    BIOME_MAPPING[BiomeBase.STONE_BEACH.id] = Biome.STONE_BEACH;
    BIOME_MAPPING[BiomeBase.COLD_BEACH.id] = Biome.COLD_BEACH;
    BIOME_MAPPING[BiomeBase.BIRCH_FOREST.id] = Biome.BIRCH_FOREST;
    BIOME_MAPPING[BiomeBase.BIRCH_FOREST_HILLS.id] = Biome.BIRCH_FOREST_HILLS;
    BIOME_MAPPING[BiomeBase.ROOFED_FOREST.id] = Biome.ROOFED_FOREST;
    BIOME_MAPPING[BiomeBase.COLD_TAIGA.id] = Biome.COLD_TAIGA;
    BIOME_MAPPING[BiomeBase.COLD_TAIGA_HILLS.id] = Biome.COLD_TAIGA_HILLS;
    BIOME_MAPPING[BiomeBase.MEGA_TAIGA.id] = Biome.MEGA_TAIGA;
    BIOME_MAPPING[BiomeBase.MEGA_TAIGA_HILLS.id] = Biome.MEGA_TAIGA_HILLS;
    BIOME_MAPPING[BiomeBase.EXTREME_HILLS_PLUS.id] = Biome.EXTREME_HILLS_PLUS;
    BIOME_MAPPING[BiomeBase.SAVANNA.id] = Biome.SAVANNA;
    BIOME_MAPPING[BiomeBase.SAVANNA_PLATEAU.id] = Biome.SAVANNA_PLATEAU;
    BIOME_MAPPING[BiomeBase.MESA.id] = Biome.MESA;
    BIOME_MAPPING[BiomeBase.MESA_PLATEAU_F.id] = Biome.MESA_PLATEAU_FOREST;
    BIOME_MAPPING[BiomeBase.MESA_PLATEAU.id] = Biome.MESA_PLATEAU;
    
    BIOME_MAPPING[(BiomeBase.PLAINS.id + 128)] = Biome.SUNFLOWER_PLAINS;
    BIOME_MAPPING[(BiomeBase.DESERT.id + 128)] = Biome.DESERT_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.FOREST.id + 128)] = Biome.FLOWER_FOREST;
    BIOME_MAPPING[(BiomeBase.TAIGA.id + 128)] = Biome.TAIGA_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.SWAMPLAND.id + 128)] = Biome.SWAMPLAND_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.ICE_PLAINS.id + 128)] = Biome.ICE_PLAINS_SPIKES;
    BIOME_MAPPING[(BiomeBase.JUNGLE.id + 128)] = Biome.JUNGLE_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.JUNGLE_EDGE.id + 128)] = Biome.JUNGLE_EDGE_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.COLD_TAIGA.id + 128)] = Biome.COLD_TAIGA_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.SAVANNA.id + 128)] = Biome.SAVANNA_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.SAVANNA_PLATEAU.id + 128)] = Biome.SAVANNA_PLATEAU_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.MESA.id + 128)] = Biome.MESA_BRYCE;
    BIOME_MAPPING[(BiomeBase.MESA_PLATEAU_F.id + 128)] = Biome.MESA_PLATEAU_FOREST_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.MESA_PLATEAU.id + 128)] = Biome.MESA_PLATEAU_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.BIRCH_FOREST.id + 128)] = Biome.BIRCH_FOREST_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.BIRCH_FOREST_HILLS.id + 128)] = Biome.BIRCH_FOREST_HILLS_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.ROOFED_FOREST.id + 128)] = Biome.ROOFED_FOREST_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.MEGA_TAIGA.id + 128)] = Biome.MEGA_SPRUCE_TAIGA;
    BIOME_MAPPING[(BiomeBase.EXTREME_HILLS.id + 128)] = Biome.EXTREME_HILLS_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.EXTREME_HILLS_PLUS.id + 128)] = Biome.EXTREME_HILLS_PLUS_MOUNTAINS;
    BIOME_MAPPING[(BiomeBase.MEGA_TAIGA_HILLS.id + 128)] = Biome.MEGA_SPRUCE_TAIGA_HILLS;
    for (int i = 0; i < BIOME_MAPPING.length; i++)
    {
      if ((BiomeBase.getBiome(i) != null) && (BIOME_MAPPING[i] == null)) {
        throw new IllegalArgumentException("Missing Biome mapping for BiomeBase[" + i + ", " + BiomeBase.getBiome(i) + "]");
      }
      if (BIOME_MAPPING[i] != null) {
        BIOMEBASE_MAPPING[BIOME_MAPPING[i].ordinal()] = BiomeBase.getBiome(i);
      }
    }
  }
  
  public void setMetadata(String metadataKey, MetadataValue newMetadataValue)
  {
    this.chunk.getCraftWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
  }
  
  public List<MetadataValue> getMetadata(String metadataKey)
  {
    return this.chunk.getCraftWorld().getBlockMetadata().getMetadata(this, metadataKey);
  }
  
  public boolean hasMetadata(String metadataKey)
  {
    return this.chunk.getCraftWorld().getBlockMetadata().hasMetadata(this, metadataKey);
  }
  
  public void removeMetadata(String metadataKey, Plugin owningPlugin)
  {
    this.chunk.getCraftWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
  }
}
