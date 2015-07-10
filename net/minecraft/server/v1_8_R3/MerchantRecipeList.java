package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;

public class MerchantRecipeList
  extends ArrayList<MerchantRecipe>
{
  public MerchantRecipeList() {}
  
  public MerchantRecipeList(NBTTagCompound ☃)
  {
    a(☃);
  }
  
  public MerchantRecipe a(ItemStack ☃, ItemStack ☃, int ☃)
  {
    if ((☃ > 0) && (☃ < size()))
    {
      MerchantRecipe ☃ = (MerchantRecipe)get(☃);
      if ((a(☃, ☃.getBuyItem1())) && (((☃ == null) && (!☃.hasSecondItem())) || ((☃.hasSecondItem()) && (a(☃, ☃.getBuyItem2())) && 
        (☃.count >= ☃.getBuyItem1().count) && ((!☃.hasSecondItem()) || (☃.count >= ☃.getBuyItem2().count))))) {
        return ☃;
      }
      return null;
    }
    for (int ☃ = 0; ☃ < size(); ☃++)
    {
      MerchantRecipe ☃ = (MerchantRecipe)get(☃);
      if ((a(☃, ☃.getBuyItem1())) && (☃.count >= ☃.getBuyItem1().count) && (((!☃.hasSecondItem()) && (☃ == null)) || ((☃.hasSecondItem()) && (a(☃, ☃.getBuyItem2())) && (☃.count >= ☃.getBuyItem2().count)))) {
        return ☃;
      }
    }
    return null;
  }
  
  private boolean a(ItemStack ☃, ItemStack ☃)
  {
    return (ItemStack.c(☃, ☃)) && ((!☃.hasTag()) || ((☃.hasTag()) && (GameProfileSerializer.a(☃.getTag(), ☃.getTag(), false))));
  }
  
  public void a(PacketDataSerializer ☃)
  {
    ☃.writeByte((byte)(size() & 0xFF));
    for (int ☃ = 0; ☃ < size(); ☃++)
    {
      MerchantRecipe ☃ = (MerchantRecipe)get(☃);
      ☃.a(☃.getBuyItem1());
      ☃.a(☃.getBuyItem3());
      
      ItemStack ☃ = ☃.getBuyItem2();
      ☃.writeBoolean(☃ != null);
      if (☃ != null) {
        ☃.a(☃);
      }
      ☃.writeBoolean(☃.h());
      ☃.writeInt(☃.e());
      ☃.writeInt(☃.f());
    }
  }
  
  public void a(NBTTagCompound ☃)
  {
    NBTTagList ☃ = ☃.getList("Recipes", 10);
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      NBTTagCompound ☃ = ☃.get(☃);
      add(new MerchantRecipe(☃));
    }
  }
  
  public NBTTagCompound a()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    
    NBTTagList ☃ = new NBTTagList();
    for (int ☃ = 0; ☃ < size(); ☃++)
    {
      MerchantRecipe ☃ = (MerchantRecipe)get(☃);
      ☃.add(☃.k());
    }
    ☃.set("Recipes", ☃);
    return ☃;
  }
}
