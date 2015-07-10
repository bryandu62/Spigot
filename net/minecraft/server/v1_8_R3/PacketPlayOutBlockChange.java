package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutBlockChange
  implements Packet<PacketListenerPlayOut>
{
  private BlockPosition a;
  public IBlockData block;
  
  public PacketPlayOutBlockChange() {}
  
  public PacketPlayOutBlockChange(World ☃, BlockPosition ☃)
  {
    this.a = ☃;
    this.block = ☃.getType(☃);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.c();
    this.block = ((IBlockData)Block.d.a(☃.e()));
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    ☃.b(Block.d.b(this.block));
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
