package org.bukkit.entity;

import java.net.InetSocketAddress;
import java.util.Set;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.map.MapView;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scoreboard.Scoreboard;

public abstract interface Player
  extends HumanEntity, Conversable, CommandSender, OfflinePlayer, PluginMessageRecipient
{
  public abstract String getDisplayName();
  
  public abstract void setDisplayName(String paramString);
  
  public abstract String getPlayerListName();
  
  public abstract void setPlayerListName(String paramString);
  
  public abstract void setCompassTarget(Location paramLocation);
  
  public abstract Location getCompassTarget();
  
  public abstract InetSocketAddress getAddress();
  
  public abstract void sendRawMessage(String paramString);
  
  public abstract void kickPlayer(String paramString);
  
  public abstract void chat(String paramString);
  
  public abstract boolean performCommand(String paramString);
  
  public abstract boolean isSneaking();
  
  public abstract void setSneaking(boolean paramBoolean);
  
  public abstract boolean isSprinting();
  
  public abstract void setSprinting(boolean paramBoolean);
  
  public abstract void saveData();
  
  public abstract void loadData();
  
  public abstract void setSleepingIgnored(boolean paramBoolean);
  
  public abstract boolean isSleepingIgnored();
  
  @Deprecated
  public abstract void playNote(Location paramLocation, byte paramByte1, byte paramByte2);
  
  public abstract void playNote(Location paramLocation, Instrument paramInstrument, Note paramNote);
  
  public abstract void playSound(Location paramLocation, Sound paramSound, float paramFloat1, float paramFloat2);
  
  public abstract void playSound(Location paramLocation, String paramString, float paramFloat1, float paramFloat2);
  
  @Deprecated
  public abstract void playEffect(Location paramLocation, Effect paramEffect, int paramInt);
  
  public abstract <T> void playEffect(Location paramLocation, Effect paramEffect, T paramT);
  
  @Deprecated
  public abstract void sendBlockChange(Location paramLocation, Material paramMaterial, byte paramByte);
  
  @Deprecated
  public abstract boolean sendChunkChange(Location paramLocation, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);
  
  @Deprecated
  public abstract void sendBlockChange(Location paramLocation, int paramInt, byte paramByte);
  
  public abstract void sendSignChange(Location paramLocation, String[] paramArrayOfString)
    throws IllegalArgumentException;
  
  public abstract void sendMap(MapView paramMapView);
  
  public abstract void updateInventory();
  
  public abstract void awardAchievement(Achievement paramAchievement);
  
  public abstract void removeAchievement(Achievement paramAchievement);
  
  public abstract boolean hasAchievement(Achievement paramAchievement);
  
  public abstract void incrementStatistic(Statistic paramStatistic)
    throws IllegalArgumentException;
  
  public abstract void decrementStatistic(Statistic paramStatistic)
    throws IllegalArgumentException;
  
  public abstract void incrementStatistic(Statistic paramStatistic, int paramInt)
    throws IllegalArgumentException;
  
  public abstract void decrementStatistic(Statistic paramStatistic, int paramInt)
    throws IllegalArgumentException;
  
  public abstract void setStatistic(Statistic paramStatistic, int paramInt)
    throws IllegalArgumentException;
  
  public abstract int getStatistic(Statistic paramStatistic)
    throws IllegalArgumentException;
  
  public abstract void incrementStatistic(Statistic paramStatistic, Material paramMaterial)
    throws IllegalArgumentException;
  
  public abstract void decrementStatistic(Statistic paramStatistic, Material paramMaterial)
    throws IllegalArgumentException;
  
  public abstract int getStatistic(Statistic paramStatistic, Material paramMaterial)
    throws IllegalArgumentException;
  
  public abstract void incrementStatistic(Statistic paramStatistic, Material paramMaterial, int paramInt)
    throws IllegalArgumentException;
  
  public abstract void decrementStatistic(Statistic paramStatistic, Material paramMaterial, int paramInt)
    throws IllegalArgumentException;
  
  public abstract void setStatistic(Statistic paramStatistic, Material paramMaterial, int paramInt)
    throws IllegalArgumentException;
  
  public abstract void incrementStatistic(Statistic paramStatistic, EntityType paramEntityType)
    throws IllegalArgumentException;
  
  public abstract void decrementStatistic(Statistic paramStatistic, EntityType paramEntityType)
    throws IllegalArgumentException;
  
  public abstract int getStatistic(Statistic paramStatistic, EntityType paramEntityType)
    throws IllegalArgumentException;
  
  public abstract void incrementStatistic(Statistic paramStatistic, EntityType paramEntityType, int paramInt)
    throws IllegalArgumentException;
  
  public abstract void decrementStatistic(Statistic paramStatistic, EntityType paramEntityType, int paramInt);
  
  public abstract void setStatistic(Statistic paramStatistic, EntityType paramEntityType, int paramInt);
  
  public abstract void setPlayerTime(long paramLong, boolean paramBoolean);
  
  public abstract long getPlayerTime();
  
  public abstract long getPlayerTimeOffset();
  
  public abstract boolean isPlayerTimeRelative();
  
  public abstract void resetPlayerTime();
  
  public abstract void setPlayerWeather(WeatherType paramWeatherType);
  
  public abstract WeatherType getPlayerWeather();
  
  public abstract void resetPlayerWeather();
  
  public abstract void giveExp(int paramInt);
  
  public abstract void giveExpLevels(int paramInt);
  
  public abstract float getExp();
  
  public abstract void setExp(float paramFloat);
  
  public abstract int getLevel();
  
  public abstract void setLevel(int paramInt);
  
  public abstract int getTotalExperience();
  
  public abstract void setTotalExperience(int paramInt);
  
  public abstract float getExhaustion();
  
  public abstract void setExhaustion(float paramFloat);
  
  public abstract float getSaturation();
  
  public abstract void setSaturation(float paramFloat);
  
  public abstract int getFoodLevel();
  
  public abstract void setFoodLevel(int paramInt);
  
  public abstract Location getBedSpawnLocation();
  
  public abstract void setBedSpawnLocation(Location paramLocation);
  
  public abstract void setBedSpawnLocation(Location paramLocation, boolean paramBoolean);
  
  public abstract boolean getAllowFlight();
  
  public abstract void setAllowFlight(boolean paramBoolean);
  
  public abstract void hidePlayer(Player paramPlayer);
  
  public abstract void showPlayer(Player paramPlayer);
  
  public abstract boolean canSee(Player paramPlayer);
  
  @Deprecated
  public abstract boolean isOnGround();
  
  public abstract boolean isFlying();
  
  public abstract void setFlying(boolean paramBoolean);
  
  public abstract void setFlySpeed(float paramFloat)
    throws IllegalArgumentException;
  
  public abstract void setWalkSpeed(float paramFloat)
    throws IllegalArgumentException;
  
  public abstract float getFlySpeed();
  
  public abstract float getWalkSpeed();
  
  @Deprecated
  public abstract void setTexturePack(String paramString);
  
  public abstract void setResourcePack(String paramString);
  
  public abstract Scoreboard getScoreboard();
  
  public abstract void setScoreboard(Scoreboard paramScoreboard)
    throws IllegalArgumentException, IllegalStateException;
  
  public abstract boolean isHealthScaled();
  
  public abstract void setHealthScaled(boolean paramBoolean);
  
  public abstract void setHealthScale(double paramDouble)
    throws IllegalArgumentException;
  
  public abstract double getHealthScale();
  
  public abstract Entity getSpectatorTarget();
  
  public abstract void setSpectatorTarget(Entity paramEntity);
  
  public abstract Spigot spigot();
  
  public static class Spigot
    extends Entity.Spigot
  {
    public InetSocketAddress getRawAddress()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean getCollidesWithEntities()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setCollidesWithEntities(boolean collides)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void respawn()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String getLocale()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Set<Player> getHiddenPlayers()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void sendMessage(BaseComponent component)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void sendMessage(BaseComponent... components)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
