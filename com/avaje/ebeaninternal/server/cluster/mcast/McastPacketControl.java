package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebeaninternal.server.cluster.Packet;
import com.avaje.ebeaninternal.server.cluster.PacketMessages;
import java.io.DataInput;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class McastPacketControl
{
  private static final Logger logger = Logger.getLogger(McastPacketControl.class.getName());
  private final String localSenderHostPort;
  private final McastClusterManager owner;
  private final HashSet<String> groupMembers = new HashSet();
  private final OutgoingPacketsAcked outgoingPacketsAcked = new OutgoingPacketsAcked();
  private final IncomingPacketsProcessed incomingPacketsProcessed;
  
  public McastPacketControl(McastClusterManager owner, String localSenderHostPort, int maxResendIncoming)
  {
    this.owner = owner;
    this.localSenderHostPort = localSenderHostPort;
    this.incomingPacketsProcessed = new IncomingPacketsProcessed(maxResendIncoming);
  }
  
  protected void onListenerTimeout()
  {
    if (this.groupMembers.size() == 0) {
      this.owner.fromListenerTimeoutNoMembers();
    }
  }
  
  protected void processMessagesPacket(String senderHostPort, Packet header, DataInput dataInput, long totalPacketsReceived, long totalBytesReceived, long totalTransEventsReceived)
    throws IOException
  {
    PacketMessages packetMessages = PacketMessages.forRead(header);
    packetMessages.read(dataInput);
    List<Message> messages = packetMessages.getMessages();
    if (logger.isLoggable(Level.FINER)) {
      logger.finer("INCOMING Messages " + messages);
    }
    MessageControl control = null;
    MessageAck ack = null;
    MessageResend resend = null;
    for (int i = 0; i < messages.size(); i++)
    {
      Message message = (Message)messages.get(i);
      if (message.isControlMessage()) {
        control = (MessageControl)message;
      } else if (this.localSenderHostPort.equals(message.getToHostPort())) {
        if ((message instanceof MessageAck)) {
          ack = (MessageAck)message;
        } else if ((message instanceof MessageResend)) {
          resend = (MessageResend)message;
        } else {
          logger.log(Level.SEVERE, "Expecting a MessageAck or MessageResend but got a " + message.getClass().getName());
        }
      }
    }
    if (control != null) {
      if (control.getControlType() == 2)
      {
        this.groupMembers.remove(senderHostPort);
        logger.info("Cluster member leaving [" + senderHostPort + "] " + this.groupMembers.size() + " other members left");
        
        this.outgoingPacketsAcked.removeMember(senderHostPort);
        this.incomingPacketsProcessed.removeMember(senderHostPort);
      }
      else
      {
        this.groupMembers.add(senderHostPort);
      }
    }
    long newMin = 0L;
    if (ack != null) {
      newMin = this.outgoingPacketsAcked.receivedAck(senderHostPort, ack);
    }
    if ((newMin > 0L) || (control != null) || (resend != null))
    {
      int groupSize = this.groupMembers.size();
      
      this.owner.fromListener(newMin, control, resend, groupSize, totalPacketsReceived, totalBytesReceived, totalTransEventsReceived);
    }
  }
  
  public boolean isProcessPacket(String memberKey, long packetId)
  {
    return this.incomingPacketsProcessed.isProcessPacket(memberKey, packetId);
  }
  
  public AckResendMessages getAckResendMessages(IncomingPacketsLastAck lastAck)
  {
    return this.incomingPacketsProcessed.getAckResendMessages(lastAck);
  }
}
