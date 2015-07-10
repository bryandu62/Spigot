package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.DatagramSocketManager;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.net.TCPSocketManager;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(name="Socket", category="Core", elementType="appender", printObject=true)
public class SocketAppender
  extends AbstractOutputStreamAppender
{
  private Object advertisement;
  private final Advertiser advertiser;
  
  protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser)
  {
    super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
    if (advertiser != null)
    {
      Map<String, String> configuration = new HashMap(layout.getContentFormat());
      configuration.putAll(manager.getContentFormat());
      configuration.put("contentType", layout.getContentType());
      configuration.put("name", name);
      this.advertisement = advertiser.advertise(configuration);
    }
    this.advertiser = advertiser;
  }
  
  public void stop()
  {
    super.stop();
    if (this.advertiser != null) {
      this.advertiser.unadvertise(this.advertisement);
    }
  }
  
  @PluginFactory
  public static SocketAppender createAppender(@PluginAttribute("host") String host, @PluginAttribute("port") String portNum, @PluginAttribute("protocol") String protocol, @PluginAttribute("reconnectionDelay") String delay, @PluginAttribute("immediateFail") String immediateFail, @PluginAttribute("name") String name, @PluginAttribute("immediateFlush") String immediateFlush, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") Filter filter, @PluginAttribute("advertise") String advertise, @PluginConfiguration Configuration config)
  {
    boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
    boolean isAdvertise = Boolean.parseBoolean(advertise);
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    boolean fail = Booleans.parseBoolean(immediateFail, true);
    int reconnectDelay = AbstractAppender.parseInt(delay, 0);
    int port = AbstractAppender.parseInt(portNum, 0);
    if (layout == null) {
      layout = SerializedLayout.createLayout();
    }
    if (name == null)
    {
      LOGGER.error("No name provided for SocketAppender");
      return null;
    }
    Protocol p = (Protocol)EnglishEnums.valueOf(Protocol.class, protocol != null ? protocol : Protocol.TCP.name());
    if (p.equals(Protocol.UDP)) {
      isFlush = true;
    }
    AbstractSocketManager manager = createSocketManager(p, host, port, reconnectDelay, fail, layout);
    if (manager == null) {
      return null;
    }
    return new SocketAppender(name, layout, filter, manager, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
  }
  
  protected static AbstractSocketManager createSocketManager(Protocol p, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout)
  {
    switch (p)
    {
    case TCP: 
      return TCPSocketManager.getSocketManager(host, port, delay, immediateFail, layout);
    case UDP: 
      return DatagramSocketManager.getSocketManager(host, port, layout);
    }
    return null;
  }
}
