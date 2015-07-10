package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.HashSet;
import java.util.List;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerList;
import org.bukkit.entity.Player;

public class LazyPlayerSet
  extends LazyHashSet<Player>
{
  HashSet<Player> makeReference()
  {
    if (this.reference != null) {
      throw new IllegalStateException("Reference already created!");
    }
    List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
    HashSet<Player> reference = new HashSet(players.size());
    for (EntityPlayer player : players) {
      reference.add(player.getBukkitEntity());
    }
    return reference;
  }
}
