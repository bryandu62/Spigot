package net.minecraft.server.v1_8_R3;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadWatchdog
  implements Runnable
{
  private static final Logger a = ;
  private final DedicatedServer b;
  private final long c;
  
  public ThreadWatchdog(DedicatedServer ☃)
  {
    this.b = ☃;
    this.c = ☃.aS();
  }
  
  public void run()
  {
    while (this.b.isRunning())
    {
      long ☃ = this.b.aL();
      long ☃ = MinecraftServer.az();
      long ☃ = ☃ - ☃;
      if (☃ > this.c)
      {
        a.fatal("A single server tick took " + String.format("%.2f", new Object[] { Float.valueOf((float)☃ / 1000.0F) }) + " seconds (should be max " + String.format("%.2f", new Object[] { Float.valueOf(0.05F) }) + ")");
        a.fatal("Considering it to be crashed, server will forcibly shutdown.");
        
        ThreadMXBean ☃ = ManagementFactory.getThreadMXBean();
        ThreadInfo[] ☃ = ☃.dumpAllThreads(true, true);
        
        StringBuilder ☃ = new StringBuilder();
        Error ☃ = new Error();
        for (ThreadInfo ☃ : ☃)
        {
          if (☃.getThreadId() == this.b.aM().getId()) {
            ☃.setStackTrace(☃.getStackTrace());
          }
          ☃.append(☃);
          ☃.append("\n");
        }
        CrashReport ☃ = new CrashReport("Watching Server", ☃);
        this.b.b(☃);
        CrashReportSystemDetails ☃ = ☃.a("Thread Dump");
        ☃.a("Threads", ☃);
        
        File ☃ = new File(new File(this.b.y(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
        if (☃.a(☃)) {
          a.error("This crash report has been saved to: " + ☃.getAbsolutePath());
        } else {
          a.error("We were unable to save this crash report to disk.");
        }
        a();
      }
      try
      {
        Thread.sleep(☃ + this.c - ☃);
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
  
  private void a()
  {
    try
    {
      Timer ☃ = new Timer();
      ☃.schedule(new TimerTask()
      {
        public void run()
        {
          Runtime.getRuntime().halt(1);
        }
      }, 10000L);
      
      System.exit(1);
    }
    catch (Throwable ☃)
    {
      Runtime.getRuntime().halt(1);
    }
  }
}
