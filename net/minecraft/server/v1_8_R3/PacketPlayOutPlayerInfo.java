package net.minecraft.server.v1_8_R3;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.io.IOException;
import java.util.List;

public class PacketPlayOutPlayerInfo
  implements Packet<PacketListenerPlayOut>
{
  private EnumPlayerInfoAction a;
  private final List<PlayerInfoData> b = Lists.newArrayList();
  
  public PacketPlayOutPlayerInfo() {}
  
  public PacketPlayOutPlayerInfo(EnumPlayerInfoAction ☃, EntityPlayer... ☃)
  {
    this.a = ☃;
    for (EntityPlayer ☃ : ☃) {
      this.b.add(new PlayerInfoData(☃.getProfile(), ☃.ping, ☃.playerInteractManager.getGameMode(), ☃.getPlayerListName()));
    }
  }
  
  public PacketPlayOutPlayerInfo(EnumPlayerInfoAction ☃, Iterable<EntityPlayer> ☃)
  {
    this.a = ☃;
    for (EntityPlayer ☃ : ☃) {
      this.b.add(new PlayerInfoData(☃.getProfile(), ☃.ping, ☃.playerInteractManager.getGameMode(), ☃.getPlayerListName()));
    }
  }
  
  public void a(PacketDataSerializer ☃)
    throws IOException
  {
    this.a = ((EnumPlayerInfoAction)☃.a(EnumPlayerInfoAction.class));
    
    int ☃ = ☃.e();
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      GameProfile ☃ = null;
      int ☃ = 0;
      WorldSettings.EnumGamemode ☃ = null;
      IChatBaseComponent ☃ = null;
      switch (1.a[this.a.ordinal()])
      {
      case 1: 
        ☃ = new GameProfile(☃.g(), ☃.c(16));
        int ☃ = ☃.e();
        for (int ☃ = 0; ☃ < ☃; ☃++)
        {
          String ☃ = ☃.c(32767);
          String ☃ = ☃.c(32767);
          if (☃.readBoolean()) {
            ☃.getProperties().put(☃, new Property(☃, ☃, ☃.c(32767)));
          } else {
            ☃.getProperties().put(☃, new Property(☃, ☃));
          }
        }
        ☃ = WorldSettings.EnumGamemode.getById(☃.e());
        ☃ = ☃.e();
        if (☃.readBoolean()) {
          ☃ = ☃.d();
        }
        break;
      case 2: 
        ☃ = new GameProfile(☃.g(), null);
        ☃ = WorldSettings.EnumGamemode.getById(☃.e());
        break;
      case 3: 
        ☃ = new GameProfile(☃.g(), null);
        ☃ = ☃.e();
        break;
      case 4: 
        ☃ = new GameProfile(☃.g(), null);
        if (☃.readBoolean()) {
          ☃ = ☃.d();
        }
        break;
      case 5: 
        ☃ = new GameProfile(☃.g(), null);
      }
      this.b.add(new PlayerInfoData(☃, ☃, ☃, ☃));
    }
  }
  
  public void b(PacketDataSerializer ☃)
    throws IOException
  {
    ☃.a(this.a);
    
    ☃.b(this.b.size());
    for (PlayerInfoData ☃ : this.b) {
      switch (1.a[this.a.ordinal()])
      {
      case 1: 
        ☃.a(☃.a().getId());
        ☃.a(☃.a().getName());
        ☃.b(☃.a().getProperties().size());
        for (Property ☃ : ☃.a().getProperties().values())
        {
          ☃.a(☃.getName());
          ☃.a(☃.getValue());
          if (☃.hasSignature())
          {
            ☃.writeBoolean(true);
            ☃.a(☃.getSignature());
          }
          else
          {
            ☃.writeBoolean(false);
          }
        }
        ☃.b(☃.c().getId());
        ☃.b(☃.b());
        if (☃.d() == null)
        {
          ☃.writeBoolean(false);
        }
        else
        {
          ☃.writeBoolean(true);
          ☃.a(☃.d());
        }
        break;
      case 2: 
        ☃.a(☃.a().getId());
        ☃.b(☃.c().getId());
        break;
      case 3: 
        ☃.a(☃.a().getId());
        ☃.b(☃.b());
        break;
      case 4: 
        ☃.a(☃.a().getId());
        if (☃.d() == null)
        {
          ☃.writeBoolean(false);
        }
        else
        {
          ☃.writeBoolean(true);
          ☃.a(☃.d());
        }
        break;
      case 5: 
        ☃.a(☃.a().getId());
      }
    }
  }
  
  public void a(PacketListenerPlayOut ☃)
  {
    ☃.a(this);
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("action", this.a).add("entries", this.b).toString();
  }
  
  public class PlayerInfoData
  {
    private final int b;
    private final WorldSettings.EnumGamemode c;
    private final GameProfile d;
    private final IChatBaseComponent e;
    
    public PlayerInfoData(GameProfile ☃, int ☃, WorldSettings.EnumGamemode ☃, IChatBaseComponent ☃)
    {
      this.d = ☃;
      this.b = ☃;
      this.c = ☃;
      this.e = ☃;
    }
    
    public GameProfile a()
    {
      return this.d;
    }
    
    public int b()
    {
      return this.b;
    }
    
    public WorldSettings.EnumGamemode c()
    {
      return this.c;
    }
    
    public IChatBaseComponent d()
    {
      return this.e;
    }
    
    public String toString()
    {
      return Objects.toStringHelper(this).add("latency", this.b).add("gameMode", this.c).add("profile", this.d).add("displayName", this.e == null ? null : IChatBaseComponent.ChatSerializer.a(this.e)).toString();
    }
  }
  
  public static enum EnumPlayerInfoAction
  {
    private EnumPlayerInfoAction() {}
  }
}
