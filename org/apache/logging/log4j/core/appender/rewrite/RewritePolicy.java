package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.core.LogEvent;

public abstract interface RewritePolicy
{
  public abstract LogEvent rewrite(LogEvent paramLogEvent);
}
