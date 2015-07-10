package com.avaje.ebeaninternal.server.lib.sql;

import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.server.lib.BackgroundRunnable;
import com.avaje.ebeaninternal.server.lib.BackgroundThread;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSourceManager
  implements DataSourceNotify
{
  private static final Logger logger = Logger.getLogger(DataSourceManager.class.getName());
  private final DataSourceAlertListener alertlistener;
  private final Hashtable<String, DataSourcePool> dsMap = new Hashtable();
  private final Object monitor = new Object();
  private final BackgroundRunnable dbChecker;
  private final int dbUpFreqInSecs;
  private final int dbDownFreqInSecs;
  private boolean shuttingDown;
  private boolean deregisterDriver;
  
  public DataSourceManager()
  {
    this.alertlistener = createAlertListener();
    
    this.dbUpFreqInSecs = GlobalProperties.getInt("datasource.heartbeatfreq", 30);
    this.dbDownFreqInSecs = GlobalProperties.getInt("datasource.deadbeatfreq", 10);
    this.dbChecker = new BackgroundRunnable(new Checker(null), this.dbUpFreqInSecs);
    this.deregisterDriver = GlobalProperties.getBoolean("datasource.deregisterDriver", true);
    try
    {
      BackgroundThread.add(this.dbChecker);
    }
    catch (Exception e)
    {
      logger.log(Level.SEVERE, null, e);
    }
  }
  
  private DataSourceAlertListener createAlertListener()
    throws DataSourceException
  {
    String alertCN = GlobalProperties.get("datasource.alert.class", null);
    if (alertCN == null) {
      return new SimpleAlerter();
    }
    try
    {
      return (DataSourceAlertListener)ClassUtil.newInstance(alertCN, getClass());
    }
    catch (Exception ex)
    {
      throw new DataSourceException(ex);
    }
  }
  
  public void notifyDataSourceUp(String dataSourceName)
  {
    this.dbChecker.setFreqInSecs(this.dbUpFreqInSecs);
    if (this.alertlistener != null) {
      this.alertlistener.dataSourceUp(dataSourceName);
    }
  }
  
  public void notifyDataSourceDown(String dataSourceName)
  {
    this.dbChecker.setFreqInSecs(this.dbDownFreqInSecs);
    if (this.alertlistener != null) {
      this.alertlistener.dataSourceDown(dataSourceName);
    }
  }
  
  public void notifyWarning(String subject, String msg)
  {
    if (this.alertlistener != null) {
      this.alertlistener.warning(subject, msg);
    }
  }
  
  /* Error */
  public boolean isShuttingDown()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 42	com/avaje/ebeaninternal/server/lib/sql/DataSourceManager:monitor	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 158	com/avaje/ebeaninternal/server/lib/sql/DataSourceManager:shuttingDown	Z
    //   11: aload_1
    //   12: monitorexit
    //   13: ireturn
    //   14: astore_2
    //   15: aload_1
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    // Line number table:
    //   Java source line #153	-> byte code offset #0
    //   Java source line #154	-> byte code offset #7
    //   Java source line #155	-> byte code offset #14
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	19	0	this	DataSourceManager
    //   5	11	1	Ljava/lang/Object;	Object
    //   14	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	13	14	finally
    //   14	17	14	finally
  }
  
  public void shutdown()
  {
    synchronized (this.monitor)
    {
      this.shuttingDown = true;
      
      Collection<DataSourcePool> values = this.dsMap.values();
      for (DataSourcePool ds : values) {
        try
        {
          ds.shutdown();
        }
        catch (DataSourceException e)
        {
          logger.log(Level.SEVERE, null, e);
        }
      }
      if (this.deregisterDriver) {
        for (DataSourcePool ds : values) {
          ds.deregisterDriver();
        }
      }
    }
  }
  
  public List<DataSourcePool> getPools()
  {
    synchronized (this.monitor)
    {
      ArrayList<DataSourcePool> list = new ArrayList();
      list.addAll(this.dsMap.values());
      return list;
    }
  }
  
  public DataSourcePool getDataSource(String name)
  {
    return getDataSource(name, null);
  }
  
  public DataSourcePool getDataSource(String name, DataSourceConfig dsConfig)
  {
    if (name == null) {
      throw new IllegalArgumentException("name not defined");
    }
    synchronized (this.monitor)
    {
      DataSourcePool pool = (DataSourcePool)this.dsMap.get(name);
      if (pool == null)
      {
        if (dsConfig == null)
        {
          dsConfig = new DataSourceConfig();
          dsConfig.loadSettings(name);
        }
        pool = new DataSourcePool(this, name, dsConfig);
        this.dsMap.put(name, pool);
      }
      return pool;
    }
  }
  
  private void checkDataSource()
  {
    synchronized (this.monitor)
    {
      if (!isShuttingDown())
      {
        Iterator<DataSourcePool> it = this.dsMap.values().iterator();
        while (it.hasNext())
        {
          DataSourcePool ds = (DataSourcePool)it.next();
          ds.checkDataSource();
        }
      }
    }
  }
  
  private final class Checker
    implements Runnable
  {
    private Checker() {}
    
    public void run()
    {
      DataSourceManager.this.checkDataSource();
    }
  }
}
