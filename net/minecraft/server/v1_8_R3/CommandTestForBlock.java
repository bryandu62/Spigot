package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandTestForBlock
  extends CommandAbstract
{
  public String getCommand()
  {
    return "testforblock";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.testforblock.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 4) {
      throw new ExceptionUsage("commands.testforblock.usage", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
    
    BlockPosition ☃ = a(☃, ☃, 0, false);
    Block ☃ = Block.getByName(☃[3]);
    if (☃ == null) {
      throw new ExceptionInvalidNumber("commands.setblock.notFound", new Object[] { ☃[3] });
    }
    int ☃ = -1;
    if (☃.length >= 5) {
      ☃ = a(☃[4], -1, 15);
    }
    World ☃ = ☃.getWorld();
    if (!☃.isLoaded(☃)) {
      throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
    }
    NBTTagCompound ☃ = new NBTTagCompound();
    boolean ☃ = false;
    if ((☃.length >= 6) && (☃.isTileEntity()))
    {
      String ☃ = a(☃, ☃, 5).c();
      try
      {
        ☃ = MojangsonParser.parse(☃);
        ☃ = true;
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.setblock.tagError", new Object[] { ☃.getMessage() });
      }
    }
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    if (☃ != ☃) {
      throw new CommandException("commands.testforblock.failed.tile", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()), ☃.getName(), ☃.getName() });
    }
    if (☃ > -1)
    {
      int ☃ = ☃.getBlock().toLegacyData(☃);
      if (☃ != ☃) {
        throw new CommandException("commands.testforblock.failed.data", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()), Integer.valueOf(☃), Integer.valueOf(☃) });
      }
    }
    if (☃)
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ == null) {
        throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
      }
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.b(☃);
      if (!GameProfileSerializer.a(☃, ☃, true)) {
        throw new CommandException("commands.testforblock.failed.nbt", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
      }
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 1);
    a(☃, this, "commands.testforblock.success", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if ((☃.length > 0) && (☃.length <= 3)) {
      return a(☃, 0, ☃);
    }
    if (☃.length == 4) {
      return a(☃, Block.REGISTRY.keySet());
    }
    return null;
  }
}
