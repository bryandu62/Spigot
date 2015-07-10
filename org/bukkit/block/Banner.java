package org.bukkit.block;

import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

public abstract interface Banner
  extends BlockState
{
  public abstract DyeColor getBaseColor();
  
  public abstract void setBaseColor(DyeColor paramDyeColor);
  
  public abstract List<Pattern> getPatterns();
  
  public abstract void setPatterns(List<Pattern> paramList);
  
  public abstract void addPattern(Pattern paramPattern);
  
  public abstract Pattern getPattern(int paramInt);
  
  public abstract Pattern removePattern(int paramInt);
  
  public abstract void setPattern(int paramInt, Pattern paramPattern);
  
  public abstract int numberOfPatterns();
}
