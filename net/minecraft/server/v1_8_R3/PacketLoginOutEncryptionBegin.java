package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.security.PublicKey;

public class PacketLoginOutEncryptionBegin
  implements Packet<PacketLoginOutListener>
{
  private String a;
  private PublicKey b;
  private byte[] c;
  
  public PacketLoginOutEncryptionBegin() {}
  
  public PacketLoginOutEncryptionBegin(String ☃, PublicKey ☃, byte[] ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(20);
    this.b = MinecraftEncryption.a(☃.a());
    this.c = ☃.a();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.a(this.b.getEncoded());
    ☃.a(this.c);
  }
  
  public void a(PacketLoginOutListener ☃)
  {
    ☃.a(this);
  }
}
