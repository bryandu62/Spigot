package com.avaje.ebean.config;

class ConfigPropertyMap
  implements GlobalProperties.PropertySource
{
  private final String serverName;
  
  public ConfigPropertyMap(String serverName)
  {
    this.serverName = serverName;
  }
  
  public String getServerName()
  {
    return this.serverName;
  }
  
  public String get(String key, String defaultValue)
  {
    String namedKey = "ebean." + this.serverName + "." + key;
    String inheritKey = "ebean." + key;
    String value = GlobalProperties.get(namedKey, null);
    if (value == null) {
      value = GlobalProperties.get(inheritKey, null);
    }
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
  
  public int getInt(String key, int defaultValue)
  {
    String value = get(key, String.valueOf(defaultValue));
    return Integer.parseInt(value);
  }
  
  public boolean getBoolean(String key, boolean defaultValue)
  {
    String value = get(key, String.valueOf(defaultValue));
    return Boolean.parseBoolean(value);
  }
  
  public <T extends Enum<T>> T getEnum(Class<T> enumType, String key, T defaultValue)
  {
    String level = get(key, defaultValue.name());
    return Enum.valueOf(enumType, level.toUpperCase());
  }
}
