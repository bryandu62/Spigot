package org.bukkit.block;

import org.bukkit.projectiles.BlockProjectileSource;

public abstract interface Dispenser
  extends BlockState, ContainerBlock
{
  public abstract BlockProjectileSource getBlockProjectileSource();
  
  public abstract boolean dispense();
}
