package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment
  implements EntityEquipment
{
  private static final int WEAPON_SLOT = 0;
  private static final int HELMET_SLOT = 4;
  private static final int CHEST_SLOT = 3;
  private static final int LEG_SLOT = 2;
  private static final int BOOT_SLOT = 1;
  private static final int INVENTORY_SLOTS = 5;
  private final CraftLivingEntity entity;
  
  public CraftEntityEquipment(CraftLivingEntity entity)
  {
    this.entity = entity;
  }
  
  public ItemStack getItemInHand()
  {
    return getEquipment(0);
  }
  
  public void setItemInHand(ItemStack stack)
  {
    setEquipment(0, stack);
  }
  
  public ItemStack getHelmet()
  {
    return getEquipment(4);
  }
  
  public void setHelmet(ItemStack helmet)
  {
    setEquipment(4, helmet);
  }
  
  public ItemStack getChestplate()
  {
    return getEquipment(3);
  }
  
  public void setChestplate(ItemStack chestplate)
  {
    setEquipment(3, chestplate);
  }
  
  public ItemStack getLeggings()
  {
    return getEquipment(2);
  }
  
  public void setLeggings(ItemStack leggings)
  {
    setEquipment(2, leggings);
  }
  
  public ItemStack getBoots()
  {
    return getEquipment(1);
  }
  
  public void setBoots(ItemStack boots)
  {
    setEquipment(1, boots);
  }
  
  public ItemStack[] getArmorContents()
  {
    ItemStack[] armor = new ItemStack[4];
    for (int slot = 1; slot < 5; slot++) {
      armor[(slot - 1)] = getEquipment(slot);
    }
    return armor;
  }
  
  public void setArmorContents(ItemStack[] items)
  {
    for (int slot = 1; slot < 5; slot++)
    {
      ItemStack equipment = (items != null) && (slot <= items.length) ? items[(slot - 1)] : null;
      setEquipment(slot, equipment);
    }
  }
  
  private ItemStack getEquipment(int slot)
  {
    return CraftItemStack.asBukkitCopy(this.entity.getHandle().getEquipment(slot));
  }
  
  private void setEquipment(int slot, ItemStack stack)
  {
    this.entity.getHandle().setEquipment(slot, CraftItemStack.asNMSCopy(stack));
  }
  
  public void clear()
  {
    for (int i = 0; i < 5; i++) {
      setEquipment(i, null);
    }
  }
  
  public Entity getHolder()
  {
    return this.entity;
  }
  
  public float getItemInHandDropChance()
  {
    return getDropChance(0);
  }
  
  public void setItemInHandDropChance(float chance)
  {
    setDropChance(0, chance);
  }
  
  public float getHelmetDropChance()
  {
    return getDropChance(4);
  }
  
  public void setHelmetDropChance(float chance)
  {
    setDropChance(4, chance);
  }
  
  public float getChestplateDropChance()
  {
    return getDropChance(3);
  }
  
  public void setChestplateDropChance(float chance)
  {
    setDropChance(3, chance);
  }
  
  public float getLeggingsDropChance()
  {
    return getDropChance(2);
  }
  
  public void setLeggingsDropChance(float chance)
  {
    setDropChance(2, chance);
  }
  
  public float getBootsDropChance()
  {
    return getDropChance(1);
  }
  
  public void setBootsDropChance(float chance)
  {
    setDropChance(1, chance);
  }
  
  private void setDropChance(int slot, float chance)
  {
    ((net.minecraft.server.v1_8_R3.EntityInsentient)this.entity.getHandle()).dropChances[slot] = (chance - 0.1F);
  }
  
  private float getDropChance(int slot)
  {
    return ((net.minecraft.server.v1_8_R3.EntityInsentient)this.entity.getHandle()).dropChances[slot] + 0.1F;
  }
}
