package org.bukkit.craftbukkit.v1_8_R3.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

class CraftAsyncDebugger
{
  private CraftAsyncDebugger next = null;
  private final int expiry;
  private final Plugin plugin;
  private final Class<? extends Runnable> clazz;
  
  CraftAsyncDebugger(int expiry, Plugin plugin, Class<? extends Runnable> clazz)
  {
    this.expiry = expiry;
    this.plugin = plugin;
    this.clazz = clazz;
  }
  
  final CraftAsyncDebugger getNextHead(int time)
  {
    CraftAsyncDebugger current = this;
    CraftAsyncDebugger next;
    while ((time > current.expiry) && ((next = current.next) != null))
    {
      CraftAsyncDebugger next;
      current = next;
    }
    return current;
  }
  
  final CraftAsyncDebugger setNext(CraftAsyncDebugger next)
  {
    return this.next = next;
  }
  
  StringBuilder debugTo(StringBuilder string)
  {
    for (CraftAsyncDebugger next = this; next != null; next = next.next) {
      string.append(next.plugin.getDescription().getName()).append(':').append(next.clazz.getName()).append('@').append(next.expiry).append(',');
    }
    return string;
  }
}
