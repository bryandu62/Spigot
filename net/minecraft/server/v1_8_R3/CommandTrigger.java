package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class CommandTrigger
  extends CommandAbstract
{
  public String getCommand()
  {
    return "trigger";
  }
  
  public int a()
  {
    return 0;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.trigger.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 3) {
      throw new ExceptionUsage("commands.trigger.usage", new Object[0]);
    }
    EntityPlayer ☃;
    if ((☃ instanceof EntityPlayer))
    {
      ☃ = (EntityPlayer)☃;
    }
    else
    {
      Entity ☃ = ☃.f();
      EntityPlayer ☃;
      if ((☃ instanceof EntityPlayer)) {
        ☃ = (EntityPlayer)☃;
      } else {
        throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
      }
    }
    EntityPlayer ☃;
    Scoreboard ☃ = MinecraftServer.getServer().getWorldServer(0).getScoreboard();
    ScoreboardObjective ☃ = ☃.getObjective(☃[0]);
    if ((☃ == null) || (☃.getCriteria() != IScoreboardCriteria.c)) {
      throw new CommandException("commands.trigger.invalidObjective", new Object[] { ☃[0] });
    }
    int ☃ = a(☃[2]);
    if (!☃.b(☃.getName(), ☃)) {
      throw new CommandException("commands.trigger.invalidObjective", new Object[] { ☃[0] });
    }
    ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃.getName(), ☃);
    if (☃.g()) {
      throw new CommandException("commands.trigger.disabled", new Object[] { ☃[0] });
    }
    if ("set".equals(☃[1])) {
      ☃.setScore(☃);
    } else if ("add".equals(☃[1])) {
      ☃.addScore(☃);
    } else {
      throw new CommandException("commands.trigger.invalidMode", new Object[] { ☃[1] });
    }
    ☃.a(true);
    if (☃.playerInteractManager.isCreative()) {
      a(☃, this, "commands.trigger.success", new Object[] { ☃[0], ☃[1], ☃[2] });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1)
    {
      Scoreboard ☃ = MinecraftServer.getServer().getWorldServer(0).getScoreboard();
      List<String> ☃ = Lists.newArrayList();
      for (ScoreboardObjective ☃ : ☃.getObjectives()) {
        if (☃.getCriteria() == IScoreboardCriteria.c) {
          ☃.add(☃.getName());
        }
      }
      return a(☃, (String[])☃.toArray(new String[☃.size()]));
    }
    if (☃.length == 2) {
      return a(☃, new String[] { "add", "set" });
    }
    return null;
  }
}
