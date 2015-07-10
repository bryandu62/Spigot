package org.bukkit.material;

import java.util.Arrays;
import java.util.EnumSet;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Vine
  extends MaterialData
{
  private static final int VINE_NORTH = 4;
  private static final int VINE_EAST = 8;
  private static final int VINE_WEST = 2;
  private static final int VINE_SOUTH = 1;
  EnumSet<BlockFace> possibleFaces = EnumSet.of(BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST);
  
  public Vine()
  {
    super(Material.VINE);
  }
  
  @Deprecated
  public Vine(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Vine(byte data)
  {
    super(Material.VINE, data);
  }
  
  public Vine(BlockFace... faces)
  {
    this(EnumSet.copyOf(Arrays.asList(faces)));
  }
  
  public Vine(EnumSet<BlockFace> faces)
  {
    this((byte)0);
    faces.retainAll(this.possibleFaces);
    
    byte data = 0;
    if (faces.contains(BlockFace.WEST)) {
      data = (byte)(data | 0x2);
    }
    if (faces.contains(BlockFace.NORTH)) {
      data = (byte)(data | 0x4);
    }
    if (faces.contains(BlockFace.SOUTH)) {
      data = (byte)(data | 0x1);
    }
    if (faces.contains(BlockFace.EAST)) {
      data = (byte)(data | 0x8);
    }
    setData(data);
  }
  
  public boolean isOnFace(BlockFace face)
  {
    switch (face)
    {
    case EAST_SOUTH_EAST: 
      return (getData() & 0x2) == 2;
    case DOWN: 
      return (getData() & 0x4) == 4;
    case EAST_NORTH_EAST: 
      return (getData() & 0x1) == 1;
    case EAST: 
      return (getData() & 0x8) == 8;
    case NORTH_NORTH_EAST: 
      return (isOnFace(BlockFace.EAST)) && (isOnFace(BlockFace.NORTH));
    case NORTH_NORTH_WEST: 
      return (isOnFace(BlockFace.WEST)) && (isOnFace(BlockFace.NORTH));
    case NORTH_WEST: 
      return (isOnFace(BlockFace.EAST)) && (isOnFace(BlockFace.SOUTH));
    case SELF: 
      return (isOnFace(BlockFace.WEST)) && (isOnFace(BlockFace.SOUTH));
    case NORTH: 
      return true;
    }
    return false;
  }
  
  public void putOnFace(BlockFace face)
  {
    switch (face)
    {
    case EAST_SOUTH_EAST: 
      setData((byte)(getData() | 0x2));
      break;
    case DOWN: 
      setData((byte)(getData() | 0x4));
      break;
    case EAST_NORTH_EAST: 
      setData((byte)(getData() | 0x1));
      break;
    case EAST: 
      setData((byte)(getData() | 0x8));
      break;
    case NORTH_NORTH_WEST: 
      putOnFace(BlockFace.WEST);
      putOnFace(BlockFace.NORTH);
      break;
    case SELF: 
      putOnFace(BlockFace.WEST);
      putOnFace(BlockFace.SOUTH);
      break;
    case NORTH_NORTH_EAST: 
      putOnFace(BlockFace.EAST);
      putOnFace(BlockFace.NORTH);
      break;
    case NORTH_WEST: 
      putOnFace(BlockFace.EAST);
      putOnFace(BlockFace.SOUTH);
      break;
    case NORTH: 
      break;
    case NORTH_EAST: 
    default: 
      throw new IllegalArgumentException("Vines can't go on face " + face.toString());
    }
  }
  
  public void removeFromFace(BlockFace face)
  {
    switch (face)
    {
    case EAST_SOUTH_EAST: 
      setData((byte)(getData() & 0xFFFFFFFD));
      break;
    case DOWN: 
      setData((byte)(getData() & 0xFFFFFFFB));
      break;
    case EAST_NORTH_EAST: 
      setData((byte)(getData() & 0xFFFFFFFE));
      break;
    case EAST: 
      setData((byte)(getData() & 0xFFFFFFF7));
      break;
    case NORTH_NORTH_WEST: 
      removeFromFace(BlockFace.WEST);
      removeFromFace(BlockFace.NORTH);
      break;
    case SELF: 
      removeFromFace(BlockFace.WEST);
      removeFromFace(BlockFace.SOUTH);
      break;
    case NORTH_NORTH_EAST: 
      removeFromFace(BlockFace.EAST);
      removeFromFace(BlockFace.NORTH);
      break;
    case NORTH_WEST: 
      removeFromFace(BlockFace.EAST);
      removeFromFace(BlockFace.SOUTH);
      break;
    case NORTH: 
      break;
    case NORTH_EAST: 
    default: 
      throw new IllegalArgumentException("Vines can't go on face " + face.toString());
    }
  }
  
  public String toString()
  {
    return "VINE";
  }
  
  public Vine clone()
  {
    return (Vine)super.clone();
  }
}
