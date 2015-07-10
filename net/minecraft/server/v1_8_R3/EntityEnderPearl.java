package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.PluginManager;

public class EntityEnderPearl
  extends EntityProjectile
{
  private EntityLiving c;
  
  public EntityEnderPearl(World world)
  {
    super(world);
  }
  
  public EntityEnderPearl(World world, EntityLiving entityliving)
  {
    super(world, entityliving);
    this.c = entityliving;
  }
  
  protected void a(MovingObjectPosition movingobjectposition)
  {
    EntityLiving entityliving = getShooter();
    if (movingobjectposition.entity != null)
    {
      if (movingobjectposition.entity == this.c) {
        return;
      }
      movingobjectposition.entity.damageEntity(DamageSource.projectile(this, entityliving), 0.0F);
    }
    for (int i = 0; i < 32; i++) {
      this.world.addParticle(EnumParticle.PORTAL, this.locX, this.locY + this.random.nextDouble() * 2.0D, this.locZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian(), new int[0]);
    }
    if (!this.world.isClientSide)
    {
      if ((entityliving instanceof EntityPlayer))
      {
        EntityPlayer entityplayer = (EntityPlayer)entityliving;
        if ((entityplayer.playerConnection.a().g()) && (entityplayer.world == this.world) && (!entityplayer.isSleeping()))
        {
          CraftPlayer player = entityplayer.getBukkitEntity();
          Location location = getBukkitEntity().getLocation();
          location.setPitch(player.getLocation().getPitch());
          location.setYaw(player.getLocation().getYaw());
          
          PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
          Bukkit.getPluginManager().callEvent(teleEvent);
          if ((!teleEvent.isCancelled()) && (!entityplayer.playerConnection.isDisconnected()))
          {
            if ((this.random.nextFloat() < 0.05F) && (this.world.getGameRules().getBoolean("doMobSpawning")))
            {
              EntityEndermite entityendermite = new EntityEndermite(this.world);
              
              entityendermite.a(true);
              entityendermite.setPositionRotation(entityliving.locX, entityliving.locY, entityliving.locZ, entityliving.yaw, entityliving.pitch);
              this.world.addEntity(entityendermite);
            }
            if (entityliving.au()) {
              entityliving.mount(null);
            }
            entityplayer.playerConnection.teleport(teleEvent.getTo());
            entityliving.fallDistance = 0.0F;
            org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory.entityDamage = this;
            entityliving.damageEntity(DamageSource.FALL, 5.0F);
            org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory.entityDamage = null;
          }
        }
      }
      else if (entityliving != null)
      {
        entityliving.enderTeleportTo(this.locX, this.locY, this.locZ);
        entityliving.fallDistance = 0.0F;
      }
      die();
    }
  }
  
  public void t_()
  {
    EntityLiving entityliving = getShooter();
    if ((entityliving != null) && ((entityliving instanceof EntityHuman)) && (!entityliving.isAlive())) {
      die();
    } else {
      super.t_();
    }
  }
}
