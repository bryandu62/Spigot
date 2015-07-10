package org.bukkit.entity;

import java.util.Set;

public abstract interface ComplexLivingEntity
  extends LivingEntity
{
  public abstract Set<ComplexEntityPart> getParts();
}
