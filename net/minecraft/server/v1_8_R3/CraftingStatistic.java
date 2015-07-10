package net.minecraft.server.v1_8_R3;

import java.util.Map;

public class CraftingStatistic
  extends Statistic
{
  private final Item a;
  
  public CraftingStatistic(String ☃, String ☃, IChatBaseComponent ☃, Item ☃)
  {
    super(☃ + ☃, ☃);
    this.a = ☃;
    
    int ☃ = Item.getId(☃);
    if (☃ != 0) {
      IScoreboardCriteria.criteria.put(☃ + ☃, k());
    }
  }
}
