package com.avaje.ebeaninternal.server.lib;

import com.avaje.ebean.common.BootupEbeanManager;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.server.lib.sql.DataSourceGlobalManager;
import com.avaje.ebeaninternal.server.lib.thread.ThreadPoolManager;
import java.io.PrintStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ShutdownManager
{
  private static final Logger logger = Logger.getLogger(BackgroundThread.class.getName());
  static final Vector<Runnable> runnables = new Vector();
  static boolean stopping;
  static BootupEbeanManager serverFactory;
  static final ShutdownHook shutdownHook = new ShutdownHook();
  
  static
  {
    register();
  }
  
  static boolean whyShutdown = GlobalProperties.getBoolean("debug.shutdown.why", false);
  
  public static void registerServerFactory(BootupEbeanManager factory)
  {
    serverFactory = factory;
  }
  
  private static void deregister()
  {
    synchronized (runnables)
    {
      try
      {
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
      }
      catch (IllegalStateException ex)
      {
        if (!ex.getMessage().equals("Shutdown in progress")) {
          throw ex;
        }
      }
    }
  }
  
  private static void register()
  {
    synchronized (runnables)
    {
      try
      {
        Runtime.getRuntime().addShutdownHook(shutdownHook);
      }
      catch (IllegalStateException ex)
      {
        if (!ex.getMessage().equals("Shutdown in progress")) {
          throw ex;
        }
      }
    }
  }
  
  public static void shutdown()
  {
    synchronized (runnables)
    {
      if (stopping) {
        return;
      }
      if (whyShutdown) {
        try
        {
          throw new RuntimeException("debug.shutdown.why=true ...");
        }
        catch (Throwable e)
        {
          logger.log(Level.WARNING, "Stacktrace showing why shutdown was fired", e);
        }
      }
      stopping = true;
      
      deregister();
      
      BackgroundThread.shutdown();
      
      String shutdownRunner = GlobalProperties.get("system.shutdown.runnable", null);
      if (shutdownRunner != null) {
        try
        {
          Runnable r = (Runnable)ClassUtil.newInstance(shutdownRunner);
          r.run();
        }
        catch (Exception e)
        {
          logger.log(Level.SEVERE, null, e);
        }
      }
      Enumeration<Runnable> e = runnables.elements();
      while (e.hasMoreElements()) {
        try
        {
          Runnable r = (Runnable)e.nextElement();
          r.run();
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
          ex.printStackTrace();
        }
      }
      try
      {
        if (serverFactory != null) {
          serverFactory.shutdown();
        }
        ThreadPoolManager.shutdown();
        
        DataSourceGlobalManager.shutdown();
        
        boolean dereg = GlobalProperties.getBoolean("datasource.deregisterAllDrivers", false);
        if (dereg) {
          deregisterAllJdbcDrivers();
        }
      }
      catch (Exception ex)
      {
        String msg = "Shutdown Exception: " + ex.getMessage();
        System.err.println(msg);
        ex.printStackTrace();
        try
        {
          logger.log(Level.SEVERE, null, ex);
        }
        catch (Exception exc)
        {
          String ms = "Error Logging error to the Log. It may be shutting down.";
          System.err.println(ms);
          exc.printStackTrace();
        }
      }
    }
  }
  
  private static void deregisterAllJdbcDrivers()
  {
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements())
    {
      Driver driver = (Driver)drivers.nextElement();
      try
      {
        DriverManager.deregisterDriver(driver);
        logger.log(Level.INFO, String.format("Deregistering jdbc driver: %s", new Object[] { driver }));
      }
      catch (SQLException e)
      {
        logger.log(Level.SEVERE, String.format("Error deregistering driver %s", new Object[] { driver }), e);
      }
    }
  }
  
  public static void register(Runnable runnable)
  {
    synchronized (runnables)
    {
      runnables.add(runnable);
    }
  }
  
  public static void touch() {}
  
  /* Error */
  public static boolean isStopping()
  {
    // Byte code:
    //   0: getstatic 33	com/avaje/ebeaninternal/server/lib/ShutdownManager:runnables	Ljava/util/Vector;
    //   3: dup
    //   4: astore_0
    //   5: monitorenter
    //   6: getstatic 35	com/avaje/ebeaninternal/server/lib/ShutdownManager:stopping	Z
    //   9: aload_0
    //   10: monitorexit
    //   11: ireturn
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Line number table:
    //   Java source line #80	-> byte code offset #0
    //   Java source line #81	-> byte code offset #6
    //   Java source line #82	-> byte code offset #12
    // Local variable table:
    //   start	length	slot	name	signature
    //   4	10	0	Ljava/lang/Object;	Object
    //   12	4	1	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	11	12	finally
    //   12	15	12	finally
  }
}
