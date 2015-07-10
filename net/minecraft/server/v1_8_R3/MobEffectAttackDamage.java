package net.minecraft.server.v1_8_R3;

public class MobEffectAttackDamage
  extends MobEffectList
{
  protected MobEffectAttackDamage(int ☃, MinecraftKey ☃, boolean ☃, int ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public double a(int ☃, AttributeModifier ☃)
  {
    if (this.id == MobEffectList.WEAKNESS.id) {
      return -0.5F * (☃ + 1);
    }
    return 1.3D * (☃ + 1);
  }
}
