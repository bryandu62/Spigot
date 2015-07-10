package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandTestFor
  extends CommandAbstract
{
  public String getCommand()
  {
    return "testfor";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.testfor.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 1) {
      throw new ExceptionUsage("commands.testfor.usage", new Object[0]);
    }
    Entity ☃ = b(☃, ☃[0]);
    NBTTagCompound ☃ = null;
    if (☃.length >= 2) {
      try
      {
        ☃ = MojangsonParser.parse(a(☃, 1));
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.testfor.tagError", new Object[] { ☃.getMessage() });
      }
    }
    if (☃ != null)
    {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.e(☃);
      if (!GameProfileSerializer.a(☃, ☃, true)) {
        throw new CommandException("commands.testfor.failure", new Object[] { ☃.getName() });
      }
    }
    a(☃, this, "commands.testfor.success", new Object[] { ☃.getName() });
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, MinecraftServer.getServer().getPlayers());
    }
    return null;
  }
}
