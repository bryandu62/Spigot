package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import net.minecraft.server.v1_8_R3.ScoreboardServer;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.spigotmc.AsyncCatcher;

public final class CraftScoreboardManager
  implements ScoreboardManager
{
  private final CraftScoreboard mainScoreboard;
  private final MinecraftServer server;
  private final Collection<CraftScoreboard> scoreboards = new WeakCollection();
  private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap();
  
  public CraftScoreboardManager(MinecraftServer minecraftserver, net.minecraft.server.v1_8_R3.Scoreboard scoreboardServer)
  {
    this.mainScoreboard = new CraftScoreboard(scoreboardServer);
    this.server = minecraftserver;
    this.scoreboards.add(this.mainScoreboard);
  }
  
  public CraftScoreboard getMainScoreboard()
  {
    return this.mainScoreboard;
  }
  
  public CraftScoreboard getNewScoreboard()
  {
    AsyncCatcher.catchOp("scoreboard creation");
    CraftScoreboard scoreboard = new CraftScoreboard(new ScoreboardServer(this.server));
    this.scoreboards.add(scoreboard);
    return scoreboard;
  }
  
  public CraftScoreboard getPlayerBoard(CraftPlayer player)
  {
    CraftScoreboard board = (CraftScoreboard)this.playerBoards.get(player);
    return board == null ? getMainScoreboard() : board;
  }
  
  public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard)
    throws IllegalArgumentException
  {
    Validate.isTrue(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");
    
    CraftScoreboard scoreboard = (CraftScoreboard)bukkitScoreboard;
    net.minecraft.server.v1_8_R3.Scoreboard oldboard = getPlayerBoard(player).getHandle();
    net.minecraft.server.v1_8_R3.Scoreboard newboard = scoreboard.getHandle();
    EntityPlayer entityplayer = player.getHandle();
    if (oldboard == newboard) {
      return;
    }
    if (scoreboard == this.mainScoreboard) {
      this.playerBoards.remove(player);
    } else {
      this.playerBoards.put(player, scoreboard);
    }
    HashSet<ScoreboardObjective> removed = new HashSet();
    for (int i = 0; i < 3; i++)
    {
      ScoreboardObjective scoreboardobjective = oldboard.getObjectiveForSlot(i);
      if ((scoreboardobjective != null) && (!removed.contains(scoreboardobjective)))
      {
        entityplayer.playerConnection.sendPacket(new PacketPlayOutScoreboardObjective(scoreboardobjective, 1));
        removed.add(scoreboardobjective);
      }
    }
    Iterator<?> iterator = oldboard.getTeams().iterator();
    while (iterator.hasNext())
    {
      ScoreboardTeam scoreboardteam = (ScoreboardTeam)iterator.next();
      entityplayer.playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(scoreboardteam, 1));
    }
    this.server.getPlayerList().sendScoreboard((ScoreboardServer)newboard, player.getHandle());
  }
  
  public void removePlayer(Player player)
  {
    this.playerBoards.remove(player);
  }
  
  public Collection<ScoreboardScore> getScoreboardScores(IScoreboardCriteria criteria, String name, Collection<ScoreboardScore> collection)
  {
    Iterator localIterator2;
    for (Iterator localIterator1 = this.scoreboards.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
    {
      CraftScoreboard scoreboard = (CraftScoreboard)localIterator1.next();
      net.minecraft.server.v1_8_R3.Scoreboard board = scoreboard.board;
      localIterator2 = board.getObjectivesForCriteria(criteria).iterator(); continue;ScoreboardObjective objective = (ScoreboardObjective)localIterator2.next();
      collection.add(board.getPlayerScoreForObjective(name, objective));
    }
    return collection;
  }
  
  public void updateAllScoresForList(IScoreboardCriteria criteria, String name, List<EntityPlayer> of)
  {
    for (ScoreboardScore score : getScoreboardScores(criteria, name, new ArrayList())) {
      score.updateForList(of);
    }
  }
}
