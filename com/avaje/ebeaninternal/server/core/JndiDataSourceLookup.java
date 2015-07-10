package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.config.GlobalProperties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

public class JndiDataSourceLookup
{
  private static final String DEFAULT_PREFIX = "java:comp/env/jdbc/";
  String jndiPrefix = GlobalProperties.get("ebean.datasource.jndi.prefix", "java:comp/env/jdbc/");
  
  public DataSource lookup(String jndiName)
  {
    try
    {
      if (!jndiName.startsWith("java:")) {
        jndiName = this.jndiPrefix + jndiName;
      }
      Context ctx = new InitialContext();
      DataSource ds = (DataSource)ctx.lookup(jndiName);
      if (ds == null) {
        throw new PersistenceException("JNDI DataSource [" + jndiName + "] not found?");
      }
      return ds;
    }
    catch (NamingException ex)
    {
      throw new PersistenceException(ex);
    }
  }
}
