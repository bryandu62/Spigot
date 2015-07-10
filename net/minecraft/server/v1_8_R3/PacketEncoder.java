package net.minecraft.server.v1_8_R3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.Attribute;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class PacketEncoder
  extends MessageToByteEncoder<Packet>
{
  private static final Logger a = ;
  private static final Marker b = MarkerManager.getMarker("PACKET_SENT", NetworkManager.b);
  private final EnumProtocolDirection c;
  
  public PacketEncoder(EnumProtocolDirection ☃)
  {
    this.c = ☃;
  }
  
  protected void a(ChannelHandlerContext ☃, Packet ☃, ByteBuf ☃)
    throws Exception
  {
    Integer ☃ = ((EnumProtocol)☃.channel().attr(NetworkManager.c).get()).a(this.c, ☃);
    if (a.isDebugEnabled()) {
      a.debug(b, "OUT: [{}:{}] {}", new Object[] { ☃.channel().attr(NetworkManager.c).get(), ☃, ☃.getClass().getName() });
    }
    if (☃ == null) {
      throw new IOException("Can't serialize unregistered packet");
    }
    PacketDataSerializer ☃ = new PacketDataSerializer(☃);
    ☃.b(☃.intValue());
    try
    {
      if ((☃ instanceof PacketPlayOutNamedEntitySpawn)) {
        ☃ = ☃;
      }
      ☃.b(☃);
    }
    catch (Throwable ☃)
    {
      a.error(☃);
    }
  }
}
