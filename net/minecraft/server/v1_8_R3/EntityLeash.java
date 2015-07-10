package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

public class EntityLeash
  extends EntityHanging
{
  public EntityLeash(World world)
  {
    super(world);
  }
  
  public EntityLeash(World world, BlockPosition blockposition)
  {
    super(world, blockposition);
    setPosition(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D);
    
    a(new AxisAlignedBB(this.locX - 0.1875D, this.locY - 0.25D + 0.125D, this.locZ - 0.1875D, this.locX + 0.1875D, this.locY + 0.25D + 0.125D, this.locZ + 0.1875D));
  }
  
  protected void h()
  {
    super.h();
  }
  
  public void setDirection(EnumDirection enumdirection) {}
  
  public int l()
  {
    return 9;
  }
  
  public int m()
  {
    return 9;
  }
  
  public float getHeadHeight()
  {
    return -0.0625F;
  }
  
  public void b(Entity entity) {}
  
  public boolean d(NBTTagCompound nbttagcompound)
  {
    return false;
  }
  
  public void b(NBTTagCompound nbttagcompound) {}
  
  public void a(NBTTagCompound nbttagcompound) {}
  
  public boolean e(EntityHuman entityhuman)
  {
    ItemStack itemstack = entityhuman.bA();
    boolean flag = false;
    if ((itemstack != null) && (itemstack.getItem() == Items.LEAD) && (!this.world.isClientSide))
    {
      double d0 = 7.0D;
      List list = this.world.a(EntityInsentient.class, new AxisAlignedBB(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + d0, this.locZ + d0));
      Iterator iterator = list.iterator();
      while (iterator.hasNext())
      {
        EntityInsentient entityinsentient = (EntityInsentient)iterator.next();
        if ((entityinsentient.cc()) && (entityinsentient.getLeashHolder() == entityhuman)) {
          if (CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, this, entityhuman).isCancelled())
          {
            ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutAttachEntity(1, entityinsentient, entityinsentient.getLeashHolder()));
          }
          else
          {
            entityinsentient.setLeashHolder(this, true);
            flag = true;
          }
        }
      }
    }
    if ((!this.world.isClientSide) && (!flag))
    {
      boolean die = true;
      
      double d0 = 7.0D;
      List list = this.world.a(EntityInsentient.class, new AxisAlignedBB(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + d0, this.locZ + d0));
      Iterator iterator = list.iterator();
      while (iterator.hasNext())
      {
        EntityInsentient entityinsentient = (EntityInsentient)iterator.next();
        if ((entityinsentient.cc()) && (entityinsentient.getLeashHolder() == this)) {
          if (CraftEventFactory.callPlayerUnleashEntityEvent(entityinsentient, entityhuman).isCancelled()) {
            die = false;
          } else {
            entityinsentient.unleash(true, !entityhuman.abilities.canInstantlyBuild);
          }
        }
      }
      if (die) {
        die();
      }
    }
    return true;
  }
  
  public boolean survives()
  {
    return this.world.getType(this.blockPosition).getBlock() instanceof BlockFence;
  }
  
  public static EntityLeash a(World world, BlockPosition blockposition)
  {
    EntityLeash entityleash = new EntityLeash(world, blockposition);
    
    entityleash.attachedToPlayer = true;
    world.addEntity(entityleash);
    return entityleash;
  }
  
  public static EntityLeash b(World world, BlockPosition blockposition)
  {
    int i = blockposition.getX();
    int j = blockposition.getY();
    int k = blockposition.getZ();
    List list = world.a(EntityLeash.class, new AxisAlignedBB(i - 1.0D, j - 1.0D, k - 1.0D, i + 1.0D, j + 1.0D, k + 1.0D));
    Iterator iterator = list.iterator();
    EntityLeash entityleash;
    do
    {
      if (!iterator.hasNext()) {
        return null;
      }
      entityleash = (EntityLeash)iterator.next();
    } while (!entityleash.getBlockPosition().equals(blockposition));
    return entityleash;
  }
}
