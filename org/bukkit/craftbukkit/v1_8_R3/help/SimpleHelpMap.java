package org.bukkit.craftbukkit.v1_8_R3.help;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.command.VanillaCommandWrapper;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.HelpTopicFactory;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.Plugin;

public class SimpleHelpMap
  implements HelpMap
{
  private HelpTopic defaultTopic;
  private final Map<String, HelpTopic> helpTopics;
  private final Map<Class, HelpTopicFactory<Command>> topicFactoryMap;
  private final CraftServer server;
  private HelpYamlReader yaml;
  
  public SimpleHelpMap(CraftServer server)
  {
    this.helpTopics = new TreeMap(HelpTopicComparator.topicNameComparatorInstance());
    this.topicFactoryMap = new HashMap();
    this.server = server;
    this.yaml = new HelpYamlReader(server);
    
    Predicate indexFilter = Predicates.not(Predicates.instanceOf(CommandAliasHelpTopic.class));
    if (!this.yaml.commandTopicsInMasterIndex()) {
      indexFilter = Predicates.and(indexFilter, Predicates.not(new IsCommandTopicPredicate(null)));
    }
    this.defaultTopic = new IndexHelpTopic("Index", null, null, Collections2.filter(this.helpTopics.values(), indexFilter), "Use /help [n] to get page n of help.");
    
    registerHelpTopicFactory(MultipleCommandAlias.class, new MultipleCommandAliasHelpTopicFactory());
  }
  
  public synchronized HelpTopic getHelpTopic(String topicName)
  {
    if (topicName.equals("")) {
      return this.defaultTopic;
    }
    if (this.helpTopics.containsKey(topicName)) {
      return (HelpTopic)this.helpTopics.get(topicName);
    }
    return null;
  }
  
  public Collection<HelpTopic> getHelpTopics()
  {
    return this.helpTopics.values();
  }
  
  public synchronized void addTopic(HelpTopic topic)
  {
    if (!this.helpTopics.containsKey(topic.getName())) {
      this.helpTopics.put(topic.getName(), topic);
    }
  }
  
  public synchronized void clear()
  {
    this.helpTopics.clear();
  }
  
  public List<String> getIgnoredPlugins()
  {
    return this.yaml.getIgnoredPlugins();
  }
  
  public synchronized void initializeGeneralTopics()
  {
    this.yaml = new HelpYamlReader(this.server);
    for (HelpTopic topic : this.yaml.getGeneralTopics()) {
      addTopic(topic);
    }
    for (HelpTopic topic : this.yaml.getIndexTopics()) {
      if (topic.getName().equals("Default")) {
        this.defaultTopic = topic;
      } else {
        addTopic(topic);
      }
    }
  }
  
  public synchronized void initializeCommands()
  {
    Set<String> ignoredPlugins = new HashSet(this.yaml.getIgnoredPlugins());
    if (ignoredPlugins.contains("All")) {
      return;
    }
    for (Command command : this.server.getCommandMap().getCommands()) {
      if (!commandInIgnoredPlugin(command, ignoredPlugins))
      {
        for (Class c : this.topicFactoryMap.keySet())
        {
          if (c.isAssignableFrom(command.getClass()))
          {
            HelpTopic t = ((HelpTopicFactory)this.topicFactoryMap.get(c)).createTopic(command);
            if (t == null) {
              break;
            }
            addTopic(t);
            break;
          }
          if (((command instanceof PluginCommand)) && (c.isAssignableFrom(((PluginCommand)command).getExecutor().getClass())))
          {
            HelpTopic t = ((HelpTopicFactory)this.topicFactoryMap.get(c)).createTopic(command);
            if (t == null) {
              break;
            }
            addTopic(t);
            break;
          }
        }
        addTopic(new GenericCommandHelpTopic(command));
      }
    }
    for (Command command : this.server.getCommandMap().getCommands()) {
      if (!commandInIgnoredPlugin(command, ignoredPlugins)) {
        for (String alias : command.getAliases()) {
          if (this.server.getCommandMap().getCommand(alias) == command) {
            addTopic(new CommandAliasHelpTopic("/" + alias, "/" + command.getLabel(), this));
          }
        }
      }
    }
    Collection<HelpTopic> filteredTopics = Collections2.filter(this.helpTopics.values(), Predicates.instanceOf(CommandAliasHelpTopic.class));
    if (!filteredTopics.isEmpty()) {
      addTopic(new IndexHelpTopic("Aliases", "Lists command aliases", null, filteredTopics));
    }
    Object pluginIndexes = new HashMap();
    fillPluginIndexes((Map)pluginIndexes, this.server.getCommandMap().getCommands());
    for (Map.Entry<String, Set<HelpTopic>> entry : ((Map)pluginIndexes).entrySet()) {
      addTopic(new IndexHelpTopic((String)entry.getKey(), "All commands for " + (String)entry.getKey(), null, (Collection)entry.getValue(), "Below is a list of all " + (String)entry.getKey() + " commands:"));
    }
    for (HelpTopicAmendment amendment : this.yaml.getTopicAmendments()) {
      if (this.helpTopics.containsKey(amendment.getTopicName()))
      {
        ((HelpTopic)this.helpTopics.get(amendment.getTopicName())).amendTopic(amendment.getShortText(), amendment.getFullText());
        if (amendment.getPermission() != null) {
          ((HelpTopic)this.helpTopics.get(amendment.getTopicName())).amendCanSee(amendment.getPermission());
        }
      }
    }
  }
  
  private void fillPluginIndexes(Map<String, Set<HelpTopic>> pluginIndexes, Collection<? extends Command> commands)
  {
    for (Command command : commands)
    {
      String pluginName = getCommandPluginName(command);
      if (pluginName != null)
      {
        HelpTopic topic = getHelpTopic("/" + command.getLabel());
        if (topic != null)
        {
          if (!pluginIndexes.containsKey(pluginName)) {
            pluginIndexes.put(pluginName, new TreeSet(HelpTopicComparator.helpTopicComparatorInstance()));
          }
          ((Set)pluginIndexes.get(pluginName)).add(topic);
        }
      }
    }
  }
  
  private String getCommandPluginName(Command command)
  {
    if ((command instanceof VanillaCommandWrapper)) {
      return "Minecraft";
    }
    if (((command instanceof BukkitCommand)) || ((command instanceof VanillaCommand))) {
      return "Bukkit";
    }
    if ((command instanceof PluginIdentifiableCommand)) {
      return ((PluginIdentifiableCommand)command).getPlugin().getName();
    }
    return null;
  }
  
  private boolean commandInIgnoredPlugin(Command command, Set<String> ignoredPlugins)
  {
    if ((((command instanceof BukkitCommand)) || ((command instanceof VanillaCommand))) && (ignoredPlugins.contains("Bukkit"))) {
      return true;
    }
    if (((command instanceof PluginIdentifiableCommand)) && (ignoredPlugins.contains(((PluginIdentifiableCommand)command).getPlugin().getName()))) {
      return true;
    }
    return false;
  }
  
  public void registerHelpTopicFactory(Class commandClass, HelpTopicFactory factory)
  {
    if ((!Command.class.isAssignableFrom(commandClass)) && (!CommandExecutor.class.isAssignableFrom(commandClass))) {
      throw new IllegalArgumentException("commandClass must implement either Command or CommandExecutor!");
    }
    this.topicFactoryMap.put(commandClass, factory);
  }
  
  private class IsCommandTopicPredicate
    implements Predicate<HelpTopic>
  {
    private IsCommandTopicPredicate() {}
    
    public boolean apply(HelpTopic topic)
    {
      return topic.getName().charAt(0) == '/';
    }
  }
}
