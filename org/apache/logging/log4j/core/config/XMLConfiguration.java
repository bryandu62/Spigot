package org.apache.logging.log4j.core.config;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLConfiguration
  extends BaseConfiguration
  implements Reconfigurable
{
  private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
  private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
  private static final String[] VERBOSE_CLASSES = { ResolverUtil.class.getName() };
  private static final String LOG4J_XSD = "Log4j-config.xsd";
  private static final int BUF_SIZE = 16384;
  private final List<Status> status = new ArrayList();
  private Element rootElement;
  private boolean strict;
  private String schema;
  private final File configFile;
  
  static DocumentBuilder newDocumentBuilder()
    throws ParserConfigurationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    enableXInclude(factory);
    return factory.newDocumentBuilder();
  }
  
  private static void enableXInclude(DocumentBuilderFactory factory)
  {
    try
    {
      factory.setXIncludeAware(true);
    }
    catch (UnsupportedOperationException e)
    {
      LOGGER.warn("The DocumentBuilderFactory does not support XInclude: " + factory, e);
    }
    catch (AbstractMethodError err)
    {
      LOGGER.warn("The DocumentBuilderFactory is out of date and does not support XInclude: " + factory);
    }
    try
    {
      factory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
    }
    catch (ParserConfigurationException e)
    {
      LOGGER.warn("The DocumentBuilderFactory [" + factory + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-base-uris" + "]", e);
    }
    catch (AbstractMethodError err)
    {
      LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + factory);
    }
    try
    {
      factory.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
    }
    catch (ParserConfigurationException e)
    {
      LOGGER.warn("The DocumentBuilderFactory [" + factory + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-language" + "]", e);
    }
    catch (AbstractMethodError err)
    {
      LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + factory);
    }
  }
  
  public XMLConfiguration(ConfigurationFactory.ConfigurationSource configSource)
  {
    this.configFile = configSource.getFile();
    byte[] buffer = null;
    try
    {
      List<String> messages = new ArrayList();
      InputStream configStream = configSource.getInputStream();
      buffer = toByteArray(configStream);
      configStream.close();
      InputSource source = new InputSource(new ByteArrayInputStream(buffer));
      Document document = newDocumentBuilder().parse(source);
      this.rootElement = document.getDocumentElement();
      Map<String, String> attrs = processAttributes(this.rootNode, this.rootElement);
      Level status = getDefaultStatus();
      boolean verbose = false;
      PrintStream stream = System.out;
      for (Map.Entry<String, String> entry : attrs.entrySet()) {
        if ("status".equalsIgnoreCase((String)entry.getKey()))
        {
          Level stat = Level.toLevel(getStrSubstitutor().replace((String)entry.getValue()), null);
          if (stat != null) {
            status = stat;
          } else {
            messages.add("Invalid status specified: " + (String)entry.getValue() + ". Defaulting to " + status);
          }
        }
        else if ("dest".equalsIgnoreCase((String)entry.getKey()))
        {
          String dest = getStrSubstitutor().replace((String)entry.getValue());
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
        else if ("packages".equalsIgnoreCase(getStrSubstitutor().replace((String)entry.getKey())))
        {
          String[] packages = ((String)entry.getValue()).split(",");
          for (String p : packages) {
            PluginManager.addPackage(p);
          }
        }
        else if ("name".equalsIgnoreCase((String)entry.getKey()))
        {
          setName(getStrSubstitutor().replace((String)entry.getValue()));
        }
        else if ("strict".equalsIgnoreCase((String)entry.getKey()))
        {
          this.strict = Boolean.parseBoolean(getStrSubstitutor().replace((String)entry.getValue()));
        }
        else if ("schema".equalsIgnoreCase((String)entry.getKey()))
        {
          this.schema = getStrSubstitutor().replace((String)entry.getValue());
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
          createAdvertiser(getStrSubstitutor().replace((String)entry.getValue()), configSource, buffer, "text/xml");
        }
      }
      Iterator<StatusListener> iter = ((StatusLogger)LOGGER).getListeners();
      boolean found = false;
      while (iter.hasNext())
      {
        StatusListener listener = (StatusListener)iter.next();
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
        for (String msg : messages) {
          LOGGER.error(msg);
        }
      }
    }
    catch (SAXException domEx)
    {
      LOGGER.error("Error parsing " + configSource.getLocation(), domEx);
    }
    catch (IOException ioe)
    {
      LOGGER.error("Error parsing " + configSource.getLocation(), ioe);
    }
    catch (ParserConfigurationException pex)
    {
      LOGGER.error("Error parsing " + configSource.getLocation(), pex);
    }
    if ((this.strict) && (this.schema != null) && (buffer != null))
    {
      InputStream is = null;
      try
      {
        is = getClass().getClassLoader().getResourceAsStream(this.schema);
      }
      catch (Exception ex)
      {
        LOGGER.error("Unable to access schema " + this.schema);
      }
      if (is != null)
      {
        Source src = new StreamSource(is, "Log4j-config.xsd");
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = null;
        try
        {
          schema = factory.newSchema(src);
        }
        catch (SAXException ex)
        {
          LOGGER.error("Error parsing Log4j schema", ex);
        }
        if (schema != null)
        {
          Validator validator = schema.newValidator();
          try
          {
            validator.validate(new StreamSource(new ByteArrayInputStream(buffer)));
          }
          catch (IOException ioe)
          {
            LOGGER.error("Error reading configuration for validation", ioe);
          }
          catch (SAXException ex)
          {
            LOGGER.error("Error validating configuration", ex);
          }
        }
      }
    }
    if (getName() == null) {
      setName(configSource.getLocation());
    }
  }
  
  public void setup()
  {
    if (this.rootElement == null)
    {
      LOGGER.error("No logging configuration");
      return;
    }
    constructHierarchy(this.rootNode, this.rootElement);
    if (this.status.size() > 0)
    {
      for (Status s : this.status) {
        LOGGER.error("Error processing element " + s.name + ": " + s.errorType);
      }
      return;
    }
    this.rootElement = null;
  }
  
  public Configuration reconfigure()
  {
    if (this.configFile != null) {
      try
      {
        ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);
        
        return new XMLConfiguration(source);
      }
      catch (FileNotFoundException ex)
      {
        LOGGER.error("Cannot locate file " + this.configFile, ex);
      }
    }
    return null;
  }
  
  private void constructHierarchy(Node node, Element element)
  {
    processAttributes(node, element);
    StringBuilder buffer = new StringBuilder();
    NodeList list = element.getChildNodes();
    List<Node> children = node.getChildren();
    for (int i = 0; i < list.getLength(); i++)
    {
      org.w3c.dom.Node w3cNode = list.item(i);
      if ((w3cNode instanceof Element))
      {
        Element child = (Element)w3cNode;
        String name = getType(child);
        PluginType<?> type = this.pluginManager.getPluginType(name);
        Node childNode = new Node(node, name, type);
        constructHierarchy(childNode, child);
        if (type == null)
        {
          String value = childNode.getValue();
          if ((!childNode.hasChildren()) && (value != null)) {
            node.getAttributes().put(name, value);
          } else {
            this.status.add(new Status(name, element, ErrorType.CLASS_NOT_FOUND));
          }
        }
        else
        {
          children.add(childNode);
        }
      }
      else if ((w3cNode instanceof Text))
      {
        Text data = (Text)w3cNode;
        buffer.append(data.getData());
      }
    }
    String text = buffer.toString().trim();
    if ((text.length() > 0) || ((!node.hasChildren()) && (!node.isRoot()))) {
      node.setValue(text);
    }
  }
  
  private String getType(Element element)
  {
    if (this.strict)
    {
      NamedNodeMap attrs = element.getAttributes();
      for (int i = 0; i < attrs.getLength(); i++)
      {
        org.w3c.dom.Node w3cNode = attrs.item(i);
        if ((w3cNode instanceof Attr))
        {
          Attr attr = (Attr)w3cNode;
          if (attr.getName().equalsIgnoreCase("type"))
          {
            String type = attr.getValue();
            attrs.removeNamedItem(attr.getName());
            return type;
          }
        }
      }
    }
    return element.getTagName();
  }
  
  private byte[] toByteArray(InputStream is)
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
  
  private Map<String, String> processAttributes(Node node, Element element)
  {
    NamedNodeMap attrs = element.getAttributes();
    Map<String, String> attributes = node.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++)
    {
      org.w3c.dom.Node w3cNode = attrs.item(i);
      if ((w3cNode instanceof Attr))
      {
        Attr attr = (Attr)w3cNode;
        if (!attr.getName().equals("xml:base")) {
          attributes.put(attr.getName(), attr.getValue());
        }
      }
    }
    return attributes;
  }
  
  private static enum ErrorType
  {
    CLASS_NOT_FOUND;
    
    private ErrorType() {}
  }
  
  private class Status
  {
    private final Element element;
    private final String name;
    private final XMLConfiguration.ErrorType errorType;
    
    public Status(String name, Element element, XMLConfiguration.ErrorType errorType)
    {
      this.name = name;
      this.element = element;
      this.errorType = errorType;
    }
  }
}
