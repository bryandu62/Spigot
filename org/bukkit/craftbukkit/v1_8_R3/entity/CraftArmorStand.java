package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand
  extends CraftLivingEntity
  implements ArmorStand
{
  public CraftArmorStand(CraftServer server, EntityArmorStand entity)
  {
    super(server, entity);
  }
  
  public String toString()
  {
    return "CraftArmorStand";
  }
  
  public EntityType getType()
  {
    return EntityType.ARMOR_STAND;
  }
  
  public EntityArmorStand getHandle()
  {
    return (EntityArmorStand)super.getHandle();
  }
  
  public ItemStack getItemInHand()
  {
    return getEquipment().getItemInHand();
  }
  
  public void setItemInHand(ItemStack item)
  {
    getEquipment().setItemInHand(item);
  }
  
  public ItemStack getBoots()
  {
    return getEquipment().getBoots();
  }
  
  public void setBoots(ItemStack item)
  {
    getEquipment().setBoots(item);
  }
  
  public ItemStack getLeggings()
  {
    return getEquipment().getLeggings();
  }
  
  public void setLeggings(ItemStack item)
  {
    getEquipment().setLeggings(item);
  }
  
  public ItemStack getChestplate()
  {
    return getEquipment().getChestplate();
  }
  
  public void setChestplate(ItemStack item)
  {
    getEquipment().setChestplate(item);
  }
  
  public ItemStack getHelmet()
  {
    return getEquipment().getHelmet();
  }
  
  public void setHelmet(ItemStack item)
  {
    getEquipment().setHelmet(item);
  }
  
  public EulerAngle getBodyPose()
  {
    return fromNMS(getHandle().bodyPose);
  }
  
  public void setBodyPose(EulerAngle pose)
  {
    getHandle().setBodyPose(toNMS(pose));
  }
  
  public EulerAngle getLeftArmPose()
  {
    return fromNMS(getHandle().leftArmPose);
  }
  
  public void setLeftArmPose(EulerAngle pose)
  {
    getHandle().setLeftArmPose(toNMS(pose));
  }
  
  public EulerAngle getRightArmPose()
  {
    return fromNMS(getHandle().rightArmPose);
  }
  
  public void setRightArmPose(EulerAngle pose)
  {
    getHandle().setRightArmPose(toNMS(pose));
  }
  
  public EulerAngle getLeftLegPose()
  {
    return fromNMS(getHandle().leftLegPose);
  }
  
  public void setLeftLegPose(EulerAngle pose)
  {
    getHandle().setLeftLegPose(toNMS(pose));
  }
  
  public EulerAngle getRightLegPose()
  {
    return fromNMS(getHandle().rightLegPose);
  }
  
  public void setRightLegPose(EulerAngle pose)
  {
    getHandle().setRightLegPose(toNMS(pose));
  }
  
  public EulerAngle getHeadPose()
  {
    return fromNMS(getHandle().headPose);
  }
  
  public void setHeadPose(EulerAngle pose)
  {
    getHandle().setHeadPose(toNMS(pose));
  }
  
  public boolean hasBasePlate()
  {
    return !getHandle().hasBasePlate();
  }
  
  public void setBasePlate(boolean basePlate)
  {
    getHandle().setBasePlate(!basePlate);
  }
  
  public boolean hasGravity()
  {
    return !getHandle().hasGravity();
  }
  
  public void setGravity(boolean gravity)
  {
    getHandle().setGravity(!gravity);
  }
  
  public boolean isVisible()
  {
    return !getHandle().isInvisible();
  }
  
  public void setVisible(boolean visible)
  {
    getHandle().setInvisible(!visible);
  }
  
  public boolean hasArms()
  {
    return getHandle().hasArms();
  }
  
  public void setArms(boolean arms)
  {
    getHandle().setArms(arms);
  }
  
  public boolean isSmall()
  {
    return getHandle().isSmall();
  }
  
  public void setSmall(boolean small)
  {
    getHandle().setSmall(small);
  }
  
  private static EulerAngle fromNMS(Vector3f old)
  {
    return new EulerAngle(
      Math.toRadians(old.getX()), 
      Math.toRadians(old.getY()), 
      Math.toRadians(old.getZ()));
  }
  
  private static Vector3f toNMS(EulerAngle old)
  {
    return new Vector3f(
      (float)Math.toDegrees(old.getX()), 
      (float)Math.toDegrees(old.getY()), 
      (float)Math.toDegrees(old.getZ()));
  }
  
  public boolean isMarker()
  {
    return getHandle().s();
  }
  
  public void setMarker(boolean marker)
  {
    getHandle().n(marker);
  }
}
