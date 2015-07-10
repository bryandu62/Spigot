package net.minecraft.server.v1_8_R3;

import java.util.Collection;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistentScoreboard
  extends PersistentBase
{
  private static final Logger b = ;
  private Scoreboard c;
  private NBTTagCompound d;
  
  public PersistentScoreboard()
  {
    this("scoreboard");
  }
  
  public PersistentScoreboard(String ☃)
  {
    super(☃);
  }
  
  public void a(Scoreboard ☃)
  {
    this.c = ☃;
    if (this.d != null) {
      a(this.d);
    }
  }
  
  public void a(NBTTagCompound ☃)
  {
    if (this.c == null)
    {
      this.d = ☃;
      return;
    }
    b(☃.getList("Objectives", 10));
    c(☃.getList("PlayerScores", 10));
    if (☃.hasKeyOfType("DisplaySlots", 10)) {
      c(☃.getCompound("DisplaySlots"));
    }
    if (☃.hasKeyOfType("Teams", 9)) {
      a(☃.getList("Teams", 10));
    }
  }
  
  protected void a(NBTTagList ☃)
  {
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      NBTTagCompound ☃ = ☃.get(☃);
      
      String ☃ = ☃.getString("Name");
      if (☃.length() > 16) {
        ☃ = ☃.substring(0, 16);
      }
      ScoreboardTeam ☃ = this.c.createTeam(☃);
      String ☃ = ☃.getString("DisplayName");
      if (☃.length() > 32) {
        ☃ = ☃.substring(0, 32);
      }
      ☃.setDisplayName(☃);
      if (☃.hasKeyOfType("TeamColor", 8)) {
        ☃.a(EnumChatFormat.b(☃.getString("TeamColor")));
      }
      ☃.setPrefix(☃.getString("Prefix"));
      ☃.setSuffix(☃.getString("Suffix"));
      if (☃.hasKeyOfType("AllowFriendlyFire", 99)) {
        ☃.setAllowFriendlyFire(☃.getBoolean("AllowFriendlyFire"));
      }
      if (☃.hasKeyOfType("SeeFriendlyInvisibles", 99)) {
        ☃.setCanSeeFriendlyInvisibles(☃.getBoolean("SeeFriendlyInvisibles"));
      }
      if (☃.hasKeyOfType("NameTagVisibility", 8))
      {
        ScoreboardTeamBase.EnumNameTagVisibility ☃ = ScoreboardTeamBase.EnumNameTagVisibility.a(☃.getString("NameTagVisibility"));
        if (☃ != null) {
          ☃.setNameTagVisibility(☃);
        }
      }
      if (☃.hasKeyOfType("DeathMessageVisibility", 8))
      {
        ScoreboardTeamBase.EnumNameTagVisibility ☃ = ScoreboardTeamBase.EnumNameTagVisibility.a(☃.getString("DeathMessageVisibility"));
        if (☃ != null) {
          ☃.b(☃);
        }
      }
      a(☃, ☃.getList("Players", 8));
    }
  }
  
  protected void a(ScoreboardTeam ☃, NBTTagList ☃)
  {
    for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
      this.c.addPlayerToTeam(☃.getString(☃), ☃.getName());
    }
  }
  
  protected void c(NBTTagCompound ☃)
  {
    for (int ☃ = 0; ☃ < 19; ☃++) {
      if (☃.hasKeyOfType("slot_" + ☃, 8))
      {
        String ☃ = ☃.getString("slot_" + ☃);
        ScoreboardObjective ☃ = this.c.getObjective(☃);
        this.c.setDisplaySlot(☃, ☃);
      }
    }
  }
  
  protected void b(NBTTagList ☃)
  {
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      NBTTagCompound ☃ = ☃.get(☃);
      
      IScoreboardCriteria ☃ = (IScoreboardCriteria)IScoreboardCriteria.criteria.get(☃.getString("CriteriaName"));
      if (☃ != null)
      {
        String ☃ = ☃.getString("Name");
        if (☃.length() > 16) {
          ☃ = ☃.substring(0, 16);
        }
        ScoreboardObjective ☃ = this.c.registerObjective(☃, ☃);
        ☃.setDisplayName(☃.getString("DisplayName"));
        ☃.a(IScoreboardCriteria.EnumScoreboardHealthDisplay.a(☃.getString("RenderType")));
      }
    }
  }
  
  protected void c(NBTTagList ☃)
  {
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      NBTTagCompound ☃ = ☃.get(☃);
      
      ScoreboardObjective ☃ = this.c.getObjective(☃.getString("Objective"));
      String ☃ = ☃.getString("Name");
      if (☃.length() > 40) {
        ☃ = ☃.substring(0, 40);
      }
      ScoreboardScore ☃ = this.c.getPlayerScoreForObjective(☃, ☃);
      ☃.setScore(☃.getInt("Score"));
      if (☃.hasKey("Locked")) {
        ☃.a(☃.getBoolean("Locked"));
      }
    }
  }
  
  public void b(NBTTagCompound ☃)
  {
    if (this.c == null)
    {
      b.warn("Tried to save scoreboard without having a scoreboard...");
      return;
    }
    ☃.set("Objectives", b());
    ☃.set("PlayerScores", e());
    ☃.set("Teams", a());
    
    d(☃);
  }
  
  protected NBTTagList a()
  {
    NBTTagList ☃ = new NBTTagList();
    Collection<ScoreboardTeam> ☃ = this.c.getTeams();
    for (ScoreboardTeam ☃ : ☃)
    {
      NBTTagCompound ☃ = new NBTTagCompound();
      
      ☃.setString("Name", ☃.getName());
      ☃.setString("DisplayName", ☃.getDisplayName());
      if (☃.l().b() >= 0) {
        ☃.setString("TeamColor", ☃.l().e());
      }
      ☃.setString("Prefix", ☃.getPrefix());
      ☃.setString("Suffix", ☃.getSuffix());
      ☃.setBoolean("AllowFriendlyFire", ☃.allowFriendlyFire());
      ☃.setBoolean("SeeFriendlyInvisibles", ☃.canSeeFriendlyInvisibles());
      ☃.setString("NameTagVisibility", ☃.getNameTagVisibility().e);
      ☃.setString("DeathMessageVisibility", ☃.j().e);
      
      NBTTagList ☃ = new NBTTagList();
      for (String ☃ : ☃.getPlayerNameSet()) {
        ☃.add(new NBTTagString(☃));
      }
      ☃.set("Players", ☃);
      
      ☃.add(☃);
    }
    return ☃;
  }
  
  protected void d(NBTTagCompound ☃)
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    boolean ☃ = false;
    for (int ☃ = 0; ☃ < 19; ☃++)
    {
      ScoreboardObjective ☃ = this.c.getObjectiveForSlot(☃);
      if (☃ != null)
      {
        ☃.setString("slot_" + ☃, ☃.getName());
        ☃ = true;
      }
    }
    if (☃) {
      ☃.set("DisplaySlots", ☃);
    }
  }
  
  protected NBTTagList b()
  {
    NBTTagList ☃ = new NBTTagList();
    Collection<ScoreboardObjective> ☃ = this.c.getObjectives();
    for (ScoreboardObjective ☃ : ☃) {
      if (☃.getCriteria() != null)
      {
        NBTTagCompound ☃ = new NBTTagCompound();
        ☃.setString("Name", ☃.getName());
        ☃.setString("CriteriaName", ☃.getCriteria().getName());
        ☃.setString("DisplayName", ☃.getDisplayName());
        ☃.setString("RenderType", ☃.e().a());
        
        ☃.add(☃);
      }
    }
    return ☃;
  }
  
  protected NBTTagList e()
  {
    NBTTagList ☃ = new NBTTagList();
    Collection<ScoreboardScore> ☃ = this.c.getScores();
    for (ScoreboardScore ☃ : ☃) {
      if (☃.getObjective() != null)
      {
        NBTTagCompound ☃ = new NBTTagCompound();
        ☃.setString("Name", ☃.getPlayerName());
        ☃.setString("Objective", ☃.getObjective().getName());
        ☃.setInt("Score", ☃.getScore());
        ☃.setBoolean("Locked", ☃.g());
        
        ☃.add(☃);
      }
    }
    return ☃;
  }
}
