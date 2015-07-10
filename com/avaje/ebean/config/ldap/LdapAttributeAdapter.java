package com.avaje.ebean.config.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;

public abstract interface LdapAttributeAdapter
{
  public abstract Object readAttribute(Attribute paramAttribute)
    throws NamingException;
  
  public abstract Attribute createAttribute(Object paramObject);
}
