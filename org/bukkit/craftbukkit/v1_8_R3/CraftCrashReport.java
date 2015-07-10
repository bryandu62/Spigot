package org.bukkit.craftbukkit.v1_8_R3;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Warning.WarningState;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class CraftCrashReport
  implements Callable<Object>
{
  public Object call()
    throws Exception
  {
    StringWriter value = new StringWriter();
    try
    {
      value.append("\n   Running: ").append(Bukkit.getName()).append(" version ").append(Bukkit.getVersion()).append(" (Implementing API version ").append(Bukkit.getBukkitVersion()).append(") ").append(String.valueOf(MinecraftServer.getServer().getOnlineMode()));
      value.append("\n   Plugins: {");
      Plugin[] arrayOfPlugin;
      int i = (arrayOfPlugin = Bukkit.getPluginManager().getPlugins()).length;
      for (int j = 0; j < i; j++)
      {
        Plugin plugin = arrayOfPlugin[j];
        PluginDescriptionFile description = plugin.getDescription();
        value.append(' ').append(description.getFullName()).append(' ').append(description.getMain()).append(' ').append(Arrays.toString(description.getAuthors().toArray())).append(',');
      }
      value.append("}\n   Warnings: ").append(Bukkit.getWarningState().name());
      value.append("\n   Reload Count: ").append(String.valueOf(MinecraftServer.getServer().server.reloadCount));
      value.append("\n   Threads: {");
      for (Map.Entry<Thread, ? extends Object[]> entry : Thread.getAllStackTraces().entrySet()) {
        value.append(' ').append(((Thread)entry.getKey()).getState().name()).append(' ').append(((Thread)entry.getKey()).getName()).append(": ").append(Arrays.toString((Object[])entry.getValue())).append(',');
      }
      value.append("}\n   ").append(Bukkit.getScheduler().toString());
    }
    catch (Throwable t)
    {
      value.append("\n   Failed to handle CraftCrashReport:\n");
      PrintWriter writer = new PrintWriter(value);
      t.printStackTrace(writer);
      writer.flush();
    }
    return value.toString();
  }
}
