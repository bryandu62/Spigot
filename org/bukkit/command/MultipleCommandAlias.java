package org.bukkit.command;

public class MultipleCommandAlias
  extends Command
{
  private Command[] commands;
  
  public MultipleCommandAlias(String name, Command[] commands)
  {
    super(name);
    this.commands = commands;
  }
  
  public Command[] getCommands()
  {
    return this.commands;
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    boolean result = false;
    Command[] arrayOfCommand;
    int i = (arrayOfCommand = this.commands).length;
    for (int j = 0; j < i; j++)
    {
      Command command = arrayOfCommand[j];
      result |= command.execute(sender, commandLabel, args);
    }
    return result;
  }
}
