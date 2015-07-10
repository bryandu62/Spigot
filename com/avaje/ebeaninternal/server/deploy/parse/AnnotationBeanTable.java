package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.TableName;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanTable;

public class AnnotationBeanTable
  extends AnnotationBase
{
  final DeployBeanTable beanTable;
  
  public AnnotationBeanTable(DeployUtil util, DeployBeanTable beanTable)
  {
    super(util);
    this.beanTable = beanTable;
  }
  
  public void parse()
  {
    TableName tableName = this.namingConvention.getTableName(this.beanTable.getBeanType());
    
    this.beanTable.setBaseTable(tableName.getQualifiedName());
  }
}
