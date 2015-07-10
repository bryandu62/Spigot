package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class EntityCow
  extends EntityAnimal
{
  public EntityCow(World world)
  {
    super(world);
    setSize(0.9F, 1.3F);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a(1, new PathfinderGoalPanic(this, 2.0D));
    this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
    this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.25D, Items.WHEAT, false));
    this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.25D));
    this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
    this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
  }
  
  protected String z()
  {
    return "mob.cow.say";
  }
  
  protected String bo()
  {
    return "mob.cow.hurt";
  }
  
  protected String bp()
  {
    return "mob.cow.hurt";
  }
  
  protected void a(BlockPosition blockposition, Block block)
  {
    makeSound("mob.cow.step", 0.15F, 1.0F);
  }
  
  protected float bB()
  {
    return 0.4F;
  }
  
  protected Item getLoot()
  {
    return Items.LEATHER;
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    int j = this.random.nextInt(3) + this.random.nextInt(1 + i);
    for (int k = 0; k < j; k++) {
      a(Items.LEATHER, 1);
    }
    j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);
    for (k = 0; k < j; k++) {
      if (isBurning()) {
        a(Items.COOKED_BEEF, 1);
      } else {
        a(Items.BEEF, 1);
      }
    }
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    ItemStack itemstack = entityhuman.inventory.getItemInHand();
    if ((itemstack != null) && (itemstack.getItem() == Items.BUCKET) && (!entityhuman.abilities.canInstantlyBuild) && (!isBaby()))
    {
      Location loc = getBukkitEntity().getLocation();
      PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), null, itemstack, Items.MILK_BUCKET);
      if (event.isCancelled()) {
        return false;
      }
      ItemStack result = CraftItemStack.asNMSCopy(event.getItemStack());
      if (--itemstack.count <= 0) {
        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, result);
      } else if (!entityhuman.inventory.pickup(result)) {
        entityhuman.drop(result, false);
      }
      return true;
    }
    return super.a(entityhuman);
  }
  
  public EntityCow b(EntityAgeable entityageable)
  {
    return new EntityCow(this.world);
  }
  
  public float getHeadHeight()
  {
    return this.length;
  }
  
  public EntityAgeable createChild(EntityAgeable entityageable)
  {
    return b(entityageable);
  }
}
