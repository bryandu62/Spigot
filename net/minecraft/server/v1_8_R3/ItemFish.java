package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Map;

public class ItemFish
  extends ItemFood
{
  private final boolean b;
  
  public ItemFish(boolean ☃)
  {
    super(0, 0.0F, false);
    
    this.b = ☃;
  }
  
  public int getNutrition(ItemStack ☃)
  {
    EnumFish ☃ = EnumFish.a(☃);
    if ((this.b) && (☃.g())) {
      return ☃.e();
    }
    return ☃.c();
  }
  
  public float getSaturationModifier(ItemStack ☃)
  {
    EnumFish ☃ = EnumFish.a(☃);
    if ((this.b) && (☃.g())) {
      return ☃.f();
    }
    return ☃.d();
  }
  
  public String j(ItemStack ☃)
  {
    if (EnumFish.a(☃) == EnumFish.PUFFERFISH) {
      return PotionBrewer.m;
    }
    return null;
  }
  
  protected void c(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    EnumFish ☃ = EnumFish.a(☃);
    if (☃ == EnumFish.PUFFERFISH)
    {
      ☃.addEffect(new MobEffect(MobEffectList.POISON.id, 1200, 3));
      ☃.addEffect(new MobEffect(MobEffectList.HUNGER.id, 300, 2));
      ☃.addEffect(new MobEffect(MobEffectList.CONFUSION.id, 300, 1));
    }
    super.c(☃, ☃, ☃);
  }
  
  public String e_(ItemStack ☃)
  {
    EnumFish ☃ = EnumFish.a(☃);
    return getName() + "." + ☃.b() + "." + ((this.b) && (☃.g()) ? "cooked" : "raw");
  }
  
  public static enum EnumFish
  {
    private static final Map<Integer, EnumFish> e;
    private final int f;
    private final String g;
    private final int h;
    private final float i;
    private final int j;
    private final float k;
    private boolean l = false;
    
    static
    {
      e = Maps.newHashMap();
      for (EnumFish ☃ : values()) {
        e.put(Integer.valueOf(☃.a()), ☃);
      }
    }
    
    private EnumFish(int ☃, String ☃, int ☃, float ☃, int ☃, float ☃)
    {
      this.f = ☃;
      this.g = ☃;
      this.h = ☃;
      this.i = ☃;
      this.j = ☃;
      this.k = ☃;
      this.l = true;
    }
    
    private EnumFish(int ☃, String ☃, int ☃, float ☃)
    {
      this.f = ☃;
      this.g = ☃;
      this.h = ☃;
      this.i = ☃;
      this.j = 0;
      this.k = 0.0F;
      this.l = false;
    }
    
    public int a()
    {
      return this.f;
    }
    
    public String b()
    {
      return this.g;
    }
    
    public int c()
    {
      return this.h;
    }
    
    public float d()
    {
      return this.i;
    }
    
    public int e()
    {
      return this.j;
    }
    
    public float f()
    {
      return this.k;
    }
    
    public boolean g()
    {
      return this.l;
    }
    
    public static EnumFish a(int ☃)
    {
      EnumFish ☃ = (EnumFish)e.get(Integer.valueOf(☃));
      if (☃ == null) {
        return COD;
      }
      return ☃;
    }
    
    public static EnumFish a(ItemStack ☃)
    {
      if ((☃.getItem() instanceof ItemFish)) {
        return a(☃.getData());
      }
      return COD;
    }
  }
}
