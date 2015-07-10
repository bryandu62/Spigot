package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandDifficulty
  extends CommandAbstract
{
  public String getCommand()
  {
    return "difficulty";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.difficulty.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length <= 0) {
      throw new ExceptionUsage("commands.difficulty.usage", new Object[0]);
    }
    EnumDifficulty ☃ = e(☃[0]);
    MinecraftServer.getServer().a(☃);
    
    a(☃, this, "commands.difficulty.success", new Object[] { new ChatMessage(☃.b(), new Object[0]) });
  }
  
  protected EnumDifficulty e(String ☃)
    throws ExceptionInvalidNumber
  {
    if ((☃.equalsIgnoreCase("peaceful")) || (☃.equalsIgnoreCase("p"))) {
      return EnumDifficulty.PEACEFUL;
    }
    if ((☃.equalsIgnoreCase("easy")) || (☃.equalsIgnoreCase("e"))) {
      return EnumDifficulty.EASY;
    }
    if ((☃.equalsIgnoreCase("normal")) || (☃.equalsIgnoreCase("n"))) {
      return EnumDifficulty.NORMAL;
    }
    if ((☃.equalsIgnoreCase("hard")) || (☃.equalsIgnoreCase("h"))) {
      return EnumDifficulty.HARD;
    }
    return EnumDifficulty.getById(a(☃, 0, 3));
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "peaceful", "easy", "normal", "hard" });
    }
    return null;
  }
}
