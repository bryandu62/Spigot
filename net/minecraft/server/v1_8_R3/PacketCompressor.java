package net.minecraft.server.v1_8_R3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class PacketCompressor
  extends MessageToByteEncoder<ByteBuf>
{
  private final byte[] a = new byte[' '];
  private final Deflater b;
  private int c;
  
  public PacketCompressor(int ☃)
  {
    this.c = ☃;
    this.b = new Deflater();
  }
  
  protected void a(ChannelHandlerContext ☃, ByteBuf ☃, ByteBuf ☃)
    throws Exception
  {
    int ☃ = ☃.readableBytes();
    PacketDataSerializer ☃ = new PacketDataSerializer(☃);
    if (☃ < this.c)
    {
      ☃.b(0);
      ☃.writeBytes(☃);
    }
    else
    {
      byte[] ☃ = new byte[☃];
      ☃.readBytes(☃);
      
      ☃.b(☃.length);
      
      this.b.setInput(☃, 0, ☃);
      this.b.finish();
      while (!this.b.finished())
      {
        int ☃ = this.b.deflate(this.a);
        ☃.writeBytes(this.a, 0, ☃);
      }
      this.b.reset();
    }
  }
  
  public void a(int ☃)
  {
    this.c = ☃;
  }
}
