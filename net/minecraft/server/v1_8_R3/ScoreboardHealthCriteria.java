package net.minecraft.server.v1_8_R3;

import java.util.List;

public class ScoreboardHealthCriteria
  extends ScoreboardBaseCriteria
{
  public ScoreboardHealthCriteria(String ☃)
  {
    super(☃);
  }
  
  public int getScoreModifier(List<EntityHuman> ☃)
  {
    float ☃ = 0.0F;
    for (EntityHuman ☃ : ☃) {
      ☃ += ☃.getHealth() + ☃.getAbsorptionHearts();
    }
    if (☃.size() > 0) {
      ☃ /= ☃.size();
    }
    return MathHelper.f(☃);
  }
  
  public boolean isReadOnly()
  {
    return true;
  }
  
  public IScoreboardCriteria.EnumScoreboardHealthDisplay c()
  {
    return IScoreboardCriteria.EnumScoreboardHealthDisplay.HEARTS;
  }
}
