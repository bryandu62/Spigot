package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RemoteConnectionThread
  implements Runnable
{
  private static final AtomicInteger h = new AtomicInteger(0);
  protected boolean a;
  protected IMinecraftServer b;
  protected final String c;
  protected Thread d;
  protected int e = 5;
  protected List<DatagramSocket> f = Lists.newArrayList();
  protected List<ServerSocket> g = Lists.newArrayList();
  
  protected RemoteConnectionThread(IMinecraftServer ☃, String ☃)
  {
    this.b = ☃;
    this.c = ☃;
    if (this.b.isDebugging()) {
      c("Debugging is enabled, performance maybe reduced!");
    }
  }
  
  public synchronized void a()
  {
    this.d = new Thread(this, this.c + " #" + h.incrementAndGet());
    this.d.start();
    this.a = true;
  }
  
  public boolean c()
  {
    return this.a;
  }
  
  protected void a(String ☃)
  {
    this.b.h(☃);
  }
  
  protected void b(String ☃)
  {
    this.b.info(☃);
  }
  
  protected void c(String ☃)
  {
    this.b.warning(☃);
  }
  
  protected void d(String ☃)
  {
    this.b.g(☃);
  }
  
  protected int d()
  {
    return this.b.I();
  }
  
  protected void a(DatagramSocket ☃)
  {
    a("registerSocket: " + ☃);
    this.f.add(☃);
  }
  
  protected boolean a(DatagramSocket ☃, boolean ☃)
  {
    a("closeSocket: " + ☃);
    if (null == ☃) {
      return false;
    }
    boolean ☃ = false;
    if (!☃.isClosed())
    {
      ☃.close();
      ☃ = true;
    }
    if (☃) {
      this.f.remove(☃);
    }
    return ☃;
  }
  
  protected boolean b(ServerSocket ☃)
  {
    return a(☃, true);
  }
  
  protected boolean a(ServerSocket ☃, boolean ☃)
  {
    a("closeSocket: " + ☃);
    if (null == ☃) {
      return false;
    }
    boolean ☃ = false;
    try
    {
      if (!☃.isClosed())
      {
        ☃.close();
        ☃ = true;
      }
    }
    catch (IOException ☃)
    {
      c("IO: " + ☃.getMessage());
    }
    if (☃) {
      this.g.remove(☃);
    }
    return ☃;
  }
  
  protected void e()
  {
    a(false);
  }
  
  protected void a(boolean ☃)
  {
    int ☃ = 0;
    for (DatagramSocket ☃ : this.f) {
      if (a(☃, false)) {
        ☃++;
      }
    }
    this.f.clear();
    for (ServerSocket ☃ : this.g) {
      if (a(☃, false)) {
        ☃++;
      }
    }
    this.g.clear();
    if ((☃) && (0 < ☃)) {
      c("Force closed " + ☃ + " sockets");
    }
  }
}
