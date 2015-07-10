package net.minecraft.server.v1_8_R3;

public class CommandSaveAll
  extends CommandAbstract
{
  public String getCommand()
  {
    return "save-all";
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.save.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    MinecraftServer ☃ = MinecraftServer.getServer();
    ☃.sendMessage(new ChatMessage("commands.save.start", new Object[0]));
    if (☃.getPlayerList() != null) {
      ☃.getPlayerList().savePlayers();
    }
    try
    {
      for (int ☃ = 0; ☃ < ☃.worldServer.length; ☃++) {
        if (☃.worldServer[☃] != null)
        {
          WorldServer ☃ = ☃.worldServer[☃];
          boolean ☃ = ☃.savingDisabled;
          ☃.savingDisabled = false;
          ☃.save(true, null);
          ☃.savingDisabled = ☃;
        }
      }
      if ((☃.length > 0) && ("flush".equals(☃[0])))
      {
        ☃.sendMessage(new ChatMessage("commands.save.flushStart", new Object[0]));
        for (int ☃ = 0; ☃ < ☃.worldServer.length; ☃++) {
          if (☃.worldServer[☃] != null)
          {
            WorldServer ☃ = ☃.worldServer[☃];
            boolean ☃ = ☃.savingDisabled;
            ☃.savingDisabled = false;
            ☃.flushSave();
            ☃.savingDisabled = ☃;
          }
        }
        ☃.sendMessage(new ChatMessage("commands.save.flushEnd", new Object[0]));
      }
    }
    catch (ExceptionWorldConflict ☃)
    {
      a(☃, this, "commands.save.failed", new Object[] { ☃.getMessage() });
      return;
    }
    a(☃, this, "commands.save.success", new Object[0]);
  }
}
