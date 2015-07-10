package com.avaje.ebeaninternal.api;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import java.util.ArrayList;
import java.util.List;

public class TransactionEventBeans
{
  ArrayList<PersistRequestBean<?>> requests = new ArrayList();
  
  public List<PersistRequestBean<?>> getRequests()
  {
    return this.requests;
  }
  
  public void add(PersistRequestBean<?> request)
  {
    this.requests.add(request);
  }
  
  public void notifyCache()
  {
    for (int i = 0; i < this.requests.size(); i++) {
      ((PersistRequestBean)this.requests.get(i)).notifyCache();
    }
  }
}
