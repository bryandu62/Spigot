package org.bukkit.scheduler;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.bukkit.plugin.Plugin;

public abstract interface BukkitScheduler
{
  public abstract int scheduleSyncDelayedTask(Plugin paramPlugin, Runnable paramRunnable, long paramLong);
  
  @Deprecated
  public abstract int scheduleSyncDelayedTask(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable, long paramLong);
  
  public abstract int scheduleSyncDelayedTask(Plugin paramPlugin, Runnable paramRunnable);
  
  @Deprecated
  public abstract int scheduleSyncDelayedTask(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable);
  
  public abstract int scheduleSyncRepeatingTask(Plugin paramPlugin, Runnable paramRunnable, long paramLong1, long paramLong2);
  
  @Deprecated
  public abstract int scheduleSyncRepeatingTask(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable, long paramLong1, long paramLong2);
  
  @Deprecated
  public abstract int scheduleAsyncDelayedTask(Plugin paramPlugin, Runnable paramRunnable, long paramLong);
  
  @Deprecated
  public abstract int scheduleAsyncDelayedTask(Plugin paramPlugin, Runnable paramRunnable);
  
  @Deprecated
  public abstract int scheduleAsyncRepeatingTask(Plugin paramPlugin, Runnable paramRunnable, long paramLong1, long paramLong2);
  
  public abstract <T> Future<T> callSyncMethod(Plugin paramPlugin, Callable<T> paramCallable);
  
  public abstract void cancelTask(int paramInt);
  
  public abstract void cancelTasks(Plugin paramPlugin);
  
  public abstract void cancelAllTasks();
  
  public abstract boolean isCurrentlyRunning(int paramInt);
  
  public abstract boolean isQueued(int paramInt);
  
  public abstract List<BukkitWorker> getActiveWorkers();
  
  public abstract List<BukkitTask> getPendingTasks();
  
  public abstract BukkitTask runTask(Plugin paramPlugin, Runnable paramRunnable)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract BukkitTask runTask(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable)
    throws IllegalArgumentException;
  
  public abstract BukkitTask runTaskAsynchronously(Plugin paramPlugin, Runnable paramRunnable)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract BukkitTask runTaskAsynchronously(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable)
    throws IllegalArgumentException;
  
  public abstract BukkitTask runTaskLater(Plugin paramPlugin, Runnable paramRunnable, long paramLong)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract BukkitTask runTaskLater(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable, long paramLong)
    throws IllegalArgumentException;
  
  public abstract BukkitTask runTaskLaterAsynchronously(Plugin paramPlugin, Runnable paramRunnable, long paramLong)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract BukkitTask runTaskLaterAsynchronously(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable, long paramLong)
    throws IllegalArgumentException;
  
  public abstract BukkitTask runTaskTimer(Plugin paramPlugin, Runnable paramRunnable, long paramLong1, long paramLong2)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract BukkitTask runTaskTimer(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable, long paramLong1, long paramLong2)
    throws IllegalArgumentException;
  
  public abstract BukkitTask runTaskTimerAsynchronously(Plugin paramPlugin, Runnable paramRunnable, long paramLong1, long paramLong2)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract BukkitTask runTaskTimerAsynchronously(Plugin paramPlugin, BukkitRunnable paramBukkitRunnable, long paramLong1, long paramLong2)
    throws IllegalArgumentException;
}
