package com.avaje.ebean.config;

import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.DbDdlSyntax;
import java.util.logging.Logger;
import javax.persistence.Inheritance;
import javax.persistence.Table;

public abstract class AbstractNamingConvention
  implements NamingConvention
{
  private static final Logger logger = Logger.getLogger(AbstractNamingConvention.class.getName());
  public static final String DEFAULT_SEQ_FORMAT = "{table}_seq";
  public static final String TABLE_PKCOLUMN_SEQ_FORMAT = "{table}_{column}_seq";
  private String catalog;
  private String schema;
  private String sequenceFormat;
  protected DatabasePlatform databasePlatform;
  protected int maxConstraintNameLength;
  protected int rhsPrefixLength = 3;
  protected boolean useForeignKeyPrefix = true;
  
  public AbstractNamingConvention(String sequenceFormat, boolean useForeignKeyPrefix)
  {
    this.sequenceFormat = sequenceFormat;
    this.useForeignKeyPrefix = useForeignKeyPrefix;
  }
  
  public AbstractNamingConvention(String sequenceFormat)
  {
    this.sequenceFormat = sequenceFormat;
  }
  
  public AbstractNamingConvention()
  {
    this("{table}_seq");
  }
  
  public void setDatabasePlatform(DatabasePlatform databasePlatform)
  {
    this.databasePlatform = databasePlatform;
    this.maxConstraintNameLength = databasePlatform.getDbDdlSyntax().getMaxConstraintNameLength();
    
    logger.finer("Using maxConstraintNameLength of " + this.maxConstraintNameLength);
  }
  
  public String getSequenceName(String tableName, String pkColumn)
  {
    String s = this.sequenceFormat.replace("{table}", tableName);
    if (pkColumn == null) {
      pkColumn = "";
    }
    return s.replace("{column}", pkColumn);
  }
  
  public String getCatalog()
  {
    return this.catalog;
  }
  
  public void setCatalog(String catalog)
  {
    this.catalog = catalog;
  }
  
  public String getSchema()
  {
    return this.schema;
  }
  
  public void setSchema(String schema)
  {
    this.schema = schema;
  }
  
  public String getSequenceFormat()
  {
    return this.sequenceFormat;
  }
  
  public void setSequenceFormat(String sequenceFormat)
  {
    this.sequenceFormat = sequenceFormat;
  }
  
  public boolean isUseForeignKeyPrefix()
  {
    return this.useForeignKeyPrefix;
  }
  
  public void setUseForeignKeyPrefix(boolean useForeignKeyPrefix)
  {
    this.useForeignKeyPrefix = useForeignKeyPrefix;
  }
  
  protected abstract TableName getTableNameByConvention(Class<?> paramClass);
  
  public TableName getTableName(Class<?> beanClass)
  {
    TableName tableName = getTableNameFromAnnotation(beanClass);
    if (tableName == null)
    {
      Class<?> supCls = beanClass.getSuperclass();
      Inheritance inheritance = (Inheritance)supCls.getAnnotation(Inheritance.class);
      if (inheritance != null) {
        return getTableName(supCls);
      }
      tableName = getTableNameByConvention(beanClass);
    }
    String catalog = tableName.getCatalog();
    if (isEmpty(catalog)) {
      catalog = getCatalog();
    }
    String schema = tableName.getSchema();
    if (isEmpty(schema)) {
      schema = getSchema();
    }
    return new TableName(catalog, schema, tableName.getName());
  }
  
  public TableName getM2MJoinTableName(TableName lhsTable, TableName rhsTable)
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append(lhsTable.getName());
    buffer.append("_");
    
    String rhsTableName = rhsTable.getName();
    if (rhsTableName.indexOf('_') < this.rhsPrefixLength) {
      rhsTableName = rhsTableName.substring(rhsTableName.indexOf('_') + 1);
    }
    buffer.append(rhsTableName);
    
    int maxConstraintNameLength = this.databasePlatform.getDbDdlSyntax().getMaxConstraintNameLength();
    if (buffer.length() > maxConstraintNameLength) {
      buffer.setLength(maxConstraintNameLength);
    }
    return new TableName(lhsTable.getCatalog(), lhsTable.getSchema(), buffer.toString());
  }
  
  protected TableName getTableNameFromAnnotation(Class<?> beanClass)
  {
    Table t = findTableAnnotation(beanClass);
    if ((t != null) && (!isEmpty(t.name()))) {
      return new TableName(quoteIdentifiers(t.catalog()), quoteIdentifiers(t.schema()), quoteIdentifiers(t.name()));
    }
    return null;
  }
  
  protected Table findTableAnnotation(Class<?> cls)
  {
    if (cls.equals(Object.class)) {
      return null;
    }
    Table table = (Table)cls.getAnnotation(Table.class);
    if (table != null) {
      return table;
    }
    return findTableAnnotation(cls.getSuperclass());
  }
  
  protected String quoteIdentifiers(String s)
  {
    return this.databasePlatform.convertQuotedIdentifiers(s);
  }
  
  protected boolean isEmpty(String s)
  {
    if ((s == null) || (s.trim().length() == 0)) {
      return true;
    }
    return false;
  }
}
