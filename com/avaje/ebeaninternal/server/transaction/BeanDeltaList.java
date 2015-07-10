package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeanDeltaList
{
  private final BeanDescriptor<?> beanDescriptor;
  private final List<BeanDelta> deltaBeans = new ArrayList();
  
  public BeanDeltaList(BeanDescriptor<?> beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
  }
  
  public String toString()
  {
    return this.deltaBeans.toString();
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public void add(BeanDelta b)
  {
    this.deltaBeans.add(b);
  }
  
  public List<BeanDelta> getDeltaBeans()
  {
    return this.deltaBeans;
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    for (int i = 0; i < this.deltaBeans.size(); i++) {
      ((BeanDelta)this.deltaBeans.get(i)).writeBinaryMessage(msgList);
    }
  }
}
