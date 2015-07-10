package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.RawSql.Sql;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.SqlLimitResponse;
import com.avaje.ebean.config.dbplatform.SqlLimiter;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryLimitRequest;
import com.avaje.ebeaninternal.server.util.BindParamsParser;

public class CQueryBuilderRawSql
  implements Constants
{
  private final SqlLimiter sqlLimiter;
  private final DatabasePlatform dbPlatform;
  
  CQueryBuilderRawSql(SqlLimiter sqlLimiter, DatabasePlatform dbPlatform)
  {
    this.sqlLimiter = sqlLimiter;
    this.dbPlatform = dbPlatform;
  }
  
  public SqlLimitResponse buildSql(OrmQueryRequest<?> request, CQueryPredicates predicates, RawSql.Sql rsql)
  {
    if (!rsql.isParsed())
    {
      String sql = rsql.getUnparsedSql();
      BindParams bindParams = request.getQuery().getBindParams();
      if ((bindParams != null) && (bindParams.requiresNamedParamsPrepare())) {
        sql = BindParamsParser.parse(bindParams, sql);
      }
      return new SqlLimitResponse(sql, false);
    }
    String orderBy = getOrderBy(predicates, rsql);
    
    String sql = buildMainQuery(orderBy, request, predicates, rsql);
    
    SpiQuery<?> query = request.getQuery();
    if ((query.hasMaxRowsOrFirstRow()) && (this.sqlLimiter != null)) {
      return this.sqlLimiter.limit(new OrmQueryLimitRequest(sql, orderBy, query, this.dbPlatform));
    }
    String prefix = "select " + (rsql.isDistinct() ? "distinct " : "");
    sql = prefix + sql;
    return new SqlLimitResponse(sql, false);
  }
  
  private String buildMainQuery(String orderBy, OrmQueryRequest<?> request, CQueryPredicates predicates, RawSql.Sql sql)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(sql.getPreFrom());
    sb.append(" ");
    sb.append('\n');
    
    String s = sql.getPreWhere();
    BindParams bindParams = request.getQuery().getBindParams();
    if ((bindParams != null) && (bindParams.requiresNamedParamsPrepare())) {
      s = BindParamsParser.parse(bindParams, s);
    }
    sb.append(s);
    sb.append(" ");
    
    String dynamicWhere = null;
    if (request.getQuery().getId() != null)
    {
      BeanDescriptor<?> descriptor = request.getBeanDescriptor();
      
      dynamicWhere = descriptor.getIdBinderIdSql();
    }
    String dbWhere = predicates.getDbWhere();
    if (!isEmpty(dbWhere)) {
      if (dynamicWhere == null) {
        dynamicWhere = dbWhere;
      } else {
        dynamicWhere = dynamicWhere + " and " + dbWhere;
      }
    }
    if (!isEmpty(dynamicWhere))
    {
      sb.append('\n');
      if (sql.isAndWhereExpr()) {
        sb.append("and ");
      } else {
        sb.append("where ");
      }
      sb.append(dynamicWhere);
      sb.append(" ");
    }
    String preHaving = sql.getPreHaving();
    if (!isEmpty(preHaving))
    {
      sb.append('\n');
      sb.append(preHaving);
      sb.append(" ");
    }
    String dbHaving = predicates.getDbHaving();
    if (!isEmpty(dbHaving))
    {
      sb.append(" ");
      sb.append('\n');
      if (sql.isAndHavingExpr()) {
        sb.append("and ");
      } else {
        sb.append("having ");
      }
      sb.append(dbHaving);
      sb.append(" ");
    }
    if (!isEmpty(orderBy))
    {
      sb.append('\n');
      sb.append(" order by ").append(orderBy);
    }
    return sb.toString().trim();
  }
  
  private boolean isEmpty(String s)
  {
    return (s == null) || (s.length() == 0);
  }
  
  private String getOrderBy(CQueryPredicates predicates, RawSql.Sql sql)
  {
    String orderBy = predicates.getDbOrderBy();
    if (orderBy != null) {
      return orderBy;
    }
    return sql.getOrderBy();
  }
}
