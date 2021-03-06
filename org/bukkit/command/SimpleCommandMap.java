package org.bukkit.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.command.defaults.VersionCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.Java15Compat;
import org.bukkit.util.StringUtil;
import org.spigotmc.CustomTimingsHandler;

public class SimpleCommandMap
  implements CommandMap
{
  private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", 16);
  protected final Map<String, Command> knownCommands = new HashMap();
  private final Server server;
  
  public SimpleCommandMap(Server server)
  {
    this.server = server;
    setDefaultCommands();
  }
  
  private void setDefaultCommands()
  {
    register("bukkit", new VersionCommand("version"));
    register("bukkit", new ReloadCommand("reload"));
    register("bukkit", new PluginsCommand("plugins"));
    register("bukkit", new TimingsCommand("timings"));
  }
  
  public void setFallbackCommands()
  {
    register("bukkit", new HelpCommand());
  }
  
  public void registerAll(String fallbackPrefix, List<Command> commands)
  {
    if (commands != null) {
      for (Command c : commands) {
        register(fallbackPrefix, c);
      }
    }
  }
  
  public boolean register(String fallbackPrefix, Command command)
  {
    return register(command.getName(), fallbackPrefix, command);
  }
  
  public boolean register(String label, String fallbackPrefix, Command command)
  {
    label = label.toLowerCase().trim();
    fallbackPrefix = fallbackPrefix.toLowerCase().trim();
    boolean registered = register(label, command, false, fallbackPrefix);
    
    Iterator<String> iterator = command.getAliases().iterator();
    while (iterator.hasNext()) {
      if (!register((String)iterator.next(), command, true, fallbackPrefix)) {
        iterator.remove();
      }
    }
    if (!registered) {
      command.setLabel(fallbackPrefix + ":" + label);
    }
    command.register(this);
    
    return registered;
  }
  
  private synchronized boolean register(String label, Command command, boolean isAlias, String fallbackPrefix)
  {
    this.knownCommands.put(fallbackPrefix + ":" + label, command);
    if ((((command instanceof VanillaCommand)) || (isAlias)) && (this.knownCommands.containsKey(label))) {
      return false;
    }
    boolean registered = true;
    
    Command conflict = (Command)this.knownCommands.get(label);
    if ((conflict != null) && (conflict.getLabel().equals(label))) {
      return false;
    }
    if (!isAlias) {
      command.setLabel(label);
    }
    this.knownCommands.put(label, command);
    
    return registered;
  }
  
  public boolean dispatch(CommandSender sender, String commandLine)
    throws CommandException
  {
    String[] args = PATTERN_ON_SPACE.split(commandLine);
    if (args.length == 0) {
      return false;
    }
    String sentCommandLabel = args[0].toLowerCase();
    Command target = getCommand(sentCommandLabel);
    if (target == null) {
      return false;
    }
    try
    {
      target.timings.startTiming();
      
      target.execute(sender, sentCommandLabel, (String[])Java15Compat.Arrays_copyOfRange(args, 1, args.length));
      target.timings.stopTiming();
    }
    catch (CommandException ex)
    {
      target.timings.stopTiming();
      throw ex;
    }
    catch (Throwable ex)
    {
      target.timings.stopTiming();
      throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex);
    }
    return true;
  }
  
  public synchronized void clearCommands()
  {
    for (Map.Entry<String, Command> entry : this.knownCommands.entrySet()) {
      ((Command)entry.getValue()).unregister(this);
    }
    this.knownCommands.clear();
    setDefaultCommands();
  }
  
  public Command getCommand(String name)
  {
    Command target = (Command)this.knownCommands.get(name.toLowerCase());
    return target;
  }
  
  public List<String> tabComplete(CommandSender sender, String cmdLine)
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(cmdLine, "Command line cannot null");
    
    int spaceIndex = cmdLine.indexOf(' ');
    if (spaceIndex == -1)
    {
      ArrayList<String> completions = new ArrayList();
      Map<String, Command> knownCommands = this.knownCommands;
      
      String prefix = (sender instanceof Player) ? "/" : "";
      for (Map.Entry<String, Command> commandEntry : knownCommands.entrySet())
      {
        Command command = (Command)commandEntry.getValue();
        if (command.testPermissionSilent(sender))
        {
          String name = (String)commandEntry.getKey();
          if (StringUtil.startsWithIgnoreCase(name, cmdLine)) {
            completions.add(prefix + name);
          }
        }
      }
      Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
      return completions;
    }
    String commandName = cmdLine.substring(0, spaceIndex);
    Command target = getCommand(commandName);
    if (target == null) {
      return null;
    }
    if (!target.testPermissionSilent(sender)) {
      return null;
    }
    String argLine = cmdLine.substring(spaceIndex + 1, cmdLine.length());
    String[] args = PATTERN_ON_SPACE.split(argLine, -1);
    try
    {
      return target.tabComplete(sender, commandName, args);
    }
    catch (CommandException ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      throw new CommandException("Unhandled exception executing tab-completer for '" + cmdLine + "' in " + target, ex);
    }
  }
  
  public Collection<Command> getCommands()
  {
    return Collections.unmodifiableCollection(this.knownCommands.values());
  }
  
  public void registerServerAliases()
  {
    Map<String, String[]> values = this.server.getCommandAliases();
    for (String alias : values.keySet()) {
      if ((alias.contains(":")) || (alias.contains(" ")))
      {
        this.server.getLogger().warning("Could not register alias " + alias + " because it contains illegal characters");
      }
      else
      {
        String[] commandStrings = (String[])values.get(alias);
        List<String> targets = new ArrayList();
        StringBuilder bad = new StringBuilder();
        String[] arrayOfString1;
        int i = (arrayOfString1 = commandStrings).length;
        for (int j = 0; j < i; j++)
        {
          String commandString = arrayOfString1[j];
          String[] commandArgs = commandString.split(" ");
          Command command = getCommand(commandArgs[0]);
          if (command == null)
          {
            if (bad.length() > 0) {
              bad.append(", ");
            }
            bad.append(commandString);
          }
          else
          {
            targets.add(commandString);
          }
        }
        if (bad.length() > 0) {
          this.server.getLogger().warning("Could not register alias " + alias + " because it contains commands that do not exist: " + bad);
        } else if (targets.size() > 0) {
          this.knownCommands.put(alias.toLowerCase(), new FormattedCommandAlias(alias.toLowerCase(), (String[])targets.toArray(new String[targets.size()])));
        } else {
          this.knownCommands.remove(alias.toLowerCase());
        }
      }
    }
  }
}
