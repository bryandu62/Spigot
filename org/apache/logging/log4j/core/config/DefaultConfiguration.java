package org.apache.logging.log4j.core.config;

import java.io.Serializable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PropertiesUtil;

public class DefaultConfiguration
  extends BaseConfiguration
{
  public static final String DEFAULT_NAME = "Default";
  public static final String DEFAULT_LEVEL = "org.apache.logging.log4j.level";
  
  public DefaultConfiguration()
  {
    setName("Default");
    Layout<? extends Serializable> layout = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", null, null, null, null);
    
    Appender appender = ConsoleAppender.createAppender(layout, null, "SYSTEM_OUT", "Console", "false", "true");
    
    appender.start();
    addAppender(appender);
    LoggerConfig root = getRootLogger();
    root.addAppender(appender, null, null);
    
    String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
    Level level = (levelName != null) && (Level.valueOf(levelName) != null) ? Level.valueOf(levelName) : Level.ERROR;
    
    root.setLevel(level);
  }
  
  protected void doConfigure() {}
}
