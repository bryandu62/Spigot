package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompound;

public abstract interface PropertyVisitor
{
  public abstract void visitMany(BeanPropertyAssocMany<?> paramBeanPropertyAssocMany);
  
  public abstract void visitOneImported(BeanPropertyAssocOne<?> paramBeanPropertyAssocOne);
  
  public abstract void visitOneExported(BeanPropertyAssocOne<?> paramBeanPropertyAssocOne);
  
  public abstract void visitEmbedded(BeanPropertyAssocOne<?> paramBeanPropertyAssocOne);
  
  public abstract void visitEmbeddedScalar(BeanProperty paramBeanProperty, BeanPropertyAssocOne<?> paramBeanPropertyAssocOne);
  
  public abstract void visitScalar(BeanProperty paramBeanProperty);
  
  public abstract void visitCompound(BeanPropertyCompound paramBeanPropertyCompound);
  
  public abstract void visitCompoundScalar(BeanPropertyCompound paramBeanPropertyCompound, BeanProperty paramBeanProperty);
}
