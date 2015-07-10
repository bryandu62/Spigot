package org.bukkit.scoreboard;

public abstract interface ScoreboardManager
{
  public abstract Scoreboard getMainScoreboard();
  
  public abstract Scoreboard getNewScoreboard();
}
