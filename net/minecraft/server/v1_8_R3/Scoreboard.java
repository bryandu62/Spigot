package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Scoreboard
{
  private final Map<String, ScoreboardObjective> objectivesByName = Maps.newHashMap();
  private final Map<IScoreboardCriteria, List<ScoreboardObjective>> objectivesByCriteria = Maps.newHashMap();
  private final Map<String, Map<ScoreboardObjective, ScoreboardScore>> playerScores = Maps.newHashMap();
  private final ScoreboardObjective[] displaySlots = new ScoreboardObjective[19];
  private final Map<String, ScoreboardTeam> teamsByName = Maps.newHashMap();
  private final Map<String, ScoreboardTeam> teamsByPlayer = Maps.newHashMap();
  
  public ScoreboardObjective getObjective(String ☃)
  {
    return (ScoreboardObjective)this.objectivesByName.get(☃);
  }
  
  public ScoreboardObjective registerObjective(String ☃, IScoreboardCriteria ☃)
  {
    if (☃.length() > 16) {
      throw new IllegalArgumentException("The objective name '" + ☃ + "' is too long!");
    }
    ScoreboardObjective ☃ = getObjective(☃);
    if (☃ != null) {
      throw new IllegalArgumentException("An objective with the name '" + ☃ + "' already exists!");
    }
    ☃ = new ScoreboardObjective(this, ☃, ☃);
    
    List<ScoreboardObjective> ☃ = (List)this.objectivesByCriteria.get(☃);
    if (☃ == null)
    {
      ☃ = Lists.newArrayList();
      this.objectivesByCriteria.put(☃, ☃);
    }
    ☃.add(☃);
    this.objectivesByName.put(☃, ☃);
    handleObjectiveAdded(☃);
    
    return ☃;
  }
  
  public Collection<ScoreboardObjective> getObjectivesForCriteria(IScoreboardCriteria ☃)
  {
    Collection<ScoreboardObjective> ☃ = (Collection)this.objectivesByCriteria.get(☃);
    
    return ☃ == null ? Lists.newArrayList() : Lists.newArrayList(☃);
  }
  
  public boolean b(String ☃, ScoreboardObjective ☃)
  {
    Map<ScoreboardObjective, ScoreboardScore> ☃ = (Map)this.playerScores.get(☃);
    if (☃ == null) {
      return false;
    }
    ScoreboardScore ☃ = (ScoreboardScore)☃.get(☃);
    if (☃ == null) {
      return false;
    }
    return true;
  }
  
  public ScoreboardScore getPlayerScoreForObjective(String ☃, ScoreboardObjective ☃)
  {
    if (☃.length() > 40) {
      throw new IllegalArgumentException("The player name '" + ☃ + "' is too long!");
    }
    Map<ScoreboardObjective, ScoreboardScore> ☃ = (Map)this.playerScores.get(☃);
    if (☃ == null)
    {
      ☃ = Maps.newHashMap();
      this.playerScores.put(☃, ☃);
    }
    ScoreboardScore ☃ = (ScoreboardScore)☃.get(☃);
    if (☃ == null)
    {
      ☃ = new ScoreboardScore(this, ☃, ☃);
      ☃.put(☃, ☃);
    }
    return ☃;
  }
  
  public Collection<ScoreboardScore> getScoresForObjective(ScoreboardObjective ☃)
  {
    List<ScoreboardScore> ☃ = Lists.newArrayList();
    for (Map<ScoreboardObjective, ScoreboardScore> ☃ : this.playerScores.values())
    {
      ScoreboardScore ☃ = (ScoreboardScore)☃.get(☃);
      if (☃ != null) {
        ☃.add(☃);
      }
    }
    Collections.sort(☃, ScoreboardScore.a);
    
    return ☃;
  }
  
  public Collection<ScoreboardObjective> getObjectives()
  {
    return this.objectivesByName.values();
  }
  
  public Collection<String> getPlayers()
  {
    return this.playerScores.keySet();
  }
  
  public void resetPlayerScores(String ☃, ScoreboardObjective ☃)
  {
    if (☃ == null)
    {
      Map<ScoreboardObjective, ScoreboardScore> ☃ = (Map)this.playerScores.remove(☃);
      if (☃ != null) {
        handlePlayerRemoved(☃);
      }
    }
    else
    {
      Map<ScoreboardObjective, ScoreboardScore> ☃ = (Map)this.playerScores.get(☃);
      if (☃ != null)
      {
        ScoreboardScore ☃ = (ScoreboardScore)☃.remove(☃);
        if (☃.size() < 1)
        {
          Map<ScoreboardObjective, ScoreboardScore> ☃ = (Map)this.playerScores.remove(☃);
          if (☃ != null) {
            handlePlayerRemoved(☃);
          }
        }
        else if (☃ != null)
        {
          a(☃, ☃);
        }
      }
    }
  }
  
  public Collection<ScoreboardScore> getScores()
  {
    Collection<Map<ScoreboardObjective, ScoreboardScore>> ☃ = this.playerScores.values();
    List<ScoreboardScore> ☃ = Lists.newArrayList();
    for (Map<ScoreboardObjective, ScoreboardScore> ☃ : ☃) {
      ☃.addAll(☃.values());
    }
    return ☃;
  }
  
  public Map<ScoreboardObjective, ScoreboardScore> getPlayerObjectives(String ☃)
  {
    Map<ScoreboardObjective, ScoreboardScore> ☃ = (Map)this.playerScores.get(☃);
    if (☃ == null) {
      ☃ = Maps.newHashMap();
    }
    return ☃;
  }
  
  public void unregisterObjective(ScoreboardObjective ☃)
  {
    this.objectivesByName.remove(☃.getName());
    for (int ☃ = 0; ☃ < 19; ☃++) {
      if (getObjectiveForSlot(☃) == ☃) {
        setDisplaySlot(☃, null);
      }
    }
    List<ScoreboardObjective> ☃ = (List)this.objectivesByCriteria.get(☃.getCriteria());
    if (☃ != null) {
      ☃.remove(☃);
    }
    for (Map<ScoreboardObjective, ScoreboardScore> ☃ : this.playerScores.values()) {
      ☃.remove(☃);
    }
    handleObjectiveRemoved(☃);
  }
  
  public void setDisplaySlot(int ☃, ScoreboardObjective ☃)
  {
    this.displaySlots[☃] = ☃;
  }
  
  public ScoreboardObjective getObjectiveForSlot(int ☃)
  {
    return this.displaySlots[☃];
  }
  
  public ScoreboardTeam getTeam(String ☃)
  {
    return (ScoreboardTeam)this.teamsByName.get(☃);
  }
  
  public ScoreboardTeam createTeam(String ☃)
  {
    if (☃.length() > 16) {
      throw new IllegalArgumentException("The team name '" + ☃ + "' is too long!");
    }
    ScoreboardTeam ☃ = getTeam(☃);
    if (☃ != null) {
      throw new IllegalArgumentException("A team with the name '" + ☃ + "' already exists!");
    }
    ☃ = new ScoreboardTeam(this, ☃);
    this.teamsByName.put(☃, ☃);
    handleTeamAdded(☃);
    
    return ☃;
  }
  
  public void removeTeam(ScoreboardTeam ☃)
  {
    this.teamsByName.remove(☃.getName());
    for (String ☃ : ☃.getPlayerNameSet()) {
      this.teamsByPlayer.remove(☃);
    }
    handleTeamRemoved(☃);
  }
  
  public boolean addPlayerToTeam(String ☃, String ☃)
  {
    if (☃.length() > 40) {
      throw new IllegalArgumentException("The player name '" + ☃ + "' is too long!");
    }
    if (!this.teamsByName.containsKey(☃)) {
      return false;
    }
    ScoreboardTeam ☃ = getTeam(☃);
    if (getPlayerTeam(☃) != null) {
      removePlayerFromTeam(☃);
    }
    this.teamsByPlayer.put(☃, ☃);
    ☃.getPlayerNameSet().add(☃);
    return true;
  }
  
  public boolean removePlayerFromTeam(String ☃)
  {
    ScoreboardTeam ☃ = getPlayerTeam(☃);
    if (☃ != null)
    {
      removePlayerFromTeam(☃, ☃);
      return true;
    }
    return false;
  }
  
  public void removePlayerFromTeam(String ☃, ScoreboardTeam ☃)
  {
    if (getPlayerTeam(☃) != ☃) {
      throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + ☃.getName() + "'.");
    }
    this.teamsByPlayer.remove(☃);
    ☃.getPlayerNameSet().remove(☃);
  }
  
  public Collection<String> getTeamNames()
  {
    return this.teamsByName.keySet();
  }
  
  public Collection<ScoreboardTeam> getTeams()
  {
    return this.teamsByName.values();
  }
  
  public ScoreboardTeam getPlayerTeam(String ☃)
  {
    return (ScoreboardTeam)this.teamsByPlayer.get(☃);
  }
  
  public void handleObjectiveAdded(ScoreboardObjective ☃) {}
  
  public void handleObjectiveChanged(ScoreboardObjective ☃) {}
  
  public void handleObjectiveRemoved(ScoreboardObjective ☃) {}
  
  public void handleScoreChanged(ScoreboardScore ☃) {}
  
  public void handlePlayerRemoved(String ☃) {}
  
  public void a(String ☃, ScoreboardObjective ☃) {}
  
  public void handleTeamAdded(ScoreboardTeam ☃) {}
  
  public void handleTeamChanged(ScoreboardTeam ☃) {}
  
  public void handleTeamRemoved(ScoreboardTeam ☃) {}
  
  public static String getSlotName(int ☃)
  {
    switch (☃)
    {
    case 0: 
      return "list";
    case 1: 
      return "sidebar";
    case 2: 
      return "belowName";
    }
    if ((☃ >= 3) && (☃ <= 18))
    {
      EnumChatFormat ☃ = EnumChatFormat.a(☃ - 3);
      if ((☃ != null) && (☃ != EnumChatFormat.RESET)) {
        return "sidebar.team." + ☃.e();
      }
    }
    return null;
  }
  
  public static int getSlotForName(String ☃)
  {
    if (☃.equalsIgnoreCase("list")) {
      return 0;
    }
    if (☃.equalsIgnoreCase("sidebar")) {
      return 1;
    }
    if (☃.equalsIgnoreCase("belowName")) {
      return 2;
    }
    if (☃.startsWith("sidebar.team."))
    {
      String ☃ = ☃.substring("sidebar.team.".length());
      EnumChatFormat ☃ = EnumChatFormat.b(☃);
      if ((☃ != null) && (☃.b() >= 0)) {
        return ☃.b() + 3;
      }
    }
    return -1;
  }
  
  private static String[] g = null;
  
  public static String[] h()
  {
    if (g == null)
    {
      g = new String[19];
      for (int ☃ = 0; ☃ < 19; ☃++) {
        g[☃] = getSlotName(☃);
      }
    }
    return g;
  }
  
  public void a(Entity ☃)
  {
    if ((☃ == null) || ((☃ instanceof EntityHuman)) || (☃.isAlive())) {
      return;
    }
    String ☃ = ☃.getUniqueID().toString();
    resetPlayerScores(☃, null);
    removePlayerFromTeam(☃);
  }
}
