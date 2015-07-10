package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PropertiesUtil;

@Plugin(name="Console", category="Core", elementType="appender", printObject=true)
public final class ConsoleAppender
  extends AbstractOutputStreamAppender
{
  private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
  private static ConsoleManagerFactory factory = new ConsoleManagerFactory(null);
  
  public static enum Target
  {
    SYSTEM_OUT,  SYSTEM_ERR;
  }
  
  private ConsoleAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions)
  {
    super(name, layout, filter, ignoreExceptions, true, manager);
  }
  
  @PluginFactory
  public static ConsoleAppender createAppender(@PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") Filter filter, @PluginAttribute("target") String t, @PluginAttribute("name") String name, @PluginAttribute("follow") String follow, @PluginAttribute("ignoreExceptions") String ignore)
  {
    if (name == null)
    {
      LOGGER.error("No name provided for ConsoleAppender");
      return null;
    }
    if (layout == null) {
      layout = PatternLayout.createLayout(null, null, null, null, null);
    }
    boolean isFollow = Boolean.parseBoolean(follow);
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    Target target = t == null ? Target.SYSTEM_OUT : Target.valueOf(t);
    return new ConsoleAppender(name, layout, filter, getManager(isFollow, target, layout), ignoreExceptions);
  }
  
  private static OutputStreamManager getManager(boolean follow, Target target, Layout<? extends Serializable> layout)
  {
    String type = target.name();
    OutputStream os = getOutputStream(follow, target);
    return OutputStreamManager.getManager(target.name() + "." + follow, new FactoryData(os, type, layout), factory);
  }
  
  private static OutputStream getOutputStream(boolean follow, Target target)
  {
    String enc = Charset.defaultCharset().name();
    PrintStream printStream = null;
    try
    {
      printStream = 
      
        follow ? new PrintStream(new SystemErrStream(), true, enc) : target == Target.SYSTEM_OUT ? System.out : follow ? new PrintStream(new SystemOutStream(), true, enc) : System.err;
    }
    catch (UnsupportedEncodingException ex)
    {
      throw new IllegalStateException("Unsupported default encoding " + enc, ex);
    }
    PropertiesUtil propsUtil = PropertiesUtil.getProperties();
    if ((!propsUtil.getStringProperty("os.name").startsWith("Windows")) || 
      (propsUtil.getBooleanProperty("log4j.skipJansi"))) {
      return printStream;
    }
    try
    {
      ClassLoader loader = Loader.getClassLoader();
      
      Class<?> clazz = loader.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
      Constructor<?> constructor = clazz.getConstructor(new Class[] { OutputStream.class });
      return (OutputStream)constructor.newInstance(new Object[] { printStream });
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      LOGGER.debug("Jansi is not installed, cannot find {}", new Object[] { "org.fusesource.jansi.WindowsAnsiOutputStream" });
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      LOGGER.warn("{} is missing the proper constructor", new Object[] { "org.fusesource.jansi.WindowsAnsiOutputStream" });
    }
    catch (Throwable localThrowable)
    {
      LOGGER.warn("Unable to instantiate {}", new Object[] { "org.fusesource.jansi.WindowsAnsiOutputStream" });
    }
    return printStream;
  }
  
  private static class SystemErrStream
    extends OutputStream
  {
    public void close() {}
    
    public void flush()
    {
      System.err.flush();
    }
    
    public void write(byte[] b)
      throws IOException
    {
      System.err.write(b);
    }
    
    public void write(byte[] b, int off, int len)
      throws IOException
    {
      System.err.write(b, off, len);
    }
    
    public void write(int b)
    {
      System.err.write(b);
    }
  }
  
  private static class SystemOutStream
    extends OutputStream
  {
    public void close() {}
    
    public void flush()
    {
      System.out.flush();
    }
    
    public void write(byte[] b)
      throws IOException
    {
      System.out.write(b);
    }
    
    public void write(byte[] b, int off, int len)
      throws IOException
    {
      System.out.write(b, off, len);
    }
    
    public void write(int b)
      throws IOException
    {
      System.out.write(b);
    }
  }
  
  private static class FactoryData
  {
    private final OutputStream os;
    private final String type;
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout)
    {
      this.os = os;
      this.type = type;
      this.layout = layout;
    }
  }
  
  private static class ConsoleManagerFactory
    implements ManagerFactory<OutputStreamManager, ConsoleAppender.FactoryData>
  {
    public OutputStreamManager createManager(String name, ConsoleAppender.FactoryData data)
    {
      return new OutputStreamManager(ConsoleAppender.FactoryData.access$0(data), ConsoleAppender.FactoryData.access$1(data), ConsoleAppender.FactoryData.access$2(data));
    }
  }
}
