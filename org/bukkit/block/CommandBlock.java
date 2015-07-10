package org.bukkit.block;

public abstract interface CommandBlock
  extends BlockState
{
  public abstract String getCommand();
  
  public abstract void setCommand(String paramString);
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
}
