package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public abstract interface IScoreboardCriteria
{
  public static final Map<String, IScoreboardCriteria> criteria = ;
  public static final IScoreboardCriteria b = new ScoreboardBaseCriteria("dummy");
  public static final IScoreboardCriteria c = new ScoreboardBaseCriteria("trigger");
  public static final IScoreboardCriteria d = new ScoreboardBaseCriteria("deathCount");
  public static final IScoreboardCriteria e = new ScoreboardBaseCriteria("playerKillCount");
  public static final IScoreboardCriteria f = new ScoreboardBaseCriteria("totalKillCount");
  public static final IScoreboardCriteria g = new ScoreboardHealthCriteria("health");
  public static final IScoreboardCriteria[] h = { new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.BLACK), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.DARK_BLUE), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.DARK_GREEN), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.DARK_AQUA), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.DARK_RED), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.DARK_PURPLE), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.GOLD), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.GRAY), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.DARK_GRAY), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.BLUE), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.GREEN), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.AQUA), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.RED), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.LIGHT_PURPLE), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.YELLOW), new ScoreboardCriteriaInteger("teamkill.", EnumChatFormat.WHITE) };
  public static final IScoreboardCriteria[] i = { new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.BLACK), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.DARK_BLUE), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.DARK_GREEN), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.DARK_AQUA), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.DARK_RED), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.DARK_PURPLE), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.GOLD), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.GRAY), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.DARK_GRAY), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.BLUE), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.GREEN), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.AQUA), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.RED), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.LIGHT_PURPLE), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.YELLOW), new ScoreboardCriteriaInteger("killedByTeam.", EnumChatFormat.WHITE) };
  
  public abstract String getName();
  
  public abstract int getScoreModifier(List<EntityHuman> paramList);
  
  public abstract boolean isReadOnly();
  
  public abstract EnumScoreboardHealthDisplay c();
  
  public static enum EnumScoreboardHealthDisplay
  {
    private static final Map<String, EnumScoreboardHealthDisplay> c;
    private final String d;
    
    static
    {
      c = Maps.newHashMap();
      for (EnumScoreboardHealthDisplay ☃ : values()) {
        c.put(☃.a(), ☃);
      }
    }
    
    private EnumScoreboardHealthDisplay(String ☃)
    {
      this.d = ☃;
    }
    
    public String a()
    {
      return this.d;
    }
    
    public static EnumScoreboardHealthDisplay a(String ☃)
    {
      EnumScoreboardHealthDisplay ☃ = (EnumScoreboardHealthDisplay)c.get(☃);
      return ☃ == null ? INTEGER : ☃;
    }
  }
}
