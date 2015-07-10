package com.avaje.ebeaninternal.server.cluster.mcast;

import java.util.HashMap;
import java.util.Map;

public class OutgoingPacketsAcked
{
  private long minimumGotAllPacketId;
  private Map<String, GroupMemberAck> recievedByMap;
  
  public OutgoingPacketsAcked()
  {
    this.recievedByMap = new HashMap();
  }
  
  /* Error */
  public int getGroupSize()
  {
    // Byte code:
    //   0: aload_0
    //   1: dup
    //   2: astore_1
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield 24	com/avaje/ebeaninternal/server/cluster/mcast/OutgoingPacketsAcked:recievedByMap	Ljava/util/Map;
    //   8: invokeinterface 33 1 0
    //   13: aload_1
    //   14: monitorexit
    //   15: ireturn
    //   16: astore_2
    //   17: aload_1
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Line number table:
    //   Java source line #32	-> byte code offset #0
    //   Java source line #33	-> byte code offset #4
    //   Java source line #34	-> byte code offset #16
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	21	0	this	OutgoingPacketsAcked
    //   2	16	1	Ljava/lang/Object;	Object
    //   16	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   4	15	16	finally
    //   16	19	16	finally
  }
  
  /* Error */
  public long getMinimumGotAllPacketId()
  {
    // Byte code:
    //   0: aload_0
    //   1: dup
    //   2: astore_1
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield 37	com/avaje/ebeaninternal/server/cluster/mcast/OutgoingPacketsAcked:minimumGotAllPacketId	J
    //   8: aload_1
    //   9: monitorexit
    //   10: lreturn
    //   11: astore_2
    //   12: aload_1
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Line number table:
    //   Java source line #38	-> byte code offset #0
    //   Java source line #39	-> byte code offset #4
    //   Java source line #40	-> byte code offset #11
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	16	0	this	OutgoingPacketsAcked
    //   2	11	1	Ljava/lang/Object;	Object
    //   11	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   4	10	11	finally
    //   11	14	11	finally
  }
  
  public void removeMember(String groupMember)
  {
    synchronized (this)
    {
      this.recievedByMap.remove(groupMember);
      resetGotAllMin();
    }
  }
  
  private boolean resetGotAllMin()
  {
    long tempMin;
    long tempMin;
    if (this.recievedByMap.isEmpty()) {
      tempMin = Long.MAX_VALUE;
    } else {
      tempMin = Long.MAX_VALUE;
    }
    for (GroupMemberAck groupMemAck : this.recievedByMap.values())
    {
      long memberMin = groupMemAck.getGotAllPacketId();
      if (memberMin < tempMin) {
        tempMin = memberMin;
      }
    }
    if (tempMin != this.minimumGotAllPacketId)
    {
      this.minimumGotAllPacketId = tempMin;
      return true;
    }
    return false;
  }
  
  public long receivedAck(String groupMember, MessageAck ack)
  {
    synchronized (this)
    {
      boolean checkMin = false;
      
      GroupMemberAck groupMemberAck = (GroupMemberAck)this.recievedByMap.get(groupMember);
      if (groupMemberAck == null)
      {
        groupMemberAck = new GroupMemberAck(null);
        groupMemberAck.setIfBigger(ack.getGotAllPacketId());
        this.recievedByMap.put(groupMember, groupMemberAck);
        checkMin = true;
      }
      else
      {
        checkMin = groupMemberAck.getGotAllPacketId() == this.minimumGotAllPacketId;
        
        groupMemberAck.setIfBigger(ack.getGotAllPacketId());
      }
      boolean minChanged = false;
      if ((checkMin) || (this.minimumGotAllPacketId == 0L)) {
        minChanged = resetGotAllMin();
      }
      return minChanged ? this.minimumGotAllPacketId : 0L;
    }
  }
  
  private static class GroupMemberAck
  {
    private long gotAllPacketId;
    
    private long getGotAllPacketId()
    {
      return this.gotAllPacketId;
    }
    
    private void setIfBigger(long newGotAll)
    {
      if (newGotAll > this.gotAllPacketId) {
        this.gotAllPacketId = newGotAll;
      }
    }
  }
}
