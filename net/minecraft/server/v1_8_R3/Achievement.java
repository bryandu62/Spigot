package net.minecraft.server.v1_8_R3;

import java.util.List;

public class Achievement
  extends Statistic
{
  public final int a;
  public final int b;
  public final Achievement c;
  private final String k;
  public final ItemStack d;
  private boolean m;
  
  public Achievement(String ☃, String ☃, int ☃, int ☃, Item ☃, Achievement ☃)
  {
    this(☃, ☃, ☃, ☃, new ItemStack(☃), ☃);
  }
  
  public Achievement(String ☃, String ☃, int ☃, int ☃, Block ☃, Achievement ☃)
  {
    this(☃, ☃, ☃, ☃, new ItemStack(☃), ☃);
  }
  
  public Achievement(String ☃, String ☃, int ☃, int ☃, ItemStack ☃, Achievement ☃)
  {
    super(☃, new ChatMessage("achievement." + ☃, new Object[0]));
    this.d = ☃;
    
    this.k = ("achievement." + ☃ + ".desc");
    this.a = ☃;
    this.b = ☃;
    if (☃ < AchievementList.a) {
      AchievementList.a = ☃;
    }
    if (☃ < AchievementList.b) {
      AchievementList.b = ☃;
    }
    if (☃ > AchievementList.c) {
      AchievementList.c = ☃;
    }
    if (☃ > AchievementList.d) {
      AchievementList.d = ☃;
    }
    this.c = ☃;
  }
  
  public Achievement a()
  {
    this.f = true;
    return this;
  }
  
  public Achievement b()
  {
    this.m = true;
    return this;
  }
  
  public Achievement c()
  {
    super.h();
    
    AchievementList.e.add(this);
    
    return this;
  }
  
  public boolean d()
  {
    return true;
  }
  
  public IChatBaseComponent e()
  {
    IChatBaseComponent ☃ = super.e();
    ☃.getChatModifier().setColor(g() ? EnumChatFormat.DARK_PURPLE : EnumChatFormat.GREEN);
    return ☃;
  }
  
  public Achievement a(Class<? extends IJsonStatistic> ☃)
  {
    return (Achievement)super.b(☃);
  }
  
  public boolean g()
  {
    return this.m;
  }
}
