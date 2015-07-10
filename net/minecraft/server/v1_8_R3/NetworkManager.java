package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager
  extends SimpleChannelInboundHandler<Packet>
{
  private static final Logger g = ;
  public static final Marker a = MarkerManager.getMarker("NETWORK");
  public static final Marker b = MarkerManager.getMarker("NETWORK_PACKETS", a);
  public static final AttributeKey<EnumProtocol> c = AttributeKey.valueOf("protocol");
  public static final LazyInitVar<NioEventLoopGroup> d = new LazyInitVar()
  {
    protected NioEventLoopGroup a()
    {
      return new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Client IO #%d").setDaemon(true).build());
    }
    
    protected Object init()
    {
      return a();
    }
  };
  public static final LazyInitVar<EpollEventLoopGroup> e = new LazyInitVar()
  {
    protected EpollEventLoopGroup a()
    {
      return new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
    }
    
    protected Object init()
    {
      return a();
    }
  };
  public static final LazyInitVar<LocalEventLoopGroup> f = new LazyInitVar()
  {
    protected LocalEventLoopGroup a()
    {
      return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
    }
    
    protected Object init()
    {
      return a();
    }
  };
  private final EnumProtocolDirection h;
  private final Queue<QueuedPacket> i = Queues.newConcurrentLinkedQueue();
  private final ReentrantReadWriteLock j = new ReentrantReadWriteLock();
  public Channel channel;
  public SocketAddress l;
  public UUID spoofedUUID;
  public Property[] spoofedProfile;
  public boolean preparing = true;
  private PacketListener m;
  private IChatBaseComponent n;
  private boolean o;
  private boolean p;
  
  public NetworkManager(EnumProtocolDirection enumprotocoldirection)
  {
    this.h = enumprotocoldirection;
  }
  
  public void channelActive(ChannelHandlerContext channelhandlercontext)
    throws Exception
  {
    super.channelActive(channelhandlercontext);
    this.channel = channelhandlercontext.channel();
    this.l = this.channel.remoteAddress();
    
    this.preparing = false;
    try
    {
      a(EnumProtocol.HANDSHAKING);
    }
    catch (Throwable throwable)
    {
      g.fatal(throwable);
    }
  }
  
  public void a(EnumProtocol enumprotocol)
  {
    this.channel.attr(c).set(enumprotocol);
    this.channel.config().setAutoRead(true);
    g.debug("Enabled auto read");
  }
  
  public void channelInactive(ChannelHandlerContext channelhandlercontext)
    throws Exception
  {
    close(new ChatMessage("disconnect.endOfStream", new Object[0]));
  }
  
  public void exceptionCaught(ChannelHandlerContext channelhandlercontext, Throwable throwable)
    throws Exception
  {
    ChatMessage chatmessage;
    ChatMessage chatmessage;
    if ((throwable instanceof TimeoutException)) {
      chatmessage = new ChatMessage("disconnect.timeout", new Object[0]);
    } else {
      chatmessage = new ChatMessage("disconnect.genericReason", new Object[] { "Internal Exception: " + throwable });
    }
    close(chatmessage);
    if (MinecraftServer.getServer().isDebugging()) {
      throwable.printStackTrace();
    }
  }
  
  protected void a(ChannelHandlerContext channelhandlercontext, Packet packet)
    throws Exception
  {
    if (this.channel.isOpen()) {
      try
      {
        packet.a(this.m);
      }
      catch (CancelledPacketHandleException localCancelledPacketHandleException) {}
    }
  }
  
  public void a(PacketListener packetlistener)
  {
    Validate.notNull(packetlistener, "packetListener", new Object[0]);
    g.debug("Set listener of {} to {}", new Object[] { this, packetlistener });
    this.m = packetlistener;
  }
  
  /* Error */
  public void handle(Packet packet)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 282	net/minecraft/server/v1_8_R3/NetworkManager:g	()Z
    //   4: ifeq +16 -> 20
    //   7: aload_0
    //   8: invokespecial 284	net/minecraft/server/v1_8_R3/NetworkManager:m	()V
    //   11: aload_0
    //   12: aload_1
    //   13: aconst_null
    //   14: invokespecial 287	net/minecraft/server/v1_8_R3/NetworkManager:a	(Lnet/minecraft/server/v1_8_R3/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
    //   17: goto +58 -> 75
    //   20: aload_0
    //   21: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   24: invokevirtual 291	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   27: invokevirtual 294	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   30: aload_0
    //   31: getfield 121	net/minecraft/server/v1_8_R3/NetworkManager:i	Ljava/util/Queue;
    //   34: new 24	net/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket
    //   37: dup
    //   38: aload_1
    //   39: aconst_null
    //   40: invokespecial 296	net/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket:<init>	(Lnet/minecraft/server/v1_8_R3/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
    //   43: invokeinterface 302 2 0
    //   48: pop
    //   49: goto +16 -> 65
    //   52: astore_2
    //   53: aload_0
    //   54: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   57: invokevirtual 291	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   60: invokevirtual 305	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   63: aload_2
    //   64: athrow
    //   65: aload_0
    //   66: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   69: invokevirtual 291	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   72: invokevirtual 305	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   75: return
    // Line number table:
    //   Java source line #139	-> byte code offset #0
    //   Java source line #140	-> byte code offset #7
    //   Java source line #141	-> byte code offset #11
    //   Java source line #142	-> byte code offset #17
    //   Java source line #143	-> byte code offset #20
    //   Java source line #146	-> byte code offset #30
    //   Java source line #147	-> byte code offset #49
    //   Java source line #148	-> byte code offset #53
    //   Java source line #149	-> byte code offset #63
    //   Java source line #148	-> byte code offset #65
    //   Java source line #152	-> byte code offset #75
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	76	0	this	NetworkManager
    //   0	76	1	packet	Packet
    //   52	12	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   30	52	52	finally
  }
  
  /* Error */
  public void a(Packet packet, GenericFutureListener<? extends Future<? super Void>> genericfuturelistener, GenericFutureListener<? extends Future<? super Void>>... agenericfuturelistener)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 282	net/minecraft/server/v1_8_R3/NetworkManager:g	()Z
    //   4: ifeq +24 -> 28
    //   7: aload_0
    //   8: invokespecial 284	net/minecraft/server/v1_8_R3/NetworkManager:m	()V
    //   11: aload_0
    //   12: aload_1
    //   13: aload_3
    //   14: iconst_0
    //   15: aload_2
    //   16: invokestatic 311	org/apache/commons/lang3/ArrayUtils:add	([Ljava/lang/Object;ILjava/lang/Object;)[Ljava/lang/Object;
    //   19: checkcast 313	[Lio/netty/util/concurrent/GenericFutureListener;
    //   22: invokespecial 287	net/minecraft/server/v1_8_R3/NetworkManager:a	(Lnet/minecraft/server/v1_8_R3/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
    //   25: goto +68 -> 93
    //   28: aload_0
    //   29: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   32: invokevirtual 291	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   35: invokevirtual 294	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
    //   38: aload_0
    //   39: getfield 121	net/minecraft/server/v1_8_R3/NetworkManager:i	Ljava/util/Queue;
    //   42: new 24	net/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket
    //   45: dup
    //   46: aload_1
    //   47: aload_3
    //   48: iconst_0
    //   49: aload_2
    //   50: invokestatic 311	org/apache/commons/lang3/ArrayUtils:add	([Ljava/lang/Object;ILjava/lang/Object;)[Ljava/lang/Object;
    //   53: checkcast 313	[Lio/netty/util/concurrent/GenericFutureListener;
    //   56: invokespecial 296	net/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket:<init>	(Lnet/minecraft/server/v1_8_R3/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
    //   59: invokeinterface 302 2 0
    //   64: pop
    //   65: goto +18 -> 83
    //   68: astore 4
    //   70: aload_0
    //   71: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   74: invokevirtual 291	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   77: invokevirtual 305	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   80: aload 4
    //   82: athrow
    //   83: aload_0
    //   84: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   87: invokevirtual 291	java/util/concurrent/locks/ReentrantReadWriteLock:writeLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   90: invokevirtual 305	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
    //   93: return
    // Line number table:
    //   Java source line #155	-> byte code offset #0
    //   Java source line #156	-> byte code offset #7
    //   Java source line #157	-> byte code offset #11
    //   Java source line #158	-> byte code offset #25
    //   Java source line #159	-> byte code offset #28
    //   Java source line #162	-> byte code offset #38
    //   Java source line #163	-> byte code offset #65
    //   Java source line #164	-> byte code offset #70
    //   Java source line #165	-> byte code offset #80
    //   Java source line #164	-> byte code offset #83
    //   Java source line #168	-> byte code offset #93
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	94	0	this	NetworkManager
    //   0	94	1	packet	Packet
    //   0	94	2	genericfuturelistener	GenericFutureListener<? extends Future<? super Void>>
    //   0	94	3	agenericfuturelistener	GenericFutureListener[]
    //   68	13	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   38	68	68	finally
  }
  
  private void a(final Packet packet, final GenericFutureListener<? extends Future<? super Void>>[] agenericfuturelistener)
  {
    final EnumProtocol enumprotocol = EnumProtocol.a(packet);
    final EnumProtocol enumprotocol1 = (EnumProtocol)this.channel.attr(c).get();
    if (enumprotocol1 != enumprotocol)
    {
      g.debug("Disabled auto read");
      this.channel.config().setAutoRead(false);
    }
    if (this.channel.eventLoop().inEventLoop())
    {
      if (enumprotocol != enumprotocol1) {
        a(enumprotocol);
      }
      ChannelFuture channelfuture = this.channel.writeAndFlush(packet);
      if (agenericfuturelistener != null) {
        channelfuture.addListeners(agenericfuturelistener);
      }
      channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
    else
    {
      this.channel.eventLoop().execute(new Runnable()
      {
        public void run()
        {
          if (enumprotocol != enumprotocol1) {
            NetworkManager.this.a(enumprotocol);
          }
          ChannelFuture channelfuture = NetworkManager.this.channel.writeAndFlush(packet);
          if (agenericfuturelistener != null) {
            channelfuture.addListeners(agenericfuturelistener);
          }
          channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
      });
    }
  }
  
  /* Error */
  private void m()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 146	net/minecraft/server/v1_8_R3/NetworkManager:channel	Lio/netty/channel/Channel;
    //   4: ifnull +91 -> 95
    //   7: aload_0
    //   8: getfield 146	net/minecraft/server/v1_8_R3/NetworkManager:channel	Lio/netty/channel/Channel;
    //   11: invokeinterface 256 1 0
    //   16: ifeq +79 -> 95
    //   19: aload_0
    //   20: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   23: invokevirtual 369	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   26: invokevirtual 370	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
    //   29: goto +28 -> 57
    //   32: aload_0
    //   33: getfield 121	net/minecraft/server/v1_8_R3/NetworkManager:i	Ljava/util/Queue;
    //   36: invokeinterface 373 1 0
    //   41: checkcast 24	net/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket
    //   44: astore_1
    //   45: aload_0
    //   46: aload_1
    //   47: invokestatic 377	net/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket:access$0	(Lnet/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket;)Lnet/minecraft/server/v1_8_R3/Packet;
    //   50: aload_1
    //   51: invokestatic 381	net/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket:access$1	(Lnet/minecraft/server/v1_8_R3/NetworkManager$QueuedPacket;)[Lio/netty/util/concurrent/GenericFutureListener;
    //   54: invokespecial 287	net/minecraft/server/v1_8_R3/NetworkManager:a	(Lnet/minecraft/server/v1_8_R3/Packet;[Lio/netty/util/concurrent/GenericFutureListener;)V
    //   57: aload_0
    //   58: getfield 121	net/minecraft/server/v1_8_R3/NetworkManager:i	Ljava/util/Queue;
    //   61: invokeinterface 384 1 0
    //   66: ifeq -34 -> 32
    //   69: goto +16 -> 85
    //   72: astore_2
    //   73: aload_0
    //   74: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   77: invokevirtual 369	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   80: invokevirtual 385	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   83: aload_2
    //   84: athrow
    //   85: aload_0
    //   86: getfield 124	net/minecraft/server/v1_8_R3/NetworkManager:j	Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   89: invokevirtual 369	java/util/concurrent/locks/ReentrantReadWriteLock:readLock	()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   92: invokevirtual 385	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
    //   95: return
    // Line number table:
    //   Java source line #212	-> byte code offset #0
    //   Java source line #213	-> byte code offset #19
    //   Java source line #216	-> byte code offset #29
    //   Java source line #217	-> byte code offset #32
    //   Java source line #219	-> byte code offset #45
    //   Java source line #216	-> byte code offset #57
    //   Java source line #221	-> byte code offset #69
    //   Java source line #222	-> byte code offset #73
    //   Java source line #223	-> byte code offset #83
    //   Java source line #222	-> byte code offset #85
    //   Java source line #226	-> byte code offset #95
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	96	0	this	NetworkManager
    //   44	7	1	networkmanager_queuedpacket	QueuedPacket
    //   72	12	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   29	72	72	finally
  }
  
  public void a()
  {
    m();
    if ((this.m instanceof IUpdatePlayerListBox)) {
      ((IUpdatePlayerListBox)this.m).c();
    }
    this.channel.flush();
  }
  
  public SocketAddress getSocketAddress()
  {
    return this.l;
  }
  
  public void close(IChatBaseComponent ichatbasecomponent)
  {
    this.preparing = false;
    if (this.channel.isOpen())
    {
      this.channel.close();
      this.n = ichatbasecomponent;
    }
  }
  
  public boolean c()
  {
    return ((this.channel instanceof LocalChannel)) || ((this.channel instanceof LocalServerChannel));
  }
  
  public void a(SecretKey secretkey)
  {
    this.o = true;
    this.channel.pipeline().addBefore("splitter", "decrypt", new PacketDecrypter(MinecraftEncryption.a(2, secretkey)));
    this.channel.pipeline().addBefore("prepender", "encrypt", new PacketEncrypter(MinecraftEncryption.a(1, secretkey)));
  }
  
  public boolean g()
  {
    return (this.channel != null) && (this.channel.isOpen());
  }
  
  public boolean h()
  {
    return this.channel == null;
  }
  
  public PacketListener getPacketListener()
  {
    return this.m;
  }
  
  public IChatBaseComponent j()
  {
    return this.n;
  }
  
  public void k()
  {
    this.channel.config().setAutoRead(false);
  }
  
  public void a(int i)
  {
    if (i >= 0)
    {
      if ((this.channel.pipeline().get("decompress") instanceof PacketDecompressor)) {
        ((PacketDecompressor)this.channel.pipeline().get("decompress")).a(i);
      } else {
        this.channel.pipeline().addBefore("decoder", "decompress", new PacketDecompressor(i));
      }
      if ((this.channel.pipeline().get("compress") instanceof PacketCompressor)) {
        ((PacketCompressor)this.channel.pipeline().get("decompress")).a(i);
      } else {
        this.channel.pipeline().addBefore("encoder", "compress", new PacketCompressor(i));
      }
    }
    else
    {
      if ((this.channel.pipeline().get("decompress") instanceof PacketDecompressor)) {
        this.channel.pipeline().remove("decompress");
      }
      if ((this.channel.pipeline().get("compress") instanceof PacketCompressor)) {
        this.channel.pipeline().remove("compress");
      }
    }
  }
  
  public void l()
  {
    if ((this.channel != null) && (!this.channel.isOpen())) {
      if (!this.p)
      {
        this.p = true;
        if (j() != null) {
          getPacketListener().a(j());
        } else if (getPacketListener() != null) {
          getPacketListener().a(new ChatComponentText("Disconnected"));
        }
      }
      else
      {
        g.warn("handleDisconnection() called twice");
      }
    }
  }
  
  protected void channelRead0(ChannelHandlerContext channelhandlercontext, Packet object)
    throws Exception
  {
    a(channelhandlercontext, object);
  }
  
  static class QueuedPacket
  {
    private final Packet a;
    private final GenericFutureListener<? extends Future<? super Void>>[] b;
    
    public QueuedPacket(Packet packet, GenericFutureListener<? extends Future<? super Void>>... agenericfuturelistener)
    {
      this.a = packet;
      this.b = agenericfuturelistener;
    }
  }
  
  public SocketAddress getRawAddress()
  {
    return this.channel.remoteAddress();
  }
}
