package net.minecraft.server.v1_8_R3;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class LocaleLanguage
{
  private static final Pattern a = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
  private static final Splitter b = Splitter.on('=').limit(2);
  private static LocaleLanguage c = new LocaleLanguage();
  private final Map<String, String> d = Maps.newHashMap();
  private long e;
  
  public LocaleLanguage()
  {
    try
    {
      InputStream ☃ = LocaleLanguage.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
      for (String ☃ : IOUtils.readLines(☃, Charsets.UTF_8)) {
        if ((!☃.isEmpty()) && (☃.charAt(0) != '#'))
        {
          String[] ☃ = (String[])Iterables.toArray(b.split(☃), String.class);
          if ((☃ != null) && (☃.length == 2))
          {
            String ☃ = ☃[0];
            String ☃ = a.matcher(☃[1]).replaceAll("%$1s");
            
            this.d.put(☃, ☃);
          }
        }
      }
      this.e = System.currentTimeMillis();
    }
    catch (IOException localIOException) {}
  }
  
  static LocaleLanguage a()
  {
    return c;
  }
  
  public synchronized String a(String ☃)
  {
    return c(☃);
  }
  
  public synchronized String a(String ☃, Object... ☃)
  {
    String ☃ = c(☃);
    try
    {
      return String.format(☃, ☃);
    }
    catch (IllegalFormatException ☃) {}
    return "Format error: " + ☃;
  }
  
  private String c(String ☃)
  {
    String ☃ = (String)this.d.get(☃);
    return ☃ == null ? ☃ : ☃;
  }
  
  public synchronized boolean b(String ☃)
  {
    return this.d.containsKey(☃);
  }
  
  public long c()
  {
    return this.e;
  }
}
