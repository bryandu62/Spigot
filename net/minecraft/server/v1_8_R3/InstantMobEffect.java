package net.minecraft.server.v1_8_R3;

public class InstantMobEffect
  extends MobEffectList
{
  public InstantMobEffect(int ☃, MinecraftKey ☃, boolean ☃, int ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public boolean isInstant()
  {
    return true;
  }
  
  public boolean a(int ☃, int ☃)
  {
    return ☃ >= 1;
  }
}
