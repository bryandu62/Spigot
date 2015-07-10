package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.HorseJumpEvent;

public class EntityHorse
  extends EntityAnimal
  implements IInventoryListener
{
  private static final Predicate<Entity> bs = new Predicate()
  {
    public boolean a(Entity entity)
    {
      return ((entity instanceof EntityHorse)) && (((EntityHorse)entity).cA());
    }
    
    public boolean apply(Object object)
    {
      return a((Entity)object);
    }
  };
  public static final IAttribute attributeJumpStrength = new AttributeRanged(null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D).a("Jump Strength").a(true);
  private static final String[] bu = { 0, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png" };
  private static final String[] bv = { "", "meo", "goo", "dio" };
  private static final int[] bw = { 0, 5, 7, 11 };
  private static final String[] bx = { "textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png" };
  private static final String[] by = { "hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb" };
  private static final String[] bz = { 0, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png" };
  private static final String[] bA = { "", "wo_", "wmo", "wdo", "bdo" };
  private int bB;
  private int bC;
  private int bD;
  public int bm;
  public int bo;
  protected boolean bp;
  public InventoryHorseChest inventoryChest;
  private boolean bF;
  protected int bq;
  protected float br;
  private boolean bG;
  private float bH;
  private float bI;
  private float bJ;
  private float bK;
  private float bL;
  private float bM;
  private int bN;
  private String bO;
  private String[] bP = new String[3];
  private boolean bQ = false;
  public int maxDomestication = 100;
  
  public EntityHorse(World world)
  {
    super(world);
    setSize(1.4F, 1.6F);
    this.fireProof = false;
    setHasChest(false);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.2D));
    this.goalSelector.a(1, new PathfinderGoalTame(this, 1.2D));
    this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
    this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.0D));
    this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.7D));
    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    loadChest();
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, Integer.valueOf(0));
    this.datawatcher.a(19, Byte.valueOf((byte)0));
    this.datawatcher.a(20, Integer.valueOf(0));
    this.datawatcher.a(21, String.valueOf(""));
    this.datawatcher.a(22, Integer.valueOf(0));
  }
  
  public void setType(int i)
  {
    this.datawatcher.watch(19, Byte.valueOf((byte)i));
    dc();
  }
  
  public int getType()
  {
    return this.datawatcher.getByte(19);
  }
  
  public void setVariant(int i)
  {
    this.datawatcher.watch(20, Integer.valueOf(i));
    dc();
  }
  
  public int getVariant()
  {
    return this.datawatcher.getInt(20);
  }
  
  public String getName()
  {
    if (hasCustomName()) {
      return getCustomName();
    }
    int i = getType();
    switch (i)
    {
    case 0: 
    default: 
      return LocaleI18n.get("entity.horse.name");
    case 1: 
      return LocaleI18n.get("entity.donkey.name");
    case 2: 
      return LocaleI18n.get("entity.mule.name");
    case 3: 
      return LocaleI18n.get("entity.zombiehorse.name");
    }
    return LocaleI18n.get("entity.skeletonhorse.name");
  }
  
  private boolean w(int i)
  {
    return (this.datawatcher.getInt(16) & i) != 0;
  }
  
  private void c(int i, boolean flag)
  {
    int j = this.datawatcher.getInt(16);
    if (flag) {
      this.datawatcher.watch(16, Integer.valueOf(j | i));
    } else {
      this.datawatcher.watch(16, Integer.valueOf(j & (i ^ 0xFFFFFFFF)));
    }
  }
  
  public boolean cn()
  {
    return !isBaby();
  }
  
  public boolean isTame()
  {
    return w(2);
  }
  
  public boolean cp()
  {
    return cn();
  }
  
  public String getOwnerUUID()
  {
    return this.datawatcher.getString(21);
  }
  
  public void setOwnerUUID(String s)
  {
    this.datawatcher.watch(21, s);
  }
  
  public float cu()
  {
    return 0.5F;
  }
  
  public void a(boolean flag)
  {
    if (flag) {
      a(cu());
    } else {
      a(1.0F);
    }
  }
  
  public boolean cv()
  {
    return this.bp;
  }
  
  public void setTame(boolean flag)
  {
    c(2, flag);
  }
  
  public void m(boolean flag)
  {
    this.bp = flag;
  }
  
  public boolean cb()
  {
    return (!cR()) && (super.cb());
  }
  
  protected void o(float f)
  {
    if ((f > 6.0F) && (cy())) {
      r(false);
    }
  }
  
  public boolean hasChest()
  {
    return w(8);
  }
  
  public int cx()
  {
    return this.datawatcher.getInt(22);
  }
  
  private int f(ItemStack itemstack)
  {
    if (itemstack == null) {
      return 0;
    }
    Item item = itemstack.getItem();
    
    return item == Items.DIAMOND_HORSE_ARMOR ? 3 : item == Items.GOLDEN_HORSE_ARMOR ? 2 : item == Items.IRON_HORSE_ARMOR ? 1 : 0;
  }
  
  public boolean cy()
  {
    return w(32);
  }
  
  public boolean cz()
  {
    return w(64);
  }
  
  public boolean cA()
  {
    return w(16);
  }
  
  public boolean cB()
  {
    return this.bF;
  }
  
  public void e(ItemStack itemstack)
  {
    this.datawatcher.watch(22, Integer.valueOf(f(itemstack)));
    dc();
  }
  
  public void n(boolean flag)
  {
    c(16, flag);
  }
  
  public void setHasChest(boolean flag)
  {
    c(8, flag);
  }
  
  public void p(boolean flag)
  {
    this.bF = flag;
  }
  
  public void q(boolean flag)
  {
    c(4, flag);
  }
  
  public int getTemper()
  {
    return this.bq;
  }
  
  public void setTemper(int i)
  {
    this.bq = i;
  }
  
  public int u(int i)
  {
    int j = MathHelper.clamp(getTemper() + i, 0, getMaxDomestication());
    
    setTemper(j);
    return j;
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    Entity entity = damagesource.getEntity();
    
    return (this.passenger != null) && (this.passenger.equals(entity)) ? false : super.damageEntity(damagesource, f);
  }
  
  public int br()
  {
    return bw[cx()];
  }
  
  public boolean ae()
  {
    return this.passenger == null;
  }
  
  public boolean cD()
  {
    int i = MathHelper.floor(this.locX);
    int j = MathHelper.floor(this.locZ);
    
    this.world.getBiome(new BlockPosition(i, 0, j));
    return true;
  }
  
  public void cE()
  {
    if ((!this.world.isClientSide) && (hasChest()))
    {
      a(Item.getItemOf(Blocks.CHEST), 1);
      setHasChest(false);
    }
  }
  
  private void cY()
  {
    df();
    if (!R()) {
      this.world.makeSound(this, "eating", 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
    }
  }
  
  public void e(float f, float f1)
  {
    if (f > 1.0F) {
      makeSound("mob.horse.land", 0.4F, 1.0F);
    }
    int i = MathHelper.f((f * 0.5F - 3.0F) * f1);
    if (i > 0)
    {
      damageEntity(DamageSource.FALL, i);
      if (this.passenger != null) {
        this.passenger.damageEntity(DamageSource.FALL, i);
      }
      Block block = this.world.getType(new BlockPosition(this.locX, this.locY - 0.2D - this.lastYaw, this.locZ)).getBlock();
      if ((block.getMaterial() != Material.AIR) && (!R()))
      {
        Block.StepSound block_stepsound = block.stepSound;
        
        this.world.makeSound(this, block_stepsound.getStepSound(), block_stepsound.getVolume1() * 0.5F, block_stepsound.getVolume2() * 0.75F);
      }
    }
  }
  
  private int cZ()
  {
    getType();
    
    return hasChest() ? 17 : 2;
  }
  
  public void loadChest()
  {
    InventoryHorseChest inventoryhorsechest = this.inventoryChest;
    
    this.inventoryChest = new InventoryHorseChest("HorseChest", cZ(), this);
    this.inventoryChest.a(getName());
    if (inventoryhorsechest != null)
    {
      inventoryhorsechest.b(this);
      int i = Math.min(inventoryhorsechest.getSize(), this.inventoryChest.getSize());
      for (int j = 0; j < i; j++)
      {
        ItemStack itemstack = inventoryhorsechest.getItem(j);
        if (itemstack != null) {
          this.inventoryChest.setItem(j, itemstack.cloneItemStack());
        }
      }
    }
    this.inventoryChest.a(this);
    db();
  }
  
  private void db()
  {
    if (!this.world.isClientSide)
    {
      q(this.inventoryChest.getItem(0) != null);
      if (cO()) {
        e(this.inventoryChest.getItem(1));
      }
    }
  }
  
  public void a(InventorySubcontainer inventorysubcontainer)
  {
    int i = cx();
    boolean flag = cG();
    
    db();
    if (this.ticksLived > 20)
    {
      if ((i == 0) && (i != cx())) {
        makeSound("mob.horse.armor", 0.5F, 1.0F);
      } else if (i != cx()) {
        makeSound("mob.horse.armor", 0.5F, 1.0F);
      }
      if ((!flag) && (cG())) {
        makeSound("mob.horse.leather", 0.5F, 1.0F);
      }
    }
  }
  
  public boolean bR()
  {
    cD();
    return super.bR();
  }
  
  protected EntityHorse a(Entity entity, double d0)
  {
    double d1 = Double.MAX_VALUE;
    Entity entity1 = null;
    List list = this.world.a(entity, entity.getBoundingBox().a(d0, d0, d0), bs);
    Iterator iterator = list.iterator();
    while (iterator.hasNext())
    {
      Entity entity2 = (Entity)iterator.next();
      double d2 = entity2.e(entity.locX, entity.locY, entity.locZ);
      if (d2 < d1)
      {
        entity1 = entity2;
        d1 = d2;
      }
    }
    return (EntityHorse)entity1;
  }
  
  public double getJumpStrength()
  {
    return getAttributeInstance(attributeJumpStrength).getValue();
  }
  
  protected String bp()
  {
    df();
    int i = getType();
    
    return (i != 1) && (i != 2) ? "mob.horse.death" : i == 4 ? "mob.horse.skeleton.death" : i == 3 ? "mob.horse.zombie.death" : "mob.horse.donkey.death";
  }
  
  protected Item getLoot()
  {
    boolean flag = this.random.nextInt(4) == 0;
    int i = getType();
    
    return i == 3 ? Items.ROTTEN_FLESH : flag ? null : i == 4 ? Items.BONE : Items.LEATHER;
  }
  
  protected String bo()
  {
    df();
    if (this.random.nextInt(3) == 0) {
      dh();
    }
    int i = getType();
    
    return (i != 1) && (i != 2) ? "mob.horse.hit" : i == 4 ? "mob.horse.skeleton.hit" : i == 3 ? "mob.horse.zombie.hit" : "mob.horse.donkey.hit";
  }
  
  public boolean cG()
  {
    return w(4);
  }
  
  protected String z()
  {
    df();
    if ((this.random.nextInt(10) == 0) && (!bD())) {
      dh();
    }
    int i = getType();
    
    return (i != 1) && (i != 2) ? "mob.horse.idle" : i == 4 ? "mob.horse.skeleton.idle" : i == 3 ? "mob.horse.zombie.idle" : "mob.horse.donkey.idle";
  }
  
  protected String cH()
  {
    df();
    dh();
    int i = getType();
    
    return (i != 3) && (i != 4) ? "mob.horse.donkey.angry" : (i != 1) && (i != 2) ? "mob.horse.angry" : null;
  }
  
  protected void a(BlockPosition blockposition, Block block)
  {
    Block.StepSound block_stepsound = block.stepSound;
    if (this.world.getType(blockposition.up()).getBlock() == Blocks.SNOW_LAYER) {
      block_stepsound = Blocks.SNOW_LAYER.stepSound;
    }
    if (!block.getMaterial().isLiquid())
    {
      int i = getType();
      if ((this.passenger != null) && (i != 1) && (i != 2))
      {
        this.bN += 1;
        if ((this.bN > 5) && (this.bN % 3 == 0))
        {
          makeSound("mob.horse.gallop", block_stepsound.getVolume1() * 0.15F, block_stepsound.getVolume2());
          if ((i == 0) && (this.random.nextInt(10) == 0)) {
            makeSound("mob.horse.breathe", block_stepsound.getVolume1() * 0.6F, block_stepsound.getVolume2());
          }
        }
        else if (this.bN <= 5)
        {
          makeSound("mob.horse.wood", block_stepsound.getVolume1() * 0.15F, block_stepsound.getVolume2());
        }
      }
      else if (block_stepsound == Block.f)
      {
        makeSound("mob.horse.wood", block_stepsound.getVolume1() * 0.15F, block_stepsound.getVolume2());
      }
      else
      {
        makeSound("mob.horse.soft", block_stepsound.getVolume1() * 0.15F, block_stepsound.getVolume2());
      }
    }
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeMap().b(attributeJumpStrength);
    getAttributeInstance(GenericAttributes.maxHealth).setValue(53.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.22499999403953552D);
  }
  
  public int bV()
  {
    return 6;
  }
  
  public int getMaxDomestication()
  {
    return this.maxDomestication;
  }
  
  protected float bB()
  {
    return 0.8F;
  }
  
  public int w()
  {
    return 400;
  }
  
  private void dc()
  {
    this.bO = null;
  }
  
  public void g(EntityHuman entityhuman)
  {
    if ((!this.world.isClientSide) && ((this.passenger == null) || (this.passenger == entityhuman)) && (isTame()))
    {
      this.inventoryChest.a(getName());
      entityhuman.openHorseInventory(this, this.inventoryChest);
    }
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    ItemStack itemstack = entityhuman.inventory.getItemInHand();
    if ((itemstack != null) && (itemstack.getItem() == Items.SPAWN_EGG)) {
      return super.a(entityhuman);
    }
    if ((!isTame()) && (cR())) {
      return false;
    }
    if ((isTame()) && (cn()) && (entityhuman.isSneaking()))
    {
      g(entityhuman);
      return true;
    }
    if ((cp()) && (this.passenger != null)) {
      return super.a(entityhuman);
    }
    if (itemstack != null)
    {
      boolean flag = false;
      if (cO())
      {
        byte b0 = -1;
        if (itemstack.getItem() == Items.IRON_HORSE_ARMOR) {
          b0 = 1;
        } else if (itemstack.getItem() == Items.GOLDEN_HORSE_ARMOR) {
          b0 = 2;
        } else if (itemstack.getItem() == Items.DIAMOND_HORSE_ARMOR) {
          b0 = 3;
        }
        if (b0 >= 0)
        {
          if (!isTame())
          {
            cW();
            return true;
          }
          g(entityhuman);
          return true;
        }
      }
      if ((!flag) && (!cR()))
      {
        float f = 0.0F;
        short short0 = 0;
        byte b1 = 0;
        if (itemstack.getItem() == Items.WHEAT)
        {
          f = 2.0F;
          short0 = 20;
          b1 = 3;
        }
        else if (itemstack.getItem() == Items.SUGAR)
        {
          f = 1.0F;
          short0 = 30;
          b1 = 3;
        }
        else if (Block.asBlock(itemstack.getItem()) == Blocks.HAY_BLOCK)
        {
          f = 20.0F;
          short0 = 180;
        }
        else if (itemstack.getItem() == Items.APPLE)
        {
          f = 3.0F;
          short0 = 60;
          b1 = 3;
        }
        else if (itemstack.getItem() == Items.GOLDEN_CARROT)
        {
          f = 4.0F;
          short0 = 60;
          b1 = 5;
          if ((isTame()) && (getAge() == 0))
          {
            flag = true;
            c(entityhuman);
          }
        }
        else if (itemstack.getItem() == Items.GOLDEN_APPLE)
        {
          f = 10.0F;
          short0 = 240;
          b1 = 10;
          if ((isTame()) && (getAge() == 0))
          {
            flag = true;
            c(entityhuman);
          }
        }
        if ((getHealth() < getMaxHealth()) && (f > 0.0F))
        {
          heal(f, EntityRegainHealthEvent.RegainReason.EATING);
          flag = true;
        }
        if ((!cn()) && (short0 > 0))
        {
          setAge(short0);
          flag = true;
        }
        if ((b1 > 0) && ((flag) || (!isTame())) && (b1 < getMaxDomestication()))
        {
          flag = true;
          u(b1);
        }
        if (flag) {
          cY();
        }
      }
      if ((!isTame()) && (!flag))
      {
        if ((itemstack != null) && (itemstack.a(entityhuman, this))) {
          return true;
        }
        cW();
        return true;
      }
      if ((!flag) && (cP()) && (!hasChest()) && (itemstack.getItem() == Item.getItemOf(Blocks.CHEST)))
      {
        setHasChest(true);
        makeSound("mob.chickenplop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        flag = true;
        loadChest();
      }
      if ((!flag) && (cp()) && (!cG()) && (itemstack.getItem() == Items.SADDLE))
      {
        g(entityhuman);
        return true;
      }
      if (flag)
      {
        if (!entityhuman.abilities.canInstantlyBuild) {
          if (--itemstack.count == 0) {
            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
          }
        }
        return true;
      }
    }
    if ((cp()) && (this.passenger == null))
    {
      if ((itemstack != null) && (itemstack.a(entityhuman, this))) {
        return true;
      }
      i(entityhuman);
      return true;
    }
    return super.a(entityhuman);
  }
  
  private void i(EntityHuman entityhuman)
  {
    entityhuman.yaw = this.yaw;
    entityhuman.pitch = this.pitch;
    r(false);
    s(false);
    if (!this.world.isClientSide) {
      entityhuman.mount(this);
    }
  }
  
  public boolean cO()
  {
    return getType() == 0;
  }
  
  public boolean cP()
  {
    int i = getType();
    
    return (i == 2) || (i == 1);
  }
  
  protected boolean bD()
  {
    return (this.passenger != null) && (cG());
  }
  
  public boolean cR()
  {
    int i = getType();
    
    return (i == 3) || (i == 4);
  }
  
  public boolean cS()
  {
    return (cR()) || (getType() == 2);
  }
  
  public boolean d(ItemStack itemstack)
  {
    return false;
  }
  
  private void de()
  {
    this.bm = 1;
  }
  
  public void die(DamageSource damagesource)
  {
    super.die(damagesource);
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    super.dropDeathLoot(flag, i);
    if (!this.world.isClientSide) {
      dropChest();
    }
  }
  
  public void m()
  {
    if (this.random.nextInt(200) == 0) {
      de();
    }
    super.m();
    if (!this.world.isClientSide)
    {
      if ((this.random.nextInt(900) == 0) && (this.deathTicks == 0)) {
        heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
      }
      if ((!cy()) && (this.passenger == null) && (this.random.nextInt(300) == 0) && (this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.locY) - 1, MathHelper.floor(this.locZ))).getBlock() == Blocks.GRASS)) {
        r(true);
      }
      if ((cy()) && (++this.bB > 50))
      {
        this.bB = 0;
        r(false);
      }
      if ((cA()) && (!cn()) && (!cy()))
      {
        EntityHorse entityhorse = a(this, 16.0D);
        if ((entityhorse != null) && (h(entityhorse) > 4.0D)) {
          this.navigation.a(entityhorse);
        }
      }
    }
  }
  
  public void t_()
  {
    super.t_();
    if ((this.world.isClientSide) && (this.datawatcher.a()))
    {
      this.datawatcher.e();
      dc();
    }
    if ((this.bC > 0) && (++this.bC > 30))
    {
      this.bC = 0;
      c(128, false);
    }
    if ((!this.world.isClientSide) && (this.bD > 0) && (++this.bD > 20))
    {
      this.bD = 0;
      s(false);
    }
    if ((this.bm > 0) && (++this.bm > 8)) {
      this.bm = 0;
    }
    if (this.bo > 0)
    {
      this.bo += 1;
      if (this.bo > 300) {
        this.bo = 0;
      }
    }
    this.bI = this.bH;
    if (cy())
    {
      this.bH += (1.0F - this.bH) * 0.4F + 0.05F;
      if (this.bH > 1.0F) {
        this.bH = 1.0F;
      }
    }
    else
    {
      this.bH += (0.0F - this.bH) * 0.4F - 0.05F;
      if (this.bH < 0.0F) {
        this.bH = 0.0F;
      }
    }
    this.bK = this.bJ;
    if (cz())
    {
      this.bI = (this.bH = 0.0F);
      this.bJ += (1.0F - this.bJ) * 0.4F + 0.05F;
      if (this.bJ > 1.0F) {
        this.bJ = 1.0F;
      }
    }
    else
    {
      this.bG = false;
      this.bJ += (0.8F * this.bJ * this.bJ * this.bJ - this.bJ) * 0.6F - 0.05F;
      if (this.bJ < 0.0F) {
        this.bJ = 0.0F;
      }
    }
    this.bM = this.bL;
    if (w(128))
    {
      this.bL += (1.0F - this.bL) * 0.7F + 0.05F;
      if (this.bL > 1.0F) {
        this.bL = 1.0F;
      }
    }
    else
    {
      this.bL += (0.0F - this.bL) * 0.7F - 0.05F;
      if (this.bL < 0.0F) {
        this.bL = 0.0F;
      }
    }
  }
  
  private void df()
  {
    if (!this.world.isClientSide)
    {
      this.bC = 1;
      c(128, true);
    }
  }
  
  private boolean dg()
  {
    return (this.passenger == null) && (this.vehicle == null) && (isTame()) && (cn()) && (!cS()) && (getHealth() >= getMaxHealth()) && (isInLove());
  }
  
  public void f(boolean flag)
  {
    c(32, flag);
  }
  
  public void r(boolean flag)
  {
    f(flag);
  }
  
  public void s(boolean flag)
  {
    if (flag) {
      r(false);
    }
    c(64, flag);
  }
  
  private void dh()
  {
    if (!this.world.isClientSide)
    {
      this.bD = 1;
      s(true);
    }
  }
  
  public void cW()
  {
    dh();
    String s = cH();
    if (s != null) {
      makeSound(s, bB(), bC());
    }
  }
  
  public void dropChest()
  {
    a(this, this.inventoryChest);
    cE();
  }
  
  private void a(Entity entity, InventoryHorseChest inventoryhorsechest)
  {
    if ((inventoryhorsechest != null) && (!this.world.isClientSide)) {
      for (int i = 0; i < inventoryhorsechest.getSize(); i++)
      {
        ItemStack itemstack = inventoryhorsechest.getItem(i);
        if (itemstack != null) {
          a(itemstack, 0.0F);
        }
      }
    }
  }
  
  public boolean h(EntityHuman entityhuman)
  {
    setOwnerUUID(entityhuman.getUniqueID().toString());
    setTame(true);
    return true;
  }
  
  public void g(float f, float f1)
  {
    if ((this.passenger != null) && ((this.passenger instanceof EntityLiving)) && (cG()))
    {
      this.lastYaw = (this.yaw = this.passenger.yaw);
      this.pitch = (this.passenger.pitch * 0.5F);
      setYawPitch(this.yaw, this.pitch);
      this.aK = (this.aI = this.yaw);
      f = ((EntityLiving)this.passenger).aZ * 0.5F;
      f1 = ((EntityLiving)this.passenger).ba;
      if (f1 <= 0.0F)
      {
        f1 *= 0.25F;
        this.bN = 0;
      }
      if ((this.onGround) && (this.br == 0.0F) && (cz()) && (!this.bG))
      {
        f = 0.0F;
        f1 = 0.0F;
      }
      if ((this.br > 0.0F) && (!cv()) && (this.onGround))
      {
        this.motY = (getJumpStrength() * this.br);
        if (hasEffect(MobEffectList.JUMP)) {
          this.motY += (getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F;
        }
        m(true);
        this.ai = true;
        if (f1 > 0.0F)
        {
          float f2 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F);
          float f3 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F);
          
          this.motX += -0.4F * f2 * this.br;
          this.motZ += 0.4F * f3 * this.br;
          makeSound("mob.horse.jump", 0.4F, 1.0F);
        }
        this.br = 0.0F;
      }
      this.S = 1.0F;
      this.aM = (bI() * 0.1F);
      if (!this.world.isClientSide)
      {
        k((float)getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
        super.g(f, f1);
      }
      if (this.onGround)
      {
        this.br = 0.0F;
        m(false);
      }
      this.aA = this.aB;
      double d0 = this.locX - this.lastX;
      double d1 = this.locZ - this.lastZ;
      float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
      if (f4 > 1.0F) {
        f4 = 1.0F;
      }
      this.aB += (f4 - this.aB) * 0.4F;
      this.aC += this.aB;
    }
    else
    {
      this.S = 0.5F;
      this.aM = 0.02F;
      super.g(f, f1);
    }
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setBoolean("EatingHaystack", cy());
    nbttagcompound.setBoolean("ChestedHorse", hasChest());
    nbttagcompound.setBoolean("HasReproduced", cB());
    nbttagcompound.setBoolean("Bred", cA());
    nbttagcompound.setInt("Type", getType());
    nbttagcompound.setInt("Variant", getVariant());
    nbttagcompound.setInt("Temper", getTemper());
    nbttagcompound.setBoolean("Tame", isTame());
    nbttagcompound.setString("OwnerUUID", getOwnerUUID());
    nbttagcompound.setInt("Bukkit.MaxDomestication", this.maxDomestication);
    if (hasChest())
    {
      NBTTagList nbttaglist = new NBTTagList();
      for (int i = 2; i < this.inventoryChest.getSize(); i++)
      {
        ItemStack itemstack = this.inventoryChest.getItem(i);
        if (itemstack != null)
        {
          NBTTagCompound nbttagcompound1 = new NBTTagCompound();
          
          nbttagcompound1.setByte("Slot", (byte)i);
          itemstack.save(nbttagcompound1);
          nbttaglist.add(nbttagcompound1);
        }
      }
      nbttagcompound.set("Items", nbttaglist);
    }
    if (this.inventoryChest.getItem(1) != null) {
      nbttagcompound.set("ArmorItem", this.inventoryChest.getItem(1).save(new NBTTagCompound()));
    }
    if (this.inventoryChest.getItem(0) != null) {
      nbttagcompound.set("SaddleItem", this.inventoryChest.getItem(0).save(new NBTTagCompound()));
    }
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    r(nbttagcompound.getBoolean("EatingHaystack"));
    n(nbttagcompound.getBoolean("Bred"));
    setHasChest(nbttagcompound.getBoolean("ChestedHorse"));
    p(nbttagcompound.getBoolean("HasReproduced"));
    setType(nbttagcompound.getInt("Type"));
    setVariant(nbttagcompound.getInt("Variant"));
    setTemper(nbttagcompound.getInt("Temper"));
    setTame(nbttagcompound.getBoolean("Tame"));
    String s = "";
    if (nbttagcompound.hasKeyOfType("OwnerUUID", 8))
    {
      s = nbttagcompound.getString("OwnerUUID");
    }
    else
    {
      String s1 = nbttagcompound.getString("Owner");
      if ((s1 == null) || (s1.isEmpty())) {
        if (nbttagcompound.hasKey("OwnerName"))
        {
          String owner = nbttagcompound.getString("OwnerName");
          if ((owner != null) && (!owner.isEmpty())) {
            s1 = owner;
          }
        }
      }
      s = NameReferencingFileConverter.a(s1);
    }
    if (s.length() > 0) {
      setOwnerUUID(s);
    }
    if (nbttagcompound.hasKey("Bukkit.MaxDomestication")) {
      this.maxDomestication = nbttagcompound.getInt("Bukkit.MaxDomestication");
    }
    AttributeInstance attributeinstance = getAttributeMap().a("Speed");
    if (attributeinstance != null) {
      getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(attributeinstance.b() * 0.25D);
    }
    if (hasChest())
    {
      NBTTagList nbttaglist = nbttagcompound.getList("Items", 10);
      
      loadChest();
      for (int i = 0; i < nbttaglist.size(); i++)
      {
        NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
        int j = nbttagcompound1.getByte("Slot") & 0xFF;
        if ((j >= 2) && (j < this.inventoryChest.getSize())) {
          this.inventoryChest.setItem(j, ItemStack.createStack(nbttagcompound1));
        }
      }
    }
    if (nbttagcompound.hasKeyOfType("ArmorItem", 10))
    {
      ItemStack itemstack = ItemStack.createStack(nbttagcompound.getCompound("ArmorItem"));
      if ((itemstack != null) && (a(itemstack.getItem()))) {
        this.inventoryChest.setItem(1, itemstack);
      }
    }
    if (nbttagcompound.hasKeyOfType("SaddleItem", 10))
    {
      ItemStack itemstack = ItemStack.createStack(nbttagcompound.getCompound("SaddleItem"));
      if ((itemstack != null) && (itemstack.getItem() == Items.SADDLE)) {
        this.inventoryChest.setItem(0, itemstack);
      }
    }
    else if (nbttagcompound.getBoolean("Saddle"))
    {
      this.inventoryChest.setItem(0, new ItemStack(Items.SADDLE));
    }
    db();
  }
  
  public boolean mate(EntityAnimal entityanimal)
  {
    if (entityanimal == this) {
      return false;
    }
    if (entityanimal.getClass() != getClass()) {
      return false;
    }
    EntityHorse entityhorse = (EntityHorse)entityanimal;
    if ((dg()) && (entityhorse.dg()))
    {
      int i = getType();
      int j = entityhorse.getType();
      
      return (i == j) || ((i == 0) && (j == 1)) || ((i == 1) && (j == 0));
    }
    return false;
  }
  
  public EntityAgeable createChild(EntityAgeable entityageable)
  {
    EntityHorse entityhorse = (EntityHorse)entityageable;
    EntityHorse entityhorse1 = new EntityHorse(this.world);
    int i = getType();
    int j = entityhorse.getType();
    int k = 0;
    if (i == j) {
      k = i;
    } else if (((i == 0) && (j == 1)) || ((i == 1) && (j == 0))) {
      k = 2;
    }
    if (k == 0)
    {
      int l = this.random.nextInt(9);
      int i1;
      int i1;
      if (l < 4)
      {
        i1 = getVariant() & 0xFF;
      }
      else
      {
        int i1;
        if (l < 8) {
          i1 = entityhorse.getVariant() & 0xFF;
        } else {
          i1 = this.random.nextInt(7);
        }
      }
      int j1 = this.random.nextInt(5);
      if (j1 < 2) {
        i1 |= getVariant() & 0xFF00;
      } else if (j1 < 4) {
        i1 |= entityhorse.getVariant() & 0xFF00;
      } else {
        i1 |= this.random.nextInt(5) << 8 & 0xFF00;
      }
      entityhorse1.setVariant(i1);
    }
    entityhorse1.setType(k);
    double d0 = getAttributeInstance(GenericAttributes.maxHealth).b() + entityageable.getAttributeInstance(GenericAttributes.maxHealth).b() + di();
    
    entityhorse1.getAttributeInstance(GenericAttributes.maxHealth).setValue(d0 / 3.0D);
    double d1 = getAttributeInstance(attributeJumpStrength).b() + entityageable.getAttributeInstance(attributeJumpStrength).b() + dj();
    
    entityhorse1.getAttributeInstance(attributeJumpStrength).setValue(d1 / 3.0D);
    double d2 = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).b() + entityageable.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).b() + dk();
    
    entityhorse1.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(d2 / 3.0D);
    return entityhorse1;
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, GroupDataEntity groupdataentity)
  {
    Object object = super.prepare(difficultydamagescaler, groupdataentity);
    
    int i = 0;
    int j;
    if ((object instanceof GroupDataHorse))
    {
      int j = ((GroupDataHorse)object).a;
      i = ((GroupDataHorse)object).b & 0xFF | this.random.nextInt(5) << 8;
    }
    else
    {
      int j;
      if (this.random.nextInt(10) == 0)
      {
        j = 1;
      }
      else
      {
        int k = this.random.nextInt(7);
        int l = this.random.nextInt(5);
        
        j = 0;
        i = k | l << 8;
      }
      object = new GroupDataHorse(j, i);
    }
    setType(j);
    setVariant(i);
    if (this.random.nextInt(5) == 0) {
      setAgeRaw(41536);
    }
    if ((j != 4) && (j != 3))
    {
      getAttributeInstance(GenericAttributes.maxHealth).setValue(di());
      if (j == 0) {
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(dk());
      } else {
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.17499999701976776D);
      }
    }
    else
    {
      getAttributeInstance(GenericAttributes.maxHealth).setValue(15.0D);
      getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
    }
    if ((j != 2) && (j != 1)) {
      getAttributeInstance(attributeJumpStrength).setValue(dj());
    } else {
      getAttributeInstance(attributeJumpStrength).setValue(0.5D);
    }
    setHealth(getMaxHealth());
    return (GroupDataEntity)object;
  }
  
  public void v(int i)
  {
    if (cG())
    {
      if (i < 0) {
        i = 0;
      }
      float power;
      float power;
      if (i >= 90) {
        power = 1.0F;
      } else {
        power = 0.4F + 0.4F * i / 90.0F;
      }
      HorseJumpEvent event = CraftEventFactory.callHorseJumpEvent(this, power);
      if (!event.isCancelled())
      {
        this.bG = true;
        dh();
        this.br = power;
      }
    }
  }
  
  public void al()
  {
    super.al();
    if (this.bK > 0.0F)
    {
      float f = MathHelper.sin(this.aI * 3.1415927F / 180.0F);
      float f1 = MathHelper.cos(this.aI * 3.1415927F / 180.0F);
      float f2 = 0.7F * this.bK;
      float f3 = 0.15F * this.bK;
      
      this.passenger.setPosition(this.locX + f2 * f, this.locY + an() + this.passenger.am() + f3, this.locZ - f2 * f1);
      if ((this.passenger instanceof EntityLiving)) {
        ((EntityLiving)this.passenger).aI = this.aI;
      }
    }
  }
  
  private float di()
  {
    return 15.0F + this.random.nextInt(8) + this.random.nextInt(9);
  }
  
  private double dj()
  {
    return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
  }
  
  private double dk()
  {
    return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
  }
  
  public static boolean a(Item item)
  {
    return (item == Items.IRON_HORSE_ARMOR) || (item == Items.GOLDEN_HORSE_ARMOR) || (item == Items.DIAMOND_HORSE_ARMOR);
  }
  
  public boolean k_()
  {
    return false;
  }
  
  public float getHeadHeight()
  {
    return this.length;
  }
  
  public boolean d(int i, ItemStack itemstack)
  {
    if ((i == 499) && (cP()))
    {
      if ((itemstack == null) && (hasChest()))
      {
        setHasChest(false);
        loadChest();
        return true;
      }
      if ((itemstack != null) && (itemstack.getItem() == Item.getItemOf(Blocks.CHEST)) && (!hasChest()))
      {
        setHasChest(true);
        loadChest();
        return true;
      }
    }
    int j = i - 400;
    if ((j >= 0) && (j < 2) && (j < this.inventoryChest.getSize()))
    {
      if ((j == 0) && (itemstack != null) && (itemstack.getItem() != Items.SADDLE)) {
        return false;
      }
      if ((j == 1) && (((itemstack != null) && (!a(itemstack.getItem()))) || (!cO()))) {
        return false;
      }
      this.inventoryChest.setItem(j, itemstack);
      db();
      return true;
    }
    int k = i - 500 + 2;
    if ((k >= 2) && (k < this.inventoryChest.getSize()))
    {
      this.inventoryChest.setItem(k, itemstack);
      return true;
    }
    return false;
  }
  
  public static class GroupDataHorse
    implements GroupDataEntity
  {
    public int a;
    public int b;
    
    public GroupDataHorse(int i, int j)
    {
      this.a = i;
      this.b = j;
    }
  }
}
