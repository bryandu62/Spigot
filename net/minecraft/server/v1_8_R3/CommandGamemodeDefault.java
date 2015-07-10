package net.minecraft.server.v1_8_R3;

public class CommandGamemodeDefault
  extends CommandGamemode
{
  public String getCommand()
  {
    return "defaultgamemode";
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.defaultgamemode.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length <= 0) {
      throw new ExceptionUsage("commands.defaultgamemode.usage", new Object[0]);
    }
    WorldSettings.EnumGamemode ☃ = h(☃, ☃[0]);
    a(☃);
    
    a(☃, this, "commands.defaultgamemode.success", new Object[] { new ChatMessage("gameMode." + ☃.b(), new Object[0]) });
  }
  
  protected void a(WorldSettings.EnumGamemode ☃)
  {
    MinecraftServer ☃ = MinecraftServer.getServer();
    ☃.setGamemode(☃);
    if (☃.getForceGamemode()) {
      for (EntityPlayer ☃ : MinecraftServer.getServer().getPlayerList().v())
      {
        ☃.a(☃);
        ☃.fallDistance = 0.0F;
      }
    }
  }
}
