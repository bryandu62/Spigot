package org.bukkit.craftbukkit.v1_8_R3;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.GameProfileBanEntry;
import net.minecraft.server.v1_8_R3.GameProfileBanList;
import net.minecraft.server.v1_8_R3.JsonListEntry;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.UserCache;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;

public class CraftProfileBanList
  implements BanList
{
  private final GameProfileBanList list;
  
  public CraftProfileBanList(GameProfileBanList list)
  {
    this.list = list;
  }
  
  public BanEntry getBanEntry(String target)
  {
    Validate.notNull(target, "Target cannot be null");
    
    GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
    if (profile == null) {
      return null;
    }
    GameProfileBanEntry entry = (GameProfileBanEntry)this.list.get(profile);
    if (entry == null) {
      return null;
    }
    return new CraftProfileBanEntry(profile, entry, this.list);
  }
  
  public BanEntry addBan(String target, String reason, Date expires, String source)
  {
    Validate.notNull(target, "Ban target cannot be null");
    
    GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
    if (profile == null) {
      return null;
    }
    GameProfileBanEntry entry = new GameProfileBanEntry(profile, new Date(), 
      StringUtils.isBlank(source) ? null : source, expires, 
      StringUtils.isBlank(reason) ? null : reason);
    
    this.list.add(entry);
    try
    {
      this.list.save();
    }
    catch (IOException ex)
    {
      Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ex.getMessage());
    }
    return new CraftProfileBanEntry(profile, entry, this.list);
  }
  
  public Set<BanEntry> getBanEntries()
  {
    ImmutableSet.Builder<BanEntry> builder = ImmutableSet.builder();
    for (JsonListEntry entry : this.list.getValues())
    {
      GameProfile profile = (GameProfile)entry.getKey();
      builder.add(new CraftProfileBanEntry(profile, (GameProfileBanEntry)entry, this.list));
    }
    return builder.build();
  }
  
  public boolean isBanned(String target)
  {
    Validate.notNull(target, "Target cannot be null");
    
    GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
    if (profile == null) {
      return false;
    }
    return this.list.isBanned(profile);
  }
  
  public void pardon(String target)
  {
    Validate.notNull(target, "Target cannot be null");
    
    GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
    this.list.remove(profile);
  }
}
