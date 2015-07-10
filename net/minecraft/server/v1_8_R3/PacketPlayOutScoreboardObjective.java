package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutScoreboardObjective
  implements Packet<PacketListenerPlayOut>
{
  private String a;
  private String b;
  private IScoreboardCriteria.EnumScoreboardHealthDisplay c;
  private int d;
  
  public PacketPlayOutScoreboardObjective() {}
  
  public PacketPlayOutScoreboardObjective(ScoreboardObjective ☃, int ☃)
  {
    this.a = ☃.getName();
    this.b = ☃.getDisplayName();
    this.c = ☃.getCriteria().c();
    this.d = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(16);
    this.d = ☃.readByte();
    if ((this.d == 0) || (this.d == 2))
    {
      this.b = ☃.c(32);
      this.c = IScoreboardCriteria.EnumScoreboardHealthDisplay.a(☃.c(16));
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeByte(this.d);
    if ((this.d == 0) || (this.d == 2))
    {
      ☃.a(this.b);
      ☃.a(this.c.a());
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
