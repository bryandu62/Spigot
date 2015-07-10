package net.minecraft.server.v1_8_R3;

public class CommandStop
  extends CommandAbstract
{
  public String getCommand()
  {
    return "stop";
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.stop.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (MinecraftServer.getServer().worldServer != null) {
      a(☃, this, "commands.stop.start", new Object[0]);
    }
    MinecraftServer.getServer().safeShutdown();
  }
}
