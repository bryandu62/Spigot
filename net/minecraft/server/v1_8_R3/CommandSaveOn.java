package net.minecraft.server.v1_8_R3;

public class CommandSaveOn
  extends CommandAbstract
{
  public String getCommand()
  {
    return "save-on";
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.save-on.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    MinecraftServer ☃ = MinecraftServer.getServer();
    
    boolean ☃ = false;
    for (int ☃ = 0; ☃ < ☃.worldServer.length; ☃++) {
      if (☃.worldServer[☃] != null)
      {
        WorldServer ☃ = ☃.worldServer[☃];
        if (☃.savingDisabled)
        {
          ☃.savingDisabled = false;
          ☃ = true;
        }
      }
    }
    if (☃) {
      a(☃, this, "commands.save.enabled", new Object[0]);
    } else {
      throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
    }
  }
}
