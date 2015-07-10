package org.apache.logging.log4j.core.config.plugins;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.Appender;

@Plugin(name="appenders", category="Core")
public final class AppendersPlugin
{
  @PluginFactory
  public static ConcurrentMap<String, Appender> createAppenders(@PluginElement("Appenders") Appender[] appenders)
  {
    ConcurrentMap<String, Appender> map = new ConcurrentHashMap();
    for (Appender appender : appenders) {
      map.put(appender.getName(), appender);
    }
    return map;
  }
}
