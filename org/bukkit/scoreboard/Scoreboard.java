package org.bukkit.scoreboard;

import java.util.Set;
import org.bukkit.OfflinePlayer;

public abstract interface Scoreboard
{
  public abstract Objective registerNewObjective(String paramString1, String paramString2)
    throws IllegalArgumentException;
  
  public abstract Objective getObjective(String paramString)
    throws IllegalArgumentException;
  
  public abstract Set<Objective> getObjectivesByCriteria(String paramString)
    throws IllegalArgumentException;
  
  public abstract Set<Objective> getObjectives();
  
  public abstract Objective getObjective(DisplaySlot paramDisplaySlot)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract Set<Score> getScores(OfflinePlayer paramOfflinePlayer)
    throws IllegalArgumentException;
  
  public abstract Set<Score> getScores(String paramString)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract void resetScores(OfflinePlayer paramOfflinePlayer)
    throws IllegalArgumentException;
  
  public abstract void resetScores(String paramString)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract Team getPlayerTeam(OfflinePlayer paramOfflinePlayer)
    throws IllegalArgumentException;
  
  public abstract Team getEntryTeam(String paramString)
    throws IllegalArgumentException;
  
  public abstract Team getTeam(String paramString)
    throws IllegalArgumentException;
  
  public abstract Set<Team> getTeams();
  
  public abstract Team registerNewTeam(String paramString)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract Set<OfflinePlayer> getPlayers();
  
  public abstract Set<String> getEntries();
  
  public abstract void clearSlot(DisplaySlot paramDisplaySlot)
    throws IllegalArgumentException;
}
