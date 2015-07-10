package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class ItemEnchantedBook
  extends Item
{
  public boolean f_(ItemStack ☃)
  {
    return false;
  }
  
  public EnumItemRarity g(ItemStack ☃)
  {
    if (h(☃).size() > 0) {
      return EnumItemRarity.UNCOMMON;
    }
    return super.g(☃);
  }
  
  public NBTTagList h(ItemStack ☃)
  {
    NBTTagCompound ☃ = ☃.getTag();
    if ((☃ == null) || (!☃.hasKeyOfType("StoredEnchantments", 9))) {
      return new NBTTagList();
    }
    return (NBTTagList)☃.get("StoredEnchantments");
  }
  
  public void a(ItemStack ☃, WeightedRandomEnchant ☃)
  {
    NBTTagList ☃ = h(☃);
    boolean ☃ = true;
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      NBTTagCompound ☃ = ☃.get(☃);
      if (☃.getShort("id") == ☃.enchantment.id)
      {
        if (☃.getShort("lvl") < ☃.level) {
          ☃.setShort("lvl", (short)☃.level);
        }
        ☃ = false;
        break;
      }
    }
    if (☃)
    {
      NBTTagCompound ☃ = new NBTTagCompound();
      
      ☃.setShort("id", (short)☃.enchantment.id);
      ☃.setShort("lvl", (short)☃.level);
      
      ☃.add(☃);
    }
    if (!☃.hasTag()) {
      ☃.setTag(new NBTTagCompound());
    }
    ☃.getTag().set("StoredEnchantments", ☃);
  }
  
  public ItemStack a(WeightedRandomEnchant ☃)
  {
    ItemStack ☃ = new ItemStack(this);
    a(☃, ☃);
    return ☃;
  }
  
  public StructurePieceTreasure b(Random ☃)
  {
    return a(☃, 1, 1, 1);
  }
  
  public StructurePieceTreasure a(Random ☃, int ☃, int ☃, int ☃)
  {
    ItemStack ☃ = new ItemStack(Items.BOOK, 1, 0);
    EnchantmentManager.a(☃, ☃, 30);
    
    return new StructurePieceTreasure(☃, ☃, ☃, ☃);
  }
}
