package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutEntity
  implements Packet<PacketListenerPlayOut>
{
  protected int a;
  protected byte b;
  protected byte c;
  protected byte d;
  protected byte e;
  protected byte f;
  protected boolean g;
  protected boolean h;
  public PacketPlayOutEntity() {}
  
  public static class PacketPlayOutRelEntityMoveLook
    extends PacketPlayOutEntity
  {
    public PacketPlayOutRelEntityMoveLook()
    {
      this.h = true;
    }
    
    public PacketPlayOutRelEntityMoveLook(int ☃, byte ☃, byte ☃, byte ☃, byte ☃, byte ☃, boolean ☃)
    {
      super();
      this.b = ☃;
      this.c = ☃;
      this.d = ☃;
      this.e = ☃;
      this.f = ☃;
      this.g = ☃;
      this.h = true;
    }
    
    public void a(PacketDataSerializer ☃)
      throws IOException
    {
      super.a(☃);
      this.b = ☃.readByte();
      this.c = ☃.readByte();
      this.d = ☃.readByte();
      this.e = ☃.readByte();
      this.f = ☃.readByte();
      this.g = ☃.readBoolean();
    }
    
    public void b(PacketDataSerializer ☃)
      throws IOException
    {
      super.b(☃);
      ☃.writeByte(this.b);
      ☃.writeByte(this.c);
      ☃.writeByte(this.d);
      ☃.writeByte(this.e);
      ☃.writeByte(this.f);
      ☃.writeBoolean(this.g);
    }
  }
  
  public static class PacketPlayOutRelEntityMove
    extends PacketPlayOutEntity
  {
    public PacketPlayOutRelEntityMove() {}
    
    public PacketPlayOutRelEntityMove(int ☃, byte ☃, byte ☃, byte ☃, boolean ☃)
    {
      super();
      this.b = ☃;
      this.c = ☃;
      this.d = ☃;
      this.g = ☃;
    }
    
    public void a(PacketDataSerializer ☃)
      throws IOException
    {
      super.a(☃);
      this.b = ☃.readByte();
      this.c = ☃.readByte();
      this.d = ☃.readByte();
      this.g = ☃.readBoolean();
    }
    
    public void b(PacketDataSerializer ☃)
      throws IOException
    {
      super.b(☃);
      ☃.writeByte(this.b);
      ☃.writeByte(this.c);
      ☃.writeByte(this.d);
      ☃.writeBoolean(this.g);
    }
  }
  
  public static class PacketPlayOutEntityLook
    extends PacketPlayOutEntity
  {
    public PacketPlayOutEntityLook()
    {
      this.h = true;
    }
    
    public PacketPlayOutEntityLook(int ☃, byte ☃, byte ☃, boolean ☃)
    {
      super();
      this.e = ☃;
      this.f = ☃;
      this.h = true;
      this.g = ☃;
    }
    
    public void a(PacketDataSerializer ☃)
      throws IOException
    {
      super.a(☃);
      this.e = ☃.readByte();
      this.f = ☃.readByte();
      this.g = ☃.readBoolean();
    }
    
    public void b(PacketDataSerializer ☃)
      throws IOException
    {
      super.b(☃);
      ☃.writeByte(this.e);
      ☃.writeByte(this.f);
      ☃.writeBoolean(this.g);
    }
  }
  
  public PacketPlayOutEntity(int ☃)
  {
    this.a = ☃;
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public String toString()
  {
    return "Entity_" + super.toString();
  }
}
