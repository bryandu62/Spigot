package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.query.CQueryPredicates;
import com.avaje.ebeaninternal.server.query.SqlTree;
import com.avaje.ebeaninternal.server.query.SqlTreeNode;
import com.avaje.ebeaninternal.server.query.SqlTreeNodeRoot;
import com.avaje.ebeaninternal.server.query.SqlTreeProperties;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class DRawSqlSelect
{
  private static final Logger logger = Logger.getLogger(DRawSqlSelect.class.getName());
  private final BeanDescriptor<?> desc;
  private final DRawSqlColumnInfo[] selectColumns;
  private final Map<String, DRawSqlColumnInfo> columnMap;
  private final String preWhereExprSql;
  private final boolean andWhereExpr;
  private final String preHavingExprSql;
  private final boolean andHavingExpr;
  private final String orderBySql;
  private final String whereClause;
  private final String havingClause;
  private final String query;
  private final String columnMapping;
  private final String name;
  private final SqlTree sqlTree;
  private boolean withId;
  private final String tableAlias;
  
  public DRawSqlSelect(BeanDescriptor<?> desc, List<DRawSqlColumnInfo> selectColumns, String tableAlias, String preWhereExprSql, boolean andWhereExpr, String preHavingExprSql, boolean andHavingExpr, String orderBySql, DRawSqlMeta meta)
  {
    this.desc = desc;
    this.tableAlias = tableAlias;
    this.selectColumns = ((DRawSqlColumnInfo[])selectColumns.toArray(new DRawSqlColumnInfo[selectColumns.size()]));
    this.preHavingExprSql = preHavingExprSql;
    this.preWhereExprSql = preWhereExprSql;
    this.andHavingExpr = andHavingExpr;
    this.andWhereExpr = andWhereExpr;
    this.orderBySql = orderBySql;
    this.name = meta.getName();
    this.whereClause = meta.getWhere();
    this.havingClause = meta.getHaving();
    this.query = meta.getQuery();
    this.columnMapping = meta.getColumnMapping();
    
    this.sqlTree = initialise(desc);
    this.columnMap = createColumnMap(this.selectColumns);
  }
  
  private Map<String, DRawSqlColumnInfo> createColumnMap(DRawSqlColumnInfo[] selectColumns)
  {
    HashMap<String, DRawSqlColumnInfo> m = new HashMap();
    for (int i = 0; i < selectColumns.length; i++) {
      m.put(selectColumns[i].getPropertyName(), selectColumns[i]);
    }
    return m;
  }
  
  private SqlTree initialise(BeanDescriptor<?> owner)
  {
    try
    {
      return buildSqlTree(owner);
    }
    catch (Exception e)
    {
      String m = "Bug? initialising query " + this.name + " on " + owner;
      throw new RuntimeException(m, e);
    }
  }
  
  public DRawSqlColumnInfo getRawSqlColumnInfo(String propertyName)
  {
    return (DRawSqlColumnInfo)this.columnMap.get(propertyName);
  }
  
  public String getTableAlias()
  {
    return this.tableAlias;
  }
  
  private SqlTree buildSqlTree(BeanDescriptor<?> desc)
  {
    SqlTree sqlTree = new SqlTree();
    sqlTree.setSummary(desc.getName());
    
    LinkedHashSet<String> includedProps = new LinkedHashSet();
    SqlTreeProperties selectProps = new SqlTreeProperties();
    for (int i = 0; i < this.selectColumns.length; i++)
    {
      DRawSqlColumnInfo columnInfo = this.selectColumns[i];
      String propName = columnInfo.getPropertyName();
      BeanProperty beanProperty = desc.getBeanProperty(propName);
      if (beanProperty != null)
      {
        if (beanProperty.isId())
        {
          if (i > 0)
          {
            String m = "With " + desc + " query:" + this.name + " the ID is not the first column in the select. It must be...";
            throw new PersistenceException(m);
          }
          this.withId = true;
        }
        else
        {
          includedProps.add(beanProperty.getName());
          selectProps.add(beanProperty);
        }
      }
      else
      {
        String m = "Mapping for " + desc.getFullName();
        m = m + " query[" + this.name + "] column[" + columnInfo + "] index[" + i;
        m = m + "] not matched to bean property?";
        logger.log(Level.SEVERE, m);
      }
    }
    selectProps.setIncludedProperties(includedProps);
    SqlTreeNode sqlRoot = new SqlTreeNodeRoot(desc, selectProps, null, this.withId);
    sqlTree.setRootNode(sqlRoot);
    
    return sqlTree;
  }
  
  public String buildSql(String orderBy, CQueryPredicates predicates, OrmQueryRequest<?> request)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(this.preWhereExprSql);
    sb.append(" ");
    
    String dynamicWhere = null;
    if (request.getQuery().getId() != null)
    {
      BeanDescriptor<?> descriptor = request.getBeanDescriptor();
      
      dynamicWhere = descriptor.getIdBinderIdSql();
    }
    String dbWhere = predicates.getDbWhere();
    if ((dbWhere != null) && (dbWhere.length() > 0)) {
      if (dynamicWhere == null) {
        dynamicWhere = dbWhere;
      } else {
        dynamicWhere = dynamicWhere + " and " + dbWhere;
      }
    }
    if (dynamicWhere != null)
    {
      if (this.andWhereExpr) {
        sb.append(" and ");
      } else {
        sb.append(" where ");
      }
      sb.append(dynamicWhere);
      sb.append(" ");
    }
    if (this.preHavingExprSql != null)
    {
      sb.append(this.preHavingExprSql);
      sb.append(" ");
    }
    String dbHaving = predicates.getDbHaving();
    if ((dbHaving != null) && (dbHaving.length() > 0))
    {
      if (this.andHavingExpr) {
        sb.append(" and ");
      } else {
        sb.append(" having ");
      }
      sb.append(dbHaving);
      sb.append(" ");
    }
    if (orderBy != null) {
      sb.append(" order by ").append(orderBy);
    }
    return sb.toString();
  }
  
  public String getOrderBy(CQueryPredicates predicates)
  {
    String orderBy = predicates.getDbOrderBy();
    if (orderBy != null) {
      return orderBy;
    }
    return this.orderBySql;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public SqlTree getSqlTree()
  {
    return this.sqlTree;
  }
  
  public boolean isWithId()
  {
    return this.withId;
  }
  
  public String getQuery()
  {
    return this.query;
  }
  
  public String getColumnMapping()
  {
    return this.columnMapping;
  }
  
  public String getWhereClause()
  {
    return this.whereClause;
  }
  
  public String getHavingClause()
  {
    return this.havingClause;
  }
  
  public String toString()
  {
    return Arrays.toString(this.selectColumns);
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.desc;
  }
  
  public DeployParser createDeployPropertyParser()
  {
    return new DeployPropertyParserRawSql(this);
  }
}
