package net.minecraft.server.v1_8_R3;

public enum EnumColor
  implements INamable
{
  private static final EnumColor[] q;
  private static final EnumColor[] r;
  private final int s;
  private final int t;
  private final String u;
  private final String v;
  private final MaterialMapColor w;
  private final EnumChatFormat x;
  
  static
  {
    q = new EnumColor[values().length];
    r = new EnumColor[values().length];
    for (EnumColor ☃ : values())
    {
      q[☃.getColorIndex()] = ☃;
      r[☃.getInvColorIndex()] = ☃;
    }
  }
  
  private EnumColor(int ☃, int ☃, String ☃, String ☃, MaterialMapColor ☃, EnumChatFormat ☃)
  {
    this.s = ☃;
    this.t = ☃;
    this.u = ☃;
    this.v = ☃;
    this.w = ☃;
    this.x = ☃;
  }
  
  public int getColorIndex()
  {
    return this.s;
  }
  
  public int getInvColorIndex()
  {
    return this.t;
  }
  
  public String d()
  {
    return this.v;
  }
  
  public MaterialMapColor e()
  {
    return this.w;
  }
  
  public static EnumColor fromInvColorIndex(int ☃)
  {
    if ((☃ < 0) || (☃ >= r.length)) {
      ☃ = 0;
    }
    return r[☃];
  }
  
  public static EnumColor fromColorIndex(int ☃)
  {
    if ((☃ < 0) || (☃ >= q.length)) {
      ☃ = 0;
    }
    return q[☃];
  }
  
  public String toString()
  {
    return this.v;
  }
  
  public String getName()
  {
    return this.u;
  }
}
