package com.avaje.ebeaninternal.server.deploy.meta;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanTable;
import javax.persistence.JoinColumn;

public class DeployTableJoinColumn
{
  String localDbColumn;
  String foreignDbColumn;
  boolean insertable;
  boolean updateable;
  
  public DeployTableJoinColumn(String localDbColumn, String foreignDbColumn)
  {
    this(localDbColumn, foreignDbColumn, true, true);
  }
  
  public DeployTableJoinColumn(String localDbColumn, String foreignDbColumn, boolean insertable, boolean updateable)
  {
    this.localDbColumn = nullEmptyString(localDbColumn);
    this.foreignDbColumn = nullEmptyString(foreignDbColumn);
    this.insertable = insertable;
    this.updateable = updateable;
  }
  
  public DeployTableJoinColumn(boolean order, JoinColumn jc, BeanTable beanTable)
  {
    this(jc.referencedColumnName(), jc.name(), jc.insertable(), jc.updatable());
    setReferencedColumn(beanTable);
    if (!order) {
      reverse();
    }
  }
  
  private void setReferencedColumn(BeanTable beanTable)
  {
    if (this.localDbColumn == null)
    {
      BeanProperty[] idProperties = beanTable.getIdProperties();
      if (idProperties.length == 1) {
        this.localDbColumn = idProperties[0].getDbColumn();
      }
    }
  }
  
  public DeployTableJoinColumn reverse()
  {
    String temp = this.localDbColumn;
    this.localDbColumn = this.foreignDbColumn;
    this.foreignDbColumn = temp;
    return this;
  }
  
  private String nullEmptyString(String s)
  {
    if ("".equals(s)) {
      return null;
    }
    return s;
  }
  
  public DeployTableJoinColumn copy(boolean reverse)
  {
    if (reverse) {
      return new DeployTableJoinColumn(this.foreignDbColumn, this.localDbColumn, this.insertable, this.updateable);
    }
    return new DeployTableJoinColumn(this.localDbColumn, this.foreignDbColumn, this.insertable, this.updateable);
  }
  
  public String toString()
  {
    return this.localDbColumn + " = " + this.foreignDbColumn;
  }
  
  public boolean hasNullColumn()
  {
    return (this.localDbColumn == null) || (this.foreignDbColumn == null);
  }
  
  public String getNonNullColumn()
  {
    if ((this.localDbColumn == null) && (this.foreignDbColumn == null)) {
      throw new IllegalStateException("expecting only one null column?");
    }
    if ((this.localDbColumn != null) && (this.foreignDbColumn != null)) {
      throw new IllegalStateException("expecting one null column?");
    }
    if (this.localDbColumn != null) {
      return this.localDbColumn;
    }
    return this.foreignDbColumn;
  }
  
  public boolean isInsertable()
  {
    return this.insertable;
  }
  
  public boolean isUpdateable()
  {
    return this.updateable;
  }
  
  public String getForeignDbColumn()
  {
    return this.foreignDbColumn;
  }
  
  public void setForeignDbColumn(String foreignDbColumn)
  {
    this.foreignDbColumn = foreignDbColumn;
  }
  
  public String getLocalDbColumn()
  {
    return this.localDbColumn;
  }
  
  public void setLocalDbColumn(String localDbColumn)
  {
    this.localDbColumn = localDbColumn;
  }
}
