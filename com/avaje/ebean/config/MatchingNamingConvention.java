package com.avaje.ebean.config;

public class MatchingNamingConvention
  extends AbstractNamingConvention
{
  public MatchingNamingConvention() {}
  
  public MatchingNamingConvention(String sequenceFormat)
  {
    super(sequenceFormat);
  }
  
  public String getColumnFromProperty(Class<?> beanClass, String propertyName)
  {
    return propertyName;
  }
  
  public TableName getTableNameByConvention(Class<?> beanClass)
  {
    return new TableName(getCatalog(), getSchema(), beanClass.getSimpleName());
  }
  
  public String getPropertyFromColumn(Class<?> beanClass, String dbColumnName)
  {
    return dbColumnName;
  }
}
