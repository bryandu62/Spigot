package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.SpigotWorldConfig;

public class EntityTNTPrimed
  extends Entity
{
  public int fuseTicks;
  private EntityLiving source;
  public float yield = 4.0F;
  public boolean isIncendiary = false;
  
  public EntityTNTPrimed(World world)
  {
    super(world);
    this.k = true;
    setSize(0.98F, 0.98F);
  }
  
  public EntityTNTPrimed(World world, double d0, double d1, double d2, EntityLiving entityliving)
  {
    this(world);
    setPosition(d0, d1, d2);
    float f = (float)(Math.random() * 3.1415927410125732D * 2.0D);
    
    this.motX = (-(float)Math.sin(f) * 0.02F);
    this.motY = 0.20000000298023224D;
    this.motZ = (-(float)Math.cos(f) * 0.02F);
    this.fuseTicks = 80;
    this.lastX = d0;
    this.lastY = d1;
    this.lastZ = d2;
    this.source = entityliving;
  }
  
  protected void h() {}
  
  protected boolean s_()
  {
    return false;
  }
  
  public boolean ad()
  {
    return !this.dead;
  }
  
  public void t_()
  {
    if (this.world.spigotConfig.currentPrimedTnt++ > this.world.spigotConfig.maxTntTicksPerTick) {
      return;
    }
    this.lastX = this.locX;
    this.lastY = this.locY;
    this.lastZ = this.locZ;
    this.motY -= 0.03999999910593033D;
    move(this.motX, this.motY, this.motZ);
    this.motX *= 0.9800000190734863D;
    this.motY *= 0.9800000190734863D;
    this.motZ *= 0.9800000190734863D;
    if (this.onGround)
    {
      this.motX *= 0.699999988079071D;
      this.motZ *= 0.699999988079071D;
      this.motY *= -0.5D;
    }
    if (this.fuseTicks-- <= 0)
    {
      if (!this.world.isClientSide) {
        explode();
      }
      die();
    }
    else
    {
      W();
      this.world.addParticle(EnumParticle.SMOKE_NORMAL, this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
    }
  }
  
  private void explode()
  {
    CraftServer server = this.world.getServer();
    
    ExplosionPrimeEvent event = new ExplosionPrimeEvent((Explosive)CraftEntity.getEntity(server, this));
    server.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      this.world.createExplosion(this, this.locX, this.locY + this.length / 2.0F, this.locZ, event.getRadius(), event.getFire(), true);
    }
  }
  
  protected void b(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setByte("Fuse", (byte)this.fuseTicks);
  }
  
  protected void a(NBTTagCompound nbttagcompound)
  {
    this.fuseTicks = nbttagcompound.getByte("Fuse");
  }
  
  public EntityLiving getSource()
  {
    return this.source;
  }
  
  public float getHeadHeight()
  {
    return 0.0F;
  }
}