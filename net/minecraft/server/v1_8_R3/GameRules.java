package net.minecraft.server.v1_8_R3;

import java.util.Set;
import java.util.TreeMap;

public class GameRules
{
  private TreeMap<String, GameRuleValue> a = new TreeMap();
  
  public GameRules()
  {
    a("doFireTick", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("mobGriefing", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("keepInventory", "false", EnumGameRuleType.BOOLEAN_VALUE);
    a("doMobSpawning", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("doMobLoot", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("doTileDrops", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("doEntityDrops", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("commandBlockOutput", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("naturalRegeneration", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("doDaylightCycle", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("logAdminCommands", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("showDeathMessages", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("randomTickSpeed", "3", EnumGameRuleType.NUMERICAL_VALUE);
    a("sendCommandFeedback", "true", EnumGameRuleType.BOOLEAN_VALUE);
    a("reducedDebugInfo", "false", EnumGameRuleType.BOOLEAN_VALUE);
  }
  
  public void a(String ☃, String ☃, EnumGameRuleType ☃)
  {
    this.a.put(☃, new GameRuleValue(☃, ☃));
  }
  
  public void set(String ☃, String ☃)
  {
    GameRuleValue ☃ = (GameRuleValue)this.a.get(☃);
    if (☃ != null) {
      ☃.a(☃);
    } else {
      a(☃, ☃, EnumGameRuleType.ANY_VALUE);
    }
  }
  
  public String get(String ☃)
  {
    GameRuleValue ☃ = (GameRuleValue)this.a.get(☃);
    if (☃ != null) {
      return ☃.a();
    }
    return "";
  }
  
  public boolean getBoolean(String ☃)
  {
    GameRuleValue ☃ = (GameRuleValue)this.a.get(☃);
    if (☃ != null) {
      return ☃.b();
    }
    return false;
  }
  
  public int c(String ☃)
  {
    GameRuleValue ☃ = (GameRuleValue)this.a.get(☃);
    if (☃ != null) {
      return ☃.c();
    }
    return 0;
  }
  
  public NBTTagCompound a()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    for (String ☃ : this.a.keySet())
    {
      GameRuleValue ☃ = (GameRuleValue)this.a.get(☃);
      ☃.setString(☃, ☃.a());
    }
    return ☃;
  }
  
  public void a(NBTTagCompound ☃)
  {
    Set<String> ☃ = ☃.c();
    for (String ☃ : ☃)
    {
      String ☃ = ☃;
      String ☃ = ☃.getString(☃);
      
      set(☃, ☃);
    }
  }
  
  public String[] getGameRules()
  {
    Set<String> ☃ = this.a.keySet();
    return (String[])☃.toArray(new String[☃.size()]);
  }
  
  public boolean contains(String ☃)
  {
    return this.a.containsKey(☃);
  }
  
  public boolean a(String ☃, EnumGameRuleType ☃)
  {
    GameRuleValue ☃ = (GameRuleValue)this.a.get(☃);
    if ((☃ != null) && ((☃.e() == ☃) || (☃ == EnumGameRuleType.ANY_VALUE))) {
      return true;
    }
    return false;
  }
  
  public static enum EnumGameRuleType
  {
    private EnumGameRuleType() {}
  }
  
  static class GameRuleValue
  {
    private String a;
    private boolean b;
    private int c;
    private double d;
    private final GameRules.EnumGameRuleType e;
    
    public GameRuleValue(String ☃, GameRules.EnumGameRuleType ☃)
    {
      this.e = ☃;
      a(☃);
    }
    
    public void a(String ☃)
    {
      this.a = ☃;
      this.b = Boolean.parseBoolean(☃);
      this.c = (this.b ? 1 : 0);
      try
      {
        this.c = Integer.parseInt(☃);
      }
      catch (NumberFormatException localNumberFormatException) {}
      try
      {
        this.d = Double.parseDouble(☃);
      }
      catch (NumberFormatException localNumberFormatException1) {}
    }
    
    public String a()
    {
      return this.a;
    }
    
    public boolean b()
    {
      return this.b;
    }
    
    public int c()
    {
      return this.c;
    }
    
    public GameRules.EnumGameRuleType e()
    {
      return this.e;
    }
  }
}
