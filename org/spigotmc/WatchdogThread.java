package org.spigotmc;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class WatchdogThread
  extends Thread
{
  private static WatchdogThread instance;
  private final long timeoutTime;
  private final boolean restart;
  private volatile long lastTick;
  private volatile boolean stopping;
  
  private WatchdogThread(long timeoutTime, boolean restart)
  {
    super("Spigot Watchdog Thread");
    this.timeoutTime = timeoutTime;
    this.restart = restart;
  }
  
  public static void doStart(int timeoutTime, boolean restart)
  {
    if (instance == null)
    {
      instance = new WatchdogThread(timeoutTime * 1000L, restart);
      instance.start();
    }
  }
  
  public static void tick()
  {
    instance.lastTick = System.currentTimeMillis();
  }
  
  public static void doStop()
  {
    if (instance != null) {
      instance.stopping = true;
    }
  }
  
  public void run()
  {
    while (!this.stopping)
    {
      if ((this.lastTick != 0L) && (System.currentTimeMillis() > this.lastTick + this.timeoutTime))
      {
        Logger log = Bukkit.getServer().getLogger();
        log.log(Level.SEVERE, "The server has stopped responding!");
        log.log(Level.SEVERE, "Please report this to http://www.spigotmc.org/");
        log.log(Level.SEVERE, "Be sure to include ALL relevant console errors and Minecraft crash reports");
        log.log(Level.SEVERE, "Spigot version: " + Bukkit.getServer().getVersion());
        if (World.haveWeSilencedAPhysicsCrash)
        {
          log.log(Level.SEVERE, "------------------------------");
          log.log(Level.SEVERE, "During the run of the server, a physics stackoverflow was supressed");
          log.log(Level.SEVERE, "near " + World.blockLocation);
        }
        log.log(Level.SEVERE, "------------------------------");
        log.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to Spigot!):");
        dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServer().primaryThread.getId(), Integer.MAX_VALUE), log);
        log.log(Level.SEVERE, "------------------------------");
        
        log.log(Level.SEVERE, "Entire Thread Dump:");
        ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
        ThreadInfo[] arrayOfThreadInfo1;
        int i = (arrayOfThreadInfo1 = threads).length;
        for (int j = 0; j < i; j++)
        {
          ThreadInfo thread = arrayOfThreadInfo1[j];
          
          dumpThread(thread, log);
        }
        log.log(Level.SEVERE, "------------------------------");
        if (!this.restart) {
          break;
        }
        RestartCommand.restart();
        
        break;
      }
      try
      {
        sleep(10000L);
      }
      catch (InterruptedException localInterruptedException)
      {
        interrupt();
      }
    }
  }
  
  private static void dumpThread(ThreadInfo thread, Logger log)
  {
    log.log(Level.SEVERE, "------------------------------");
    
    log.log(Level.SEVERE, "Current Thread: " + thread.getThreadName());
    log.log(Level.SEVERE, "\tPID: " + thread.getThreadId() + 
      " | Suspended: " + thread.isSuspended() + 
      " | Native: " + thread.isInNative() + 
      " | State: " + thread.getThreadState());
    Object localObject;
    if (thread.getLockedMonitors().length != 0)
    {
      log.log(Level.SEVERE, "\tThread is waiting on monitor(s):");
      i = (localObject = thread.getLockedMonitors()).length;
      for (j = 0; j < i; j++)
      {
        MonitorInfo monitor = localObject[j];
        
        log.log(Level.SEVERE, "\t\tLocked on:" + monitor.getLockedStackFrame());
      }
    }
    log.log(Level.SEVERE, "\tStack:");
    
    int i = (localObject = thread.getStackTrace()).length;
    for (int j = 0; j < i; j++)
    {
      StackTraceElement stack = localObject[j];
      
      log.log(Level.SEVERE, "\t\t" + stack);
    }
  }
}
