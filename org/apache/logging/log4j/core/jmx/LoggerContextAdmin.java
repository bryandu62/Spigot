package org.apache.logging.log4j.core.jmx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.LoggerContext.Status;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationFactory.ConfigurationSource;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.status.StatusLogger;

public class LoggerContextAdmin
  extends NotificationBroadcasterSupport
  implements LoggerContextAdminMBean, PropertyChangeListener
{
  private static final int PAGE = 4096;
  private static final int TEXT_BUFFER = 65536;
  private static final int BUFFER_SIZE = 2048;
  private static final StatusLogger LOGGER = ;
  private final AtomicLong sequenceNo = new AtomicLong();
  private final ObjectName objectName;
  private final LoggerContext loggerContext;
  private String customConfigText;
  
  public LoggerContextAdmin(LoggerContext loggerContext, Executor executor)
  {
    super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
    this.loggerContext = ((LoggerContext)Assert.isNotNull(loggerContext, "loggerContext"));
    try
    {
      String ctxName = Server.escape(loggerContext.getName());
      String name = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s", new Object[] { ctxName });
      this.objectName = new ObjectName(name);
    }
    catch (Exception e)
    {
      throw new IllegalStateException(e);
    }
    loggerContext.addPropertyChangeListener(this);
  }
  
  private static MBeanNotificationInfo createNotificationInfo()
  {
    String[] notifTypes = { "com.apache.logging.log4j.core.jmx.config.reconfigured" };
    
    String name = Notification.class.getName();
    String description = "Configuration reconfigured";
    return new MBeanNotificationInfo(notifTypes, name, "Configuration reconfigured");
  }
  
  public String getStatus()
  {
    return this.loggerContext.getStatus().toString();
  }
  
  public String getName()
  {
    return this.loggerContext.getName();
  }
  
  private Configuration getConfig()
  {
    return this.loggerContext.getConfiguration();
  }
  
  public String getConfigLocationURI()
  {
    if (this.loggerContext.getConfigLocation() != null) {
      return String.valueOf(this.loggerContext.getConfigLocation());
    }
    if (getConfigName() != null) {
      return String.valueOf(new File(getConfigName()).toURI());
    }
    return "";
  }
  
  public void setConfigLocationURI(String configLocation)
    throws URISyntaxException, IOException
  {
    LOGGER.debug("---------");
    LOGGER.debug("Remote request to reconfigure using location " + configLocation);
    
    URI uri = new URI(configLocation);
    
    uri.toURL().openStream().close();
    
    this.loggerContext.setConfigLocation(uri);
    LOGGER.debug("Completed remote request to reconfigure.");
  }
  
  public void propertyChange(PropertyChangeEvent evt)
  {
    if (!"config".equals(evt.getPropertyName())) {
      return;
    }
    if (this.loggerContext.getConfiguration().getName() != null) {
      this.customConfigText = null;
    }
    Notification notif = new Notification("com.apache.logging.log4j.core.jmx.config.reconfigured", getObjectName(), nextSeqNo(), now(), null);
    
    sendNotification(notif);
  }
  
  public String getConfigText()
    throws IOException
  {
    return getConfigText(Charsets.UTF_8.name());
  }
  
  public String getConfigText(String charsetName)
    throws IOException
  {
    if (this.customConfigText != null) {
      return this.customConfigText;
    }
    try
    {
      Charset charset = Charset.forName(charsetName);
      return readContents(new URI(getConfigLocationURI()), charset);
    }
    catch (Exception ex)
    {
      StringWriter sw = new StringWriter(2048);
      ex.printStackTrace(new PrintWriter(sw));
      return sw.toString();
    }
  }
  
  public void setConfigText(String configText, String charsetName)
  {
    String old = this.customConfigText;
    this.customConfigText = ((String)Assert.isNotNull(configText, "configText"));
    LOGGER.debug("---------");
    LOGGER.debug("Remote request to reconfigure from config text.");
    try
    {
      InputStream in = new ByteArrayInputStream(configText.getBytes(charsetName));
      
      ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource(in);
      Configuration updated = ConfigurationFactory.getInstance().getConfiguration(source);
      
      this.loggerContext.start(updated);
      LOGGER.debug("Completed remote request to reconfigure from config text.");
    }
    catch (Exception ex)
    {
      this.customConfigText = old;
      String msg = "Could not reconfigure from config text";
      LOGGER.error("Could not reconfigure from config text", ex);
      throw new IllegalArgumentException("Could not reconfigure from config text", ex);
    }
  }
  
  private String readContents(URI uri, Charset charset)
    throws IOException
  {
    InputStream in = null;
    Reader reader = null;
    try
    {
      in = uri.toURL().openStream();
      reader = new InputStreamReader(in, charset);
      StringBuilder result = new StringBuilder(65536);
      char[] buff = new char['က'];
      int count = -1;
      while ((count = reader.read(buff)) >= 0) {
        result.append(buff, 0, count);
      }
      return result.toString();
    }
    finally
    {
      Closer.closeSilent(in);
      Closer.closeSilent(reader);
    }
  }
  
  public String getConfigName()
  {
    return getConfig().getName();
  }
  
  public String getConfigClassName()
  {
    return getConfig().getClass().getName();
  }
  
  public String getConfigFilter()
  {
    return String.valueOf(getConfig().getFilter());
  }
  
  public String getConfigMonitorClassName()
  {
    return getConfig().getConfigurationMonitor().getClass().getName();
  }
  
  public Map<String, String> getConfigProperties()
  {
    return getConfig().getProperties();
  }
  
  public ObjectName getObjectName()
  {
    return this.objectName;
  }
  
  private long nextSeqNo()
  {
    return this.sequenceNo.getAndIncrement();
  }
  
  private long now()
  {
    return System.currentTimeMillis();
  }
}
