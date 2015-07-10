package org.bukkit.scoreboard;

import org.bukkit.OfflinePlayer;

public abstract interface Objective
{
  public abstract String getName()
    throws IllegalStateException;
  
  public abstract String getDisplayName()
    throws IllegalStateException;
  
  public abstract void setDisplayName(String paramString)
    throws IllegalStateException, IllegalArgumentException;
  
  public abstract String getCriteria()
    throws IllegalStateException;
  
  public abstract boolean isModifiable()
    throws IllegalStateException;
  
  public abstract Scoreboard getScoreboard();
  
  public abstract void unregister()
    throws IllegalStateException;
  
  public abstract void setDisplaySlot(DisplaySlot paramDisplaySlot)
    throws IllegalStateException;
  
  public abstract DisplaySlot getDisplaySlot()
    throws IllegalStateException;
  
  @Deprecated
  public abstract Score getScore(OfflinePlayer paramOfflinePlayer)
    throws IllegalArgumentException, IllegalStateException;
  
  public abstract Score getScore(String paramString)
    throws IllegalArgumentException, IllegalStateException;
}
