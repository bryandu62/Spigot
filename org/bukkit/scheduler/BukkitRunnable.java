package org.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class BukkitRunnable
  implements Runnable
{
  private int taskId = -1;
  
  public synchronized void cancel()
    throws IllegalStateException
  {
    Bukkit.getScheduler().cancelTask(getTaskId());
  }
  
  public synchronized BukkitTask runTask(Plugin plugin)
    throws IllegalArgumentException, IllegalStateException
  {
    checkState();
    return setupId(Bukkit.getScheduler().runTask(plugin, this));
  }
  
  public synchronized BukkitTask runTaskAsynchronously(Plugin plugin)
    throws IllegalArgumentException, IllegalStateException
  {
    checkState();
    return setupId(Bukkit.getScheduler().runTaskAsynchronously(plugin, this));
  }
  
  public synchronized BukkitTask runTaskLater(Plugin plugin, long delay)
    throws IllegalArgumentException, IllegalStateException
  {
    checkState();
    return setupId(Bukkit.getScheduler().runTaskLater(plugin, this, delay));
  }
  
  public synchronized BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay)
    throws IllegalArgumentException, IllegalStateException
  {
    checkState();
    return setupId(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay));
  }
  
  public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period)
    throws IllegalArgumentException, IllegalStateException
  {
    checkState();
    return setupId(Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period));
  }
  
  public synchronized BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period)
    throws IllegalArgumentException, IllegalStateException
  {
    checkState();
    return setupId(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period));
  }
  
  public synchronized int getTaskId()
    throws IllegalStateException
  {
    int id = this.taskId;
    if (id == -1) {
      throw new IllegalStateException("Not scheduled yet");
    }
    return id;
  }
  
  private void checkState()
  {
    if (this.taskId != -1) {
      throw new IllegalStateException("Already scheduled as " + this.taskId);
    }
  }
  
  private BukkitTask setupId(BukkitTask task)
  {
    this.taskId = task.getTaskId();
    return task;
  }
}
