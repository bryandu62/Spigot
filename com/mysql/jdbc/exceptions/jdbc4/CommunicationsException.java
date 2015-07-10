package com.mysql.jdbc.exceptions.jdbc4;

import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.StreamingNotifiable;
import java.sql.SQLRecoverableException;

public class CommunicationsException
  extends SQLRecoverableException
  implements StreamingNotifiable
{
  private String exceptionMessage;
  private boolean streamingResultSetInPlay = false;
  
  public CommunicationsException(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException)
  {
    this.exceptionMessage = SQLError.createLinkFailureMessageBasedOnHeuristics(conn, lastPacketSentTimeMs, lastPacketReceivedTimeMs, underlyingException, this.streamingResultSetInPlay);
    if (underlyingException != null) {
      initCause(underlyingException);
    }
  }
  
  public String getMessage()
  {
    return this.exceptionMessage;
  }
  
  public String getSQLState()
  {
    return "08S01";
  }
  
  public void setWasStreamingResults()
  {
    this.streamingResultSetInPlay = true;
  }
}
