package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PacketPlayOutUpdateAttributes
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private final List<AttributeSnapshot> b = Lists.newArrayList();
  
  public PacketPlayOutUpdateAttributes() {}
  
  public PacketPlayOutUpdateAttributes(int ☃, Collection<AttributeInstance> ☃)
  {
    this.a = ☃;
    for (AttributeInstance ☃ : ☃) {
      this.b.add(new AttributeSnapshot(☃.getAttribute().getName(), ☃.b(), ☃.c()));
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    
    int ☃ = ☃.readInt();
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      String ☃ = ☃.c(64);
      double ☃ = ☃.readDouble();
      List<AttributeModifier> ☃ = Lists.newArrayList();
      int ☃ = ☃.e();
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        UUID ☃ = ☃.g();
        ☃.add(new AttributeModifier(☃, "Unknown synced attribute modifier", ☃.readDouble(), ☃.readByte()));
      }
      this.b.add(new AttributeSnapshot(☃, ☃, ☃));
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.writeInt(this.b.size());
    for (AttributeSnapshot ☃ : this.b)
    {
      ☃.a(☃.a());
      ☃.writeDouble(☃.b());
      ☃.b(☃.c().size());
      for (AttributeModifier ☃ : ☃.c())
      {
        ☃.a(☃.a());
        ☃.writeDouble(☃.d());
        ☃.writeByte(☃.c());
      }
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public class AttributeSnapshot
  {
    private final String b;
    private final double c;
    private final Collection<AttributeModifier> d;
    
    public AttributeSnapshot(double ☃, Collection<AttributeModifier> arg4)
    {
      this.b = ☃;
      this.c = ☃;
      this.d = ☃;
    }
    
    public String a()
    {
      return this.b;
    }
    
    public double b()
    {
      return this.c;
    }
    
    public Collection<AttributeModifier> c()
    {
      return this.d;
    }
  }
}
