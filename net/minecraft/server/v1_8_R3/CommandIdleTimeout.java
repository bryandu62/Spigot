package net.minecraft.server.v1_8_R3;

public class CommandIdleTimeout
  extends CommandAbstract
{
  public String getCommand()
  {
    return "setidletimeout";
  }
  
  public int a()
  {
    return 3;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.setidletimeout.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length != 1) {
      throw new ExceptionUsage("commands.setidletimeout.usage", new Object[0]);
    }
    int ☃ = a(☃[0], 0);
    MinecraftServer.getServer().setIdleTimeout(☃);
    a(☃, this, "commands.setidletimeout.success", new Object[] { Integer.valueOf(☃) });
  }
}
