package org.bukkit.craftbukkit;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal;
import org.bukkit.craftbukkit.libs.joptsimple.ArgumentAcceptingOptionSpec;
import org.bukkit.craftbukkit.libs.joptsimple.OptionException;
import org.bukkit.craftbukkit.libs.joptsimple.OptionParser;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSpecBuilder;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.fusesource.jansi.AnsiConsole;

public class Main
{
  public static boolean useJline = true;
  public static boolean useConsole = true;
  
  public static void main(String[] args)
  {
    OptionParser parser = new OptionParser() {};
    OptionSet options = null;
    try
    {
      options = parser.parse(args);
    }
    catch (OptionException ex)
    {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
    }
    if ((options == null) || (options.has("?")))
    {
      try
      {
        parser.printHelpOn(System.out);
      }
      catch (IOException ex)
      {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    else if (options.has("v"))
    {
      System.out.println(CraftServer.class.getPackage().getImplementationVersion());
    }
    else
    {
      String path = new File(".").getAbsolutePath();
      if ((path.contains("!")) || (path.contains("+")))
      {
        System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.");
        return;
      }
      try
      {
        String jline_UnsupportedTerminal = new String(new char[] { 'j', 'l', 'i', 'n', 'e', '.', 'U', 'n', 's', 'u', 'p', 'p', 'o', 'r', 't', 'e', 'd', 'T', 'e', 'r', 'm', 'i', 'n', 'a', 'l' });
        String jline_terminal = new String(new char[] { 'j', 'l', 'i', 'n', 'e', '.', 't', 'e', 'r', 'm', 'i', 'n', 'a', 'l' });
        
        useJline = !jline_UnsupportedTerminal.equals(System.getProperty(jline_terminal));
        if (options.has("nojline"))
        {
          System.setProperty("user.language", "en");
          useJline = false;
        }
        if (useJline) {
          AnsiConsole.systemInstall();
        } else {
          System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", UnsupportedTerminal.class.getName());
        }
        if (options.has("noconsole")) {
          useConsole = false;
        }
        int maxPermGen = 0;
        for (String s : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
          if (s.startsWith("-XX:MaxPermSize"))
          {
            maxPermGen = Integer.parseInt(s.replaceAll("[^\\d]", ""));
            maxPermGen <<= 10 * "kmg".indexOf(Character.toLowerCase(s.charAt(s.length() - 1)));
          }
        }
        if ((Float.parseFloat(System.getProperty("java.class.version")) < 52.0F) && (maxPermGen < 131072))
        {
          System.out.println("Warning, your max perm gen size is not set or less than 128mb. It is recommended you restart Java with the following argument: -XX:MaxPermSize=128M");
          System.out.println("Please see http://www.spigotmc.org/wiki/changing-permgen-size/ for more details and more in-depth instructions.");
        }
        System.out.println("Loading libraries, please wait...");
        MinecraftServer.main(options);
      }
      catch (Throwable t)
      {
        t.printStackTrace();
      }
    }
  }
  
  private static List<String> asList(String... params)
  {
    return Arrays.asList(params);
  }
}
