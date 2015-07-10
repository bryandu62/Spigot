package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.type.ScalarType;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeanDelta
{
  private final List<BeanDeltaProperty> properties;
  private final BeanDescriptor<?> beanDescriptor;
  private final Object id;
  
  public BeanDelta(BeanDescriptor<?> beanDescriptor, Object id)
  {
    this.beanDescriptor = beanDescriptor;
    this.id = id;
    this.properties = new ArrayList();
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("BeanDelta[");
    sb.append(this.beanDescriptor.getName()).append(":");
    sb.append(this.properties);
    sb.append("]");
    return sb.toString();
  }
  
  public Object getId()
  {
    return this.id;
  }
  
  public void add(BeanProperty beanProperty, Object value)
  {
    this.properties.add(new BeanDeltaProperty(beanProperty, value));
  }
  
  public void add(BeanDeltaProperty propertyDelta)
  {
    this.properties.add(propertyDelta);
  }
  
  public void apply(Object bean)
  {
    for (int i = 0; i < this.properties.size(); i++) {
      ((BeanDeltaProperty)this.properties.get(i)).apply(bean);
    }
  }
  
  public static BeanDelta readBinaryMessage(SpiEbeanServer server, DataInput dataInput)
    throws IOException
  {
    String descriptorId = dataInput.readUTF();
    BeanDescriptor<?> desc = server.getBeanDescriptorById(descriptorId);
    Object id = desc.getIdBinder().readData(dataInput);
    BeanDelta bp = new BeanDelta(desc, id);
    
    int count = dataInput.readInt();
    for (int i = 0; i < count; i++)
    {
      String propName = dataInput.readUTF();
      BeanProperty beanProperty = desc.getBeanProperty(propName);
      Object value = beanProperty.getScalarType().readData(dataInput);
      bp.add(beanProperty, value);
    }
    return bp;
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    BinaryMessage m = new BinaryMessage(50);
    
    DataOutputStream os = m.getOs();
    os.writeInt(3);
    os.writeUTF(this.beanDescriptor.getDescriptorId());
    
    this.beanDescriptor.getIdBinder().writeData(os, this.id);
    os.writeInt(this.properties.size());
    for (int i = 0; i < this.properties.size(); i++) {
      ((BeanDeltaProperty)this.properties.get(i)).writeBinaryMessage(m);
    }
    os.flush();
    msgList.add(m);
  }
}
