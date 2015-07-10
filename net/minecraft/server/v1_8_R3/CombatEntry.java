package net.minecraft.server.v1_8_R3;

public class CombatEntry
{
  private final DamageSource a;
  private final int b;
  private final float c;
  private final float d;
  private final String e;
  private final float f;
  
  public CombatEntry(DamageSource ☃, int ☃, float ☃, float ☃, String ☃, float ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
    this.f = ☃;
  }
  
  public DamageSource a()
  {
    return this.a;
  }
  
  public float c()
  {
    return this.c;
  }
  
  public boolean f()
  {
    return this.a.getEntity() instanceof EntityLiving;
  }
  
  public String g()
  {
    return this.e;
  }
  
  public IChatBaseComponent h()
  {
    return a().getEntity() == null ? null : a().getEntity().getScoreboardDisplayName();
  }
  
  public float i()
  {
    if (this.a == DamageSource.OUT_OF_WORLD) {
      return Float.MAX_VALUE;
    }
    return this.f;
  }
}
