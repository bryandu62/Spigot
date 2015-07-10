package org.bukkit.command;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class FormattedCommandAlias
  extends Command
{
  private final String[] formatStrings;
  
  public FormattedCommandAlias(String alias, String[] formatStrings)
  {
    super(alias);
    this.formatStrings = formatStrings;
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    boolean result = false;
    ArrayList<String> commands = new ArrayList();
    String[] arrayOfString;
    int i = (arrayOfString = this.formatStrings).length;
    for (int j = 0; j < i; j++)
    {
      String formatString = arrayOfString[j];
      try
      {
        commands.add(buildCommand(formatString, args));
      }
      catch (Throwable throwable)
      {
        if ((throwable instanceof IllegalArgumentException)) {
          sender.sendMessage(throwable.getMessage());
        } else {
          sender.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
        }
        return false;
      }
    }
    for (String command : commands) {
      result |= Bukkit.dispatchCommand(sender, command);
    }
    return result;
  }
  
  private String buildCommand(String formatString, String[] args)
  {
    int index = formatString.indexOf("$");
    while (index != -1)
    {
      int start = index;
      if ((index > 0) && (formatString.charAt(start - 1) == '\\'))
      {
        formatString = formatString.substring(0, start - 1) + formatString.substring(start);
        index = formatString.indexOf("$", index);
      }
      else
      {
        boolean required = false;
        if (formatString.charAt(index + 1) == '$')
        {
          required = true;
          
          index++;
        }
        index++;
        int argStart = index;
        while ((index < formatString.length()) && (inRange(formatString.charAt(index) - '0', 0, 9))) {
          index++;
        }
        if (argStart == index) {
          throw new IllegalArgumentException("Invalid replacement token");
        }
        int position = Integer.valueOf(formatString.substring(argStart, index)).intValue();
        if (position == 0) {
          throw new IllegalArgumentException("Invalid replacement token");
        }
        position--;
        
        boolean rest = false;
        if ((index < formatString.length()) && (formatString.charAt(index) == '-'))
        {
          rest = true;
          
          index++;
        }
        int end = index;
        if ((required) && (position >= args.length)) {
          throw new IllegalArgumentException("Missing required argument " + (position + 1));
        }
        StringBuilder replacement = new StringBuilder();
        if ((rest) && (position < args.length)) {
          for (int i = position; i < args.length; i++)
          {
            if (i != position) {
              replacement.append(' ');
            }
            replacement.append(args[i]);
          }
        } else if (position < args.length) {
          replacement.append(args[position]);
        }
        formatString = formatString.substring(0, start) + replacement.toString() + formatString.substring(end);
        
        index = start + replacement.length();
        
        index = formatString.indexOf("$", index);
      }
    }
    return formatString;
  }
  
  private static boolean inRange(int i, int j, int k)
  {
    return (i >= j) && (i <= k);
  }
}
