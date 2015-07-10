package org.apache.logging.log4j.core.net;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;

public class DatagramSocketManager
  extends AbstractSocketManager
{
  private static final DatagramSocketManagerFactory FACTORY = new DatagramSocketManagerFactory(null);
  
  protected DatagramSocketManager(String name, OutputStream os, InetAddress address, String host, int port, Layout<? extends Serializable> layout)
  {
    super(name, os, address, host, port, layout);
  }
  
  public static DatagramSocketManager getSocketManager(String host, int port, Layout<? extends Serializable> layout)
  {
    if (Strings.isEmpty(host)) {
      throw new IllegalArgumentException("A host name is required");
    }
    if (port <= 0) {
      throw new IllegalArgumentException("A port value is required");
    }
    return (DatagramSocketManager)getManager("UDP:" + host + ":" + port, new FactoryData(host, port, layout), FACTORY);
  }
  
  public Map<String, String> getContentFormat()
  {
    Map<String, String> result = new HashMap(super.getContentFormat());
    result.put("protocol", "udp");
    result.put("direction", "out");
    
    return result;
  }
  
  private static class FactoryData
  {
    private final String host;
    private final int port;
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(String host, int port, Layout<? extends Serializable> layout)
    {
      this.host = host;
      this.port = port;
      this.layout = layout;
    }
  }
  
  private static class DatagramSocketManagerFactory
    implements ManagerFactory<DatagramSocketManager, DatagramSocketManager.FactoryData>
  {
    public DatagramSocketManager createManager(String name, DatagramSocketManager.FactoryData data)
    {
      OutputStream os = new DatagramOutputStream(DatagramSocketManager.FactoryData.access$100(data), DatagramSocketManager.FactoryData.access$200(data), DatagramSocketManager.FactoryData.access$300(data).getHeader(), DatagramSocketManager.FactoryData.access$300(data).getFooter());
      InetAddress address;
      try
      {
        address = InetAddress.getByName(DatagramSocketManager.FactoryData.access$100(data));
      }
      catch (UnknownHostException ex)
      {
        DatagramSocketManager.LOGGER.error("Could not find address of " + DatagramSocketManager.FactoryData.access$100(data), ex);
        return null;
      }
      return new DatagramSocketManager(name, os, address, DatagramSocketManager.FactoryData.access$100(data), DatagramSocketManager.FactoryData.access$200(data), DatagramSocketManager.FactoryData.access$300(data));
    }
  }
}
