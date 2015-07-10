package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.UUID;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityHorse;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.inventory.HorseInventory;

public class CraftHorse
  extends CraftAnimals
  implements Horse
{
  public CraftHorse(CraftServer server, EntityHorse entity)
  {
    super(server, entity);
  }
  
  public EntityHorse getHandle()
  {
    return (EntityHorse)this.entity;
  }
  
  public Horse.Variant getVariant()
  {
    return Horse.Variant.values()[getHandle().getType()];
  }
  
  public void setVariant(Horse.Variant variant)
  {
    Validate.notNull(variant, "Variant cannot be null");
    getHandle().setType(variant.ordinal());
  }
  
  public Horse.Color getColor()
  {
    return Horse.Color.values()[(getHandle().getVariant() & 0xFF)];
  }
  
  public void setColor(Horse.Color color)
  {
    Validate.notNull(color, "Color cannot be null");
    getHandle().setVariant(color.ordinal() & 0xFF | getStyle().ordinal() << 8);
  }
  
  public Horse.Style getStyle()
  {
    return Horse.Style.values()[(getHandle().getVariant() >>> 8)];
  }
  
  public void setStyle(Horse.Style style)
  {
    Validate.notNull(style, "Style cannot be null");
    getHandle().setVariant(getColor().ordinal() & 0xFF | style.ordinal() << 8);
  }
  
  public boolean isCarryingChest()
  {
    return getHandle().hasChest();
  }
  
  public void setCarryingChest(boolean chest)
  {
    if (chest == isCarryingChest()) {
      return;
    }
    getHandle().setHasChest(chest);
    getHandle().loadChest();
  }
  
  public int getDomestication()
  {
    return getHandle().getTemper();
  }
  
  public void setDomestication(int value)
  {
    Validate.isTrue(value >= 0, "Domestication cannot be less than zero");
    Validate.isTrue(value <= getMaxDomestication(), "Domestication cannot be greater than the max domestication");
    getHandle().setTemper(value);
  }
  
  public int getMaxDomestication()
  {
    return getHandle().getMaxDomestication();
  }
  
  public void setMaxDomestication(int value)
  {
    Validate.isTrue(value > 0, "Max domestication cannot be zero or less");
    getHandle().maxDomestication = value;
  }
  
  public double getJumpStrength()
  {
    return getHandle().getJumpStrength();
  }
  
  public void setJumpStrength(double strength)
  {
    Validate.isTrue(strength >= 0.0D, "Jump strength cannot be less than zero");
    getHandle().getAttributeInstance(EntityHorse.attributeJumpStrength).setValue(strength);
  }
  
  public boolean isTamed()
  {
    return getHandle().isTame();
  }
  
  public void setTamed(boolean tamed)
  {
    getHandle().setTame(tamed);
  }
  
  public AnimalTamer getOwner()
  {
    if (getOwnerUUID() == null) {
      return null;
    }
    return getServer().getOfflinePlayer(getOwnerUUID());
  }
  
  public void setOwner(AnimalTamer owner)
  {
    if (owner != null)
    {
      setTamed(true);
      getHandle().setGoalTarget(null, null, false);
      setOwnerUUID(owner.getUniqueId());
    }
    else
    {
      setTamed(false);
      setOwnerUUID(null);
    }
  }
  
  public UUID getOwnerUUID()
  {
    try
    {
      return UUID.fromString(getHandle().getOwnerUUID());
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return null;
  }
  
  public void setOwnerUUID(UUID uuid)
  {
    if (uuid == null) {
      getHandle().setOwnerUUID("");
    } else {
      getHandle().setOwnerUUID(uuid.toString());
    }
  }
  
  public HorseInventory getInventory()
  {
    return new CraftInventoryHorse(getHandle().inventoryChest);
  }
  
  public String toString()
  {
    return "CraftHorse{variant=" + getVariant() + ", owner=" + getOwner() + '}';
  }
  
  public EntityType getType()
  {
    return EntityType.HORSE;
  }
}
