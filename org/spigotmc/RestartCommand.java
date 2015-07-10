package org.spigotmc;

import java.io.File;
import java.io.PrintStream;
import java.util.Queue;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.ServerConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RestartCommand
  extends Command
{
  public RestartCommand(String name)
  {
    super(name);
    this.description = "Restarts the server";
    this.usageMessage = "/restart";
    setPermission("bukkit.command.restart");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (testPermission(sender)) {
      MinecraftServer.getServer().processQueue.add(new Runnable()
      {
        public void run() {}
      });
    }
    return true;
  }
  
  public static void restart()
  {
    restart(new File(SpigotConfig.restartScript));
  }
  
  public static void restart(File script)
  {
    AsyncCatcher.enabled = false;
    try
    {
      if (script.isFile())
      {
        System.out.println("Attempting to restart with " + SpigotConfig.restartScript);
        
        WatchdogThread.doStop();
        for (EntityPlayer p : MinecraftServer.getServer().getPlayerList().players) {
          p.playerConnection.disconnect(SpigotConfig.restartMessage);
        }
        try
        {
          Thread.sleep(100L);
        }
        catch (InterruptedException localInterruptedException1) {}
        MinecraftServer.getServer().getServerConnection().b();
        try
        {
          Thread.sleep(100L);
        }
        catch (InterruptedException localInterruptedException2) {}
        try
        {
          MinecraftServer.getServer().stop();
        }
        catch (Throwable localThrowable) {}
        Thread shutdownHook = new Thread()
        {
          public void run()
          {
            try
            {
              String os = System.getProperty("os.name").toLowerCase();
              if (os.contains("win")) {
                Runtime.getRuntime().exec("cmd /c start " + RestartCommand.this.getPath());
              } else {
                Runtime.getRuntime().exec(
                  new String[] {
                  "sh", RestartCommand.this.getPath() });
              }
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
          }
        };
        shutdownHook.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
      }
      else
      {
        System.out.println("Startup script '" + SpigotConfig.restartScript + "' does not exist! Stopping server.");
      }
      System.exit(0);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
