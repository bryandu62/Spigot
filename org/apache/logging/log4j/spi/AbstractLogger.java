package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractLogger
  implements Logger
{
  public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
  public static final Marker ENTRY_MARKER = MarkerManager.getMarker("ENTRY", FLOW_MARKER);
  public static final Marker EXIT_MARKER = MarkerManager.getMarker("EXIT", FLOW_MARKER);
  public static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
  public static final Marker THROWING_MARKER = MarkerManager.getMarker("THROWING", EXCEPTION_MARKER);
  public static final Marker CATCHING_MARKER = MarkerManager.getMarker("CATCHING", EXCEPTION_MARKER);
  public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS = ParameterizedMessageFactory.class;
  private static final String FQCN = AbstractLogger.class.getName();
  private static final String THROWING = "throwing";
  private static final String CATCHING = "catching";
  private final String name;
  private final MessageFactory messageFactory;
  
  public AbstractLogger()
  {
    this.name = getClass().getName();
    this.messageFactory = createDefaultMessageFactory();
  }
  
  public AbstractLogger(String name)
  {
    this.name = name;
    this.messageFactory = createDefaultMessageFactory();
  }
  
  public AbstractLogger(String name, MessageFactory messageFactory)
  {
    this.name = name;
    this.messageFactory = (messageFactory == null ? createDefaultMessageFactory() : messageFactory);
  }
  
  public static void checkMessageFactory(Logger logger, MessageFactory messageFactory)
  {
    String name = logger.getName();
    MessageFactory loggerMessageFactory = logger.getMessageFactory();
    if ((messageFactory != null) && (!loggerMessageFactory.equals(messageFactory))) {
      StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", new Object[] { name, loggerMessageFactory, messageFactory });
    } else if ((messageFactory == null) && (!loggerMessageFactory.getClass().equals(DEFAULT_MESSAGE_FACTORY_CLASS))) {
      StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", new Object[] { name, loggerMessageFactory, DEFAULT_MESSAGE_FACTORY_CLASS.getName() });
    }
  }
  
  public void catching(Level level, Throwable t)
  {
    catching(FQCN, level, t);
  }
  
  public void catching(Throwable t)
  {
    catching(FQCN, Level.ERROR, t);
  }
  
  protected void catching(String fqcn, Level level, Throwable t)
  {
    if (isEnabled(level, CATCHING_MARKER, (Object)null, null)) {
      log(CATCHING_MARKER, fqcn, level, this.messageFactory.newMessage("catching"), t);
    }
  }
  
  private MessageFactory createDefaultMessageFactory()
  {
    try
    {
      return (MessageFactory)DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
    }
    catch (InstantiationException e)
    {
      throw new IllegalStateException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new IllegalStateException(e);
    }
  }
  
  public void debug(Marker marker, Message msg)
  {
    if (isEnabled(Level.DEBUG, marker, msg, null)) {
      log(marker, FQCN, Level.DEBUG, msg, null);
    }
  }
  
  public void debug(Marker marker, Message msg, Throwable t)
  {
    if (isEnabled(Level.DEBUG, marker, msg, t)) {
      log(marker, FQCN, Level.DEBUG, msg, t);
    }
  }
  
  public void debug(Marker marker, Object message)
  {
    if (isEnabled(Level.DEBUG, marker, message, null)) {
      log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void debug(Marker marker, Object message, Throwable t)
  {
    if (isEnabled(Level.DEBUG, marker, message, t)) {
      log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void debug(Marker marker, String message)
  {
    if (isEnabled(Level.DEBUG, marker, message)) {
      log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void debug(Marker marker, String message, Object... params)
  {
    if (isEnabled(Level.DEBUG, marker, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(marker, FQCN, Level.DEBUG, msg, msg.getThrowable());
    }
  }
  
  public void debug(Marker marker, String message, Throwable t)
  {
    if (isEnabled(Level.DEBUG, marker, message, t)) {
      log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void debug(Message msg)
  {
    if (isEnabled(Level.DEBUG, null, msg, null)) {
      log(null, FQCN, Level.DEBUG, msg, null);
    }
  }
  
  public void debug(Message msg, Throwable t)
  {
    if (isEnabled(Level.DEBUG, null, msg, t)) {
      log(null, FQCN, Level.DEBUG, msg, t);
    }
  }
  
  public void debug(Object message)
  {
    if (isEnabled(Level.DEBUG, null, message, null)) {
      log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void debug(Object message, Throwable t)
  {
    if (isEnabled(Level.DEBUG, null, message, t)) {
      log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void debug(String message)
  {
    if (isEnabled(Level.DEBUG, null, message)) {
      log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void debug(String message, Object... params)
  {
    if (isEnabled(Level.DEBUG, null, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(null, FQCN, Level.DEBUG, msg, msg.getThrowable());
    }
  }
  
  public void debug(String message, Throwable t)
  {
    if (isEnabled(Level.DEBUG, null, message, t)) {
      log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void entry()
  {
    entry(FQCN, new Object[0]);
  }
  
  public void entry(Object... params)
  {
    entry(FQCN, params);
  }
  
  protected void entry(String fqcn, Object... params)
  {
    if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, null)) {
      log(ENTRY_MARKER, fqcn, Level.TRACE, entryMsg(params.length, params), null);
    }
  }
  
  private Message entryMsg(int count, Object... params)
  {
    if (count == 0) {
      return this.messageFactory.newMessage("entry");
    }
    StringBuilder sb = new StringBuilder("entry params(");
    int i = 0;
    for (Object parm : params)
    {
      if (parm != null) {
        sb.append(parm.toString());
      } else {
        sb.append("null");
      }
      i++;
      if (i < params.length) {
        sb.append(", ");
      }
    }
    sb.append(")");
    return this.messageFactory.newMessage(sb.toString());
  }
  
  public void error(Marker marker, Message msg)
  {
    if (isEnabled(Level.ERROR, marker, msg, null)) {
      log(marker, FQCN, Level.ERROR, msg, null);
    }
  }
  
  public void error(Marker marker, Message msg, Throwable t)
  {
    if (isEnabled(Level.ERROR, marker, msg, t)) {
      log(marker, FQCN, Level.ERROR, msg, t);
    }
  }
  
  public void error(Marker marker, Object message)
  {
    if (isEnabled(Level.ERROR, marker, message, null)) {
      log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void error(Marker marker, Object message, Throwable t)
  {
    if (isEnabled(Level.ERROR, marker, message, t)) {
      log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void error(Marker marker, String message)
  {
    if (isEnabled(Level.ERROR, marker, message)) {
      log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void error(Marker marker, String message, Object... params)
  {
    if (isEnabled(Level.ERROR, marker, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(marker, FQCN, Level.ERROR, msg, msg.getThrowable());
    }
  }
  
  public void error(Marker marker, String message, Throwable t)
  {
    if (isEnabled(Level.ERROR, marker, message, t)) {
      log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void error(Message msg)
  {
    if (isEnabled(Level.ERROR, null, msg, null)) {
      log(null, FQCN, Level.ERROR, msg, null);
    }
  }
  
  public void error(Message msg, Throwable t)
  {
    if (isEnabled(Level.ERROR, null, msg, t)) {
      log(null, FQCN, Level.ERROR, msg, t);
    }
  }
  
  public void error(Object message)
  {
    if (isEnabled(Level.ERROR, null, message, null)) {
      log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void error(Object message, Throwable t)
  {
    if (isEnabled(Level.ERROR, null, message, t)) {
      log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void error(String message)
  {
    if (isEnabled(Level.ERROR, null, message)) {
      log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void error(String message, Object... params)
  {
    if (isEnabled(Level.ERROR, null, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(null, FQCN, Level.ERROR, msg, msg.getThrowable());
    }
  }
  
  public void error(String message, Throwable t)
  {
    if (isEnabled(Level.ERROR, null, message, t)) {
      log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void exit()
  {
    exit(FQCN, null);
  }
  
  public <R> R exit(R result)
  {
    return (R)exit(FQCN, result);
  }
  
  protected <R> R exit(String fqcn, R result)
  {
    if (isEnabled(Level.TRACE, EXIT_MARKER, (Object)null, null)) {
      log(EXIT_MARKER, fqcn, Level.TRACE, toExitMsg(result), null);
    }
    return result;
  }
  
  public void fatal(Marker marker, Message msg)
  {
    if (isEnabled(Level.FATAL, marker, msg, null)) {
      log(marker, FQCN, Level.FATAL, msg, null);
    }
  }
  
  public void fatal(Marker marker, Message msg, Throwable t)
  {
    if (isEnabled(Level.FATAL, marker, msg, t)) {
      log(marker, FQCN, Level.FATAL, msg, t);
    }
  }
  
  public void fatal(Marker marker, Object message)
  {
    if (isEnabled(Level.FATAL, marker, message, null)) {
      log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void fatal(Marker marker, Object message, Throwable t)
  {
    if (isEnabled(Level.FATAL, marker, message, t)) {
      log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void fatal(Marker marker, String message)
  {
    if (isEnabled(Level.FATAL, marker, message)) {
      log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void fatal(Marker marker, String message, Object... params)
  {
    if (isEnabled(Level.FATAL, marker, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(marker, FQCN, Level.FATAL, msg, msg.getThrowable());
    }
  }
  
  public void fatal(Marker marker, String message, Throwable t)
  {
    if (isEnabled(Level.FATAL, marker, message, t)) {
      log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void fatal(Message msg)
  {
    if (isEnabled(Level.FATAL, null, msg, null)) {
      log(null, FQCN, Level.FATAL, msg, null);
    }
  }
  
  public void fatal(Message msg, Throwable t)
  {
    if (isEnabled(Level.FATAL, null, msg, t)) {
      log(null, FQCN, Level.FATAL, msg, t);
    }
  }
  
  public void fatal(Object message)
  {
    if (isEnabled(Level.FATAL, null, message, null)) {
      log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void fatal(Object message, Throwable t)
  {
    if (isEnabled(Level.FATAL, null, message, t)) {
      log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void fatal(String message)
  {
    if (isEnabled(Level.FATAL, null, message)) {
      log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void fatal(String message, Object... params)
  {
    if (isEnabled(Level.FATAL, null, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(null, FQCN, Level.FATAL, msg, msg.getThrowable());
    }
  }
  
  public void fatal(String message, Throwable t)
  {
    if (isEnabled(Level.FATAL, null, message, t)) {
      log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
    }
  }
  
  public MessageFactory getMessageFactory()
  {
    return this.messageFactory;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void info(Marker marker, Message msg)
  {
    if (isEnabled(Level.INFO, marker, msg, null)) {
      log(marker, FQCN, Level.INFO, msg, null);
    }
  }
  
  public void info(Marker marker, Message msg, Throwable t)
  {
    if (isEnabled(Level.INFO, marker, msg, t)) {
      log(marker, FQCN, Level.INFO, msg, t);
    }
  }
  
  public void info(Marker marker, Object message)
  {
    if (isEnabled(Level.INFO, marker, message, null)) {
      log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void info(Marker marker, Object message, Throwable t)
  {
    if (isEnabled(Level.INFO, marker, message, t)) {
      log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void info(Marker marker, String message)
  {
    if (isEnabled(Level.INFO, marker, message)) {
      log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void info(Marker marker, String message, Object... params)
  {
    if (isEnabled(Level.INFO, marker, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(marker, FQCN, Level.INFO, msg, msg.getThrowable());
    }
  }
  
  public void info(Marker marker, String message, Throwable t)
  {
    if (isEnabled(Level.INFO, marker, message, t)) {
      log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void info(Message msg)
  {
    if (isEnabled(Level.INFO, null, msg, null)) {
      log(null, FQCN, Level.INFO, msg, null);
    }
  }
  
  public void info(Message msg, Throwable t)
  {
    if (isEnabled(Level.INFO, null, msg, t)) {
      log(null, FQCN, Level.INFO, msg, t);
    }
  }
  
  public void info(Object message)
  {
    if (isEnabled(Level.INFO, null, message, null)) {
      log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void info(Object message, Throwable t)
  {
    if (isEnabled(Level.INFO, null, message, t)) {
      log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void info(String message)
  {
    if (isEnabled(Level.INFO, null, message)) {
      log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void info(String message, Object... params)
  {
    if (isEnabled(Level.INFO, null, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(null, FQCN, Level.INFO, msg, msg.getThrowable());
    }
  }
  
  public void info(String message, Throwable t)
  {
    if (isEnabled(Level.INFO, null, message, t)) {
      log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
    }
  }
  
  public boolean isDebugEnabled()
  {
    return isEnabled(Level.DEBUG, null, null);
  }
  
  public boolean isDebugEnabled(Marker marker)
  {
    return isEnabled(Level.DEBUG, marker, (Object)null, null);
  }
  
  public boolean isEnabled(Level level)
  {
    return isEnabled(level, null, (Object)null, null);
  }
  
  protected abstract boolean isEnabled(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  protected abstract boolean isEnabled(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  protected abstract boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString);
  
  protected abstract boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  protected abstract boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public boolean isErrorEnabled()
  {
    return isEnabled(Level.ERROR, null, (Object)null, null);
  }
  
  public boolean isErrorEnabled(Marker marker)
  {
    return isEnabled(Level.ERROR, marker, (Object)null, null);
  }
  
  public boolean isFatalEnabled()
  {
    return isEnabled(Level.FATAL, null, (Object)null, null);
  }
  
  public boolean isFatalEnabled(Marker marker)
  {
    return isEnabled(Level.FATAL, marker, (Object)null, null);
  }
  
  public boolean isInfoEnabled()
  {
    return isEnabled(Level.INFO, null, (Object)null, null);
  }
  
  public boolean isInfoEnabled(Marker marker)
  {
    return isEnabled(Level.INFO, marker, (Object)null, null);
  }
  
  public boolean isTraceEnabled()
  {
    return isEnabled(Level.TRACE, null, (Object)null, null);
  }
  
  public boolean isTraceEnabled(Marker marker)
  {
    return isEnabled(Level.TRACE, marker, (Object)null, null);
  }
  
  public boolean isWarnEnabled()
  {
    return isEnabled(Level.WARN, null, (Object)null, null);
  }
  
  public boolean isWarnEnabled(Marker marker)
  {
    return isEnabled(Level.WARN, marker, (Object)null, null);
  }
  
  public boolean isEnabled(Level level, Marker marker)
  {
    return isEnabled(level, marker, (Object)null, null);
  }
  
  public void log(Level level, Marker marker, Message msg)
  {
    if (isEnabled(level, marker, msg, null)) {
      log(marker, FQCN, level, msg, null);
    }
  }
  
  public void log(Level level, Marker marker, Message msg, Throwable t)
  {
    if (isEnabled(level, marker, msg, t)) {
      log(marker, FQCN, level, msg, t);
    }
  }
  
  public void log(Level level, Marker marker, Object message)
  {
    if (isEnabled(level, marker, message, null)) {
      log(marker, FQCN, level, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void log(Level level, Marker marker, Object message, Throwable t)
  {
    if (isEnabled(level, marker, message, t)) {
      log(marker, FQCN, level, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void log(Level level, Marker marker, String message)
  {
    if (isEnabled(level, marker, message)) {
      log(marker, FQCN, level, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void log(Level level, Marker marker, String message, Object... params)
  {
    if (isEnabled(level, marker, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(marker, FQCN, level, msg, msg.getThrowable());
    }
  }
  
  public void log(Level level, Marker marker, String message, Throwable t)
  {
    if (isEnabled(level, marker, message, t)) {
      log(marker, FQCN, level, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void log(Level level, Message msg)
  {
    if (isEnabled(level, null, msg, null)) {
      log(null, FQCN, level, msg, null);
    }
  }
  
  public void log(Level level, Message msg, Throwable t)
  {
    if (isEnabled(level, null, msg, t)) {
      log(null, FQCN, level, msg, t);
    }
  }
  
  public void log(Level level, Object message)
  {
    if (isEnabled(level, null, message, null)) {
      log(null, FQCN, level, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void log(Level level, Object message, Throwable t)
  {
    if (isEnabled(level, null, message, t)) {
      log(null, FQCN, level, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void log(Level level, String message)
  {
    if (isEnabled(level, null, message)) {
      log(null, FQCN, level, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void log(Level level, String message, Object... params)
  {
    if (isEnabled(level, null, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(null, FQCN, level, msg, msg.getThrowable());
    }
  }
  
  public void log(Level level, String message, Throwable t)
  {
    if (isEnabled(level, null, message, t)) {
      log(null, FQCN, level, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void printf(Level level, String format, Object... params)
  {
    if (isEnabled(level, null, format, params))
    {
      Message msg = new StringFormattedMessage(format, params);
      log(null, FQCN, level, msg, msg.getThrowable());
    }
  }
  
  public void printf(Level level, Marker marker, String format, Object... params)
  {
    if (isEnabled(level, marker, format, params))
    {
      Message msg = new StringFormattedMessage(format, params);
      log(marker, FQCN, level, msg, msg.getThrowable());
    }
  }
  
  public abstract void log(Marker paramMarker, String paramString, Level paramLevel, Message paramMessage, Throwable paramThrowable);
  
  public <T extends Throwable> T throwing(Level level, T t)
  {
    return throwing(FQCN, level, t);
  }
  
  public <T extends Throwable> T throwing(T t)
  {
    return throwing(FQCN, Level.ERROR, t);
  }
  
  protected <T extends Throwable> T throwing(String fqcn, Level level, T t)
  {
    if (isEnabled(level, THROWING_MARKER, (Object)null, null)) {
      log(THROWING_MARKER, fqcn, level, this.messageFactory.newMessage("throwing"), t);
    }
    return t;
  }
  
  private Message toExitMsg(Object result)
  {
    if (result == null) {
      return this.messageFactory.newMessage("exit");
    }
    return this.messageFactory.newMessage("exit with(" + result + ")");
  }
  
  public String toString()
  {
    return this.name;
  }
  
  public void trace(Marker marker, Message msg)
  {
    if (isEnabled(Level.TRACE, marker, msg, null)) {
      log(marker, FQCN, Level.TRACE, msg, null);
    }
  }
  
  public void trace(Marker marker, Message msg, Throwable t)
  {
    if (isEnabled(Level.TRACE, marker, msg, t)) {
      log(marker, FQCN, Level.TRACE, msg, t);
    }
  }
  
  public void trace(Marker marker, Object message)
  {
    if (isEnabled(Level.TRACE, marker, message, null)) {
      log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void trace(Marker marker, Object message, Throwable t)
  {
    if (isEnabled(Level.TRACE, marker, message, t)) {
      log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void trace(Marker marker, String message)
  {
    if (isEnabled(Level.TRACE, marker, message)) {
      log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void trace(Marker marker, String message, Object... params)
  {
    if (isEnabled(Level.TRACE, marker, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(marker, FQCN, Level.TRACE, msg, msg.getThrowable());
    }
  }
  
  public void trace(Marker marker, String message, Throwable t)
  {
    if (isEnabled(Level.TRACE, marker, message, t)) {
      log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void trace(Message msg)
  {
    if (isEnabled(Level.TRACE, null, msg, null)) {
      log(null, FQCN, Level.TRACE, msg, null);
    }
  }
  
  public void trace(Message msg, Throwable t)
  {
    if (isEnabled(Level.TRACE, null, msg, t)) {
      log(null, FQCN, Level.TRACE, msg, t);
    }
  }
  
  public void trace(Object message)
  {
    if (isEnabled(Level.TRACE, null, message, null)) {
      log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void trace(Object message, Throwable t)
  {
    if (isEnabled(Level.TRACE, null, message, t)) {
      log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void trace(String message)
  {
    if (isEnabled(Level.TRACE, null, message)) {
      log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void trace(String message, Object... params)
  {
    if (isEnabled(Level.TRACE, null, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(null, FQCN, Level.TRACE, msg, msg.getThrowable());
    }
  }
  
  public void trace(String message, Throwable t)
  {
    if (isEnabled(Level.TRACE, null, message, t)) {
      log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void warn(Marker marker, Message msg)
  {
    if (isEnabled(Level.WARN, marker, msg, null)) {
      log(marker, FQCN, Level.WARN, msg, null);
    }
  }
  
  public void warn(Marker marker, Message msg, Throwable t)
  {
    if (isEnabled(Level.WARN, marker, msg, t)) {
      log(marker, FQCN, Level.WARN, msg, t);
    }
  }
  
  public void warn(Marker marker, Object message)
  {
    if (isEnabled(Level.WARN, marker, message, null)) {
      log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void warn(Marker marker, Object message, Throwable t)
  {
    if (isEnabled(Level.WARN, marker, message, t)) {
      log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void warn(Marker marker, String message)
  {
    if (isEnabled(Level.WARN, marker, message)) {
      log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void warn(Marker marker, String message, Object... params)
  {
    if (isEnabled(Level.WARN, marker, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(marker, FQCN, Level.WARN, msg, msg.getThrowable());
    }
  }
  
  public void warn(Marker marker, String message, Throwable t)
  {
    if (isEnabled(Level.WARN, marker, message, t)) {
      log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void warn(Message msg)
  {
    if (isEnabled(Level.WARN, null, msg, null)) {
      log(null, FQCN, Level.WARN, msg, null);
    }
  }
  
  public void warn(Message msg, Throwable t)
  {
    if (isEnabled(Level.WARN, null, msg, t)) {
      log(null, FQCN, Level.WARN, msg, t);
    }
  }
  
  public void warn(Object message)
  {
    if (isEnabled(Level.WARN, null, message, null)) {
      log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void warn(Object message, Throwable t)
  {
    if (isEnabled(Level.WARN, null, message, t)) {
      log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
    }
  }
  
  public void warn(String message)
  {
    if (isEnabled(Level.WARN, null, message)) {
      log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
    }
  }
  
  public void warn(String message, Object... params)
  {
    if (isEnabled(Level.WARN, null, message, params))
    {
      Message msg = this.messageFactory.newMessage(message, params);
      log(null, FQCN, Level.WARN, msg, msg.getThrowable());
    }
  }
  
  public void warn(String message, Throwable t)
  {
    if (isEnabled(Level.WARN, null, message, t)) {
      log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
    }
  }
}
