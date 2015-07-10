package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CounterStatistic
  extends Statistic
{
  public CounterStatistic(String ☃, IChatBaseComponent ☃, Counter ☃)
  {
    super(☃, ☃, ☃);
  }
  
  public CounterStatistic(String ☃, IChatBaseComponent ☃)
  {
    super(☃, ☃);
  }
  
  public Statistic h()
  {
    super.h();
    
    StatisticList.c.add(this);
    
    return this;
  }
}
