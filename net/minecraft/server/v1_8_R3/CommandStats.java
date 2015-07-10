package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;

public class CommandStats
  extends CommandAbstract
{
  public String getCommand()
  {
    return "stats";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.stats.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 1) {
      throw new ExceptionUsage("commands.stats.usage", new Object[0]);
    }
    boolean ☃;
    if (☃[0].equals("entity"))
    {
      ☃ = false;
    }
    else
    {
      boolean ☃;
      if (☃[0].equals("block")) {
        ☃ = true;
      } else {
        throw new ExceptionUsage("commands.stats.usage", new Object[0]);
      }
    }
    boolean ☃;
    int ☃;
    int ☃;
    if (☃)
    {
      if (☃.length < 5) {
        throw new ExceptionUsage("commands.stats.block.usage", new Object[0]);
      }
      ☃ = 4;
    }
    else
    {
      if (☃.length < 3) {
        throw new ExceptionUsage("commands.stats.entity.usage", new Object[0]);
      }
      ☃ = 2;
    }
    String ☃ = ☃[(☃++)];
    if ("set".equals(☃))
    {
      if (☃.length < ☃ + 3)
      {
        if (☃ == 5) {
          throw new ExceptionUsage("commands.stats.block.set.usage", new Object[0]);
        }
        throw new ExceptionUsage("commands.stats.entity.set.usage", new Object[0]);
      }
    }
    else if ("clear".equals(☃))
    {
      if (☃.length < ☃ + 1)
      {
        if (☃ == 5) {
          throw new ExceptionUsage("commands.stats.block.clear.usage", new Object[0]);
        }
        throw new ExceptionUsage("commands.stats.entity.clear.usage", new Object[0]);
      }
    }
    else {
      throw new ExceptionUsage("commands.stats.usage", new Object[0]);
    }
    CommandObjectiveExecutor.EnumCommandResult ☃ = CommandObjectiveExecutor.EnumCommandResult.a(☃[(☃++)]);
    if (☃ == null) {
      throw new CommandException("commands.stats.failed", new Object[0]);
    }
    World ☃ = ☃.getWorld();
    CommandObjectiveExecutor ☃;
    CommandObjectiveExecutor ☃;
    if (☃)
    {
      BlockPosition ☃ = a(☃, ☃, 1, false);
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ == null) {
        throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
      }
      CommandObjectiveExecutor ☃;
      if ((☃ instanceof TileEntityCommand))
      {
        ☃ = ((TileEntityCommand)☃).c();
      }
      else
      {
        CommandObjectiveExecutor ☃;
        if ((☃ instanceof TileEntitySign)) {
          ☃ = ((TileEntitySign)☃).d();
        } else {
          throw new CommandException("commands.stats.noCompatibleBlock", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
        }
      }
    }
    else
    {
      Entity ☃ = b(☃, ☃[1]);
      ☃ = ☃.aU();
    }
    if ("set".equals(☃))
    {
      String ☃ = ☃[(☃++)];
      String ☃ = ☃[☃];
      if ((☃.length() == 0) || (☃.length() == 0)) {
        throw new CommandException("commands.stats.failed", new Object[0]);
      }
      CommandObjectiveExecutor.a(☃, ☃, ☃, ☃);
      a(☃, this, "commands.stats.success", new Object[] { ☃.b(), ☃, ☃ });
    }
    else if ("clear".equals(☃))
    {
      CommandObjectiveExecutor.a(☃, ☃, null, null);
      a(☃, this, "commands.stats.cleared", new Object[] { ☃.b() });
    }
    if (☃)
    {
      BlockPosition ☃ = a(☃, ☃, 1, false);
      TileEntity ☃ = ☃.getTileEntity(☃);
      ☃.update();
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "entity", "block" });
    }
    if ((☃.length == 2) && (☃[0].equals("entity"))) {
      return a(☃, d());
    }
    if ((☃.length >= 2) && (☃.length <= 4) && (☃[0].equals("block"))) {
      return a(☃, 1, ☃);
    }
    if (((☃.length == 3) && (☃[0].equals("entity"))) || ((☃.length == 5) && (☃[0].equals("block")))) {
      return a(☃, new String[] { "set", "clear" });
    }
    if (((☃.length == 4) && (☃[0].equals("entity"))) || ((☃.length == 6) && (☃[0].equals("block")))) {
      return a(☃, CommandObjectiveExecutor.EnumCommandResult.c());
    }
    if (((☃.length == 6) && (☃[0].equals("entity"))) || ((☃.length == 8) && (☃[0].equals("block")))) {
      return a(☃, e());
    }
    return null;
  }
  
  protected String[] d()
  {
    return MinecraftServer.getServer().getPlayers();
  }
  
  protected List<String> e()
  {
    Collection<ScoreboardObjective> ☃ = MinecraftServer.getServer().getWorldServer(0).getScoreboard().getObjectives();
    List<String> ☃ = Lists.newArrayList();
    for (ScoreboardObjective ☃ : ☃) {
      if (!☃.getCriteria().isReadOnly()) {
        ☃.add(☃.getName());
      }
    }
    return ☃;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return (☃.length > 0) && (☃[0].equals("entity")) && (☃ == 1);
  }
}
