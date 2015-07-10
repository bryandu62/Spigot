package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;

public class ScoreboardTeam
  extends ScoreboardTeamBase
{
  private final Scoreboard a;
  private final String b;
  private final Set<String> c = Sets.newHashSet();
  private String d;
  private String e = "";
  private String f = "";
  private boolean g = true;
  private boolean h = true;
  private ScoreboardTeamBase.EnumNameTagVisibility i = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS;
  private ScoreboardTeamBase.EnumNameTagVisibility j = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS;
  private EnumChatFormat k = EnumChatFormat.RESET;
  
  public ScoreboardTeam(Scoreboard ☃, String ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.d = ☃;
  }
  
  public String getName()
  {
    return this.b;
  }
  
  public String getDisplayName()
  {
    return this.d;
  }
  
  public void setDisplayName(String ☃)
  {
    if (☃ == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    this.d = ☃;
    this.a.handleTeamChanged(this);
  }
  
  public Collection<String> getPlayerNameSet()
  {
    return this.c;
  }
  
  public String getPrefix()
  {
    return this.e;
  }
  
  public void setPrefix(String ☃)
  {
    if (☃ == null) {
      throw new IllegalArgumentException("Prefix cannot be null");
    }
    this.e = ☃;
    this.a.handleTeamChanged(this);
  }
  
  public String getSuffix()
  {
    return this.f;
  }
  
  public void setSuffix(String ☃)
  {
    this.f = ☃;
    this.a.handleTeamChanged(this);
  }
  
  public String getFormattedName(String ☃)
  {
    return getPrefix() + ☃ + getSuffix();
  }
  
  public static String getPlayerDisplayName(ScoreboardTeamBase ☃, String ☃)
  {
    if (☃ == null) {
      return ☃;
    }
    return ☃.getFormattedName(☃);
  }
  
  public boolean allowFriendlyFire()
  {
    return this.g;
  }
  
  public void setAllowFriendlyFire(boolean ☃)
  {
    this.g = ☃;
    this.a.handleTeamChanged(this);
  }
  
  public boolean canSeeFriendlyInvisibles()
  {
    return this.h;
  }
  
  public void setCanSeeFriendlyInvisibles(boolean ☃)
  {
    this.h = ☃;
    this.a.handleTeamChanged(this);
  }
  
  public ScoreboardTeamBase.EnumNameTagVisibility getNameTagVisibility()
  {
    return this.i;
  }
  
  public ScoreboardTeamBase.EnumNameTagVisibility j()
  {
    return this.j;
  }
  
  public void setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility ☃)
  {
    this.i = ☃;
    this.a.handleTeamChanged(this);
  }
  
  public void b(ScoreboardTeamBase.EnumNameTagVisibility ☃)
  {
    this.j = ☃;
    this.a.handleTeamChanged(this);
  }
  
  public int packOptionData()
  {
    int ☃ = 0;
    if (allowFriendlyFire()) {
      ☃ |= 0x1;
    }
    if (canSeeFriendlyInvisibles()) {
      ☃ |= 0x2;
    }
    return ☃;
  }
  
  public void a(EnumChatFormat ☃)
  {
    this.k = ☃;
  }
  
  public EnumChatFormat l()
  {
    return this.k;
  }
}
