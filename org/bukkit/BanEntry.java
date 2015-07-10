package org.bukkit;

import java.util.Date;

public abstract interface BanEntry
{
  public abstract String getTarget();
  
  public abstract Date getCreated();
  
  public abstract void setCreated(Date paramDate);
  
  public abstract String getSource();
  
  public abstract void setSource(String paramString);
  
  public abstract Date getExpiration();
  
  public abstract void setExpiration(Date paramDate);
  
  public abstract String getReason();
  
  public abstract void setReason(String paramString);
  
  public abstract void save();
}
