package org.bukkit.craftbukkit.libs.jline.console.internal;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.jline.console.completer.ArgumentCompleter;
import org.bukkit.craftbukkit.libs.jline.console.completer.Completer;
import org.bukkit.craftbukkit.libs.jline.console.history.FileHistory;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;

public class ConsoleRunner
{
  public static final String property = "org.bukkit.craftbukkit.libs.jline.history";
  
  public static void main(String[] args)
    throws Exception
  {
    List<String> argList = new ArrayList(Arrays.asList(args));
    if (argList.size() == 0)
    {
      usage();
      return;
    }
    String historyFileName = System.getProperty("org.bukkit.craftbukkit.libs.jline.history", null);
    
    String mainClass = (String)argList.remove(0);
    ConsoleReader reader = new ConsoleReader();
    if (historyFileName != null) {
      reader.setHistory(new FileHistory(new File(Configuration.getUserHome(), String.format(".org.bukkit.craftbukkit.libs.jline-%s.%s.history", new Object[] { mainClass, historyFileName }))));
    } else {
      reader.setHistory(new FileHistory(new File(Configuration.getUserHome(), String.format(".org.bukkit.craftbukkit.libs.jline-%s.history", new Object[] { mainClass }))));
    }
    String completors = System.getProperty(ConsoleRunner.class.getName() + ".completers", "");
    List<Completer> completorList = new ArrayList();
    for (StringTokenizer tok = new StringTokenizer(completors, ","); tok.hasMoreTokens();)
    {
      Object obj = Class.forName(tok.nextToken()).newInstance();
      completorList.add((Completer)obj);
    }
    if (completorList.size() > 0) {
      reader.addCompleter(new ArgumentCompleter(completorList));
    }
    ConsoleReaderInputStream.setIn(reader);
    try
    {
      Class type = Class.forName(mainClass);
      Method method = type.getMethod("main", new Class[] { String[].class });
      method.invoke(null, new Object[0]);
    }
    finally
    {
      ConsoleReaderInputStream.restoreIn();
    }
  }
  
  private static void usage()
  {
    System.out.println("Usage: \n   java [-Djline.history='name'] " + ConsoleRunner.class.getName() + " <target class name> [args]" + "\n\nThe -Djline.history option will avoid history" + "\nmangling when running ConsoleRunner on the same application." + "\n\nargs will be passed directly to the target class name.");
  }
}
