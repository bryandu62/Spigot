package com.avaje.ebeaninternal.server.cluster.mcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class IncomingPacketsProcessed
{
  private final ConcurrentHashMap<String, GotAllPoint> mapByMember = new ConcurrentHashMap();
  private final int maxResendIncoming;
  
  public IncomingPacketsProcessed(int maxResendIncoming)
  {
    this.maxResendIncoming = maxResendIncoming;
  }
  
  public void removeMember(String memberKey)
  {
    this.mapByMember.remove(memberKey);
  }
  
  public boolean isProcessPacket(String memberKey, long packetId)
  {
    GotAllPoint memberPackets = getMemberPackets(memberKey);
    return memberPackets.processPacket(packetId);
  }
  
  public AckResendMessages getAckResendMessages(IncomingPacketsLastAck lastAck)
  {
    AckResendMessages response = new AckResendMessages();
    for (GotAllPoint member : this.mapByMember.values())
    {
      MessageAck lastAckMessage = lastAck.getLastAck(member.getMemberKey());
      
      member.addAckResendMessages(response, lastAckMessage);
    }
    return response;
  }
  
  private GotAllPoint getMemberPackets(String memberKey)
  {
    GotAllPoint memberGotAllPoint = (GotAllPoint)this.mapByMember.get(memberKey);
    if (memberGotAllPoint == null)
    {
      memberGotAllPoint = new GotAllPoint(memberKey, this.maxResendIncoming);
      this.mapByMember.put(memberKey, memberGotAllPoint);
    }
    return memberGotAllPoint;
  }
  
  public static class GotAllPoint
  {
    private static final Logger logger = Logger.getLogger(GotAllPoint.class.getName());
    private final String memberKey;
    private final int maxResendIncoming;
    private long gotAllPoint;
    private long gotMaxPoint;
    private ArrayList<Long> outOfOrderList = new ArrayList();
    private HashMap<Long, Integer> resendCountMap = new HashMap();
    
    public GotAllPoint(String memberKey, int maxResendIncoming)
    {
      this.memberKey = memberKey;
      this.maxResendIncoming = maxResendIncoming;
    }
    
    public void addAckResendMessages(AckResendMessages response, MessageAck lastAckMessage)
    {
      synchronized (this)
      {
        if ((lastAckMessage == null) || (lastAckMessage.getGotAllPacketId() < this.gotAllPoint)) {
          response.add(new MessageAck(this.memberKey, this.gotAllPoint));
        }
        if (getMissingPacketCount() > 0)
        {
          List<Long> missingPackets = getMissingPackets();
          response.add(new MessageResend(this.memberKey, missingPackets));
        }
      }
    }
    
    public String getMemberKey()
    {
      return this.memberKey;
    }
    
    /* Error */
    public long getGotAllPoint()
    {
      // Byte code:
      //   0: aload_0
      //   1: dup
      //   2: astore_1
      //   3: monitorenter
      //   4: aload_0
      //   5: getfield 56	com/avaje/ebeaninternal/server/cluster/mcast/IncomingPacketsProcessed$GotAllPoint:gotAllPoint	J
      //   8: aload_1
      //   9: monitorexit
      //   10: lreturn
      //   11: astore_2
      //   12: aload_1
      //   13: monitorexit
      //   14: aload_2
      //   15: athrow
      // Line number table:
      //   Java source line #158	-> byte code offset #0
      //   Java source line #159	-> byte code offset #4
      //   Java source line #160	-> byte code offset #11
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	16	0	this	GotAllPoint
      //   2	11	1	Ljava/lang/Object;	Object
      //   11	4	2	localObject1	Object
      // Exception table:
      //   from	to	target	type
      //   4	10	11	finally
      //   11	14	11	finally
    }
    
    /* Error */
    public long getGotMaxPoint()
    {
      // Byte code:
      //   0: aload_0
      //   1: dup
      //   2: astore_1
      //   3: monitorenter
      //   4: aload_0
      //   5: getfield 94	com/avaje/ebeaninternal/server/cluster/mcast/IncomingPacketsProcessed$GotAllPoint:gotMaxPoint	J
      //   8: aload_1
      //   9: monitorexit
      //   10: lreturn
      //   11: astore_2
      //   12: aload_1
      //   13: monitorexit
      //   14: aload_2
      //   15: athrow
      // Line number table:
      //   Java source line #164	-> byte code offset #0
      //   Java source line #165	-> byte code offset #4
      //   Java source line #166	-> byte code offset #11
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	16	0	this	GotAllPoint
      //   2	11	1	Ljava/lang/Object;	Object
      //   11	4	2	localObject1	Object
      // Exception table:
      //   from	to	target	type
      //   4	10	11	finally
      //   11	14	11	finally
    }
    
    private int getMissingPacketCount()
    {
      if (this.gotMaxPoint <= this.gotAllPoint)
      {
        if (!this.resendCountMap.isEmpty()) {
          this.resendCountMap.clear();
        }
        return 0;
      }
      return (int)(this.gotMaxPoint - this.gotAllPoint) - this.outOfOrderList.size();
    }
    
    public List<Long> getMissingPackets()
    {
      synchronized (this)
      {
        ArrayList<Long> missingList = new ArrayList();
        
        boolean lostPacket = false;
        for (long i = this.gotAllPoint + 1L; i < this.gotMaxPoint; i += 1L)
        {
          Long packetId = Long.valueOf(i);
          if (!this.outOfOrderList.contains(packetId)) {
            if (incrementResendCount(packetId)) {
              missingList.add(packetId);
            } else {
              lostPacket = true;
            }
          }
        }
        if (lostPacket) {
          checkOutOfOrderList();
        }
        return missingList;
      }
    }
    
    private boolean incrementResendCount(Long packetId)
    {
      Integer resendCount = (Integer)this.resendCountMap.get(packetId);
      if (resendCount != null)
      {
        int i = resendCount.intValue() + 1;
        if (i > this.maxResendIncoming)
        {
          logger.warning("Exceeded maxResendIncoming[" + this.maxResendIncoming + "] for packet[" + packetId + "]. Giving up on requesting it.");
          this.resendCountMap.remove(packetId);
          this.outOfOrderList.add(packetId);
          return false;
        }
        resendCount = Integer.valueOf(i);
        this.resendCountMap.put(packetId, resendCount);
      }
      else
      {
        this.resendCountMap.put(packetId, ONE);
      }
      return true;
    }
    
    private static final Integer ONE = Integer.valueOf(1);
    
    public boolean processPacket(long packetId)
    {
      synchronized (this)
      {
        if (this.gotAllPoint == 0L)
        {
          this.gotAllPoint = packetId;
          return true;
        }
        if (packetId <= this.gotAllPoint) {
          return false;
        }
        if (!this.resendCountMap.isEmpty()) {
          this.resendCountMap.remove(Long.valueOf(packetId));
        }
        if (packetId == this.gotAllPoint + 1L)
        {
          this.gotAllPoint = packetId;
        }
        else
        {
          if (packetId > this.gotMaxPoint) {
            this.gotMaxPoint = packetId;
          }
          this.outOfOrderList.add(Long.valueOf(packetId));
        }
        checkOutOfOrderList();
        return true;
      }
    }
    
    private void checkOutOfOrderList()
    {
      if (this.outOfOrderList.size() == 0) {
        return;
      }
      boolean continueCheck;
      do
      {
        continueCheck = false;
        long nextPoint = this.gotAllPoint + 1L;
        
        Iterator<Long> it = this.outOfOrderList.iterator();
        while (it.hasNext())
        {
          Long id = (Long)it.next();
          if (id.longValue() == nextPoint)
          {
            it.remove();
            this.gotAllPoint = nextPoint;
            continueCheck = true;
            break;
          }
        }
      } while (continueCheck);
    }
  }
}
