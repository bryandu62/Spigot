package net.minecraft.server.v1_8_R3;

public class SlotMerchantResult
  extends Slot
{
  private final InventoryMerchant a;
  private EntityHuman b;
  private int c;
  private final IMerchant h;
  
  public SlotMerchantResult(EntityHuman ☃, IMerchant ☃, InventoryMerchant ☃, int ☃, int ☃, int ☃)
  {
    super(☃, ☃, ☃, ☃);
    this.b = ☃;
    this.h = ☃;
    this.a = ☃;
  }
  
  public boolean isAllowed(ItemStack ☃)
  {
    return false;
  }
  
  public ItemStack a(int ☃)
  {
    if (hasItem()) {
      this.c += Math.min(☃, getItem().count);
    }
    return super.a(☃);
  }
  
  protected void a(ItemStack ☃, int ☃)
  {
    this.c += ☃;
    c(☃);
  }
  
  protected void c(ItemStack ☃)
  {
    ☃.a(this.b.world, this.b, this.c);
    this.c = 0;
  }
  
  public void a(EntityHuman ☃, ItemStack ☃)
  {
    c(☃);
    
    MerchantRecipe ☃ = this.a.getRecipe();
    if (☃ != null)
    {
      ItemStack ☃ = this.a.getItem(0);
      ItemStack ☃ = this.a.getItem(1);
      if ((a(☃, ☃, ☃)) || (a(☃, ☃, ☃)))
      {
        this.h.a(☃);
        ☃.b(StatisticList.G);
        if ((☃ != null) && (☃.count <= 0)) {
          ☃ = null;
        }
        if ((☃ != null) && (☃.count <= 0)) {
          ☃ = null;
        }
        this.a.setItem(0, ☃);
        this.a.setItem(1, ☃);
      }
    }
  }
  
  private boolean a(MerchantRecipe ☃, ItemStack ☃, ItemStack ☃)
  {
    ItemStack ☃ = ☃.getBuyItem1();
    ItemStack ☃ = ☃.getBuyItem2();
    if ((☃ != null) && (☃.getItem() == ☃.getItem()))
    {
      if ((☃ != null) && (☃ != null) && (☃.getItem() == ☃.getItem()))
      {
        ☃.count -= ☃.count;
        ☃.count -= ☃.count;
        return true;
      }
      if ((☃ == null) && (☃ == null))
      {
        ☃.count -= ☃.count;
        return true;
      }
    }
    return false;
  }
}
