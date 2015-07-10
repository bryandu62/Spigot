package org.apache.logging.log4j;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public abstract interface Logger
{
  public abstract void catching(Level paramLevel, Throwable paramThrowable);
  
  public abstract void catching(Throwable paramThrowable);
  
  public abstract void debug(Marker paramMarker, Message paramMessage);
  
  public abstract void debug(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void debug(Marker paramMarker, Object paramObject);
  
  public abstract void debug(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract void debug(Marker paramMarker, String paramString);
  
  public abstract void debug(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void debug(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public abstract void debug(Message paramMessage);
  
  public abstract void debug(Message paramMessage, Throwable paramThrowable);
  
  public abstract void debug(Object paramObject);
  
  public abstract void debug(Object paramObject, Throwable paramThrowable);
  
  public abstract void debug(String paramString);
  
  public abstract void debug(String paramString, Object... paramVarArgs);
  
  public abstract void debug(String paramString, Throwable paramThrowable);
  
  public abstract void entry();
  
  public abstract void entry(Object... paramVarArgs);
  
  public abstract void error(Marker paramMarker, Message paramMessage);
  
  public abstract void error(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void error(Marker paramMarker, Object paramObject);
  
  public abstract void error(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract void error(Marker paramMarker, String paramString);
  
  public abstract void error(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void error(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public abstract void error(Message paramMessage);
  
  public abstract void error(Message paramMessage, Throwable paramThrowable);
  
  public abstract void error(Object paramObject);
  
  public abstract void error(Object paramObject, Throwable paramThrowable);
  
  public abstract void error(String paramString);
  
  public abstract void error(String paramString, Object... paramVarArgs);
  
  public abstract void error(String paramString, Throwable paramThrowable);
  
  public abstract void exit();
  
  public abstract <R> R exit(R paramR);
  
  public abstract void fatal(Marker paramMarker, Message paramMessage);
  
  public abstract void fatal(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void fatal(Marker paramMarker, Object paramObject);
  
  public abstract void fatal(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract void fatal(Marker paramMarker, String paramString);
  
  public abstract void fatal(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void fatal(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public abstract void fatal(Message paramMessage);
  
  public abstract void fatal(Message paramMessage, Throwable paramThrowable);
  
  public abstract void fatal(Object paramObject);
  
  public abstract void fatal(Object paramObject, Throwable paramThrowable);
  
  public abstract void fatal(String paramString);
  
  public abstract void fatal(String paramString, Object... paramVarArgs);
  
  public abstract void fatal(String paramString, Throwable paramThrowable);
  
  public abstract MessageFactory getMessageFactory();
  
  public abstract String getName();
  
  public abstract void info(Marker paramMarker, Message paramMessage);
  
  public abstract void info(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void info(Marker paramMarker, Object paramObject);
  
  public abstract void info(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract void info(Marker paramMarker, String paramString);
  
  public abstract void info(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void info(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public abstract void info(Message paramMessage);
  
  public abstract void info(Message paramMessage, Throwable paramThrowable);
  
  public abstract void info(Object paramObject);
  
  public abstract void info(Object paramObject, Throwable paramThrowable);
  
  public abstract void info(String paramString);
  
  public abstract void info(String paramString, Object... paramVarArgs);
  
  public abstract void info(String paramString, Throwable paramThrowable);
  
  public abstract boolean isDebugEnabled();
  
  public abstract boolean isDebugEnabled(Marker paramMarker);
  
  public abstract boolean isEnabled(Level paramLevel);
  
  public abstract boolean isEnabled(Level paramLevel, Marker paramMarker);
  
  public abstract boolean isErrorEnabled();
  
  public abstract boolean isErrorEnabled(Marker paramMarker);
  
  public abstract boolean isFatalEnabled();
  
  public abstract boolean isFatalEnabled(Marker paramMarker);
  
  public abstract boolean isInfoEnabled();
  
  public abstract boolean isInfoEnabled(Marker paramMarker);
  
  public abstract boolean isTraceEnabled();
  
  public abstract boolean isTraceEnabled(Marker paramMarker);
  
  public abstract boolean isWarnEnabled();
  
  public abstract boolean isWarnEnabled(Marker paramMarker);
  
  public abstract void log(Level paramLevel, Marker paramMarker, Message paramMessage);
  
  public abstract void log(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void log(Level paramLevel, Marker paramMarker, Object paramObject);
  
  public abstract void log(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract void log(Level paramLevel, Marker paramMarker, String paramString);
  
  public abstract void log(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void log(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public abstract void log(Level paramLevel, Message paramMessage);
  
  public abstract void log(Level paramLevel, Message paramMessage, Throwable paramThrowable);
  
  public abstract void log(Level paramLevel, Object paramObject);
  
  public abstract void log(Level paramLevel, Object paramObject, Throwable paramThrowable);
  
  public abstract void log(Level paramLevel, String paramString);
  
  public abstract void log(Level paramLevel, String paramString, Object... paramVarArgs);
  
  public abstract void log(Level paramLevel, String paramString, Throwable paramThrowable);
  
  public abstract void printf(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void printf(Level paramLevel, String paramString, Object... paramVarArgs);
  
  public abstract <T extends Throwable> T throwing(Level paramLevel, T paramT);
  
  public abstract <T extends Throwable> T throwing(T paramT);
  
  public abstract void trace(Marker paramMarker, Message paramMessage);
  
  public abstract void trace(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void trace(Marker paramMarker, Object paramObject);
  
  public abstract void trace(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract void trace(Marker paramMarker, String paramString);
  
  public abstract void trace(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void trace(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public abstract void trace(Message paramMessage);
  
  public abstract void trace(Message paramMessage, Throwable paramThrowable);
  
  public abstract void trace(Object paramObject);
  
  public abstract void trace(Object paramObject, Throwable paramThrowable);
  
  public abstract void trace(String paramString);
  
  public abstract void trace(String paramString, Object... paramVarArgs);
  
  public abstract void trace(String paramString, Throwable paramThrowable);
  
  public abstract void warn(Marker paramMarker, Message paramMessage);
  
  public abstract void warn(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void warn(Marker paramMarker, Object paramObject);
  
  public abstract void warn(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract void warn(Marker paramMarker, String paramString);
  
  public abstract void warn(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract void warn(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  public abstract void warn(Message paramMessage);
  
  public abstract void warn(Message paramMessage, Throwable paramThrowable);
  
  public abstract void warn(Object paramObject);
  
  public abstract void warn(Object paramObject, Throwable paramThrowable);
  
  public abstract void warn(String paramString);
  
  public abstract void warn(String paramString, Object... paramVarArgs);
  
  public abstract void warn(String paramString, Throwable paramThrowable);
}
