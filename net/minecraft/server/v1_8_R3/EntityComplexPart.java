package net.minecraft.server.v1_8_R3;

public class EntityComplexPart
  extends Entity
{
  public final IComplex owner;
  public final String b;
  
  public EntityComplexPart(IComplex ☃, String ☃, float ☃, float ☃)
  {
    super(☃.a());
    setSize(☃, ☃);
    this.owner = ☃;
    this.b = ☃;
  }
  
  protected void h() {}
  
  protected void a(NBTTagCompound ☃) {}
  
  protected void b(NBTTagCompound ☃) {}
  
  public boolean ad()
  {
    return true;
  }
  
  public boolean damageEntity(DamageSource ☃, float ☃)
  {
    if (isInvulnerable(☃)) {
      return false;
    }
    return this.owner.a(this, ☃, ☃);
  }
  
  public boolean k(Entity ☃)
  {
    return (this == ☃) || (this.owner == ☃);
  }
}
