package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.persist.BeanPersister;
import com.avaje.ebeaninternal.server.persist.BeanPersisterFactory;

public class DmlBeanPersisterFactory
  implements BeanPersisterFactory
{
  private final MetaFactory metaFactory;
  
  public DmlBeanPersisterFactory(DatabasePlatform dbPlatform)
  {
    this.metaFactory = new MetaFactory(dbPlatform);
  }
  
  public BeanPersister create(BeanDescriptor<?> desc)
  {
    UpdateMeta updMeta = this.metaFactory.createUpdate(desc);
    DeleteMeta delMeta = this.metaFactory.createDelete(desc);
    InsertMeta insMeta = this.metaFactory.createInsert(desc);
    
    return new DmlBeanPersister(updMeta, insMeta, delMeta);
  }
}
