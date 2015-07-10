package net.minecraft.server.v1_8_R3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketPrepender
  extends MessageToByteEncoder<ByteBuf>
{
  protected void a(ChannelHandlerContext ☃, ByteBuf ☃, ByteBuf ☃)
    throws Exception
  {
    int ☃ = ☃.readableBytes();
    int ☃ = PacketDataSerializer.a(☃);
    if (☃ > 3) {
      throw new IllegalArgumentException("unable to fit " + ☃ + " into " + 3);
    }
    PacketDataSerializer ☃ = new PacketDataSerializer(☃);
    
    ☃.ensureWritable(☃ + ☃);
    
    ☃.b(☃);
    ☃.writeBytes(☃, ☃.readerIndex(), ☃);
  }
}
