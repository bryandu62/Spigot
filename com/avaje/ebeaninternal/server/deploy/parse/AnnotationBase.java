package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class AnnotationBase
{
  protected final DatabasePlatform databasePlatform;
  protected final NamingConvention namingConvention;
  protected final DeployUtil util;
  
  protected AnnotationBase(DeployUtil util)
  {
    this.util = util;
    this.databasePlatform = util.getDbPlatform();
    this.namingConvention = util.getNamingConvention();
  }
  
  public abstract void parse();
  
  protected boolean isEmpty(String s)
  {
    if ((s == null) || (s.trim().length() == 0)) {
      return true;
    }
    return false;
  }
  
  protected <T extends Annotation> T get(DeployBeanProperty prop, Class<T> annClass)
  {
    T a = null;
    Field field = prop.getField();
    if (field != null) {
      a = field.getAnnotation(annClass);
    }
    if (a == null)
    {
      Method m = prop.getReadMethod();
      if (m != null) {
        a = m.getAnnotation(annClass);
      }
    }
    return a;
  }
  
  protected <T extends Annotation> T find(DeployBeanProperty prop, Class<T> annClass)
  {
    T a = get(prop, annClass);
    if (a == null) {
      a = prop.getOwningType().getAnnotation(annClass);
    }
    return a;
  }
}
