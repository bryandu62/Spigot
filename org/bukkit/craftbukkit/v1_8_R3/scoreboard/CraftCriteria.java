package org.bukkit.craftbukkit.v1_8_R3.scoreboard;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;

final class CraftCriteria
{
  static final Map<String, CraftCriteria> DEFAULTS;
  
  static
  {
    ImmutableMap.Builder<String, CraftCriteria> defaults = ImmutableMap.builder();
    for (Map.Entry<?, ?> entry : IScoreboardCriteria.criteria.entrySet())
    {
      String name = entry.getKey().toString();
      IScoreboardCriteria criteria = (IScoreboardCriteria)entry.getValue();
      
      defaults.put(name, new CraftCriteria(criteria));
    }
    DEFAULTS = defaults.build();
  }
  
  static final CraftCriteria DUMMY = (CraftCriteria)DEFAULTS.get("dummy");
  final IScoreboardCriteria criteria;
  final String bukkitName;
  
  private CraftCriteria(String bukkitName)
  {
    this.bukkitName = bukkitName;
    this.criteria = DUMMY.criteria;
  }
  
  private CraftCriteria(IScoreboardCriteria criteria)
  {
    this.criteria = criteria;
    this.bukkitName = criteria.getName();
  }
  
  static CraftCriteria getFromNMS(ScoreboardObjective objective)
  {
    return (CraftCriteria)DEFAULTS.get(objective.getCriteria().getName());
  }
  
  static CraftCriteria getFromBukkit(String name)
  {
    CraftCriteria criteria = (CraftCriteria)DEFAULTS.get(name);
    if (criteria != null) {
      return criteria;
    }
    return new CraftCriteria(name);
  }
  
  public boolean equals(Object that)
  {
    if (!(that instanceof CraftCriteria)) {
      return false;
    }
    return ((CraftCriteria)that).bukkitName.equals(this.bukkitName);
  }
  
  public int hashCode()
  {
    return this.bukkitName.hashCode() ^ CraftCriteria.class.hashCode();
  }
}
