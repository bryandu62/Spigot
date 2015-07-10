package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class ItemFood
  extends Item
{
  public final int a = 32;
  private final int b;
  private final float c;
  private final boolean d;
  private boolean k;
  private int l;
  private int m;
  private int n;
  private float o;
  
  public ItemFood(int ☃, float ☃, boolean ☃)
  {
    this.b = ☃;
    this.d = ☃;
    this.c = ☃;
    a(CreativeModeTab.h);
  }
  
  public ItemFood(int ☃, boolean ☃)
  {
    this(☃, 0.6F, ☃);
  }
  
  public ItemStack b(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    ☃.count -= 1;
    ☃.getFoodData().a(this, ☃);
    ☃.makeSound(☃, "random.burp", 0.5F, ☃.random.nextFloat() * 0.1F + 0.9F);
    
    c(☃, ☃, ☃);
    ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    
    return ☃;
  }
  
  protected void c(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if ((!☃.isClientSide) && (this.l > 0) && (☃.random.nextFloat() < this.o)) {
      ☃.addEffect(new MobEffect(this.l, this.m * 20, this.n));
    }
  }
  
  public int d(ItemStack ☃)
  {
    return 32;
  }
  
  public EnumAnimation e(ItemStack ☃)
  {
    return EnumAnimation.EAT;
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if (☃.j(this.k)) {
      ☃.a(☃, d(☃));
    }
    return ☃;
  }
  
  public int getNutrition(ItemStack ☃)
  {
    return this.b;
  }
  
  public float getSaturationModifier(ItemStack ☃)
  {
    return this.c;
  }
  
  public boolean g()
  {
    return this.d;
  }
  
  public ItemFood a(int ☃, int ☃, int ☃, float ☃)
  {
    this.l = ☃;
    this.m = ☃;
    this.n = ☃;
    this.o = ☃;
    return this;
  }
  
  public ItemFood h()
  {
    this.k = true;
    return this;
  }
}
