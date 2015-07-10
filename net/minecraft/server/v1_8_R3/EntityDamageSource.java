package net.minecraft.server.v1_8_R3;

public class EntityDamageSource
  extends DamageSource
{
  protected Entity q;
  private boolean r = false;
  
  public EntityDamageSource(String ☃, Entity ☃)
  {
    super(☃);
    this.q = ☃;
  }
  
  public EntityDamageSource v()
  {
    this.r = true;
    return this;
  }
  
  public boolean w()
  {
    return this.r;
  }
  
  public Entity getEntity()
  {
    return this.q;
  }
  
  public IChatBaseComponent getLocalizedDeathMessage(EntityLiving ☃)
  {
    ItemStack ☃ = (this.q instanceof EntityLiving) ? ((EntityLiving)this.q).bA() : null;
    String ☃ = "death.attack." + this.translationIndex;
    String ☃ = ☃ + ".item";
    if ((☃ != null) && (☃.hasName()) && (LocaleI18n.c(☃))) {
      return new ChatMessage(☃, new Object[] { ☃.getScoreboardDisplayName(), this.q.getScoreboardDisplayName(), ☃.C() });
    }
    return new ChatMessage(☃, new Object[] { ☃.getScoreboardDisplayName(), this.q.getScoreboardDisplayName() });
  }
  
  public boolean r()
  {
    return (this.q != null) && ((this.q instanceof EntityLiving)) && (!(this.q instanceof EntityHuman));
  }
}
