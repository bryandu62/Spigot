package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.PersistRequest;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class BatchedBeanControl
{
  private final HashMap<String, BatchedBeanHolder> beanHoldMap = new HashMap();
  private final SpiTransaction transaction;
  private final BatchControl batchControl;
  private int topOrder;
  
  public BatchedBeanControl(SpiTransaction t, BatchControl batchControl)
  {
    this.transaction = t;
    this.batchControl = batchControl;
  }
  
  public ArrayList<PersistRequest> getPersistList(PersistRequestBean<?> request)
  {
    return getBeanHolder(request).getList(request);
  }
  
  private BatchedBeanHolder getBeanHolder(PersistRequestBean<?> request)
  {
    BeanDescriptor<?> beanDescriptor = request.getBeanDescriptor();
    BatchedBeanHolder batchBeanHolder = (BatchedBeanHolder)this.beanHoldMap.get(beanDescriptor.getFullName());
    if (batchBeanHolder == null)
    {
      int relativeDepth = this.transaction.depth(0);
      if (relativeDepth == 0) {
        this.topOrder += 1;
      }
      int stmtOrder = this.topOrder * 100 + relativeDepth;
      
      batchBeanHolder = new BatchedBeanHolder(this.batchControl, beanDescriptor, stmtOrder);
      this.beanHoldMap.put(beanDescriptor.getFullName(), batchBeanHolder);
    }
    return batchBeanHolder;
  }
  
  public boolean isEmpty()
  {
    return this.beanHoldMap.isEmpty();
  }
  
  public BatchedBeanHolder[] getArray()
  {
    BatchedBeanHolder[] bsArray = new BatchedBeanHolder[this.beanHoldMap.size()];
    this.beanHoldMap.values().toArray(bsArray);
    return bsArray;
  }
}
