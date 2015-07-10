package org.bukkit.help;

import java.util.Collection;
import java.util.List;

public abstract interface HelpMap
{
  public abstract HelpTopic getHelpTopic(String paramString);
  
  public abstract Collection<HelpTopic> getHelpTopics();
  
  public abstract void addTopic(HelpTopic paramHelpTopic);
  
  public abstract void clear();
  
  public abstract void registerHelpTopicFactory(Class<?> paramClass, HelpTopicFactory<?> paramHelpTopicFactory);
  
  public abstract List<String> getIgnoredPlugins();
}
