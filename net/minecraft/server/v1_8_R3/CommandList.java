package net.minecraft.server.v1_8_R3;

public class CommandList
  extends CommandAbstract
{
  public String getCommand()
  {
    return "list";
  }
  
  public int a()
  {
    return 0;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.players.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    int ☃ = MinecraftServer.getServer().I();
    ☃.sendMessage(new ChatMessage("commands.players.list", new Object[] { Integer.valueOf(☃), Integer.valueOf(MinecraftServer.getServer().J()) }));
    ☃.sendMessage(new ChatComponentText(MinecraftServer.getServer().getPlayerList().b((☃.length > 0) && ("uuids".equalsIgnoreCase(☃[0])))));
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃);
  }
}
