package org.bukkit.craftbukkit.libs.jline.internal;

import java.io.PrintStream;

public final class Log
{
  public static enum Level
  {
    TRACE,  DEBUG,  INFO,  WARN,  ERROR;
    
    private Level() {}
  }
  
  public static final boolean TRACE = Boolean.getBoolean(Log.class.getName() + ".trace");
  public static final boolean DEBUG = (TRACE) || (Boolean.getBoolean(Log.class.getName() + ".debug"));
  private static PrintStream output = System.err;
  
  public static PrintStream getOutput()
  {
    return output;
  }
  
  public static void setOutput(PrintStream out)
  {
    output = (PrintStream)Preconditions.checkNotNull(out);
  }
  
  @TestAccessible
  static void render(PrintStream out, Object message)
  {
    if (message.getClass().isArray())
    {
      Object[] array = (Object[])message;
      
      out.print("[");
      for (int i = 0; i < array.length; i++)
      {
        out.print(array[i]);
        if (i + 1 < array.length) {
          out.print(",");
        }
      }
      out.print("]");
    }
    else
    {
      out.print(message);
    }
  }
  
  @TestAccessible
  static void log(Level level, Object... messages)
  {
    synchronized (output)
    {
      output.format("[%s] ", new Object[] { level });
      for (int i = 0; i < messages.length; i++) {
        if ((i + 1 == messages.length) && ((messages[i] instanceof Throwable)))
        {
          output.println();
          ((Throwable)messages[i]).printStackTrace(output);
        }
        else
        {
          render(output, messages[i]);
        }
      }
      output.println();
      output.flush();
    }
  }
  
  public static void trace(Object... messages)
  {
    if (TRACE) {
      log(Level.TRACE, messages);
    }
  }
  
  public static void debug(Object... messages)
  {
    if ((TRACE) || (DEBUG)) {
      log(Level.DEBUG, messages);
    }
  }
  
  public static void info(Object... messages)
  {
    log(Level.INFO, messages);
  }
  
  public static void warn(Object... messages)
  {
    log(Level.WARN, messages);
  }
  
  public static void error(Object... messages)
  {
    log(Level.ERROR, messages);
  }
}
