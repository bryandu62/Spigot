package net.minecraft.server.v1_8_R3;

public class CommandEntityData
  extends CommandAbstract
{
  public String getCommand()
  {
    return "entitydata";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.entitydata.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.entitydata.usage", new Object[0]);
    }
    Entity ☃ = b(☃, ☃[0]);
    if ((☃ instanceof EntityHuman)) {
      throw new CommandException("commands.entitydata.noPlayers", new Object[] { ☃.getScoreboardDisplayName() });
    }
    NBTTagCompound ☃ = new NBTTagCompound();
    ☃.e(☃);
    NBTTagCompound ☃ = (NBTTagCompound)☃.clone();
    NBTTagCompound ☃;
    try
    {
      ☃ = MojangsonParser.parse(a(☃, ☃, 1).c());
    }
    catch (MojangsonParseException ☃)
    {
      throw new CommandException("commands.entitydata.tagError", new Object[] { ☃.getMessage() });
    }
    ☃.remove("UUIDMost");
    ☃.remove("UUIDLeast");
    
    ☃.a(☃);
    if (☃.equals(☃)) {
      throw new CommandException("commands.entitydata.failed", new Object[] { ☃.toString() });
    }
    ☃.f(☃);
    
    a(☃, this, "commands.entitydata.success", new Object[] { ☃.toString() });
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
}
