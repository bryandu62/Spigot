package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;

public abstract interface BeanVisitor
{
  public abstract void visitBegin();
  
  public abstract boolean visitBean(BeanDescriptor<?> paramBeanDescriptor);
  
  public abstract PropertyVisitor visitProperty(BeanProperty paramBeanProperty);
  
  public abstract void visitBeanEnd(BeanDescriptor<?> paramBeanDescriptor);
  
  public abstract void visitEnd();
}
