package net.minecraft.server.v1_8_R3;

public class MobEffectHealthBoost
  extends MobEffectList
{
  public MobEffectHealthBoost(int ☃, MinecraftKey ☃, boolean ☃, int ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public void a(EntityLiving ☃, AttributeMapBase ☃, int ☃)
  {
    super.a(☃, ☃, ☃);
    if (☃.getHealth() > ☃.getMaxHealth()) {
      ☃.setHealth(☃.getMaxHealth());
    }
  }
}
