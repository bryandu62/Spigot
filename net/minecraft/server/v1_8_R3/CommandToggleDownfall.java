package net.minecraft.server.v1_8_R3;

public class CommandToggleDownfall
  extends CommandAbstract
{
  public String getCommand()
  {
    return "toggledownfall";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.downfall.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    d();
    a(☃, this, "commands.downfall.success", new Object[0]);
  }
  
  protected void d()
  {
    WorldData ☃ = MinecraftServer.getServer().worldServer[0].getWorldData();
    
    ☃.setStorm(!☃.hasStorm());
  }
}
