package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.status.StatusLogger;

public class JNDIContextSelector
  implements NamedContextSelector
{
  private static final LoggerContext CONTEXT = new LoggerContext("Default");
  private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP = new ConcurrentHashMap();
  private static final StatusLogger LOGGER = StatusLogger.getLogger();
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext)
  {
    return getContext(fqcn, loader, currentContext, null);
  }
  
  public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation)
  {
    LoggerContext lc = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
    if (lc != null) {
      return lc;
    }
    String loggingContextName = null;
    try
    {
      Context ctx = new InitialContext();
      loggingContextName = (String)lookup(ctx, "java:comp/env/log4j/context-name");
    }
    catch (NamingException ne)
    {
      LOGGER.error("Unable to lookup java:comp/env/log4j/context-name", ne);
    }
    return loggingContextName == null ? CONTEXT : locateContext(loggingContextName, null, configLocation);
  }
  
  public LoggerContext locateContext(String name, Object externalContext, URI configLocation)
  {
    if (name == null)
    {
      LOGGER.error("A context name is required to locate a LoggerContext");
      return null;
    }
    if (!CONTEXT_MAP.containsKey(name))
    {
      LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
      CONTEXT_MAP.putIfAbsent(name, ctx);
    }
    return (LoggerContext)CONTEXT_MAP.get(name);
  }
  
  public void removeContext(LoggerContext context)
  {
    for (Map.Entry<String, LoggerContext> entry : CONTEXT_MAP.entrySet()) {
      if (((LoggerContext)entry.getValue()).equals(context)) {
        CONTEXT_MAP.remove(entry.getKey());
      }
    }
  }
  
  public LoggerContext removeContext(String name)
  {
    return (LoggerContext)CONTEXT_MAP.remove(name);
  }
  
  public List<LoggerContext> getLoggerContexts()
  {
    List<LoggerContext> list = new ArrayList(CONTEXT_MAP.values());
    return Collections.unmodifiableList(list);
  }
  
  protected static Object lookup(Context ctx, String name)
    throws NamingException
  {
    if (ctx == null) {
      return null;
    }
    try
    {
      return ctx.lookup(name);
    }
    catch (NameNotFoundException e)
    {
      LOGGER.error("Could not find name [" + name + "].");
      throw e;
    }
  }
}
