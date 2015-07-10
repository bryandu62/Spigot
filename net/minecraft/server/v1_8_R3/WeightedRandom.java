package net.minecraft.server.v1_8_R3;

import java.util.Collection;
import java.util.Random;

public class WeightedRandom
{
  public static int a(Collection<? extends WeightedRandomChoice> ☃)
  {
    int ☃ = 0;
    for (WeightedRandomChoice ☃ : ☃) {
      ☃ += ☃.a;
    }
    return ☃;
  }
  
  public static <T extends WeightedRandomChoice> T a(Random ☃, Collection<T> ☃, int ☃)
  {
    if (☃ <= 0) {
      throw new IllegalArgumentException();
    }
    int ☃ = ☃.nextInt(☃);
    return a(☃, ☃);
  }
  
  public static <T extends WeightedRandomChoice> T a(Collection<T> ☃, int ☃)
  {
    for (T ☃ : ☃)
    {
      ☃ -= ☃.a;
      if (☃ < 0) {
        return ☃;
      }
    }
    return null;
  }
  
  public static <T extends WeightedRandomChoice> T a(Random ☃, Collection<T> ☃)
  {
    return a(☃, ☃, a(☃));
  }
  
  public static class WeightedRandomChoice
  {
    protected int a;
    
    public WeightedRandomChoice(int ☃)
    {
      this.a = ☃;
    }
  }
}
