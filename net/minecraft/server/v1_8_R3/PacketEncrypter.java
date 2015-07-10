package net.minecraft.server.v1_8_R3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;

public class PacketEncrypter
  extends MessageToByteEncoder<ByteBuf>
{
  private final PacketEncryptionHandler a;
  
  public PacketEncrypter(Cipher ☃)
  {
    this.a = new PacketEncryptionHandler(☃);
  }
  
  protected void a(ChannelHandlerContext ☃, ByteBuf ☃, ByteBuf ☃)
    throws Exception
  {
    this.a.a(☃, ☃);
  }
}
