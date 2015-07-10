package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public abstract class EntityAgeable
  extends EntityCreature
{
  protected int a;
  protected int b;
  protected int c;
  private float bm = -1.0F;
  private float bn;
  public boolean ageLocked = false;
  
  public void inactiveTick()
  {
    super.inactiveTick();
    if ((this.world.isClientSide) || (this.ageLocked))
    {
      a(isBaby());
    }
    else
    {
      int i = getAge();
      if (i < 0)
      {
        i++;
        setAgeRaw(i);
      }
      else if (i > 0)
      {
        i--;
        setAgeRaw(i);
      }
    }
  }
  
  public EntityAgeable(World world)
  {
    super(world);
  }
  
  public abstract EntityAgeable createChild(EntityAgeable paramEntityAgeable);
  
  public boolean a(EntityHuman entityhuman)
  {
    ItemStack itemstack = entityhuman.inventory.getItemInHand();
    if ((itemstack != null) && (itemstack.getItem() == Items.SPAWN_EGG))
    {
      if (!this.world.isClientSide)
      {
        Class oclass = EntityTypes.a(itemstack.getData());
        if ((oclass != null) && (getClass() == oclass))
        {
          EntityAgeable entityageable = createChild(this);
          if (entityageable != null)
          {
            entityageable.setAgeRaw(41536);
            entityageable.setPositionRotation(this.locX, this.locY, this.locZ, 0.0F, 0.0F);
            this.world.addEntity(entityageable, CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
            if (itemstack.hasName()) {
              entityageable.setCustomName(itemstack.getName());
            }
            if (!entityhuman.abilities.canInstantlyBuild)
            {
              itemstack.count -= 1;
              if (itemstack.count == 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
              }
            }
          }
        }
      }
      return true;
    }
    return false;
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(12, Byte.valueOf((byte)0));
  }
  
  public int getAge()
  {
    return this.world.isClientSide ? this.datawatcher.getByte(12) : this.a;
  }
  
  public void setAge(int i, boolean flag)
  {
    int j = getAge();
    int k = j;
    
    j += i * 20;
    if (j > 0)
    {
      j = 0;
      if (k < 0) {
        n();
      }
    }
    int l = j - k;
    
    setAgeRaw(j);
    if (flag)
    {
      this.b += l;
      if (this.c == 0) {
        this.c = 40;
      }
    }
    if (getAge() == 0) {
      setAgeRaw(this.b);
    }
  }
  
  public void setAge(int i)
  {
    setAge(i, false);
  }
  
  public void setAgeRaw(int i)
  {
    this.datawatcher.watch(12, Byte.valueOf((byte)MathHelper.clamp(i, -1, 1)));
    this.a = i;
    a(isBaby());
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("Age", getAge());
    nbttagcompound.setInt("ForcedAge", this.b);
    nbttagcompound.setBoolean("AgeLocked", this.ageLocked);
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    setAgeRaw(nbttagcompound.getInt("Age"));
    this.b = nbttagcompound.getInt("ForcedAge");
    this.ageLocked = nbttagcompound.getBoolean("AgeLocked");
  }
  
  public void m()
  {
    super.m();
    if ((this.world.isClientSide) || (this.ageLocked))
    {
      if (this.c > 0)
      {
        if (this.c % 4 == 0) {
          this.world.addParticle(EnumParticle.VILLAGER_HAPPY, this.locX + this.random.nextFloat() * this.width * 2.0F - this.width, this.locY + 0.5D + this.random.nextFloat() * this.length, this.locZ + this.random.nextFloat() * this.width * 2.0F - this.width, 0.0D, 0.0D, 0.0D, new int[0]);
        }
        this.c -= 1;
      }
      a(isBaby());
    }
    else
    {
      int i = getAge();
      if (i < 0)
      {
        i++;
        setAgeRaw(i);
        if (i == 0) {
          n();
        }
      }
      else if (i > 0)
      {
        i--;
        setAgeRaw(i);
      }
    }
  }
  
  protected void n() {}
  
  public boolean isBaby()
  {
    return getAge() < 0;
  }
  
  public void a(boolean flag)
  {
    a(flag ? 0.5F : 1.0F);
  }
  
  public final void setSize(float f, float f1)
  {
    boolean flag = this.bm > 0.0F;
    
    this.bm = f;
    this.bn = f1;
    if (!flag) {
      a(1.0F);
    }
  }
  
  protected final void a(float f)
  {
    super.setSize(this.bm * f, this.bn * f);
  }
}
