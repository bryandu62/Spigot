package com.avaje.ebean.config;

public final class TableName
{
  private String catalog;
  private String schema;
  private String name;
  
  public TableName(String catalog, String schema, String name)
  {
    this.catalog = (catalog != null ? catalog.trim() : null);
    this.schema = (schema != null ? schema.trim() : null);
    this.name = (name != null ? name.trim() : null);
  }
  
  public TableName(String qualifiedTableName)
  {
    String[] split = qualifiedTableName.split("\\.");
    int len = split.length;
    if (split.length > 3)
    {
      String m = "Error splitting " + qualifiedTableName + ". Expecting at most 2 '.' characters";
      throw new RuntimeException(m);
    }
    if (len == 3) {
      this.catalog = split[0];
    }
    if (len >= 2) {
      this.schema = split[(len - 2)];
    }
    this.name = split[(len - 1)];
  }
  
  public String toString()
  {
    return getQualifiedName();
  }
  
  public String getCatalog()
  {
    return this.catalog;
  }
  
  public String getSchema()
  {
    return this.schema;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getQualifiedName()
  {
    StringBuilder buffer = new StringBuilder();
    if (this.catalog != null) {
      buffer.append(this.catalog);
    }
    if (this.schema != null)
    {
      if (buffer.length() > 0) {
        buffer.append(".");
      }
      buffer.append(this.schema);
    }
    if (buffer.length() > 0) {
      buffer.append(".");
    }
    buffer.append(this.name);
    
    return buffer.toString();
  }
  
  public boolean isValid()
  {
    return (this.name != null) && (this.name.length() > 0);
  }
}
