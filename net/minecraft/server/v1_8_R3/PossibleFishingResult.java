package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PossibleFishingResult
  extends WeightedRandom.WeightedRandomChoice
{
  private final ItemStack b;
  private float c;
  private boolean d;
  
  public PossibleFishingResult(ItemStack ☃, int ☃)
  {
    super(☃);
    this.b = ☃;
  }
  
  public ItemStack a(Random ☃)
  {
    ItemStack ☃ = this.b.cloneItemStack();
    if (this.c > 0.0F)
    {
      int ☃ = (int)(this.c * this.b.j());
      int ☃ = ☃.j() - ☃.nextInt(☃.nextInt(☃) + 1);
      if (☃ > ☃) {
        ☃ = ☃;
      }
      if (☃ < 1) {
        ☃ = 1;
      }
      ☃.setData(☃);
    }
    if (this.d) {
      EnchantmentManager.a(☃, ☃, 30);
    }
    return ☃;
  }
  
  public PossibleFishingResult a(float ☃)
  {
    this.c = ☃;
    return this;
  }
  
  public PossibleFishingResult a()
  {
    this.d = true;
    return this;
  }
}
