package net.minecraft.server.v1_8_R3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobEffect
{
  private static final Logger a = ;
  private int effectId;
  private int duration;
  private int amplification;
  private boolean splash;
  private boolean ambient;
  private boolean particles;
  
  public MobEffect(int ☃, int ☃)
  {
    this(☃, ☃, 0);
  }
  
  public MobEffect(int ☃, int ☃, int ☃)
  {
    this(☃, ☃, ☃, false, true);
  }
  
  public MobEffect(int ☃, int ☃, int ☃, boolean ☃, boolean ☃)
  {
    this.effectId = ☃;
    this.duration = ☃;
    this.amplification = ☃;
    this.ambient = ☃;
    this.particles = ☃;
  }
  
  public MobEffect(MobEffect ☃)
  {
    this.effectId = ☃.effectId;
    this.duration = ☃.duration;
    this.amplification = ☃.amplification;
    this.ambient = ☃.ambient;
    this.particles = ☃.particles;
  }
  
  public void a(MobEffect ☃)
  {
    if (this.effectId != ☃.effectId) {
      a.warn("This method should only be called for matching effects!");
    }
    if (☃.amplification > this.amplification)
    {
      this.amplification = ☃.amplification;
      this.duration = ☃.duration;
    }
    else if ((☃.amplification == this.amplification) && (this.duration < ☃.duration))
    {
      this.duration = ☃.duration;
    }
    else if ((!☃.ambient) && (this.ambient))
    {
      this.ambient = ☃.ambient;
    }
    this.particles = ☃.particles;
  }
  
  public int getEffectId()
  {
    return this.effectId;
  }
  
  public int getDuration()
  {
    return this.duration;
  }
  
  public int getAmplifier()
  {
    return this.amplification;
  }
  
  public void setSplash(boolean ☃)
  {
    this.splash = ☃;
  }
  
  public boolean isAmbient()
  {
    return this.ambient;
  }
  
  public boolean isShowParticles()
  {
    return this.particles;
  }
  
  public boolean tick(EntityLiving ☃)
  {
    if (this.duration > 0)
    {
      if (MobEffectList.byId[this.effectId].a(this.duration, this.amplification)) {
        b(☃);
      }
      i();
    }
    return this.duration > 0;
  }
  
  private int i()
  {
    return --this.duration;
  }
  
  public void b(EntityLiving ☃)
  {
    if (this.duration > 0) {
      MobEffectList.byId[this.effectId].tick(☃, this.amplification);
    }
  }
  
  public String g()
  {
    return MobEffectList.byId[this.effectId].a();
  }
  
  public int hashCode()
  {
    return this.effectId;
  }
  
  public String toString()
  {
    String ☃ = "";
    if (getAmplifier() > 0) {
      ☃ = g() + " x " + (getAmplifier() + 1) + ", Duration: " + getDuration();
    } else {
      ☃ = g() + ", Duration: " + getDuration();
    }
    if (this.splash) {
      ☃ = ☃ + ", Splash: true";
    }
    if (!this.particles) {
      ☃ = ☃ + ", Particles: false";
    }
    if (MobEffectList.byId[this.effectId].j()) {
      return "(" + ☃ + ")";
    }
    return ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (!(☃ instanceof MobEffect)) {
      return false;
    }
    MobEffect ☃ = (MobEffect)☃;
    return (this.effectId == ☃.effectId) && (this.amplification == ☃.amplification) && (this.duration == ☃.duration) && (this.splash == ☃.splash) && (this.ambient == ☃.ambient);
  }
  
  public NBTTagCompound a(NBTTagCompound ☃)
  {
    ☃.setByte("Id", (byte)getEffectId());
    ☃.setByte("Amplifier", (byte)getAmplifier());
    ☃.setInt("Duration", getDuration());
    ☃.setBoolean("Ambient", isAmbient());
    ☃.setBoolean("ShowParticles", isShowParticles());
    return ☃;
  }
  
  public static MobEffect b(NBTTagCompound ☃)
  {
    int ☃ = ☃.getByte("Id");
    if ((☃ < 0) || (☃ >= MobEffectList.byId.length) || (MobEffectList.byId[☃] == null)) {
      return null;
    }
    int ☃ = ☃.getByte("Amplifier");
    int ☃ = ☃.getInt("Duration");
    boolean ☃ = ☃.getBoolean("Ambient");
    boolean ☃ = true;
    if (☃.hasKeyOfType("ShowParticles", 1)) {
      ☃ = ☃.getBoolean("ShowParticles");
    }
    return new MobEffect(☃, ☃, ☃, ☃, ☃);
  }
}
