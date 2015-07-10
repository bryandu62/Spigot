package net.minecraft.server.v1_8_R3;

public abstract interface IMerchant
{
  public abstract void a_(EntityHuman paramEntityHuman);
  
  public abstract EntityHuman v_();
  
  public abstract MerchantRecipeList getOffers(EntityHuman paramEntityHuman);
  
  public abstract void a(MerchantRecipe paramMerchantRecipe);
  
  public abstract void a_(ItemStack paramItemStack);
  
  public abstract IChatBaseComponent getScoreboardDisplayName();
}
