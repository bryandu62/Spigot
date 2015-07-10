package org.bukkit.craftbukkit.libs.joptsimple.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.craftbukkit.libs.joptsimple.ValueConversionException;
import org.bukkit.craftbukkit.libs.joptsimple.ValueConverter;

public class DateConverter
  implements ValueConverter<Date>
{
  private final DateFormat formatter;
  
  public DateConverter(DateFormat formatter)
  {
    if (formatter == null) {
      throw new NullPointerException("illegal null formatter");
    }
    this.formatter = formatter;
  }
  
  public static DateConverter datePattern(String pattern)
  {
    SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    formatter.setLenient(false);
    
    return new DateConverter(formatter);
  }
  
  public Date convert(String value)
  {
    ParsePosition position = new ParsePosition(0);
    
    Date date = this.formatter.parse(value, position);
    if (position.getIndex() != value.length()) {
      throw new ValueConversionException(message(value));
    }
    return date;
  }
  
  public Class<Date> valueType()
  {
    return Date.class;
  }
  
  public String valuePattern()
  {
    return (this.formatter instanceof SimpleDateFormat) ? ((SimpleDateFormat)this.formatter).toLocalizedPattern() : "";
  }
  
  private String message(String value)
  {
    String message = "Value [" + value + "] does not match date/time pattern";
    if ((this.formatter instanceof SimpleDateFormat)) {
      message = message + " [" + ((SimpleDateFormat)this.formatter).toLocalizedPattern() + ']';
    }
    return message;
  }
}
