package com.avaje.ebeaninternal.server.ldap;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.core.ConcurrencyMode;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanManager;
import java.util.Set;
import javax.naming.ldap.LdapName;

public class LdapPersistBeanRequest<T>
  extends PersistRequestBean<T>
{
  private final DefaultLdapPersister persister;
  
  public LdapPersistBeanRequest(SpiEbeanServer server, T bean, Object parentBean, BeanManager<T> mgr, DefaultLdapPersister persister)
  {
    super(server, bean, parentBean, mgr, null, null);
    this.persister = persister;
  }
  
  public LdapPersistBeanRequest(SpiEbeanServer server, T bean, Object parentBean, BeanManager<T> mgr, DefaultLdapPersister persister, Set<String> updateProps, ConcurrencyMode concurrencyMode)
  {
    super(server, bean, parentBean, mgr, null, null, updateProps, concurrencyMode);
    this.persister = persister;
  }
  
  public LdapName createLdapName()
  {
    return this.beanDescriptor.createLdapName(this.bean);
  }
  
  public int executeNow()
  {
    return this.persister.persist(this);
  }
  
  public int executeOrQueue()
  {
    return executeNow();
  }
  
  public void initTransIfRequired() {}
  
  public void commitTransIfRequired() {}
  
  public void rollbackTransIfRequired() {}
}
