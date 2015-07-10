package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

public abstract interface Score
{
  @Deprecated
  public abstract OfflinePlayer getPlayer();
  
  public abstract String getEntry();
  
  public abstract Objective getObjective();
  
  public abstract int getScore()
    throws IllegalStateException;
  
  public abstract void setScore(int paramInt)
    throws IllegalStateException;
  
  public abstract boolean isScoreSet()
    throws IllegalStateException;
  
  public abstract Scoreboard getScoreboard();
}
