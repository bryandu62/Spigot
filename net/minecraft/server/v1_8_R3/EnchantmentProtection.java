package net.minecraft.server.v1_8_R3;

public class EnchantmentProtection
  extends Enchantment
{
  private static final String[] E = { "all", "fire", "fall", "explosion", "projectile" };
  private static final int[] F = { 1, 10, 5, 5, 3 };
  private static final int[] G = { 11, 8, 6, 8, 6 };
  private static final int[] H = { 20, 12, 10, 12, 15 };
  public final int a;
  
  public EnchantmentProtection(int ☃, MinecraftKey ☃, int ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.ARMOR);
    this.a = ☃;
    if (☃ == 2) {
      this.slot = EnchantmentSlotType.ARMOR_FEET;
    }
  }
  
  public int a(int ☃)
  {
    return F[this.a] + (☃ - 1) * G[this.a];
  }
  
  public int b(int ☃)
  {
    return a(☃) + H[this.a];
  }
  
  public int getMaxLevel()
  {
    return 4;
  }
  
  public int a(int ☃, DamageSource ☃)
  {
    if (☃.ignoresInvulnerability()) {
      return 0;
    }
    float ☃ = (6 + ☃ * ☃) / 3.0F;
    if (this.a == 0) {
      return MathHelper.d(☃ * 0.75F);
    }
    if ((this.a == 1) && (☃.o())) {
      return MathHelper.d(☃ * 1.25F);
    }
    if ((this.a == 2) && (☃ == DamageSource.FALL)) {
      return MathHelper.d(☃ * 2.5F);
    }
    if ((this.a == 3) && (☃.isExplosion())) {
      return MathHelper.d(☃ * 1.5F);
    }
    if ((this.a == 4) && (☃.a())) {
      return MathHelper.d(☃ * 1.5F);
    }
    return 0;
  }
  
  public String a()
  {
    return "enchantment.protect." + E[this.a];
  }
  
  public boolean a(Enchantment ☃)
  {
    if ((☃ instanceof EnchantmentProtection))
    {
      EnchantmentProtection ☃ = (EnchantmentProtection)☃;
      if (☃.a == this.a) {
        return false;
      }
      if ((this.a == 2) || (☃.a == 2)) {
        return true;
      }
      return false;
    }
    return super.a(☃);
  }
  
  public static int a(Entity ☃, int ☃)
  {
    int ☃ = EnchantmentManager.a(Enchantment.PROTECTION_FIRE.id, ☃.getEquipment());
    if (☃ > 0) {
      ☃ -= MathHelper.d(☃ * (☃ * 0.15F));
    }
    return ☃;
  }
  
  public static double a(Entity ☃, double ☃)
  {
    int ☃ = EnchantmentManager.a(Enchantment.PROTECTION_EXPLOSIONS.id, ☃.getEquipment());
    if (☃ > 0) {
      ☃ -= MathHelper.floor(☃ * (☃ * 0.15F));
    }
    return ☃;
  }
}
