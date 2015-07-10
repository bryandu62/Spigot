package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.Cancellable;

public class StatisticManager
{
  protected final Map<Statistic, StatisticWrapper> a = Maps.newConcurrentMap();
  
  public boolean hasAchievement(Achievement achievement)
  {
    return getStatisticValue(achievement) > 0;
  }
  
  public boolean b(Achievement achievement)
  {
    return (achievement.c == null) || (hasAchievement(achievement.c));
  }
  
  public void b(EntityHuman entityhuman, Statistic statistic, int i)
  {
    if ((!statistic.d()) || (b((Achievement)statistic)))
    {
      Cancellable cancellable = CraftEventFactory.handleStatisticsIncrease(entityhuman, statistic, getStatisticValue(statistic), i);
      if ((cancellable != null) && (cancellable.isCancelled())) {
        return;
      }
      setStatistic(entityhuman, statistic, getStatisticValue(statistic) + i);
    }
  }
  
  public void setStatistic(EntityHuman entityhuman, Statistic statistic, int i)
  {
    StatisticWrapper statisticwrapper = (StatisticWrapper)this.a.get(statistic);
    if (statisticwrapper == null)
    {
      statisticwrapper = new StatisticWrapper();
      this.a.put(statistic, statisticwrapper);
    }
    statisticwrapper.a(i);
  }
  
  public int getStatisticValue(Statistic statistic)
  {
    StatisticWrapper statisticwrapper = (StatisticWrapper)this.a.get(statistic);
    
    return statisticwrapper == null ? 0 : statisticwrapper.a();
  }
  
  public <T extends IJsonStatistic> T b(Statistic statistic)
  {
    StatisticWrapper statisticwrapper = (StatisticWrapper)this.a.get(statistic);
    
    return statisticwrapper != null ? statisticwrapper.b() : null;
  }
  
  public <T extends IJsonStatistic> T a(Statistic statistic, T t0)
  {
    StatisticWrapper statisticwrapper = (StatisticWrapper)this.a.get(statistic);
    if (statisticwrapper == null)
    {
      statisticwrapper = new StatisticWrapper();
      this.a.put(statistic, statisticwrapper);
    }
    statisticwrapper.a(t0);
    return t0;
  }
}
