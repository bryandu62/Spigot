package org.bukkit.craftbukkit.v1_8_R3;

import net.minecraft.server.v1_8_R3.EntityPainting.EnumArt;
import org.bukkit.Art;

public class CraftArt
{
  public static Art NotchToBukkit(EntityPainting.EnumArt art)
  {
    switch (art)
    {
    case ALBAN: 
      return Art.KEBAB;
    case AZTEC: 
      return Art.AZTEC;
    case AZTEC_2: 
      return Art.ALBAN;
    case BOMB: 
      return Art.AZTEC2;
    case BURNING_SKULL: 
      return Art.BOMB;
    case BUST: 
      return Art.PLANT;
    case COURBET: 
      return Art.WASTELAND;
    case CREEBET: 
      return Art.POOL;
    case DONKEY_KONG: 
      return Art.COURBET;
    case FIGHTERS: 
      return Art.SEA;
    case GRAHAM: 
      return Art.SUNSET;
    case KEBAB: 
      return Art.CREEBET;
    case MATCH: 
      return Art.WANDERER;
    case PIGSCENE: 
      return Art.GRAHAM;
    case PLANT: 
      return Art.MATCH;
    case POINTER: 
      return Art.BUST;
    case POOL: 
      return Art.STAGE;
    case SEA: 
      return Art.VOID;
    case SKELETON: 
      return Art.SKULL_AND_ROSES;
    case STAGE: 
      return Art.FIGHTERS;
    case SUNSET: 
      return Art.POINTER;
    case VOID: 
      return Art.PIGSCENE;
    case WANDERER: 
      return Art.BURNINGSKULL;
    case WASTELAND: 
      return Art.SKELETON;
    case WITHER: 
      return Art.DONKEYKONG;
    case SKULL_AND_ROSES: 
      return Art.WITHER;
    }
    throw new AssertionError(art);
  }
  
  public static EntityPainting.EnumArt BukkitToNotch(Art art)
  {
    switch (art)
    {
    case ALBAN: 
      return EntityPainting.EnumArt.KEBAB;
    case AZTEC: 
      return EntityPainting.EnumArt.AZTEC;
    case AZTEC2: 
      return EntityPainting.EnumArt.ALBAN;
    case BOMB: 
      return EntityPainting.EnumArt.AZTEC_2;
    case BURNINGSKULL: 
      return EntityPainting.EnumArt.BOMB;
    case BUST: 
      return EntityPainting.EnumArt.PLANT;
    case COURBET: 
      return EntityPainting.EnumArt.WASTELAND;
    case CREEBET: 
      return EntityPainting.EnumArt.POOL;
    case DONKEYKONG: 
      return EntityPainting.EnumArt.COURBET;
    case FIGHTERS: 
      return EntityPainting.EnumArt.SEA;
    case GRAHAM: 
      return EntityPainting.EnumArt.SUNSET;
    case KEBAB: 
      return EntityPainting.EnumArt.CREEBET;
    case MATCH: 
      return EntityPainting.EnumArt.WANDERER;
    case PIGSCENE: 
      return EntityPainting.EnumArt.GRAHAM;
    case PLANT: 
      return EntityPainting.EnumArt.MATCH;
    case POINTER: 
      return EntityPainting.EnumArt.BUST;
    case POOL: 
      return EntityPainting.EnumArt.STAGE;
    case SEA: 
      return EntityPainting.EnumArt.VOID;
    case SKELETON: 
      return EntityPainting.EnumArt.SKULL_AND_ROSES;
    case STAGE: 
      return EntityPainting.EnumArt.FIGHTERS;
    case SUNSET: 
      return EntityPainting.EnumArt.POINTER;
    case VOID: 
      return EntityPainting.EnumArt.PIGSCENE;
    case WANDERER: 
      return EntityPainting.EnumArt.BURNING_SKULL;
    case WASTELAND: 
      return EntityPainting.EnumArt.SKELETON;
    case WITHER: 
      return EntityPainting.EnumArt.DONKEY_KONG;
    case SKULL_AND_ROSES: 
      return EntityPainting.EnumArt.WITHER;
    }
    throw new AssertionError(art);
  }
}
