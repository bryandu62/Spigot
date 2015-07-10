package org.bukkit;

import java.util.Date;
import java.util.Set;

public abstract interface BanList
{
  public abstract BanEntry getBanEntry(String paramString);
  
  public abstract BanEntry addBan(String paramString1, String paramString2, Date paramDate, String paramString3);
  
  public abstract Set<BanEntry> getBanEntries();
  
  public abstract boolean isBanned(String paramString);
  
  public abstract void pardon(String paramString);
  
  public static enum Type
  {
    NAME,  IP;
  }
}
