package com.avaje.ebeaninternal.server.ldap;

import com.avaje.ebean.config.ldap.LdapContextFactory;
import java.util.List;
import javax.naming.directory.DirContext;

public class LdapOrmQueryEngine
{
  private final boolean defaultVanillaMode;
  private final LdapContextFactory contextFactory;
  
  public LdapOrmQueryEngine(boolean defaultVanillaMode, LdapContextFactory contextFactory)
  {
    this.defaultVanillaMode = defaultVanillaMode;
    this.contextFactory = contextFactory;
  }
  
  public <T> T findId(LdapOrmQueryRequest<T> request)
  {
    DirContext dc = this.contextFactory.createContext();
    LdapOrmQueryExecute<T> exe = new LdapOrmQueryExecute(request, this.defaultVanillaMode, dc);
    
    return (T)exe.findId();
  }
  
  public <T> List<T> findList(LdapOrmQueryRequest<T> request)
  {
    DirContext dc = this.contextFactory.createContext();
    
    LdapOrmQueryExecute<T> exe = new LdapOrmQueryExecute(request, this.defaultVanillaMode, dc);
    
    return exe.findList();
  }
}
