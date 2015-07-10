package net.minecraft.server.v1_8_R3;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Statistic
{
  public final String name;
  private final IChatBaseComponent a;
  public boolean f;
  private final Counter b;
  private final IScoreboardCriteria c;
  private Class<? extends IJsonStatistic> d;
  
  public Statistic(String ☃, IChatBaseComponent ☃, Counter ☃)
  {
    this.name = ☃;
    this.a = ☃;
    this.b = ☃;
    this.c = new ScoreboardStatisticCriteria(this);
    
    IScoreboardCriteria.criteria.put(this.c.getName(), this.c);
  }
  
  public Statistic(String ☃, IChatBaseComponent ☃)
  {
    this(☃, ☃, g);
  }
  
  public Statistic i()
  {
    this.f = true;
    return this;
  }
  
  public Statistic h()
  {
    if (StatisticList.a.containsKey(this.name)) {
      throw new RuntimeException("Duplicate stat id: \"" + ((Statistic)StatisticList.a.get(this.name)).a + "\" and \"" + this.a + "\" at id " + this.name);
    }
    StatisticList.stats.add(this);
    StatisticList.a.put(this.name, this);
    
    return this;
  }
  
  public boolean d()
  {
    return false;
  }
  
  private static NumberFormat k = NumberFormat.getIntegerInstance(Locale.US);
  public static Counter g = new Counter() {};
  private static DecimalFormat l = new DecimalFormat("########0.00");
  public static Counter h = new Counter() {};
  public static Counter i = new Counter() {};
  public static Counter j = new Counter() {};
  
  public IChatBaseComponent e()
  {
    IChatBaseComponent ☃ = this.a.f();
    ☃.getChatModifier().setColor(EnumChatFormat.GRAY);
    ☃.getChatModifier().setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ACHIEVEMENT, new ChatComponentText(this.name)));
    return ☃;
  }
  
  public IChatBaseComponent j()
  {
    IChatBaseComponent ☃ = e();
    IChatBaseComponent ☃ = new ChatComponentText("[").addSibling(☃).a("]");
    ☃.setChatModifier(☃.getChatModifier());
    return ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ == null) || (getClass() != ☃.getClass())) {
      return false;
    }
    Statistic ☃ = (Statistic)☃;
    
    return this.name.equals(☃.name);
  }
  
  public int hashCode()
  {
    return this.name.hashCode();
  }
  
  public String toString()
  {
    return "Stat{id=" + this.name + ", nameId=" + this.a + ", awardLocallyOnly=" + this.f + ", formatter=" + this.b + ", objectiveCriteria=" + this.c + '}';
  }
  
  public IScoreboardCriteria k()
  {
    return this.c;
  }
  
  public Class<? extends IJsonStatistic> l()
  {
    return this.d;
  }
  
  public Statistic b(Class<? extends IJsonStatistic> ☃)
  {
    this.d = ☃;
    return this;
  }
}
