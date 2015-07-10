package org.bukkit.craftbukkit.v1_8_R3.command;

import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.libs.jline.Terminal;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.conversations.ConversationTracker;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;

public class ColouredConsoleSender
  extends CraftConsoleCommandSender
{
  private final Terminal terminal;
  private final Map<ChatColor, String> replacements = new EnumMap(ChatColor.class);
  private final ChatColor[] colors = ChatColor.values();
  
  protected ColouredConsoleSender()
  {
    this.terminal = ((CraftServer)getServer()).getReader().getTerminal();
    
    this.replacements.put(ChatColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
    this.replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
    this.replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
    this.replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
    this.replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
    this.replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
    this.replacements.put(ChatColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
    this.replacements.put(ChatColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
    this.replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
    this.replacements.put(ChatColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
    this.replacements.put(ChatColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
    this.replacements.put(ChatColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
    this.replacements.put(ChatColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
    this.replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
    this.replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
    this.replacements.put(ChatColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
    this.replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
    this.replacements.put(ChatColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
    this.replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
    this.replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
    this.replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
    this.replacements.put(ChatColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
  }
  
  public void sendMessage(String message)
  {
    if (this.terminal.isAnsiSupported())
    {
      if (!this.conversationTracker.isConversingModaly())
      {
        String result = message;
        ChatColor[] arrayOfChatColor;
        int i = (arrayOfChatColor = this.colors).length;
        for (int j = 0; j < i; j++)
        {
          ChatColor color = arrayOfChatColor[j];
          if (this.replacements.containsKey(color)) {
            result = result.replaceAll("(?i)" + color.toString(), (String)this.replacements.get(color));
          } else {
            result = result.replaceAll("(?i)" + color.toString(), "");
          }
        }
        System.out.println(result + Ansi.ansi().reset().toString());
      }
    }
    else {
      super.sendMessage(message);
    }
  }
  
  public static ConsoleCommandSender getInstance()
  {
    if (Bukkit.getConsoleSender() != null) {
      return Bukkit.getConsoleSender();
    }
    return new ColouredConsoleSender();
  }
}
