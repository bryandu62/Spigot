package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RemoteControlListener
  extends RemoteConnectionThread
{
  private int h;
  private int i;
  private String j;
  private ServerSocket k;
  private String l;
  private Map<SocketAddress, RemoteControlSession> m;
  
  public RemoteControlListener(IMinecraftServer ☃)
  {
    super(☃, "RCON Listener");
    this.h = ☃.a("rcon.port", 0);
    this.l = ☃.a("rcon.password", "");
    this.j = ☃.E();
    this.i = ☃.F();
    if (0 == this.h)
    {
      this.h = (this.i + 10);
      b("Setting default rcon port to " + this.h);
      ☃.a("rcon.port", Integer.valueOf(this.h));
      if (0 == this.l.length()) {
        ☃.a("rcon.password", "");
      }
      ☃.a();
    }
    if (0 == this.j.length()) {
      this.j = "0.0.0.0";
    }
    f();
    this.k = null;
  }
  
  private void f()
  {
    this.m = Maps.newHashMap();
  }
  
  private void g()
  {
    Iterator<Map.Entry<SocketAddress, RemoteControlSession>> ☃ = this.m.entrySet().iterator();
    while (☃.hasNext())
    {
      Map.Entry<SocketAddress, RemoteControlSession> ☃ = (Map.Entry)☃.next();
      if (!((RemoteControlSession)☃.getValue()).c()) {
        ☃.remove();
      }
    }
  }
  
  public void run()
  {
    b("RCON running on " + this.j + ":" + this.h);
    try
    {
      while (this.a) {
        try
        {
          Socket ☃ = this.k.accept();
          ☃.setSoTimeout(500);
          RemoteControlSession ☃ = new RemoteControlSession(this.b, ☃);
          ☃.a();
          this.m.put(☃.getRemoteSocketAddress(), ☃);
          
          g();
        }
        catch (SocketTimeoutException ☃)
        {
          g();
        }
        catch (IOException ☃)
        {
          if (this.a) {
            b("IO: " + ☃.getMessage());
          }
        }
      }
    }
    finally
    {
      b(this.k);
    }
  }
  
  public void a()
  {
    if (0 == this.l.length())
    {
      c("No rcon password set in '" + this.b.b() + "', rcon disabled!");
      return;
    }
    if ((0 >= this.h) || (65535 < this.h))
    {
      c("Invalid rcon port " + this.h + " found in '" + this.b.b() + "', rcon disabled!");
      return;
    }
    if (this.a) {
      return;
    }
    try
    {
      this.k = new ServerSocket(this.h, 0, InetAddress.getByName(this.j));
      this.k.setSoTimeout(500);
      super.a();
    }
    catch (IOException ☃)
    {
      c("Unable to initialise rcon on " + this.j + ":" + this.h + " : " + ☃.getMessage());
    }
  }
}
