package org.bukkit.craftbukkit.libs.jline.console.history;

import java.io.IOException;

public abstract interface PersistentHistory
  extends History
{
  public abstract void flush()
    throws IOException;
  
  public abstract void purge()
    throws IOException;
}
