package com.avaje.ebeaninternal.server.deploy.parse;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.meta.DeployTableJoin;
import java.util.HashMap;

public class DeployBeanInfo<T>
{
  private final HashMap<String, DeployTableJoin> tableJoinMap = new HashMap();
  private final DeployUtil util;
  private final DeployBeanDescriptor<T> descriptor;
  
  public DeployBeanInfo(DeployUtil util, DeployBeanDescriptor<T> descriptor)
  {
    this.util = util;
    this.descriptor = descriptor;
  }
  
  public String toString()
  {
    return "" + this.descriptor;
  }
  
  public DeployBeanDescriptor<T> getDescriptor()
  {
    return this.descriptor;
  }
  
  public DeployUtil getUtil()
  {
    return this.util;
  }
  
  public DeployTableJoin getTableJoin(String tableName)
  {
    String key = tableName.toLowerCase();
    
    DeployTableJoin tableJoin = (DeployTableJoin)this.tableJoinMap.get(key);
    if (tableJoin == null)
    {
      tableJoin = new DeployTableJoin();
      tableJoin.setTable(tableName);
      tableJoin.setType("join");
      this.descriptor.addTableJoin(tableJoin);
      
      this.tableJoinMap.put(key, tableJoin);
    }
    return tableJoin;
  }
  
  public void setBeanJoinType(DeployBeanPropertyAssocOne<?> beanProp, boolean outerJoin)
  {
    String joinType = "join";
    if (outerJoin) {
      joinType = "left outer join";
    }
    DeployTableJoin tableJoin = beanProp.getTableJoin();
    tableJoin.setType(joinType);
  }
}
