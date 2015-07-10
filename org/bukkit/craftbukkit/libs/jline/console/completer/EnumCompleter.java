package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.util.Collection;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class EnumCompleter
  extends StringsCompleter
{
  public EnumCompleter(Class<? extends Enum> source)
  {
    Preconditions.checkNotNull(source);
    for (Enum<?> n : (Enum[])source.getEnumConstants()) {
      getStrings().add(n.name().toLowerCase());
    }
  }
}
