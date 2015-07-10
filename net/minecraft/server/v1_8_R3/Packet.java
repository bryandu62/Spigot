package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public abstract interface Packet<T extends PacketListener>
{
  public abstract void a(PacketDataSerializer paramPacketDataSerializer)
    throws IOException;
  
  public abstract void b(PacketDataSerializer paramPacketDataSerializer)
    throws IOException;
  
  public abstract void a(T paramT);
}
