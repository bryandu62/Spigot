package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Map;

public class ScoreboardBaseCriteria
  implements IScoreboardCriteria
{
  private final String j;
  
  public ScoreboardBaseCriteria(String ☃)
  {
    this.j = ☃;
    IScoreboardCriteria.criteria.put(☃, this);
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
