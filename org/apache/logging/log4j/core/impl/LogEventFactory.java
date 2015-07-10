package org.apache.logging.log4j.core.impl;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.message.Message;

public abstract interface LogEventFactory
{
  public abstract LogEvent createEvent(String paramString1, Marker paramMarker, String paramString2, Level paramLevel, Message paramMessage, List<Property> paramList, Throwable paramThrowable);
}
