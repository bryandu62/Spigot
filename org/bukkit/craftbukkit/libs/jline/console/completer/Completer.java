package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.util.List;

public abstract interface Completer
{
  public abstract int complete(String paramString, int paramInt, List<CharSequence> paramList);
}
