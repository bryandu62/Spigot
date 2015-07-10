package org.apache.logging.log4j.core.appender.db.jpa;

import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextMapAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextStackAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.MarkerAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.MessageAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.StackTraceElementAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ThrowableAttributeConverter;
import org.apache.logging.log4j.message.Message;

@MappedSuperclass
public abstract class BasicLogEventEntity
  extends AbstractLogEventWrapperEntity
{
  private static final long serialVersionUID = 1L;
  
  public BasicLogEventEntity() {}
  
  public BasicLogEventEntity(LogEvent wrappedEvent)
  {
    super(wrappedEvent);
  }
  
  @Basic
  @Enumerated(EnumType.STRING)
  public Level getLevel()
  {
    return getWrappedEvent().getLevel();
  }
  
  @Basic
  public String getLoggerName()
  {
    return getWrappedEvent().getLoggerName();
  }
  
  @Convert(converter=StackTraceElementAttributeConverter.class)
  public StackTraceElement getSource()
  {
    return getWrappedEvent().getSource();
  }
  
  @Convert(converter=MessageAttributeConverter.class)
  public Message getMessage()
  {
    return getWrappedEvent().getMessage();
  }
  
  @Convert(converter=MarkerAttributeConverter.class)
  public Marker getMarker()
  {
    return getWrappedEvent().getMarker();
  }
  
  @Basic
  public String getThreadName()
  {
    return getWrappedEvent().getThreadName();
  }
  
  @Basic
  public long getMillis()
  {
    return getWrappedEvent().getMillis();
  }
  
  @Convert(converter=ThrowableAttributeConverter.class)
  public Throwable getThrown()
  {
    return getWrappedEvent().getThrown();
  }
  
  @Convert(converter=ContextMapAttributeConverter.class)
  public Map<String, String> getContextMap()
  {
    return getWrappedEvent().getContextMap();
  }
  
  @Convert(converter=ContextStackAttributeConverter.class)
  public ThreadContext.ContextStack getContextStack()
  {
    return getWrappedEvent().getContextStack();
  }
  
  @Basic
  public String getFQCN()
  {
    return getWrappedEvent().getFQCN();
  }
}
