package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginManager;

public class EntitySheep
  extends EntityAnimal
{
  private final InventoryCrafting bm = new InventoryCrafting(new Container()
  {
    public boolean a(EntityHuman entityhuman)
    {
      return false;
    }
    
    public InventoryView getBukkitView()
    {
      return null;
    }
  }, 
  
    2, 1);
  private static final Map<EnumColor, float[]> bo = Maps.newEnumMap(EnumColor.class);
  private int bp;
  private PathfinderGoalEatTile bq = new PathfinderGoalEatTile(this);
  
  public static float[] a(EnumColor enumcolor)
  {
    return (float[])bo.get(enumcolor);
  }
  
  public EntitySheep(World world)
  {
    super(world);
    setSize(0.9F, 1.3F);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
    this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
    this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.1D, Items.WHEAT, false));
    this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
    this.goalSelector.a(5, this.bq);
    this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    this.bm.setItem(0, new ItemStack(Items.DYE, 1, 0));
    this.bm.setItem(1, new ItemStack(Items.DYE, 1, 0));
    this.bm.resultInventory = new InventoryCraftResult();
  }
  
  protected void E()
  {
    this.bp = this.bq.f();
    super.E();
  }
  
  public void m()
  {
    if (this.world.isClientSide) {
      this.bp = Math.max(0, this.bp - 1);
    }
    super.m();
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D);
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, new Byte((byte)0));
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    if (!isSheared()) {
      a(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, getColor().getColorIndex()), 0.0F);
    }
    int j = this.random.nextInt(2) + 1 + this.random.nextInt(1 + i);
    for (int k = 0; k < j; k++) {
      if (isBurning()) {
        a(Items.COOKED_MUTTON, 1);
      } else {
        a(Items.MUTTON, 1);
      }
    }
  }
  
  protected Item getLoot()
  {
    return Item.getItemOf(Blocks.WOOL);
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    ItemStack itemstack = entityhuman.inventory.getItemInHand();
    if ((itemstack != null) && (itemstack.getItem() == Items.SHEARS) && (!isSheared()) && (!isBaby()))
    {
      if (!this.world.isClientSide)
      {
        PlayerShearEntityEvent event = new PlayerShearEntityEvent((Player)entityhuman.getBukkitEntity(), getBukkitEntity());
        this.world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
          return false;
        }
        setSheared(true);
        int i = 1 + this.random.nextInt(3);
        for (int j = 0; j < i; j++)
        {
          EntityItem entityitem = a(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, getColor().getColorIndex()), 1.0F);
          
          entityitem.motY += this.random.nextFloat() * 0.05F;
          entityitem.motX += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
          entityitem.motZ += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
        }
      }
      itemstack.damage(1, entityhuman);
      makeSound("mob.sheep.shear", 1.0F, 1.0F);
    }
    return super.a(entityhuman);
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setBoolean("Sheared", isSheared());
    nbttagcompound.setByte("Color", (byte)getColor().getColorIndex());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    setSheared(nbttagcompound.getBoolean("Sheared"));
    setColor(EnumColor.fromColorIndex(nbttagcompound.getByte("Color")));
  }
  
  protected String z()
  {
    return "mob.sheep.say";
  }
  
  protected String bo()
  {
    return "mob.sheep.say";
  }
  
  protected String bp()
  {
    return "mob.sheep.say";
  }
  
  protected void a(BlockPosition blockposition, Block block)
  {
    makeSound("mob.sheep.step", 0.15F, 1.0F);
  }
  
  public EnumColor getColor()
  {
    return EnumColor.fromColorIndex(this.datawatcher.getByte(16) & 0xF);
  }
  
  public void setColor(EnumColor enumcolor)
  {
    byte b0 = this.datawatcher.getByte(16);
    
    this.datawatcher.watch(16, Byte.valueOf((byte)(b0 & 0xF0 | enumcolor.getColorIndex() & 0xF)));
  }
  
  public boolean isSheared()
  {
    return (this.datawatcher.getByte(16) & 0x10) != 0;
  }
  
  public void setSheared(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(16);
    if (flag) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(b0 | 0x10)));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
    }
  }
  
  public static EnumColor a(Random random)
  {
    int i = random.nextInt(100);
    
    return random.nextInt(500) == 0 ? EnumColor.PINK : i < 18 ? EnumColor.BROWN : i < 15 ? EnumColor.SILVER : i < 10 ? EnumColor.GRAY : i < 5 ? EnumColor.BLACK : EnumColor.WHITE;
  }
  
  public EntitySheep b(EntityAgeable entityageable)
  {
    EntitySheep entitysheep = (EntitySheep)entityageable;
    EntitySheep entitysheep1 = new EntitySheep(this.world);
    
    entitysheep1.setColor(a(this, entitysheep));
    return entitysheep1;
  }
  
  public void v()
  {
    SheepRegrowWoolEvent event = new SheepRegrowWoolEvent((Sheep)getBukkitEntity());
    this.world.getServer().getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      setSheared(false);
    }
    if (isBaby()) {
      setAge(60);
    }
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, GroupDataEntity groupdataentity)
  {
    groupdataentity = super.prepare(difficultydamagescaler, groupdataentity);
    setColor(a(this.world.random));
    return groupdataentity;
  }
  
  private EnumColor a(EntityAnimal entityanimal, EntityAnimal entityanimal1)
  {
    int i = ((EntitySheep)entityanimal).getColor().getInvColorIndex();
    int j = ((EntitySheep)entityanimal1).getColor().getInvColorIndex();
    
    this.bm.getItem(0).setData(i);
    this.bm.getItem(1).setData(j);
    ItemStack itemstack = CraftingManager.getInstance().craft(this.bm, ((EntitySheep)entityanimal).world);
    int k;
    int k;
    if ((itemstack != null) && (itemstack.getItem() == Items.DYE)) {
      k = itemstack.getData();
    } else {
      k = this.world.random.nextBoolean() ? i : j;
    }
    return EnumColor.fromInvColorIndex(k);
  }
  
  public float getHeadHeight()
  {
    return 0.95F * this.length;
  }
  
  public EntityAgeable createChild(EntityAgeable entityageable)
  {
    return b(entityageable);
  }
  
  static
  {
    bo.put(EnumColor.WHITE, new float[] { 1.0F, 1.0F, 1.0F });
    bo.put(EnumColor.ORANGE, new float[] { 0.85F, 0.5F, 0.2F });
    bo.put(EnumColor.MAGENTA, new float[] { 0.7F, 0.3F, 0.85F });
    bo.put(EnumColor.LIGHT_BLUE, new float[] { 0.4F, 0.6F, 0.85F });
    bo.put(EnumColor.YELLOW, new float[] { 0.9F, 0.9F, 0.2F });
    bo.put(EnumColor.LIME, new float[] { 0.5F, 0.8F, 0.1F });
    bo.put(EnumColor.PINK, new float[] { 0.95F, 0.5F, 0.65F });
    bo.put(EnumColor.GRAY, new float[] { 0.3F, 0.3F, 0.3F });
    bo.put(EnumColor.SILVER, new float[] { 0.6F, 0.6F, 0.6F });
    bo.put(EnumColor.CYAN, new float[] { 0.3F, 0.5F, 0.6F });
    bo.put(EnumColor.PURPLE, new float[] { 0.5F, 0.25F, 0.7F });
    bo.put(EnumColor.BLUE, new float[] { 0.2F, 0.3F, 0.7F });
    bo.put(EnumColor.BROWN, new float[] { 0.4F, 0.3F, 0.2F });
    bo.put(EnumColor.GREEN, new float[] { 0.4F, 0.5F, 0.2F });
    bo.put(EnumColor.RED, new float[] { 0.6F, 0.2F, 0.2F });
    bo.put(EnumColor.BLACK, new float[] { 0.1F, 0.1F, 0.1F });
  }
}
