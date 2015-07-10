package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.List;

public class CommandAchievement
  extends CommandAbstract
{
  public String getCommand()
  {
    return "achievement";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.achievement.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.achievement.usage", new Object[0]);
    }
    final Statistic ☃ = StatisticList.getStatistic(☃[1]);
    if ((☃ == null) && (!☃[1].equals("*"))) {
      throw new CommandException("commands.achievement.unknownAchievement", new Object[] { ☃[1] });
    }
    final EntityPlayer ☃ = ☃.length >= 3 ? a(☃, ☃[2]) : b(☃);
    boolean ☃ = ☃[0].equalsIgnoreCase("give");
    boolean ☃ = ☃[0].equalsIgnoreCase("take");
    if ((!☃) && (!☃)) {
      return;
    }
    if (☃ == null)
    {
      if (☃)
      {
        for (Achievement ☃ : AchievementList.e) {
          ☃.b(☃);
        }
        a(☃, this, "commands.achievement.give.success.all", new Object[] { ☃.getName() });
      }
      else if (☃)
      {
        for (Achievement ☃ : Lists.reverse(AchievementList.e)) {
          ☃.a(☃);
        }
        a(☃, this, "commands.achievement.take.success.all", new Object[] { ☃.getName() });
      }
      return;
    }
    if ((☃ instanceof Achievement))
    {
      Achievement ☃ = (Achievement)☃;
      if (☃)
      {
        if (☃.getStatisticManager().hasAchievement(☃)) {
          throw new CommandException("commands.achievement.alreadyHave", new Object[] { ☃.getName(), ☃.j() });
        }
        List<Achievement> ☃ = Lists.newArrayList();
        while ((☃.c != null) && (!☃.getStatisticManager().hasAchievement(☃.c)))
        {
          ☃.add(☃.c);
          ☃ = ☃.c;
        }
        for (Achievement ☃ : Lists.reverse(☃)) {
          ☃.b(☃);
        }
      }
      else if (☃)
      {
        if (!☃.getStatisticManager().hasAchievement(☃)) {
          throw new CommandException("commands.achievement.dontHave", new Object[] { ☃.getName(), ☃.j() });
        }
        List<Achievement> ☃ = Lists.newArrayList(Iterators.filter(AchievementList.e.iterator(), new Predicate()
        {
          public boolean a(Achievement ☃)
          {
            return (☃.getStatisticManager().hasAchievement(☃)) && (☃ != ☃);
          }
        }));
        List<Achievement> ☃ = Lists.newArrayList(☃);
        for (Achievement ☃ : ☃)
        {
          Achievement ☃ = ☃;
          boolean ☃ = false;
          while (☃ != null)
          {
            if (☃ == ☃) {
              ☃ = true;
            }
            ☃ = ☃.c;
          }
          if (!☃)
          {
            ☃ = ☃;
            while (☃ != null)
            {
              ☃.remove(☃);
              ☃ = ☃.c;
            }
          }
        }
        for (Achievement ☃ : ☃) {
          ☃.a(☃);
        }
      }
    }
    if (☃)
    {
      ☃.b(☃);
      a(☃, this, "commands.achievement.give.success.one", new Object[] { ☃.getName(), ☃.j() });
    }
    else if (☃)
    {
      ☃.a(☃);
      a(☃, this, "commands.achievement.take.success.one", new Object[] { ☃.j(), ☃.getName() });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "give", "take" });
    }
    if (☃.length == 2)
    {
      List<String> ☃ = Lists.newArrayList();
      for (Statistic ☃ : StatisticList.stats) {
        ☃.add(☃.name);
      }
      return a(☃, ☃);
    }
    if (☃.length == 3) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 2;
  }
}
