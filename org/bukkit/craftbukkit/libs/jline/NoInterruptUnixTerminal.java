package org.bukkit.craftbukkit.libs.jline;

import org.bukkit.craftbukkit.libs.jline.internal.TerminalLineSettings;

public class NoInterruptUnixTerminal
  extends UnixTerminal
{
  public NoInterruptUnixTerminal()
    throws Exception
  {}
  
  public void init()
    throws Exception
  {
    super.init();
    getSettings().set("intr undef");
  }
  
  public void restore()
    throws Exception
  {
    getSettings().set("intr ^C");
    super.restore();
  }
}
