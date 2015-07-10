package org.bukkit.craftbukkit.v1_8_R3.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import net.minecraft.server.v1_8_R3.UserCache;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class CraftSkull
  extends CraftBlockState
  implements Skull
{
  private static final int MAX_OWNER_LENGTH = 16;
  private final TileEntitySkull skull;
  private GameProfile profile;
  private SkullType skullType;
  private byte rotation;
  
  public CraftSkull(Block block)
  {
    super(block);
    
    CraftWorld world = (CraftWorld)block.getWorld();
    this.skull = ((TileEntitySkull)world.getTileEntityAt(getX(), getY(), getZ()));
    this.profile = this.skull.getGameProfile();
    this.skullType = getSkullType(this.skull.getSkullType());
    this.rotation = ((byte)this.skull.getRotation());
  }
  
  public CraftSkull(Material material, TileEntitySkull te)
  {
    super(material);
    this.skull = te;
    this.profile = this.skull.getGameProfile();
    this.skullType = getSkullType(this.skull.getSkullType());
    this.rotation = ((byte)this.skull.getRotation());
  }
  
  static SkullType getSkullType(int id)
  {
    switch (id)
    {
    case 0: 
      return SkullType.SKELETON;
    case 1: 
      return SkullType.WITHER;
    case 2: 
      return SkullType.ZOMBIE;
    case 3: 
      return SkullType.PLAYER;
    case 4: 
      return SkullType.CREEPER;
    }
    throw new AssertionError(id);
  }
  
  static int getSkullType(SkullType type)
  {
    switch (type)
    {
    case CREEPER: 
      return 0;
    case PLAYER: 
      return 1;
    case SKELETON: 
      return 2;
    case WITHER: 
      return 3;
    case ZOMBIE: 
      return 4;
    }
    throw new AssertionError(type);
  }
  
  static byte getBlockFace(BlockFace rotation)
  {
    switch (rotation)
    {
    case DOWN: 
      return 0;
    case SOUTH_SOUTH_EAST: 
      return 1;
    case NORTH_NORTH_EAST: 
      return 2;
    case SOUTH_SOUTH_WEST: 
      return 3;
    case EAST: 
      return 4;
    case SOUTH_WEST: 
      return 5;
    case NORTH_WEST: 
      return 6;
    case UP: 
      return 7;
    case EAST_NORTH_EAST: 
      return 8;
    case WEST: 
      return 9;
    case SELF: 
      return 10;
    case WEST_NORTH_WEST: 
      return 11;
    case EAST_SOUTH_EAST: 
      return 12;
    case SOUTH: 
      return 13;
    case NORTH_NORTH_WEST: 
      return 14;
    case SOUTH_EAST: 
      return 15;
    }
    throw new IllegalArgumentException("Invalid BlockFace rotation: " + rotation);
  }
  
  static BlockFace getBlockFace(byte rotation)
  {
    switch (rotation)
    {
    case 0: 
      return BlockFace.NORTH;
    case 1: 
      return BlockFace.NORTH_NORTH_EAST;
    case 2: 
      return BlockFace.NORTH_EAST;
    case 3: 
      return BlockFace.EAST_NORTH_EAST;
    case 4: 
      return BlockFace.EAST;
    case 5: 
      return BlockFace.EAST_SOUTH_EAST;
    case 6: 
      return BlockFace.SOUTH_EAST;
    case 7: 
      return BlockFace.SOUTH_SOUTH_EAST;
    case 8: 
      return BlockFace.SOUTH;
    case 9: 
      return BlockFace.SOUTH_SOUTH_WEST;
    case 10: 
      return BlockFace.SOUTH_WEST;
    case 11: 
      return BlockFace.WEST_SOUTH_WEST;
    case 12: 
      return BlockFace.WEST;
    case 13: 
      return BlockFace.WEST_NORTH_WEST;
    case 14: 
      return BlockFace.NORTH_WEST;
    case 15: 
      return BlockFace.NORTH_NORTH_WEST;
    }
    throw new AssertionError(rotation);
  }
  
  public boolean hasOwner()
  {
    return this.profile != null;
  }
  
  public String getOwner()
  {
    return hasOwner() ? this.profile.getName() : null;
  }
  
  public boolean setOwner(String name)
  {
    if ((name == null) || (name.length() > 16)) {
      return false;
    }
    GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(name);
    if (profile == null) {
      return false;
    }
    if (this.skullType != SkullType.PLAYER) {
      this.skullType = SkullType.PLAYER;
    }
    this.profile = profile;
    return true;
  }
  
  public BlockFace getRotation()
  {
    return getBlockFace(this.rotation);
  }
  
  public void setRotation(BlockFace rotation)
  {
    this.rotation = getBlockFace(rotation);
  }
  
  public SkullType getSkullType()
  {
    return this.skullType;
  }
  
  public void setSkullType(SkullType skullType)
  {
    this.skullType = skullType;
    if (skullType != SkullType.PLAYER) {
      this.profile = null;
    }
  }
  
  public boolean update(boolean force, boolean applyPhysics)
  {
    boolean result = super.update(force, applyPhysics);
    if (result)
    {
      if (this.skullType == SkullType.PLAYER) {
        this.skull.setGameProfile(this.profile);
      } else {
        this.skull.setSkullType(getSkullType(this.skullType));
      }
      this.skull.setRotation(this.rotation);
      this.skull.update();
    }
    return result;
  }
  
  public TileEntitySkull getTileEntity()
  {
    return this.skull;
  }
}
