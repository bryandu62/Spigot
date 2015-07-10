package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MojangStatisticsGenerator
{
  private final Map<String, Object> a = Maps.newHashMap();
  private final Map<String, Object> b = Maps.newHashMap();
  private final String c = UUID.randomUUID().toString();
  private final URL d;
  private final IMojangStatistics e;
  private final Timer f = new Timer("Snooper Timer", true);
  private final Object g = new Object();
  private final long h;
  private boolean i;
  private int j;
  
  public MojangStatisticsGenerator(String ☃, IMojangStatistics ☃, long ☃)
  {
    try
    {
      this.d = new URL("http://snoop.minecraft.net/" + ☃ + "?version=" + 2);
    }
    catch (MalformedURLException ☃)
    {
      throw new IllegalArgumentException();
    }
    this.e = ☃;
    this.h = ☃;
  }
  
  public void a()
  {
    if (this.i) {
      return;
    }
    this.i = true;
    
    h();
    
    this.f.schedule(new TimerTask()
    {
      public void run()
      {
        if (!MojangStatisticsGenerator.a(MojangStatisticsGenerator.this).getSnooperEnabled()) {
          return;
        }
        Map<String, Object> ☃;
        synchronized (MojangStatisticsGenerator.b(MojangStatisticsGenerator.this))
        {
          ☃ = Maps.newHashMap(MojangStatisticsGenerator.c(MojangStatisticsGenerator.this));
          if (MojangStatisticsGenerator.d(MojangStatisticsGenerator.this) == 0) {
            ☃.putAll(MojangStatisticsGenerator.e(MojangStatisticsGenerator.this));
          }
          ☃.put("snooper_count", Integer.valueOf(MojangStatisticsGenerator.f(MojangStatisticsGenerator.this)));
          ☃.put("snooper_token", MojangStatisticsGenerator.g(MojangStatisticsGenerator.this));
        }
        HttpUtilities.a(MojangStatisticsGenerator.h(MojangStatisticsGenerator.this), ☃, true);
      }
    }, 0L, 900000L);
  }
  
  private void h()
  {
    i();
    
    a("snooper_token", this.c);
    b("snooper_token", this.c);
    b("os_name", System.getProperty("os.name"));
    b("os_version", System.getProperty("os.version"));
    b("os_architecture", System.getProperty("os.arch"));
    b("java_version", System.getProperty("java.version"));
    a("version", "1.8.7");
    
    this.e.b(this);
  }
  
  private void i()
  {
    RuntimeMXBean ☃ = ManagementFactory.getRuntimeMXBean();
    List<String> ☃ = ☃.getInputArguments();
    int ☃ = 0;
    for (String ☃ : ☃) {
      if (☃.startsWith("-X")) {
        a("jvm_arg[" + ☃++ + "]", ☃);
      }
    }
    a("jvm_args", Integer.valueOf(☃));
  }
  
  public void b()
  {
    b("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
    b("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
    b("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
    b("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
    
    this.e.a(this);
  }
  
  public void a(String ☃, Object ☃)
  {
    synchronized (this.g)
    {
      this.b.put(☃, ☃);
    }
  }
  
  public void b(String ☃, Object ☃)
  {
    synchronized (this.g)
    {
      this.a.put(☃, ☃);
    }
  }
  
  public boolean d()
  {
    return this.i;
  }
  
  public void e()
  {
    this.f.cancel();
  }
  
  public long g()
  {
    return this.h;
  }
}
