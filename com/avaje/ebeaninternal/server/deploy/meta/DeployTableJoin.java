package com.avaje.ebeaninternal.server.deploy.meta;

import com.avaje.ebeaninternal.server.core.Message;
import com.avaje.ebeaninternal.server.deploy.BeanCascadeInfo;
import com.avaje.ebeaninternal.server.deploy.BeanTable;
import java.util.ArrayList;
import javax.persistence.JoinColumn;

public class DeployTableJoin
{
  private boolean importedPrimaryKey;
  private String table;
  private String type = "join";
  private ArrayList<DeployBeanProperty> properties = new ArrayList();
  private ArrayList<DeployTableJoinColumn> columns = new ArrayList();
  private BeanCascadeInfo cascadeInfo = new BeanCascadeInfo();
  
  public String toString()
  {
    return this.type + " " + this.table + " " + this.columns;
  }
  
  public boolean isImportedPrimaryKey()
  {
    return this.importedPrimaryKey;
  }
  
  public void setImportedPrimaryKey(boolean importedPrimaryKey)
  {
    this.importedPrimaryKey = importedPrimaryKey;
  }
  
  public boolean hasJoinColumns()
  {
    return this.columns.size() > 0;
  }
  
  public BeanCascadeInfo getCascadeInfo()
  {
    return this.cascadeInfo;
  }
  
  public void setColumns(DeployTableJoinColumn[] cols, boolean reverse)
  {
    this.columns = new ArrayList();
    for (int i = 0; i < cols.length; i++) {
      addJoinColumn(cols[i].copy(reverse));
    }
  }
  
  public void addJoinColumn(DeployTableJoinColumn pair)
  {
    this.columns.add(pair);
  }
  
  public void addJoinColumn(boolean order, JoinColumn jc, BeanTable beanTable)
  {
    if (!"".equals(jc.table())) {
      setTable(jc.table());
    }
    addJoinColumn(new DeployTableJoinColumn(order, jc, beanTable));
  }
  
  public void addJoinColumn(boolean order, JoinColumn[] jcArray, BeanTable beanTable)
  {
    for (int i = 0; i < jcArray.length; i++) {
      addJoinColumn(order, jcArray[i], beanTable);
    }
  }
  
  public DeployTableJoinColumn[] columns()
  {
    return (DeployTableJoinColumn[])this.columns.toArray(new DeployTableJoinColumn[this.columns.size()]);
  }
  
  public DeployBeanProperty[] properties()
  {
    return (DeployBeanProperty[])this.properties.toArray(new DeployBeanProperty[this.properties.size()]);
  }
  
  public String getTable()
  {
    return this.table;
  }
  
  public void setTable(String table)
  {
    this.table = table;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public boolean isOuterJoin()
  {
    return this.type.equals("left outer join");
  }
  
  public void setType(String joinType)
  {
    joinType = joinType.toUpperCase();
    if (joinType.equalsIgnoreCase("join")) {
      this.type = "join";
    } else if (joinType.indexOf("LEFT") > -1) {
      this.type = "left outer join";
    } else if (joinType.indexOf("OUTER") > -1) {
      this.type = "left outer join";
    } else if (joinType.indexOf("INNER") > -1) {
      this.type = "join";
    } else {
      throw new RuntimeException(Message.msg("join.type.unknown", joinType));
    }
  }
  
  public DeployTableJoin createInverse(String tableName)
  {
    DeployTableJoin inverse = new DeployTableJoin();
    
    return copyTo(inverse, true, tableName);
  }
  
  public DeployTableJoin copyTo(DeployTableJoin destJoin, boolean reverse, String tableName)
  {
    destJoin.setTable(tableName);
    destJoin.setType(this.type);
    destJoin.setColumns(columns(), reverse);
    
    return destJoin;
  }
}
