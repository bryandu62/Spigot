package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Fish;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class CraftLivingEntity
  extends CraftEntity
  implements LivingEntity
{
  private CraftEntityEquipment equipment;
  
  public CraftLivingEntity(CraftServer server, EntityLiving entity)
  {
    super(server, entity);
    if (((entity instanceof EntityInsentient)) || ((entity instanceof EntityArmorStand))) {
      this.equipment = new CraftEntityEquipment(this);
    }
  }
  
  public double getHealth()
  {
    return Math.min(Math.max(0.0F, getHandle().getHealth()), getMaxHealth());
  }
  
  public void setHealth(double health)
  {
    if ((health < 0.0D) || (health > getMaxHealth())) {
      throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth());
    }
    if (((this.entity instanceof EntityPlayer)) && (health == 0.0D)) {
      ((EntityPlayer)this.entity).die(DamageSource.GENERIC);
    }
    getHandle().setHealth((float)health);
  }
  
  public double getMaxHealth()
  {
    return getHandle().getMaxHealth();
  }
  
  public void setMaxHealth(double amount)
  {
    Validate.isTrue(amount > 0.0D, "Max health must be greater than 0");
    
    getHandle().getAttributeInstance(GenericAttributes.maxHealth).setValue(amount);
    if (getHealth() > amount) {
      setHealth(amount);
    }
  }
  
  public void resetMaxHealth()
  {
    setMaxHealth(getHandle().getMaxHealth());
  }
  
  @Deprecated
  public Egg throwEgg()
  {
    return (Egg)launchProjectile(Egg.class);
  }
  
  @Deprecated
  public Snowball throwSnowball()
  {
    return (Snowball)launchProjectile(Snowball.class);
  }
  
  public double getEyeHeight()
  {
    return getHandle().getHeadHeight();
  }
  
  public double getEyeHeight(boolean ignoreSneaking)
  {
    return getEyeHeight();
  }
  
  private List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance, int maxLength)
  {
    if (maxDistance > 120) {
      maxDistance = 120;
    }
    ArrayList<Block> blocks = new ArrayList();
    Iterator<Block> itr = new BlockIterator(this, maxDistance);
    while (itr.hasNext())
    {
      Block block = (Block)itr.next();
      blocks.add(block);
      if ((maxLength != 0) && (blocks.size() > maxLength)) {
        blocks.remove(0);
      }
      int id = block.getTypeId();
      if (transparent == null ? 
        id != 0 : 
        
        !transparent.contains(Byte.valueOf((byte)id))) {
        break;
      }
    }
    return blocks;
  }
  
  private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength)
  {
    if (maxDistance > 120) {
      maxDistance = 120;
    }
    ArrayList<Block> blocks = new ArrayList();
    Iterator<Block> itr = new BlockIterator(this, maxDistance);
    while (itr.hasNext())
    {
      Block block = (Block)itr.next();
      blocks.add(block);
      if ((maxLength != 0) && (blocks.size() > maxLength)) {
        blocks.remove(0);
      }
      Material material = block.getType();
      if (transparent == null ? 
        !material.equals(Material.AIR) : 
        
        !transparent.contains(material)) {
        break;
      }
    }
    return blocks;
  }
  
  public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance)
  {
    return getLineOfSight(transparent, maxDistance, 0);
  }
  
  public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance)
  {
    return getLineOfSight(transparent, maxDistance, 0);
  }
  
  public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance)
  {
    List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
    return (Block)blocks.get(0);
  }
  
  public Block getTargetBlock(Set<Material> transparent, int maxDistance)
  {
    List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
    return (Block)blocks.get(0);
  }
  
  public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance)
  {
    return getLineOfSight(transparent, maxDistance, 2);
  }
  
  public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance)
  {
    return getLineOfSight(transparent, maxDistance, 2);
  }
  
  @Deprecated
  public Arrow shootArrow()
  {
    return (Arrow)launchProjectile(Arrow.class);
  }
  
  public int getRemainingAir()
  {
    return getHandle().getAirTicks();
  }
  
  public void setRemainingAir(int ticks)
  {
    getHandle().setAirTicks(ticks);
  }
  
  public int getMaximumAir()
  {
    return getHandle().maxAirTicks;
  }
  
  public void setMaximumAir(int ticks)
  {
    getHandle().maxAirTicks = ticks;
  }
  
  public void damage(double amount)
  {
    damage(amount, null);
  }
  
  public void damage(double amount, org.bukkit.entity.Entity source)
  {
    DamageSource reason = DamageSource.GENERIC;
    if ((source instanceof HumanEntity)) {
      reason = DamageSource.playerAttack(((CraftHumanEntity)source).getHandle());
    } else if ((source instanceof LivingEntity)) {
      reason = DamageSource.mobAttack(((CraftLivingEntity)source).getHandle());
    }
    this.entity.damageEntity(reason, (float)amount);
  }
  
  public Location getEyeLocation()
  {
    Location loc = getLocation();
    loc.setY(loc.getY() + getEyeHeight());
    return loc;
  }
  
  public int getMaximumNoDamageTicks()
  {
    return getHandle().maxNoDamageTicks;
  }
  
  public void setMaximumNoDamageTicks(int ticks)
  {
    getHandle().maxNoDamageTicks = ticks;
  }
  
  public double getLastDamage()
  {
    return getHandle().lastDamage;
  }
  
  public void setLastDamage(double damage)
  {
    getHandle().lastDamage = ((float)damage);
  }
  
  public int getNoDamageTicks()
  {
    return getHandle().noDamageTicks;
  }
  
  public void setNoDamageTicks(int ticks)
  {
    getHandle().noDamageTicks = ticks;
  }
  
  public EntityLiving getHandle()
  {
    return (EntityLiving)this.entity;
  }
  
  public void setHandle(EntityLiving entity)
  {
    super.setHandle(entity);
  }
  
  public String toString()
  {
    return "CraftLivingEntity{id=" + getEntityId() + '}';
  }
  
  public Player getKiller()
  {
    return getHandle().killer == null ? null : (Player)getHandle().killer.getBukkitEntity();
  }
  
  public boolean addPotionEffect(PotionEffect effect)
  {
    return addPotionEffect(effect, false);
  }
  
  public boolean addPotionEffect(PotionEffect effect, boolean force)
  {
    if (hasPotionEffect(effect.getType()))
    {
      if (!force) {
        return false;
      }
      removePotionEffect(effect.getType());
    }
    getHandle().addEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
    return true;
  }
  
  public boolean addPotionEffects(Collection<PotionEffect> effects)
  {
    boolean success = true;
    for (PotionEffect effect : effects) {
      success &= addPotionEffect(effect);
    }
    return success;
  }
  
  public boolean hasPotionEffect(PotionEffectType type)
  {
    return getHandle().hasEffect(net.minecraft.server.v1_8_R3.MobEffectList.byId[type.getId()]);
  }
  
  public void removePotionEffect(PotionEffectType type)
  {
    getHandle().removeEffect(type.getId());
  }
  
  public Collection<PotionEffect> getActivePotionEffects()
  {
    List<PotionEffect> effects = new ArrayList();
    for (Object raw : getHandle().effects.values()) {
      if ((raw instanceof MobEffect))
      {
        MobEffect handle = (MobEffect)raw;
        effects.add(new PotionEffect(PotionEffectType.getById(handle.getEffectId()), handle.getDuration(), handle.getAmplifier(), handle.isAmbient(), handle.isShowParticles()));
      }
    }
    return effects;
  }
  
  public <T extends Projectile> T launchProjectile(Class<? extends T> projectile)
  {
    return launchProjectile(projectile, null);
  }
  
  public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity)
  {
    World world = ((CraftWorld)getWorld()).getHandle();
    net.minecraft.server.v1_8_R3.Entity launch = null;
    if (Snowball.class.isAssignableFrom(projectile))
    {
      launch = new EntitySnowball(world, getHandle());
    }
    else if (Egg.class.isAssignableFrom(projectile))
    {
      launch = new EntityEgg(world, getHandle());
    }
    else if (EnderPearl.class.isAssignableFrom(projectile))
    {
      launch = new EntityEnderPearl(world, getHandle());
    }
    else if (Arrow.class.isAssignableFrom(projectile))
    {
      launch = new EntityArrow(world, getHandle(), 1.0F);
    }
    else if (ThrownPotion.class.isAssignableFrom(projectile))
    {
      launch = new EntityPotion(world, getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
    }
    else if (ThrownExpBottle.class.isAssignableFrom(projectile))
    {
      launch = new EntityThrownExpBottle(world, getHandle());
    }
    else if ((Fish.class.isAssignableFrom(projectile)) && ((getHandle() instanceof EntityHuman)))
    {
      launch = new EntityFishingHook(world, (EntityHuman)getHandle());
    }
    else if (Fireball.class.isAssignableFrom(projectile))
    {
      Location location = getEyeLocation();
      Vector direction = location.getDirection().multiply(10);
      if (SmallFireball.class.isAssignableFrom(projectile)) {
        launch = new EntitySmallFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
      } else if (WitherSkull.class.isAssignableFrom(projectile)) {
        launch = new EntityWitherSkull(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
      } else {
        launch = new EntityLargeFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
      }
      ((EntityFireball)launch).projectileSource = this;
      launch.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    Validate.notNull(launch, "Projectile not supported");
    if (velocity != null) {
      ((Projectile)launch.getBukkitEntity()).setVelocity(velocity);
    }
    world.addEntity(launch);
    return (Projectile)launch.getBukkitEntity();
  }
  
  public EntityType getType()
  {
    return EntityType.UNKNOWN;
  }
  
  public boolean hasLineOfSight(org.bukkit.entity.Entity other)
  {
    return getHandle().hasLineOfSight(((CraftEntity)other).getHandle());
  }
  
  public boolean getRemoveWhenFarAway()
  {
    return ((getHandle() instanceof EntityInsentient)) && (!((EntityInsentient)getHandle()).persistent);
  }
  
  public void setRemoveWhenFarAway(boolean remove)
  {
    if ((getHandle() instanceof EntityInsentient)) {
      ((EntityInsentient)getHandle()).persistent = (!remove);
    }
  }
  
  public EntityEquipment getEquipment()
  {
    return this.equipment;
  }
  
  public void setCanPickupItems(boolean pickup)
  {
    if ((getHandle() instanceof EntityInsentient)) {
      ((EntityInsentient)getHandle()).canPickUpLoot = pickup;
    }
  }
  
  public boolean getCanPickupItems()
  {
    return ((getHandle() instanceof EntityInsentient)) && (((EntityInsentient)getHandle()).canPickUpLoot);
  }
  
  public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause)
  {
    if (getHealth() == 0.0D) {
      return false;
    }
    return super.teleport(location, cause);
  }
  
  public boolean isLeashed()
  {
    if (!(getHandle() instanceof EntityInsentient)) {
      return false;
    }
    return ((EntityInsentient)getHandle()).getLeashHolder() != null;
  }
  
  public org.bukkit.entity.Entity getLeashHolder()
    throws IllegalStateException
  {
    if (!isLeashed()) {
      throw new IllegalStateException("Entity not leashed");
    }
    return ((EntityInsentient)getHandle()).getLeashHolder().getBukkitEntity();
  }
  
  private boolean unleash()
  {
    if (!isLeashed()) {
      return false;
    }
    ((EntityInsentient)getHandle()).unleash(true, false);
    return true;
  }
  
  public boolean setLeashHolder(org.bukkit.entity.Entity holder)
  {
    if (((getHandle() instanceof EntityWither)) || (!(getHandle() instanceof EntityInsentient))) {
      return false;
    }
    if (holder == null) {
      return unleash();
    }
    if (holder.isDead()) {
      return false;
    }
    unleash();
    ((EntityInsentient)getHandle()).setLeashHolder(((CraftEntity)holder).getHandle(), true);
    return true;
  }
}
