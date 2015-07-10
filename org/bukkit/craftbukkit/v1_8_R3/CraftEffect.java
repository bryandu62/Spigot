package org.bukkit.craftbukkit.v1_8_R3;

import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.potion.Potion;

public class CraftEffect
{
  public static <T> int getDataValue(Effect effect, T data)
  {
    int datavalue;
    int datavalue;
    int datavalue;
    int datavalue;
    int datavalue;
    switch (effect)
    {
    case FLAME: 
      datavalue = ((Potion)data).toDamageValue() & 0x3F;
      break;
    case COLOURED_DUST: 
      Validate.isTrue(((Material)data).isRecord(), "Invalid record type!");
      datavalue = ((Material)data).getId();
      break;
    case EXTINGUISH: 
      int datavalue;
      int datavalue;
      int datavalue;
      int datavalue;
      int datavalue;
      int datavalue;
      int datavalue;
      int datavalue;
      int datavalue;
      switch ((org.bukkit.block.BlockFace)data)
      {
      case NORTH_WEST: 
        datavalue = 0;
        break;
      case EAST_NORTH_EAST: 
        datavalue = 1;
        break;
      case SELF: 
        datavalue = 2;
        break;
      case EAST: 
        datavalue = 3;
        break;
      case NORTH: 
      case WEST_SOUTH_WEST: 
        datavalue = 4;
        break;
      case EAST_SOUTH_EAST: 
        datavalue = 5;
        break;
      case NORTH_NORTH_EAST: 
        datavalue = 6;
        break;
      case DOWN: 
        datavalue = 7;
        break;
      case NORTH_NORTH_WEST: 
        datavalue = 8;
        break;
      case NORTH_EAST: 
      case SOUTH: 
      case SOUTH_EAST: 
      case SOUTH_SOUTH_EAST: 
      case SOUTH_SOUTH_WEST: 
      case SOUTH_WEST: 
      case UP: 
      case WEST: 
      case WEST_NORTH_WEST: 
      default: 
        throw new IllegalArgumentException("Bad smoke direction!");
      }
      break;
    case FIREWORKS_SPARK: 
      Validate.isTrue(((Material)data).isBlock(), "Material is not a block!");
      datavalue = ((Material)data).getId();
      break;
    case ZOMBIE_CHEW_IRON_DOOR: 
      datavalue = ((Material)data).getId();
      break;
    default: 
      datavalue = 0;
    }
    return datavalue;
  }
}
