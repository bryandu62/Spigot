package net.minecraft.server.v1_8_R3;

public abstract interface PacketLoginInListener
  extends PacketListener
{
  public abstract void a(PacketLoginInStart paramPacketLoginInStart);
  
  public abstract void a(PacketLoginInEncryptionBegin paramPacketLoginInEncryptionBegin);
}
