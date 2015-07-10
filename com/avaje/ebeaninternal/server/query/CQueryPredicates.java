package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSql.Sql;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.BindParams.OrderedList;
import com.avaje.ebeaninternal.api.SpiExpressionList;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DeployParser;
import com.avaje.ebeaninternal.server.persist.Binder;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.util.BindParamsParser;
import com.avaje.ebeaninternal.util.DefaultExpressionRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CQueryPredicates
{
  private static final Logger logger = Logger.getLogger(CQueryPredicates.class.getName());
  private final Binder binder;
  private final OrmQueryRequest<?> request;
  private final SpiQuery<?> query;
  private final Object idValue;
  private boolean rawSql;
  private final BindParams bindParams;
  private BindParams.OrderedList havingNamedParams;
  private ArrayList<Object> filterManyExprBindValues;
  private String filterManyExprSql;
  private ArrayList<Object> whereExprBindValues;
  private String whereExprSql;
  private String whereRawSql;
  private ArrayList<Object> havingExprBindValues;
  private String havingExprSql;
  private String havingRawSql;
  private String dbHaving;
  private String dbWhere;
  private String dbFilterMany;
  private String logicalOrderBy;
  private String dbOrderBy;
  private Set<String> predicateIncludes;
  
  public CQueryPredicates(Binder binder, OrmQueryRequest<?> request)
  {
    this.binder = binder;
    this.request = request;
    this.query = request.getQuery();
    this.bindParams = this.query.getBindParams();
    this.idValue = this.query.getId();
  }
  
  public String bind(DataBind dataBind)
    throws SQLException
  {
    StringBuilder bindLog = new StringBuilder();
    if (this.idValue != null)
    {
      this.request.getBeanDescriptor().bindId(dataBind, this.idValue);
      bindLog.append(this.idValue);
    }
    if (this.bindParams != null) {
      this.binder.bind(this.bindParams, dataBind, bindLog);
    }
    if (this.whereExprBindValues != null) {
      for (int i = 0; i < this.whereExprBindValues.size(); i++)
      {
        Object bindValue = this.whereExprBindValues.get(i);
        this.binder.bindObject(dataBind, bindValue);
        if ((i > 0) || (this.idValue != null)) {
          bindLog.append(", ");
        }
        bindLog.append(bindValue);
      }
    }
    if (this.filterManyExprBindValues != null) {
      for (int i = 0; i < this.filterManyExprBindValues.size(); i++)
      {
        Object bindValue = this.filterManyExprBindValues.get(i);
        this.binder.bindObject(dataBind, bindValue);
        if ((i > 0) || (this.idValue != null)) {
          bindLog.append(", ");
        }
        bindLog.append(bindValue);
      }
    }
    if (this.havingNamedParams != null)
    {
      bindLog.append(" havingNamed ");
      this.binder.bind(this.havingNamedParams.list(), dataBind, bindLog);
    }
    if (this.havingExprBindValues != null)
    {
      bindLog.append(" having ");
      for (int i = 0; i < this.havingExprBindValues.size(); i++)
      {
        Object bindValue = this.havingExprBindValues.get(i);
        this.binder.bindObject(dataBind, bindValue);
        if (i > 0) {
          bindLog.append(", ");
        }
        bindLog.append(bindValue);
      }
    }
    return bindLog.toString();
  }
  
  private void buildBindHavingRawSql(boolean buildSql, boolean parseRaw, DeployParser deployParser)
  {
    if ((buildSql) || (this.bindParams != null))
    {
      this.havingRawSql = this.query.getAdditionalHaving();
      if (parseRaw) {
        this.havingRawSql = deployParser.parse(this.havingRawSql);
      }
      if ((this.havingRawSql != null) && (this.bindParams != null))
      {
        this.havingNamedParams = BindParamsParser.parseNamedParams(this.bindParams, this.havingRawSql);
        this.havingRawSql = this.havingNamedParams.getPreparedSql();
      }
    }
  }
  
  private void buildBindWhereRawSql(boolean buildSql, boolean parseRaw, DeployParser parser)
  {
    if ((buildSql) || (this.bindParams != null))
    {
      this.whereRawSql = buildWhereRawSql();
      boolean hasRaw = !"".equals(this.whereRawSql);
      if ((hasRaw) && (parseRaw))
      {
        parser.setEncrypted(true);
        this.whereRawSql = parser.parse(this.whereRawSql);
        parser.setEncrypted(false);
      }
      if (this.bindParams != null) {
        if (hasRaw)
        {
          this.whereRawSql = BindParamsParser.parse(this.bindParams, this.whereRawSql, this.request.getBeanDescriptor());
        }
        else if ((this.query.isRawSql()) && (!buildSql))
        {
          String s = this.query.getRawSql().getSql().getPreWhere();
          if (this.bindParams.requiresNamedParamsPrepare()) {
            BindParamsParser.parse(this.bindParams, s);
          }
        }
      }
    }
  }
  
  private String buildWhereRawSql()
  {
    String whereRaw = this.query.getRawWhereClause();
    if (whereRaw == null) {
      whereRaw = "";
    }
    String additionalWhere = this.query.getAdditionalWhere();
    if (additionalWhere != null) {
      whereRaw = whereRaw + additionalWhere;
    }
    return whereRaw;
  }
  
  public void prepare(boolean buildSql)
  {
    DeployParser deployParser = this.request.createDeployParser();
    
    prepare(buildSql, true, deployParser);
  }
  
  public void prepareRawSql(DeployParser deployParser)
  {
    prepare(true, false, deployParser);
  }
  
  private void prepare(boolean buildSql, boolean parseRaw, DeployParser deployParser)
  {
    buildBindWhereRawSql(buildSql, parseRaw, deployParser);
    buildBindHavingRawSql(buildSql, parseRaw, deployParser);
    
    SpiExpressionList<?> whereExp = this.query.getWhereExpressions();
    if (whereExp != null)
    {
      DefaultExpressionRequest whereReq = new DefaultExpressionRequest(this.request, deployParser);
      this.whereExprBindValues = whereExp.buildBindValues(whereReq);
      if (buildSql) {
        this.whereExprSql = whereExp.buildSql(whereReq);
      }
    }
    BeanPropertyAssocMany<?> manyProperty = this.request.getManyProperty();
    if (manyProperty != null)
    {
      OrmQueryProperties chunk = this.query.getDetail().getChunk(manyProperty.getName(), false);
      SpiExpressionList<?> filterMany = chunk.getFilterMany();
      if (filterMany != null)
      {
        DefaultExpressionRequest filterReq = new DefaultExpressionRequest(this.request, deployParser);
        this.filterManyExprBindValues = filterMany.buildBindValues(filterReq);
        if (buildSql) {
          this.filterManyExprSql = filterMany.buildSql(filterReq);
        }
      }
    }
    SpiExpressionList<?> havingExpr = this.query.getHavingExpressions();
    if (havingExpr != null)
    {
      DefaultExpressionRequest havingReq = new DefaultExpressionRequest(this.request, deployParser);
      this.havingExprBindValues = havingExpr.buildBindValues(havingReq);
      if (buildSql) {
        this.havingExprSql = havingExpr.buildSql(havingReq);
      }
    }
    if (buildSql) {
      parsePropertiesToDbColumns(deployParser);
    }
  }
  
  private void parsePropertiesToDbColumns(DeployParser deployParser)
  {
    this.dbWhere = deriveWhere(deployParser);
    this.dbFilterMany = deriveFilterMany(deployParser);
    this.dbHaving = deriveHaving(deployParser);
    
    this.logicalOrderBy = deriveOrderByWithMany(this.request.getManyProperty());
    if (this.logicalOrderBy != null) {
      this.dbOrderBy = deployParser.parse(this.logicalOrderBy);
    }
    this.predicateIncludes = deployParser.getIncludes();
  }
  
  private String deriveFilterMany(DeployParser deployParser)
  {
    if (isEmpty(this.filterManyExprSql)) {
      return null;
    }
    return deployParser.parse(this.filterManyExprSql);
  }
  
  private String deriveWhere(DeployParser deployParser)
  {
    return parse(this.whereRawSql, this.whereExprSql, deployParser);
  }
  
  public void parseTableAlias(SqlTreeAlias alias)
  {
    if (this.dbWhere != null) {
      this.dbWhere = alias.parseWhere(this.dbWhere);
    }
    if (this.dbFilterMany != null) {
      this.dbFilterMany = alias.parse(this.dbFilterMany);
    }
    if (this.dbHaving != null) {
      this.dbHaving = alias.parseWhere(this.dbHaving);
    }
    if (this.dbOrderBy != null) {
      this.dbOrderBy = alias.parse(this.dbOrderBy);
    }
  }
  
  private boolean isEmpty(String s)
  {
    return (s == null) || (s.length() == 0);
  }
  
  private String parse(String raw, String expr, DeployParser deployParser)
  {
    StringBuilder sb = new StringBuilder();
    if (!isEmpty(raw)) {
      sb.append(raw);
    }
    if (!isEmpty(expr))
    {
      if (sb.length() > 0) {
        sb.append(" and ");
      }
      sb.append(deployParser.parse(expr));
    }
    return sb.toString();
  }
  
  private String deriveHaving(DeployParser deployParser)
  {
    return parse(this.havingRawSql, this.havingExprSql, deployParser);
  }
  
  private String parseOrderBy()
  {
    return CQueryOrderBy.parse(this.request.getBeanDescriptor(), this.query);
  }
  
  private String deriveOrderByWithMany(BeanPropertyAssocMany<?> manyProp)
  {
    if (manyProp == null) {
      return parseOrderBy();
    }
    String orderBy = parseOrderBy();
    
    BeanDescriptor<?> desc = this.request.getBeanDescriptor();
    String orderById = desc.getDefaultOrderBy();
    if (orderBy == null) {
      orderBy = orderById;
    }
    String manyOrderBy = manyProp.getFetchOrderBy();
    if (manyOrderBy != null) {
      orderBy = orderBy + ", " + CQueryBuilder.prefixOrderByFields(manyProp.getName(), manyOrderBy);
    }
    if (this.request.isFindById()) {
      return orderBy;
    }
    if (orderBy.startsWith(orderById)) {
      return orderBy;
    }
    int manyPos = orderBy.indexOf(manyProp.getName());
    int idPos = orderBy.indexOf(" " + orderById);
    if (manyPos == -1)
    {
      if (idPos == -1) {
        return orderBy + ", " + orderById;
      }
      return orderBy;
    }
    if ((idPos <= -1) || (idPos >= manyPos))
    {
      if (idPos > manyPos)
      {
        String msg = "A Query on [" + desc + "] includes a join to a 'many' association [" + manyProp.getName();
        msg = msg + "] with an incorrect orderBy [" + orderBy + "]. The id property [" + orderById + "]";
        msg = msg + " must come before the many property [" + manyProp.getName() + "] in the orderBy.";
        msg = msg + " Ebean has automatically modified the orderBy clause to do this.";
        
        logger.log(Level.WARNING, msg);
      }
      orderBy = orderBy.substring(0, manyPos) + orderById + ", " + orderBy.substring(manyPos);
    }
    return orderBy;
  }
  
  public ArrayList<Object> getWhereExprBindValues()
  {
    return this.whereExprBindValues;
  }
  
  public String getDbHaving()
  {
    return this.dbHaving;
  }
  
  public String getDbWhere()
  {
    return this.dbWhere;
  }
  
  public String getDbFilterMany()
  {
    return this.dbFilterMany;
  }
  
  public String getDbOrderBy()
  {
    return this.dbOrderBy;
  }
  
  public Set<String> getPredicateIncludes()
  {
    return this.predicateIncludes;
  }
  
  public String getWhereRawSql()
  {
    return this.whereRawSql;
  }
  
  public String getWhereExpressionSql()
  {
    return this.whereExprSql;
  }
  
  public String getHavingRawSql()
  {
    return this.havingRawSql;
  }
  
  public String getHavingExpressionSql()
  {
    return this.havingExprSql;
  }
  
  public String getLogWhereSql()
  {
    if (this.rawSql) {
      return "";
    }
    if ((this.dbWhere == null) && (this.dbFilterMany == null)) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    if (this.dbWhere != null) {
      sb.append(this.dbWhere);
    }
    if (this.dbFilterMany != null)
    {
      if (sb.length() > 0) {
        sb.append(" and ");
      }
      sb.append(this.dbFilterMany);
    }
    String logPred = sb.toString();
    if (logPred.length() > 400) {
      logPred = logPred.substring(0, 400) + " ...";
    }
    return logPred;
  }
}
