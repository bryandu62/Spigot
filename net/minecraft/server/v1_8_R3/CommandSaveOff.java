package net.minecraft.server.v1_8_R3;

public class CommandSaveOff
  extends CommandAbstract
{
  public String getCommand()
  {
    return "save-off";
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.save-off.usage";
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
        if (!☃.savingDisabled)
        {
          ☃.savingDisabled = true;
          ☃ = true;
        }
      }
    }
    if (☃) {
      a(☃, this, "commands.save.disabled", new Object[0]);
    } else {
      throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
    }
  }
}
