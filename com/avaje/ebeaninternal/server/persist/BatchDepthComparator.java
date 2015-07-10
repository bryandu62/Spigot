package com.avaje.ebeaninternal.server.persist;

import java.io.Serializable;
import java.util.Comparator;

public class BatchDepthComparator
  implements Comparator<BatchedBeanHolder>, Serializable
{
  private static final long serialVersionUID = 264611821665757991L;
  
  public int compare(BatchedBeanHolder b1, BatchedBeanHolder b2)
  {
    if (b1.getOrder() < b2.getOrder()) {
      return -1;
    }
    if (b1.getOrder() == b2.getOrder()) {
      return 0;
    }
    return 1;
  }
}
