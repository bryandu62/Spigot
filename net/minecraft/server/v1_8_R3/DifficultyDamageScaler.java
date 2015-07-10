package net.minecraft.server.v1_8_R3;

public class DifficultyDamageScaler
{
  private final EnumDifficulty a;
  private final float b;
  
  public DifficultyDamageScaler(EnumDifficulty ☃, long ☃, long ☃, float ☃)
  {
    this.a = ☃;
    this.b = a(☃, ☃, ☃, ☃);
  }
  
  public float c()
  {
    if (this.b < 2.0F) {
      return 0.0F;
    }
    if (this.b > 4.0F) {
      return 1.0F;
    }
    return (this.b - 2.0F) / 2.0F;
  }
  
  private float a(EnumDifficulty ☃, long ☃, long ☃, float ☃)
  {
    if (☃ == EnumDifficulty.PEACEFUL) {
      return 0.0F;
    }
    boolean ☃ = ☃ == EnumDifficulty.HARD;
    float ☃ = 0.75F;
    
    float ☃ = MathHelper.a(((float)☃ + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
    ☃ += ☃;
    
    float ☃ = 0.0F;
    
    ☃ += MathHelper.a((float)☃ / 3600000.0F, 0.0F, 1.0F) * (☃ ? 1.0F : 0.75F);
    ☃ += MathHelper.a(☃ * 0.25F, 0.0F, ☃);
    if (☃ == EnumDifficulty.EASY) {
      ☃ *= 0.5F;
    }
    ☃ += ☃;
    
    return ☃.a() * ☃;
  }
}
