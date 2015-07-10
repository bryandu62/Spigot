package net.minecraft.server.v1_8_R3;

public abstract interface PacketStatusInListener
  extends PacketListener
{
  public abstract void a(PacketStatusInPing paramPacketStatusInPing);
  
  public abstract void a(PacketStatusInStart paramPacketStatusInStart);
}
