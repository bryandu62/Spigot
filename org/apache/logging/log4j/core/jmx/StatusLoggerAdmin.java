package org.apache.logging.log4j.core.jmx;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

public class StatusLoggerAdmin
  extends NotificationBroadcasterSupport
  implements StatusListener, StatusLoggerAdminMBean
{
  private final AtomicLong sequenceNo = new AtomicLong();
  private final ObjectName objectName;
  private Level level = Level.WARN;
  
  public StatusLoggerAdmin(Executor executor)
  {
    super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
    try
    {
      this.objectName = new ObjectName("org.apache.logging.log4j2:type=StatusLogger");
    }
    catch (Exception e)
    {
      throw new IllegalStateException(e);
    }
    StatusLogger.getLogger().registerListener(this);
  }
  
  private static MBeanNotificationInfo createNotificationInfo()
  {
    String[] notifTypes = { "com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message" };
    
    String name = Notification.class.getName();
    String description = "StatusLogger has logged an event";
    return new MBeanNotificationInfo(notifTypes, name, "StatusLogger has logged an event");
  }
  
  public String[] getStatusDataHistory()
  {
    List<StatusData> data = getStatusData();
    String[] result = new String[data.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = ((StatusData)data.get(i)).getFormattedStatus();
    }
    return result;
  }
  
  public List<StatusData> getStatusData()
  {
    return StatusLogger.getLogger().getStatusData();
  }
  
  public String getLevel()
  {
    return this.level.name();
  }
  
  public Level getStatusLevel()
  {
    return this.level;
  }
  
  public void setLevel(String level)
  {
    this.level = Level.toLevel(level, Level.ERROR);
  }
  
  public void log(StatusData data)
  {
    Notification notifMsg = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", getObjectName(), nextSeqNo(), now(), data.getFormattedStatus());
    
    sendNotification(notifMsg);
    
    Notification notifData = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", getObjectName(), nextSeqNo(), now());
    
    notifData.setUserData(data);
    sendNotification(notifData);
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
