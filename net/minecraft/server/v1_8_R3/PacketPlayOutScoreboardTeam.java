package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;

public class PacketPlayOutScoreboardTeam
  implements Packet<PacketListenerPlayOut>
{
  private String a = "";
  private String b = "";
  private String c = "";
  private String d = "";
  private String e = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.e;
  private int f = -1;
  private Collection<String> g = Lists.newArrayList();
  private int h;
  private int i;
  
  public PacketPlayOutScoreboardTeam() {}
  
  public PacketPlayOutScoreboardTeam(ScoreboardTeam ☃, int ☃)
  {
    this.a = ☃.getName();
    this.h = ☃;
    if ((☃ == 0) || (☃ == 2))
    {
      this.b = ☃.getDisplayName();
      this.c = ☃.getPrefix();
      this.d = ☃.getSuffix();
      this.i = ☃.packOptionData();
      this.e = ☃.getNameTagVisibility().e;
      this.f = ☃.l().b();
    }
    if (☃ == 0) {
      this.g.addAll(☃.getPlayerNameSet());
    }
  }
  
  public PacketPlayOutScoreboardTeam(ScoreboardTeam ☃, Collection<String> ☃, int ☃)
  {
    if ((☃ != 3) && (☃ != 4)) {
      throw new IllegalArgumentException("Method must be join or leave for player constructor");
    }
    if ((☃ == null) || (☃.isEmpty())) {
      throw new IllegalArgumentException("Players cannot be null/empty");
    }
    this.h = ☃;
    this.a = ☃.getName();
    this.g.addAll(☃);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(16);
    this.h = ☃.readByte();
    if ((this.h == 0) || (this.h == 2))
    {
      this.b = ☃.c(32);
      this.c = ☃.c(16);
      this.d = ☃.c(16);
      this.i = ☃.readByte();
      this.e = ☃.c(32);
      this.f = ☃.readByte();
    }
    if ((this.h == 0) || (this.h == 3) || (this.h == 4))
    {
      int ☃ = ☃.e();
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        this.g.add(☃.c(40));
      }
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeByte(this.h);
    if ((this.h == 0) || (this.h == 2))
    {
      ☃.a(this.b);
      ☃.a(this.c);
      ☃.a(this.d);
      ☃.writeByte(this.i);
      ☃.a(this.e);
      ☃.writeByte(this.f);
    }
    if ((this.h == 0) || (this.h == 3) || (this.h == 4))
    {
      ☃.b(this.g.size());
      for (String ☃ : this.g) {
        ☃.a(☃);
      }
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
