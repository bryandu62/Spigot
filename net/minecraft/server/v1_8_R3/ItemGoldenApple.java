package net.minecraft.server.v1_8_R3;

public class ItemGoldenApple
  extends ItemFood
{
  public ItemGoldenApple(int ☃, float ☃, boolean ☃)
  {
    super(☃, ☃, ☃);
    
    a(true);
  }
  
  public EnumItemRarity g(ItemStack ☃)
  {
    if (☃.getData() == 0) {
      return EnumItemRarity.RARE;
    }
    return EnumItemRarity.EPIC;
  }
  
  protected void c(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if (!☃.isClientSide) {
      ☃.addEffect(new MobEffect(MobEffectList.ABSORBTION.id, 2400, 0));
    }
    if (☃.getData() > 0)
    {
      if (!☃.isClientSide)
      {
        ☃.addEffect(new MobEffect(MobEffectList.REGENERATION.id, 600, 4));
        ☃.addEffect(new MobEffect(MobEffectList.RESISTANCE.id, 6000, 0));
        ☃.addEffect(new MobEffect(MobEffectList.FIRE_RESISTANCE.id, 6000, 0));
      }
    }
    else {
      super.c(☃, ☃, ☃);
    }
  }
}
