package org.bukkit.projectiles;

import org.bukkit.block.Block;

public abstract interface BlockProjectileSource
  extends ProjectileSource
{
  public abstract Block getBlock();
}
