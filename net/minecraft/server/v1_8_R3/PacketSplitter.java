package net.minecraft.server.v1_8_R3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import java.util.List;

public class PacketSplitter
  extends ByteToMessageDecoder
{
  protected void decode(ChannelHandlerContext ☃, ByteBuf ☃, List<Object> ☃)
    throws Exception
  {
    ☃.markReaderIndex();
    
    byte[] ☃ = new byte[3];
    for (int ☃ = 0; ☃ < ☃.length; ☃++)
    {
      if (!☃.isReadable())
      {
        ☃.resetReaderIndex();
        return;
      }
      ☃[☃] = ☃.readByte();
      if (☃[☃] >= 0)
      {
        PacketDataSerializer ☃ = new PacketDataSerializer(Unpooled.wrappedBuffer(☃));
        try
        {
          int ☃ = ☃.e();
          if (☃.readableBytes() < ☃)
          {
            ☃.resetReaderIndex(); return;
          }
          ☃.add(☃.readBytes(☃)); return;
        }
        finally
        {
          ☃.release();
        }
      }
    }
    throw new CorruptedFrameException("length wider than 21-bit");
  }
}
