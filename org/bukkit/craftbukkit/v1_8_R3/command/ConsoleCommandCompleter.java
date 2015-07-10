package org.bukkit.craftbukkit.v1_8_R3.command;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.libs.jline.console.completer.Completer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.Waitable;

public class ConsoleCommandCompleter
  implements Completer
{
  private final CraftServer server;
  
  public ConsoleCommandCompleter(CraftServer server)
  {
    this.server = server;
  }
  
  public int complete(final String buffer, int cursor, List<CharSequence> candidates)
  {
    Waitable<List<String>> waitable = new Waitable()
    {
      protected List<String> evaluate()
      {
        return ConsoleCommandCompleter.this.server.getCommandMap().tabComplete(ConsoleCommandCompleter.this.server.getConsoleSender(), buffer);
      }
    };
    this.server.getServer().processQueue.add(waitable);
    try
    {
      List<String> offers = (List)waitable.get();
      if (offers == null) {
        return cursor;
      }
      candidates.addAll(offers);
      
      int lastSpace = buffer.lastIndexOf(' ');
      if (lastSpace == -1) {
        return cursor - buffer.length();
      }
      return cursor - (buffer.length() - lastSpace - 1);
    }
    catch (ExecutionException e)
    {
      this.server.getLogger().log(Level.WARNING, "Unhandled exception when tab completing", e);
    }
    catch (InterruptedException localInterruptedException)
    {
      Thread.currentThread().interrupt();
    }
    return cursor;
  }
}
