package net.minecraft.server.v1_8_R3;

import com.google.gson.Gson;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.spigotmc.SpigotConfig;

public class HandshakeListener
  implements PacketHandshakingInListener
{
  private static final Gson gson = new Gson();
  private static final HashMap<InetAddress, Long> throttleTracker = new HashMap();
  private static int throttleCounter = 0;
  private final MinecraftServer a;
  private final NetworkManager b;
  
  public HandshakeListener(MinecraftServer minecraftserver, NetworkManager networkmanager)
  {
    this.a = minecraftserver;
    this.b = networkmanager;
  }
  
  public void a(PacketHandshakingInSetProtocol packethandshakinginsetprotocol)
  {
    switch (SyntheticClass_1.a[packethandshakinginsetprotocol.a().ordinal()])
    {
    case 1: 
      this.b.a(EnumProtocol.LOGIN);
      try
      {
        long currentTime = System.currentTimeMillis();
        long connectionThrottle = MinecraftServer.getServer().server.getConnectionThrottle();
        InetAddress address = ((InetSocketAddress)this.b.getSocketAddress()).getAddress();
        synchronized (throttleTracker)
        {
          if ((throttleTracker.containsKey(address)) && (!"127.0.0.1".equals(address.getHostAddress())) && (currentTime - ((Long)throttleTracker.get(address)).longValue() < connectionThrottle))
          {
            throttleTracker.put(address, Long.valueOf(currentTime));
            ChatComponentText chatcomponenttext = new ChatComponentText("Connection throttled! Please wait before reconnecting.");
            this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
            this.b.close(chatcomponenttext);
            return;
          }
          throttleTracker.put(address, Long.valueOf(currentTime));
          throttleCounter += 1;
          if (throttleCounter > 200)
          {
            throttleCounter = 0;
            
            Iterator iter = throttleTracker.entrySet().iterator();
            while (iter.hasNext())
            {
              Map.Entry<InetAddress, Long> entry = (Map.Entry)iter.next();
              if (((Long)entry.getValue()).longValue() > connectionThrottle) {
                iter.remove();
              }
            }
          }
        }
        ChatComponentText chatcomponenttext;
        ChatComponentText chatcomponenttext;
        String[] split;
        ChatComponentText chatcomponenttext;
        this.b.a(EnumProtocol.STATUS);
      }
      catch (Throwable t)
      {
        LogManager.getLogger().debug("Failed to check connection throttle", t);
        if (packethandshakinginsetprotocol.b() > 47)
        {
          chatcomponenttext = new ChatComponentText(MessageFormat.format(SpigotConfig.outdatedServerMessage, new Object[] { "1.8.7" }));
          this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
          this.b.close(chatcomponenttext);
        }
        else if (packethandshakinginsetprotocol.b() < 47)
        {
          chatcomponenttext = new ChatComponentText(MessageFormat.format(SpigotConfig.outdatedClientMessage, new Object[] { "1.8.7" }));
          this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
          this.b.close(chatcomponenttext);
        }
        else
        {
          this.b.a(new LoginListener(this.a, this.b));
          if (SpigotConfig.bungee)
          {
            split = packethandshakinginsetprotocol.b.split("\000");
            if ((split.length == 3) || (split.length == 4))
            {
              packethandshakinginsetprotocol.b = split[0];
              this.b.l = new InetSocketAddress(split[1], ((InetSocketAddress)this.b.getSocketAddress()).getPort());
              this.b.spoofedUUID = UUIDTypeAdapter.fromString(split[2]);
            }
            else
            {
              chatcomponenttext = new ChatComponentText("If you wish to use IP forwarding, please enable it in your BungeeCord config as well!");
              this.b.handle(new PacketLoginOutDisconnect(chatcomponenttext));
              this.b.close(chatcomponenttext);
              return;
            }
            if (split.length == 4) {
              this.b.spoofedProfile = ((Property[])gson.fromJson(split[3], Property[].class));
            }
          }
          ((LoginListener)this.b.getPacketListener()).hostname = (packethandshakinginsetprotocol.b + ":" + packethandshakinginsetprotocol.c);
        }
      }
    case 2: 
      this.b.a(new PacketStatusListener(this.a, this.b));
      break;
    default: 
      throw new UnsupportedOperationException("Invalid intention " + packethandshakinginsetprotocol.a());
    }
  }
  
  public void a(IChatBaseComponent ichatbasecomponent) {}
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumProtocol.values().length];
    
    static
    {
      try
      {
        a[EnumProtocol.LOGIN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumProtocol.STATUS.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
    }
  }
}
