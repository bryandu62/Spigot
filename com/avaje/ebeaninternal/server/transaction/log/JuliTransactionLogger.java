package com.avaje.ebeaninternal.server.transaction.log;

import com.avaje.ebeaninternal.server.transaction.TransactionLogBuffer;
import com.avaje.ebeaninternal.server.transaction.TransactionLogBuffer.LogEntry;
import com.avaje.ebeaninternal.server.transaction.TransactionLogWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JuliTransactionLogger
  implements TransactionLogWriter
{
  private static Logger logger = Logger.getLogger(JuliTransactionLogger.class.getName());
  
  public void log(TransactionLogBuffer logBuffer)
  {
    String txnId = logBuffer.getTransactionId();
    
    List<TransactionLogBuffer.LogEntry> messages = logBuffer.messages();
    for (int i = 0; i < messages.size(); i++)
    {
      TransactionLogBuffer.LogEntry logEntry = (TransactionLogBuffer.LogEntry)messages.get(i);
      log(txnId, logEntry);
    }
  }
  
  public void shutdown() {}
  
  private void log(String txnId, TransactionLogBuffer.LogEntry entry)
  {
    String message = entry.getMsg();
    if ((txnId != null) && (message != null) && (!message.startsWith("Trans["))) {
      message = "Trans[" + txnId + "] " + message;
    }
    logger.log(Level.INFO, message);
  }
}
