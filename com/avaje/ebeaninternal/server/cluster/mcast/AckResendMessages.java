package com.avaje.ebeaninternal.server.cluster.mcast;

import java.util.ArrayList;
import java.util.List;

public class AckResendMessages
{
  ArrayList<Message> messages = new ArrayList();
  
  public String toString()
  {
    return this.messages.toString();
  }
  
  public int size()
  {
    return this.messages.size();
  }
  
  public void add(MessageAck ack)
  {
    this.messages.add(ack);
  }
  
  public void add(MessageResend resend)
  {
    this.messages.add(resend);
  }
  
  public List<Message> getMessages()
  {
    return this.messages;
  }
}
