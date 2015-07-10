package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutScoreboardScore
  implements Packet<PacketListenerPlayOut>
{
  private String a = "";
  private String b = "";
  private int c;
  private EnumScoreboardAction d;
  
  public PacketPlayOutScoreboardScore() {}
  
  public PacketPlayOutScoreboardScore(ScoreboardScore ☃)
  {
    this.a = ☃.getPlayerName();
    this.b = ☃.getObjective().getName();
    this.c = ☃.getScore();
    this.d = EnumScoreboardAction.CHANGE;
  }
  
  public PacketPlayOutScoreboardScore(String ☃)
  {
    this.a = ☃;
    this.b = "";
    this.c = 0;
    this.d = EnumScoreboardAction.REMOVE;
  }
  
  public PacketPlayOutScoreboardScore(String ☃, ScoreboardObjective ☃)
  {
    this.a = ☃;
    this.b = ☃.getName();
    this.c = 0;
    this.d = EnumScoreboardAction.REMOVE;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(40);
    this.d = ((EnumScoreboardAction)☃.a(EnumScoreboardAction.class));
    this.b = ☃.c(16);
    if (this.d != EnumScoreboardAction.REMOVE) {
      this.c = ☃.e();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.a(this.d);
    ☃.a(this.b);
    if (this.d != EnumScoreboardAction.REMOVE) {
      ☃.b(this.c);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public static enum EnumScoreboardAction
  {
    private EnumScoreboardAction() {}
  }
}
