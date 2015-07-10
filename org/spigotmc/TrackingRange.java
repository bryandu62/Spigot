package org.spigotmc;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.World;

public class TrackingRange
{
  public static int getEntityTrackingRange(Entity entity, int defaultRange)
  {
    SpigotWorldConfig config = entity.world.spigotConfig;
    if ((entity instanceof EntityPlayer)) {
      return config.playerTrackingRange;
    }
    if (entity.activationType == 1) {
      return config.monsterTrackingRange;
    }
    if ((entity instanceof EntityGhast))
    {
      if (config.monsterTrackingRange > config.monsterActivationRange) {
        return config.monsterTrackingRange;
      }
      return config.monsterActivationRange;
    }
    if (entity.activationType == 2) {
      return config.animalTrackingRange;
    }
    if (((entity instanceof EntityItemFrame)) || ((entity instanceof EntityPainting)) || ((entity instanceof EntityItem)) || ((entity instanceof EntityExperienceOrb))) {
      return config.miscTrackingRange;
    }
    return config.otherTrackingRange;
  }
}
