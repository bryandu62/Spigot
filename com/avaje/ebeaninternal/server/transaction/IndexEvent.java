package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

public class IndexEvent
{
  public static final int COMMIT_EVENT = 1;
  public static final int OPTIMISE_EVENT = 2;
  private final int eventType;
  private final String indexName;
  
  public IndexEvent(int eventType, String indexName)
  {
    this.eventType = eventType;
    this.indexName = indexName;
  }
  
  public int getEventType()
  {
    return this.eventType;
  }
  
  public String getIndexName()
  {
    return this.indexName;
  }
  
  public static IndexEvent readBinaryMessage(DataInput dataInput)
    throws IOException
  {
    int eventType = dataInput.readInt();
    String indexName = dataInput.readUTF();
    return new IndexEvent(eventType, indexName);
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    BinaryMessage msg = new BinaryMessage(this.indexName.length() + 10);
    DataOutputStream os = msg.getOs();
    os.writeInt(7);
    os.writeInt(this.eventType);
    os.writeUTF(this.indexName);
    
    msgList.add(msg);
  }
}
