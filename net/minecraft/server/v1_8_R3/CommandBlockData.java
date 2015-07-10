package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandBlockData
  extends CommandAbstract
{
  public String getCommand()
  {
    return "blockdata";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.blockdata.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 4) {
      throw new ExceptionUsage("commands.blockdata.usage", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
    
    BlockPosition ☃ = a(☃, ☃, 0, false);
    
    World ☃ = ☃.getWorld();
    if (!☃.isLoaded(☃)) {
      throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if (☃ == null) {
      throw new CommandException("commands.blockdata.notValid", new Object[0]);
    }
    NBTTagCompound ☃ = new NBTTagCompound();
    ☃.b(☃);
    NBTTagCompound ☃ = (NBTTagCompound)☃.clone();
    NBTTagCompound ☃;
    try
    {
      ☃ = MojangsonParser.parse(a(☃, ☃, 3).c());
    }
    catch (MojangsonParseException ☃)
    {
      throw new CommandException("commands.blockdata.tagError", new Object[] { ☃.getMessage() });
    }
    ☃.a(☃);
    
    ☃.setInt("x", ☃.getX());
    ☃.setInt("y", ☃.getY());
    ☃.setInt("z", ☃.getZ());
    if (☃.equals(☃)) {
      throw new CommandException("commands.blockdata.failed", new Object[] { ☃.toString() });
    }
    ☃.a(☃);
    ☃.update();
    ☃.notify(☃);
    
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 1);
    a(☃, this, "commands.blockdata.success", new Object[] { ☃.toString() });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if ((☃.length > 0) && (☃.length <= 3)) {
      return a(☃, 0, ☃);
    }
    return null;
  }
}
