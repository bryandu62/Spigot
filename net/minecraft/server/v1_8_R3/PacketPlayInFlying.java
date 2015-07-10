package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayInFlying
  implements Packet<PacketListenerPlayIn>
{
  protected double x;
  protected double y;
  protected double z;
  protected float yaw;
  protected float pitch;
  protected boolean f;
  protected boolean hasPos;
  protected boolean hasLook;
  
  public static class PacketPlayInPositionLook
    extends PacketPlayInFlying
  {
    public PacketPlayInPositionLook()
    {
      this.hasPos = true;
      this.hasLook = true;
    }
    
    public void a(PacketDataSerializer ☃)
      throws IOException
    {
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
      this.yaw = ☃.readFloat();
      this.pitch = ☃.readFloat();
      super.a(☃);
    }
    
    public void b(PacketDataSerializer ☃)
      throws IOException
    {
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
      ☃.writeFloat(this.yaw);
      ☃.writeFloat(this.pitch);
      super.b(☃);
    }
  }
  
  public static class PacketPlayInPosition
    extends PacketPlayInFlying
  {
    public PacketPlayInPosition()
    {
      this.hasPos = true;
    }
    
    public void a(PacketDataSerializer ☃)
      throws IOException
    {
      this.x = ☃.readDouble();
      this.y = ☃.readDouble();
      this.z = ☃.readDouble();
      super.a(☃);
    }
    
    public void b(PacketDataSerializer ☃)
      throws IOException
    {
      ☃.writeDouble(this.x);
      ☃.writeDouble(this.y);
      ☃.writeDouble(this.z);
      super.b(☃);
    }
  }
  
  public static class PacketPlayInLook
    extends PacketPlayInFlying
  {
    public PacketPlayInLook()
    {
      this.hasLook = true;
    }
    
    public void a(PacketDataSerializer ☃)
      throws IOException
    {
      this.yaw = ☃.readFloat();
      this.pitch = ☃.readFloat();
      super.a(☃);
    }
    
    public void b(PacketDataSerializer ☃)
      throws IOException
    {
      ☃.writeFloat(this.yaw);
      ☃.writeFloat(this.pitch);
      super.b(☃);
    }
  }
  
  public void a(PacketListenerPlayIn ☃)
  {
    ☃.a(this);
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.f = (☃.readUnsignedByte() != 0);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.writeByte(this.f ? 1 : 0);
  }
  
  public double a()
  {
    return this.x;
  }
  
  public double b()
  {
    return this.y;
  }
  
  public double c()
  {
    return this.z;
  }
  
  public float d()
  {
    return this.yaw;
  }
  
  public float e()
  {
    return this.pitch;
  }
  
  public boolean f()
  {
    return this.f;
  }
  
  public boolean g()
  {
    return this.hasPos;
  }
  
  public boolean h()
  {
    return this.hasLook;
  }
  
  public void a(boolean ☃)
  {
    this.hasPos = ☃;
  }
}
