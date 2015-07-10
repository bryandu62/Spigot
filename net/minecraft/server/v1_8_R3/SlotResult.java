package net.minecraft.server.v1_8_R3;

public class SlotResult
  extends Slot
{
  private final InventoryCrafting a;
  private final EntityHuman b;
  private int c;
  
  public SlotResult(EntityHuman ☃, InventoryCrafting ☃, IInventory ☃, int ☃, int ☃, int ☃)
  {
    super(☃, ☃, ☃, ☃);
    this.b = ☃;
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
    if (this.c > 0) {
      ☃.a(this.b.world, this.b, this.c);
    }
    this.c = 0;
    if (☃.getItem() == Item.getItemOf(Blocks.CRAFTING_TABLE)) {
      this.b.b(AchievementList.h);
    }
    if ((☃.getItem() instanceof ItemPickaxe)) {
      this.b.b(AchievementList.i);
    }
    if (☃.getItem() == Item.getItemOf(Blocks.FURNACE)) {
      this.b.b(AchievementList.j);
    }
    if ((☃.getItem() instanceof ItemHoe)) {
      this.b.b(AchievementList.l);
    }
    if (☃.getItem() == Items.BREAD) {
      this.b.b(AchievementList.m);
    }
    if (☃.getItem() == Items.CAKE) {
      this.b.b(AchievementList.n);
    }
    if (((☃.getItem() instanceof ItemPickaxe)) && (((ItemPickaxe)☃.getItem()).g() != Item.EnumToolMaterial.WOOD)) {
      this.b.b(AchievementList.o);
    }
    if ((☃.getItem() instanceof ItemSword)) {
      this.b.b(AchievementList.r);
    }
    if (☃.getItem() == Item.getItemOf(Blocks.ENCHANTING_TABLE)) {
      this.b.b(AchievementList.E);
    }
    if (☃.getItem() == Item.getItemOf(Blocks.BOOKSHELF)) {
      this.b.b(AchievementList.G);
    }
    if ((☃.getItem() == Items.GOLDEN_APPLE) && (☃.getData() == 1)) {
      this.b.b(AchievementList.M);
    }
  }
  
  public void a(EntityHuman ☃, ItemStack ☃)
  {
    c(☃);
    
    ItemStack[] ☃ = CraftingManager.getInstance().b(this.a, ☃.world);
    for (int ☃ = 0; ☃ < ☃.length; ☃++)
    {
      ItemStack ☃ = this.a.getItem(☃);
      ItemStack ☃ = ☃[☃];
      if (☃ != null) {
        this.a.splitStack(☃, 1);
      }
      if (☃ != null) {
        if (this.a.getItem(☃) == null) {
          this.a.setItem(☃, ☃);
        } else if (!this.b.inventory.pickup(☃)) {
          this.b.drop(☃, false);
        }
      }
    }
  }
}
