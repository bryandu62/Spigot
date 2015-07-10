package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.core.InternString;

public class BeanForeignKey
{
  private final String dbColumn;
  private final int dbType;
  
  public BeanForeignKey(String dbColumn, int dbType)
  {
    this.dbColumn = InternString.intern(dbColumn);
    this.dbType = dbType;
  }
  
  public String getDbColumn()
  {
    return this.dbColumn;
  }
  
  public int getDbType()
  {
    return this.dbType;
  }
  
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if ((obj instanceof BeanForeignKey)) {
      return obj.hashCode() == hashCode();
    }
    return false;
  }
  
  public int hashCode()
  {
    int hc = getClass().hashCode();
    hc = hc * 31 + (this.dbColumn != null ? this.dbColumn.hashCode() : 0);
    return hc;
  }
  
  public String toString()
  {
    return this.dbColumn;
  }
}
