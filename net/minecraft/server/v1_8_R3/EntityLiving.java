package net.minecraft.server.v1_8_R3;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings;
import org.bukkit.craftbukkit.v1_8_R3.TrigMath;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotWorldConfig;

public abstract class EntityLiving
  extends Entity
{
  private static final UUID a = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
  private static final AttributeModifier b = new AttributeModifier(a, "Sprinting speed boost", 0.30000001192092896D, 2).a(false);
  private AttributeMapBase c;
  public CombatTracker combatTracker = new CombatTracker(this);
  public final Map<Integer, MobEffect> effects = Maps.newHashMap();
  private final ItemStack[] h = new ItemStack[5];
  public boolean ar;
  public int as;
  public int at;
  public int hurtTicks;
  public int av;
  public float aw;
  public int deathTicks;
  public float ay;
  public float az;
  public float aA;
  public float aB;
  public float aC;
  public int maxNoDamageTicks = 20;
  public float aE;
  public float aF;
  public float aG;
  public float aH;
  public float aI;
  public float aJ;
  public float aK;
  public float aL;
  public float aM = 0.02F;
  public EntityHuman killer;
  protected int lastDamageByPlayerTime;
  protected boolean aP;
  protected int ticksFarFromPlayer;
  protected float aR;
  protected float aS;
  protected float aT;
  protected float aU;
  protected float aV;
  protected int aW;
  public float lastDamage;
  protected boolean aY;
  public float aZ;
  public float ba;
  protected float bb;
  protected int bc;
  protected double bd;
  protected double be;
  protected double bf;
  protected double bg;
  protected double bh;
  public boolean updateEffects = true;
  public EntityLiving lastDamager;
  public int hurtTimestamp;
  private EntityLiving bk;
  private int bl;
  private float bm;
  private int bn;
  private float bo;
  public int expToDrop;
  public int maxAirTicks = 300;
  ArrayList<org.bukkit.inventory.ItemStack> drops = null;
  
  public void inactiveTick()
  {
    super.inactiveTick();
    this.ticksFarFromPlayer += 1;
  }
  
  public void G()
  {
    damageEntity(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
  }
  
  public EntityLiving(World world)
  {
    super(world);
    initAttributes();
    
    this.datawatcher.watch(6, Float.valueOf((float)getAttributeInstance(GenericAttributes.maxHealth).getValue()));
    this.k = true;
    this.aH = ((float)((Math.random() + 1.0D) * 0.009999999776482582D));
    setPosition(this.locX, this.locY, this.locZ);
    this.aG = ((float)Math.random() * 12398.0F);
    this.yaw = ((float)(Math.random() * 3.1415927410125732D * 2.0D));
    this.aK = this.yaw;
    this.S = 0.6F;
  }
  
  protected void h()
  {
    this.datawatcher.a(7, Integer.valueOf(0));
    this.datawatcher.a(8, Byte.valueOf((byte)0));
    this.datawatcher.a(9, Byte.valueOf((byte)0));
    this.datawatcher.a(6, Float.valueOf(1.0F));
  }
  
  protected void initAttributes()
  {
    getAttributeMap().b(GenericAttributes.maxHealth);
    getAttributeMap().b(GenericAttributes.c);
    getAttributeMap().b(GenericAttributes.MOVEMENT_SPEED);
  }
  
  protected void a(double d0, boolean flag, Block block, BlockPosition blockposition)
  {
    if (!V()) {
      W();
    }
    if ((!this.world.isClientSide) && (this.fallDistance > 3.0F) && (flag))
    {
      IBlockData iblockdata = this.world.getType(blockposition);
      Block block1 = iblockdata.getBlock();
      float f = MathHelper.f(this.fallDistance - 3.0F);
      if (block1.getMaterial() != Material.AIR)
      {
        double d1 = Math.min(0.2F + f / 15.0F, 10.0F);
        if (d1 > 2.5D) {
          d1 = 2.5D;
        }
        int i = (int)(150.0D * d1);
        if ((this instanceof EntityPlayer)) {
          ((WorldServer)this.world).sendParticles((EntityPlayer)this, EnumParticle.BLOCK_DUST, false, this.locX, this.locY, this.locZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getCombinedId(iblockdata) });
        } else {
          ((WorldServer)this.world).a(EnumParticle.BLOCK_DUST, this.locX, this.locY, this.locZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getCombinedId(iblockdata) });
        }
      }
    }
    super.a(d0, flag, block, blockposition);
  }
  
  public boolean aY()
  {
    return false;
  }
  
  public void K()
  {
    this.ay = this.az;
    super.K();
    this.world.methodProfiler.a("livingEntityBaseTick");
    boolean flag = this instanceof EntityHuman;
    if (isAlive()) {
      if (inBlock())
      {
        damageEntity(DamageSource.STUCK, 1.0F);
      }
      else if ((flag) && (!this.world.getWorldBorder().a(getBoundingBox())))
      {
        double d0 = this.world.getWorldBorder().a(this) + this.world.getWorldBorder().getDamageBuffer();
        if (d0 < 0.0D) {
          damageEntity(DamageSource.STUCK, Math.max(1, MathHelper.floor(-d0 * this.world.getWorldBorder().getDamageAmount())));
        }
      }
    }
    if ((isFireProof()) || (this.world.isClientSide)) {
      extinguish();
    }
    boolean flag1 = (flag) && (((EntityHuman)this).abilities.isInvulnerable);
    if (isAlive()) {
      if (a(Material.WATER))
      {
        if ((!aY()) && (!hasEffect(MobEffectList.WATER_BREATHING.id)) && (!flag1))
        {
          setAirTicks(j(getAirTicks()));
          if (getAirTicks() == -20)
          {
            setAirTicks(0);
            for (int i = 0; i < 8; i++)
            {
              float f = this.random.nextFloat() - this.random.nextFloat();
              float f1 = this.random.nextFloat() - this.random.nextFloat();
              float f2 = this.random.nextFloat() - this.random.nextFloat();
              
              this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX + f, this.locY + f1, this.locZ + f2, this.motX, this.motY, this.motZ, new int[0]);
            }
            damageEntity(DamageSource.DROWN, 2.0F);
          }
        }
        if ((!this.world.isClientSide) && (au()) && ((this.vehicle instanceof EntityLiving))) {
          mount(null);
        }
      }
      else if (getAirTicks() != 300)
      {
        setAirTicks(this.maxAirTicks);
      }
    }
    if ((isAlive()) && (U())) {
      extinguish();
    }
    this.aE = this.aF;
    if (this.hurtTicks > 0) {
      this.hurtTicks -= 1;
    }
    if ((this.noDamageTicks > 0) && (!(this instanceof EntityPlayer))) {
      this.noDamageTicks -= 1;
    }
    if (getHealth() <= 0.0F) {
      aZ();
    }
    if (this.lastDamageByPlayerTime > 0) {
      this.lastDamageByPlayerTime -= 1;
    } else {
      this.killer = null;
    }
    if ((this.bk != null) && (!this.bk.isAlive())) {
      this.bk = null;
    }
    if (this.lastDamager != null) {
      if (!this.lastDamager.isAlive()) {
        b(null);
      } else if (this.ticksLived - this.hurtTimestamp > 100) {
        b(null);
      }
    }
    bi();
    this.aU = this.aT;
    this.aJ = this.aI;
    this.aL = this.aK;
    this.lastYaw = this.yaw;
    this.lastPitch = this.pitch;
    this.world.methodProfiler.b();
  }
  
  public int getExpReward()
  {
    int exp = getExpValue(this.killer);
    if ((!this.world.isClientSide) && ((this.lastDamageByPlayerTime > 0) || (alwaysGivesExp())) && (ba()) && (this.world.getGameRules().getBoolean("doMobLoot"))) {
      return exp;
    }
    return 0;
  }
  
  public boolean isBaby()
  {
    return false;
  }
  
  protected void aZ()
  {
    this.deathTicks += 1;
    if ((this.deathTicks >= 20) && (!this.dead))
    {
      int i = this.expToDrop;
      while (i > 0)
      {
        int j = EntityExperienceOrb.getOrbValue(i);
        i -= j;
        this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
      }
      this.expToDrop = 0;
      
      die();
      for (i = 0; i < 20; i++)
      {
        double d0 = this.random.nextGaussian() * 0.02D;
        double d1 = this.random.nextGaussian() * 0.02D;
        double d2 = this.random.nextGaussian() * 0.02D;
        
        this.world.addParticle(EnumParticle.EXPLOSION_NORMAL, this.locX + this.random.nextFloat() * this.width * 2.0F - this.width, this.locY + this.random.nextFloat() * this.length, this.locZ + this.random.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
      }
    }
  }
  
  protected boolean ba()
  {
    return !isBaby();
  }
  
  protected int j(int i)
  {
    int j = EnchantmentManager.getOxygenEnchantmentLevel(this);
    
    return (j > 0) && (this.random.nextInt(j + 1) > 0) ? i : i - 1;
  }
  
  protected int getExpValue(EntityHuman entityhuman)
  {
    return 0;
  }
  
  protected boolean alwaysGivesExp()
  {
    return false;
  }
  
  public Random bc()
  {
    return this.random;
  }
  
  public EntityLiving getLastDamager()
  {
    return this.lastDamager;
  }
  
  public int be()
  {
    return this.hurtTimestamp;
  }
  
  public void b(EntityLiving entityliving)
  {
    this.lastDamager = entityliving;
    this.hurtTimestamp = this.ticksLived;
  }
  
  public EntityLiving bf()
  {
    return this.bk;
  }
  
  public int bg()
  {
    return this.bl;
  }
  
  public void p(Entity entity)
  {
    if ((entity instanceof EntityLiving)) {
      this.bk = ((EntityLiving)entity);
    } else {
      this.bk = null;
    }
    this.bl = this.ticksLived;
  }
  
  public int bh()
  {
    return this.ticksFarFromPlayer;
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setFloat("HealF", getHealth());
    nbttagcompound.setShort("Health", (short)(int)Math.ceil(getHealth()));
    nbttagcompound.setShort("HurtTime", (short)this.hurtTicks);
    nbttagcompound.setInt("HurtByTimestamp", this.hurtTimestamp);
    nbttagcompound.setShort("DeathTime", (short)this.deathTicks);
    nbttagcompound.setFloat("AbsorptionAmount", getAbsorptionHearts());
    ItemStack[] aitemstack = getEquipment();
    int i = aitemstack.length;
    for (int j = 0; j < i; j++)
    {
      ItemStack itemstack = aitemstack[j];
      if (itemstack != null) {
        this.c.a(itemstack.B());
      }
    }
    nbttagcompound.set("Attributes", GenericAttributes.a(getAttributeMap()));
    aitemstack = getEquipment();
    i = aitemstack.length;
    for (j = 0; j < i; j++)
    {
      ItemStack itemstack = aitemstack[j];
      if (itemstack != null) {
        this.c.b(itemstack.B());
      }
    }
    if (!this.effects.isEmpty())
    {
      NBTTagList nbttaglist = new NBTTagList();
      Iterator iterator = this.effects.values().iterator();
      while (iterator.hasNext())
      {
        MobEffect mobeffect = (MobEffect)iterator.next();
        
        nbttaglist.add(mobeffect.a(new NBTTagCompound()));
      }
      nbttagcompound.set("ActiveEffects", nbttaglist);
    }
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    setAbsorptionHearts(nbttagcompound.getFloat("AbsorptionAmount"));
    if ((nbttagcompound.hasKeyOfType("Attributes", 9)) && (this.world != null) && (!this.world.isClientSide)) {
      GenericAttributes.a(getAttributeMap(), nbttagcompound.getList("Attributes", 10));
    }
    if (nbttagcompound.hasKeyOfType("ActiveEffects", 9))
    {
      NBTTagList nbttaglist = nbttagcompound.getList("ActiveEffects", 10);
      for (int i = 0; i < nbttaglist.size(); i++)
      {
        NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
        MobEffect mobeffect = MobEffect.b(nbttagcompound1);
        if (mobeffect != null) {
          this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
        }
      }
    }
    if (nbttagcompound.hasKey("Bukkit.MaxHealth"))
    {
      NBTBase nbtbase = nbttagcompound.get("Bukkit.MaxHealth");
      if (nbtbase.getTypeId() == 5) {
        getAttributeInstance(GenericAttributes.maxHealth).setValue(((NBTTagFloat)nbtbase).c());
      } else if (nbtbase.getTypeId() == 3) {
        getAttributeInstance(GenericAttributes.maxHealth).setValue(((NBTTagInt)nbtbase).d());
      }
    }
    if (nbttagcompound.hasKeyOfType("HealF", 99))
    {
      setHealth(nbttagcompound.getFloat("HealF"));
    }
    else
    {
      NBTBase nbtbase = nbttagcompound.get("Health");
      if (nbtbase == null) {
        setHealth(getMaxHealth());
      } else if (nbtbase.getTypeId() == 5) {
        setHealth(((NBTTagFloat)nbtbase).h());
      } else if (nbtbase.getTypeId() == 2) {
        setHealth(((NBTTagShort)nbtbase).e());
      }
    }
    this.hurtTicks = nbttagcompound.getShort("HurtTime");
    this.deathTicks = nbttagcompound.getShort("DeathTime");
    this.hurtTimestamp = nbttagcompound.getInt("HurtByTimestamp");
  }
  
  private boolean isTickingEffects = false;
  private List<Object> effectsToProcess = Lists.newArrayList();
  
  protected void bi()
  {
    Iterator iterator = this.effects.keySet().iterator();
    
    this.isTickingEffects = true;
    MobEffect mobeffect;
    while (iterator.hasNext())
    {
      Integer integer = (Integer)iterator.next();
      mobeffect = (MobEffect)this.effects.get(integer);
      if (!mobeffect.tick(this))
      {
        if (!this.world.isClientSide)
        {
          iterator.remove();
          b(mobeffect);
        }
      }
      else if (mobeffect.getDuration() % 600 == 0) {
        a(mobeffect, false);
      }
    }
    this.isTickingEffects = false;
    for (Object e : this.effectsToProcess) {
      if ((e instanceof MobEffect)) {
        addEffect((MobEffect)e);
      } else {
        removeEffect(((Integer)e).intValue());
      }
    }
    if (this.updateEffects)
    {
      if (!this.world.isClientSide) {
        B();
      }
      this.updateEffects = false;
    }
    int i = this.datawatcher.getInt(7);
    boolean flag = this.datawatcher.getByte(8) > 0;
    if (i > 0)
    {
      boolean flag1 = false;
      if (!isInvisible()) {
        flag1 = this.random.nextBoolean();
      } else {
        flag1 = this.random.nextInt(15) == 0;
      }
      if (flag) {
        flag1 &= this.random.nextInt(5) == 0;
      }
      if ((flag1) && (i > 0))
      {
        double d0 = (i >> 16 & 0xFF) / 255.0D;
        double d1 = (i >> 8 & 0xFF) / 255.0D;
        double d2 = (i >> 0 & 0xFF) / 255.0D;
        
        this.world.addParticle(flag ? EnumParticle.SPELL_MOB_AMBIENT : EnumParticle.SPELL_MOB, this.locX + (this.random.nextDouble() - 0.5D) * this.width, this.locY + this.random.nextDouble() * this.length, this.locZ + (this.random.nextDouble() - 0.5D) * this.width, d0, d1, d2, new int[0]);
      }
    }
  }
  
  protected void B()
  {
    if (this.effects.isEmpty())
    {
      bj();
      setInvisible(false);
    }
    else
    {
      int i = PotionBrewer.a(this.effects.values());
      
      this.datawatcher.watch(8, Byte.valueOf((byte)(PotionBrewer.b(this.effects.values()) ? 1 : 0)));
      this.datawatcher.watch(7, Integer.valueOf(i));
      setInvisible(hasEffect(MobEffectList.INVISIBILITY.id));
    }
  }
  
  protected void bj()
  {
    this.datawatcher.watch(8, Byte.valueOf((byte)0));
    this.datawatcher.watch(7, Integer.valueOf(0));
  }
  
  public void removeAllEffects()
  {
    Iterator iterator = this.effects.keySet().iterator();
    while (iterator.hasNext())
    {
      Integer integer = (Integer)iterator.next();
      MobEffect mobeffect = (MobEffect)this.effects.get(integer);
      if (!this.world.isClientSide)
      {
        iterator.remove();
        b(mobeffect);
      }
    }
  }
  
  public Collection<MobEffect> getEffects()
  {
    return this.effects.values();
  }
  
  public boolean hasEffect(int i)
  {
    return (this.effects.size() != 0) && (this.effects.containsKey(Integer.valueOf(i)));
  }
  
  public boolean hasEffect(MobEffectList mobeffectlist)
  {
    return this.effects.containsKey(Integer.valueOf(mobeffectlist.id));
  }
  
  public MobEffect getEffect(MobEffectList mobeffectlist)
  {
    return (MobEffect)this.effects.get(Integer.valueOf(mobeffectlist.id));
  }
  
  public void addEffect(MobEffect mobeffect)
  {
    AsyncCatcher.catchOp("effect add");
    if (this.isTickingEffects)
    {
      this.effectsToProcess.add(mobeffect);
      return;
    }
    if (d(mobeffect)) {
      if (this.effects.containsKey(Integer.valueOf(mobeffect.getEffectId())))
      {
        ((MobEffect)this.effects.get(Integer.valueOf(mobeffect.getEffectId()))).a(mobeffect);
        a((MobEffect)this.effects.get(Integer.valueOf(mobeffect.getEffectId())), true);
      }
      else
      {
        this.effects.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
        a(mobeffect);
      }
    }
  }
  
  public boolean d(MobEffect mobeffect)
  {
    if (getMonsterType() == EnumMonsterType.UNDEAD)
    {
      int i = mobeffect.getEffectId();
      if ((i == MobEffectList.REGENERATION.id) || (i == MobEffectList.POISON.id)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean bm()
  {
    return getMonsterType() == EnumMonsterType.UNDEAD;
  }
  
  public void removeEffect(int i)
  {
    if (this.isTickingEffects)
    {
      this.effectsToProcess.add(Integer.valueOf(i));
      return;
    }
    MobEffect mobeffect = (MobEffect)this.effects.remove(Integer.valueOf(i));
    if (mobeffect != null) {
      b(mobeffect);
    }
  }
  
  protected void a(MobEffect mobeffect)
  {
    this.updateEffects = true;
    if (!this.world.isClientSide) {
      MobEffectList.byId[mobeffect.getEffectId()].b(this, getAttributeMap(), mobeffect.getAmplifier());
    }
  }
  
  protected void a(MobEffect mobeffect, boolean flag)
  {
    this.updateEffects = true;
    if ((flag) && (!this.world.isClientSide))
    {
      MobEffectList.byId[mobeffect.getEffectId()].a(this, getAttributeMap(), mobeffect.getAmplifier());
      MobEffectList.byId[mobeffect.getEffectId()].b(this, getAttributeMap(), mobeffect.getAmplifier());
    }
  }
  
  protected void b(MobEffect mobeffect)
  {
    this.updateEffects = true;
    if (!this.world.isClientSide) {
      MobEffectList.byId[mobeffect.getEffectId()].a(this, getAttributeMap(), mobeffect.getAmplifier());
    }
  }
  
  public void heal(float f)
  {
    heal(f, EntityRegainHealthEvent.RegainReason.CUSTOM);
  }
  
  public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason)
  {
    float f1 = getHealth();
    if (f1 > 0.0F)
    {
      EntityRegainHealthEvent event = new EntityRegainHealthEvent(getBukkitEntity(), f, regainReason);
      this.world.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
        setHealth((float)(getHealth() + event.getAmount()));
      }
    }
  }
  
  public final float getHealth()
  {
    if ((this instanceof EntityPlayer)) {
      return (float)((EntityPlayer)this).getBukkitEntity().getHealth();
    }
    return this.datawatcher.getFloat(6);
  }
  
  public void setHealth(float f)
  {
    if ((this instanceof EntityPlayer))
    {
      CraftPlayer player = ((EntityPlayer)this).getBukkitEntity();
      if (f < 0.0F) {
        player.setRealHealth(0.0D);
      } else if (f > player.getMaxHealth()) {
        player.setRealHealth(player.getMaxHealth());
      } else {
        player.setRealHealth(f);
      }
      this.datawatcher.watch(6, Float.valueOf(player.getScaledHealth()));
      return;
    }
    this.datawatcher.watch(6, Float.valueOf(MathHelper.a(f, 0.0F, getMaxHealth())));
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    if (this.world.isClientSide) {
      return false;
    }
    this.ticksFarFromPlayer = 0;
    if (getHealth() <= 0.0F) {
      return false;
    }
    if ((damagesource.o()) && (hasEffect(MobEffectList.FIRE_RESISTANCE))) {
      return false;
    }
    this.aB = 1.5F;
    boolean flag = true;
    if (this.noDamageTicks > this.maxNoDamageTicks / 2.0F)
    {
      if (f <= this.lastDamage) {
        return false;
      }
      if (!d(damagesource, f - this.lastDamage)) {
        return false;
      }
      this.lastDamage = f;
      flag = false;
    }
    else
    {
      getHealth();
      if (!d(damagesource, f)) {
        return false;
      }
      this.lastDamage = f;
      this.noDamageTicks = this.maxNoDamageTicks;
      
      this.hurtTicks = (this.av = 10);
    }
    if ((this instanceof EntityAnimal))
    {
      ((EntityAnimal)this).cq();
      if ((this instanceof EntityTameableAnimal)) {
        ((EntityTameableAnimal)this).getGoalSit().setSitting(false);
      }
    }
    this.aw = 0.0F;
    Entity entity = damagesource.getEntity();
    if (entity != null)
    {
      if ((entity instanceof EntityLiving)) {
        b((EntityLiving)entity);
      }
      if ((entity instanceof EntityHuman))
      {
        this.lastDamageByPlayerTime = 100;
        this.killer = ((EntityHuman)entity);
      }
      else if ((entity instanceof EntityWolf))
      {
        EntityWolf entitywolf = (EntityWolf)entity;
        if (entitywolf.isTamed())
        {
          this.lastDamageByPlayerTime = 100;
          this.killer = null;
        }
      }
    }
    if (flag)
    {
      this.world.broadcastEntityEffect(this, (byte)2);
      if (damagesource != DamageSource.DROWN) {
        ac();
      }
      if (entity != null)
      {
        double d0 = entity.locX - this.locX;
        for (double d1 = entity.locZ - this.locZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
          d0 = (Math.random() - Math.random()) * 0.01D;
        }
        this.aw = ((float)(MathHelper.b(d1, d0) * 180.0D / 3.1415927410125732D - this.yaw));
        a(entity, f, d0, d1);
      }
      else
      {
        this.aw = ((int)(Math.random() * 2.0D) * 180);
      }
    }
    if (getHealth() <= 0.0F)
    {
      String s = bp();
      if ((flag) && (s != null)) {
        makeSound(s, bB(), bC());
      }
      die(damagesource);
    }
    else
    {
      String s = bo();
      if ((flag) && (s != null)) {
        makeSound(s, bB(), bC());
      }
    }
    return true;
  }
  
  public void b(ItemStack itemstack)
  {
    makeSound("random.break", 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);
    for (int i = 0; i < 5; i++)
    {
      Vec3D vec3d = new Vec3D((this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
      
      vec3d = vec3d.a(-this.pitch * 3.1415927F / 180.0F);
      vec3d = vec3d.b(-this.yaw * 3.1415927F / 180.0F);
      double d0 = -this.random.nextFloat() * 0.6D - 0.3D;
      Vec3D vec3d1 = new Vec3D((this.random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
      
      vec3d1 = vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
      vec3d1 = vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
      vec3d1 = vec3d1.add(this.locX, this.locY + getHeadHeight(), this.locZ);
      this.world.addParticle(EnumParticle.ITEM_CRACK, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c, new int[] { Item.getId(itemstack.getItem()) });
    }
  }
  
  public void die(DamageSource damagesource)
  {
    Entity entity = damagesource.getEntity();
    EntityLiving entityliving = bt();
    if ((this.aW >= 0) && (entityliving != null)) {
      entityliving.b(this, this.aW);
    }
    if (entity != null) {
      entity.a(this);
    }
    this.aP = true;
    bs().g();
    if (!this.world.isClientSide)
    {
      int i = 0;
      if ((entity instanceof EntityHuman)) {
        i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving)entity);
      }
      if ((ba()) && (this.world.getGameRules().getBoolean("doMobLoot")))
      {
        this.drops = new ArrayList();
        
        dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
        dropEquipment(this.lastDamageByPlayerTime > 0, i);
        if ((this.lastDamageByPlayerTime > 0) && (this.random.nextFloat() < 0.025F + i * 0.01F)) {
          getRareDrop();
        }
        CraftEventFactory.callEntityDeathEvent(this, this.drops);
        this.drops = null;
      }
      else
      {
        CraftEventFactory.callEntityDeathEvent(this);
      }
    }
    this.world.broadcastEntityEffect(this, (byte)3);
  }
  
  protected void dropEquipment(boolean flag, int i) {}
  
  public void a(Entity entity, float f, double d0, double d1)
  {
    if (this.random.nextDouble() >= getAttributeInstance(GenericAttributes.c).getValue())
    {
      this.ai = true;
      float f1 = MathHelper.sqrt(d0 * d0 + d1 * d1);
      float f2 = 0.4F;
      
      this.motX /= 2.0D;
      this.motY /= 2.0D;
      this.motZ /= 2.0D;
      this.motX -= d0 / f1 * f2;
      this.motY += f2;
      this.motZ -= d1 / f1 * f2;
      if (this.motY > 0.4000000059604645D) {
        this.motY = 0.4000000059604645D;
      }
    }
  }
  
  protected String bo()
  {
    return "game.neutral.hurt";
  }
  
  protected String bp()
  {
    return "game.neutral.die";
  }
  
  protected void getRareDrop() {}
  
  protected void dropDeathLoot(boolean flag, int i) {}
  
  public boolean k_()
  {
    int i = MathHelper.floor(this.locX);
    int j = MathHelper.floor(getBoundingBox().b);
    int k = MathHelper.floor(this.locZ);
    Block block = this.world.getType(new BlockPosition(i, j, k)).getBlock();
    
    return ((block == Blocks.LADDER) || (block == Blocks.VINE)) && ((!(this instanceof EntityHuman)) || (!((EntityHuman)this).isSpectator()));
  }
  
  public boolean isAlive()
  {
    return (!this.dead) && (getHealth() > 0.0F);
  }
  
  public void e(float f, float f1)
  {
    super.e(f, f1);
    MobEffect mobeffect = getEffect(MobEffectList.JUMP);
    float f2 = mobeffect != null ? mobeffect.getAmplifier() + 1 : 0.0F;
    int i = MathHelper.f((f - 3.0F - f2) * f1);
    if (i > 0)
    {
      if (!damageEntity(DamageSource.FALL, i)) {
        return;
      }
      makeSound(n(i), 1.0F, 1.0F);
      
      int j = MathHelper.floor(this.locX);
      int k = MathHelper.floor(this.locY - 0.20000000298023224D);
      int l = MathHelper.floor(this.locZ);
      Block block = this.world.getType(new BlockPosition(j, k, l)).getBlock();
      if (block.getMaterial() != Material.AIR)
      {
        Block.StepSound block_stepsound = block.stepSound;
        
        makeSound(block_stepsound.getStepSound(), block_stepsound.getVolume1() * 0.5F, block_stepsound.getVolume2() * 0.75F);
      }
    }
  }
  
  protected String n(int i)
  {
    return i > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
  }
  
  public int br()
  {
    int i = 0;
    ItemStack[] aitemstack = getEquipment();
    int j = aitemstack.length;
    for (int k = 0; k < j; k++)
    {
      ItemStack itemstack = aitemstack[k];
      if ((itemstack != null) && ((itemstack.getItem() instanceof ItemArmor)))
      {
        int l = ((ItemArmor)itemstack.getItem()).c;
        
        i += l;
      }
    }
    return i;
  }
  
  protected void damageArmor(float f) {}
  
  protected float applyArmorModifier(DamageSource damagesource, float f)
  {
    if (!damagesource.ignoresArmor())
    {
      int i = 25 - br();
      float f1 = f * i;
      
      f = f1 / 25.0F;
    }
    return f;
  }
  
  protected float applyMagicModifier(DamageSource damagesource, float f)
  {
    if (damagesource.isStarvation()) {
      return f;
    }
    if (f <= 0.0F) {
      return 0.0F;
    }
    int i = EnchantmentManager.a(getEquipment(), damagesource);
    if (i > 20) {
      i = 20;
    }
    if ((i > 0) && (i <= 20))
    {
      int j = 25 - i;
      float f1 = f * j;
      f = f1 / 25.0F;
    }
    return f;
  }
  
  protected boolean d(final DamageSource damagesource, float f)
  {
    if (!isInvulnerable(damagesource))
    {
      final boolean human = this instanceof EntityHuman;
      float originalDamage = f;
      Function<Double, Double> hardHat = new Function()
      {
        public Double apply(Double f)
        {
          if (((damagesource == DamageSource.ANVIL) || (damagesource == DamageSource.FALLING_BLOCK)) && (EntityLiving.this.getEquipment(4) != null)) {
            return Double.valueOf(-(f.doubleValue() - f.doubleValue() * 0.75D));
          }
          return Double.valueOf(-0.0D);
        }
      };
      float hardHatModifier = ((Double)hardHat.apply(Double.valueOf(f))).floatValue();
      f += hardHatModifier;
      
      Function<Double, Double> blocking = new Function()
      {
        public Double apply(Double f)
        {
          if ((human) && 
            (!damagesource.ignoresArmor()) && (((EntityHuman)EntityLiving.this).isBlocking()) && (f.doubleValue() > 0.0D)) {
            return Double.valueOf(-(f.doubleValue() - (1.0D + f.doubleValue()) * 0.5D));
          }
          return Double.valueOf(-0.0D);
        }
      };
      float blockingModifier = ((Double)blocking.apply(Double.valueOf(f))).floatValue();
      f += blockingModifier;
      
      Function<Double, Double> armor = new Function()
      {
        public Double apply(Double f)
        {
          return Double.valueOf(-(f.doubleValue() - EntityLiving.this.applyArmorModifier(damagesource, f.floatValue())));
        }
      };
      float armorModifier = ((Double)armor.apply(Double.valueOf(f))).floatValue();
      f += armorModifier;
      
      Function<Double, Double> resistance = new Function()
      {
        public Double apply(Double f)
        {
          if ((!damagesource.isStarvation()) && (EntityLiving.this.hasEffect(MobEffectList.RESISTANCE)) && (damagesource != DamageSource.OUT_OF_WORLD))
          {
            int i = (EntityLiving.this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - i;
            float f1 = f.floatValue() * j;
            return Double.valueOf(-(f.doubleValue() - f1 / 25.0F));
          }
          return Double.valueOf(-0.0D);
        }
      };
      float resistanceModifier = ((Double)resistance.apply(Double.valueOf(f))).floatValue();
      f += resistanceModifier;
      
      Function<Double, Double> magic = new Function()
      {
        public Double apply(Double f)
        {
          return Double.valueOf(-(f.doubleValue() - EntityLiving.this.applyMagicModifier(damagesource, f.floatValue())));
        }
      };
      float magicModifier = ((Double)magic.apply(Double.valueOf(f))).floatValue();
      f += magicModifier;
      
      Function<Double, Double> absorption = new Function()
      {
        public Double apply(Double f)
        {
          return Double.valueOf(-Math.max(f.doubleValue() - Math.max(f.doubleValue() - EntityLiving.this.getAbsorptionHearts(), 0.0D), 0.0D));
        }
      };
      float absorptionModifier = ((Double)absorption.apply(Double.valueOf(f))).floatValue();
      
      EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, originalDamage, hardHatModifier, blockingModifier, armorModifier, resistanceModifier, magicModifier, absorptionModifier, hardHat, blocking, armor, resistance, magic, absorption);
      if (event.isCancelled()) {
        return false;
      }
      f = (float)event.getFinalDamage();
      if (((damagesource == DamageSource.ANVIL) || (damagesource == DamageSource.FALLING_BLOCK)) && (getEquipment(4) != null)) {
        getEquipment(4).damage((int)(event.getDamage() * 4.0D + this.random.nextFloat() * event.getDamage() * 2.0D), this);
      }
      if (!damagesource.ignoresArmor())
      {
        float armorDamage = (float)(event.getDamage() + event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) + event.getDamage(EntityDamageEvent.DamageModifier.HARD_HAT));
        damageArmor(armorDamage);
      }
      absorptionModifier = (float)-event.getDamage(EntityDamageEvent.DamageModifier.ABSORPTION);
      setAbsorptionHearts(Math.max(getAbsorptionHearts() - absorptionModifier, 0.0F));
      if (f != 0.0F)
      {
        if (human)
        {
          ((EntityHuman)this).applyExhaustion(damagesource.getExhaustionCost());
          if (f < 3.4028235E37F) {
            ((EntityHuman)this).a(StatisticList.x, Math.round(f * 10.0F));
          }
        }
        float f2 = getHealth();
        
        setHealth(f2 - f);
        bs().a(damagesource, f2, f);
        if (human) {
          return true;
        }
        setAbsorptionHearts(getAbsorptionHearts() - f);
      }
      return true;
    }
    return false;
  }
  
  public CombatTracker bs()
  {
    return this.combatTracker;
  }
  
  public EntityLiving bt()
  {
    return this.lastDamager != null ? this.lastDamager : this.killer != null ? this.killer : this.combatTracker.c() != null ? this.combatTracker.c() : null;
  }
  
  public final float getMaxHealth()
  {
    return (float)getAttributeInstance(GenericAttributes.maxHealth).getValue();
  }
  
  public final int bv()
  {
    return this.datawatcher.getByte(9);
  }
  
  public final void o(int i)
  {
    this.datawatcher.watch(9, Byte.valueOf((byte)i));
  }
  
  private int n()
  {
    return hasEffect(MobEffectList.SLOWER_DIG) ? 6 + (1 + getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2 : hasEffect(MobEffectList.FASTER_DIG) ? 6 - (1 + getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1 : 6;
  }
  
  public void bw()
  {
    if ((!this.ar) || (this.as >= n() / 2) || (this.as < 0))
    {
      this.as = -1;
      this.ar = true;
      if ((this.world instanceof WorldServer)) {
        ((WorldServer)this.world).getTracker().a(this, new PacketPlayOutAnimation(this, 0));
      }
    }
  }
  
  protected void O()
  {
    damageEntity(DamageSource.OUT_OF_WORLD, 4.0F);
  }
  
  protected void bx()
  {
    int i = n();
    if (this.ar)
    {
      this.as += 1;
      if (this.as >= i)
      {
        this.as = 0;
        this.ar = false;
      }
    }
    else
    {
      this.as = 0;
    }
    this.az = (this.as / i);
  }
  
  public AttributeInstance getAttributeInstance(IAttribute iattribute)
  {
    return getAttributeMap().a(iattribute);
  }
  
  public AttributeMapBase getAttributeMap()
  {
    if (this.c == null) {
      this.c = new AttributeMapServer();
    }
    return this.c;
  }
  
  public EnumMonsterType getMonsterType()
  {
    return EnumMonsterType.UNDEFINED;
  }
  
  public abstract ItemStack bA();
  
  public abstract ItemStack getEquipment(int paramInt);
  
  public abstract void setEquipment(int paramInt, ItemStack paramItemStack);
  
  public void setSprinting(boolean flag)
  {
    super.setSprinting(flag);
    AttributeInstance attributeinstance = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
    if (attributeinstance.a(a) != null) {
      attributeinstance.c(b);
    }
    if (flag) {
      attributeinstance.b(b);
    }
  }
  
  public abstract ItemStack[] getEquipment();
  
  protected float bB()
  {
    return 1.0F;
  }
  
  protected float bC()
  {
    return isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
  }
  
  protected boolean bD()
  {
    return getHealth() <= 0.0F;
  }
  
  public void q(Entity entity)
  {
    double d0 = entity.locX;
    double d1 = entity.getBoundingBox().b + entity.length;
    double d2 = entity.locZ;
    byte b0 = 1;
    for (int i = -b0; i <= b0; i++) {
      for (int j = -b0; j < b0; j++) {
        if ((i != 0) || (j != 0))
        {
          int k = (int)(this.locX + i);
          int l = (int)(this.locZ + j);
          AxisAlignedBB axisalignedbb = getBoundingBox().c(i, 1.0D, j);
          if (this.world.a(axisalignedbb).isEmpty())
          {
            if (World.a(this.world, new BlockPosition(k, (int)this.locY, l)))
            {
              enderTeleportTo(this.locX + i, this.locY + 1.0D, this.locZ + j);
              return;
            }
            if ((World.a(this.world, new BlockPosition(k, (int)this.locY - 1, l))) || (this.world.getType(new BlockPosition(k, (int)this.locY - 1, l)).getBlock().getMaterial() == Material.WATER))
            {
              d0 = this.locX + i;
              d1 = this.locY + 1.0D;
              d2 = this.locZ + j;
            }
          }
        }
      }
    }
    enderTeleportTo(d0, d1, d2);
  }
  
  protected float bE()
  {
    return 0.42F;
  }
  
  protected void bF()
  {
    this.motY = bE();
    if (hasEffect(MobEffectList.JUMP)) {
      this.motY += (getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F;
    }
    if (isSprinting())
    {
      float f = this.yaw * 0.017453292F;
      
      this.motX -= MathHelper.sin(f) * 0.2F;
      this.motZ += MathHelper.cos(f) * 0.2F;
    }
    this.ai = true;
  }
  
  protected void bG()
  {
    this.motY += 0.03999999910593033D;
  }
  
  protected void bH()
  {
    this.motY += 0.03999999910593033D;
  }
  
  public void g(float f, float f1)
  {
    if (bM()) {
      if ((V()) && ((!(this instanceof EntityHuman)) || (!((EntityHuman)this).abilities.isFlying)))
      {
        double d0 = this.locY;
        float f3 = 0.8F;
        float f4 = 0.02F;
        float f2 = EnchantmentManager.b(this);
        if (f2 > 3.0F) {
          f2 = 3.0F;
        }
        if (!this.onGround) {
          f2 *= 0.5F;
        }
        if (f2 > 0.0F)
        {
          f3 += (0.54600006F - f3) * f2 / 3.0F;
          f4 += (bI() * 1.0F - f4) * f2 / 3.0F;
        }
        a(f, f1, f4);
        move(this.motX, this.motY, this.motZ);
        this.motX *= f3;
        this.motY *= 0.800000011920929D;
        this.motZ *= f3;
        this.motY -= 0.02D;
        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
          this.motY = 0.30000001192092896D;
        }
      }
      else if ((ab()) && ((!(this instanceof EntityHuman)) || (!((EntityHuman)this).abilities.isFlying)))
      {
        double d0 = this.locY;
        a(f, f1, 0.02F);
        move(this.motX, this.motY, this.motZ);
        this.motX *= 0.5D;
        this.motY *= 0.5D;
        this.motZ *= 0.5D;
        this.motY -= 0.02D;
        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
          this.motY = 0.30000001192092896D;
        }
      }
      else
      {
        float f5 = 0.91F;
        if (this.onGround) {
          f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
        }
        float f6 = 0.16277136F / (f5 * f5 * f5);
        float f3;
        float f3;
        if (this.onGround) {
          f3 = bI() * f6;
        } else {
          f3 = this.aM;
        }
        a(f, f1, f3);
        f5 = 0.91F;
        if (this.onGround) {
          f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
        }
        if (k_())
        {
          float f4 = 0.15F;
          this.motX = MathHelper.a(this.motX, -f4, f4);
          this.motZ = MathHelper.a(this.motZ, -f4, f4);
          this.fallDistance = 0.0F;
          if (this.motY < -0.15D) {
            this.motY = -0.15D;
          }
          boolean flag = (isSneaking()) && ((this instanceof EntityHuman));
          if ((flag) && (this.motY < 0.0D)) {
            this.motY = 0.0D;
          }
        }
        move(this.motX, this.motY, this.motZ);
        if ((this.positionChanged) && (k_())) {
          this.motY = 0.2D;
        }
        if ((this.world.isClientSide) && ((!this.world.isLoaded(new BlockPosition((int)this.locX, 0, (int)this.locZ))) || (!this.world.getChunkAtWorldCoords(new BlockPosition((int)this.locX, 0, (int)this.locZ)).o())))
        {
          if (this.locY > 0.0D) {
            this.motY = -0.1D;
          } else {
            this.motY = 0.0D;
          }
        }
        else {
          this.motY -= 0.08D;
        }
        this.motY *= 0.9800000190734863D;
        this.motX *= f5;
        this.motZ *= f5;
      }
    }
    this.aA = this.aB;
    double d0 = this.locX - this.lastX;
    double d1 = this.locZ - this.lastZ;
    
    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
    if (f2 > 1.0F) {
      f2 = 1.0F;
    }
    this.aB += (f2 - this.aB) * 0.4F;
    this.aC += this.aB;
  }
  
  public float bI()
  {
    return this.bm;
  }
  
  public void k(float f)
  {
    this.bm = f;
  }
  
  public boolean r(Entity entity)
  {
    p(entity);
    return false;
  }
  
  public boolean isSleeping()
  {
    return false;
  }
  
  public void t_()
  {
    SpigotTimings.timerEntityBaseTick.startTiming();
    super.t_();
    if (!this.world.isClientSide)
    {
      int i = bv();
      if (i > 0)
      {
        if (this.at <= 0) {
          this.at = (20 * (30 - i));
        }
        this.at -= 1;
        if (this.at <= 0) {
          o(i - 1);
        }
      }
      for (int j = 0; j < 5; j++)
      {
        ItemStack itemstack = this.h[j];
        ItemStack itemstack1 = getEquipment(j);
        if (!ItemStack.matches(itemstack1, itemstack))
        {
          ((WorldServer)this.world).getTracker().a(this, new PacketPlayOutEntityEquipment(getId(), j, itemstack1));
          if (itemstack != null) {
            this.c.a(itemstack.B());
          }
          if (itemstack1 != null) {
            this.c.b(itemstack1.B());
          }
          this.h[j] = (itemstack1 == null ? null : itemstack1.cloneItemStack());
        }
      }
      if (this.ticksLived % 20 == 0) {
        bs().g();
      }
    }
    SpigotTimings.timerEntityBaseTick.stopTiming();
    m();
    SpigotTimings.timerEntityTickRest.startTiming();
    double d0 = this.locX - this.lastX;
    double d1 = this.locZ - this.lastZ;
    float f = (float)(d0 * d0 + d1 * d1);
    float f1 = this.aI;
    float f2 = 0.0F;
    
    this.aR = this.aS;
    float f3 = 0.0F;
    if (f > 0.0025000002F)
    {
      f3 = 1.0F;
      f2 = (float)Math.sqrt(f) * 3.0F;
      
      f1 = (float)TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
    }
    if (this.az > 0.0F) {
      f1 = this.yaw;
    }
    if (!this.onGround) {
      f3 = 0.0F;
    }
    this.aS += (f3 - this.aS) * 0.3F;
    this.world.methodProfiler.a("headTurn");
    f2 = h(f1, f2);
    this.world.methodProfiler.b();
    this.world.methodProfiler.a("rangeChecks");
    while (this.yaw - this.lastYaw < -180.0F) {
      this.lastYaw -= 360.0F;
    }
    while (this.yaw - this.lastYaw >= 180.0F) {
      this.lastYaw += 360.0F;
    }
    while (this.aI - this.aJ < -180.0F) {
      this.aJ -= 360.0F;
    }
    while (this.aI - this.aJ >= 180.0F) {
      this.aJ += 360.0F;
    }
    while (this.pitch - this.lastPitch < -180.0F) {
      this.lastPitch -= 360.0F;
    }
    while (this.pitch - this.lastPitch >= 180.0F) {
      this.lastPitch += 360.0F;
    }
    while (this.aK - this.aL < -180.0F) {
      this.aL -= 360.0F;
    }
    while (this.aK - this.aL >= 180.0F) {
      this.aL += 360.0F;
    }
    this.world.methodProfiler.b();
    this.aT += f2;
    SpigotTimings.timerEntityTickRest.stopTiming();
  }
  
  protected float h(float f, float f1)
  {
    float f2 = MathHelper.g(f - this.aI);
    
    this.aI += f2 * 0.3F;
    float f3 = MathHelper.g(this.yaw - this.aI);
    boolean flag = (f3 < -90.0F) || (f3 >= 90.0F);
    if (f3 < -75.0F) {
      f3 = -75.0F;
    }
    if (f3 >= 75.0F) {
      f3 = 75.0F;
    }
    this.aI = (this.yaw - f3);
    if (f3 * f3 > 2500.0F) {
      this.aI += f3 * 0.2F;
    }
    if (flag) {
      f1 *= -1.0F;
    }
    return f1;
  }
  
  public void m()
  {
    if (this.bn > 0) {
      this.bn -= 1;
    }
    if (this.bc > 0)
    {
      double d0 = this.locX + (this.bd - this.locX) / this.bc;
      double d1 = this.locY + (this.be - this.locY) / this.bc;
      double d2 = this.locZ + (this.bf - this.locZ) / this.bc;
      double d3 = MathHelper.g(this.bg - this.yaw);
      
      this.yaw = ((float)(this.yaw + d3 / this.bc));
      this.pitch = ((float)(this.pitch + (this.bh - this.pitch) / this.bc));
      this.bc -= 1;
      setPosition(d0, d1, d2);
      setYawPitch(this.yaw, this.pitch);
    }
    else if (!bM())
    {
      this.motX *= 0.98D;
      this.motY *= 0.98D;
      this.motZ *= 0.98D;
    }
    if (Math.abs(this.motX) < 0.005D) {
      this.motX = 0.0D;
    }
    if (Math.abs(this.motY) < 0.005D) {
      this.motY = 0.0D;
    }
    if (Math.abs(this.motZ) < 0.005D) {
      this.motZ = 0.0D;
    }
    this.world.methodProfiler.a("ai");
    SpigotTimings.timerEntityAI.startTiming();
    if (bD())
    {
      this.aY = false;
      this.aZ = 0.0F;
      this.ba = 0.0F;
      this.bb = 0.0F;
    }
    else if (bM())
    {
      this.world.methodProfiler.a("newAi");
      doTick();
      this.world.methodProfiler.b();
    }
    SpigotTimings.timerEntityAI.stopTiming();
    
    this.world.methodProfiler.b();
    this.world.methodProfiler.a("jump");
    if (this.aY)
    {
      if (V())
      {
        bG();
      }
      else if (ab())
      {
        bH();
      }
      else if ((this.onGround) && (this.bn == 0))
      {
        bF();
        this.bn = 10;
      }
    }
    else {
      this.bn = 0;
    }
    this.world.methodProfiler.b();
    this.world.methodProfiler.a("travel");
    this.aZ *= 0.98F;
    this.ba *= 0.98F;
    this.bb *= 0.9F;
    SpigotTimings.timerEntityAIMove.startTiming();
    g(this.aZ, this.ba);
    SpigotTimings.timerEntityAIMove.stopTiming();
    this.world.methodProfiler.b();
    this.world.methodProfiler.a("push");
    if (!this.world.isClientSide)
    {
      SpigotTimings.timerEntityAICollision.startTiming();
      bL();
      SpigotTimings.timerEntityAICollision.stopTiming();
    }
    this.world.methodProfiler.b();
  }
  
  protected void doTick() {}
  
  protected void bL()
  {
    List list = this.world.a(this, getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D), Predicates.and(IEntitySelector.d, new Predicate()
    {
      public boolean a(Entity entity)
      {
        return entity.ae();
      }
      
      public boolean apply(Object object)
      {
        return a((Entity)object);
      }
    }));
    if ((ad()) && (!list.isEmpty()))
    {
      this.numCollisions -= this.world.spigotConfig.maxCollisionsPerEntity;
      for (int i = 0; i < list.size(); i++)
      {
        if (this.numCollisions > this.world.spigotConfig.maxCollisionsPerEntity) {
          break;
        }
        Entity entity = (Entity)list.get(i);
        if ((!(entity instanceof EntityLiving)) || ((this instanceof EntityPlayer)) || (this.ticksLived % 2 != 0))
        {
          entity.numCollisions += 1;
          this.numCollisions += 1;
          s(entity);
        }
      }
      this.numCollisions = 0;
    }
  }
  
  protected void s(Entity entity)
  {
    entity.collide(this);
  }
  
  public void mount(Entity entity)
  {
    if ((this.vehicle != null) && (entity == null))
    {
      Entity originalVehicle = this.vehicle;
      if (((this.bukkitEntity instanceof LivingEntity)) && ((this.vehicle.getBukkitEntity() instanceof Vehicle)))
      {
        VehicleExitEvent event = new VehicleExitEvent((Vehicle)this.vehicle.getBukkitEntity(), (LivingEntity)this.bukkitEntity);
        getBukkitEntity().getServer().getPluginManager().callEvent(event);
        if ((event.isCancelled()) || (this.vehicle != originalVehicle)) {
          return;
        }
      }
      if (!this.world.isClientSide) {
        q(this.vehicle);
      }
      if (this.vehicle != null) {
        this.vehicle.passenger = null;
      }
      this.vehicle = null;
    }
    else
    {
      super.mount(entity);
    }
  }
  
  public void ak()
  {
    super.ak();
    this.aR = this.aS;
    this.aS = 0.0F;
    this.fallDistance = 0.0F;
  }
  
  public void i(boolean flag)
  {
    this.aY = flag;
  }
  
  public void receive(Entity entity, int i)
  {
    if ((!entity.dead) && (!this.world.isClientSide))
    {
      EntityTracker entitytracker = ((WorldServer)this.world).getTracker();
      if ((entity instanceof EntityItem)) {
        entitytracker.a(entity, new PacketPlayOutCollect(entity.getId(), getId()));
      }
      if ((entity instanceof EntityArrow)) {
        entitytracker.a(entity, new PacketPlayOutCollect(entity.getId(), getId()));
      }
      if ((entity instanceof EntityExperienceOrb)) {
        entitytracker.a(entity, new PacketPlayOutCollect(entity.getId(), getId()));
      }
    }
  }
  
  public boolean hasLineOfSight(Entity entity)
  {
    return this.world.rayTrace(new Vec3D(this.locX, this.locY + getHeadHeight(), this.locZ), new Vec3D(entity.locX, entity.locY + entity.getHeadHeight(), entity.locZ)) == null;
  }
  
  public Vec3D ap()
  {
    return d(1.0F);
  }
  
  public Vec3D d(float f)
  {
    if (f == 1.0F) {
      return f(this.pitch, this.aK);
    }
    float f1 = this.lastPitch + (this.pitch - this.lastPitch) * f;
    float f2 = this.aL + (this.aK - this.aL) * f;
    
    return f(f1, f2);
  }
  
  public boolean bM()
  {
    return !this.world.isClientSide;
  }
  
  public boolean ad()
  {
    return !this.dead;
  }
  
  public boolean ae()
  {
    return !this.dead;
  }
  
  protected void ac()
  {
    this.velocityChanged = (this.random.nextDouble() >= getAttributeInstance(GenericAttributes.c).getValue());
  }
  
  public float getHeadRotation()
  {
    return this.aK;
  }
  
  public void f(float f)
  {
    this.aK = f;
  }
  
  public void g(float f)
  {
    this.aI = f;
  }
  
  public float getAbsorptionHearts()
  {
    return this.bo;
  }
  
  public void setAbsorptionHearts(float f)
  {
    if (f < 0.0F) {
      f = 0.0F;
    }
    this.bo = f;
  }
  
  public ScoreboardTeamBase getScoreboardTeam()
  {
    return this.world.getScoreboard().getPlayerTeam(getUniqueID().toString());
  }
  
  public boolean c(EntityLiving entityliving)
  {
    return a(entityliving.getScoreboardTeam());
  }
  
  public boolean a(ScoreboardTeamBase scoreboardteambase)
  {
    return getScoreboardTeam() != null ? getScoreboardTeam().isAlly(scoreboardteambase) : false;
  }
  
  public void enterCombat() {}
  
  public void exitCombat() {}
  
  protected void bP()
  {
    this.updateEffects = true;
  }
}
