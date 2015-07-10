package com.avaje.ebean.text.csv;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import java.util.logging.Logger;

public class DefaultCsvCallback<T>
  implements CsvCallback<T>
{
  private static final Logger logger = Logger.getLogger(DefaultCsvCallback.class.getName());
  protected Transaction transaction;
  protected boolean createdTransaction;
  protected EbeanServer server;
  protected int logInfoFrequency;
  protected int persistBatchSize;
  protected long startTime;
  protected long exeTime;
  
  public DefaultCsvCallback()
  {
    this(30, 1000);
  }
  
  public DefaultCsvCallback(int persistBatchSize, int logInfoFrequency)
  {
    this.persistBatchSize = persistBatchSize;
    this.logInfoFrequency = logInfoFrequency;
  }
  
  public void begin(EbeanServer server)
  {
    this.server = server;
    this.startTime = System.currentTimeMillis();
    
    initTransactionIfRequired();
  }
  
  public void readHeader(String[] line) {}
  
  public boolean processLine(int row, String[] line)
  {
    return true;
  }
  
  public void processBean(int row, String[] line, T bean)
  {
    this.server.save(bean, this.transaction);
    if ((this.logInfoFrequency > 0) && (row % this.logInfoFrequency == 0)) {
      logger.info("processed " + row + " rows");
    }
  }
  
  public void end(int row)
  {
    commitTransactionIfCreated();
    
    this.exeTime = (System.currentTimeMillis() - this.startTime);
    logger.info("Csv finished, rows[" + row + "] exeMillis[" + this.exeTime + "]");
  }
  
  public void endWithError(int row, Exception e)
  {
    rollbackTransactionIfCreated(e);
  }
  
  protected void initTransactionIfRequired()
  {
    this.transaction = this.server.currentTransaction();
    if ((this.transaction == null) || (!this.transaction.isActive()))
    {
      this.transaction = this.server.beginTransaction();
      this.createdTransaction = true;
      if (this.persistBatchSize > 1)
      {
        logger.info("Creating transaction, batchSize[" + this.persistBatchSize + "]");
        this.transaction.setBatchMode(true);
        this.transaction.setBatchSize(this.persistBatchSize);
      }
      else
      {
        this.transaction.setBatchMode(false);
        logger.info("Creating transaction with no JDBC batching");
      }
    }
  }
  
  protected void commitTransactionIfCreated()
  {
    if (this.createdTransaction)
    {
      this.transaction.commit();
      logger.info("Committed transaction");
    }
  }
  
  protected void rollbackTransactionIfCreated(Throwable e)
  {
    if (this.createdTransaction)
    {
      this.transaction.rollback(e);
      logger.info("Rolled back transaction");
    }
  }
}
