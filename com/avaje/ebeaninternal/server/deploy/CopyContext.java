package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.server.transaction.DefaultPersistenceContext;

public class CopyContext
{
  private final boolean vanillaMode;
  private final boolean sharing;
  private final PersistenceContext pc;
  
  public CopyContext(boolean vanillaMode, boolean sharing)
  {
    this.vanillaMode = vanillaMode;
    this.sharing = sharing;
    this.pc = new DefaultPersistenceContext();
  }
  
  public CopyContext(boolean vanillaMode)
  {
    this(vanillaMode, false);
  }
  
  public boolean isVanillaMode()
  {
    return this.vanillaMode;
  }
  
  public boolean isSharing()
  {
    return this.sharing;
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.pc;
  }
  
  public Object putIfAbsent(Object id, Object bean)
  {
    return this.pc.putIfAbsent(id, bean);
  }
  
  public Object get(Class<?> beanType, Object beanId)
  {
    return this.pc.get(beanType, beanId);
  }
}
