package org.apache.logging.log4j.core.appender;

public class TLSSyslogFrame
{
  public static final char SPACE = ' ';
  private String message;
  private int messageLengthInBytes;
  
  public TLSSyslogFrame(String message)
  {
    setMessage(message);
  }
  
  public String getMessage()
  {
    return this.message;
  }
  
  public void setMessage(String message)
  {
    this.message = message;
    setLengthInBytes();
  }
  
  private void setLengthInBytes()
  {
    this.messageLengthInBytes = this.message.length();
  }
  
  public byte[] getBytes()
  {
    String frame = toString();
    return frame.getBytes();
  }
  
  public String toString()
  {
    String length = Integer.toString(this.messageLengthInBytes);
    return length + ' ' + this.message;
  }
  
  public boolean equals(Object frame)
  {
    return super.equals(frame);
  }
  
  public boolean equals(TLSSyslogFrame frame)
  {
    return (isLengthEquals(frame)) && (isMessageEquals(frame));
  }
  
  private boolean isLengthEquals(TLSSyslogFrame frame)
  {
    return this.messageLengthInBytes == frame.messageLengthInBytes;
  }
  
  private boolean isMessageEquals(TLSSyslogFrame frame)
  {
    return this.message.equals(frame.message);
  }
}
