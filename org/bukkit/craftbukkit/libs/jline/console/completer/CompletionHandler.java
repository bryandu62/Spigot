package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.io.IOException;
import java.util.List;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;

public abstract interface CompletionHandler
{
  public abstract boolean complete(ConsoleReader paramConsoleReader, List<CharSequence> paramList, int paramInt)
    throws IOException;
}
