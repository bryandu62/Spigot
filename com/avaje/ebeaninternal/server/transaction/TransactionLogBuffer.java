package com.avaje.ebeaninternal.server.transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionLogBuffer
{
  private final String transactionId;
  private final ArrayList<LogEntry> buffer;
  private final int maxSize;
  private int currentSize;
  
  public TransactionLogBuffer(int maxSize, String transactionId)
  {
    this.maxSize = maxSize;
    this.transactionId = transactionId;
    this.buffer = new ArrayList(maxSize);
  }
  
  public TransactionLogBuffer newBuffer()
  {
    return new TransactionLogBuffer(this.maxSize, this.transactionId);
  }
  
  public String getTransactionId()
  {
    return this.transactionId;
  }
  
  public boolean add(String msg)
  {
    this.buffer.add(new LogEntry(msg));
    return ++this.currentSize >= this.maxSize;
  }
  
  public boolean isEmpty()
  {
    return this.buffer.isEmpty();
  }
  
  public List<LogEntry> messages()
  {
    return this.buffer;
  }
  
  public class LogEntry
  {
    private final long timestamp;
    private final String msg;
    
    public LogEntry(String msg)
    {
      this.timestamp = System.currentTimeMillis();
      this.msg = msg;
    }
    
    public long getTimestamp()
    {
      return this.timestamp;
    }
    
    public String getMsg()
    {
      return this.msg;
    }
  }
}
