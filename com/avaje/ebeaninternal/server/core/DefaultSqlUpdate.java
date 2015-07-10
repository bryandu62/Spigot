package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.SpiSqlUpdate;
import java.io.Serializable;

public final class DefaultSqlUpdate
  implements Serializable, SpiSqlUpdate
{
  private static final long serialVersionUID = -6493829438421253102L;
  private final transient EbeanServer server;
  private final BindParams bindParams;
  private final String sql;
  private String label = "";
  private int timeout;
  private boolean isAutoTableMod = true;
  private int addPos;
  
  public DefaultSqlUpdate(EbeanServer server, String sql, BindParams bindParams)
  {
    this.server = server;
    this.sql = sql;
    this.bindParams = bindParams;
  }
  
  public DefaultSqlUpdate(EbeanServer server, String sql)
  {
    this(server, sql, new BindParams());
  }
  
  public DefaultSqlUpdate(String sql)
  {
    this(null, sql, new BindParams());
  }
  
  public int execute()
  {
    if (this.server != null) {
      return this.server.execute(this);
    }
    return Ebean.execute(this);
  }
  
  public boolean isAutoTableMod()
  {
    return this.isAutoTableMod;
  }
  
  public SqlUpdate setAutoTableMod(boolean isAutoTableMod)
  {
    this.isAutoTableMod = isAutoTableMod;
    return this;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public SqlUpdate setLabel(String label)
  {
    this.label = label;
    return this;
  }
  
  public String getSql()
  {
    return this.sql;
  }
  
  public int getTimeout()
  {
    return this.timeout;
  }
  
  public SqlUpdate setTimeout(int secs)
  {
    this.timeout = secs;
    return this;
  }
  
  public SqlUpdate addParameter(Object value)
  {
    return setParameter(++this.addPos, value);
  }
  
  public SqlUpdate setParameter(int position, Object value)
  {
    this.bindParams.setParameter(position, value);
    return this;
  }
  
  public SqlUpdate setNull(int position, int jdbcType)
  {
    this.bindParams.setNullParameter(position, jdbcType);
    return this;
  }
  
  public SqlUpdate setNullParameter(int position, int jdbcType)
  {
    this.bindParams.setNullParameter(position, jdbcType);
    return this;
  }
  
  public SqlUpdate setParameter(String name, Object param)
  {
    this.bindParams.setParameter(name, param);
    return this;
  }
  
  public SqlUpdate setNull(String name, int jdbcType)
  {
    this.bindParams.setNullParameter(name, jdbcType);
    return this;
  }
  
  public SqlUpdate setNullParameter(String name, int jdbcType)
  {
    this.bindParams.setNullParameter(name, jdbcType);
    return this;
  }
  
  public BindParams getBindParams()
  {
    return this.bindParams;
  }
}
