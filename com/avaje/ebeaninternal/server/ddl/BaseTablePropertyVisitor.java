package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyCompound;

public abstract class BaseTablePropertyVisitor
  implements PropertyVisitor
{
  public void visitEmbedded(BeanPropertyAssocOne<?> p) {}
  
  public abstract void visitEmbeddedScalar(BeanProperty paramBeanProperty, BeanPropertyAssocOne<?> paramBeanPropertyAssocOne);
  
  public void visitMany(BeanPropertyAssocMany<?> p) {}
  
  public void visitOneExported(BeanPropertyAssocOne<?> p) {}
  
  public abstract void visitOneImported(BeanPropertyAssocOne<?> paramBeanPropertyAssocOne);
  
  public abstract void visitScalar(BeanProperty paramBeanProperty);
  
  public void visitCompound(BeanPropertyCompound p) {}
  
  public abstract void visitCompoundScalar(BeanPropertyCompound paramBeanPropertyCompound, BeanProperty paramBeanProperty);
}
