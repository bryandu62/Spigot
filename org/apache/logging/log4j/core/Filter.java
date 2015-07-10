package org.apache.logging.log4j.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.EnglishEnums;

public abstract interface Filter
{
  public abstract Result getOnMismatch();
  
  public abstract Result getOnMatch();
  
  public abstract Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  public abstract Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  public abstract Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract Result filter(LogEvent paramLogEvent);
  
  public static enum Result
  {
    ACCEPT,  NEUTRAL,  DENY;
    
    private Result() {}
    
    public static Result toResult(String name)
    {
      return toResult(name, null);
    }
    
    public static Result toResult(String name, Result defaultResult)
    {
      return (Result)EnglishEnums.valueOf(Result.class, name, defaultResult);
    }
  }
}
