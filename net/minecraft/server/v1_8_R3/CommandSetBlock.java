package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandSetBlock
  extends CommandAbstract
{
  public String getCommand()
  {
    return "setblock";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.setblock.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 4) {
      throw new ExceptionUsage("commands.setblock.usage", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
    
    BlockPosition ☃ = a(☃, ☃, 0, false);
    Block ☃ = CommandAbstract.g(☃, ☃[3]);
    
    int ☃ = 0;
    if (☃.length >= 5) {
      ☃ = a(☃[4], 0, 15);
    }
    World ☃ = ☃.getWorld();
    if (!☃.isLoaded(☃)) {
      throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
    }
    NBTTagCompound ☃ = new NBTTagCompound();
    boolean ☃ = false;
    if ((☃.length >= 7) && (☃.isTileEntity()))
    {
      String ☃ = a(☃, ☃, 6).c();
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
    if (☃.length >= 6) {
      if (☃[5].equals("destroy"))
      {
        ☃.setAir(☃, true);
        if (☃ == Blocks.AIR) {
          a(☃, this, "commands.setblock.success", new Object[0]);
        }
      }
      else if ((☃[5].equals("keep")) && 
        (!☃.isEmpty(☃)))
      {
        throw new CommandException("commands.setblock.noChange", new Object[0]);
      }
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if (☃ != null)
    {
      if ((☃ instanceof IInventory)) {
        ((IInventory)☃).l();
      }
      ☃.setTypeAndData(☃, Blocks.AIR.getBlockData(), ☃ == Blocks.AIR ? 2 : 4);
    }
    IBlockData ☃ = ☃.fromLegacyData(☃);
    if (!☃.setTypeAndData(☃, ☃, 2)) {
      throw new CommandException("commands.setblock.noChange", new Object[0]);
    }
    if (☃)
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ != null)
      {
        ☃.setInt("x", ☃.getX());
        ☃.setInt("y", ☃.getY());
        ☃.setInt("z", ☃.getZ());
        
        ☃.a(☃);
      }
    }
    ☃.update(☃, ☃.getBlock());
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 1);
    a(☃, this, "commands.setblock.success", new Object[0]);
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if ((☃.length > 0) && (☃.length <= 3)) {
      return a(☃, 0, ☃);
    }
    if (☃.length == 4) {
      return a(☃, Block.REGISTRY.keySet());
    }
    if (☃.length == 6) {
      return a(☃, new String[] { "replace", "destroy", "keep" });
    }
    return null;
  }
}
