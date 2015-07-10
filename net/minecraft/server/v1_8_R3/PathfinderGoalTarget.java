package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.apache.commons.lang3.StringUtils;

public abstract class PathfinderGoalTarget
  extends PathfinderGoal
{
  protected final EntityCreature e;
  protected boolean f;
  private boolean a;
  private int b;
  private int c;
  private int d;
  
  public PathfinderGoalTarget(EntityCreature ☃, boolean ☃)
  {
    this(☃, ☃, false);
  }
  
  public PathfinderGoalTarget(EntityCreature ☃, boolean ☃, boolean ☃)
  {
    this.e = ☃;
    this.f = ☃;
    this.a = ☃;
  }
  
  public boolean b()
  {
    EntityLiving ☃ = this.e.getGoalTarget();
    if (☃ == null) {
      return false;
    }
    if (!☃.isAlive()) {
      return false;
    }
    ScoreboardTeamBase ☃ = this.e.getScoreboardTeam();
    ScoreboardTeamBase ☃ = ☃.getScoreboardTeam();
    if ((☃ != null) && (☃ == ☃)) {
      return false;
    }
    double ☃ = f();
    if (this.e.h(☃) > ☃ * ☃) {
      return false;
    }
    if (this.f) {
      if (this.e.getEntitySenses().a(☃)) {
        this.d = 0;
      } else if (++this.d > 60) {
        return false;
      }
    }
    if (((☃ instanceof EntityHuman)) && 
      (((EntityHuman)☃).abilities.isInvulnerable)) {
      return false;
    }
    return true;
  }
  
  protected double f()
  {
    AttributeInstance ☃ = this.e.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
    return ☃ == null ? 16.0D : ☃.getValue();
  }
  
  public void c()
  {
    this.b = 0;
    this.c = 0;
    this.d = 0;
  }
  
  public void d()
  {
    this.e.setGoalTarget(null);
  }
  
  public static boolean a(EntityInsentient ☃, EntityLiving ☃, boolean ☃, boolean ☃)
  {
    if (☃ == null) {
      return false;
    }
    if (☃ == ☃) {
      return false;
    }
    if (!☃.isAlive()) {
      return false;
    }
    if (!☃.a(☃.getClass())) {
      return false;
    }
    ScoreboardTeamBase ☃ = ☃.getScoreboardTeam();
    ScoreboardTeamBase ☃ = ☃.getScoreboardTeam();
    if ((☃ != null) && (☃ == ☃)) {
      return false;
    }
    if (((☃ instanceof EntityOwnable)) && (StringUtils.isNotEmpty(((EntityOwnable)☃).getOwnerUUID())))
    {
      if (((☃ instanceof EntityOwnable)) && (((EntityOwnable)☃).getOwnerUUID().equals(((EntityOwnable)☃).getOwnerUUID()))) {
        return false;
      }
      if (☃ == ((EntityOwnable)☃).getOwner()) {
        return false;
      }
    }
    else if (((☃ instanceof EntityHuman)) && 
      (!☃) && (((EntityHuman)☃).abilities.isInvulnerable))
    {
      return false;
    }
    if ((☃) && (!☃.getEntitySenses().a(☃))) {
      return false;
    }
    return true;
  }
  
  protected boolean a(EntityLiving ☃, boolean ☃)
  {
    if (!a(this.e, ☃, ☃, this.f)) {
      return false;
    }
    if (!this.e.e(new BlockPosition(☃))) {
      return false;
    }
    if (this.a)
    {
      if (--this.c <= 0) {
        this.b = 0;
      }
      if (this.b == 0) {
        this.b = (a(☃) ? 1 : 2);
      }
      if (this.b == 2) {
        return false;
      }
    }
    return true;
  }
  
  private boolean a(EntityLiving ☃)
  {
    this.c = (10 + this.e.bc().nextInt(5));
    PathEntity ☃ = this.e.getNavigation().a(☃);
    if (☃ == null) {
      return false;
    }
    PathPoint ☃ = ☃.c();
    if (☃ == null) {
      return false;
    }
    int ☃ = ☃.a - MathHelper.floor(☃.locX);
    int ☃ = ☃.c - MathHelper.floor(☃.locZ);
    return ☃ * ☃ + ☃ * ☃ <= 2.25D;
  }
}
