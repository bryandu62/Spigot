package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BeanPathUpdateIds
{
  private transient BeanDescriptor<?> beanDescriptor;
  private final String descriptorId;
  private String path;
  private ArrayList<Serializable> ids;
  
  public BeanPathUpdateIds(BeanDescriptor<?> desc, String path)
  {
    this.beanDescriptor = desc;
    this.descriptorId = desc.getDescriptorId();
    this.path = path;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if (this.beanDescriptor != null) {
      sb.append(this.beanDescriptor.getFullName());
    } else {
      sb.append("descId:").append(this.descriptorId);
    }
    sb.append(" path:").append(this.path);
    sb.append(" ids:").append(this.ids);
    return sb.toString();
  }
  
  public static BeanPathUpdateIds readBinaryMessage(SpiEbeanServer server, DataInput dataInput)
    throws IOException
  {
    String descriptorId = dataInput.readUTF();
    String path = dataInput.readUTF();
    BeanDescriptor<?> desc = server.getBeanDescriptorById(descriptorId);
    BeanPathUpdateIds bp = new BeanPathUpdateIds(desc, path);
    bp.read(dataInput);
    return bp;
  }
  
  private void read(DataInput dataInput)
    throws IOException
  {
    IdBinder idBinder = this.beanDescriptor.getIdBinder();
    this.ids = readIdList(dataInput, idBinder);
  }
  
  private ArrayList<Serializable> readIdList(DataInput dataInput, IdBinder idBinder)
    throws IOException
  {
    int count = dataInput.readInt();
    if (count < 1) {
      return null;
    }
    ArrayList<Serializable> idList = new ArrayList(count);
    for (int i = 0; i < count; i++)
    {
      Object id = idBinder.readData(dataInput);
      idList.add((Serializable)id);
    }
    return idList;
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    IdBinder idBinder = this.beanDescriptor.getIdBinder();
    
    int count = this.ids == null ? 0 : this.ids.size();
    if (count > 0)
    {
      int loop = 0;
      int i = 0;
      int eof = this.ids.size();
      do
      {
        loop++;
        int endOfLoop = Math.min(eof, loop * 100);
        
        BinaryMessage m = new BinaryMessage(endOfLoop * 4 + 20);
        
        DataOutputStream os = m.getOs();
        os.writeInt(4);
        os.writeUTF(this.descriptorId);
        os.writeUTF(this.path);
        os.writeInt(count);
        for (; i < endOfLoop; i++)
        {
          Serializable idValue = (Serializable)this.ids.get(i);
          idBinder.writeData(os, idValue);
        }
        os.flush();
        msgList.add(m);
      } while (i < eof);
    }
  }
  
  public void addId(Serializable id)
  {
    this.ids.add(id);
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public String getDescriptorId()
  {
    return this.descriptorId;
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public List<Serializable> getIds()
  {
    return this.ids;
  }
}
