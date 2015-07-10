package org.apache.logging.log4j.core.lookup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="date", category="Lookup")
public class DateLookup
  implements StrLookup
{
  private static final Logger LOGGER = ;
  
  public String lookup(String key)
  {
    return formatDate(System.currentTimeMillis(), key);
  }
  
  public String lookup(LogEvent event, String key)
  {
    return formatDate(event.getMillis(), key);
  }
  
  private String formatDate(long date, String format)
  {
    DateFormat dateFormat = null;
    if (format != null) {
      try
      {
        dateFormat = new SimpleDateFormat(format);
      }
      catch (Exception ex)
      {
        LOGGER.error("Invalid date format: \"" + format + "\", using default", ex);
      }
    }
    if (dateFormat == null) {
      dateFormat = DateFormat.getInstance();
    }
    return dateFormat.format(new Date(date));
  }
}
