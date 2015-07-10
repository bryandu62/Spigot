package net.minecraft.server.v1_8_R3;

import java.util.Random;
import java.util.UUID;

public abstract class EntityTameableAnimal
  extends EntityAnimal
  implements EntityOwnable
{
  protected PathfinderGoalSit bm = new PathfinderGoalSit(this);
  
  public EntityTameableAnimal(World ☃)
  {
    super(☃);
    cm();
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, Byte.valueOf((byte)0));
    this.datawatcher.a(17, "");
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    if (getOwnerUUID() == null) {
      ☃.setString("OwnerUUID", "");
    } else {
      ☃.setString("OwnerUUID", getOwnerUUID());
    }
    ☃.setBoolean("Sitting", isSitting());
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    String ☃ = "";
    if (☃.hasKeyOfType("OwnerUUID", 8))
    {
      ☃ = ☃.getString("OwnerUUID");
    }
    else
    {
      String ☃ = ☃.getString("Owner");
      ☃ = NameReferencingFileConverter.a(☃);
    }
    if (☃.length() > 0)
    {
      setOwnerUUID(☃);
      setTamed(true);
    }
    this.bm.setSitting(☃.getBoolean("Sitting"));
    setSitting(☃.getBoolean("Sitting"));
  }
  
  protected void l(boolean ☃)
  {
    EnumParticle ☃ = EnumParticle.HEART;
    if (!☃) {
      ☃ = EnumParticle.SMOKE_NORMAL;
    }
    for (int ☃ = 0; ☃ < 7; ☃++)
    {
      double ☃ = this.random.nextGaussian() * 0.02D;
      double ☃ = this.random.nextGaussian() * 0.02D;
      double ☃ = this.random.nextGaussian() * 0.02D;
      this.world.addParticle(☃, this.locX + this.random.nextFloat() * this.width * 2.0F - this.width, this.locY + 0.5D + this.random.nextFloat() * this.length, this.locZ + this.random.nextFloat() * this.width * 2.0F - this.width, ☃, ☃, ☃, new int[0]);
    }
  }
  
  public boolean isTamed()
  {
    return (this.datawatcher.getByte(16) & 0x4) != 0;
  }
  
  public void setTamed(boolean ☃)
  {
    byte ☃ = this.datawatcher.getByte(16);
    if (☃) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(☃ | 0x4)));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)(☃ & 0xFFFFFFFB)));
    }
    cm();
  }
  
  protected void cm() {}
  
  public boolean isSitting()
  {
    return (this.datawatcher.getByte(16) & 0x1) != 0;
  }
  
  public void setSitting(boolean ☃)
  {
    byte ☃ = this.datawatcher.getByte(16);
    if (☃) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(☃ | 0x1)));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)(☃ & 0xFFFFFFFE)));
    }
  }
  
  public String getOwnerUUID()
  {
    return this.datawatcher.getString(17);
  }
  
  public void setOwnerUUID(String ☃)
  {
    this.datawatcher.watch(17, ☃);
  }
  
  public EntityLiving getOwner()
  {
    try
    {
      UUID ☃ = UUID.fromString(getOwnerUUID());
      if (☃ == null) {
        return null;
      }
      return this.world.b(☃);
    }
    catch (IllegalArgumentException ☃) {}
    return null;
  }
  
  public boolean e(EntityLiving ☃)
  {
    return ☃ == getOwner();
  }
  
  public PathfinderGoalSit getGoalSit()
  {
    return this.bm;
  }
  
  public boolean a(EntityLiving ☃, EntityLiving ☃)
  {
    return true;
  }
  
  public ScoreboardTeamBase getScoreboardTeam()
  {
    if (isTamed())
    {
      EntityLiving ☃ = getOwner();
      if (☃ != null) {
        return ☃.getScoreboardTeam();
      }
    }
    return super.getScoreboardTeam();
  }
  
  public boolean c(EntityLiving ☃)
  {
    if (isTamed())
    {
      EntityLiving ☃ = getOwner();
      if (☃ == ☃) {
        return true;
      }
      if (☃ != null) {
        return ☃.c(☃);
      }
    }
    return super.c(☃);
  }
  
  public void die(DamageSource ☃)
  {
    if ((!this.world.isClientSide) && (this.world.getGameRules().getBoolean("showDeathMessages")) && (hasCustomName()) && 
      ((getOwner() instanceof EntityPlayer))) {
      ((EntityPlayer)getOwner()).sendMessage(bs().b());
    }
    super.die(☃);
  }
}
