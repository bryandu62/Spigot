package net.minecraft.server.v1_8_R3;

public enum EnumDifficulty
{
  private static final EnumDifficulty[] e;
  private final int f;
  private final String g;
  
  private EnumDifficulty(int ☃, String ☃)
  {
    this.f = ☃;
    this.g = ☃;
  }
  
  public int a()
  {
    return this.f;
  }
  
  public static EnumDifficulty getById(int ☃)
  {
    return e[(☃ % e.length)];
  }
  
  static
  {
    e = new EnumDifficulty[values().length];
    for (EnumDifficulty ☃ : values()) {
      e[☃.f] = ☃;
    }
  }
  
  public String b()
  {
    return this.g;
  }
}
