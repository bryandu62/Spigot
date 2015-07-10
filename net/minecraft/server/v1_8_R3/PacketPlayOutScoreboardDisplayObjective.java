package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutScoreboardDisplayObjective
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private String b;
  
  public PacketPlayOutScoreboardDisplayObjective() {}
  
  public PacketPlayOutScoreboardDisplayObjective(int ☃, ScoreboardObjective ☃)
  {
    this.a = ☃;
    if (☃ == null) {
      this.b = "";
    } else {
      this.b = ☃.getName();
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.readByte();
    this.b = ☃.c(16);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.a);
    ☃.a(this.b);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
