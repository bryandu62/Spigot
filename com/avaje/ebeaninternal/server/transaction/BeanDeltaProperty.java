package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.type.ScalarType;
import java.io.DataOutputStream;
import java.io.IOException;

public class BeanDeltaProperty
{
  private final BeanProperty beanProperty;
  private final Object value;
  
  public BeanDeltaProperty(BeanProperty beanProperty, Object value)
  {
    this.beanProperty = beanProperty;
    this.value = value;
  }
  
  public String toString()
  {
    return this.beanProperty.getName() + ":" + this.value;
  }
  
  public void apply(Object bean)
  {
    this.beanProperty.setValue(bean, this.value);
  }
  
  public void writeBinaryMessage(BinaryMessage m)
    throws IOException
  {
    DataOutputStream os = m.getOs();
    os.writeUTF(this.beanProperty.getName());
    this.beanProperty.getScalarType().writeData(os, this.value);
  }
}
