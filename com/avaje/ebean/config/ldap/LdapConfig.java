package com.avaje.ebean.config.ldap;

public class LdapConfig
{
  private LdapContextFactory contextFactory;
  private boolean vanillaMode;
  
  public LdapContextFactory getContextFactory()
  {
    return this.contextFactory;
  }
  
  public void setContextFactory(LdapContextFactory contextFactory)
  {
    this.contextFactory = contextFactory;
  }
  
  public boolean isVanillaMode()
  {
    return this.vanillaMode;
  }
  
  public void setVanillaMode(boolean vanillaMode)
  {
    this.vanillaMode = vanillaMode;
  }
}
