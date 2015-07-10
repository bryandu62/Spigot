package net.minecraft.server.v1_8_R3;

public class MobEffectAbsorption
  extends MobEffectList
{
  protected MobEffectAbsorption(int ☃, MinecraftKey ☃, boolean ☃, int ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public void a(EntityLiving ☃, AttributeMapBase ☃, int ☃)
  {
    ☃.setAbsorptionHearts(☃.getAbsorptionHearts() - 4 * (☃ + 1));
    super.a(☃, ☃, ☃);
  }
  
  public void b(EntityLiving ☃, AttributeMapBase ☃, int ☃)
  {
    ☃.setAbsorptionHearts(☃.getAbsorptionHearts() + 4 * (☃ + 1));
    super.b(☃, ☃, ☃);
  }
}
