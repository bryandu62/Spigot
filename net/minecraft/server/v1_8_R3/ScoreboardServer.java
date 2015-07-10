package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;

public class ScoreboardServer
  extends Scoreboard
{
  private final MinecraftServer a;
  private final Set<ScoreboardObjective> b = Sets.newHashSet();
  private PersistentScoreboard c;
  
  public ScoreboardServer(MinecraftServer minecraftserver)
  {
    this.a = minecraftserver;
  }
  
  public void handleScoreChanged(ScoreboardScore scoreboardscore)
  {
    super.handleScoreChanged(scoreboardscore);
    if (this.b.contains(scoreboardscore.getObjective())) {
      sendAll(new PacketPlayOutScoreboardScore(scoreboardscore));
    }
    b();
  }
  
  public void handlePlayerRemoved(String s)
  {
    super.handlePlayerRemoved(s);
    sendAll(new PacketPlayOutScoreboardScore(s));
    b();
  }
  
  public void a(String s, ScoreboardObjective scoreboardobjective)
  {
    super.a(s, scoreboardobjective);
    sendAll(new PacketPlayOutScoreboardScore(s, scoreboardobjective));
    b();
  }
  
  public void setDisplaySlot(int i, ScoreboardObjective scoreboardobjective)
  {
    ScoreboardObjective scoreboardobjective1 = getObjectiveForSlot(i);
    
    super.setDisplaySlot(i, scoreboardobjective);
    if ((scoreboardobjective1 != scoreboardobjective) && (scoreboardobjective1 != null)) {
      if (h(scoreboardobjective1) > 0) {
        sendAll(new PacketPlayOutScoreboardDisplayObjective(i, scoreboardobjective));
      } else {
        g(scoreboardobjective1);
      }
    }
    if (scoreboardobjective != null) {
      if (this.b.contains(scoreboardobjective)) {
        sendAll(new PacketPlayOutScoreboardDisplayObjective(i, scoreboardobjective));
      } else {
        e(scoreboardobjective);
      }
    }
    b();
  }
  
  public boolean addPlayerToTeam(String s, String s1)
  {
    if (super.addPlayerToTeam(s, s1))
    {
      ScoreboardTeam scoreboardteam = getTeam(s1);
      
      sendAll(new PacketPlayOutScoreboardTeam(scoreboardteam, Arrays.asList(new String[] { s }), 3));
      b();
      return true;
    }
    return false;
  }
  
  public void removePlayerFromTeam(String s, ScoreboardTeam scoreboardteam)
  {
    super.removePlayerFromTeam(s, scoreboardteam);
    sendAll(new PacketPlayOutScoreboardTeam(scoreboardteam, Arrays.asList(new String[] { s }), 4));
    b();
  }
  
  public void handleObjectiveAdded(ScoreboardObjective scoreboardobjective)
  {
    super.handleObjectiveAdded(scoreboardobjective);
    b();
  }
  
  public void handleObjectiveChanged(ScoreboardObjective scoreboardobjective)
  {
    super.handleObjectiveChanged(scoreboardobjective);
    if (this.b.contains(scoreboardobjective)) {
      sendAll(new PacketPlayOutScoreboardObjective(scoreboardobjective, 2));
    }
    b();
  }
  
  public void handleObjectiveRemoved(ScoreboardObjective scoreboardobjective)
  {
    super.handleObjectiveRemoved(scoreboardobjective);
    if (this.b.contains(scoreboardobjective)) {
      g(scoreboardobjective);
    }
    b();
  }
  
  public void handleTeamAdded(ScoreboardTeam scoreboardteam)
  {
    super.handleTeamAdded(scoreboardteam);
    sendAll(new PacketPlayOutScoreboardTeam(scoreboardteam, 0));
    b();
  }
  
  public void handleTeamChanged(ScoreboardTeam scoreboardteam)
  {
    super.handleTeamChanged(scoreboardteam);
    sendAll(new PacketPlayOutScoreboardTeam(scoreboardteam, 2));
    b();
  }
  
  public void handleTeamRemoved(ScoreboardTeam scoreboardteam)
  {
    super.handleTeamRemoved(scoreboardteam);
    sendAll(new PacketPlayOutScoreboardTeam(scoreboardteam, 1));
    b();
  }
  
  public void a(PersistentScoreboard persistentscoreboard)
  {
    this.c = persistentscoreboard;
  }
  
  protected void b()
  {
    if (this.c != null) {
      this.c.c();
    }
  }
  
  public List<Packet> getScoreboardScorePacketsForObjective(ScoreboardObjective scoreboardobjective)
  {
    ArrayList arraylist = Lists.newArrayList();
    
    arraylist.add(new PacketPlayOutScoreboardObjective(scoreboardobjective, 0));
    for (int i = 0; i < 19; i++) {
      if (getObjectiveForSlot(i) == scoreboardobjective) {
        arraylist.add(new PacketPlayOutScoreboardDisplayObjective(i, scoreboardobjective));
      }
    }
    Iterator iterator = getScoresForObjective(scoreboardobjective).iterator();
    while (iterator.hasNext())
    {
      ScoreboardScore scoreboardscore = (ScoreboardScore)iterator.next();
      
      arraylist.add(new PacketPlayOutScoreboardScore(scoreboardscore));
    }
    return arraylist;
  }
  
  public void e(ScoreboardObjective scoreboardobjective)
  {
    List list = getScoreboardScorePacketsForObjective(scoreboardobjective);
    Iterator iterator = this.a.getPlayerList().v().iterator();
    while (iterator.hasNext())
    {
      EntityPlayer entityplayer = (EntityPlayer)iterator.next();
      if (entityplayer.getBukkitEntity().getScoreboard().getHandle() == this)
      {
        Iterator iterator1 = list.iterator();
        while (iterator1.hasNext())
        {
          Packet packet = (Packet)iterator1.next();
          
          entityplayer.playerConnection.sendPacket(packet);
        }
      }
    }
    this.b.add(scoreboardobjective);
  }
  
  public List<Packet> f(ScoreboardObjective scoreboardobjective)
  {
    ArrayList arraylist = Lists.newArrayList();
    
    arraylist.add(new PacketPlayOutScoreboardObjective(scoreboardobjective, 1));
    for (int i = 0; i < 19; i++) {
      if (getObjectiveForSlot(i) == scoreboardobjective) {
        arraylist.add(new PacketPlayOutScoreboardDisplayObjective(i, scoreboardobjective));
      }
    }
    return arraylist;
  }
  
  public void g(ScoreboardObjective scoreboardobjective)
  {
    List list = f(scoreboardobjective);
    Iterator iterator = this.a.getPlayerList().v().iterator();
    while (iterator.hasNext())
    {
      EntityPlayer entityplayer = (EntityPlayer)iterator.next();
      if (entityplayer.getBukkitEntity().getScoreboard().getHandle() == this)
      {
        Iterator iterator1 = list.iterator();
        while (iterator1.hasNext())
        {
          Packet packet = (Packet)iterator1.next();
          
          entityplayer.playerConnection.sendPacket(packet);
        }
      }
    }
    this.b.remove(scoreboardobjective);
  }
  
  public int h(ScoreboardObjective scoreboardobjective)
  {
    int i = 0;
    for (int j = 0; j < 19; j++) {
      if (getObjectiveForSlot(j) == scoreboardobjective) {
        i++;
      }
    }
    return i;
  }
  
  private void sendAll(Packet packet)
  {
    for (EntityPlayer entityplayer : this.a.getPlayerList().players) {
      if (entityplayer.getBukkitEntity().getScoreboard().getHandle() == this) {
        entityplayer.playerConnection.sendPacket(packet);
      }
    }
  }
}
