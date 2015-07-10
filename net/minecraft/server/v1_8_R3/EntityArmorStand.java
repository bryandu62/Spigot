package net.minecraft.server.v1_8_R3;

import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginManager;

public class EntityArmorStand
  extends EntityLiving
{
  private static final Vector3f a = new Vector3f(0.0F, 0.0F, 0.0F);
  private static final Vector3f b = new Vector3f(0.0F, 0.0F, 0.0F);
  private static final Vector3f c = new Vector3f(-10.0F, 0.0F, -10.0F);
  private static final Vector3f d = new Vector3f(-15.0F, 0.0F, 10.0F);
  private static final Vector3f e = new Vector3f(-1.0F, 0.0F, -1.0F);
  private static final Vector3f f = new Vector3f(1.0F, 0.0F, 1.0F);
  private final ItemStack[] items;
  private boolean h;
  private long i;
  private int bi;
  private boolean bj;
  public Vector3f headPose;
  public Vector3f bodyPose;
  public Vector3f leftArmPose;
  public Vector3f rightArmPose;
  public Vector3f leftLegPose;
  public Vector3f rightLegPose;
  
  public EntityArmorStand(World world)
  {
    super(world);
    this.items = new ItemStack[5];
    this.headPose = a;
    this.bodyPose = b;
    this.leftArmPose = c;
    this.rightArmPose = d;
    this.leftLegPose = e;
    this.rightLegPose = f;
    b(true);
    this.noclip = hasGravity();
    setSize(0.5F, 1.975F);
  }
  
  public EntityArmorStand(World world, double d0, double d1, double d2)
  {
    this(world);
    setPosition(d0, d1, d2);
  }
  
  public boolean bM()
  {
    return (super.bM()) && (!hasGravity());
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(10, Byte.valueOf((byte)0));
    this.datawatcher.a(11, a);
    this.datawatcher.a(12, b);
    this.datawatcher.a(13, c);
    this.datawatcher.a(14, d);
    this.datawatcher.a(15, e);
    this.datawatcher.a(16, f);
  }
  
  public ItemStack bA()
  {
    return this.items[0];
  }
  
  public ItemStack getEquipment(int i)
  {
    return this.items[i];
  }
  
  public void setEquipment(int i, ItemStack itemstack)
  {
    this.items[i] = itemstack;
  }
  
  public ItemStack[] getEquipment()
  {
    return this.items;
  }
  
  public boolean d(int i, ItemStack itemstack)
  {
    int j;
    int j;
    if (i == 99)
    {
      j = 0;
    }
    else
    {
      j = i - 100 + 1;
      if ((j < 0) || (j >= this.items.length)) {
        return false;
      }
    }
    if ((itemstack != null) && (EntityInsentient.c(itemstack) != j) && ((j != 4) || (!(itemstack.getItem() instanceof ItemBlock)))) {
      return false;
    }
    setEquipment(j, itemstack);
    return true;
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < this.items.length; i++)
    {
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      if (this.items[i] != null) {
        this.items[i].save(nbttagcompound1);
      }
      nbttaglist.add(nbttagcompound1);
    }
    nbttagcompound.set("Equipment", nbttaglist);
    if ((getCustomNameVisible()) && ((getCustomName() == null) || (getCustomName().length() == 0))) {
      nbttagcompound.setBoolean("CustomNameVisible", getCustomNameVisible());
    }
    nbttagcompound.setBoolean("Invisible", isInvisible());
    nbttagcompound.setBoolean("Small", isSmall());
    nbttagcompound.setBoolean("ShowArms", hasArms());
    nbttagcompound.setInt("DisabledSlots", this.bi);
    nbttagcompound.setBoolean("NoGravity", hasGravity());
    nbttagcompound.setBoolean("NoBasePlate", hasBasePlate());
    if (s()) {
      nbttagcompound.setBoolean("Marker", s());
    }
    nbttagcompound.set("Pose", z());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    if (nbttagcompound.hasKeyOfType("Equipment", 9))
    {
      NBTTagList nbttaglist = nbttagcompound.getList("Equipment", 10);
      for (int i = 0; i < this.items.length; i++) {
        this.items[i] = ItemStack.createStack(nbttaglist.get(i));
      }
    }
    setInvisible(nbttagcompound.getBoolean("Invisible"));
    setSmall(nbttagcompound.getBoolean("Small"));
    setArms(nbttagcompound.getBoolean("ShowArms"));
    this.bi = nbttagcompound.getInt("DisabledSlots");
    setGravity(nbttagcompound.getBoolean("NoGravity"));
    setBasePlate(nbttagcompound.getBoolean("NoBasePlate"));
    n(nbttagcompound.getBoolean("Marker"));
    this.bj = (!s());
    this.noclip = hasGravity();
    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Pose");
    
    h(nbttagcompound1);
  }
  
  private void h(NBTTagCompound nbttagcompound)
  {
    NBTTagList nbttaglist = nbttagcompound.getList("Head", 5);
    if (nbttaglist.size() > 0) {
      setHeadPose(new Vector3f(nbttaglist));
    } else {
      setHeadPose(a);
    }
    NBTTagList nbttaglist1 = nbttagcompound.getList("Body", 5);
    if (nbttaglist1.size() > 0) {
      setBodyPose(new Vector3f(nbttaglist1));
    } else {
      setBodyPose(b);
    }
    NBTTagList nbttaglist2 = nbttagcompound.getList("LeftArm", 5);
    if (nbttaglist2.size() > 0) {
      setLeftArmPose(new Vector3f(nbttaglist2));
    } else {
      setLeftArmPose(c);
    }
    NBTTagList nbttaglist3 = nbttagcompound.getList("RightArm", 5);
    if (nbttaglist3.size() > 0) {
      setRightArmPose(new Vector3f(nbttaglist3));
    } else {
      setRightArmPose(d);
    }
    NBTTagList nbttaglist4 = nbttagcompound.getList("LeftLeg", 5);
    if (nbttaglist4.size() > 0) {
      setLeftLegPose(new Vector3f(nbttaglist4));
    } else {
      setLeftLegPose(e);
    }
    NBTTagList nbttaglist5 = nbttagcompound.getList("RightLeg", 5);
    if (nbttaglist5.size() > 0) {
      setRightLegPose(new Vector3f(nbttaglist5));
    } else {
      setRightLegPose(f);
    }
  }
  
  private NBTTagCompound z()
  {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    if (!a.equals(this.headPose)) {
      nbttagcompound.set("Head", this.headPose.a());
    }
    if (!b.equals(this.bodyPose)) {
      nbttagcompound.set("Body", this.bodyPose.a());
    }
    if (!c.equals(this.leftArmPose)) {
      nbttagcompound.set("LeftArm", this.leftArmPose.a());
    }
    if (!d.equals(this.rightArmPose)) {
      nbttagcompound.set("RightArm", this.rightArmPose.a());
    }
    if (!e.equals(this.leftLegPose)) {
      nbttagcompound.set("LeftLeg", this.leftLegPose.a());
    }
    if (!f.equals(this.rightLegPose)) {
      nbttagcompound.set("RightLeg", this.rightLegPose.a());
    }
    return nbttagcompound;
  }
  
  public boolean ae()
  {
    return false;
  }
  
  protected void s(Entity entity) {}
  
  protected void bL()
  {
    List list = this.world.getEntities(this, getBoundingBox());
    if ((list != null) && (!list.isEmpty())) {
      for (int i = 0; i < list.size(); i++)
      {
        Entity entity = (Entity)list.get(i);
        if (((entity instanceof EntityMinecartAbstract)) && (((EntityMinecartAbstract)entity).s() == EntityMinecartAbstract.EnumMinecartType.RIDEABLE) && (h(entity) <= 0.2D)) {
          entity.collide(this);
        }
      }
    }
  }
  
  public boolean a(EntityHuman entityhuman, Vec3D vec3d)
  {
    if (s()) {
      return false;
    }
    if ((!this.world.isClientSide) && (!entityhuman.isSpectator()))
    {
      byte b0 = 0;
      ItemStack itemstack = entityhuman.bZ();
      boolean flag = itemstack != null;
      if ((flag) && ((itemstack.getItem() instanceof ItemArmor)))
      {
        ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
        if (itemarmor.b == 3) {
          b0 = 1;
        } else if (itemarmor.b == 2) {
          b0 = 2;
        } else if (itemarmor.b == 1) {
          b0 = 3;
        } else if (itemarmor.b == 0) {
          b0 = 4;
        }
      }
      if ((flag) && ((itemstack.getItem() == Items.SKULL) || (itemstack.getItem() == Item.getItemOf(Blocks.PUMPKIN)))) {
        b0 = 4;
      }
      byte b1 = 0;
      boolean flag1 = isSmall();
      double d4 = flag1 ? vec3d.b * 2.0D : vec3d.b;
      if (d4 >= 0.1D) {
        if ((d4 < 0.1D + (flag1 ? 0.8D : 0.45D)) && (this.items[1] != null))
        {
          b1 = 1;
          break label361;
        }
      }
      if (d4 >= 0.9D + (flag1 ? 0.3D : 0.0D)) {
        if ((d4 < 0.9D + (flag1 ? 1.0D : 0.7D)) && (this.items[3] != null))
        {
          b1 = 3;
          break label361;
        }
      }
      if (d4 >= 0.4D) {
        if ((d4 < 0.4D + (flag1 ? 1.0D : 0.8D)) && (this.items[2] != null))
        {
          b1 = 2;
          break label361;
        }
      }
      if ((d4 >= 1.6D) && (this.items[4] != null)) {
        b1 = 4;
      }
      label361:
      boolean flag2 = this.items[b1] != null;
      if (((this.bi & 1 << b1) != 0) || ((this.bi & 1 << b0) != 0))
      {
        b1 = b0;
        if ((this.bi & 1 << b0) != 0)
        {
          if ((this.bi & 0x1) != 0) {
            return true;
          }
          b1 = 0;
        }
      }
      if ((flag) && (b0 == 0) && (!hasArms())) {
        return true;
      }
      if (flag) {
        a(entityhuman, b0);
      } else if (flag2) {
        a(entityhuman, b1);
      }
      return true;
    }
    return true;
  }
  
  private void a(EntityHuman entityhuman, int i)
  {
    ItemStack itemstack = this.items[i];
    if (((itemstack == null) || ((this.bi & 1 << i + 8) == 0)) && (
      (itemstack != null) || ((this.bi & 1 << i + 16) == 0)))
    {
      int j = entityhuman.inventory.itemInHandIndex;
      ItemStack itemstack1 = entityhuman.inventory.getItem(j);
      
      org.bukkit.inventory.ItemStack armorStandItem = CraftItemStack.asCraftMirror(itemstack);
      org.bukkit.inventory.ItemStack playerHeldItem = CraftItemStack.asCraftMirror(itemstack1);
      
      Player player = (Player)entityhuman.getBukkitEntity();
      ArmorStand self = (ArmorStand)getBukkitEntity();
      
      EquipmentSlot slot = CraftEquipmentSlot.getSlot(i);
      PlayerArmorStandManipulateEvent armorStandManipulateEvent = new PlayerArmorStandManipulateEvent(player, self, playerHeldItem, armorStandItem, slot);
      this.world.getServer().getPluginManager().callEvent(armorStandManipulateEvent);
      if (armorStandManipulateEvent.isCancelled()) {
        return;
      }
      if ((entityhuman.abilities.canInstantlyBuild) && ((itemstack == null) || (itemstack.getItem() == Item.getItemOf(Blocks.AIR))) && (itemstack1 != null))
      {
        ItemStack itemstack2 = itemstack1.cloneItemStack();
        itemstack2.count = 1;
        setEquipment(i, itemstack2);
      }
      else if ((itemstack1 != null) && (itemstack1.count > 1))
      {
        if (itemstack == null)
        {
          ItemStack itemstack2 = itemstack1.cloneItemStack();
          itemstack2.count = 1;
          setEquipment(i, itemstack2);
          itemstack1.count -= 1;
        }
      }
      else
      {
        setEquipment(i, itemstack1);
        entityhuman.inventory.setItem(j, itemstack);
      }
    }
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
      return false;
    }
    if (this.world.isClientSide) {
      return false;
    }
    if (DamageSource.OUT_OF_WORLD.equals(damagesource))
    {
      die();
      return false;
    }
    if ((!isInvulnerable(damagesource)) && (!this.h) && (!s()))
    {
      if (damagesource.isExplosion())
      {
        D();
        die();
        return false;
      }
      if (DamageSource.FIRE.equals(damagesource))
      {
        if (!isBurning()) {
          setOnFire(5);
        } else {
          a(0.15F);
        }
        return false;
      }
      if ((DamageSource.BURN.equals(damagesource)) && (getHealth() > 0.5F))
      {
        a(4.0F);
        return false;
      }
      boolean flag = "arrow".equals(damagesource.p());
      boolean flag1 = "player".equals(damagesource.p());
      if ((!flag1) && (!flag)) {
        return false;
      }
      if ((damagesource.i() instanceof EntityArrow)) {
        damagesource.i().die();
      }
      if (((damagesource.getEntity() instanceof EntityHuman)) && (!((EntityHuman)damagesource.getEntity()).abilities.mayBuild)) {
        return false;
      }
      if (damagesource.u())
      {
        A();
        die();
        return false;
      }
      long i = this.world.getTime();
      if ((i - this.i > 5L) && (!flag))
      {
        this.i = i;
      }
      else
      {
        C();
        A();
        die();
      }
      return false;
    }
    return false;
  }
  
  private void A()
  {
    if ((this.world instanceof WorldServer)) {
      ((WorldServer)this.world).a(EnumParticle.BLOCK_DUST, this.locX, this.locY + this.length / 1.5D, this.locZ, 10, this.width / 4.0F, this.length / 4.0F, this.width / 4.0F, 0.05D, new int[] { Block.getCombinedId(Blocks.PLANKS.getBlockData()) });
    }
  }
  
  private void a(float f)
  {
    float f1 = getHealth();
    
    f1 -= f;
    if (f1 <= 0.5F)
    {
      D();
      die();
    }
    else
    {
      setHealth(f1);
    }
  }
  
  private void C()
  {
    Block.a(this.world, new BlockPosition(this), new ItemStack(Items.ARMOR_STAND));
    D();
  }
  
  private void D()
  {
    for (int i = 0; i < this.items.length; i++) {
      if ((this.items[i] != null) && (this.items[i].count > 0))
      {
        if (this.items[i] != null) {
          Block.a(this.world, new BlockPosition(this).up(), this.items[i]);
        }
        this.items[i] = null;
      }
    }
  }
  
  protected float h(float f, float f1)
  {
    this.aJ = this.lastYaw;
    this.aI = this.yaw;
    return 0.0F;
  }
  
  public float getHeadHeight()
  {
    return isBaby() ? this.length * 0.5F : this.length * 0.9F;
  }
  
  public void g(float f, float f1)
  {
    if (!hasGravity()) {
      super.g(f, f1);
    }
  }
  
  public void t_()
  {
    super.t_();
    Vector3f vector3f = this.datawatcher.h(11);
    if (!this.headPose.equals(vector3f)) {
      setHeadPose(vector3f);
    }
    Vector3f vector3f1 = this.datawatcher.h(12);
    if (!this.bodyPose.equals(vector3f1)) {
      setBodyPose(vector3f1);
    }
    Vector3f vector3f2 = this.datawatcher.h(13);
    if (!this.leftArmPose.equals(vector3f2)) {
      setLeftArmPose(vector3f2);
    }
    Vector3f vector3f3 = this.datawatcher.h(14);
    if (!this.rightArmPose.equals(vector3f3)) {
      setRightArmPose(vector3f3);
    }
    Vector3f vector3f4 = this.datawatcher.h(15);
    if (!this.leftLegPose.equals(vector3f4)) {
      setLeftLegPose(vector3f4);
    }
    Vector3f vector3f5 = this.datawatcher.h(16);
    if (!this.rightLegPose.equals(vector3f5)) {
      setRightLegPose(vector3f5);
    }
    boolean flag = s();
    if ((!this.bj) && (flag))
    {
      a(false);
    }
    else
    {
      if ((!this.bj) || (flag)) {
        return;
      }
      a(true);
    }
    this.bj = flag;
  }
  
  private void a(boolean flag)
  {
    double d0 = this.locX;
    double d1 = this.locY;
    double d2 = this.locZ;
    if (flag) {
      setSize(0.5F, 1.975F);
    } else {
      setSize(0.0F, 0.0F);
    }
    setPosition(d0, d1, d2);
  }
  
  protected void B()
  {
    setInvisible(this.h);
  }
  
  public void setInvisible(boolean flag)
  {
    this.h = flag;
    super.setInvisible(flag);
  }
  
  public boolean isBaby()
  {
    return isSmall();
  }
  
  public void G()
  {
    die();
  }
  
  public boolean aW()
  {
    return isInvisible();
  }
  
  public void setSmall(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(10);
    if (flag) {
      b0 = (byte)(b0 | 0x1);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFE);
    }
    this.datawatcher.watch(10, Byte.valueOf(b0));
  }
  
  public boolean isSmall()
  {
    return (this.datawatcher.getByte(10) & 0x1) != 0;
  }
  
  public void setGravity(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(10);
    if (flag) {
      b0 = (byte)(b0 | 0x2);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFD);
    }
    this.datawatcher.watch(10, Byte.valueOf(b0));
  }
  
  public boolean hasGravity()
  {
    return (this.datawatcher.getByte(10) & 0x2) != 0;
  }
  
  public void setArms(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(10);
    if (flag) {
      b0 = (byte)(b0 | 0x4);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFFB);
    }
    this.datawatcher.watch(10, Byte.valueOf(b0));
  }
  
  public boolean hasArms()
  {
    return (this.datawatcher.getByte(10) & 0x4) != 0;
  }
  
  public void setBasePlate(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(10);
    if (flag) {
      b0 = (byte)(b0 | 0x8);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFF7);
    }
    this.datawatcher.watch(10, Byte.valueOf(b0));
  }
  
  public boolean hasBasePlate()
  {
    return (this.datawatcher.getByte(10) & 0x8) != 0;
  }
  
  public void n(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(10);
    if (flag) {
      b0 = (byte)(b0 | 0x10);
    } else {
      b0 = (byte)(b0 & 0xFFFFFFEF);
    }
    this.datawatcher.watch(10, Byte.valueOf(b0));
  }
  
  public boolean s()
  {
    return (this.datawatcher.getByte(10) & 0x10) != 0;
  }
  
  public void setHeadPose(Vector3f vector3f)
  {
    this.headPose = vector3f;
    this.datawatcher.watch(11, vector3f);
  }
  
  public void setBodyPose(Vector3f vector3f)
  {
    this.bodyPose = vector3f;
    this.datawatcher.watch(12, vector3f);
  }
  
  public void setLeftArmPose(Vector3f vector3f)
  {
    this.leftArmPose = vector3f;
    this.datawatcher.watch(13, vector3f);
  }
  
  public void setRightArmPose(Vector3f vector3f)
  {
    this.rightArmPose = vector3f;
    this.datawatcher.watch(14, vector3f);
  }
  
  public void setLeftLegPose(Vector3f vector3f)
  {
    this.leftLegPose = vector3f;
    this.datawatcher.watch(15, vector3f);
  }
  
  public void setRightLegPose(Vector3f vector3f)
  {
    this.rightLegPose = vector3f;
    this.datawatcher.watch(16, vector3f);
  }
  
  public Vector3f t()
  {
    return this.headPose;
  }
  
  public Vector3f u()
  {
    return this.bodyPose;
  }
  
  public boolean ad()
  {
    return (super.ad()) && (!s());
  }
}
