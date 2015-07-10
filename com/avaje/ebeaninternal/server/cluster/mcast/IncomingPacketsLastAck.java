package com.avaje.ebeaninternal.server.cluster.mcast;

import java.util.HashMap;
import java.util.List;

public class IncomingPacketsLastAck
{
  private HashMap<String, MessageAck> lastAckMap = new HashMap();
  
  public String toString()
  {
    return this.lastAckMap.values().toString();
  }
  
  public void remove(String memberHostPort)
  {
    this.lastAckMap.remove(memberHostPort);
  }
  
  public MessageAck getLastAck(String memberHostPort)
  {
    return (MessageAck)this.lastAckMap.get(memberHostPort);
  }
  
  public void updateLastAck(AckResendMessages ackResendMessages)
  {
    List<Message> messages = ackResendMessages.getMessages();
    for (int i = 0; i < messages.size(); i++)
    {
      Message msg = (Message)messages.get(i);
      if ((msg instanceof MessageAck))
      {
        MessageAck lastAck = (MessageAck)msg;
        this.lastAckMap.put(lastAck.getToHostPort(), lastAck);
      }
    }
  }
}
