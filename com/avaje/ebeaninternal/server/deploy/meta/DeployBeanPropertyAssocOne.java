package com.avaje.ebeaninternal.server.deploy.meta;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor.EntityType;

public class DeployBeanPropertyAssocOne<T>
  extends DeployBeanPropertyAssoc<T>
{
  boolean oneToOne;
  boolean oneToOneExported;
  boolean importedPrimaryKey;
  DeployBeanEmbedded deployEmbedded;
  
  public DeployBeanPropertyAssocOne(DeployBeanDescriptor<?> desc, Class<T> targetType)
  {
    super(desc, targetType);
  }
  
  public DeployBeanEmbedded getDeployEmbedded()
  {
    if (this.deployEmbedded == null) {
      this.deployEmbedded = new DeployBeanEmbedded();
    }
    return this.deployEmbedded;
  }
  
  public String getDbColumn()
  {
    DeployTableJoinColumn[] columns = this.tableJoin.columns();
    if (columns.length == 1) {
      return columns[0].getLocalDbColumn();
    }
    return super.getDbColumn();
  }
  
  public String getElPlaceHolder(BeanDescriptor.EntityType et)
  {
    return super.getElPlaceHolder(et);
  }
  
  public boolean isOneToOne()
  {
    return this.oneToOne;
  }
  
  public void setOneToOne(boolean oneToOne)
  {
    this.oneToOne = oneToOne;
  }
  
  public boolean isOneToOneExported()
  {
    return this.oneToOneExported;
  }
  
  public void setOneToOneExported(boolean oneToOneExported)
  {
    this.oneToOneExported = oneToOneExported;
  }
  
  public boolean isImportedPrimaryKey()
  {
    return this.importedPrimaryKey;
  }
  
  public void setImportedPrimaryKey(boolean importedPrimaryKey)
  {
    this.importedPrimaryKey = importedPrimaryKey;
  }
}
