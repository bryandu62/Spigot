package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.event.BeanPersistListener;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import com.avaje.ebeaninternal.server.core.PersistRequest.Type;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BeanPersistIds
  implements Serializable
{
  private static final long serialVersionUID = 8389469180931531409L;
  private transient BeanDescriptor<?> beanDescriptor;
  private final String descriptorId;
  private ArrayList<Serializable> insertIds;
  private ArrayList<Serializable> updateIds;
  private ArrayList<Serializable> deleteIds;
  
  public BeanPersistIds(BeanDescriptor<?> desc)
  {
    this.beanDescriptor = desc;
    this.descriptorId = desc.getDescriptorId();
  }
  
  public static BeanPersistIds readBinaryMessage(SpiEbeanServer server, DataInput dataInput)
    throws IOException
  {
    String descriptorId = dataInput.readUTF();
    BeanDescriptor<?> desc = server.getBeanDescriptorById(descriptorId);
    BeanPersistIds bp = new BeanPersistIds(desc);
    bp.read(dataInput);
    return bp;
  }
  
  private void read(DataInput dataInput)
    throws IOException
  {
    IdBinder idBinder = this.beanDescriptor.getIdBinder();
    
    int iudType = dataInput.readInt();
    ArrayList<Serializable> idList = readIdList(dataInput, idBinder);
    switch (iudType)
    {
    case 0: 
      this.insertIds = idList;
      break;
    case 1: 
      this.updateIds = idList;
      break;
    case 2: 
      this.deleteIds = idList;
      break;
    default: 
      throw new RuntimeException("Invalid iudType " + iudType);
    }
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    writeIdList(this.beanDescriptor, 0, this.insertIds, msgList);
    writeIdList(this.beanDescriptor, 1, this.updateIds, msgList);
    writeIdList(this.beanDescriptor, 2, this.deleteIds, msgList);
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
  
  private void writeIdList(BeanDescriptor<?> desc, int iudType, ArrayList<Serializable> idList, BinaryMessageList msgList)
    throws IOException
  {
    IdBinder idBinder = desc.getIdBinder();
    
    int count = idList == null ? 0 : idList.size();
    if (count > 0)
    {
      int loop = 0;
      int i = 0;
      int eof = idList.size();
      do
      {
        loop++;
        int endOfLoop = Math.min(eof, loop * 100);
        
        BinaryMessage m = new BinaryMessage(endOfLoop * 4 + 20);
        
        DataOutputStream os = m.getOs();
        os.writeInt(1);
        os.writeUTF(this.descriptorId);
        os.writeInt(iudType);
        os.writeInt(count);
        for (; i < endOfLoop; i++)
        {
          Serializable idValue = (Serializable)idList.get(i);
          idBinder.writeData(os, idValue);
        }
        os.flush();
        msgList.add(m);
      } while (i < eof);
    }
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if (this.beanDescriptor != null) {
      sb.append(this.beanDescriptor.getFullName());
    } else {
      sb.append("descId:").append(this.descriptorId);
    }
    if (this.insertIds != null) {
      sb.append(" insertIds:").append(this.insertIds);
    }
    if (this.updateIds != null) {
      sb.append(" updateIds:").append(this.updateIds);
    }
    if (this.deleteIds != null) {
      sb.append(" deleteIds:").append(this.deleteIds);
    }
    return sb.toString();
  }
  
  public void addId(PersistRequest.Type type, Serializable id)
  {
    switch (type)
    {
    case INSERT: 
      addInsertId(id);
      break;
    case UPDATE: 
      addUpdateId(id);
      break;
    case DELETE: 
      addDeleteId(id);
      break;
    }
  }
  
  private void addInsertId(Serializable id)
  {
    if (this.insertIds == null) {
      this.insertIds = new ArrayList();
    }
    this.insertIds.add(id);
  }
  
  private void addUpdateId(Serializable id)
  {
    if (this.updateIds == null) {
      this.updateIds = new ArrayList();
    }
    this.updateIds.add(id);
  }
  
  private void addDeleteId(Serializable id)
  {
    if (this.deleteIds == null) {
      this.deleteIds = new ArrayList();
    }
    this.deleteIds.add(id);
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public String getDescriptorId()
  {
    return this.descriptorId;
  }
  
  public List<Serializable> getInsertIds()
  {
    return this.insertIds;
  }
  
  public List<Serializable> getUpdateIds()
  {
    return this.updateIds;
  }
  
  public List<Serializable> getDeleteIds()
  {
    return this.deleteIds;
  }
  
  public void setBeanDescriptor(BeanDescriptor<?> beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
  }
  
  public void notifyCacheAndListener()
  {
    BeanPersistListener<?> listener = this.beanDescriptor.getPersistListener();
    
    this.beanDescriptor.queryCacheClear();
    if ((this.insertIds != null) && 
      (listener != null)) {
      for (int i = 0; i < this.insertIds.size(); i++) {
        listener.remoteInsert(this.insertIds.get(i));
      }
    }
    if (this.updateIds != null) {
      for (int i = 0; i < this.updateIds.size(); i++)
      {
        Serializable id = (Serializable)this.updateIds.get(i);
        
        this.beanDescriptor.cacheRemove(id);
        if (listener != null) {
          listener.remoteInsert(id);
        }
      }
    }
    if (this.deleteIds != null) {
      for (int i = 0; i < this.deleteIds.size(); i++)
      {
        Serializable id = (Serializable)this.deleteIds.get(i);
        
        this.beanDescriptor.cacheRemove(id);
        if (listener != null) {
          listener.remoteInsert(id);
        }
      }
    }
  }
}
