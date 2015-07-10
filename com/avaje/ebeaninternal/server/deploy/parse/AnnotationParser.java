package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.annotation.LdapAttribute;
import com.avaje.ebean.config.ldap.LdapAttributeAdapter;
import com.avaje.ebeaninternal.server.deploy.BeanCascadeInfo;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import javax.persistence.CascadeType;
import javax.persistence.PersistenceException;

public abstract class AnnotationParser
  extends AnnotationBase
{
  protected final DeployBeanInfo<?> info;
  protected final DeployBeanDescriptor<?> descriptor;
  protected final Class<?> beanType;
  
  public AnnotationParser(DeployBeanInfo<?> info)
  {
    super(info.getUtil());
    this.info = info;
    this.beanType = info.getDescriptor().getBeanType();
    this.descriptor = info.getDescriptor();
  }
  
  public abstract void parse();
  
  protected void setCascadeTypes(CascadeType[] cascadeTypes, BeanCascadeInfo cascadeInfo)
  {
    if ((cascadeTypes != null) && (cascadeTypes.length > 0)) {
      cascadeInfo.setTypes(cascadeTypes);
    }
  }
  
  protected void readLdapAttribute(LdapAttribute ldapAttribute, DeployBeanProperty prop)
  {
    if (!isEmpty(ldapAttribute.name())) {
      prop.setDbColumn(ldapAttribute.name());
    }
    prop.setDbInsertable(ldapAttribute.insertable());
    prop.setDbUpdateable(ldapAttribute.updatable());
    
    Class<?> adapterCls = ldapAttribute.adapter();
    if ((adapterCls != null) && (!Void.TYPE.equals(adapterCls))) {
      try
      {
        LdapAttributeAdapter adapter = (LdapAttributeAdapter)adapterCls.newInstance();
        prop.setLdapAttributeAdapter(adapter);
      }
      catch (Exception e)
      {
        String msg = "Error creating LdapAttributeAdapter for [" + prop.getFullBeanName() + "] " + "with class [" + adapterCls + "] using the default constructor.";
        
        throw new PersistenceException(msg, e);
      }
    }
  }
}
