package org.bukkit.help;

import org.bukkit.command.Command;

public abstract interface HelpTopicFactory<TCommand extends Command>
{
  public abstract HelpTopic createTopic(TCommand paramTCommand);
}
