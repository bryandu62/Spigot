package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessage
  extends ChatBaseComponent
{
  private final String d;
  private final Object[] e;
  private final Object f = new Object();
  private long g = -1L;
  List<IChatBaseComponent> b = Lists.newArrayList();
  public static final Pattern c = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
  
  public ChatMessage(String ☃, Object... ☃)
  {
    this.d = ☃;
    this.e = ☃;
    for (Object ☃ : ☃) {
      if ((☃ instanceof IChatBaseComponent)) {
        ((IChatBaseComponent)☃).getChatModifier().setChatModifier(getChatModifier());
      }
    }
  }
  
  synchronized void g()
  {
    synchronized (this.f)
    {
      long ☃ = LocaleI18n.a();
      if (☃ == this.g) {
        return;
      }
      this.g = ☃;
      this.b.clear();
    }
    try
    {
      b(LocaleI18n.get(this.d));
    }
    catch (ChatMessageException ☃)
    {
      this.b.clear();
      try
      {
        b(LocaleI18n.b(this.d));
      }
      catch (ChatMessageException ☃)
      {
        throw ☃;
      }
    }
  }
  
  protected void b(String ☃)
  {
    boolean ☃ = false;
    Matcher ☃ = c.matcher(☃);
    
    int ☃ = 0;
    int ☃ = 0;
    try
    {
      while (☃.find(☃))
      {
        int ☃ = ☃.start();
        int ☃ = ☃.end();
        if (☃ > ☃)
        {
          ChatComponentText ☃ = new ChatComponentText(String.format(☃.substring(☃, ☃), new Object[0]));
          ☃.getChatModifier().setChatModifier(getChatModifier());
          this.b.add(☃);
        }
        String ☃ = ☃.group(2);
        String ☃ = ☃.substring(☃, ☃);
        if (("%".equals(☃)) && ("%%".equals(☃)))
        {
          ChatComponentText ☃ = new ChatComponentText("%");
          ☃.getChatModifier().setChatModifier(getChatModifier());
          this.b.add(☃);
        }
        else if ("s".equals(☃))
        {
          String ☃ = ☃.group(1);
          int ☃ = ☃ != null ? Integer.parseInt(☃) - 1 : ☃++;
          if (☃ < this.e.length) {
            this.b.add(a(☃));
          }
        }
        else
        {
          throw new ChatMessageException(this, "Unsupported format: '" + ☃ + "'");
        }
        ☃ = ☃;
      }
      if (☃ < ☃.length())
      {
        ChatComponentText ☃ = new ChatComponentText(String.format(☃.substring(☃), new Object[0]));
        ☃.getChatModifier().setChatModifier(getChatModifier());
        this.b.add(☃);
      }
    }
    catch (IllegalFormatException ☃)
    {
      throw new ChatMessageException(this, ☃);
    }
  }
  
  private IChatBaseComponent a(int ☃)
  {
    if (☃ >= this.e.length) {
      throw new ChatMessageException(this, ☃);
    }
    Object ☃ = this.e[☃];
    IChatBaseComponent ☃;
    IChatBaseComponent ☃;
    if ((☃ instanceof IChatBaseComponent))
    {
      ☃ = (IChatBaseComponent)☃;
    }
    else
    {
      ☃ = new ChatComponentText(☃ == null ? "null" : ☃.toString());
      ☃.getChatModifier().setChatModifier(getChatModifier());
    }
    return ☃;
  }
  
  public IChatBaseComponent setChatModifier(ChatModifier ☃)
  {
    super.setChatModifier(☃);
    for (Object ☃ : this.e) {
      if ((☃ instanceof IChatBaseComponent)) {
        ((IChatBaseComponent)☃).getChatModifier().setChatModifier(getChatModifier());
      }
    }
    if (this.g > -1L) {
      for (IChatBaseComponent ☃ : this.b) {
        ☃.getChatModifier().setChatModifier(☃);
      }
    }
    return this;
  }
  
  public Iterator<IChatBaseComponent> iterator()
  {
    g();
    
    return Iterators.concat(a(this.b), a(this.a));
  }
  
  public String getText()
  {
    g();
    
    StringBuilder ☃ = new StringBuilder();
    for (IChatBaseComponent ☃ : this.b) {
      ☃.append(☃.getText());
    }
    return ☃.toString();
  }
  
  public ChatMessage h()
  {
    Object[] ☃ = new Object[this.e.length];
    for (int ☃ = 0; ☃ < this.e.length; ☃++) {
      if ((this.e[☃] instanceof IChatBaseComponent)) {
        ☃[☃] = ((IChatBaseComponent)this.e[☃]).f();
      } else {
        ☃[☃] = this.e[☃];
      }
    }
    ChatMessage ☃ = new ChatMessage(this.d, ☃);
    ☃.setChatModifier(getChatModifier().clone());
    for (IChatBaseComponent ☃ : a()) {
      ☃.addSibling(☃.f());
    }
    return ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ instanceof ChatMessage))
    {
      ChatMessage ☃ = (ChatMessage)☃;
      return (Arrays.equals(this.e, ☃.e)) && (this.d.equals(☃.d)) && (super.equals(☃));
    }
    return false;
  }
  
  public int hashCode()
  {
    int ☃ = super.hashCode();
    ☃ = 31 * ☃ + this.d.hashCode();
    ☃ = 31 * ☃ + Arrays.hashCode(this.e);
    return ☃;
  }
  
  public String toString()
  {
    return "TranslatableComponent{key='" + this.d + '\'' + ", args=" + Arrays.toString(this.e) + ", siblings=" + this.a + ", style=" + getChatModifier() + '}';
  }
  
  public String i()
  {
    return this.d;
  }
  
  public Object[] j()
  {
    return this.e;
  }
}
