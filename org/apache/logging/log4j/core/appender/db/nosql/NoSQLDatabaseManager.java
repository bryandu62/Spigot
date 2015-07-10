package org.apache.logging.log4j.core.appender.db.nosql;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager.AbstractFactoryData;
import org.apache.logging.log4j.message.Message;

public final class NoSQLDatabaseManager<W>
  extends AbstractDatabaseManager
{
  private static final NoSQLDatabaseManagerFactory FACTORY = new NoSQLDatabaseManagerFactory(null);
  private final NoSQLProvider<NoSQLConnection<W, ? extends NoSQLObject<W>>> provider;
  private NoSQLConnection<W, ? extends NoSQLObject<W>> connection;
  
  private NoSQLDatabaseManager(String name, int bufferSize, NoSQLProvider<NoSQLConnection<W, ? extends NoSQLObject<W>>> provider)
  {
    super(name, bufferSize);
    this.provider = provider;
  }
  
  protected void connectInternal()
  {
    this.connection = this.provider.getConnection();
  }
  
  protected void disconnectInternal()
  {
    if ((this.connection != null) && (!this.connection.isClosed())) {
      this.connection.close();
    }
  }
  
  protected void writeInternal(LogEvent event)
  {
    if ((!isConnected()) || (this.connection == null) || (this.connection.isClosed())) {
      throw new AppenderLoggingException("Cannot write logging event; NoSQL manager not connected to the database.");
    }
    NoSQLObject<W> entity = this.connection.createObject();
    entity.set("level", event.getLevel());
    entity.set("loggerName", event.getLoggerName());
    entity.set("message", event.getMessage() == null ? null : event.getMessage().getFormattedMessage());
    
    StackTraceElement source = event.getSource();
    if (source == null) {
      entity.set("source", (Object)null);
    } else {
      entity.set("source", convertStackTraceElement(source));
    }
    Marker marker = event.getMarker();
    if (marker == null)
    {
      entity.set("marker", (Object)null);
    }
    else
    {
      NoSQLObject<W> originalMarkerEntity = this.connection.createObject();
      NoSQLObject<W> markerEntity = originalMarkerEntity;
      markerEntity.set("name", marker.getName());
      while (marker.getParent() != null)
      {
        marker = marker.getParent();
        NoSQLObject<W> parentMarkerEntity = this.connection.createObject();
        parentMarkerEntity.set("name", marker.getName());
        markerEntity.set("parent", parentMarkerEntity);
        markerEntity = parentMarkerEntity;
      }
      entity.set("marker", originalMarkerEntity);
    }
    entity.set("threadName", event.getThreadName());
    entity.set("millis", Long.valueOf(event.getMillis()));
    entity.set("date", new Date(event.getMillis()));
    
    Throwable thrown = event.getThrown();
    if (thrown == null)
    {
      entity.set("thrown", (Object)null);
    }
    else
    {
      NoSQLObject<W> originalExceptionEntity = this.connection.createObject();
      NoSQLObject<W> exceptionEntity = originalExceptionEntity;
      exceptionEntity.set("type", thrown.getClass().getName());
      exceptionEntity.set("message", thrown.getMessage());
      exceptionEntity.set("stackTrace", convertStackTrace(thrown.getStackTrace()));
      while (thrown.getCause() != null)
      {
        thrown = thrown.getCause();
        NoSQLObject<W> causingExceptionEntity = this.connection.createObject();
        causingExceptionEntity.set("type", thrown.getClass().getName());
        causingExceptionEntity.set("message", thrown.getMessage());
        causingExceptionEntity.set("stackTrace", convertStackTrace(thrown.getStackTrace()));
        exceptionEntity.set("cause", causingExceptionEntity);
        exceptionEntity = causingExceptionEntity;
      }
      entity.set("thrown", originalExceptionEntity);
    }
    Map<String, String> contextMap = event.getContextMap();
    if (contextMap == null)
    {
      entity.set("contextMap", (Object)null);
    }
    else
    {
      NoSQLObject<W> contextMapEntity = this.connection.createObject();
      for (Map.Entry<String, String> entry : contextMap.entrySet()) {
        contextMapEntity.set((String)entry.getKey(), entry.getValue());
      }
      entity.set("contextMap", contextMapEntity);
    }
    ThreadContext.ContextStack contextStack = event.getContextStack();
    if (contextStack == null) {
      entity.set("contextStack", (Object)null);
    } else {
      entity.set("contextStack", contextStack.asList().toArray());
    }
    this.connection.insertObject(entity);
  }
  
  private NoSQLObject<W>[] convertStackTrace(StackTraceElement[] stackTrace)
  {
    NoSQLObject<W>[] stackTraceEntities = this.connection.createList(stackTrace.length);
    for (int i = 0; i < stackTrace.length; i++) {
      stackTraceEntities[i] = convertStackTraceElement(stackTrace[i]);
    }
    return stackTraceEntities;
  }
  
  private NoSQLObject<W> convertStackTraceElement(StackTraceElement element)
  {
    NoSQLObject<W> elementEntity = this.connection.createObject();
    elementEntity.set("className", element.getClassName());
    elementEntity.set("methodName", element.getMethodName());
    elementEntity.set("fileName", element.getFileName());
    elementEntity.set("lineNumber", Integer.valueOf(element.getLineNumber()));
    return elementEntity;
  }
  
  public static NoSQLDatabaseManager<?> getNoSQLDatabaseManager(String name, int bufferSize, NoSQLProvider<?> provider)
  {
    return (NoSQLDatabaseManager)AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, provider), FACTORY);
  }
  
  private static final class FactoryData
    extends AbstractDatabaseManager.AbstractFactoryData
  {
    private final NoSQLProvider<?> provider;
    
    protected FactoryData(int bufferSize, NoSQLProvider<?> provider)
    {
      super();
      this.provider = provider;
    }
  }
  
  private static final class NoSQLDatabaseManagerFactory
    implements ManagerFactory<NoSQLDatabaseManager<?>, NoSQLDatabaseManager.FactoryData>
  {
    public NoSQLDatabaseManager<?> createManager(String name, NoSQLDatabaseManager.FactoryData data)
    {
      return new NoSQLDatabaseManager(name, data.getBufferSize(), NoSQLDatabaseManager.FactoryData.access$100(data), null);
    }
  }
}
