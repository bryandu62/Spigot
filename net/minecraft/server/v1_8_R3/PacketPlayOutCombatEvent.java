package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public class PacketPlayOutCombatEvent
  implements Packet<PacketListenerPlayOut>
{
  public EnumCombatEventType a;
  public int b;
  public int c;
  public int d;
  public String e;
  
  public PacketPlayOutCombatEvent() {}
  
  public PacketPlayOutCombatEvent(CombatTracker ☃, EnumCombatEventType ☃)
  {
    this.a = ☃;
    
    EntityLiving ☃ = ☃.c();
    switch (1.a[☃.ordinal()])
    {
    case 1: 
      this.d = ☃.f();
      this.c = (☃ == null ? -1 : ☃.getId());
      break;
    case 2: 
      this.b = ☃.h().getId();
      this.c = (☃ == null ? -1 : ☃.getId());
      this.e = ☃.b().c();
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ((EnumCombatEventType)☃.a(EnumCombatEventType.class));
    if (this.a == EnumCombatEventType.END_COMBAT)
    {
      this.d = ☃.e();
      this.c = ☃.readInt();
    }
    else if (this.a == EnumCombatEventType.ENTITY_DIED)
    {
      this.b = ☃.e();
      this.c = ☃.readInt();
      this.e = ☃.c(32767);
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    if (this.a == EnumCombatEventType.END_COMBAT)
    {
      ☃.b(this.d);
      ☃.writeInt(this.c);
    }
    else if (this.a == EnumCombatEventType.ENTITY_DIED)
    {
      ☃.b(this.b);
      ☃.writeInt(this.c);
      ☃.a(this.e);
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public static enum EnumCombatEventType
  {
    private EnumCombatEventType() {}
  }
}
