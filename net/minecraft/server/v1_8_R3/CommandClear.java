package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandClear
  extends CommandAbstract
{
  public String getCommand()
  {
    return "clear";
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.clear.usage";
  }
  
  public int a()
  {
    return 2;
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    EntityPlayer ☃ = ☃.length == 0 ? b(☃) : a(☃, ☃[0]);
    Item ☃ = ☃.length >= 2 ? f(☃, ☃[1]) : null;
    int ☃ = ☃.length >= 3 ? a(☃[2], -1) : -1;
    int ☃ = ☃.length >= 4 ? a(☃[3], -1) : -1;
    
    NBTTagCompound ☃ = null;
    if (☃.length >= 5) {
      try
      {
        ☃ = MojangsonParser.parse(a(☃, 4));
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.clear.tagError", new Object[] { ☃.getMessage() });
      }
    }
    if ((☃.length >= 2) && (☃ == null)) {
      throw new CommandException("commands.clear.failure", new Object[] { ☃.getName() });
    }
    int ☃ = ☃.inventory.a(☃, ☃, ☃, ☃);
    ☃.defaultContainer.b();
    if (!☃.abilities.canInstantlyBuild) {
      ☃.broadcastCarriedItem();
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, ☃);
    if (☃ == 0) {
      throw new CommandException("commands.clear.failure", new Object[] { ☃.getName() });
    }
    if (☃ == 0) {
      ☃.sendMessage(new ChatMessage("commands.clear.testing", new Object[] { ☃.getName(), Integer.valueOf(☃) }));
    } else {
      a(☃, this, "commands.clear.success", new Object[] { ☃.getName(), Integer.valueOf(☃) });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, d());
    }
    if (☃.length == 2) {
      return a(☃, Item.REGISTRY.keySet());
    }
    return null;
  }
  
  protected String[] d()
  {
    return MinecraftServer.getServer().getPlayers();
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
}
