package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutWorldBorder
  implements Packet<PacketListenerPlayOut>
{
  private EnumWorldBorderAction a;
  private int b;
  private double c;
  private double d;
  private double e;
  private double f;
  private long g;
  private int h;
  private int i;
  
  public PacketPlayOutWorldBorder() {}
  
  public PacketPlayOutWorldBorder(WorldBorder ☃, EnumWorldBorderAction ☃)
  {
    this.a = ☃;
    this.c = ☃.getCenterX();
    this.d = ☃.getCenterZ();
    this.f = ☃.getSize();
    this.e = ☃.j();
    this.g = ☃.i();
    this.b = ☃.l();
    this.i = ☃.getWarningDistance();
    this.h = ☃.getWarningTime();
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ((EnumWorldBorderAction)☃.a(EnumWorldBorderAction.class));
    switch (1.a[this.a.ordinal()])
    {
    case 1: 
      this.e = ☃.readDouble();
      break;
    case 2: 
      this.f = ☃.readDouble();
      this.e = ☃.readDouble();
      this.g = ☃.f();
      break;
    case 3: 
      this.c = ☃.readDouble();
      this.d = ☃.readDouble();
      break;
    case 4: 
      this.i = ☃.e();
      break;
    case 5: 
      this.h = ☃.e();
      break;
    case 6: 
      this.c = ☃.readDouble();
      this.d = ☃.readDouble();
      this.f = ☃.readDouble();
      this.e = ☃.readDouble();
      this.g = ☃.f();
      this.b = ☃.e();
      this.i = ☃.e();
      this.h = ☃.e();
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    switch (1.a[this.a.ordinal()])
    {
    case 1: 
      ☃.writeDouble(this.e);
      break;
    case 2: 
      ☃.writeDouble(this.f);
      ☃.writeDouble(this.e);
      ☃.b(this.g);
      break;
    case 3: 
      ☃.writeDouble(this.c);
      ☃.writeDouble(this.d);
      break;
    case 5: 
      ☃.b(this.h);
      break;
    case 4: 
      ☃.b(this.i);
      break;
    case 6: 
      ☃.writeDouble(this.c);
      ☃.writeDouble(this.d);
      ☃.writeDouble(this.f);
      ☃.writeDouble(this.e);
      ☃.b(this.g);
      ☃.b(this.b);
      ☃.b(this.i);
      ☃.b(this.h);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public static enum EnumWorldBorderAction
  {
    private EnumWorldBorderAction() {}
  }
}
