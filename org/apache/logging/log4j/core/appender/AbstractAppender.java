package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.Integers;

public abstract class AbstractAppender
  extends AbstractFilterable
  implements Appender
{
  private final boolean ignoreExceptions;
  private ErrorHandler handler = new DefaultErrorHandler(this);
  private final Layout<? extends Serializable> layout;
  private final String name;
  private boolean started = false;
  
  public static int parseInt(String s, int defaultValue)
  {
    try
    {
      return Integers.parseInt(s, defaultValue);
    }
    catch (NumberFormatException e)
    {
      LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", new Object[] { s, Integer.valueOf(defaultValue), e });
    }
    return defaultValue;
  }
  
  protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout)
  {
    this(name, filter, layout, true);
  }
  
  protected AbstractAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions)
  {
    super(filter);
    this.name = name;
    this.layout = layout;
    this.ignoreExceptions = ignoreExceptions;
  }
  
  public void error(String msg)
  {
    this.handler.error(msg);
  }
  
  public void error(String msg, LogEvent event, Throwable t)
  {
    this.handler.error(msg, event, t);
  }
  
  public void error(String msg, Throwable t)
  {
    this.handler.error(msg, t);
  }
  
  public ErrorHandler getHandler()
  {
    return this.handler;
  }
  
  public Layout<? extends Serializable> getLayout()
  {
    return this.layout;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public boolean ignoreExceptions()
  {
    return this.ignoreExceptions;
  }
  
  public boolean isStarted()
  {
    return this.started;
  }
  
  public void setHandler(ErrorHandler handler)
  {
    if (handler == null) {
      LOGGER.error("The handler cannot be set to null");
    }
    if (isStarted())
    {
      LOGGER.error("The handler cannot be changed once the appender is started");
      return;
    }
    this.handler = handler;
  }
  
  public void start()
  {
    startFilter();
    this.started = true;
  }
  
  public void stop()
  {
    this.started = false;
    stopFilter();
  }
  
  public String toString()
  {
    return this.name;
  }
}
