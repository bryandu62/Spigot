package org.apache.logging.log4j.core.config;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

public class JSONConfiguration
  extends BaseConfiguration
  implements Reconfigurable
{
  private static final String[] VERBOSE_CLASSES = { ResolverUtil.class.getName() };
  private static final int BUF_SIZE = 16384;
  private final List<Status> status = new ArrayList();
  private JsonNode root;
  private final List<String> messages = new ArrayList();
  private final File configFile;
  
  public JSONConfiguration(ConfigurationFactory.ConfigurationSource configSource)
  {
    this.configFile = configSource.getFile();
    try
    {
      InputStream configStream = configSource.getInputStream();
      byte[] buffer = toByteArray(configStream);
      configStream.close();
      InputStream is = new ByteArrayInputStream(buffer);
      ObjectMapper mapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
      this.root = mapper.readTree(is);
      if (this.root.size() == 1)
      {
        Iterator<JsonNode> i = this.root.elements();
        this.root = ((JsonNode)i.next());
      }
      processAttributes(this.rootNode, this.root);
      Level status = getDefaultStatus();
      boolean verbose = false;
      PrintStream stream = System.out;
      for (Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
        if ("status".equalsIgnoreCase((String)entry.getKey()))
        {
          status = Level.toLevel(getStrSubstitutor().replace((String)entry.getValue()), null);
          if (status == null)
          {
            status = Level.ERROR;
            this.messages.add("Invalid status specified: " + (String)entry.getValue() + ". Defaulting to ERROR");
          }
        }
        else if ("dest".equalsIgnoreCase((String)entry.getKey()))
        {
          String dest = (String)entry.getValue();
          if (dest != null) {
            if (dest.equalsIgnoreCase("err")) {
              stream = System.err;
            } else {
              try
              {
                File destFile = FileUtils.fileFromURI(new URI(dest));
                String enc = Charset.defaultCharset().name();
                stream = new PrintStream(new FileOutputStream(destFile), true, enc);
              }
              catch (URISyntaxException use)
              {
                System.err.println("Unable to write to " + dest + ". Writing to stdout");
              }
            }
          }
        }
        else if ("shutdownHook".equalsIgnoreCase((String)entry.getKey()))
        {
          String hook = getStrSubstitutor().replace((String)entry.getValue());
          this.isShutdownHookEnabled = (!hook.equalsIgnoreCase("disable"));
        }
        else if ("verbose".equalsIgnoreCase((String)entry.getKey()))
        {
          verbose = Boolean.parseBoolean(getStrSubstitutor().replace((String)entry.getValue()));
        }
        else if ("packages".equalsIgnoreCase((String)entry.getKey()))
        {
          String[] packages = getStrSubstitutor().replace((String)entry.getValue()).split(",");
          for (String p : packages) {
            PluginManager.addPackage(p);
          }
        }
        else if ("name".equalsIgnoreCase((String)entry.getKey()))
        {
          setName(getStrSubstitutor().replace((String)entry.getValue()));
        }
        else if ("monitorInterval".equalsIgnoreCase((String)entry.getKey()))
        {
          int interval = Integer.parseInt(getStrSubstitutor().replace((String)entry.getValue()));
          if ((interval > 0) && (this.configFile != null)) {
            this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, interval);
          }
        }
        else if ("advertiser".equalsIgnoreCase((String)entry.getKey()))
        {
          createAdvertiser(getStrSubstitutor().replace((String)entry.getValue()), configSource, buffer, "application/json");
        }
      }
      Iterator<StatusListener> statusIter = ((StatusLogger)LOGGER).getListeners();
      boolean found = false;
      while (statusIter.hasNext())
      {
        StatusListener listener = (StatusListener)statusIter.next();
        if ((listener instanceof StatusConsoleListener))
        {
          found = true;
          ((StatusConsoleListener)listener).setLevel(status);
          if (!verbose) {
            ((StatusConsoleListener)listener).setFilters(VERBOSE_CLASSES);
          }
        }
      }
      if ((!found) && (status != Level.OFF))
      {
        StatusConsoleListener listener = new StatusConsoleListener(status, stream);
        if (!verbose) {
          listener.setFilters(VERBOSE_CLASSES);
        }
        ((StatusLogger)LOGGER).registerListener(listener);
        for (String msg : this.messages) {
          LOGGER.error(msg);
        }
      }
      if (getName() == null) {
        setName(configSource.getLocation());
      }
    }
    catch (Exception ex)
    {
      LOGGER.error("Error parsing " + configSource.getLocation(), ex);
      ex.printStackTrace();
    }
  }
  
  public void stop()
  {
    super.stop();
  }
  
  public void setup()
  {
    Iterator<Map.Entry<String, JsonNode>> iter = this.root.fields();
    List<Node> children = this.rootNode.getChildren();
    while (iter.hasNext())
    {
      Map.Entry<String, JsonNode> entry = (Map.Entry)iter.next();
      JsonNode n = (JsonNode)entry.getValue();
      if (n.isObject())
      {
        LOGGER.debug("Processing node for object " + (String)entry.getKey());
        children.add(constructNode((String)entry.getKey(), this.rootNode, n));
      }
      else if (n.isArray())
      {
        LOGGER.error("Arrays are not supported at the root configuration.");
      }
    }
    LOGGER.debug("Completed parsing configuration");
    if (this.status.size() > 0)
    {
      for (Status s : this.status) {
        LOGGER.error("Error processing element " + s.name + ": " + s.errorType);
      }
      return;
    }
  }
  
  public Configuration reconfigure()
  {
    if (this.configFile != null) {
      try
      {
        ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);
        
        return new JSONConfiguration(source);
      }
      catch (FileNotFoundException ex)
      {
        LOGGER.error("Cannot locate file " + this.configFile, ex);
      }
    }
    return null;
  }
  
  private Node constructNode(String name, Node parent, JsonNode jsonNode)
  {
    PluginType<?> type = this.pluginManager.getPluginType(name);
    Node node = new Node(parent, name, type);
    processAttributes(node, jsonNode);
    Iterator<Map.Entry<String, JsonNode>> iter = jsonNode.fields();
    List<Node> children = node.getChildren();
    while (iter.hasNext())
    {
      Map.Entry<String, JsonNode> entry = (Map.Entry)iter.next();
      JsonNode n = (JsonNode)entry.getValue();
      if ((n.isArray()) || (n.isObject()))
      {
        if (type == null) {
          this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
        }
        if (n.isArray())
        {
          LOGGER.debug("Processing node for array " + (String)entry.getKey());
          for (int i = 0; i < n.size(); i++)
          {
            String pluginType = getType(n.get(i), (String)entry.getKey());
            PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
            Node item = new Node(node, (String)entry.getKey(), entryType);
            processAttributes(item, n.get(i));
            if (pluginType.equals(entry.getKey())) {
              LOGGER.debug("Processing " + (String)entry.getKey() + "[" + i + "]");
            } else {
              LOGGER.debug("Processing " + pluginType + " " + (String)entry.getKey() + "[" + i + "]");
            }
            Iterator<Map.Entry<String, JsonNode>> itemIter = n.get(i).fields();
            List<Node> itemChildren = item.getChildren();
            while (itemIter.hasNext())
            {
              Map.Entry<String, JsonNode> itemEntry = (Map.Entry)itemIter.next();
              if (((JsonNode)itemEntry.getValue()).isObject())
              {
                LOGGER.debug("Processing node for object " + (String)itemEntry.getKey());
                itemChildren.add(constructNode((String)itemEntry.getKey(), item, (JsonNode)itemEntry.getValue()));
              }
            }
            children.add(item);
          }
        }
        else
        {
          LOGGER.debug("Processing node for object " + (String)entry.getKey());
          children.add(constructNode((String)entry.getKey(), node, n));
        }
      }
    }
    String t;
    String t;
    if (type == null) {
      t = "null";
    } else {
      t = type.getElementName() + ":" + type.getPluginClass();
    }
    String p = node.getParent().getName() == null ? "root" : node.getParent() == null ? "null" : node.getParent().getName();
    
    LOGGER.debug("Returning " + node.getName() + " with parent " + p + " of type " + t);
    return node;
  }
  
  private String getType(JsonNode node, String name)
  {
    Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
    while (iter.hasNext())
    {
      Map.Entry<String, JsonNode> entry = (Map.Entry)iter.next();
      if (((String)entry.getKey()).equalsIgnoreCase("type"))
      {
        JsonNode n = (JsonNode)entry.getValue();
        if (n.isValueNode()) {
          return n.asText();
        }
      }
    }
    return name;
  }
  
  private void processAttributes(Node parent, JsonNode node)
  {
    Map<String, String> attrs = parent.getAttributes();
    Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
    while (iter.hasNext())
    {
      Map.Entry<String, JsonNode> entry = (Map.Entry)iter.next();
      if (!((String)entry.getKey()).equalsIgnoreCase("type"))
      {
        JsonNode n = (JsonNode)entry.getValue();
        if (n.isValueNode()) {
          attrs.put(entry.getKey(), n.asText());
        }
      }
    }
  }
  
  protected byte[] toByteArray(InputStream is)
    throws IOException
  {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    
    byte[] data = new byte['䀀'];
    int nRead;
    while ((nRead = is.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    return buffer.toByteArray();
  }
  
  private static enum ErrorType
  {
    CLASS_NOT_FOUND;
    
    private ErrorType() {}
  }
  
  private class Status
  {
    private final JsonNode node;
    private final String name;
    private final JSONConfiguration.ErrorType errorType;
    
    public Status(String name, JsonNode node, JSONConfiguration.ErrorType errorType)
    {
      this.name = name;
      this.node = node;
      this.errorType = errorType;
    }
  }
}
