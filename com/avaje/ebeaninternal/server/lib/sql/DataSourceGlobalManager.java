package com.avaje.ebeaninternal.server.lib.sql;

import com.avaje.ebean.config.DataSourceConfig;
import java.util.List;

public final class DataSourceGlobalManager
{
  private static final DataSourceManager manager = new DataSourceManager();
  
  public static boolean isShuttingDown()
  {
    return manager.isShuttingDown();
  }
  
  public static void shutdown()
  {
    manager.shutdown();
  }
  
  public static List<DataSourcePool> getPools()
  {
    return manager.getPools();
  }
  
  public static DataSourcePool getDataSource(String name)
  {
    return manager.getDataSource(name);
  }
  
  public static DataSourcePool getDataSource(String name, DataSourceConfig dsConfig)
  {
    return manager.getDataSource(name, dsConfig);
  }
}
