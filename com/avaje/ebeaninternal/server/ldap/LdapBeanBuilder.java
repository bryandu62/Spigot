package com.avaje.ebeaninternal.server.ldap;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class LdapBeanBuilder<T>
{
  private static final Logger logger = Logger.getLogger(LdapBeanBuilder.class.getName());
  private final BeanDescriptor<T> beanDescriptor;
  private final boolean vanillaMode;
  private Set<String> loadedProps;
  
  public LdapBeanBuilder(BeanDescriptor<T> beanDescriptor, boolean vanillaMode)
  {
    this.beanDescriptor = beanDescriptor;
    this.vanillaMode = vanillaMode;
  }
  
  public T readAttributes(Attributes attributes)
    throws NamingException
  {
    Object bean = this.beanDescriptor.createBean(this.vanillaMode);
    
    NamingEnumeration<? extends Attribute> all = attributes.getAll();
    
    boolean setLoadedProps = false;
    if (this.loadedProps == null)
    {
      setLoadedProps = true;
      this.loadedProps = new LinkedHashSet();
    }
    while (all.hasMoreElements())
    {
      Attribute attr = (Attribute)all.nextElement();
      String attrName = attr.getID();
      
      BeanProperty prop = this.beanDescriptor.getBeanPropertyFromDbColumn(attrName);
      if (prop == null)
      {
        if (!"objectclass".equalsIgnoreCase(attrName)) {
          logger.info("... hmm, no property to map to attribute[" + attrName + "] value[" + attr.get() + "]");
        }
      }
      else
      {
        prop.setAttributeValue(bean, attr);
        if (setLoadedProps) {
          this.loadedProps.add(prop.getName());
        }
      }
    }
    if ((bean instanceof EntityBean))
    {
      EntityBeanIntercept ebi = ((EntityBean)bean)._ebean_getIntercept();
      ebi.setLoadedProps(this.loadedProps);
      ebi.setLoaded();
    }
    BeanPersistController persistController = this.beanDescriptor.getPersistController();
    if (persistController != null) {
      persistController.postLoad(bean, this.loadedProps);
    }
    return (T)bean;
  }
}
