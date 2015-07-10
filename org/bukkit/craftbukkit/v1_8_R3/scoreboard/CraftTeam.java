package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Collection;
import java.util.Set;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

final class CraftTeam
  extends CraftScoreboardComponent
  implements Team
{
  private final ScoreboardTeam team;
  
  CraftTeam(CraftScoreboard scoreboard, ScoreboardTeam team)
  {
    super(scoreboard);
    this.team = team;
  }
  
  public String getName()
    throws IllegalStateException
  {
    checkState();
    
    return this.team.getName();
  }
  
  public String getDisplayName()
    throws IllegalStateException
  {
    checkState();
    
    return this.team.getDisplayName();
  }
  
  public void setDisplayName(String displayName)
    throws IllegalStateException
  {
    Validate.notNull(displayName, "Display name cannot be null");
    Validate.isTrue(displayName.length() <= 32, "Display name '" + displayName + "' is longer than the limit of 32 characters");
    checkState();
    
    this.team.setDisplayName(displayName);
  }
  
  public String getPrefix()
    throws IllegalStateException
  {
    checkState();
    
    return this.team.getPrefix();
  }
  
  public void setPrefix(String prefix)
    throws IllegalStateException, IllegalArgumentException
  {
    Validate.notNull(prefix, "Prefix cannot be null");
    Validate.isTrue(prefix.length() <= 32, "Prefix '" + prefix + "' is longer than the limit of 32 characters");
    checkState();
    
    this.team.setPrefix(prefix);
  }
  
  public String getSuffix()
    throws IllegalStateException
  {
    checkState();
    
    return this.team.getSuffix();
  }
  
  public void setSuffix(String suffix)
    throws IllegalStateException, IllegalArgumentException
  {
    Validate.notNull(suffix, "Suffix cannot be null");
    Validate.isTrue(suffix.length() <= 32, "Suffix '" + suffix + "' is longer than the limit of 32 characters");
    checkState();
    
    this.team.setSuffix(suffix);
  }
  
  public boolean allowFriendlyFire()
    throws IllegalStateException
  {
    checkState();
    
    return this.team.allowFriendlyFire();
  }
  
  public void setAllowFriendlyFire(boolean enabled)
    throws IllegalStateException
  {
    checkState();
    
    this.team.setAllowFriendlyFire(enabled);
  }
  
  public boolean canSeeFriendlyInvisibles()
    throws IllegalStateException
  {
    checkState();
    
    return this.team.canSeeFriendlyInvisibles();
  }
  
  public void setCanSeeFriendlyInvisibles(boolean enabled)
    throws IllegalStateException
  {
    checkState();
    
    this.team.setCanSeeFriendlyInvisibles(enabled);
  }
  
  public NameTagVisibility getNameTagVisibility()
    throws IllegalArgumentException
  {
    checkState();
    
    return notchToBukkit(this.team.getNameTagVisibility());
  }
  
  public void setNameTagVisibility(NameTagVisibility visibility)
    throws IllegalArgumentException
  {
    checkState();
    
    this.team.setNameTagVisibility(bukkitToNotch(visibility));
  }
  
  public Set<OfflinePlayer> getPlayers()
    throws IllegalStateException
  {
    checkState();
    
    ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
    for (String playerName : this.team.getPlayerNameSet()) {
      players.add(Bukkit.getOfflinePlayer(playerName));
    }
    return players.build();
  }
  
  public Set<String> getEntries()
    throws IllegalStateException
  {
    checkState();
    
    ImmutableSet.Builder<String> entries = ImmutableSet.builder();
    for (String playerName : this.team.getPlayerNameSet()) {
      entries.add(playerName);
    }
    return entries.build();
  }
  
  public int getSize()
    throws IllegalStateException
  {
    checkState();
    
    return this.team.getPlayerNameSet().size();
  }
  
  public void addPlayer(OfflinePlayer player)
    throws IllegalStateException, IllegalArgumentException
  {
    Validate.notNull(player, "OfflinePlayer cannot be null");
    addEntry(player.getName());
  }
  
  public void addEntry(String entry)
    throws IllegalStateException, IllegalArgumentException
  {
    Validate.notNull(entry, "Entry cannot be null");
    CraftScoreboard scoreboard = checkState();
    
    scoreboard.board.addPlayerToTeam(entry, this.team.getName());
  }
  
  public boolean removePlayer(OfflinePlayer player)
    throws IllegalStateException, IllegalArgumentException
  {
    Validate.notNull(player, "OfflinePlayer cannot be null");
    return removeEntry(player.getName());
  }
  
  public boolean removeEntry(String entry)
    throws IllegalStateException, IllegalArgumentException
  {
    Validate.notNull(entry, "Entry cannot be null");
    CraftScoreboard scoreboard = checkState();
    if (!this.team.getPlayerNameSet().contains(entry)) {
      return false;
    }
    scoreboard.board.removePlayerFromTeam(entry, this.team);
    return true;
  }
  
  public boolean hasPlayer(OfflinePlayer player)
    throws IllegalArgumentException, IllegalStateException
  {
    Validate.notNull(player, "OfflinePlayer cannot be null");
    return hasEntry(player.getName());
  }
  
  public boolean hasEntry(String entry)
    throws IllegalArgumentException, IllegalStateException
  {
    Validate.notNull("Entry cannot be null");
    
    checkState();
    
    return this.team.getPlayerNameSet().contains(entry);
  }
  
  public void unregister()
    throws IllegalStateException
  {
    CraftScoreboard scoreboard = checkState();
    
    scoreboard.board.removeTeam(this.team);
  }
  
  public static ScoreboardTeamBase.EnumNameTagVisibility bukkitToNotch(NameTagVisibility visibility)
  {
    switch (visibility)
    {
    case ALWAYS: 
      return ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS;
    case HIDE_FOR_OTHER_TEAMS: 
      return ScoreboardTeamBase.EnumNameTagVisibility.NEVER;
    case HIDE_FOR_OWN_TEAM: 
      return ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS;
    case NEVER: 
      return ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM;
    }
    throw new IllegalArgumentException("Unknown visibility level " + visibility);
  }
  
  public static NameTagVisibility notchToBukkit(ScoreboardTeamBase.EnumNameTagVisibility visibility)
  {
    switch (visibility)
    {
    case ALWAYS: 
      return NameTagVisibility.ALWAYS;
    case HIDE_FOR_OTHER_TEAMS: 
      return NameTagVisibility.NEVER;
    case HIDE_FOR_OWN_TEAM: 
      return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
    case NEVER: 
      return NameTagVisibility.HIDE_FOR_OWN_TEAM;
    }
    throw new IllegalArgumentException("Unknown visibility level " + visibility);
  }
  
  CraftScoreboard checkState()
    throws IllegalStateException
  {
    if (getScoreboard().board.getTeam(this.team.getName()) == null) {
      throw new IllegalStateException("Unregistered scoreboard component");
    }
    return getScoreboard();
  }
  
  public int hashCode()
  {
    int hash = 7;
    hash = 43 * hash + (this.team != null ? this.team.hashCode() : 0);
    return hash;
  }
  
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CraftTeam other = (CraftTeam)obj;
    return (this.team == other.team) || ((this.team != null) && (this.team.equals(other.team)));
  }
}
