package io.netty.util.internal.logging;

import io.netty.util.internal.StringUtil;
import java.io.ObjectStreamException;
import java.io.Serializable;

public abstract class AbstractInternalLogger
  implements InternalLogger, Serializable
{
  private static final long serialVersionUID = -6382972526573193470L;
  private final String name;
  
  protected AbstractInternalLogger(String name)
  {
    if (name == null) {
      throw new NullPointerException("name");
    }
    this.name = name;
  }
  
  public String name()
  {
    return this.name;
  }
  
  public boolean isEnabled(InternalLogLevel level)
  {
    switch (level)
    {
    case TRACE: 
      return isTraceEnabled();
    case DEBUG: 
      return isDebugEnabled();
    case INFO: 
      return isInfoEnabled();
    case WARN: 
      return isWarnEnabled();
    case ERROR: 
      return isErrorEnabled();
    }
    throw new Error();
  }
  
  public void log(InternalLogLevel level, String msg, Throwable cause)
  {
    switch (level)
    {
    case TRACE: 
      trace(msg, cause);
      break;
    case DEBUG: 
      debug(msg, cause);
      break;
    case INFO: 
      info(msg, cause);
      break;
    case WARN: 
      warn(msg, cause);
      break;
    case ERROR: 
      error(msg, cause);
      break;
    default: 
      throw new Error();
    }
  }
  
  public void log(InternalLogLevel level, String msg)
  {
    switch (level)
    {
    case TRACE: 
      trace(msg);
      break;
    case DEBUG: 
      debug(msg);
      break;
    case INFO: 
      info(msg);
      break;
    case WARN: 
      warn(msg);
      break;
    case ERROR: 
      error(msg);
      break;
    default: 
      throw new Error();
    }
  }
  
  public void log(InternalLogLevel level, String format, Object arg)
  {
    switch (level)
    {
    case TRACE: 
      trace(format, arg);
      break;
    case DEBUG: 
      debug(format, arg);
      break;
    case INFO: 
      info(format, arg);
      break;
    case WARN: 
      warn(format, arg);
      break;
    case ERROR: 
      error(format, arg);
      break;
    default: 
      throw new Error();
    }
  }
  
  public void log(InternalLogLevel level, String format, Object argA, Object argB)
  {
    switch (level)
    {
    case TRACE: 
      trace(format, argA, argB);
      break;
    case DEBUG: 
      debug(format, argA, argB);
      break;
    case INFO: 
      info(format, argA, argB);
      break;
    case WARN: 
      warn(format, argA, argB);
      break;
    case ERROR: 
      error(format, argA, argB);
      break;
    default: 
      throw new Error();
    }
  }
  
  public void log(InternalLogLevel level, String format, Object... arguments)
  {
    switch (level)
    {
    case TRACE: 
      trace(format, arguments);
      break;
    case DEBUG: 
      debug(format, arguments);
      break;
    case INFO: 
      info(format, arguments);
      break;
    case WARN: 
      warn(format, arguments);
      break;
    case ERROR: 
      error(format, arguments);
      break;
    default: 
      throw new Error();
    }
  }
  
  protected Object readResolve()
    throws ObjectStreamException
  {
    return InternalLoggerFactory.getInstance(name());
  }
  
  public String toString()
  {
    return StringUtil.simpleClassName(this) + '(' + name() + ')';
  }
}
