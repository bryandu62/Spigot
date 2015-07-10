package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler
  implements ICommandHandler
{
  private static final Logger a = ;
  private final Map<String, ICommand> b = Maps.newHashMap();
  private final Set<ICommand> c = Sets.newHashSet();
  
  public int a(ICommandListener ☃, String ☃)
  {
    ☃ = ☃.trim();
    if (☃.startsWith("/")) {
      ☃ = ☃.substring(1);
    }
    String[] ☃ = ☃.split(" ");
    String ☃ = ☃[0];
    
    ☃ = a(☃);
    
    ICommand ☃ = (ICommand)this.b.get(☃);
    int ☃ = a(☃, ☃);
    int ☃ = 0;
    if (☃ == null)
    {
      ChatMessage ☃ = new ChatMessage("commands.generic.notFound", new Object[0]);
      ☃.getChatModifier().setColor(EnumChatFormat.RED);
      ☃.sendMessage(☃);
    }
    else if (☃.canUse(☃))
    {
      if (☃ > -1)
      {
        List<Entity> ☃ = PlayerSelector.getPlayers(☃, ☃[☃], Entity.class);
        String ☃ = ☃[☃];
        ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, ☃.size());
        for (Entity ☃ : ☃)
        {
          ☃[☃] = ☃.getUniqueID().toString();
          if (a(☃, ☃, ☃, ☃)) {
            ☃++;
          }
        }
        ☃[☃] = ☃;
      }
      else
      {
        ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, 1);
        if (a(☃, ☃, ☃, ☃)) {
          ☃++;
        }
      }
    }
    else
    {
      ChatMessage ☃ = new ChatMessage("commands.generic.permission", new Object[0]);
      ☃.getChatModifier().setColor(EnumChatFormat.RED);
      ☃.sendMessage(☃);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.SUCCESS_COUNT, ☃);
    return ☃;
  }
  
  protected boolean a(ICommandListener ☃, String[] ☃, ICommand ☃, String ☃)
  {
    try
    {
      ☃.execute(☃, ☃);
      return true;
    }
    catch (ExceptionUsage ☃)
    {
      ChatMessage ☃ = new ChatMessage("commands.generic.usage", new Object[] { new ChatMessage(☃.getMessage(), ☃.getArgs()) });
      ☃.getChatModifier().setColor(EnumChatFormat.RED);
      ☃.sendMessage(☃);
    }
    catch (CommandException ☃)
    {
      ChatMessage ☃ = new ChatMessage(☃.getMessage(), ☃.getArgs());
      ☃.getChatModifier().setColor(EnumChatFormat.RED);
      ☃.sendMessage(☃);
    }
    catch (Throwable ☃)
    {
      ChatMessage ☃ = new ChatMessage("commands.generic.exception", new Object[0]);
      ☃.getChatModifier().setColor(EnumChatFormat.RED);
      ☃.sendMessage(☃);
      a.warn("Couldn't process command: '" + ☃ + "'");
    }
    return false;
  }
  
  public ICommand a(ICommand ☃)
  {
    this.b.put(☃.getCommand(), ☃);
    this.c.add(☃);
    for (String ☃ : ☃.b())
    {
      ICommand ☃ = (ICommand)this.b.get(☃);
      if ((☃ == null) || (!☃.getCommand().equals(☃))) {
        this.b.put(☃, ☃);
      }
    }
    return ☃;
  }
  
  private static String[] a(String[] ☃)
  {
    String[] ☃ = new String[☃.length - 1];
    System.arraycopy(☃, 1, ☃, 0, ☃.length - 1);
    return ☃;
  }
  
  public List<String> a(ICommandListener ☃, String ☃, BlockPosition ☃)
  {
    String[] ☃ = ☃.split(" ", -1);
    String ☃ = ☃[0];
    if (☃.length == 1)
    {
      List<String> ☃ = Lists.newArrayList();
      for (Map.Entry<String, ICommand> ☃ : this.b.entrySet()) {
        if ((CommandAbstract.a(☃, (String)☃.getKey())) && (((ICommand)☃.getValue()).canUse(☃))) {
          ☃.add(☃.getKey());
        }
      }
      return ☃;
    }
    if (☃.length > 1)
    {
      ICommand ☃ = (ICommand)this.b.get(☃);
      if ((☃ != null) && (☃.canUse(☃))) {
        return ☃.tabComplete(☃, a(☃), ☃);
      }
    }
    return null;
  }
  
  public List<ICommand> a(ICommandListener ☃)
  {
    List<ICommand> ☃ = Lists.newArrayList();
    for (ICommand ☃ : this.c) {
      if (☃.canUse(☃)) {
        ☃.add(☃);
      }
    }
    return ☃;
  }
  
  public Map<String, ICommand> getCommands()
  {
    return this.b;
  }
  
  private int a(ICommand ☃, String[] ☃)
  {
    if (☃ == null) {
      return -1;
    }
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      if ((☃.isListStart(☃, ☃)) && (PlayerSelector.isList(☃[☃]))) {
        return ☃;
      }
    }
    return -1;
  }
}
