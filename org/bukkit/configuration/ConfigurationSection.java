package org.bukkit.configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract interface ConfigurationSection
{
  public abstract Set<String> getKeys(boolean paramBoolean);
  
  public abstract Map<String, Object> getValues(boolean paramBoolean);
  
  public abstract boolean contains(String paramString);
  
  public abstract boolean isSet(String paramString);
  
  public abstract String getCurrentPath();
  
  public abstract String getName();
  
  public abstract Configuration getRoot();
  
  public abstract ConfigurationSection getParent();
  
  public abstract Object get(String paramString);
  
  public abstract Object get(String paramString, Object paramObject);
  
  public abstract void set(String paramString, Object paramObject);
  
  public abstract ConfigurationSection createSection(String paramString);
  
  public abstract ConfigurationSection createSection(String paramString, Map<?, ?> paramMap);
  
  public abstract String getString(String paramString);
  
  public abstract String getString(String paramString1, String paramString2);
  
  public abstract boolean isString(String paramString);
  
  public abstract int getInt(String paramString);
  
  public abstract int getInt(String paramString, int paramInt);
  
  public abstract boolean isInt(String paramString);
  
  public abstract boolean getBoolean(String paramString);
  
  public abstract boolean getBoolean(String paramString, boolean paramBoolean);
  
  public abstract boolean isBoolean(String paramString);
  
  public abstract double getDouble(String paramString);
  
  public abstract double getDouble(String paramString, double paramDouble);
  
  public abstract boolean isDouble(String paramString);
  
  public abstract long getLong(String paramString);
  
  public abstract long getLong(String paramString, long paramLong);
  
  public abstract boolean isLong(String paramString);
  
  public abstract List<?> getList(String paramString);
  
  public abstract List<?> getList(String paramString, List<?> paramList);
  
  public abstract boolean isList(String paramString);
  
  public abstract List<String> getStringList(String paramString);
  
  public abstract List<Integer> getIntegerList(String paramString);
  
  public abstract List<Boolean> getBooleanList(String paramString);
  
  public abstract List<Double> getDoubleList(String paramString);
  
  public abstract List<Float> getFloatList(String paramString);
  
  public abstract List<Long> getLongList(String paramString);
  
  public abstract List<Byte> getByteList(String paramString);
  
  public abstract List<Character> getCharacterList(String paramString);
  
  public abstract List<Short> getShortList(String paramString);
  
  public abstract List<Map<?, ?>> getMapList(String paramString);
  
  public abstract Vector getVector(String paramString);
  
  public abstract Vector getVector(String paramString, Vector paramVector);
  
  public abstract boolean isVector(String paramString);
  
  public abstract OfflinePlayer getOfflinePlayer(String paramString);
  
  public abstract OfflinePlayer getOfflinePlayer(String paramString, OfflinePlayer paramOfflinePlayer);
  
  public abstract boolean isOfflinePlayer(String paramString);
  
  public abstract ItemStack getItemStack(String paramString);
  
  public abstract ItemStack getItemStack(String paramString, ItemStack paramItemStack);
  
  public abstract boolean isItemStack(String paramString);
  
  public abstract Color getColor(String paramString);
  
  public abstract Color getColor(String paramString, Color paramColor);
  
  public abstract boolean isColor(String paramString);
  
  public abstract ConfigurationSection getConfigurationSection(String paramString);
  
  public abstract boolean isConfigurationSection(String paramString);
  
  public abstract ConfigurationSection getDefaultSection();
  
  public abstract void addDefault(String paramString, Object paramObject);
}
