package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.config.ldap.LdapAttributeAdapter;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertySimpleCollection;
import com.avaje.ebeaninternal.server.ldap.LdapPersistenceException;
import com.avaje.ebeaninternal.server.type.ScalarType;
import java.util.Iterator;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

public class BeanPropertySimpleCollection<T>
  extends BeanPropertyAssocMany<T>
{
  private final ScalarType<T> collectionScalarType;
  
  public BeanPropertySimpleCollection(BeanDescriptorMap owner, BeanDescriptor<?> descriptor, DeployBeanPropertySimpleCollection<T> deploy)
  {
    super(owner, descriptor, deploy);
    this.collectionScalarType = deploy.getCollectionScalarType();
  }
  
  public void initialise()
  {
    super.initialise();
  }
  
  public Attribute createAttribute(Object bean)
  {
    Object v = getValue(bean);
    if (v == null) {
      return null;
    }
    if (this.ldapAttributeAdapter != null) {
      return this.ldapAttributeAdapter.createAttribute(v);
    }
    BasicAttribute attrs = new BasicAttribute(getDbColumn());
    
    Iterator<?> it = this.help.getIterator(v);
    if (it != null) {
      while (it.hasNext())
      {
        Object beanValue = it.next();
        Object attrValue = this.collectionScalarType.toJdbcType(beanValue);
        attrs.add(attrValue);
      }
    }
    return attrs;
  }
  
  public void setAttributeValue(Object bean, Attribute attr)
  {
    try
    {
      if (attr != null)
      {
        Object beanValue;
        Object beanValue;
        if (this.ldapAttributeAdapter != null)
        {
          beanValue = this.ldapAttributeAdapter.readAttribute(attr);
        }
        else
        {
          boolean vanilla = true;
          beanValue = this.help.createEmpty(vanilla);
          BeanCollectionAdd collAdd = this.help.getBeanCollectionAdd(beanValue, this.mapKey);
          
          NamingEnumeration<?> en = attr.getAll();
          while (en.hasMoreElements())
          {
            Object attrValue = en.nextElement();
            Object collValue = this.collectionScalarType.toBeanType(attrValue);
            collAdd.addBean(collValue);
          }
        }
        setValue(bean, beanValue);
      }
    }
    catch (NamingException e)
    {
      throw new LdapPersistenceException(e);
    }
  }
}
