package net.minecraft.server.v1_8_R3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandHelp
  extends CommandAbstract
{
  public String getCommand()
  {
    return "help";
  }
  
  public int a()
  {
    return 0;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.help.usage";
  }
  
  public List<String> b()
  {
    return Arrays.asList(new String[] { "?" });
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    List<ICommand> ☃ = d(☃);
    int ☃ = 7;
    int ☃ = (☃.size() - 1) / 7;
    int ☃ = 0;
    try
    {
      ☃ = ☃.length == 0 ? 0 : a(☃[0], 1, ☃ + 1) - 1;
    }
    catch (ExceptionInvalidNumber ☃)
    {
      Map<String, ICommand> ☃ = d();
      ICommand ☃ = (ICommand)☃.get(☃[0]);
      if (☃ != null) {
        throw new ExceptionUsage(☃.getUsage(☃), new Object[0]);
      }
      if (MathHelper.a(☃[0], -1) != -1) {
        throw ☃;
      }
      throw new ExceptionUnknownCommand();
    }
    int ☃ = Math.min((☃ + 1) * 7, ☃.size());
    
    ChatMessage ☃ = new ChatMessage("commands.help.header", new Object[] { Integer.valueOf(☃ + 1), Integer.valueOf(☃ + 1) });
    ☃.getChatModifier().setColor(EnumChatFormat.DARK_GREEN);
    ☃.sendMessage(☃);
    for (int ☃ = ☃ * 7; ☃ < ☃; ☃++)
    {
      ICommand ☃ = (ICommand)☃.get(☃);
      
      ChatMessage ☃ = new ChatMessage(☃.getUsage(☃), new Object[0]);
      ☃.getChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/" + ☃.getCommand() + " "));
      ☃.sendMessage(☃);
    }
    if ((☃ == 0) && ((☃ instanceof EntityHuman)))
    {
      ChatMessage ☃ = new ChatMessage("commands.help.footer", new Object[0]);
      ☃.getChatModifier().setColor(EnumChatFormat.GREEN);
      ☃.sendMessage(☃);
    }
  }
  
  protected List<ICommand> d(ICommandListener ☃)
  {
    List<ICommand> ☃ = MinecraftServer.getServer().getCommandHandler().a(☃);
    Collections.sort(☃);
    return ☃;
  }
  
  protected Map<String, ICommand> d()
  {
    return MinecraftServer.getServer().getCommandHandler().getCommands();
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1)
    {
      Set<String> ☃ = d().keySet();
      return a(☃, (String[])☃.toArray(new String[☃.size()]));
    }
    return null;
  }
}
