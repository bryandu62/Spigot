package net.minecraft.server.v1_8_R3;

public class DamageSource
{
  public static DamageSource FIRE = new DamageSource("inFire").setExplosion();
  public static DamageSource LIGHTNING = new DamageSource("lightningBolt");
  public static DamageSource BURN = new DamageSource("onFire").setIgnoreArmor().setExplosion();
  public static DamageSource LAVA = new DamageSource("lava").setExplosion();
  public static DamageSource STUCK = new DamageSource("inWall").setIgnoreArmor();
  public static DamageSource DROWN = new DamageSource("drown").setIgnoreArmor();
  public static DamageSource STARVE = new DamageSource("starve").setIgnoreArmor().m();
  public static DamageSource CACTUS = new DamageSource("cactus");
  public static DamageSource FALL = new DamageSource("fall").setIgnoreArmor();
  public static DamageSource OUT_OF_WORLD = new DamageSource("outOfWorld").setIgnoreArmor().l();
  public static DamageSource GENERIC = new DamageSource("generic").setIgnoreArmor();
  public static DamageSource MAGIC = new DamageSource("magic").setIgnoreArmor().setMagic();
  public static DamageSource WITHER = new DamageSource("wither").setIgnoreArmor();
  public static DamageSource ANVIL = new DamageSource("anvil");
  public static DamageSource FALLING_BLOCK = new DamageSource("fallingBlock");
  private boolean q;
  private boolean r;
  private boolean s;
  
  public static DamageSource mobAttack(EntityLiving ☃)
  {
    return new EntityDamageSource("mob", ☃);
  }
  
  public static DamageSource playerAttack(EntityHuman ☃)
  {
    return new EntityDamageSource("player", ☃);
  }
  
  public static DamageSource arrow(EntityArrow ☃, Entity ☃)
  {
    return new EntityDamageSourceIndirect("arrow", ☃, ☃).b();
  }
  
  public static DamageSource fireball(EntityFireball ☃, Entity ☃)
  {
    if (☃ == null) {
      return new EntityDamageSourceIndirect("onFire", ☃, ☃).setExplosion().b();
    }
    return new EntityDamageSourceIndirect("fireball", ☃, ☃).setExplosion().b();
  }
  
  public static DamageSource projectile(Entity ☃, Entity ☃)
  {
    return new EntityDamageSourceIndirect("thrown", ☃, ☃).b();
  }
  
  public static DamageSource b(Entity ☃, Entity ☃)
  {
    return new EntityDamageSourceIndirect("indirectMagic", ☃, ☃).setIgnoreArmor().setMagic();
  }
  
  public static DamageSource a(Entity ☃)
  {
    return new EntityDamageSource("thorns", ☃).v().setMagic();
  }
  
  public static DamageSource explosion(Explosion ☃)
  {
    if ((☃ != null) && (☃.c() != null)) {
      return new EntityDamageSource("explosion.player", ☃.c()).q().d();
    }
    return new DamageSource("explosion").q().d();
  }
  
  private float t = 0.3F;
  private boolean u;
  private boolean v;
  private boolean w;
  private boolean x;
  private boolean y;
  public String translationIndex;
  
  public boolean a()
  {
    return this.v;
  }
  
  public DamageSource b()
  {
    this.v = true;
    return this;
  }
  
  public boolean isExplosion()
  {
    return this.y;
  }
  
  public DamageSource d()
  {
    this.y = true;
    return this;
  }
  
  public boolean ignoresArmor()
  {
    return this.q;
  }
  
  public float getExhaustionCost()
  {
    return this.t;
  }
  
  public boolean ignoresInvulnerability()
  {
    return this.r;
  }
  
  public boolean isStarvation()
  {
    return this.s;
  }
  
  protected DamageSource(String ☃)
  {
    this.translationIndex = ☃;
  }
  
  public Entity i()
  {
    return getEntity();
  }
  
  public Entity getEntity()
  {
    return null;
  }
  
  protected DamageSource setIgnoreArmor()
  {
    this.q = true;
    
    this.t = 0.0F;
    return this;
  }
  
  protected DamageSource l()
  {
    this.r = true;
    return this;
  }
  
  protected DamageSource m()
  {
    this.s = true;
    
    this.t = 0.0F;
    return this;
  }
  
  protected DamageSource setExplosion()
  {
    this.u = true;
    return this;
  }
  
  public IChatBaseComponent getLocalizedDeathMessage(EntityLiving ☃)
  {
    EntityLiving ☃ = ☃.bt();
    String ☃ = "death.attack." + this.translationIndex;
    String ☃ = ☃ + ".player";
    if ((☃ != null) && (LocaleI18n.c(☃))) {
      return new ChatMessage(☃, new Object[] { ☃.getScoreboardDisplayName(), ☃.getScoreboardDisplayName() });
    }
    return new ChatMessage(☃, new Object[] { ☃.getScoreboardDisplayName() });
  }
  
  public boolean o()
  {
    return this.u;
  }
  
  public String p()
  {
    return this.translationIndex;
  }
  
  public DamageSource q()
  {
    this.w = true;
    return this;
  }
  
  public boolean r()
  {
    return this.w;
  }
  
  public boolean isMagic()
  {
    return this.x;
  }
  
  public DamageSource setMagic()
  {
    this.x = true;
    return this;
  }
  
  public boolean u()
  {
    Entity ☃ = getEntity();
    if (((☃ instanceof EntityHuman)) && (((EntityHuman)☃).abilities.canInstantlyBuild)) {
      return true;
    }
    return false;
  }
}
