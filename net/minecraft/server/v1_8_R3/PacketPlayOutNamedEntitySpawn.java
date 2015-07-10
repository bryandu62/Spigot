package net.minecraft.server.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PacketPlayOutNamedEntitySpawn
  implements Packet<PacketListenerPlayOut>
{
  private int a;
  private UUID b;
  private int c;
  private int d;
  private int e;
  private byte f;
  private byte g;
  private int h;
  private DataWatcher i;
  private List<DataWatcher.WatchableObject> j;
  
  public PacketPlayOutNamedEntitySpawn() {}
  
  public PacketPlayOutNamedEntitySpawn(EntityHuman ☃)
  {
    this.a = ☃.getId();
    this.b = ☃.getProfile().getId();
    this.c = MathHelper.floor(☃.locX * 32.0D);
    this.d = MathHelper.floor(☃.locY * 32.0D);
    this.e = MathHelper.floor(☃.locZ * 32.0D);
    this.f = ((byte)(int)(☃.yaw * 256.0F / 360.0F));
    this.g = ((byte)(int)(☃.pitch * 256.0F / 360.0F));
    
    ItemStack ☃ = ☃.inventory.getItemInHand();
    this.h = (☃ == null ? 0 : Item.getId(☃.getItem()));
    
    this.i = ☃.getDataWatcher();
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ☃.e();
    this.b = ☃.g();
    this.c = ☃.readInt();
    this.d = ☃.readInt();
    this.e = ☃.readInt();
    this.f = ☃.readByte();
    this.g = ☃.readByte();
    this.h = ☃.readShort();
    this.j = DataWatcher.b(☃);
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.b(this.a);
    ☃.a(this.b);
    ☃.writeInt(this.c);
    ☃.writeInt(this.d);
    ☃.writeInt(this.e);
    ☃.writeByte(this.f);
    ☃.writeByte(this.g);
    ☃.writeShort(this.h);
    this.i.a(☃);
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
}
