package com.avaje.ebeaninternal.server.cluster.socket;

public class SocketClusterStatus
{
  private final int currentGroupSize;
  private final int txnIncoming;
  private final int txtOutgoing;
  
  public SocketClusterStatus(int currentGroupSize, int txnIncoming, int txnOutgoing)
  {
    this.currentGroupSize = currentGroupSize;
    this.txnIncoming = txnIncoming;
    this.txtOutgoing = txnOutgoing;
  }
  
  public int getCurrentGroupSize()
  {
    return this.currentGroupSize;
  }
  
  public int getTxnIncoming()
  {
    return this.txnIncoming;
  }
  
  public int getTxtOutgoing()
  {
    return this.txtOutgoing;
  }
}
