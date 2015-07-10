package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketHandshakingInSetProtocol
  implements Packet<PacketHandshakingInListener>
{
  private int a;
  public String b;
  public int c;
  private EnumProtocol d;
  
  public void a(PacketDataSerializer packetdataserializer)
    throws IOException
  {
    this.a = packetdataserializer.e();
    this.b = packetdataserializer.c(32767);
    this.c = packetdataserializer.readUnsignedShort();
    this.d = EnumProtocol.a(packetdataserializer.e());
  }
  
  public void b(PacketDataSerializer packetdataserializer)
    throws IOException
  {
    packetdataserializer.b(this.a);
    packetdataserializer.a(this.b);
    packetdataserializer.writeShort(this.c);
    packetdataserializer.b(this.d.a());
  }
  
  public void a(PacketHandshakingInListener packethandshakinginlistener)
  {
    packethandshakinginlistener.a(this);
  }
  
  public EnumProtocol a()
  {
    return this.d;
  }
  
  public int b()
  {
    return this.a;
  }
}
