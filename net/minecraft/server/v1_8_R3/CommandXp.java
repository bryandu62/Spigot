package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandXp
  extends CommandAbstract
{
  public String getCommand()
  {
    return "xp";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.xp.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length <= 0) {
      throw new ExceptionUsage("commands.xp.usage", new Object[0]);
    }
    String ☃ = ☃[0];
    boolean ☃ = (☃.endsWith("l")) || (☃.endsWith("L"));
    if ((☃) && (☃.length() > 1)) {
      ☃ = ☃.substring(0, ☃.length() - 1);
    }
    int ☃ = a(☃);
    
    boolean ☃ = ☃ < 0;
    if (☃) {
      ☃ *= -1;
    }
    EntityHuman ☃ = ☃.length > 1 ? a(☃, ☃[1]) : b(☃);
    if (☃)
    {
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃.expLevel);
      if (☃)
      {
        ☃.levelDown(-☃);
        a(☃, this, "commands.xp.success.negative.levels", new Object[] { Integer.valueOf(☃), ☃.getName() });
      }
      else
      {
        ☃.levelDown(☃);
        a(☃, this, "commands.xp.success.levels", new Object[] { Integer.valueOf(☃), ☃.getName() });
      }
    }
    else
    {
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃.expTotal);
      if (☃) {
        throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
      }
      ☃.giveExp(☃);
      a(☃, this, "commands.xp.success", new Object[] { Integer.valueOf(☃), ☃.getName() });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 2) {
      return a(☃, d());
    }
    return null;
  }
  
  protected String[] d()
  {
    return MinecraftServer.getServer().getPlayers();
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 1;
  }
}
