package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class ScoreboardTeamBase
{
  public boolean isAlly(ScoreboardTeamBase ☃)
  {
    if (☃ == null) {
      return false;
    }
    if (this == ☃) {
      return true;
    }
    return false;
  }
  
  public abstract String getName();
  
  public abstract String getFormattedName(String paramString);
  
  public abstract boolean allowFriendlyFire();
  
  public abstract Collection<String> getPlayerNameSet();
  
  public abstract EnumNameTagVisibility j();
  
  public static enum EnumNameTagVisibility
  {
    private static Map<String, EnumNameTagVisibility> g;
    public final String e;
    public final int f;
    
    static
    {
      g = Maps.newHashMap();
      for (EnumNameTagVisibility ☃ : values()) {
        g.put(☃.e, ☃);
      }
    }
    
    public static String[] a()
    {
      return (String[])g.keySet().toArray(new String[g.size()]);
    }
    
    public static EnumNameTagVisibility a(String ☃)
    {
      return (EnumNameTagVisibility)g.get(☃);
    }
    
    private EnumNameTagVisibility(String ☃, int ☃)
    {
      this.e = ☃;
      this.f = ☃;
    }
  }
}
