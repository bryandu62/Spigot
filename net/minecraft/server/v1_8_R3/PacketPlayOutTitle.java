package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutTitle
  implements Packet<PacketListenerPlayOut>
{
  private EnumTitleAction a;
  private IChatBaseComponent b;
  private int c;
  private int d;
  private int e;
  
  public PacketPlayOutTitle() {}
  
  public PacketPlayOutTitle(EnumTitleAction ☃, IChatBaseComponent ☃)
  {
    this(☃, ☃, -1, -1, -1);
  }
  
  public PacketPlayOutTitle(int ☃, int ☃, int ☃)
  {
    this(EnumTitleAction.TIMES, null, ☃, ☃, ☃);
  }
  
  public PacketPlayOutTitle(EnumTitleAction ☃, IChatBaseComponent ☃, int ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ((EnumTitleAction)☃.a(EnumTitleAction.class));
    if ((this.a == EnumTitleAction.TITLE) || (this.a == EnumTitleAction.SUBTITLE)) {
      this.b = ☃.d();
    }
    if (this.a == EnumTitleAction.TIMES)
    {
      this.c = ☃.readInt();
      this.d = ☃.readInt();
      this.e = ☃.readInt();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    if ((this.a == EnumTitleAction.TITLE) || (this.a == EnumTitleAction.SUBTITLE)) {
      ☃.a(this.b);
    }
    if (this.a == EnumTitleAction.TIMES)
    {
      ☃.writeInt(this.c);
      ☃.writeInt(this.d);
      ☃.writeInt(this.e);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public static enum EnumTitleAction
  {
    private EnumTitleAction() {}
    
    public static EnumTitleAction a(String ☃)
    {
      for (EnumTitleAction ☃ : ) {
        if (☃.name().equalsIgnoreCase(☃)) {
          return ☃;
        }
      }
      return TITLE;
    }
    
    public static String[] a()
    {
      String[] ☃ = new String[values().length];
      int ☃ = 0;
      for (EnumTitleAction ☃ : values()) {
        ☃[(☃++)] = ☃.name().toLowerCase();
      }
      return ☃;
    }
  }
}
