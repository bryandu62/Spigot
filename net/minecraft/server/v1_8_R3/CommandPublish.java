package net.minecraft.server.v1_8_R3;

public class CommandPublish
  extends CommandAbstract
{
  public String getCommand()
  {
    return "publish";
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.publish.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    String ☃ = MinecraftServer.getServer().a(WorldSettings.EnumGamemode.SURVIVAL, false);
    if (☃ != null) {
      a(☃, this, "commands.publish.started", new Object[] { ☃ });
    } else {
      a(☃, this, "commands.publish.failed", new Object[0]);
    }
  }
}
