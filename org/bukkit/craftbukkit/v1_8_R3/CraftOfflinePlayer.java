package org.bukkit.craftbukkit.v1_8_R3;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.DedicatedPlayerList;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WhiteList;
import net.minecraft.server.v1_8_R3.WorldNBTStorage;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_8_R3.metadata.PlayerMetadataStore;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

@SerializableAs("Player")
public class CraftOfflinePlayer
  implements OfflinePlayer, ConfigurationSerializable
{
  private final GameProfile profile;
  private final CraftServer server;
  private final WorldNBTStorage storage;
  
  protected CraftOfflinePlayer(CraftServer server, GameProfile profile)
  {
    this.server = server;
    this.profile = profile;
    this.storage = ((WorldNBTStorage)((WorldServer)server.console.worlds.get(0)).getDataManager());
  }
  
  public GameProfile getProfile()
  {
    return this.profile;
  }
  
  public boolean isOnline()
  {
    return getPlayer() != null;
  }
  
  public String getName()
  {
    Player player = getPlayer();
    if (player != null) {
      return player.getName();
    }
    if (this.profile.getName() != null) {
      return this.profile.getName();
    }
    NBTTagCompound data = getBukkitData();
    if ((data != null) && 
      (data.hasKey("lastKnownName"))) {
      return data.getString("lastKnownName");
    }
    return null;
  }
  
  public UUID getUniqueId()
  {
    return this.profile.getId();
  }
  
  public Server getServer()
  {
    return this.server;
  }
  
  public boolean isOp()
  {
    return this.server.getHandle().isOp(this.profile);
  }
  
  public void setOp(boolean value)
  {
    if (value == isOp()) {
      return;
    }
    if (value) {
      this.server.getHandle().addOp(this.profile);
    } else {
      this.server.getHandle().removeOp(this.profile);
    }
  }
  
  public boolean isBanned()
  {
    if (getName() == null) {
      return false;
    }
    return this.server.getBanList(BanList.Type.NAME).isBanned(getName());
  }
  
  public void setBanned(boolean value)
  {
    if (getName() == null) {
      return;
    }
    if (value) {
      this.server.getBanList(BanList.Type.NAME).addBan(getName(), null, null, null);
    } else {
      this.server.getBanList(BanList.Type.NAME).pardon(getName());
    }
  }
  
  public boolean isWhitelisted()
  {
    return this.server.getHandle().getWhitelist().isWhitelisted(this.profile);
  }
  
  public void setWhitelisted(boolean value)
  {
    if (value) {
      this.server.getHandle().addWhitelist(this.profile);
    } else {
      this.server.getHandle().removeWhitelist(this.profile);
    }
  }
  
  public Map<String, Object> serialize()
  {
    Map<String, Object> result = new LinkedHashMap();
    
    result.put("UUID", this.profile.getId().toString());
    
    return result;
  }
  
  public static OfflinePlayer deserialize(Map<String, Object> args)
  {
    if (args.get("name") != null) {
      return Bukkit.getServer().getOfflinePlayer((String)args.get("name"));
    }
    return Bukkit.getServer().getOfflinePlayer(UUID.fromString((String)args.get("UUID")));
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + "[UUID=" + this.profile.getId() + "]";
  }
  
  public Player getPlayer()
  {
    for (Object obj : this.server.getHandle().players)
    {
      EntityPlayer player = (EntityPlayer)obj;
      if (player.getUniqueID().equals(getUniqueId())) {
        return player.playerConnection != null ? player.playerConnection.getPlayer() : null;
      }
    }
    return null;
  }
  
  public boolean equals(Object obj)
  {
    if ((obj == null) || (!(obj instanceof OfflinePlayer))) {
      return false;
    }
    OfflinePlayer other = (OfflinePlayer)obj;
    if ((getUniqueId() == null) || (other.getUniqueId() == null)) {
      return false;
    }
    return getUniqueId().equals(other.getUniqueId());
  }
  
  public int hashCode()
  {
    int hash = 5;
    hash = 97 * hash + (getUniqueId() != null ? getUniqueId().hashCode() : 0);
    return hash;
  }
  
  private NBTTagCompound getData()
  {
    return this.storage.getPlayerData(getUniqueId().toString());
  }
  
  private NBTTagCompound getBukkitData()
  {
    NBTTagCompound result = getData();
    if (result != null)
    {
      if (!result.hasKey("bukkit")) {
        result.set("bukkit", new NBTTagCompound());
      }
      result = result.getCompound("bukkit");
    }
    return result;
  }
  
  private File getDataFile()
  {
    return new File(this.storage.getPlayerDir(), getUniqueId() + ".dat");
  }
  
  public long getFirstPlayed()
  {
    Player player = getPlayer();
    if (player != null) {
      return player.getFirstPlayed();
    }
    NBTTagCompound data = getBukkitData();
    if (data != null)
    {
      if (data.hasKey("firstPlayed")) {
        return data.getLong("firstPlayed");
      }
      File file = getDataFile();
      return file.lastModified();
    }
    return 0L;
  }
  
  public long getLastPlayed()
  {
    Player player = getPlayer();
    if (player != null) {
      return player.getLastPlayed();
    }
    NBTTagCompound data = getBukkitData();
    if (data != null)
    {
      if (data.hasKey("lastPlayed")) {
        return data.getLong("lastPlayed");
      }
      File file = getDataFile();
      return file.lastModified();
    }
    return 0L;
  }
  
  public boolean hasPlayedBefore()
  {
    return getData() != null;
  }
  
  public Location getBedSpawnLocation()
  {
    NBTTagCompound data = getData();
    if (data == null) {
      return null;
    }
    if ((data.hasKey("SpawnX")) && (data.hasKey("SpawnY")) && (data.hasKey("SpawnZ")))
    {
      String spawnWorld = data.getString("SpawnWorld");
      if (spawnWorld.equals("")) {
        spawnWorld = ((World)this.server.getWorlds().get(0)).getName();
      }
      return new Location(this.server.getWorld(spawnWorld), data.getInt("SpawnX"), data.getInt("SpawnY"), data.getInt("SpawnZ"));
    }
    return null;
  }
  
  public void setMetadata(String metadataKey, MetadataValue metadataValue)
  {
    this.server.getPlayerMetadata().setMetadata(this, metadataKey, metadataValue);
  }
  
  public List<MetadataValue> getMetadata(String metadataKey)
  {
    return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
  }
  
  public boolean hasMetadata(String metadataKey)
  {
    return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
  }
  
  public void removeMetadata(String metadataKey, Plugin plugin)
  {
    this.server.getPlayerMetadata().removeMetadata(this, metadataKey, plugin);
  }
}
