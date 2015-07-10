package net.minecraft.server.v1_8_R3;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.TrigMath;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.spigotmc.SpigotWorldConfig;

public abstract class EntityHuman
  extends EntityLiving
{
  public PlayerInventory inventory = new PlayerInventory(this);
  private InventoryEnderChest enderChest = new InventoryEnderChest();
  public Container defaultContainer;
  public Container activeContainer;
  protected FoodMetaData foodData = new FoodMetaData(this);
  protected int bm;
  public float bn;
  public float bo;
  public int bp;
  public double bq;
  public double br;
  public double bs;
  public double bt;
  public double bu;
  public double bv;
  public boolean sleeping;
  public BlockPosition bx;
  public int sleepTicks;
  public float by;
  public float bz;
  private BlockPosition c;
  private boolean d;
  private BlockPosition e;
  public PlayerAbilities abilities = new PlayerAbilities();
  public int expLevel;
  public int expTotal;
  public float exp;
  private int f;
  private ItemStack g;
  private int h;
  protected float bE = 0.1F;
  protected float bF = 0.02F;
  private int i;
  private final GameProfile bH;
  private boolean bI = false;
  public EntityFishingHook hookedFish;
  public boolean fauxSleeping;
  public String spawnWorld = "";
  public int oldLevel = -1;
  
  public CraftHumanEntity getBukkitEntity()
  {
    return (CraftHumanEntity)super.getBukkitEntity();
  }
  
  public EntityHuman(World world, GameProfile gameprofile)
  {
    super(world);
    this.uniqueID = a(gameprofile);
    this.bH = gameprofile;
    this.defaultContainer = new ContainerPlayer(this.inventory, !world.isClientSide, this);
    this.activeContainer = this.defaultContainer;
    BlockPosition blockposition = world.getSpawn();
    
    setPositionRotation(blockposition.getX() + 0.5D, blockposition.getY() + 1, blockposition.getZ() + 0.5D, 0.0F, 0.0F);
    this.aV = 180.0F;
    this.maxFireTicks = 20;
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(1.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.10000000149011612D);
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, Byte.valueOf((byte)0));
    this.datawatcher.a(17, Float.valueOf(0.0F));
    this.datawatcher.a(18, Integer.valueOf(0));
    this.datawatcher.a(10, Byte.valueOf((byte)0));
  }
  
  public boolean bS()
  {
    return this.g != null;
  }
  
  public void bU()
  {
    if (this.g != null) {
      this.g.b(this.world, this, this.h);
    }
    bV();
  }
  
  public void bV()
  {
    this.g = null;
    this.h = 0;
    if (!this.world.isClientSide) {
      f(false);
    }
  }
  
  public boolean isBlocking()
  {
    return (bS()) && (this.g.getItem().e(this.g) == EnumAnimation.BLOCK);
  }
  
  public void t_()
  {
    this.noclip = isSpectator();
    if (isSpectator()) {
      this.onGround = false;
    }
    if (this.g != null)
    {
      ItemStack itemstack = this.inventory.getItemInHand();
      if (itemstack == this.g)
      {
        if ((this.h <= 25) && (this.h % 4 == 0)) {
          b(itemstack, 5);
        }
        if ((--this.h == 0) && (!this.world.isClientSide)) {
          s();
        }
      }
      else
      {
        bV();
      }
    }
    if (this.bp > 0) {
      this.bp -= 1;
    }
    if (isSleeping())
    {
      this.sleepTicks += 1;
      if (this.sleepTicks > 100) {
        this.sleepTicks = 100;
      }
      if (!this.world.isClientSide) {
        if (!p()) {
          a(true, true, false);
        } else if (this.world.w()) {
          a(false, true, true);
        }
      }
    }
    else if (this.sleepTicks > 0)
    {
      this.sleepTicks += 1;
      if (this.sleepTicks >= 110) {
        this.sleepTicks = 0;
      }
    }
    super.t_();
    if ((!this.world.isClientSide) && (this.activeContainer != null) && (!this.activeContainer.a(this)))
    {
      closeInventory();
      this.activeContainer = this.defaultContainer;
    }
    if ((isBurning()) && (this.abilities.isInvulnerable)) {
      extinguish();
    }
    this.bq = this.bt;
    this.br = this.bu;
    this.bs = this.bv;
    double d0 = this.locX - this.bt;
    double d1 = this.locY - this.bu;
    double d2 = this.locZ - this.bv;
    double d3 = 10.0D;
    if (d0 > d3) {
      this.bq = (this.bt = this.locX);
    }
    if (d2 > d3) {
      this.bs = (this.bv = this.locZ);
    }
    if (d1 > d3) {
      this.br = (this.bu = this.locY);
    }
    if (d0 < -d3) {
      this.bq = (this.bt = this.locX);
    }
    if (d2 < -d3) {
      this.bs = (this.bv = this.locZ);
    }
    if (d1 < -d3) {
      this.br = (this.bu = this.locY);
    }
    this.bt += d0 * 0.25D;
    this.bv += d2 * 0.25D;
    this.bu += d1 * 0.25D;
    if (this.vehicle == null) {
      this.e = null;
    }
    if (!this.world.isClientSide)
    {
      this.foodData.a(this);
      b(StatisticList.g);
      if (isAlive()) {
        b(StatisticList.h);
      }
    }
    double d4 = MathHelper.a(this.locX, -2.9999999E7D, 2.9999999E7D);
    double d5 = MathHelper.a(this.locZ, -2.9999999E7D, 2.9999999E7D);
    if ((d4 != this.locX) || (d5 != this.locZ)) {
      setPosition(d4, this.locY, d5);
    }
  }
  
  public int L()
  {
    return this.abilities.isInvulnerable ? 0 : 80;
  }
  
  protected String P()
  {
    return "game.player.swim";
  }
  
  protected String aa()
  {
    return "game.player.swim.splash";
  }
  
  public int aq()
  {
    return 10;
  }
  
  public void makeSound(String s, float f, float f1)
  {
    this.world.a(this, s, f, f1);
  }
  
  protected void b(ItemStack itemstack, int i)
  {
    if (itemstack.m() == EnumAnimation.DRINK) {
      makeSound("random.drink", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
    if (itemstack.m() == EnumAnimation.EAT)
    {
      for (int j = 0; j < i; j++)
      {
        Vec3D vec3d = new Vec3D((this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
        
        vec3d = vec3d.a(-this.pitch * 3.1415927F / 180.0F);
        vec3d = vec3d.b(-this.yaw * 3.1415927F / 180.0F);
        double d0 = -this.random.nextFloat() * 0.6D - 0.3D;
        Vec3D vec3d1 = new Vec3D((this.random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
        
        vec3d1 = vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
        vec3d1 = vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
        vec3d1 = vec3d1.add(this.locX, this.locY + getHeadHeight(), this.locZ);
        if (itemstack.usesData()) {
          this.world.addParticle(EnumParticle.ITEM_CRACK, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c, new int[] { Item.getId(itemstack.getItem()), itemstack.getData() });
        } else {
          this.world.addParticle(EnumParticle.ITEM_CRACK, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c, new int[] { Item.getId(itemstack.getItem()) });
        }
      }
      makeSound("random.eat", 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }
  }
  
  protected void s()
  {
    if (this.g != null)
    {
      b(this.g, 16);
      int i = this.g.count;
      
      org.bukkit.inventory.ItemStack craftItem = CraftItemStack.asBukkitCopy(this.g);
      PlayerItemConsumeEvent event = new PlayerItemConsumeEvent((Player)getBukkitEntity(), craftItem);
      this.world.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled())
      {
        if ((this instanceof EntityPlayer))
        {
          ((EntityPlayer)this).playerConnection.sendPacket(new PacketPlayOutSetSlot(0, this.activeContainer.getSlot(this.inventory, this.inventory.itemInHandIndex).index, this.g));
          
          ((EntityPlayer)this).getBukkitEntity().updateInventory();
          ((EntityPlayer)this).getBukkitEntity().updateScaledHealth();
        }
        return;
      }
      if (!craftItem.equals(event.getItem()))
      {
        CraftItemStack.asNMSCopy(event.getItem()).b(this.world, this);
        if ((this instanceof EntityPlayer)) {
          ((EntityPlayer)this).playerConnection.sendPacket(new PacketPlayOutSetSlot(0, this.activeContainer.getSlot(this.inventory, this.inventory.itemInHandIndex).index, this.g));
        }
        return;
      }
      ItemStack itemstack = this.g.b(this.world, this);
      if ((itemstack != this.g) || ((itemstack != null) && (itemstack.count != i)))
      {
        this.inventory.items[this.inventory.itemInHandIndex] = itemstack;
        if (itemstack.count == 0) {
          this.inventory.items[this.inventory.itemInHandIndex] = null;
        }
      }
      bV();
    }
  }
  
  protected boolean bD()
  {
    return (getHealth() <= 0.0F) || (isSleeping());
  }
  
  public void closeInventory()
  {
    this.activeContainer = this.defaultContainer;
  }
  
  public void ak()
  {
    if ((!this.world.isClientSide) && (isSneaking()))
    {
      mount(null);
      setSneaking(false);
    }
    else
    {
      double d0 = this.locX;
      double d1 = this.locY;
      double d2 = this.locZ;
      float f = this.yaw;
      float f1 = this.pitch;
      
      super.ak();
      this.bn = this.bo;
      this.bo = 0.0F;
      l(this.locX - d0, this.locY - d1, this.locZ - d2);
      if ((this.vehicle instanceof EntityPig))
      {
        this.pitch = f1;
        this.yaw = f;
        this.aI = ((EntityPig)this.vehicle).aI;
      }
    }
  }
  
  protected void doTick()
  {
    super.doTick();
    bx();
    this.aK = this.yaw;
  }
  
  public void m()
  {
    if (this.bm > 0) {
      this.bm -= 1;
    }
    if ((this.world.getDifficulty() == EnumDifficulty.PEACEFUL) && (this.world.getGameRules().getBoolean("naturalRegeneration")))
    {
      if ((getHealth() < getMaxHealth()) && (this.ticksLived % 20 == 0)) {
        heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
      }
      if ((this.foodData.c()) && (this.ticksLived % 10 == 0)) {
        this.foodData.a(this.foodData.getFoodLevel() + 1);
      }
    }
    this.inventory.k();
    this.bn = this.bo;
    super.m();
    AttributeInstance attributeinstance = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
    if (!this.world.isClientSide) {
      attributeinstance.setValue(this.abilities.b());
    }
    this.aM = this.bF;
    if (isSprinting()) {
      this.aM = ((float)(this.aM + this.bF * 0.3D));
    }
    k((float)attributeinstance.getValue());
    float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
    float f1 = (float)(TrigMath.atan(-this.motY * 0.20000000298023224D) * 15.0D);
    if (f > 0.1F) {
      f = 0.1F;
    }
    if ((!this.onGround) || (getHealth() <= 0.0F)) {
      f = 0.0F;
    }
    if ((this.onGround) || (getHealth() <= 0.0F)) {
      f1 = 0.0F;
    }
    this.bo += (f - this.bo) * 0.4F;
    this.aF += (f1 - this.aF) * 0.8F;
    if ((getHealth() > 0.0F) && (!isSpectator()))
    {
      AxisAlignedBB axisalignedbb = null;
      if ((this.vehicle != null) && (!this.vehicle.dead)) {
        axisalignedbb = getBoundingBox().a(this.vehicle.getBoundingBox()).grow(1.0D, 0.0D, 1.0D);
      } else {
        axisalignedbb = getBoundingBox().grow(1.0D, 0.5D, 1.0D);
      }
      List list = this.world.getEntities(this, axisalignedbb);
      if (ae()) {
        for (int i = 0; i < list.size(); i++)
        {
          Entity entity = (Entity)list.get(i);
          if (!entity.dead) {
            d(entity);
          }
        }
      }
    }
  }
  
  private void d(Entity entity)
  {
    entity.d(this);
  }
  
  public int getScore()
  {
    return this.datawatcher.getInt(18);
  }
  
  public void setScore(int i)
  {
    this.datawatcher.watch(18, Integer.valueOf(i));
  }
  
  public void addScore(int i)
  {
    int j = getScore();
    
    this.datawatcher.watch(18, Integer.valueOf(j + i));
  }
  
  public void die(DamageSource damagesource)
  {
    super.die(damagesource);
    setSize(0.2F, 0.2F);
    setPosition(this.locX, this.locY, this.locZ);
    this.motY = 0.10000000149011612D;
    if (getName().equals("Notch")) {
      a(new ItemStack(Items.APPLE, 1), true, false);
    }
    if (!this.world.getGameRules().getBoolean("keepInventory")) {
      this.inventory.n();
    }
    if (damagesource != null)
    {
      this.motX = (-MathHelper.cos((this.aw + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
      this.motZ = (-MathHelper.sin((this.aw + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
    }
    else
    {
      this.motX = (this.motZ = 0.0D);
    }
    b(StatisticList.y);
    a(StatisticList.h);
  }
  
  protected String bo()
  {
    return "game.player.hurt";
  }
  
  protected String bp()
  {
    return "game.player.die";
  }
  
  public void b(Entity entity, int i)
  {
    addScore(i);
    
    Collection<ScoreboardScore> collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreboardCriteria.f, getName(), new ArrayList());
    if ((entity instanceof EntityHuman))
    {
      b(StatisticList.B);
      
      this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreboardCriteria.e, getName(), collection);
      collection.addAll(e(entity));
    }
    else
    {
      b(StatisticList.z);
    }
    Iterator iterator = collection.iterator();
    while (iterator.hasNext())
    {
      ScoreboardScore scoreboardscore = (ScoreboardScore)iterator.next();
      
      scoreboardscore.incrementScore();
    }
  }
  
  private Collection e(Entity entity)
  {
    ScoreboardTeam scoreboardteam = getScoreboard().getPlayerTeam(getName());
    if (scoreboardteam != null)
    {
      int i = scoreboardteam.l().b();
      if ((i >= 0) && (i < IScoreboardCriteria.i.length))
      {
        Iterator iterator = getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.i[i]).iterator();
        while (iterator.hasNext())
        {
          ScoreboardObjective scoreboardobjective = (ScoreboardObjective)iterator.next();
          ScoreboardScore scoreboardscore = getScoreboard().getPlayerScoreForObjective(entity.getName(), scoreboardobjective);
          
          scoreboardscore.incrementScore();
        }
      }
    }
    ScoreboardTeam scoreboardteam1 = getScoreboard().getPlayerTeam(entity.getName());
    if (scoreboardteam1 != null)
    {
      int j = scoreboardteam1.l().b();
      if ((j >= 0) && (j < IScoreboardCriteria.h.length)) {
        return getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.h[j]);
      }
    }
    return Lists.newArrayList();
  }
  
  public EntityItem a(boolean flag)
  {
    return a(this.inventory.splitStack(this.inventory.itemInHandIndex, (flag) && (this.inventory.getItemInHand() != null) ? this.inventory.getItemInHand().count : 1), false, true);
  }
  
  public EntityItem drop(ItemStack itemstack, boolean flag)
  {
    return a(itemstack, false, false);
  }
  
  public EntityItem a(ItemStack itemstack, boolean flag, boolean flag1)
  {
    if (itemstack == null) {
      return null;
    }
    if (itemstack.count == 0) {
      return null;
    }
    double d0 = this.locY - 0.30000001192092896D + getHeadHeight();
    EntityItem entityitem = new EntityItem(this.world, this.locX, d0, this.locZ, itemstack);
    
    entityitem.a(40);
    if (flag1) {
      entityitem.c(getName());
    }
    if (flag)
    {
      float f = this.random.nextFloat() * 0.5F;
      float f1 = this.random.nextFloat() * 3.1415927F * 2.0F;
      entityitem.motX = (-MathHelper.sin(f1) * f);
      entityitem.motZ = (MathHelper.cos(f1) * f);
      entityitem.motY = 0.20000000298023224D;
    }
    else
    {
      float f = 0.3F;
      entityitem.motX = (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
      entityitem.motZ = (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
      entityitem.motY = (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * f + 0.1F);
      float f1 = this.random.nextFloat() * 3.1415927F * 2.0F;
      f = 0.02F * this.random.nextFloat();
      entityitem.motX += Math.cos(f1) * f;
      entityitem.motY += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
      entityitem.motZ += Math.sin(f1) * f;
    }
    Player player = (Player)getBukkitEntity();
    CraftItem drop = new CraftItem(this.world.getServer(), entityitem);
    
    PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
    this.world.getServer().getPluginManager().callEvent(event);
    if (event.isCancelled())
    {
      org.bukkit.inventory.ItemStack cur = player.getInventory().getItemInHand();
      if ((flag1) && ((cur == null) || (cur.getAmount() == 0)))
      {
        player.getInventory().setItemInHand(drop.getItemStack());
      }
      else if ((flag1) && (cur.isSimilar(drop.getItemStack())) && (drop.getItemStack().getAmount() == 1))
      {
        cur.setAmount(cur.getAmount() + 1);
        player.getInventory().setItemInHand(cur);
      }
      else
      {
        player.getInventory().addItem(new org.bukkit.inventory.ItemStack[] { drop.getItemStack() });
      }
      return null;
    }
    a(entityitem);
    if (flag1) {
      b(StatisticList.v);
    }
    return entityitem;
  }
  
  protected void a(EntityItem entityitem)
  {
    this.world.addEntity(entityitem);
  }
  
  public float a(Block block)
  {
    float f = this.inventory.a(block);
    if (f > 1.0F)
    {
      int i = EnchantmentManager.getDigSpeedEnchantmentLevel(this);
      ItemStack itemstack = this.inventory.getItemInHand();
      if ((i > 0) && (itemstack != null)) {
        f += i * i + 1;
      }
    }
    if (hasEffect(MobEffectList.FASTER_DIG)) {
      f *= (1.0F + (getEffect(MobEffectList.FASTER_DIG).getAmplifier() + 1) * 0.2F);
    }
    if (hasEffect(MobEffectList.SLOWER_DIG))
    {
      float f1 = 1.0F;
      switch (getEffect(MobEffectList.SLOWER_DIG).getAmplifier())
      {
      case 0: 
        f1 = 0.3F;
        break;
      case 1: 
        f1 = 0.09F;
        break;
      case 2: 
        f1 = 0.0027F;
        break;
      case 3: 
      default: 
        f1 = 8.1E-4F;
      }
      f *= f1;
    }
    if ((a(Material.WATER)) && (!EnchantmentManager.j(this))) {
      f /= 5.0F;
    }
    if (!this.onGround) {
      f /= 5.0F;
    }
    return f;
  }
  
  public boolean b(Block block)
  {
    return this.inventory.b(block);
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    this.uniqueID = a(this.bH);
    NBTTagList nbttaglist = nbttagcompound.getList("Inventory", 10);
    
    this.inventory.b(nbttaglist);
    this.inventory.itemInHandIndex = nbttagcompound.getInt("SelectedItemSlot");
    this.sleeping = nbttagcompound.getBoolean("Sleeping");
    this.sleepTicks = nbttagcompound.getShort("SleepTimer");
    this.exp = nbttagcompound.getFloat("XpP");
    this.expLevel = nbttagcompound.getInt("XpLevel");
    this.expTotal = nbttagcompound.getInt("XpTotal");
    this.f = nbttagcompound.getInt("XpSeed");
    if (this.f == 0) {
      this.f = this.random.nextInt();
    }
    setScore(nbttagcompound.getInt("Score"));
    if (this.sleeping)
    {
      this.bx = new BlockPosition(this);
      a(true, true, false);
    }
    this.spawnWorld = nbttagcompound.getString("SpawnWorld");
    if ("".equals(this.spawnWorld)) {
      this.spawnWorld = ((org.bukkit.World)this.world.getServer().getWorlds().get(0)).getName();
    }
    if ((nbttagcompound.hasKeyOfType("SpawnX", 99)) && (nbttagcompound.hasKeyOfType("SpawnY", 99)) && (nbttagcompound.hasKeyOfType("SpawnZ", 99)))
    {
      this.c = new BlockPosition(nbttagcompound.getInt("SpawnX"), nbttagcompound.getInt("SpawnY"), nbttagcompound.getInt("SpawnZ"));
      this.d = nbttagcompound.getBoolean("SpawnForced");
    }
    this.foodData.a(nbttagcompound);
    this.abilities.b(nbttagcompound);
    if (nbttagcompound.hasKeyOfType("EnderItems", 9))
    {
      NBTTagList nbttaglist1 = nbttagcompound.getList("EnderItems", 10);
      
      this.enderChest.a(nbttaglist1);
    }
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.set("Inventory", this.inventory.a(new NBTTagList()));
    nbttagcompound.setInt("SelectedItemSlot", this.inventory.itemInHandIndex);
    nbttagcompound.setBoolean("Sleeping", this.sleeping);
    nbttagcompound.setShort("SleepTimer", (short)this.sleepTicks);
    nbttagcompound.setFloat("XpP", this.exp);
    nbttagcompound.setInt("XpLevel", this.expLevel);
    nbttagcompound.setInt("XpTotal", this.expTotal);
    nbttagcompound.setInt("XpSeed", this.f);
    nbttagcompound.setInt("Score", getScore());
    if (this.c != null)
    {
      nbttagcompound.setInt("SpawnX", this.c.getX());
      nbttagcompound.setInt("SpawnY", this.c.getY());
      nbttagcompound.setInt("SpawnZ", this.c.getZ());
      nbttagcompound.setBoolean("SpawnForced", this.d);
    }
    this.foodData.b(nbttagcompound);
    this.abilities.a(nbttagcompound);
    nbttagcompound.set("EnderItems", this.enderChest.h());
    ItemStack itemstack = this.inventory.getItemInHand();
    if ((itemstack != null) && (itemstack.getItem() != null)) {
      nbttagcompound.set("SelectedItem", itemstack.save(new NBTTagCompound()));
    }
    nbttagcompound.setString("SpawnWorld", this.spawnWorld);
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    if ((this.abilities.isInvulnerable) && (!damagesource.ignoresInvulnerability())) {
      return false;
    }
    this.ticksFarFromPlayer = 0;
    if (getHealth() <= 0.0F) {
      return false;
    }
    if ((isSleeping()) && (!this.world.isClientSide)) {
      a(true, true, false);
    }
    if (damagesource.r())
    {
      if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
        return false;
      }
      if (this.world.getDifficulty() == EnumDifficulty.EASY) {
        f = f / 2.0F + 1.0F;
      }
      if (this.world.getDifficulty() == EnumDifficulty.HARD) {
        f = f * 3.0F / 2.0F;
      }
    }
    Entity entity = damagesource.getEntity();
    if (((entity instanceof EntityArrow)) && (((EntityArrow)entity).shooter != null)) {
      entity = ((EntityArrow)entity).shooter;
    }
    return super.damageEntity(damagesource, f);
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    Team team;
    if ((entityhuman instanceof EntityPlayer))
    {
      EntityPlayer thatPlayer = (EntityPlayer)entityhuman;
      Team team = thatPlayer.getBukkitEntity().getScoreboard().getPlayerTeam(thatPlayer.getBukkitEntity());
      if ((team == null) || (team.allowFriendlyFire())) {
        return true;
      }
    }
    else
    {
      OfflinePlayer thisPlayer = entityhuman.world.getServer().getOfflinePlayer(entityhuman.getName());
      team = entityhuman.world.getServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(thisPlayer);
      if ((team == null) || (team.allowFriendlyFire())) {
        return true;
      }
    }
    if ((this instanceof EntityPlayer)) {
      return !team.hasPlayer(((EntityPlayer)this).getBukkitEntity());
    }
    return !team.hasPlayer(this.world.getServer().getOfflinePlayer(getName()));
  }
  
  protected void damageArmor(float f)
  {
    this.inventory.a(f);
  }
  
  public int br()
  {
    return this.inventory.m();
  }
  
  public float bY()
  {
    int i = 0;
    ItemStack[] aitemstack = this.inventory.armor;
    int j = aitemstack.length;
    for (int k = 0; k < j; k++)
    {
      ItemStack itemstack = aitemstack[k];
      if (itemstack != null) {
        i++;
      }
    }
    return i / this.inventory.armor.length;
  }
  
  protected boolean d(DamageSource damagesource, float f)
  {
    return super.d(damagesource, f);
  }
  
  public void openSign(TileEntitySign tileentitysign) {}
  
  public void a(CommandBlockListenerAbstract commandblocklistenerabstract) {}
  
  public void openTrade(IMerchant imerchant) {}
  
  public void openContainer(IInventory iinventory) {}
  
  public void openHorseInventory(EntityHorse entityhorse, IInventory iinventory) {}
  
  public void openTileEntity(ITileEntityContainer itileentitycontainer) {}
  
  public void openBook(ItemStack itemstack) {}
  
  public boolean u(Entity entity)
  {
    if (isSpectator())
    {
      if ((entity instanceof IInventory)) {
        openContainer((IInventory)entity);
      }
      return false;
    }
    ItemStack itemstack = bZ();
    ItemStack itemstack1 = itemstack != null ? itemstack.cloneItemStack() : null;
    if (!entity.e(this))
    {
      if ((itemstack != null) && ((entity instanceof EntityLiving)))
      {
        if (this.abilities.canInstantlyBuild) {
          itemstack = itemstack1;
        }
        if (itemstack.a(this, (EntityLiving)entity))
        {
          if ((itemstack.count == 0) && (!this.abilities.canInstantlyBuild)) {
            ca();
          }
          return true;
        }
      }
      return false;
    }
    if ((itemstack != null) && (itemstack == bZ())) {
      if ((itemstack.count <= 0) && (!this.abilities.canInstantlyBuild)) {
        ca();
      } else if ((itemstack.count < itemstack1.count) && (this.abilities.canInstantlyBuild)) {
        itemstack.count = itemstack1.count;
      }
    }
    return true;
  }
  
  public ItemStack bZ()
  {
    return this.inventory.getItemInHand();
  }
  
  public void ca()
  {
    this.inventory.setItem(this.inventory.itemInHandIndex, null);
  }
  
  public double am()
  {
    return -0.35D;
  }
  
  public void attack(Entity entity)
  {
    if ((entity.aD()) && 
      (!entity.l(this)))
    {
      float f = (float)getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
      byte b0 = 0;
      float f1 = 0.0F;
      if ((entity instanceof EntityLiving)) {
        f1 = EnchantmentManager.a(bA(), ((EntityLiving)entity).getMonsterType());
      } else {
        f1 = EnchantmentManager.a(bA(), EnumMonsterType.UNDEFINED);
      }
      int i = b0 + EnchantmentManager.a(this);
      if (isSprinting()) {
        i++;
      }
      if ((f > 0.0F) || (f1 > 0.0F))
      {
        boolean flag = (this.fallDistance > 0.0F) && (!this.onGround) && (!k_()) && (!V()) && (!hasEffect(MobEffectList.BLINDNESS)) && (this.vehicle == null) && ((entity instanceof EntityLiving));
        if ((flag) && (f > 0.0F)) {
          f *= 1.5F;
        }
        f += f1;
        boolean flag1 = false;
        int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);
        if (((entity instanceof EntityLiving)) && (j > 0) && (!entity.isBurning()))
        {
          EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(getBukkitEntity(), entity.getBukkitEntity(), 1);
          Bukkit.getPluginManager().callEvent(combustEvent);
          if (!combustEvent.isCancelled())
          {
            flag1 = true;
            entity.setOnFire(combustEvent.getDuration());
          }
        }
        double d0 = entity.motX;
        double d1 = entity.motY;
        double d2 = entity.motZ;
        boolean flag2 = entity.damageEntity(DamageSource.playerAttack(this), f);
        if (flag2)
        {
          if (i > 0)
          {
            entity.g(-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * i * 0.5F);
            this.motX *= 0.6D;
            this.motZ *= 0.6D;
            setSprinting(false);
          }
          if (((entity instanceof EntityPlayer)) && (entity.velocityChanged))
          {
            boolean cancelled = false;
            Player player = (Player)entity.getBukkitEntity();
            Vector velocity = new Vector(d0, d1, d2);
            
            PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity.clone());
            this.world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
              cancelled = true;
            } else if (!velocity.equals(event.getVelocity())) {
              player.setVelocity(velocity);
            }
            if (!cancelled)
            {
              ((EntityPlayer)entity).playerConnection.sendPacket(new PacketPlayOutEntityVelocity(entity));
              entity.velocityChanged = false;
              entity.motX = d0;
              entity.motY = d1;
              entity.motZ = d2;
            }
          }
          if (flag) {
            b(entity);
          }
          if (f1 > 0.0F) {
            c(entity);
          }
          if (f >= 18.0F) {
            b(AchievementList.F);
          }
          p(entity);
          if ((entity instanceof EntityLiving)) {
            EnchantmentManager.a((EntityLiving)entity, this);
          }
          EnchantmentManager.b(this, entity);
          ItemStack itemstack = bZ();
          Object object = entity;
          if ((entity instanceof EntityComplexPart))
          {
            IComplex icomplex = ((EntityComplexPart)entity).owner;
            if ((icomplex instanceof EntityLiving)) {
              object = (EntityLiving)icomplex;
            }
          }
          if ((itemstack != null) && ((object instanceof EntityLiving)))
          {
            itemstack.a((EntityLiving)object, this);
            if (itemstack.count == 0) {
              ca();
            }
          }
          if ((entity instanceof EntityLiving))
          {
            a(StatisticList.w, Math.round(f * 10.0F));
            if (j > 0)
            {
              EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(getBukkitEntity(), entity.getBukkitEntity(), j * 4);
              Bukkit.getPluginManager().callEvent(combustEvent);
              if (!combustEvent.isCancelled()) {
                entity.setOnFire(combustEvent.getDuration());
              }
            }
          }
          applyExhaustion(this.world.spigotConfig.combatExhaustion);
        }
        else if (flag1)
        {
          entity.extinguish();
        }
      }
    }
  }
  
  public void b(Entity entity) {}
  
  public void c(Entity entity) {}
  
  public void die()
  {
    super.die();
    this.defaultContainer.b(this);
    if (this.activeContainer != null) {
      this.activeContainer.b(this);
    }
  }
  
  public boolean inBlock()
  {
    return (!this.sleeping) && (super.inBlock());
  }
  
  public GameProfile getProfile()
  {
    return this.bH;
  }
  
  public EnumBedResult a(BlockPosition blockposition)
  {
    if (!this.world.isClientSide)
    {
      if ((isSleeping()) || (!isAlive())) {
        return EnumBedResult.OTHER_PROBLEM;
      }
      if (!this.world.worldProvider.d()) {
        return EnumBedResult.NOT_POSSIBLE_HERE;
      }
      if (this.world.w()) {
        return EnumBedResult.NOT_POSSIBLE_NOW;
      }
      if ((Math.abs(this.locX - blockposition.getX()) > 3.0D) || (Math.abs(this.locY - blockposition.getY()) > 2.0D) || (Math.abs(this.locZ - blockposition.getZ()) > 3.0D)) {
        return EnumBedResult.TOO_FAR_AWAY;
      }
      double d0 = 8.0D;
      double d1 = 5.0D;
      List list = this.world.a(EntityMonster.class, new AxisAlignedBB(blockposition.getX() - d0, blockposition.getY() - d1, blockposition.getZ() - d0, blockposition.getX() + d0, blockposition.getY() + d1, blockposition.getZ() + d0));
      if (!list.isEmpty()) {
        return EnumBedResult.NOT_SAFE;
      }
    }
    if (au()) {
      mount(null);
    }
    if ((getBukkitEntity() instanceof Player))
    {
      Player player = (Player)getBukkitEntity();
      org.bukkit.block.Block bed = this.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      
      PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
      this.world.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        return EnumBedResult.OTHER_PROBLEM;
      }
    }
    setSize(0.2F, 0.2F);
    if (this.world.isLoaded(blockposition))
    {
      EnumDirection enumdirection = (EnumDirection)this.world.getType(blockposition).get(BlockDirectional.FACING);
      float f = 0.5F;
      float f1 = 0.5F;
      switch (SyntheticClass_1.a[enumdirection.ordinal()])
      {
      case 1: 
        f1 = 0.9F;
        break;
      case 2: 
        f1 = 0.1F;
        break;
      case 3: 
        f = 0.1F;
        break;
      case 4: 
        f = 0.9F;
      }
      a(enumdirection);
      setPosition(blockposition.getX() + f, blockposition.getY() + 0.6875F, blockposition.getZ() + f1);
    }
    else
    {
      setPosition(blockposition.getX() + 0.5F, blockposition.getY() + 0.6875F, blockposition.getZ() + 0.5F);
    }
    this.sleeping = true;
    this.sleepTicks = 0;
    this.bx = blockposition;
    this.motX = (this.motZ = this.motY = 0.0D);
    if (!this.world.isClientSide) {
      this.world.everyoneSleeping();
    }
    return EnumBedResult.OK;
  }
  
  private void a(EnumDirection enumdirection)
  {
    this.by = 0.0F;
    this.bz = 0.0F;
    switch (SyntheticClass_1.a[enumdirection.ordinal()])
    {
    case 1: 
      this.bz = -1.8F;
      break;
    case 2: 
      this.bz = 1.8F;
      break;
    case 3: 
      this.by = 1.8F;
      break;
    case 4: 
      this.by = -1.8F;
    }
  }
  
  public void a(boolean flag, boolean flag1, boolean flag2)
  {
    setSize(0.6F, 1.8F);
    IBlockData iblockdata = this.world.getType(this.bx);
    if ((this.bx != null) && (iblockdata.getBlock() == Blocks.BED))
    {
      this.world.setTypeAndData(this.bx, iblockdata.set(BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
      BlockPosition blockposition = BlockBed.a(this.world, this.bx, 0);
      if (blockposition == null) {
        blockposition = this.bx.up();
      }
      setPosition(blockposition.getX() + 0.5F, blockposition.getY() + 0.1F, blockposition.getZ() + 0.5F);
    }
    this.sleeping = false;
    if ((!this.world.isClientSide) && (flag1)) {
      this.world.everyoneSleeping();
    }
    if ((getBukkitEntity() instanceof Player))
    {
      Player player = (Player)getBukkitEntity();
      
      BlockPosition blockposition = this.bx;
      org.bukkit.block.Block bed;
      org.bukkit.block.Block bed;
      if (blockposition != null) {
        bed = this.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
      } else {
        bed = this.world.getWorld().getBlockAt(player.getLocation());
      }
      PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
      this.world.getServer().getPluginManager().callEvent(event);
    }
    this.sleepTicks = (flag ? 0 : 100);
    if (flag2) {
      setRespawnPosition(this.bx, false);
    }
  }
  
  private boolean p()
  {
    return this.world.getType(this.bx).getBlock() == Blocks.BED;
  }
  
  public static BlockPosition getBed(World world, BlockPosition blockposition, boolean flag)
  {
    ((ChunkProviderServer)world.chunkProvider).getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    Block block = world.getType(blockposition).getBlock();
    if (block != Blocks.BED)
    {
      if (!flag) {
        return null;
      }
      boolean flag1 = block.g();
      boolean flag2 = world.getType(blockposition.up()).getBlock().g();
      
      return (flag1) && (flag2) ? blockposition : null;
    }
    return BlockBed.a(world, blockposition, 0);
  }
  
  public boolean isSleeping()
  {
    return this.sleeping;
  }
  
  public boolean isDeeplySleeping()
  {
    return (this.sleeping) && (this.sleepTicks >= 100);
  }
  
  public void b(IChatBaseComponent ichatbasecomponent) {}
  
  public BlockPosition getBed()
  {
    return this.c;
  }
  
  public boolean isRespawnForced()
  {
    return this.d;
  }
  
  public void setRespawnPosition(BlockPosition blockposition, boolean flag)
  {
    if (blockposition != null)
    {
      this.c = blockposition;
      this.d = flag;
      this.spawnWorld = this.world.worldData.getName();
    }
    else
    {
      this.c = null;
      this.d = false;
      this.spawnWorld = "";
    }
  }
  
  public void b(Statistic statistic)
  {
    a(statistic, 1);
  }
  
  public void a(Statistic statistic, int i) {}
  
  public void a(Statistic statistic) {}
  
  public void bF()
  {
    super.bF();
    b(StatisticList.u);
    if (isSprinting()) {
      applyExhaustion(this.world.spigotConfig.sprintExhaustion);
    } else {
      applyExhaustion(this.world.spigotConfig.walkExhaustion);
    }
  }
  
  public void g(float f, float f1)
  {
    double d0 = this.locX;
    double d1 = this.locY;
    double d2 = this.locZ;
    if ((this.abilities.isFlying) && (this.vehicle == null))
    {
      double d3 = this.motY;
      float f2 = this.aM;
      
      this.aM = (this.abilities.a() * (isSprinting() ? 2 : 1));
      super.g(f, f1);
      this.motY = (d3 * 0.6D);
      this.aM = f2;
    }
    else
    {
      super.g(f, f1);
    }
    checkMovement(this.locX - d0, this.locY - d1, this.locZ - d2);
  }
  
  public float bI()
  {
    return (float)getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
  }
  
  public void checkMovement(double d0, double d1, double d2)
  {
    if (this.vehicle == null) {
      if (a(Material.WATER))
      {
        int i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
        if (i > 0)
        {
          a(StatisticList.p, i);
          applyExhaustion(0.015F * i * 0.01F);
        }
      }
      else if (V())
      {
        int i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
        if (i > 0)
        {
          a(StatisticList.l, i);
          applyExhaustion(0.015F * i * 0.01F);
        }
      }
      else if (k_())
      {
        if (d1 > 0.0D) {
          a(StatisticList.n, (int)Math.round(d1 * 100.0D));
        }
      }
      else if (this.onGround)
      {
        int i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
        if (i > 0)
        {
          a(StatisticList.i, i);
          if (isSprinting())
          {
            a(StatisticList.k, i);
            applyExhaustion(0.099999994F * i * 0.01F);
          }
          else
          {
            if (isSneaking()) {
              a(StatisticList.j, i);
            }
            applyExhaustion(0.01F * i * 0.01F);
          }
        }
      }
      else
      {
        int i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
        if (i > 25) {
          a(StatisticList.o, i);
        }
      }
    }
  }
  
  private void l(double d0, double d1, double d2)
  {
    if (this.vehicle != null)
    {
      int i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
      if (i > 0) {
        if ((this.vehicle instanceof EntityMinecartAbstract))
        {
          a(StatisticList.q, i);
          if (this.e == null) {
            this.e = new BlockPosition(this);
          } else if (this.e.c(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) >= 1000000.0D) {
            b(AchievementList.q);
          }
        }
        else if ((this.vehicle instanceof EntityBoat))
        {
          a(StatisticList.r, i);
        }
        else if ((this.vehicle instanceof EntityPig))
        {
          a(StatisticList.s, i);
        }
        else if ((this.vehicle instanceof EntityHorse))
        {
          a(StatisticList.t, i);
        }
      }
    }
  }
  
  public void e(float f, float f1)
  {
    if (!this.abilities.canFly)
    {
      if (f >= 2.0F) {
        a(StatisticList.m, (int)Math.round(f * 100.0D));
      }
      super.e(f, f1);
    }
  }
  
  protected void X()
  {
    if (!isSpectator()) {
      super.X();
    }
  }
  
  protected String n(int i)
  {
    return i > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
  }
  
  public void a(EntityLiving entityliving)
  {
    if ((entityliving instanceof IMonster)) {
      b(AchievementList.s);
    }
    EntityTypes.MonsterEggInfo entitytypes_monsteregginfo = (EntityTypes.MonsterEggInfo)EntityTypes.eggInfo.get(Integer.valueOf(EntityTypes.a(entityliving)));
    if (entitytypes_monsteregginfo != null) {
      b(entitytypes_monsteregginfo.killEntityStatistic);
    }
  }
  
  public void aA()
  {
    if (!this.abilities.isFlying) {
      super.aA();
    }
  }
  
  public ItemStack q(int i)
  {
    return this.inventory.e(i);
  }
  
  public void giveExp(int i)
  {
    addScore(i);
    int j = Integer.MAX_VALUE - this.expTotal;
    if (i > j) {
      i = j;
    }
    this.exp += i / getExpToLevel();
    for (this.expTotal += i; this.exp >= 1.0F; this.exp /= getExpToLevel())
    {
      this.exp = ((this.exp - 1.0F) * getExpToLevel());
      levelDown(1);
    }
  }
  
  public int cj()
  {
    return this.f;
  }
  
  public void b(int i)
  {
    this.expLevel -= i;
    if (this.expLevel < 0)
    {
      this.expLevel = 0;
      this.exp = 0.0F;
      this.expTotal = 0;
    }
    this.f = this.random.nextInt();
  }
  
  public void levelDown(int i)
  {
    this.expLevel += i;
    if (this.expLevel < 0)
    {
      this.expLevel = 0;
      this.exp = 0.0F;
      this.expTotal = 0;
    }
    if ((i > 0) && (this.expLevel % 5 == 0) && (this.i < this.ticksLived - 100.0F))
    {
      float f = this.expLevel > 30 ? 1.0F : this.expLevel / 30.0F;
      
      this.world.makeSound(this, "random.levelup", f * 0.75F, 1.0F);
      this.i = this.ticksLived;
    }
  }
  
  public int getExpToLevel()
  {
    return this.expLevel >= 15 ? 37 + (this.expLevel - 15) * 5 : this.expLevel >= 30 ? 112 + (this.expLevel - 30) * 9 : 7 + this.expLevel * 2;
  }
  
  public void applyExhaustion(float f)
  {
    if ((!this.abilities.isInvulnerable) && 
      (!this.world.isClientSide)) {
      this.foodData.a(f);
    }
  }
  
  public FoodMetaData getFoodData()
  {
    return this.foodData;
  }
  
  public boolean j(boolean flag)
  {
    return ((flag) || (this.foodData.c())) && (!this.abilities.isInvulnerable);
  }
  
  public boolean cm()
  {
    return (getHealth() > 0.0F) && (getHealth() < getMaxHealth());
  }
  
  public void a(ItemStack itemstack, int i)
  {
    if (itemstack != this.g)
    {
      this.g = itemstack;
      this.h = i;
      if (!this.world.isClientSide) {
        f(true);
      }
    }
  }
  
  public boolean cn()
  {
    return this.abilities.mayBuild;
  }
  
  public boolean a(BlockPosition blockposition, EnumDirection enumdirection, ItemStack itemstack)
  {
    if (this.abilities.mayBuild) {
      return true;
    }
    if (itemstack == null) {
      return false;
    }
    BlockPosition blockposition1 = blockposition.shift(enumdirection.opposite());
    Block block = this.world.getType(blockposition1).getBlock();
    
    return (itemstack.d(block)) || (itemstack.x());
  }
  
  protected int getExpValue(EntityHuman entityhuman)
  {
    if (this.world.getGameRules().getBoolean("keepInventory")) {
      return 0;
    }
    int i = this.expLevel * 7;
    
    return i > 100 ? 100 : i;
  }
  
  protected boolean alwaysGivesExp()
  {
    return true;
  }
  
  public void copyTo(EntityHuman entityhuman, boolean flag)
  {
    if (flag)
    {
      this.inventory.b(entityhuman.inventory);
      setHealth(entityhuman.getHealth());
      this.foodData = entityhuman.foodData;
      this.expLevel = entityhuman.expLevel;
      this.expTotal = entityhuman.expTotal;
      this.exp = entityhuman.exp;
      setScore(entityhuman.getScore());
      this.an = entityhuman.an;
      this.ao = entityhuman.ao;
      this.ap = entityhuman.ap;
    }
    else if (this.world.getGameRules().getBoolean("keepInventory"))
    {
      this.inventory.b(entityhuman.inventory);
      this.expLevel = entityhuman.expLevel;
      this.expTotal = entityhuman.expTotal;
      this.exp = entityhuman.exp;
      setScore(entityhuman.getScore());
    }
    this.f = entityhuman.f;
    this.enderChest = entityhuman.enderChest;
    getDataWatcher().watch(10, Byte.valueOf(entityhuman.getDataWatcher().getByte(10)));
  }
  
  protected boolean s_()
  {
    return !this.abilities.isFlying;
  }
  
  public void updateAbilities() {}
  
  public void a(WorldSettings.EnumGamemode worldsettings_enumgamemode) {}
  
  public String getName()
  {
    return this.bH.getName();
  }
  
  public InventoryEnderChest getEnderChest()
  {
    return this.enderChest;
  }
  
  public ItemStack getEquipment(int i)
  {
    return i == 0 ? this.inventory.getItemInHand() : this.inventory.armor[(i - 1)];
  }
  
  public ItemStack bA()
  {
    return this.inventory.getItemInHand();
  }
  
  public void setEquipment(int i, ItemStack itemstack)
  {
    this.inventory.armor[i] = itemstack;
  }
  
  public abstract boolean isSpectator();
  
  public ItemStack[] getEquipment()
  {
    return this.inventory.armor;
  }
  
  public boolean aL()
  {
    return !this.abilities.isFlying;
  }
  
  public Scoreboard getScoreboard()
  {
    return this.world.getScoreboard();
  }
  
  public ScoreboardTeamBase getScoreboardTeam()
  {
    return getScoreboard().getPlayerTeam(getName());
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    ChatComponentText chatcomponenttext = new ChatComponentText(ScoreboardTeam.getPlayerDisplayName(getScoreboardTeam(), getName()));
    
    chatcomponenttext.getChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/msg " + getName() + " "));
    chatcomponenttext.getChatModifier().setChatHoverable(aQ());
    chatcomponenttext.getChatModifier().setInsertion(getName());
    return chatcomponenttext;
  }
  
  public float getHeadHeight()
  {
    float f = 1.62F;
    if (isSleeping()) {
      f = 0.2F;
    }
    if (isSneaking()) {
      f -= 0.08F;
    }
    return f;
  }
  
  public void setAbsorptionHearts(float f)
  {
    if (f < 0.0F) {
      f = 0.0F;
    }
    getDataWatcher().watch(17, Float.valueOf(f));
  }
  
  public float getAbsorptionHearts()
  {
    return getDataWatcher().getFloat(17);
  }
  
  public static UUID a(GameProfile gameprofile)
  {
    UUID uuid = gameprofile.getId();
    if (uuid == null) {
      uuid = b(gameprofile.getName());
    }
    return uuid;
  }
  
  public static UUID b(String s)
  {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + s).getBytes(Charsets.UTF_8));
  }
  
  public boolean a(ChestLock chestlock)
  {
    if (chestlock.a()) {
      return true;
    }
    ItemStack itemstack = bZ();
    
    return (itemstack != null) && (itemstack.hasName()) ? itemstack.getName().equals(chestlock.b()) : false;
  }
  
  public boolean getSendCommandFeedback()
  {
    return MinecraftServer.getServer().worldServer[0].getGameRules().getBoolean("sendCommandFeedback");
  }
  
  public boolean d(int i, ItemStack itemstack)
  {
    if ((i >= 0) && (i < this.inventory.items.length))
    {
      this.inventory.setItem(i, itemstack);
      return true;
    }
    int j = i - 100;
    if ((j >= 0) && (j < this.inventory.armor.length))
    {
      int k = j + 1;
      if ((itemstack != null) && (itemstack.getItem() != null)) {
        if ((itemstack.getItem() instanceof ItemArmor))
        {
          if (EntityInsentient.c(itemstack) != k) {
            return false;
          }
        }
        else if ((k != 4) || ((itemstack.getItem() != Items.SKULL) && (!(itemstack.getItem() instanceof ItemBlock)))) {
          return false;
        }
      }
      this.inventory.setItem(j + this.inventory.items.length, itemstack);
      return true;
    }
    int k = i - 200;
    if ((k >= 0) && (k < this.enderChest.getSize()))
    {
      this.enderChest.setItem(k, itemstack);
      return true;
    }
    return false;
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumDirection.values().length];
    
    static
    {
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[EnumDirection.WEST.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[EnumDirection.EAST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
    }
  }
  
  public static enum EnumBedResult
  {
    OK,  NOT_POSSIBLE_HERE,  NOT_POSSIBLE_NOW,  TOO_FAR_AWAY,  OTHER_PROBLEM,  NOT_SAFE;
  }
  
  public static enum EnumChatVisibility
  {
    FULL(0, "options.chat.visibility.full"),  SYSTEM(1, "options.chat.visibility.system"),  HIDDEN(2, "options.chat.visibility.hidden");
    
    private static final EnumChatVisibility[] d;
    private final int e;
    private final String f;
    
    private EnumChatVisibility(int i, String s)
    {
      this.e = i;
      this.f = s;
    }
    
    public int a()
    {
      return this.e;
    }
    
    public static EnumChatVisibility a(int i)
    {
      return d[(i % d.length)];
    }
    
    static
    {
      d = new EnumChatVisibility[values().length];
      
      EnumChatVisibility[] aentityhuman_enumchatvisibility = values();
      int i = aentityhuman_enumchatvisibility.length;
      for (int j = 0; j < i; j++)
      {
        EnumChatVisibility entityhuman_enumchatvisibility = aentityhuman_enumchatvisibility[j];
        
        d[entityhuman_enumchatvisibility.e] = entityhuman_enumchatvisibility;
      }
    }
  }
}
