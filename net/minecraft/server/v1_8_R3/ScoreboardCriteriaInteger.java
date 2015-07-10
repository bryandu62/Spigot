package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Map;

public class ScoreboardCriteriaInteger
  implements IScoreboardCriteria
{
  private final String j;
  
  public ScoreboardCriteriaInteger(String ☃, EnumChatFormat ☃)
  {
    this.j = (☃ + ☃.e());
    IScoreboardCriteria.criteria.put(this.j, this);
  }
  
  public String getName()
  {
    return this.j;
  }
  
  public int getScoreModifier(List<EntityHuman> ☃)
  {
    return 0;
  }
  
  public boolean isReadOnly()
  {
    return false;
  }
  
  public IScoreboardCriteria.EnumScoreboardHealthDisplay c()
  {
    return IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER;
  }
}
