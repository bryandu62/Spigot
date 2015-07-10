package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.security.PrivateKey;
import javax.crypto.SecretKey;

public class PacketLoginInEncryptionBegin
  implements Packet<PacketLoginInListener>
{
  private byte[] a = new byte[0];
  private byte[] b = new byte[0];
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.a();
    this.b = ☃.a();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.a(this.b);
  }
  
  public void a(PacketLoginInListener ☃)
  {
    ☃.a(this);
  }
  
  public SecretKey a(PrivateKey ☃)
  {
    return MinecraftEncryption.a(☃, this.a);
  }
  
  public byte[] b(PrivateKey ☃)
  {
    if (☃ == null) {
      return this.b;
    }
    return MinecraftEncryption.b(☃, this.b);
  }
}
