package org.bukkit.craftbukkit.v1_8_R3;

import org.apache.commons.lang.Validate;
import org.bukkit.Sound;

public class CraftSound
{
  private static final String[] sounds = new String[Sound.values().length];
  
  static
  {
    set(Sound.AMBIENCE_CAVE, "ambient.cave.cave");
    set(Sound.AMBIENCE_RAIN, "ambient.weather.rain");
    set(Sound.AMBIENCE_THUNDER, "ambient.weather.thunder");
    
    set(Sound.HURT_FLESH, "game.neutral.hurt");
    set(Sound.FALL_BIG, "game.neutral.hurt.fall.big");
    set(Sound.FALL_SMALL, "game.neutral.hurt.fall.small");
    
    set(Sound.DIG_WOOL, "dig.cloth");
    set(Sound.DIG_GRASS, "dig.grass");
    set(Sound.DIG_GRAVEL, "dig.gravel");
    set(Sound.DIG_SAND, "dig.sand");
    set(Sound.DIG_SNOW, "dig.snow");
    set(Sound.DIG_STONE, "dig.stone");
    set(Sound.DIG_WOOD, "dig.wood");
    
    set(Sound.FIRE, "fire.fire");
    set(Sound.FIRE_IGNITE, "fire.ignite");
    
    set(Sound.FIREWORK_BLAST, "fireworks.blast");
    set(Sound.FIREWORK_BLAST2, "fireworks.blast_far");
    set(Sound.FIREWORK_LARGE_BLAST, "fireworks.largeBlast");
    set(Sound.FIREWORK_LARGE_BLAST2, "fireworks.largeBlast_far");
    set(Sound.FIREWORK_TWINKLE, "fireworks.twinkle");
    set(Sound.FIREWORK_TWINKLE2, "fireworks.twinkle_far");
    set(Sound.FIREWORK_LAUNCH, "fireworks.launch");
    
    set(Sound.SPLASH2, "game.neutral.swim.splash");
    set(Sound.SWIM, "game.neutral.swim");
    set(Sound.WATER, "liquid.water");
    set(Sound.LAVA, "liquid.lava");
    set(Sound.LAVA_POP, "liquid.lavapop");
    
    set(Sound.MINECART_BASE, "minecart.base");
    set(Sound.MINECART_INSIDE, "minecart.inside");
    
    set(Sound.BAT_DEATH, "mob.bat.death");
    set(Sound.BAT_HURT, "mob.bat.hurt");
    set(Sound.BAT_IDLE, "mob.bat.idle");
    set(Sound.BAT_LOOP, "mob.bat.loop");
    set(Sound.BAT_TAKEOFF, "mob.bat.takeoff");
    set(Sound.BLAZE_BREATH, "mob.blaze.breathe");
    set(Sound.BLAZE_DEATH, "mob.blaze.death");
    set(Sound.BLAZE_HIT, "mob.blaze.hit");
    set(Sound.CAT_HISS, "mob.cat.hiss");
    set(Sound.CAT_HIT, "mob.cat.hitt");
    set(Sound.CAT_MEOW, "mob.cat.meow");
    set(Sound.CAT_PURR, "mob.cat.purr");
    set(Sound.CAT_PURREOW, "mob.cat.purreow");
    set(Sound.CHICKEN_IDLE, "mob.chicken.say");
    set(Sound.CHICKEN_HURT, "mob.chicken.hurt");
    set(Sound.CHICKEN_EGG_POP, "mob.chicken.plop");
    set(Sound.CHICKEN_WALK, "mob.chicken.step");
    set(Sound.COW_HURT, "mob.cow.hurt");
    set(Sound.COW_IDLE, "mob.cow.say");
    set(Sound.COW_WALK, "mob.cow.step");
    set(Sound.CREEPER_DEATH, "mob.creeper.death");
    set(Sound.CREEPER_HISS, "mob.creeper.say");
    set(Sound.ENDERDRAGON_DEATH, "mob.enderdragon.end");
    set(Sound.ENDERDRAGON_GROWL, "mob.enderdragon.growl");
    set(Sound.ENDERDRAGON_HIT, "mob.enderdragon.hit");
    set(Sound.ENDERDRAGON_WINGS, "mob.enderdragon.wings");
    set(Sound.ENDERMAN_DEATH, "mob.endermen.death");
    set(Sound.ENDERMAN_HIT, "mob.endermen.hit");
    set(Sound.ENDERMAN_IDLE, "mob.endermen.idle");
    set(Sound.ENDERMAN_TELEPORT, "mob.endermen.portal");
    set(Sound.ENDERMAN_SCREAM, "mob.endermen.scream");
    set(Sound.ENDERMAN_STARE, "mob.endermen.stare");
    set(Sound.GHAST_SCREAM2, "mob.ghast.affectionate_scream");
    set(Sound.GHAST_CHARGE, "mob.ghast.charge");
    set(Sound.GHAST_DEATH, "mob.ghast.death");
    set(Sound.GHAST_FIREBALL, "mob.ghast.fireball");
    set(Sound.GHAST_MOAN, "mob.ghast.moan");
    set(Sound.GHAST_SCREAM, "mob.ghast.scream");
    set(Sound.HORSE_ANGRY, "mob.horse.angry");
    set(Sound.HORSE_ARMOR, "mob.horse.armor");
    set(Sound.HORSE_BREATHE, "mob.horse.breathe");
    set(Sound.HORSE_DEATH, "mob.horse.death");
    set(Sound.HORSE_GALLOP, "mob.horse.gallop");
    set(Sound.HORSE_HIT, "mob.horse.hit");
    set(Sound.HORSE_IDLE, "mob.horse.idle");
    set(Sound.HORSE_JUMP, "mob.horse.jump");
    set(Sound.HORSE_LAND, "mob.horse.land");
    set(Sound.HORSE_SADDLE, "mob.horse.leather");
    set(Sound.HORSE_SOFT, "mob.horse.soft");
    set(Sound.HORSE_WOOD, "mob.horse.wood");
    set(Sound.DONKEY_ANGRY, "mob.horse.donkey.angry");
    set(Sound.DONKEY_DEATH, "mob.horse.donkey.death");
    set(Sound.DONKEY_HIT, "mob.horse.donkey.hit");
    set(Sound.DONKEY_IDLE, "mob.horse.donkey.idle");
    set(Sound.HORSE_SKELETON_DEATH, "mob.horse.skeleton.death");
    set(Sound.HORSE_SKELETON_HIT, "mob.horse.skeleton.hit");
    set(Sound.HORSE_SKELETON_IDLE, "mob.horse.skeleton.idle");
    set(Sound.HORSE_ZOMBIE_DEATH, "mob.horse.zombie.death");
    set(Sound.HORSE_ZOMBIE_HIT, "mob.horse.zombie.hit");
    set(Sound.HORSE_ZOMBIE_IDLE, "mob.horse.zombie.idle");
    set(Sound.IRONGOLEM_DEATH, "mob.irongolem.death");
    set(Sound.IRONGOLEM_HIT, "mob.irongolem.hit");
    set(Sound.IRONGOLEM_THROW, "mob.irongolem.throw");
    set(Sound.IRONGOLEM_WALK, "mob.irongolem.walk");
    set(Sound.MAGMACUBE_WALK, "mob.magmacube.small");
    set(Sound.MAGMACUBE_WALK2, "mob.magmacube.big");
    set(Sound.MAGMACUBE_JUMP, "mob.magmacube.jump");
    set(Sound.PIG_IDLE, "mob.pig.say");
    set(Sound.PIG_DEATH, "mob.pig.death");
    set(Sound.PIG_WALK, "mob.pig.step");
    set(Sound.SHEEP_IDLE, "mob.sheep.say");
    set(Sound.SHEEP_SHEAR, "mob.sheep.shear");
    set(Sound.SHEEP_WALK, "mob.sheep.step");
    set(Sound.SILVERFISH_HIT, "mob.silverfish.hit");
    set(Sound.SILVERFISH_KILL, "mob.silverfish.kill");
    set(Sound.SILVERFISH_IDLE, "mob.silverfish.say");
    set(Sound.SILVERFISH_WALK, "mob.silverfish.step");
    set(Sound.SKELETON_IDLE, "mob.skeleton.say");
    set(Sound.SKELETON_DEATH, "mob.skeleton.death");
    set(Sound.SKELETON_HURT, "mob.skeleton.hurt");
    set(Sound.SKELETON_WALK, "mob.skeleton.step");
    set(Sound.SLIME_ATTACK, "mob.slime.attack");
    set(Sound.SLIME_WALK, "mob.slime.small");
    set(Sound.SLIME_WALK2, "mob.slime.big");
    set(Sound.SPIDER_IDLE, "mob.spider.say");
    set(Sound.SPIDER_DEATH, "mob.spider.death");
    set(Sound.SPIDER_WALK, "mob.spider.step");
    set(Sound.VILLAGER_DEATH, "mob.villager.death");
    set(Sound.VILLAGER_HAGGLE, "mob.villager.haggle");
    set(Sound.VILLAGER_HIT, "mob.villager.hit");
    set(Sound.VILLAGER_IDLE, "mob.villager.idle");
    set(Sound.VILLAGER_NO, "mob.villager.no");
    set(Sound.VILLAGER_YES, "mob.villager.yes");
    set(Sound.WITHER_DEATH, "mob.wither.death");
    set(Sound.WITHER_HURT, "mob.wither.hurt");
    set(Sound.WITHER_IDLE, "mob.wither.idle");
    set(Sound.WITHER_SHOOT, "mob.wither.shoot");
    set(Sound.WITHER_SPAWN, "mob.wither.spawn");
    set(Sound.WOLF_BARK, "mob.wolf.bark");
    set(Sound.WOLF_DEATH, "mob.wolf.death");
    set(Sound.WOLF_GROWL, "mob.wolf.growl");
    set(Sound.WOLF_HOWL, "mob.wolf.howl");
    set(Sound.WOLF_HURT, "mob.wolf.hurt");
    set(Sound.WOLF_PANT, "mob.wolf.panting");
    set(Sound.WOLF_SHAKE, "mob.wolf.shake");
    set(Sound.WOLF_WALK, "mob.wolf.step");
    set(Sound.WOLF_WHINE, "mob.wolf.whine");
    set(Sound.ZOMBIE_METAL, "mob.zombie.metal");
    set(Sound.ZOMBIE_WOOD, "mob.zombie.wood");
    set(Sound.ZOMBIE_WOODBREAK, "mob.zombie.woodbreak");
    set(Sound.ZOMBIE_IDLE, "mob.zombie.say");
    set(Sound.ZOMBIE_DEATH, "mob.zombie.death");
    set(Sound.ZOMBIE_HURT, "mob.zombie.hurt");
    set(Sound.ZOMBIE_INFECT, "mob.zombie.infect");
    set(Sound.ZOMBIE_UNFECT, "mob.zombie.unfect");
    set(Sound.ZOMBIE_REMEDY, "mob.zombie.remedy");
    set(Sound.ZOMBIE_WALK, "mob.zombie.step");
    set(Sound.ZOMBIE_PIG_IDLE, "mob.zombiepig.zpig");
    set(Sound.ZOMBIE_PIG_ANGRY, "mob.zombiepig.zpigangry");
    set(Sound.ZOMBIE_PIG_DEATH, "mob.zombiepig.zpigdeath");
    set(Sound.ZOMBIE_PIG_HURT, "mob.zombiepig.zpighurt");
    
    set(Sound.NOTE_BASS_GUITAR, "note.bassattack");
    set(Sound.NOTE_SNARE_DRUM, "note.snare");
    set(Sound.NOTE_PLING, "note.pling");
    set(Sound.NOTE_BASS, "note.bass");
    set(Sound.NOTE_PIANO, "note.harp");
    set(Sound.NOTE_BASS_DRUM, "note.bd");
    set(Sound.NOTE_STICKS, "note.hat");
    
    set(Sound.PORTAL, "portal.portal");
    set(Sound.PORTAL_TRAVEL, "portal.travel");
    set(Sound.PORTAL_TRIGGER, "portal.trigger");
    
    set(Sound.ANVIL_BREAK, "random.anvil_break");
    set(Sound.ANVIL_LAND, "random.anvil_land");
    set(Sound.ANVIL_USE, "random.anvil_use");
    set(Sound.SHOOT_ARROW, "random.bow");
    set(Sound.ARROW_HIT, "random.bowhit");
    set(Sound.ITEM_BREAK, "random.break");
    set(Sound.BURP, "random.burp");
    set(Sound.CHEST_CLOSE, "random.chestclosed");
    set(Sound.CHEST_OPEN, "random.chestopen");
    set(Sound.CLICK, "random.click");
    set(Sound.DOOR_CLOSE, "random.door_close");
    set(Sound.DOOR_OPEN, "random.door_open");
    set(Sound.DRINK, "random.drink");
    set(Sound.EAT, "random.eat");
    set(Sound.EXPLODE, "random.explode");
    set(Sound.FIZZ, "random.fizz");
    set(Sound.FUSE, "creeper.primed");
    set(Sound.GLASS, "dig.glass");
    set(Sound.LEVEL_UP, "random.levelup");
    set(Sound.ORB_PICKUP, "random.orb");
    set(Sound.ITEM_PICKUP, "random.pop");
    set(Sound.SPLASH, "random.splash");
    set(Sound.SUCCESSFUL_HIT, "random.successful_hit");
    set(Sound.WOOD_CLICK, "random.wood_click");
    
    set(Sound.STEP_WOOL, "step.cloth");
    set(Sound.STEP_GRASS, "step.grass");
    set(Sound.STEP_GRAVEL, "step.gravel");
    set(Sound.STEP_LADDER, "step.ladder");
    set(Sound.STEP_SAND, "step.sand");
    set(Sound.STEP_SNOW, "step.snow");
    set(Sound.STEP_STONE, "step.stone");
    set(Sound.STEP_WOOD, "step.wood");
    
    set(Sound.PISTON_EXTEND, "tile.piston.out");
    set(Sound.PISTON_RETRACT, "tile.piston.in");
  }
  
  private static void set(Sound sound, String key)
  {
    sounds[sound.ordinal()] = key;
  }
  
  public static String getSound(Sound sound)
  {
    Validate.notNull(sound, "Sound cannot be null");
    return sounds[sound.ordinal()];
  }
}
