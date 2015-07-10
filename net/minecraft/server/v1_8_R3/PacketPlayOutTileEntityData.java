package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutTileEntityData
  implements Packet<PacketListenerPlayOut>
{
  private BlockPosition a;
  private int b;
  private NBTTagCompound c;
  
  public PacketPlayOutTileEntityData() {}
  
  public PacketPlayOutTileEntityData(BlockPosition ☃, int ☃, NBTTagCompound ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c();
    this.b = ☃.readUnsignedByte();
    this.c = ☃.h();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.writeByte((byte)this.b);
    ☃.a(this.c);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
