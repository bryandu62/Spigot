package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;

public class IndexInvalidate
{
  private final String indexName;
  
  public IndexInvalidate(String indexName)
  {
    this.indexName = indexName;
  }
  
  public String getIndexName()
  {
    return this.indexName;
  }
  
  public int hashCode()
  {
    int hc = IndexInvalidate.class.hashCode();
    hc = hc * 31 + this.indexName.hashCode();
    return hc;
  }
  
  public boolean equals(Object o)
  {
    if (!(o instanceof IndexInvalidate)) {
      return false;
    }
    return this.indexName.equals(((IndexInvalidate)o).indexName);
  }
  
  public static IndexInvalidate readBinaryMessage(DataInput dataInput)
    throws IOException
  {
    String indexName = dataInput.readUTF();
    return new IndexInvalidate(indexName);
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    BinaryMessage msg = new BinaryMessage(this.indexName.length() + 10);
    DataOutputStream os = msg.getOs();
    os.writeInt(6);
    os.writeUTF(this.indexName);
    
    msgList.add(msg);
  }
}
