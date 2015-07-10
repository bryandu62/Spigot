package org.apache.logging.log4j.message;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ThreadDumpMessage
  implements Message
{
  private static final long serialVersionUID = -1103400781608841088L;
  
  static
  {
    Method[] methods = ThreadInfo.class.getMethods();
    boolean basic = true;
    for (Method method : methods) {
      if (method.getName().equals("getLockInfo"))
      {
        basic = false;
        break;
      }
    }
  }
  
  private static final ThreadInfoFactory FACTORY = basic ? new BasicThreadInfoFactory(null) : new ExtendedThreadInfoFactory(null);
  private volatile Map<ThreadInformation, StackTraceElement[]> threads;
  private final String title;
  private String formattedMessage;
  
  public ThreadDumpMessage(String title)
  {
    this.title = (title == null ? "" : title);
    this.threads = FACTORY.createThreadInfo();
  }
  
  private ThreadDumpMessage(String formattedMsg, String title)
  {
    this.formattedMessage = formattedMsg;
    this.title = (title == null ? "" : title);
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder("ThreadDumpMessage[");
    if (this.title.length() > 0) {
      sb.append("Title=\"").append(this.title).append("\"");
    }
    sb.append("]");
    return sb.toString();
  }
  
  public String getFormattedMessage()
  {
    if (this.formattedMessage != null) {
      return this.formattedMessage;
    }
    StringBuilder sb = new StringBuilder(this.title);
    if (this.title.length() > 0) {
      sb.append("\n");
    }
    for (Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet())
    {
      ThreadInformation info = (ThreadInformation)entry.getKey();
      info.printThreadInfo(sb);
      info.printStack(sb, (StackTraceElement[])entry.getValue());
      sb.append("\n");
    }
    return sb.toString();
  }
  
  public String getFormat()
  {
    return this.title == null ? "" : this.title;
  }
  
  public Object[] getParameters()
  {
    return null;
  }
  
  protected Object writeReplace()
  {
    return new ThreadDumpMessageProxy(this);
  }
  
  private void readObject(ObjectInputStream stream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Proxy required");
  }
  
  private static class ThreadDumpMessageProxy
    implements Serializable
  {
    private static final long serialVersionUID = -3476620450287648269L;
    private final String formattedMsg;
    private final String title;
    
    public ThreadDumpMessageProxy(ThreadDumpMessage msg)
    {
      this.formattedMsg = msg.getFormattedMessage();
      this.title = msg.title;
    }
    
    protected Object readResolve()
    {
      return new ThreadDumpMessage(this.formattedMsg, this.title, null);
    }
  }
  
  private static abstract interface ThreadInfoFactory
  {
    public abstract Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
  }
  
  private static class BasicThreadInfoFactory
    implements ThreadDumpMessage.ThreadInfoFactory
  {
    public Map<ThreadInformation, StackTraceElement[]> createThreadInfo()
    {
      Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
      Map<ThreadInformation, StackTraceElement[]> threads = new HashMap(map.size());
      for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
        threads.put(new BasicThreadInformation((Thread)entry.getKey()), entry.getValue());
      }
      return threads;
    }
  }
  
  private static class ExtendedThreadInfoFactory
    implements ThreadDumpMessage.ThreadInfoFactory
  {
    public Map<ThreadInformation, StackTraceElement[]> createThreadInfo()
    {
      ThreadMXBean bean = ManagementFactory.getThreadMXBean();
      ThreadInfo[] array = bean.dumpAllThreads(true, true);
      
      Map<ThreadInformation, StackTraceElement[]> threads = new HashMap(array.length);
      for (ThreadInfo info : array) {
        threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
      }
      return threads;
    }
  }
  
  public Throwable getThrowable()
  {
    return null;
  }
}
