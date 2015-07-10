package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.List;

public class PacketPlayOutEntityMetadata
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private List<DataWatcher.WatchableObject> b;
  
  public PacketPlayOutEntityMetadata() {}
  
  public PacketPlayOutEntityMetadata(int ☃, DataWatcher ☃, boolean ☃)
  {
    this.a = ☃;
    if (☃) {
      this.b = ☃.c();
    } else {
      this.b = ☃.b();
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = DataWatcher.b(☃);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    DataWatcher.a(this.b, ☃);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
