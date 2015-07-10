package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSql.ColumnMapping;
import com.avaje.ebean.RawSql.ColumnMapping.Column;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.SqlLimitRequest;
import com.avaje.ebean.config.dbplatform.SqlLimitResponse;
import com.avaje.ebean.config.dbplatform.SqlLimiter;
import com.avaje.ebean.text.PathProperties;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.persist.Binder;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryLimitRequest;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.PersistenceException;

public class CQueryBuilder
  implements Constants
{
  private final String tableAliasPlaceHolder;
  private final String columnAliasPrefix;
  private final SqlLimiter sqlLimiter;
  private final RawSqlSelectClauseBuilder sqlSelectBuilder;
  private final CQueryBuilderRawSql rawSqlHandler;
  private final Binder binder;
  private final BackgroundExecutor backgroundExecutor;
  private final boolean selectCountWithAlias;
  private DatabasePlatform dbPlatform;
  
  public CQueryBuilder(BackgroundExecutor backgroundExecutor, DatabasePlatform dbPlatform, Binder binder)
  {
    this.backgroundExecutor = backgroundExecutor;
    this.binder = binder;
    this.tableAliasPlaceHolder = GlobalProperties.get("ebean.tableAliasPlaceHolder", "${ta}");
    this.columnAliasPrefix = GlobalProperties.get("ebean.columnAliasPrefix", "c");
    this.sqlSelectBuilder = new RawSqlSelectClauseBuilder(dbPlatform, binder);
    
    this.sqlLimiter = dbPlatform.getSqlLimiter();
    this.rawSqlHandler = new CQueryBuilderRawSql(this.sqlLimiter, dbPlatform);
    
    this.selectCountWithAlias = dbPlatform.isSelectCountWithAlias();
    
    this.dbPlatform = dbPlatform;
  }
  
  public static String prefixOrderByFields(String name, String orderBy)
  {
    StringBuilder sb = new StringBuilder();
    for (String token : orderBy.split(","))
    {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(name);
      sb.append(".");
      sb.append(token.trim());
    }
    return sb.toString();
  }
  
  public <T> CQueryFetchIds buildFetchIdsQuery(OrmQueryRequest<T> request)
  {
    SpiQuery<T> query = request.getQuery();
    
    query.setSelectId();
    
    CQueryPredicates predicates = new CQueryPredicates(this.binder, request);
    CQueryPlan queryPlan = request.getQueryPlan();
    if (queryPlan != null)
    {
      predicates.prepare(false);
      String sql = queryPlan.getSql();
      return new CQueryFetchIds(request, predicates, sql, this.backgroundExecutor);
    }
    predicates.prepare(true);
    
    SqlTree sqlTree = createSqlTree(request, predicates);
    SqlLimitResponse s = buildSql(null, request, predicates, sqlTree);
    String sql = s.getSql();
    
    queryPlan = new CQueryPlan(sql, sqlTree, false, s.isIncludesRowNumberColumn(), predicates.getLogWhereSql());
    
    request.putQueryPlan(queryPlan);
    return new CQueryFetchIds(request, predicates, sql, this.backgroundExecutor);
  }
  
  public <T> CQueryRowCount buildRowCountQuery(OrmQueryRequest<T> request)
  {
    SpiQuery<T> query = request.getQuery();
    
    query.setOrder(null);
    
    boolean hasMany = !query.getManyWhereJoins().isEmpty();
    
    query.setSelectId();
    
    String sqlSelect = "select count(*)";
    if (hasMany)
    {
      query.setDistinct(true);
      sqlSelect = null;
    }
    CQueryPredicates predicates = new CQueryPredicates(this.binder, request);
    CQueryPlan queryPlan = request.getQueryPlan();
    if (queryPlan != null)
    {
      predicates.prepare(false);
      String sql = queryPlan.getSql();
      return new CQueryRowCount(request, predicates, sql);
    }
    predicates.prepare(true);
    
    SqlTree sqlTree = createSqlTree(request, predicates);
    SqlLimitResponse s = buildSql(sqlSelect, request, predicates, sqlTree);
    String sql = s.getSql();
    if (hasMany)
    {
      sql = "select count(*) from ( " + sql + ")";
      if (this.selectCountWithAlias) {
        sql = sql + " as c";
      }
    }
    queryPlan = new CQueryPlan(sql, sqlTree, false, s.isIncludesRowNumberColumn(), predicates.getLogWhereSql());
    request.putQueryPlan(queryPlan);
    
    return new CQueryRowCount(request, predicates, sql);
  }
  
  public <T> CQuery<T> buildQuery(OrmQueryRequest<T> request)
  {
    if (request.isSqlSelect()) {
      return this.sqlSelectBuilder.build(request);
    }
    CQueryPredicates predicates = new CQueryPredicates(this.binder, request);
    
    CQueryPlan queryPlan = request.getQueryPlan();
    if (queryPlan != null)
    {
      predicates.prepare(false);
      return new CQuery(request, predicates, queryPlan);
    }
    predicates.prepare(true);
    
    SqlTree sqlTree = createSqlTree(request, predicates);
    SqlLimitResponse res = buildSql(null, request, predicates, sqlTree);
    
    boolean rawSql = request.isRawSql();
    if (rawSql) {
      queryPlan = new CQueryPlanRawSql(request, res, sqlTree, predicates.getLogWhereSql());
    } else {
      queryPlan = new CQueryPlan(request, res, sqlTree, rawSql, predicates.getLogWhereSql(), null);
    }
    request.putQueryPlan(queryPlan);
    
    return new CQuery(request, predicates, queryPlan);
  }
  
  private SqlTree createSqlTree(OrmQueryRequest<?> request, CQueryPredicates predicates)
  {
    if (request.isRawSql()) {
      return createRawSqlSqlTree(request, predicates);
    }
    return new SqlTreeBuilder(this.tableAliasPlaceHolder, this.columnAliasPrefix, request, predicates).build();
  }
  
  private SqlTree createRawSqlSqlTree(OrmQueryRequest<?> request, CQueryPredicates predicates)
  {
    BeanDescriptor<?> descriptor = request.getBeanDescriptor();
    RawSql.ColumnMapping columnMapping = request.getQuery().getRawSql().getColumnMapping();
    
    PathProperties pathProps = new PathProperties();
    
    Iterator<RawSql.ColumnMapping.Column> it = columnMapping.getColumns();
    while (it.hasNext())
    {
      RawSql.ColumnMapping.Column column = (RawSql.ColumnMapping.Column)it.next();
      String propertyName = column.getPropertyName();
      if (!"$$_IGNORE_COLUMN_$$".equals(propertyName))
      {
        ElPropertyValue el = descriptor.getElGetValue(propertyName);
        if (el == null)
        {
          String msg = "Property [" + propertyName + "] not found on " + descriptor.getFullName();
          throw new PersistenceException(msg);
        }
        BeanProperty beanProperty = el.getBeanProperty();
        if (beanProperty.isId())
        {
          propertyName = SplitName.parent(propertyName);
        }
        else if ((beanProperty instanceof BeanPropertyAssocOne))
        {
          String msg = "Column [" + column.getDbColumn() + "] mapped to complex Property[" + propertyName + "]";
          msg = msg + ". It should be mapped to a simple property (proably the Id property). ";
          throw new PersistenceException(msg);
        }
        if (propertyName != null)
        {
          String[] pathProp = SplitName.split(propertyName);
          pathProps.addToPath(pathProp[0], pathProp[1]);
        }
      }
    }
    OrmQueryDetail detail = new OrmQueryDetail();
    
    Iterator<String> pathIt = pathProps.getPaths().iterator();
    while (pathIt.hasNext())
    {
      String path = (String)pathIt.next();
      Set<String> props = pathProps.get(path);
      detail.getChunk(path, true).setDefaultProperties(null, props);
    }
    return new SqlTreeBuilder(request, predicates, detail).build();
  }
  
  private SqlLimitResponse buildSql(String selectClause, OrmQueryRequest<?> request, CQueryPredicates predicates, SqlTree select)
  {
    SpiQuery<?> query = request.getQuery();
    
    RawSql rawSql = query.getRawSql();
    if (rawSql != null) {
      return this.rawSqlHandler.buildSql(request, predicates, rawSql.getSql());
    }
    BeanPropertyAssocMany<?> manyProp = select.getManyProperty();
    
    boolean useSqlLimiter = false;
    
    StringBuilder sb = new StringBuilder(500);
    if (selectClause != null)
    {
      sb.append(selectClause);
    }
    else
    {
      useSqlLimiter = (query.hasMaxRowsOrFirstRow()) && (manyProp == null);
      if (!useSqlLimiter)
      {
        sb.append("select ");
        if (query.isDistinct()) {
          sb.append("distinct ");
        }
      }
      sb.append(select.getSelectSql());
    }
    sb.append(" ").append('\n');
    sb.append("from ");
    
    sb.append(select.getFromSql());
    
    String inheritanceWhere = select.getInheritanceWhereSql();
    
    boolean hasWhere = false;
    if (inheritanceWhere.length() > 0)
    {
      sb.append(" ").append('\n').append("where");
      sb.append(inheritanceWhere);
      hasWhere = true;
    }
    if ((request.isFindById()) || (query.getId() != null))
    {
      if (hasWhere) {
        sb.append(" and ");
      } else {
        sb.append('\n').append("where ");
      }
      BeanDescriptor<?> desc = request.getBeanDescriptor();
      String idSql = desc.getIdBinderIdSql();
      if (idSql.isEmpty()) {
        throw new IllegalStateException("Executing FindById query on entity bean " + desc.getName() + " that doesn't have an @Id property??");
      }
      sb.append(idSql).append(" ");
      hasWhere = true;
    }
    String dbWhere = predicates.getDbWhere();
    if (!isEmpty(dbWhere))
    {
      if (!hasWhere)
      {
        hasWhere = true;
        sb.append(" ").append('\n').append("where ");
      }
      else
      {
        sb.append("and ");
      }
      sb.append(dbWhere);
    }
    String dbFilterMany = predicates.getDbFilterMany();
    if (!isEmpty(dbFilterMany))
    {
      if (!hasWhere) {
        sb.append(" ").append('\n').append("where ");
      } else {
        sb.append("and ");
      }
      sb.append(dbFilterMany);
    }
    String dbOrderBy = predicates.getDbOrderBy();
    if (dbOrderBy != null)
    {
      sb.append(" ").append('\n');
      sb.append("order by ").append(dbOrderBy);
    }
    if (useSqlLimiter)
    {
      SqlLimitRequest r = new OrmQueryLimitRequest(sb.toString(), dbOrderBy, query, this.dbPlatform);
      return this.sqlLimiter.limit(r);
    }
    return new SqlLimitResponse(this.dbPlatform.completeSql(sb.toString(), query), false);
  }
  
  private boolean isEmpty(String s)
  {
    return (s == null) || (s.length() == 0);
  }
}
