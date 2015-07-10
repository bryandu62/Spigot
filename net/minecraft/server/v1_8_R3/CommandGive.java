package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class CommandGive
  extends CommandAbstract
{
  public String getCommand()
  {
    return "give";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.give.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.give.usage", new Object[0]);
    }
    EntityHuman ☃ = a(☃, ☃[0]);
    Item ☃ = f(☃, ☃[1]);
    int ☃ = ☃.length >= 3 ? a(☃[2], 1, 64) : 1;
    int ☃ = ☃.length >= 4 ? a(☃[3]) : 0;
    ItemStack ☃ = new ItemStack(☃, ☃, ☃);
    if (☃.length >= 5)
    {
      String ☃ = a(☃, ☃, 4).c();
      try
      {
        ☃.setTag(MojangsonParser.parse(☃));
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.give.tagError", new Object[] { ☃.getMessage() });
      }
    }
    boolean ☃ = ☃.inventory.pickup(☃);
    if (☃)
    {
      ☃.world.makeSound(☃, "random.pop", 0.2F, ((☃.bc().nextFloat() - ☃.bc().nextFloat()) * 0.7F + 1.0F) * 2.0F);
      ☃.defaultContainer.b();
    }
    if ((!☃) || (☃.count > 0))
    {
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, ☃ - ☃.count);
      EntityItem ☃ = ☃.drop(☃, false);
      if (☃ != null)
      {
        ☃.q();
        ☃.b(☃.getName());
      }
    }
    else
    {
      ☃.count = 1;
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, ☃);
      EntityItem ☃ = ☃.drop(☃, false);
      if (☃ != null) {
        ☃.v();
      }
    }
    a(☃, this, "commands.give.success", new Object[] { ☃.C(), Integer.valueOf(☃), ☃.getName() });
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
