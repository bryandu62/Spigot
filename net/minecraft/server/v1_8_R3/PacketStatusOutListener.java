package net.minecraft.server.v1_8_R3;

public abstract interface PacketStatusOutListener
  extends PacketListener
{
  public abstract void a(PacketStatusOutServerInfo paramPacketStatusOutServerInfo);
  
  public abstract void a(PacketStatusOutPong paramPacketStatusOutPong);
}
