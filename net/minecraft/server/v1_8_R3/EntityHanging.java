package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Painting;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.SpigotWorldConfig;

public abstract class EntityHanging
  extends Entity
{
  private int c;
  public BlockPosition blockPosition;
  public EnumDirection direction;
  
  public EntityHanging(World world)
  {
    super(world);
    setSize(0.5F, 0.5F);
  }
  
  public EntityHanging(World world, BlockPosition blockposition)
  {
    this(world);
    this.blockPosition = blockposition;
  }
  
  protected void h() {}
  
  public void setDirection(EnumDirection enumdirection)
  {
    Validate.notNull(enumdirection);
    Validate.isTrue(enumdirection.k().c());
    this.direction = enumdirection;
    this.lastYaw = (this.yaw = this.direction.b() * 90);
    updateBoundingBox();
  }
  
  public static AxisAlignedBB calculateBoundingBox(BlockPosition blockPosition, EnumDirection direction, int width, int height)
  {
    double d0 = blockPosition.getX() + 0.5D;
    double d1 = blockPosition.getY() + 0.5D;
    double d2 = blockPosition.getZ() + 0.5D;
    
    double d4 = width % 32 == 0 ? 0.5D : 0.0D;
    double d5 = height % 32 == 0 ? 0.5D : 0.0D;
    
    d0 -= direction.getAdjacentX() * 0.46875D;
    d2 -= direction.getAdjacentZ() * 0.46875D;
    d1 += d5;
    EnumDirection enumdirection = direction.f();
    
    d0 += d4 * enumdirection.getAdjacentX();
    d2 += d4 * enumdirection.getAdjacentZ();
    double d6 = width;
    double d7 = height;
    double d8 = width;
    if (direction.k() == EnumDirection.EnumAxis.Z) {
      d8 = 1.0D;
    } else {
      d6 = 1.0D;
    }
    d6 /= 32.0D;
    d7 /= 32.0D;
    d8 /= 32.0D;
    return new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8);
  }
  
  private void updateBoundingBox()
  {
    if (this.direction != null)
    {
      AxisAlignedBB bb = calculateBoundingBox(this.blockPosition, this.direction, l(), m());
      this.locX = ((bb.a + bb.d) / 2.0D);
      this.locY = ((bb.b + bb.e) / 2.0D);
      this.locZ = ((bb.c + bb.f) / 2.0D);
      a(bb);
    }
  }
  
  private double a(int i)
  {
    return i % 32 == 0 ? 0.5D : 0.0D;
  }
  
  public void t_()
  {
    this.lastX = this.locX;
    this.lastY = this.locY;
    this.lastZ = this.locZ;
    if ((this.c++ == this.world.spigotConfig.hangingTickFrequency) && (!this.world.isClientSide))
    {
      this.c = 0;
      if ((!this.dead) && (!survives()))
      {
        Material material = this.world.getType(new BlockPosition(this)).getBlock().getMaterial();
        HangingBreakEvent.RemoveCause cause;
        HangingBreakEvent.RemoveCause cause;
        if (!material.equals(Material.AIR)) {
          cause = HangingBreakEvent.RemoveCause.OBSTRUCTION;
        } else {
          cause = HangingBreakEvent.RemoveCause.PHYSICS;
        }
        HangingBreakEvent event = new HangingBreakEvent((Hanging)getBukkitEntity(), cause);
        this.world.getServer().getPluginManager().callEvent(event);
        
        PaintingBreakEvent paintingEvent = null;
        if ((this instanceof EntityPainting))
        {
          paintingEvent = new PaintingBreakEvent((Painting)getBukkitEntity(), PaintingBreakEvent.RemoveCause.valueOf(cause.name()));
          paintingEvent.setCancelled(event.isCancelled());
          this.world.getServer().getPluginManager().callEvent(paintingEvent);
        }
        if ((this.dead) || (event.isCancelled()) || ((paintingEvent != null) && (paintingEvent.isCancelled()))) {
          return;
        }
        die();
        b(null);
      }
    }
  }
  
  public boolean survives()
  {
    if (!this.world.getCubes(this, getBoundingBox()).isEmpty()) {
      return false;
    }
    int i = Math.max(1, l() / 16);
    int j = Math.max(1, m() / 16);
    BlockPosition blockposition = this.blockPosition.shift(this.direction.opposite());
    EnumDirection enumdirection = this.direction.f();
    for (int k = 0; k < i; k++) {
      for (int l = 0; l < j; l++)
      {
        BlockPosition blockposition1 = blockposition.shift(enumdirection, k).up(l);
        Block block = this.world.getType(blockposition1).getBlock();
        if ((!block.getMaterial().isBuildable()) && (!BlockDiodeAbstract.d(block))) {
          return false;
        }
      }
    }
    List list = this.world.getEntities(this, getBoundingBox());
    Iterator iterator = list.iterator();
    Entity entity;
    do
    {
      if (!iterator.hasNext()) {
        return true;
      }
      entity = (Entity)iterator.next();
    } while (!(entity instanceof EntityHanging));
    return false;
  }
  
  public boolean ad()
  {
    return true;
  }
  
  public boolean l(Entity entity)
  {
    return (entity instanceof EntityHuman) ? damageEntity(DamageSource.playerAttack((EntityHuman)entity), 0.0F) : false;
  }
  
  public EnumDirection getDirection()
  {
    return this.direction;
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    if ((!this.dead) && (!this.world.isClientSide))
    {
      HangingBreakEvent event = new HangingBreakEvent((Hanging)getBukkitEntity(), HangingBreakEvent.RemoveCause.DEFAULT);
      PaintingBreakEvent paintingEvent = null;
      if (damagesource.getEntity() != null)
      {
        event = new HangingBreakByEntityEvent((Hanging)getBukkitEntity(), damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity());
        if ((this instanceof EntityPainting)) {
          paintingEvent = new PaintingBreakByEntityEvent((Painting)getBukkitEntity(), damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity());
        }
      }
      else if (damagesource.isExplosion())
      {
        event = new HangingBreakEvent((Hanging)getBukkitEntity(), HangingBreakEvent.RemoveCause.EXPLOSION);
      }
      this.world.getServer().getPluginManager().callEvent(event);
      if (paintingEvent != null)
      {
        paintingEvent.setCancelled(event.isCancelled());
        this.world.getServer().getPluginManager().callEvent(paintingEvent);
      }
      if ((this.dead) || (event.isCancelled()) || ((paintingEvent != null) && (paintingEvent.isCancelled()))) {
        return true;
      }
      die();
      ac();
      b(damagesource.getEntity());
    }
    return true;
  }
  
  public void move(double d0, double d1, double d2)
  {
    if ((!this.world.isClientSide) && (!this.dead) && (d0 * d0 + d1 * d1 + d2 * d2 > 0.0D))
    {
      if (this.dead) {
        return;
      }
      HangingBreakEvent event = new HangingBreakEvent((Hanging)getBukkitEntity(), HangingBreakEvent.RemoveCause.PHYSICS);
      this.world.getServer().getPluginManager().callEvent(event);
      if ((this.dead) || (event.isCancelled())) {
        return;
      }
      die();
      b(null);
    }
  }
  
  public void g(double d0, double d1, double d2) {}
  
  public void b(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setByte("Facing", (byte)this.direction.b());
    nbttagcompound.setInt("TileX", getBlockPosition().getX());
    nbttagcompound.setInt("TileY", getBlockPosition().getY());
    nbttagcompound.setInt("TileZ", getBlockPosition().getZ());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    this.blockPosition = new BlockPosition(nbttagcompound.getInt("TileX"), nbttagcompound.getInt("TileY"), nbttagcompound.getInt("TileZ"));
    EnumDirection enumdirection;
    if (nbttagcompound.hasKeyOfType("Direction", 99))
    {
      EnumDirection enumdirection = EnumDirection.fromType2(nbttagcompound.getByte("Direction"));
      this.blockPosition = this.blockPosition.shift(enumdirection);
    }
    else
    {
      EnumDirection enumdirection;
      if (nbttagcompound.hasKeyOfType("Facing", 99)) {
        enumdirection = EnumDirection.fromType2(nbttagcompound.getByte("Facing"));
      } else {
        enumdirection = EnumDirection.fromType2(nbttagcompound.getByte("Dir"));
      }
    }
    setDirection(enumdirection);
  }
  
  public abstract int l();
  
  public abstract int m();
  
  public abstract void b(Entity paramEntity);
  
  protected boolean af()
  {
    return false;
  }
  
  public void setPosition(double d0, double d1, double d2)
  {
    this.locX = d0;
    this.locY = d1;
    this.locZ = d2;
    BlockPosition blockposition = this.blockPosition;
    
    this.blockPosition = new BlockPosition(d0, d1, d2);
    if (!this.blockPosition.equals(blockposition))
    {
      updateBoundingBox();
      this.ai = true;
    }
  }
  
  public BlockPosition getBlockPosition()
  {
    return this.blockPosition;
  }
}
