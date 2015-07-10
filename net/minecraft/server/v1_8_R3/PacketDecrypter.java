package net.minecraft.server.v1_8_R3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import javax.crypto.Cipher;

public class PacketDecrypter
  extends MessageToMessageDecoder<ByteBuf>
{
  private final PacketEncryptionHandler a;
  
  public PacketDecrypter(Cipher ☃)
  {
    this.a = new PacketEncryptionHandler(☃);
  }
  
  protected void a(ChannelHandlerContext ☃, ByteBuf ☃, List<Object> ☃)
    throws Exception
  {
    ☃.add(this.a.a(☃, ☃));
  }
}
