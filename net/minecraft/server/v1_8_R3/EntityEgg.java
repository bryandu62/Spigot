package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.plugin.PluginManager;

public class EntityEgg
  extends EntityProjectile
{
  public EntityEgg(World world)
  {
    super(world);
  }
  
  public EntityEgg(World world, EntityLiving entityliving)
  {
    super(world, entityliving);
  }
  
  public EntityEgg(World world, double d0, double d1, double d2)
  {
    super(world, d0, d1, d2);
  }
  
  protected void a(MovingObjectPosition movingobjectposition)
  {
    if (movingobjectposition.entity != null) {
      movingobjectposition.entity.damageEntity(DamageSource.projectile(this, getShooter()), 0.0F);
    }
    boolean hatching = (!this.world.isClientSide) && (this.random.nextInt(8) == 0);
    int numHatching = this.random.nextInt(32) == 0 ? 4 : 1;
    if (!hatching) {
      numHatching = 0;
    }
    EntityType hatchingType = EntityType.CHICKEN;
    
    Entity shooter = getShooter();
    if ((shooter instanceof EntityPlayer))
    {
      Player player = shooter == null ? null : (Player)shooter.getBukkitEntity();
      
      PlayerEggThrowEvent event = new PlayerEggThrowEvent(player, (Egg)getBukkitEntity(), hatching, (byte)numHatching, hatchingType);
      this.world.getServer().getPluginManager().callEvent(event);
      
      hatching = event.isHatching();
      numHatching = event.getNumHatches();
      hatchingType = event.getHatchingType();
    }
    if (hatching) {
      for (int k = 0; k < numHatching; k++)
      {
        Entity entity = this.world.getWorld().createEntity(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ, this.yaw, 0.0F), hatchingType.getEntityClass());
        if ((entity.getBukkitEntity() instanceof Ageable)) {
          ((Ageable)entity.getBukkitEntity()).setBaby();
        }
        this.world.getWorld().addEntity(entity, CreatureSpawnEvent.SpawnReason.EGG);
      }
    }
    for (int j = 0; j < 8; j++) {
      this.world.addParticle(EnumParticle.ITEM_CRACK, this.locX, this.locY, this.locZ, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, new int[] { Item.getId(Items.EGG) });
    }
    if (!this.world.isClientSide) {
      die();
    }
  }
}
