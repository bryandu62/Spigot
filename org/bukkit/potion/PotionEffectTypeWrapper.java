package org.bukkit.potion;

public class PotionEffectTypeWrapper
  extends PotionEffectType
{
  protected PotionEffectTypeWrapper(int id)
  {
    super(id);
  }
  
  public double getDurationModifier()
  {
    return getType().getDurationModifier();
  }
  
  public String getName()
  {
    return getType().getName();
  }
  
  public PotionEffectType getType()
  {
    return PotionEffectType.getById(getId());
  }
  
  public boolean isInstant()
  {
    return getType().isInstant();
  }
}
