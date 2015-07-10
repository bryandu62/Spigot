package org.bukkit.craftbukkit.libs.jline;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class TerminalFactory
{
  public static final String JLINE_TERMINAL = "org.bukkit.craftbukkit.libs.jline.terminal";
  public static final String AUTO = "auto";
  public static final String UNIX = "unix";
  public static final String WIN = "win";
  public static final String WINDOWS = "windows";
  public static final String NONE = "none";
  public static final String OFF = "off";
  public static final String FALSE = "false";
  private static Terminal term = null;
  
  public static synchronized Terminal create()
  {
    if (Log.TRACE) {
      Log.trace(new Object[] { new Throwable("CREATE MARKER") });
    }
    String type = Configuration.getString("org.bukkit.craftbukkit.libs.jline.terminal", "auto");
    if ("dumb".equals(System.getenv("TERM")))
    {
      type = "none";
      Log.debug(new Object[] { "$TERM=dumb; setting type=", type });
    }
    Log.debug(new Object[] { "Creating terminal; type=", type });
    Terminal t;
    try
    {
      String tmp = type.toLowerCase();
      Terminal t;
      if (tmp.equals("unix"))
      {
        t = getFlavor(Flavor.UNIX);
      }
      else
      {
        Terminal t;
        if ((tmp.equals("win") | tmp.equals("windows")))
        {
          t = getFlavor(Flavor.WINDOWS);
        }
        else
        {
          Terminal t;
          if ((tmp.equals("none")) || (tmp.equals("off")) || (tmp.equals("false")))
          {
            t = new UnsupportedTerminal();
          }
          else
          {
            Terminal t;
            if (tmp.equals("auto"))
            {
              String os = Configuration.getOsName();
              Flavor flavor = Flavor.UNIX;
              if (os.contains("windows")) {
                flavor = Flavor.WINDOWS;
              }
              t = getFlavor(flavor);
            }
            else
            {
              try
              {
                t = (Terminal)Thread.currentThread().getContextClassLoader().loadClass(type).newInstance();
              }
              catch (Exception e)
              {
                throw new IllegalArgumentException(MessageFormat.format("Invalid terminal type: {0}", new Object[] { type }), e);
              }
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      Log.error(new Object[] { "Failed to construct terminal; falling back to unsupported", e });
      t = new UnsupportedTerminal();
    }
    Log.debug(new Object[] { "Created Terminal: ", t });
    try
    {
      t.init();
    }
    catch (Throwable e)
    {
      Log.error(new Object[] { "Terminal initialization failed; falling back to unsupported", e });
      return new UnsupportedTerminal();
    }
    return t;
  }
  
  public static synchronized void reset()
  {
    term = null;
  }
  
  public static synchronized void resetIf(Terminal t)
  {
    if (t == term) {
      reset();
    }
  }
  
  public static enum Type
  {
    AUTO,  WINDOWS,  UNIX,  NONE;
    
    private Type() {}
  }
  
  public static synchronized void configure(String type)
  {
    Preconditions.checkNotNull(type);
    System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", type);
  }
  
  public static synchronized void configure(Type type)
  {
    Preconditions.checkNotNull(type);
    configure(type.name().toLowerCase());
  }
  
  public static enum Flavor
  {
    WINDOWS,  UNIX;
    
    private Flavor() {}
  }
  
  private static final Map<Flavor, Class<? extends Terminal>> FLAVORS = new HashMap();
  
  static
  {
    registerFlavor(Flavor.WINDOWS, AnsiWindowsTerminal.class);
    registerFlavor(Flavor.UNIX, UnixTerminal.class);
  }
  
  public static synchronized Terminal get()
  {
    if (term == null) {
      term = create();
    }
    return term;
  }
  
  public static Terminal getFlavor(Flavor flavor)
    throws Exception
  {
    Class<? extends Terminal> type = (Class)FLAVORS.get(flavor);
    if (type != null) {
      return (Terminal)type.newInstance();
    }
    throw new InternalError();
  }
  
  public static void registerFlavor(Flavor flavor, Class<? extends Terminal> type)
  {
    FLAVORS.put(flavor, type);
  }
}
