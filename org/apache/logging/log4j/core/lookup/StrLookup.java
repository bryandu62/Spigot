package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public abstract interface StrLookup
{
  public abstract String lookup(String paramString);
  
  public abstract String lookup(LogEvent paramLogEvent, String paramString);
}
