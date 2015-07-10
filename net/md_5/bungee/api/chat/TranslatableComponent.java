package net.md_5.bungee.api.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class TranslatableComponent
  extends BaseComponent
{
  public void setTranslate(String translate)
  {
    this.translate = translate;
  }
  
  public String toString()
  {
    return "TranslatableComponent(locales=" + getLocales() + ", format=" + getFormat() + ", translate=" + getTranslate() + ", with=" + getWith() + ")";
  }
  
  public ResourceBundle getLocales()
  {
    return this.locales;
  }
  
  private final ResourceBundle locales = ResourceBundle.getBundle("mojang-translations/en_US");
  
  public Pattern getFormat()
  {
    return this.format;
  }
  
  private final Pattern format = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
  private String translate;
  private List<BaseComponent> with;
  
  public String getTranslate()
  {
    return this.translate;
  }
  
  public List<BaseComponent> getWith()
  {
    return this.with;
  }
  
  public TranslatableComponent(TranslatableComponent original)
  {
    super(original);
    setTranslate(original.getTranslate());
    for (BaseComponent baseComponent : original.getWith()) {
      this.with.add(baseComponent.duplicate());
    }
  }
  
  public TranslatableComponent(String translate, Object... with)
  {
    setTranslate(translate);
    List<BaseComponent> temp = new ArrayList();
    for (Object w : with) {
      if ((w instanceof String)) {
        temp.add(new TextComponent((String)w));
      } else {
        temp.add((BaseComponent)w);
      }
    }
    setWith(temp);
  }
  
  public BaseComponent duplicate()
  {
    return new TranslatableComponent(this);
  }
  
  public void setWith(List<BaseComponent> components)
  {
    for (BaseComponent component : components) {
      component.parent = this;
    }
    this.with = components;
  }
  
  public void addWith(String text)
  {
    addWith(new TextComponent(text));
  }
  
  public void addWith(BaseComponent component)
  {
    if (this.with == null) {
      this.with = new ArrayList();
    }
    component.parent = this;
    this.with.add(component);
  }
  
  protected void toPlainText(StringBuilder builder)
  {
    try
    {
      String trans = this.locales.getString(this.translate);
      Matcher matcher = this.format.matcher(trans);
      int position = 0;
      int i = 0;
      while (matcher.find(position))
      {
        int pos = matcher.start();
        if (pos != position) {
          builder.append(trans.substring(position, pos));
        }
        position = matcher.end();
        
        String formatCode = matcher.group(2);
        switch (formatCode.charAt(0))
        {
        case 'd': 
        case 's': 
          String withIndex = matcher.group(1);
          ((BaseComponent)this.with.get(withIndex != null ? Integer.parseInt(withIndex) - 1 : i++)).toPlainText(builder);
          break;
        case '%': 
          builder.append('%');
        }
      }
      if (trans.length() != position) {
        builder.append(trans.substring(position, trans.length()));
      }
    }
    catch (MissingResourceException e)
    {
      builder.append(this.translate);
    }
    super.toPlainText(builder);
  }
  
  protected void toLegacyText(StringBuilder builder)
  {
    try
    {
      String trans = this.locales.getString(this.translate);
      Matcher matcher = this.format.matcher(trans);
      int position = 0;
      int i = 0;
      while (matcher.find(position))
      {
        int pos = matcher.start();
        if (pos != position)
        {
          addFormat(builder);
          builder.append(trans.substring(position, pos));
        }
        position = matcher.end();
        
        String formatCode = matcher.group(2);
        switch (formatCode.charAt(0))
        {
        case 'd': 
        case 's': 
          String withIndex = matcher.group(1);
          ((BaseComponent)this.with.get(withIndex != null ? Integer.parseInt(withIndex) - 1 : i++)).toLegacyText(builder);
          break;
        case '%': 
          addFormat(builder);
          builder.append('%');
        }
      }
      if (trans.length() != position)
      {
        addFormat(builder);
        builder.append(trans.substring(position, trans.length()));
      }
    }
    catch (MissingResourceException e)
    {
      addFormat(builder);
      builder.append(this.translate);
    }
    super.toLegacyText(builder);
  }
  
  private void addFormat(StringBuilder builder)
  {
    builder.append(getColor());
    if (isBold()) {
      builder.append(ChatColor.BOLD);
    }
    if (isItalic()) {
      builder.append(ChatColor.ITALIC);
    }
    if (isUnderlined()) {
      builder.append(ChatColor.UNDERLINE);
    }
    if (isStrikethrough()) {
      builder.append(ChatColor.STRIKETHROUGH);
    }
    if (isObfuscated()) {
      builder.append(ChatColor.MAGIC);
    }
  }
  
  public TranslatableComponent() {}
}
