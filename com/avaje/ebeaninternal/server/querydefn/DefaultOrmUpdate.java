package com.avaje.ebeaninternal.server.querydefn;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.SpiUpdate;
import com.avaje.ebeaninternal.api.SpiUpdate.OrmUpdateType;
import com.avaje.ebeaninternal.server.deploy.DeployNamedUpdate;
import java.io.Serializable;

public final class DefaultOrmUpdate<T>
  implements SpiUpdate<T>, Serializable
{
  private static final long serialVersionUID = -8791423602246515438L;
  private final transient EbeanServer server;
  private final Class<?> beanType;
  private final String name;
  private final BindParams bindParams = new BindParams();
  private final String updateStatement;
  private boolean notifyCache = true;
  private int timeout;
  private String generatedSql;
  private final String baseTable;
  private final SpiUpdate.OrmUpdateType type;
  
  public DefaultOrmUpdate(Class<?> beanType, EbeanServer server, String baseTable, String updateStatement)
  {
    this.beanType = beanType;
    this.server = server;
    this.baseTable = baseTable;
    this.name = "";
    this.updateStatement = updateStatement;
    this.type = deriveType(updateStatement);
  }
  
  public DefaultOrmUpdate(Class<?> beanType, EbeanServer server, String baseTable, DeployNamedUpdate namedUpdate)
  {
    this.beanType = beanType;
    this.server = server;
    this.baseTable = baseTable;
    this.name = namedUpdate.getName();
    this.notifyCache = namedUpdate.isNotifyCache();
    
    this.updateStatement = namedUpdate.getSqlUpdateStatement();
    this.type = deriveType(this.updateStatement);
  }
  
  public DefaultOrmUpdate<T> setTimeout(int secs)
  {
    this.timeout = secs;
    return this;
  }
  
  public Class<?> getBeanType()
  {
    return this.beanType;
  }
  
  public int getTimeout()
  {
    return this.timeout;
  }
  
  private SpiUpdate.OrmUpdateType deriveType(String updateStatement)
  {
    updateStatement = updateStatement.trim();
    int spacepos = updateStatement.indexOf(' ');
    if (spacepos == -1) {
      return SpiUpdate.OrmUpdateType.UNKNOWN;
    }
    String firstWord = updateStatement.substring(0, spacepos);
    if (firstWord.equalsIgnoreCase("update")) {
      return SpiUpdate.OrmUpdateType.UPDATE;
    }
    if (firstWord.equalsIgnoreCase("insert")) {
      return SpiUpdate.OrmUpdateType.INSERT;
    }
    if (firstWord.equalsIgnoreCase("delete")) {
      return SpiUpdate.OrmUpdateType.DELETE;
    }
    return SpiUpdate.OrmUpdateType.UNKNOWN;
  }
  
  public int execute()
  {
    return this.server.execute(this);
  }
  
  public DefaultOrmUpdate<T> setNotifyCache(boolean notifyCache)
  {
    this.notifyCache = notifyCache;
    return this;
  }
  
  public boolean isNotifyCache()
  {
    return this.notifyCache;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getUpdateStatement()
  {
    return this.updateStatement;
  }
  
  public DefaultOrmUpdate<T> set(int position, Object value)
  {
    this.bindParams.setParameter(position, value);
    return this;
  }
  
  public DefaultOrmUpdate<T> setParameter(int position, Object value)
  {
    this.bindParams.setParameter(position, value);
    return this;
  }
  
  public DefaultOrmUpdate<T> setNull(int position, int jdbcType)
  {
    this.bindParams.setNullParameter(position, jdbcType);
    return this;
  }
  
  public DefaultOrmUpdate<T> setNullParameter(int position, int jdbcType)
  {
    this.bindParams.setNullParameter(position, jdbcType);
    return this;
  }
  
  public DefaultOrmUpdate<T> set(String name, Object value)
  {
    this.bindParams.setParameter(name, value);
    return this;
  }
  
  public DefaultOrmUpdate<T> setParameter(String name, Object param)
  {
    this.bindParams.setParameter(name, param);
    return this;
  }
  
  public DefaultOrmUpdate<T> setNull(String name, int jdbcType)
  {
    this.bindParams.setNullParameter(name, jdbcType);
    return this;
  }
  
  public DefaultOrmUpdate<T> setNullParameter(String name, int jdbcType)
  {
    this.bindParams.setNullParameter(name, jdbcType);
    return this;
  }
  
  public BindParams getBindParams()
  {
    return this.bindParams;
  }
  
  public String getGeneratedSql()
  {
    return this.generatedSql;
  }
  
  public void setGeneratedSql(String generatedSql)
  {
    this.generatedSql = generatedSql;
  }
  
  public String getBaseTable()
  {
    return this.baseTable;
  }
  
  public SpiUpdate.OrmUpdateType getOrmUpdateType()
  {
    return this.type;
  }
}
