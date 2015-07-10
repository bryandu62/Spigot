package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.PluginManager;

public class EntityEnderCrystal
  extends Entity
{
  public int a;
  public int b;
  
  public EntityEnderCrystal(World world)
  {
    super(world);
    this.k = true;
    setSize(2.0F, 2.0F);
    this.b = 5;
    this.a = this.random.nextInt(100000);
  }
  
  protected boolean s_()
  {
    return false;
  }
  
  protected void h()
  {
    this.datawatcher.a(8, Integer.valueOf(this.b));
  }
  
  public void t_()
  {
    this.lastX = this.locX;
    this.lastY = this.locY;
    this.lastZ = this.locZ;
    this.a += 1;
    this.datawatcher.watch(8, Integer.valueOf(this.b));
    int i = MathHelper.floor(this.locX);
    int j = MathHelper.floor(this.locY);
    int k = MathHelper.floor(this.locZ);
    if (((this.world.worldProvider instanceof WorldProviderTheEnd)) && (this.world.getType(new BlockPosition(i, j, k)).getBlock() != Blocks.FIRE)) {
      if (!CraftEventFactory.callBlockIgniteEvent(this.world, i, j, k, this).isCancelled()) {
        this.world.setTypeUpdate(new BlockPosition(i, j, k), Blocks.FIRE.getBlockData());
      }
    }
  }
  
  protected void b(NBTTagCompound nbttagcompound) {}
  
  protected void a(NBTTagCompound nbttagcompound) {}
  
  public boolean ad()
  {
    return true;
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    if ((!this.dead) && (!this.world.isClientSide))
    {
      if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
        return false;
      }
      this.b = 0;
      if (this.b <= 0)
      {
        die();
        if (!this.world.isClientSide)
        {
          ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 6.0F, false);
          this.world.getServer().getPluginManager().callEvent(event);
          if (event.isCancelled())
          {
            this.dead = false;
            return false;
          }
          this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), true);
        }
      }
    }
    return true;
  }
}
