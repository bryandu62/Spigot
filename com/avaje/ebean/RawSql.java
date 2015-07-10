package com.avaje.ebean;

import com.avaje.ebeaninternal.util.CamelCaseHelper;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RawSql
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Sql sql;
  private final ColumnMapping columnMapping;
  
  protected RawSql(Sql sql, ColumnMapping columnMapping)
  {
    this.sql = sql;
    this.columnMapping = columnMapping;
  }
  
  public Sql getSql()
  {
    return this.sql;
  }
  
  public ColumnMapping getColumnMapping()
  {
    return this.columnMapping;
  }
  
  public int queryHash()
  {
    return 31 * this.sql.queryHash() + this.columnMapping.queryHash();
  }
  
  public static final class Sql
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private final boolean parsed;
    private final String unparsedSql;
    private final String preFrom;
    private final String preWhere;
    private final boolean andWhereExpr;
    private final String preHaving;
    private final boolean andHavingExpr;
    private final String orderBy;
    private final boolean distinct;
    private final int queryHashCode;
    
    protected Sql(String unparsedSql)
    {
      this.queryHashCode = unparsedSql.hashCode();
      this.parsed = false;
      this.unparsedSql = unparsedSql;
      this.preFrom = null;
      this.preHaving = null;
      this.preWhere = null;
      this.andHavingExpr = false;
      this.andWhereExpr = false;
      this.orderBy = null;
      this.distinct = false;
    }
    
    protected Sql(int queryHashCode, String preFrom, String preWhere, boolean andWhereExpr, String preHaving, boolean andHavingExpr, String orderBy, boolean distinct)
    {
      this.queryHashCode = queryHashCode;
      this.parsed = true;
      this.unparsedSql = null;
      this.preFrom = preFrom;
      this.preHaving = preHaving;
      this.preWhere = preWhere;
      this.andHavingExpr = andHavingExpr;
      this.andWhereExpr = andWhereExpr;
      this.orderBy = orderBy;
      this.distinct = distinct;
    }
    
    public int queryHash()
    {
      return this.queryHashCode;
    }
    
    public String toString()
    {
      if (!this.parsed) {
        return "unparsed[" + this.unparsedSql + "]";
      }
      return "select[" + this.preFrom + "] preWhere[" + this.preWhere + "] preHaving[" + this.preHaving + "] orderBy[" + this.orderBy + "]";
    }
    
    public boolean isDistinct()
    {
      return this.distinct;
    }
    
    public boolean isParsed()
    {
      return this.parsed;
    }
    
    public String getUnparsedSql()
    {
      return this.unparsedSql;
    }
    
    public String getPreFrom()
    {
      return this.preFrom;
    }
    
    public String getPreWhere()
    {
      return this.preWhere;
    }
    
    public boolean isAndWhereExpr()
    {
      return this.andWhereExpr;
    }
    
    public String getPreHaving()
    {
      return this.preHaving;
    }
    
    public boolean isAndHavingExpr()
    {
      return this.andHavingExpr;
    }
    
    public String getOrderBy()
    {
      return this.orderBy;
    }
  }
  
  public static final class ColumnMapping
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private final LinkedHashMap<String, Column> dbColumnMap;
    private final Map<String, String> propertyMap;
    private final Map<String, Column> propertyColumnMap;
    private final boolean parsed;
    private final boolean immutable;
    private final int queryHashCode;
    
    protected ColumnMapping(List<Column> columns)
    {
      this.queryHashCode = 0;
      this.immutable = false;
      this.parsed = true;
      this.propertyMap = null;
      this.propertyColumnMap = null;
      this.dbColumnMap = new LinkedHashMap();
      for (int i = 0; i < columns.size(); i++)
      {
        Column c = (Column)columns.get(i);
        this.dbColumnMap.put(c.getDbColumn(), c);
      }
    }
    
    protected ColumnMapping()
    {
      this.queryHashCode = 0;
      this.immutable = false;
      this.parsed = false;
      this.propertyMap = null;
      this.propertyColumnMap = null;
      this.dbColumnMap = new LinkedHashMap();
    }
    
    protected ColumnMapping(boolean parsed, LinkedHashMap<String, Column> dbColumnMap)
    {
      this.immutable = true;
      this.parsed = parsed;
      this.dbColumnMap = dbColumnMap;
      
      int hc = ColumnMapping.class.getName().hashCode();
      
      HashMap<String, Column> pcMap = new HashMap();
      HashMap<String, String> pMap = new HashMap();
      for (Column c : dbColumnMap.values())
      {
        pMap.put(c.getPropertyName(), c.getDbColumn());
        pcMap.put(c.getPropertyName(), c);
        
        hc = 31 * hc + c.getPropertyName() == null ? 0 : c.getPropertyName().hashCode();
        hc = 31 * hc + c.getDbColumn() == null ? 0 : c.getDbColumn().hashCode();
      }
      this.propertyMap = Collections.unmodifiableMap(pMap);
      this.propertyColumnMap = Collections.unmodifiableMap(pcMap);
      this.queryHashCode = hc;
    }
    
    protected ColumnMapping createImmutableCopy()
    {
      for (Column c : this.dbColumnMap.values()) {
        c.checkMapping();
      }
      return new ColumnMapping(this.parsed, this.dbColumnMap);
    }
    
    protected void columnMapping(String dbColumn, String propertyName)
    {
      if (this.immutable) {
        throw new IllegalStateException("Should never happen");
      }
      if (!this.parsed)
      {
        int pos = this.dbColumnMap.size();
        this.dbColumnMap.put(dbColumn, new Column(pos, dbColumn, null, propertyName, null));
      }
      else
      {
        Column column = (Column)this.dbColumnMap.get(dbColumn);
        if (column == null)
        {
          String msg = "DB Column [" + dbColumn + "] not found in mapping. Expecting one of [" + this.dbColumnMap.keySet() + "]";
          throw new IllegalArgumentException(msg);
        }
        column.setPropertyName(propertyName);
      }
    }
    
    public int queryHash()
    {
      if (this.queryHashCode == 0) {
        throw new RuntimeException("Bug: queryHashCode == 0");
      }
      return this.queryHashCode;
    }
    
    public boolean isParsed()
    {
      return this.parsed;
    }
    
    public int size()
    {
      return this.dbColumnMap.size();
    }
    
    protected Map<String, Column> mapping()
    {
      return this.dbColumnMap;
    }
    
    public Map<String, String> getMapping()
    {
      return this.propertyMap;
    }
    
    public int getIndexPosition(String property)
    {
      Column c = (Column)this.propertyColumnMap.get(property);
      return c == null ? -1 : c.getIndexPos();
    }
    
    public Iterator<Column> getColumns()
    {
      return this.dbColumnMap.values().iterator();
    }
    
    public static class Column
      implements Serializable
    {
      private static final long serialVersionUID = 1L;
      private final int indexPos;
      private final String dbColumn;
      private final String dbAlias;
      private String propertyName;
      
      public Column(int indexPos, String dbColumn, String dbAlias)
      {
        this(indexPos, dbColumn, dbAlias, derivePropertyName(dbAlias, dbColumn));
      }
      
      private Column(int indexPos, String dbColumn, String dbAlias, String propertyName)
      {
        this.indexPos = indexPos;
        this.dbColumn = dbColumn;
        this.dbAlias = dbAlias;
        if ((propertyName == null) && (dbAlias != null)) {
          this.propertyName = dbAlias;
        } else {
          this.propertyName = propertyName;
        }
      }
      
      private static String derivePropertyName(String dbAlias, String dbColumn)
      {
        if (dbAlias != null) {
          return dbAlias;
        }
        int dotPos = dbColumn.indexOf('.');
        if (dotPos > -1) {
          dbColumn = dbColumn.substring(dotPos + 1);
        }
        return CamelCaseHelper.toCamelFromUnderscore(dbColumn);
      }
      
      private void checkMapping()
      {
        if (this.propertyName == null)
        {
          String msg = "No propertyName defined (Column mapping) for dbColumn [" + this.dbColumn + "]";
          throw new IllegalStateException(msg);
        }
      }
      
      public String toString()
      {
        return this.dbColumn + "->" + this.propertyName;
      }
      
      public int getIndexPos()
      {
        return this.indexPos;
      }
      
      public String getDbColumn()
      {
        return this.dbColumn;
      }
      
      public String getDbAlias()
      {
        return this.dbAlias;
      }
      
      public String getPropertyName()
      {
        return this.propertyName;
      }
      
      private void setPropertyName(String propertyName)
      {
        this.propertyName = propertyName;
      }
    }
  }
}
