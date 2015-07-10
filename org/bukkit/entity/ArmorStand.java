package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public abstract interface ArmorStand
  extends LivingEntity
{
  public abstract ItemStack getItemInHand();
  
  public abstract void setItemInHand(ItemStack paramItemStack);
  
  public abstract ItemStack getBoots();
  
  public abstract void setBoots(ItemStack paramItemStack);
  
  public abstract ItemStack getLeggings();
  
  public abstract void setLeggings(ItemStack paramItemStack);
  
  public abstract ItemStack getChestplate();
  
  public abstract void setChestplate(ItemStack paramItemStack);
  
  public abstract ItemStack getHelmet();
  
  public abstract void setHelmet(ItemStack paramItemStack);
  
  public abstract EulerAngle getBodyPose();
  
  public abstract void setBodyPose(EulerAngle paramEulerAngle);
  
  public abstract EulerAngle getLeftArmPose();
  
  public abstract void setLeftArmPose(EulerAngle paramEulerAngle);
  
  public abstract EulerAngle getRightArmPose();
  
  public abstract void setRightArmPose(EulerAngle paramEulerAngle);
  
  public abstract EulerAngle getLeftLegPose();
  
  public abstract void setLeftLegPose(EulerAngle paramEulerAngle);
  
  public abstract EulerAngle getRightLegPose();
  
  public abstract void setRightLegPose(EulerAngle paramEulerAngle);
  
  public abstract EulerAngle getHeadPose();
  
  public abstract void setHeadPose(EulerAngle paramEulerAngle);
  
  public abstract boolean hasBasePlate();
  
  public abstract void setBasePlate(boolean paramBoolean);
  
  public abstract boolean hasGravity();
  
  public abstract void setGravity(boolean paramBoolean);
  
  public abstract boolean isVisible();
  
  public abstract void setVisible(boolean paramBoolean);
  
  public abstract boolean hasArms();
  
  public abstract void setArms(boolean paramBoolean);
  
  public abstract boolean isSmall();
  
  public abstract void setSmall(boolean paramBoolean);
  
  public abstract boolean isMarker();
  
  public abstract void setMarker(boolean paramBoolean);
}
