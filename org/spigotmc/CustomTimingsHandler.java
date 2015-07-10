package org.spigotmc;

import java.io.PrintStream;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

public class CustomTimingsHandler
{
  private static Queue<CustomTimingsHandler> HANDLERS = new ConcurrentLinkedQueue();
  private final String name;
  private final CustomTimingsHandler parent;
  private long count = 0L;
  private long start = 0L;
  private long timingDepth = 0L;
  private long totalTime = 0L;
  private long curTickTotal = 0L;
  private long violations = 0L;
  
  public CustomTimingsHandler(String name)
  {
    this(name, null);
  }
  
  public CustomTimingsHandler(String name, CustomTimingsHandler parent)
  {
    this.name = name;
    this.parent = parent;
    HANDLERS.add(this);
  }
  
  public static void printTimings(PrintStream printStream)
  {
    printStream.println("Minecraft");
    for (CustomTimingsHandler timings : HANDLERS)
    {
      long time = timings.totalTime;
      long count = timings.count;
      if (count != 0L)
      {
        long avg = time / count;
        
        printStream.println("    " + timings.name + " Time: " + time + " Count: " + count + " Avg: " + avg + " Violations: " + timings.violations);
      }
    }
    printStream.println("# Version " + Bukkit.getVersion());
    int entities = 0;
    int livingEntities = 0;
    for (World world : Bukkit.getWorlds())
    {
      entities += world.getEntities().size();
      livingEntities += world.getLivingEntities().size();
    }
    printStream.println("# Entities " + entities);
    printStream.println("# LivingEntities " + livingEntities);
  }
  
  public static void reload()
  {
    if (Bukkit.getPluginManager().useTimings()) {
      for (CustomTimingsHandler timings : HANDLERS) {
        timings.reset();
      }
    }
    org.bukkit.command.defaults.TimingsCommand.timingStart = System.nanoTime();
  }
  
  public static void tick()
  {
    if (Bukkit.getPluginManager().useTimings()) {
      for (CustomTimingsHandler timings : HANDLERS)
      {
        if (timings.curTickTotal > 50000000L)
        {
          CustomTimingsHandler tmp45_44 = timings;tmp45_44.violations = ((tmp45_44.violations + Math.ceil(tmp45_44.curTickTotal / 50000000L)));
        }
        tmp45_44.curTickTotal = 0L;
        tmp45_44.timingDepth = 0L;
      }
    }
  }
  
  public void startTiming()
  {
    if ((Bukkit.getPluginManager().useTimings()) && (++this.timingDepth == 1L))
    {
      this.start = System.nanoTime();
      if (this.parent != null) {
        if (++this.parent.timingDepth == 1L) {
          this.parent.start = this.start;
        }
      }
    }
  }
  
  public void stopTiming()
  {
    if (Bukkit.getPluginManager().useTimings())
    {
      if ((--this.timingDepth != 0L) || (this.start == 0L)) {
        return;
      }
      long diff = System.nanoTime() - this.start;
      this.totalTime += diff;
      this.curTickTotal += diff;
      this.count += 1L;
      this.start = 0L;
      if (this.parent != null) {
        this.parent.stopTiming();
      }
    }
  }
  
  public void reset()
  {
    this.count = 0L;
    this.violations = 0L;
    this.curTickTotal = 0L;
    this.totalTime = 0L;
    this.start = 0L;
    this.timingDepth = 0L;
  }
}
