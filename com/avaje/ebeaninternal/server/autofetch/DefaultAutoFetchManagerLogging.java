package com.avaje.ebeaninternal.server.autofetch;

import com.avaje.ebean.config.AutofetchConfig;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.server.lib.BackgroundThread;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.transaction.log.SimpleLogger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultAutoFetchManagerLogging
{
  private static final Logger logger = Logger.getLogger(DefaultAutoFetchManagerLogging.class.getName());
  private final SimpleLogger fileLogger;
  private final DefaultAutoFetchManager manager;
  private final boolean useFileLogger;
  private final boolean traceUsageCollection;
  
  public DefaultAutoFetchManagerLogging(ServerConfig serverConfig, DefaultAutoFetchManager profileListener)
  {
    this.manager = profileListener;
    
    AutofetchConfig autofetchConfig = serverConfig.getAutofetchConfig();
    
    this.traceUsageCollection = GlobalProperties.getBoolean("ebean.autofetch.traceUsageCollection", false);
    this.useFileLogger = autofetchConfig.isUseFileLogging();
    if (!this.useFileLogger)
    {
      this.fileLogger = null;
    }
    else
    {
      String baseDir = serverConfig.getLoggingDirectoryWithEval();
      this.fileLogger = new SimpleLogger(baseDir, "autofetch", true, "csv");
    }
    int updateFreqInSecs = autofetchConfig.getProfileUpdateFrequency();
    
    BackgroundThread.add(updateFreqInSecs, new UpdateProfile(null));
  }
  
  private final class UpdateProfile
    implements Runnable
  {
    private UpdateProfile() {}
    
    public void run()
    {
      DefaultAutoFetchManagerLogging.this.manager.updateTunedQueryInfo();
    }
  }
  
  public void logError(Level level, String msg, Throwable e)
  {
    if (this.useFileLogger)
    {
      String errMsg = e == null ? "" : e.getMessage();
      this.fileLogger.log("\"Error\",\"" + msg + " " + errMsg + "\",,,,");
    }
    logger.log(level, msg, e);
  }
  
  public void logToJavaLogger(String msg)
  {
    logger.info(msg);
  }
  
  public void logSummary(String summaryInfo)
  {
    String msg = "\"Summary\",\"" + summaryInfo + "\",,,,";
    if (this.useFileLogger) {
      this.fileLogger.log(msg);
    }
    logger.fine(msg);
  }
  
  public void logChanged(TunedQueryInfo tunedFetch, OrmQueryDetail newQueryDetail)
  {
    String msg = tunedFetch.getLogOutput(newQueryDetail);
    if (this.useFileLogger) {
      this.fileLogger.log(msg);
    } else {
      logger.fine(msg);
    }
  }
  
  public void logNew(TunedQueryInfo tunedFetch)
  {
    String msg = tunedFetch.getLogOutput(null);
    if (this.useFileLogger) {
      this.fileLogger.log(msg);
    } else {
      logger.fine(msg);
    }
  }
  
  public boolean isTraceUsageCollection()
  {
    return this.traceUsageCollection;
  }
}
