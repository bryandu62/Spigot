package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class CommandWeather
  extends CommandAbstract
{
  public String getCommand()
  {
    return "weather";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.weather.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if ((☃.length < 1) || (☃.length > 2)) {
      throw new ExceptionUsage("commands.weather.usage", new Object[0]);
    }
    int ☃ = (300 + new Random().nextInt(600)) * 20;
    if (☃.length >= 2) {
      ☃ = a(☃[1], 1, 1000000) * 20;
    }
    World ☃ = MinecraftServer.getServer().worldServer[0];
    WorldData ☃ = ☃.getWorldData();
    if ("clear".equalsIgnoreCase(☃[0]))
    {
      ☃.i(☃);
      ☃.setWeatherDuration(0);
      ☃.setThunderDuration(0);
      ☃.setStorm(false);
      ☃.setThundering(false);
      a(☃, this, "commands.weather.clear", new Object[0]);
    }
    else if ("rain".equalsIgnoreCase(☃[0]))
    {
      ☃.i(0);
      ☃.setWeatherDuration(☃);
      ☃.setThunderDuration(☃);
      ☃.setStorm(true);
      ☃.setThundering(false);
      a(☃, this, "commands.weather.rain", new Object[0]);
    }
    else if ("thunder".equalsIgnoreCase(☃[0]))
    {
      ☃.i(0);
      ☃.setWeatherDuration(☃);
      ☃.setThunderDuration(☃);
      ☃.setStorm(true);
      ☃.setThundering(true);
      a(☃, this, "commands.weather.thunder", new Object[0]);
    }
    else
    {
      throw new ExceptionUsage("commands.weather.usage", new Object[0]);
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "clear", "rain", "thunder" });
    }
    return null;
  }
}
