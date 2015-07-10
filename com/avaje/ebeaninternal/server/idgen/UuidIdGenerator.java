package com.avaje.ebeaninternal.server.idgen;

import com.avaje.ebean.Transaction;
import com.avaje.ebean.config.dbplatform.IdGenerator;
import java.util.UUID;

public class UuidIdGenerator
  implements IdGenerator
{
  public Object nextId(Transaction t)
  {
    return UUID.randomUUID();
  }
  
  public String getName()
  {
    return "uuid";
  }
  
  public boolean isDbSequence()
  {
    return false;
  }
  
  public void preAllocateIds(int allocateSize) {}
}
