package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class RemoteStatusListener
  extends RemoteConnectionThread
{
  private long h;
  private int i;
  private int j;
  private int k;
  private String l;
  private String m;
  private DatagramSocket n;
  private byte[] o = new byte['ִ'];
  private DatagramPacket p;
  private Map<SocketAddress, String> q;
  private String r;
  private String s;
  private Map<SocketAddress, RemoteStatusChallenge> t;
  private long u;
  private RemoteStatusReply v;
  private long w;
  
  public RemoteStatusListener(IMinecraftServer ☃)
  {
    super(☃, "Query Listener");
    
    this.i = ☃.a("query.port", 0);
    this.s = ☃.E();
    this.j = ☃.F();
    this.l = ☃.G();
    this.k = ☃.J();
    this.m = ☃.U();
    
    this.w = 0L;
    
    this.r = "0.0.0.0";
    if ((0 == this.s.length()) || (this.r.equals(this.s)))
    {
      this.s = "0.0.0.0";
      try
      {
        InetAddress ☃ = InetAddress.getLocalHost();
        this.r = ☃.getHostAddress();
      }
      catch (UnknownHostException ☃)
      {
        c("Unable to determine local host IP, please set server-ip in '" + ☃.b() + "' : " + ☃.getMessage());
      }
    }
    else
    {
      this.r = this.s;
    }
    if (0 == this.i)
    {
      this.i = this.j;
      b("Setting default query port to " + this.i);
      ☃.a("query.port", Integer.valueOf(this.i));
      ☃.a("debug", Boolean.valueOf(false));
      ☃.a();
    }
    this.q = Maps.newHashMap();
    this.v = new RemoteStatusReply(1460);
    this.t = Maps.newHashMap();
    this.u = new Date().getTime();
  }
  
  private void a(byte[] ☃, DatagramPacket ☃)
    throws IOException
  {
    this.n.send(new DatagramPacket(☃, ☃.length, ☃.getSocketAddress()));
  }
  
  private boolean a(DatagramPacket ☃)
    throws IOException
  {
    byte[] ☃ = ☃.getData();
    int ☃ = ☃.getLength();
    SocketAddress ☃ = ☃.getSocketAddress();
    a("Packet len " + ☃ + " [" + ☃ + "]");
    if ((3 > ☃) || (-2 != ☃[0]) || (-3 != ☃[1]))
    {
      a("Invalid packet [" + ☃ + "]");
      return false;
    }
    a("Packet '" + StatusChallengeUtils.a(☃[2]) + "' [" + ☃ + "]");
    switch (☃[2])
    {
    case 9: 
      d(☃);
      a("Challenge [" + ☃ + "]");
      return true;
    case 0: 
      if (!c(☃).booleanValue())
      {
        a("Invalid challenge [" + ☃ + "]");
        return false;
      }
      if (15 == ☃)
      {
        a(b(☃), ☃);
        a("Rules [" + ☃ + "]");
      }
      else
      {
        RemoteStatusReply ☃ = new RemoteStatusReply(1460);
        ☃.a(0);
        ☃.a(a(☃.getSocketAddress()));
        ☃.a(this.l);
        ☃.a("SMP");
        ☃.a(this.m);
        ☃.a(Integer.toString(d()));
        ☃.a(Integer.toString(this.k));
        ☃.a((short)this.j);
        ☃.a(this.r);
        
        a(☃.a(), ☃);
        a("Status [" + ☃ + "]");
      }
      break;
    }
    return true;
  }
  
  private byte[] b(DatagramPacket ☃)
    throws IOException
  {
    long ☃ = MinecraftServer.az();
    if (☃ < this.w + 5000L)
    {
      byte[] ☃ = this.v.a();
      byte[] ☃ = a(☃.getSocketAddress());
      ☃[1] = ☃[0];
      ☃[2] = ☃[1];
      ☃[3] = ☃[2];
      ☃[4] = ☃[3];
      
      return ☃;
    }
    this.w = ☃;
    
    this.v.b();
    this.v.a(0);
    this.v.a(a(☃.getSocketAddress()));
    this.v.a("splitnum");
    this.v.a(128);
    this.v.a(0);
    
    this.v.a("hostname");
    this.v.a(this.l);
    this.v.a("gametype");
    this.v.a("SMP");
    this.v.a("game_id");
    this.v.a("MINECRAFT");
    this.v.a("version");
    this.v.a(this.b.getVersion());
    this.v.a("plugins");
    this.v.a(this.b.getPlugins());
    this.v.a("map");
    this.v.a(this.m);
    this.v.a("numplayers");
    this.v.a("" + d());
    this.v.a("maxplayers");
    this.v.a("" + this.k);
    this.v.a("hostport");
    this.v.a("" + this.j);
    this.v.a("hostip");
    this.v.a(this.r);
    this.v.a(0);
    this.v.a(1);
    
    this.v.a("player_");
    this.v.a(0);
    
    String[] ☃ = this.b.getPlayers();
    for (String ☃ : ☃) {
      this.v.a(☃);
    }
    this.v.a(0);
    
    return this.v.a();
  }
  
  private byte[] a(SocketAddress ☃)
  {
    return ((RemoteStatusChallenge)this.t.get(☃)).c();
  }
  
  private Boolean c(DatagramPacket ☃)
  {
    SocketAddress ☃ = ☃.getSocketAddress();
    if (!this.t.containsKey(☃)) {
      return Boolean.valueOf(false);
    }
    byte[] ☃ = ☃.getData();
    if (((RemoteStatusChallenge)this.t.get(☃)).a() != StatusChallengeUtils.c(☃, 7, ☃.getLength())) {
      return Boolean.valueOf(false);
    }
    return Boolean.valueOf(true);
  }
  
  private void d(DatagramPacket ☃)
    throws IOException
  {
    RemoteStatusChallenge ☃ = new RemoteStatusChallenge(☃);
    this.t.put(☃.getSocketAddress(), ☃);
    
    a(☃.b(), ☃);
  }
  
  private void f()
  {
    if (!this.a) {
      return;
    }
    long ☃ = MinecraftServer.az();
    if (☃ < this.h + 30000L) {
      return;
    }
    this.h = ☃;
    
    Iterator<Map.Entry<SocketAddress, RemoteStatusChallenge>> ☃ = this.t.entrySet().iterator();
    while (☃.hasNext())
    {
      Map.Entry<SocketAddress, RemoteStatusChallenge> ☃ = (Map.Entry)☃.next();
      if (((RemoteStatusChallenge)☃.getValue()).a(☃).booleanValue()) {
        ☃.remove();
      }
    }
  }
  
  public void run()
  {
    b("Query running on " + this.s + ":" + this.i);
    this.h = MinecraftServer.az();
    this.p = new DatagramPacket(this.o, this.o.length);
    try
    {
      while (this.a) {
        try
        {
          this.n.receive(this.p);
          
          f();
          
          a(this.p);
        }
        catch (SocketTimeoutException ☃)
        {
          f();
        }
        catch (PortUnreachableException localPortUnreachableException) {}catch (IOException ☃)
        {
          a(☃);
        }
      }
    }
    finally
    {
      e();
    }
  }
  
  public void a()
  {
    if (this.a) {
      return;
    }
    if ((0 >= this.i) || (65535 < this.i))
    {
      c("Invalid query port " + this.i + " found in '" + this.b.b() + "' (queries disabled)");
      return;
    }
    if (g()) {
      super.a();
    }
  }
  
  private void a(Exception ☃)
  {
    if (!this.a) {
      return;
    }
    c("Unexpected exception, buggy JRE? (" + ☃.toString() + ")");
    if (!g())
    {
      d("Failed to recover from buggy JRE, shutting down!");
      this.a = false;
    }
  }
  
  private boolean g()
  {
    try
    {
      this.n = new DatagramSocket(this.i, InetAddress.getByName(this.s));
      a(this.n);
      this.n.setSoTimeout(500);
      return true;
    }
    catch (SocketException ☃)
    {
      c("Unable to initialise query system on " + this.s + ":" + this.i + " (Socket): " + ☃.getMessage());
    }
    catch (UnknownHostException ☃)
    {
      c("Unable to initialise query system on " + this.s + ":" + this.i + " (Unknown Host): " + ☃.getMessage());
    }
    catch (Exception ☃)
    {
      c("Unable to initialise query system on " + this.s + ":" + this.i + " (E): " + ☃.getMessage());
    }
    return false;
  }
  
  class RemoteStatusChallenge
  {
    private long time;
    private int token;
    private byte[] identity;
    private byte[] response;
    private String f;
    
    public RemoteStatusChallenge(DatagramPacket ☃)
    {
      this.time = new Date().getTime();
      byte[] ☃ = ☃.getData();
      this.identity = new byte[4];
      this.identity[0] = ☃[3];
      this.identity[1] = ☃[4];
      this.identity[2] = ☃[5];
      this.identity[3] = ☃[6];
      this.f = new String(this.identity);
      this.token = new Random().nextInt(16777216);
      this.response = String.format("\t%s%d\000", new Object[] { this.f, Integer.valueOf(this.token) }).getBytes();
    }
    
    public Boolean a(long ☃)
    {
      return Boolean.valueOf(this.time < ☃);
    }
    
    public int a()
    {
      return this.token;
    }
    
    public byte[] b()
    {
      return this.response;
    }
    
    public byte[] c()
    {
      return this.identity;
    }
  }
}
