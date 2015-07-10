package net.minecraft.server.v1_8_R3;

public abstract interface PacketLoginOutListener
  extends PacketListener
{
  public abstract void a(PacketLoginOutEncryptionBegin paramPacketLoginOutEncryptionBegin);
  
  public abstract void a(PacketLoginOutSuccess paramPacketLoginOutSuccess);
  
  public abstract void a(PacketLoginOutDisconnect paramPacketLoginOutDisconnect);
  
  public abstract void a(PacketLoginOutSetCompression paramPacketLoginOutSetCompression);
}
