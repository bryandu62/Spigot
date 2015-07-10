package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

public class PacketPlayInTabComplete
  implements Packet<PacketListenerPlayIn>
{
  private String a;
  private BlockPosition b;
  
  public PacketPlayInTabComplete() {}
  
  public PacketPlayInTabComplete(String ☃)
  {
    this(☃, null);
  }
  
  public PacketPlayInTabComplete(String ☃, BlockPosition ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c(32767);
    boolean ☃ = ☃.readBoolean();
    if (☃) {
      this.b = ☃.c();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(StringUtils.substring(this.a, 0, 32767));
    boolean ☃ = this.b != null;
    ☃.writeBoolean(☃);
    if (☃) {
      ☃.a(this.b);
    }
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public String a()
  {
    return this.a;
  }
  
  public BlockPosition b()
  {
    return this.b;
  }
}
