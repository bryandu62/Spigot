package com.avaje.ebean.config.ldap;

import javax.naming.directory.DirContext;

public abstract interface LdapContextFactory
{
  public abstract DirContext createContext();
}
