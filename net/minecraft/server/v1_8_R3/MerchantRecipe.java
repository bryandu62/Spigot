package net.minecraft.server.v1_8_R3;

public class MerchantRecipe
{
  private ItemStack buyingItem1;
  private ItemStack buyingItem2;
  private ItemStack sellingItem;
  private int uses;
  private int maxUses;
  private boolean rewardExp;
  
  public MerchantRecipe(NBTTagCompound ☃)
  {
    a(☃);
  }
  
  public MerchantRecipe(ItemStack ☃, ItemStack ☃, ItemStack ☃)
  {
    this(☃, ☃, ☃, 0, 7);
  }
  
  public MerchantRecipe(ItemStack ☃, ItemStack ☃, ItemStack ☃, int ☃, int ☃)
  {
    this.buyingItem1 = ☃;
    this.buyingItem2 = ☃;
    this.sellingItem = ☃;
    this.uses = ☃;
    this.maxUses = ☃;
    this.rewardExp = true;
  }
  
  public MerchantRecipe(ItemStack ☃, ItemStack ☃)
  {
    this(☃, null, ☃);
  }
  
  public MerchantRecipe(ItemStack ☃, Item ☃)
  {
    this(☃, new ItemStack(☃));
  }
  
  public ItemStack getBuyItem1()
  {
    return this.buyingItem1;
  }
  
  public ItemStack getBuyItem2()
  {
    return this.buyingItem2;
  }
  
  public boolean hasSecondItem()
  {
    return this.buyingItem2 != null;
  }
  
  public ItemStack getBuyItem3()
  {
    return this.sellingItem;
  }
  
  public int e()
  {
    return this.uses;
  }
  
  public int f()
  {
    return this.maxUses;
  }
  
  public void g()
  {
    this.uses += 1;
  }
  
  public void a(int ☃)
  {
    this.maxUses += ☃;
  }
  
  public boolean h()
  {
    return this.uses >= this.maxUses;
  }
  
  public boolean j()
  {
    return this.rewardExp;
  }
  
  public void a(NBTTagCompound ☃)
  {
    NBTTagCompound ☃ = ☃.getCompound("buy");
    this.buyingItem1 = ItemStack.createStack(☃);
    NBTTagCompound ☃ = ☃.getCompound("sell");
    this.sellingItem = ItemStack.createStack(☃);
    if (☃.hasKeyOfType("buyB", 10)) {
      this.buyingItem2 = ItemStack.createStack(☃.getCompound("buyB"));
    }
    if (☃.hasKeyOfType("uses", 99)) {
      this.uses = ☃.getInt("uses");
    }
    if (☃.hasKeyOfType("maxUses", 99)) {
      this.maxUses = ☃.getInt("maxUses");
    } else {
      this.maxUses = 7;
    }
    if (☃.hasKeyOfType("rewardExp", 1)) {
      this.rewardExp = ☃.getBoolean("rewardExp");
    } else {
      this.rewardExp = true;
    }
  }
  
  public NBTTagCompound k()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    ☃.set("buy", this.buyingItem1.save(new NBTTagCompound()));
    ☃.set("sell", this.sellingItem.save(new NBTTagCompound()));
    if (this.buyingItem2 != null) {
      ☃.set("buyB", this.buyingItem2.save(new NBTTagCompound()));
    }
    ☃.setInt("uses", this.uses);
    ☃.setInt("maxUses", this.maxUses);
    ☃.setBoolean("rewardExp", this.rewardExp);
    return ☃;
  }
}
