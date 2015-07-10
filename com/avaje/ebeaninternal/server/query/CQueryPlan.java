package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.dbplatform.SqlLimitResponse;
import com.avaje.ebean.meta.MetaQueryStatistic;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.type.DataReader;
import com.avaje.ebeaninternal.server.type.RsetDataReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CQueryPlan
{
  private final boolean autofetchTuned;
  private final int hash;
  private final boolean rawSql;
  private final boolean rowNumberIncluded;
  private final String sql;
  private final String logWhereSql;
  private final SqlTree sqlTree;
  private final BeanProperty[] encryptedProps;
  private CQueryStats queryStats = new CQueryStats();
  
  public CQueryPlan(OrmQueryRequest<?> request, SqlLimitResponse sqlRes, SqlTree sqlTree, boolean rawSql, String logWhereSql, String luceneQueryDescription)
  {
    this.hash = request.getQueryPlanHash();
    this.autofetchTuned = request.getQuery().isAutofetchTuned();
    if (sqlRes != null)
    {
      this.sql = sqlRes.getSql();
      this.rowNumberIncluded = sqlRes.isIncludesRowNumberColumn();
    }
    else
    {
      this.sql = luceneQueryDescription;
      this.rowNumberIncluded = false;
    }
    this.sqlTree = sqlTree;
    this.rawSql = rawSql;
    this.logWhereSql = logWhereSql;
    this.encryptedProps = sqlTree.getEncryptedProps();
  }
  
  public CQueryPlan(String sql, SqlTree sqlTree, boolean rawSql, boolean rowNumberIncluded, String logWhereSql)
  {
    this.hash = 0;
    this.autofetchTuned = false;
    this.sql = sql;
    this.sqlTree = sqlTree;
    this.rawSql = rawSql;
    this.rowNumberIncluded = rowNumberIncluded;
    this.logWhereSql = logWhereSql;
    this.encryptedProps = sqlTree.getEncryptedProps();
  }
  
  public boolean isLucene()
  {
    return false;
  }
  
  public DataReader createDataReader(ResultSet rset)
  {
    return new RsetDataReader(rset);
  }
  
  public void bindEncryptedProperties(DataBind dataBind)
    throws SQLException
  {
    if (this.encryptedProps != null) {
      for (int i = 0; i < this.encryptedProps.length; i++)
      {
        String key = this.encryptedProps[i].getEncryptKey().getStringValue();
        dataBind.setString(key);
      }
    }
  }
  
  public boolean isAutofetchTuned()
  {
    return this.autofetchTuned;
  }
  
  public int getHash()
  {
    return this.hash;
  }
  
  public String getSql()
  {
    return this.sql;
  }
  
  public SqlTree getSqlTree()
  {
    return this.sqlTree;
  }
  
  public boolean isRawSql()
  {
    return this.rawSql;
  }
  
  public boolean isRowNumberIncluded()
  {
    return this.rowNumberIncluded;
  }
  
  public String getLogWhereSql()
  {
    return this.logWhereSql;
  }
  
  public void resetStatistics()
  {
    this.queryStats = new CQueryStats();
  }
  
  public void executionTime(int loadedBeanCount, int timeMicros)
  {
    this.queryStats = this.queryStats.add(loadedBeanCount, timeMicros);
  }
  
  public CQueryStats getQueryStats()
  {
    return this.queryStats;
  }
  
  public long getLastQueryTime()
  {
    return this.queryStats.getLastQueryTime();
  }
  
  public MetaQueryStatistic createMetaQueryStatistic(String beanName)
  {
    return this.queryStats.createMetaQueryStatistic(beanName, this);
  }
}
