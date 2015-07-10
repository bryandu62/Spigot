package org.bukkit.command;

import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public final class PluginCommand
  extends Command
  implements PluginIdentifiableCommand
{
  private final Plugin owningPlugin;
  private CommandExecutor executor;
  private TabCompleter completer;
  
  protected PluginCommand(String name, Plugin owner)
  {
    super(name);
    this.executor = owner;
    this.owningPlugin = owner;
    this.usageMessage = "";
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    boolean success = false;
    if (!this.owningPlugin.isEnabled()) {
      return false;
    }
    if (!testPermission(sender)) {
      return true;
    }
    try
    {
      success = this.executor.onCommand(sender, this, commandLabel, args);
    }
    catch (Throwable ex)
    {
      throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + this.owningPlugin.getDescription().getFullName(), ex);
    }
    if ((!success) && (this.usageMessage.length() > 0))
    {
      String[] arrayOfString;
      int i = (arrayOfString = this.usageMessage.replace("<command>", commandLabel).split("\n")).length;
      for (int j = 0; j < i; j++)
      {
        String line = arrayOfString[j];
        sender.sendMessage(line);
      }
    }
    return success;
  }
  
  public void setExecutor(CommandExecutor executor)
  {
    this.executor = (executor == null ? this.owningPlugin : executor);
  }
  
  public CommandExecutor getExecutor()
  {
    return this.executor;
  }
  
  public void setTabCompleter(TabCompleter completer)
  {
    this.completer = completer;
  }
  
  public TabCompleter getTabCompleter()
  {
    return this.completer;
  }
  
  public Plugin getPlugin()
  {
    return this.owningPlugin;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws CommandException, IllegalArgumentException
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    
    List<String> completions = null;
    try
    {
      if (this.completer != null) {
        completions = this.completer.onTabComplete(sender, this, alias, args);
      }
      if ((completions == null) && ((this.executor instanceof TabCompleter))) {
        completions = ((TabCompleter)this.executor).onTabComplete(sender, this, alias, args);
      }
    }
    catch (Throwable ex)
    {
      StringBuilder message = new StringBuilder();
      message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
      String[] arrayOfString;
      int i = (arrayOfString = args).length;
      for (int j = 0; j < i; j++)
      {
        String arg = arrayOfString[j];
        message.append(arg).append(' ');
      }
      message.deleteCharAt(message.length() - 1).append("' in plugin ").append(this.owningPlugin.getDescription().getFullName());
      throw new CommandException(message.toString(), ex);
    }
    if (completions == null) {
      return super.tabComplete(sender, alias, args);
    }
    return completions;
  }
  
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder(super.toString());
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    stringBuilder.append(", ").append(this.owningPlugin.getDescription().getFullName()).append(')');
    return stringBuilder.toString();
  }
}
