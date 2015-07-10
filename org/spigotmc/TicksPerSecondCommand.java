package org.spigotmc;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TicksPerSecondCommand
  extends Command
{
  public TicksPerSecondCommand(String name)
  {
    super(name);
    this.description = "Gets the current ticks per second for the server";
    this.usageMessage = "/tps";
    setPermission("bukkit.command.tps");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    StringBuilder sb = new StringBuilder(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: ");
    double[] arrayOfDouble;
    int i = (arrayOfDouble = MinecraftServer.getServer().recentTps).length;
    for (int j = 0; j < i; j++)
    {
      double tps = arrayOfDouble[j];
      
      sb.append(format(tps));
      sb.append(", ");
    }
    sender.sendMessage(sb.substring(0, sb.length() - 2));
    
    return true;
  }
  
  private String format(double tps)
  {
    return 
      (tps > 16.0D ? ChatColor.YELLOW : tps > 18.0D ? ChatColor.GREEN : ChatColor.RED).toString() + (tps > 20.0D ? "*" : "") + Math.min(Math.round(tps * 100.0D) / 100.0D, 20.0D);
  }
}
