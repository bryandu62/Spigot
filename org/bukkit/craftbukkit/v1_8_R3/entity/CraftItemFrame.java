package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang.Validate;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame
  extends CraftHanging
  implements ItemFrame
{
  public CraftItemFrame(CraftServer server, EntityItemFrame entity)
  {
    super(server, entity);
  }
  
  public boolean setFacingDirection(BlockFace face, boolean force)
  {
    if (!super.setFacingDirection(face, force)) {
      return false;
    }
    update();
    
    return true;
  }
  
  private void update()
  {
    EntityItemFrame old = getHandle();
    
    WorldServer world = ((CraftWorld)getWorld()).getHandle();
    BlockPosition position = old.getBlockPosition();
    EnumDirection direction = old.getDirection();
    net.minecraft.server.v1_8_R3.ItemStack item = old.getItem() != null ? old.getItem().cloneItemStack() : null;
    
    old.die();
    
    EntityItemFrame frame = new EntityItemFrame(world, position, direction);
    frame.setItem(item);
    world.addEntity(frame);
    this.entity = frame;
  }
  
  public void setItem(org.bukkit.inventory.ItemStack item)
  {
    if ((item == null) || (item.getTypeId() == 0)) {
      getHandle().setItem(null);
    } else {
      getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }
  }
  
  public org.bukkit.inventory.ItemStack getItem()
  {
    return CraftItemStack.asBukkitCopy(getHandle().getItem());
  }
  
  public Rotation getRotation()
  {
    return toBukkitRotation(getHandle().getRotation());
  }
  
  Rotation toBukkitRotation(int value)
  {
    switch (value)
    {
    case 0: 
      return Rotation.NONE;
    case 1: 
      return Rotation.CLOCKWISE_45;
    case 2: 
      return Rotation.CLOCKWISE;
    case 3: 
      return Rotation.CLOCKWISE_135;
    case 4: 
      return Rotation.FLIPPED;
    case 5: 
      return Rotation.FLIPPED_45;
    case 6: 
      return Rotation.COUNTER_CLOCKWISE;
    case 7: 
      return Rotation.COUNTER_CLOCKWISE_45;
    }
    throw new AssertionError("Unknown rotation " + value + " for " + getHandle());
  }
  
  public void setRotation(Rotation rotation)
  {
    Validate.notNull(rotation, "Rotation cannot be null");
    getHandle().setRotation(toInteger(rotation));
  }
  
  static int toInteger(Rotation rotation)
  {
    switch (rotation)
    {
    case CLOCKWISE: 
      return 0;
    case CLOCKWISE_135: 
      return 1;
    case CLOCKWISE_45: 
      return 2;
    case COUNTER_CLOCKWISE: 
      return 3;
    case COUNTER_CLOCKWISE_45: 
      return 4;
    case FLIPPED: 
      return 5;
    case FLIPPED_45: 
      return 6;
    case NONE: 
      return 7;
    }
    throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
  }
  
  public EntityItemFrame getHandle()
  {
    return (EntityItemFrame)this.entity;
  }
  
  public String toString()
  {
    return "CraftItemFrame{item=" + getItem() + ", rotation=" + getRotation() + "}";
  }
  
  public EntityType getType()
  {
    return EntityType.ITEM_FRAME;
  }
}
