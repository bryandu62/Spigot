package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EnchantmentWeaponDamage
  extends Enchantment
{
  private static final String[] E = { "all", "undead", "arthropods" };
  private static final int[] F = { 1, 5, 5 };
  private static final int[] G = { 11, 8, 8 };
  private static final int[] H = { 20, 20, 20 };
  public final int a;
  
  public EnchantmentWeaponDamage(int ☃, MinecraftKey ☃, int ☃, int ☃)
  {
    super(☃, ☃, ☃, EnchantmentSlotType.WEAPON);
    this.a = ☃;
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
    return 5;
  }
  
  public float a(int ☃, EnumMonsterType ☃)
  {
    if (this.a == 0) {
      return ☃ * 1.25F;
    }
    if ((this.a == 1) && (☃ == EnumMonsterType.UNDEAD)) {
      return ☃ * 2.5F;
    }
    if ((this.a == 2) && (☃ == EnumMonsterType.ARTHROPOD)) {
      return ☃ * 2.5F;
    }
    return 0.0F;
  }
  
  public String a()
  {
    return "enchantment.damage." + E[this.a];
  }
  
  public boolean a(Enchantment ☃)
  {
    return !(☃ instanceof EnchantmentWeaponDamage);
  }
  
  public boolean canEnchant(ItemStack ☃)
  {
    if ((☃.getItem() instanceof ItemAxe)) {
      return true;
    }
    return super.canEnchant(☃);
  }
  
  public void a(EntityLiving ☃, Entity ☃, int ☃)
  {
    if ((☃ instanceof EntityLiving))
    {
      EntityLiving ☃ = (EntityLiving)☃;
      if ((this.a == 2) && (☃.getMonsterType() == EnumMonsterType.ARTHROPOD))
      {
        int ☃ = 20 + ☃.bc().nextInt(10 * ☃);
        ☃.addEffect(new MobEffect(MobEffectList.SLOWER_MOVEMENT.id, ☃, 3));
      }
    }
  }
}
