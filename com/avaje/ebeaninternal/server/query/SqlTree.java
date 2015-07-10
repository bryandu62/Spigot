package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SqlTree
{
  private SqlTreeNode rootNode;
  private BeanPropertyAssocMany<?> manyProperty;
  private String manyPropertyName;
  private ElPropertyValue manyPropEl;
  private Set<String> includes;
  private String summary;
  private String selectSql;
  private String fromSql;
  private BeanProperty[] encryptedProps;
  private String inheritanceWhereSql;
  
  public List<String> buildSelectExpressionChain()
  {
    ArrayList<String> list = new ArrayList();
    this.rootNode.buildSelectExpressionChain(list);
    return list;
  }
  
  public Set<String> getIncludes()
  {
    return this.includes;
  }
  
  public void setIncludes(Set<String> includes)
  {
    this.includes = includes;
  }
  
  public void setManyProperty(BeanPropertyAssocMany<?> manyProperty, String manyPropertyName, ElPropertyValue manyPropEl)
  {
    this.manyProperty = manyProperty;
    this.manyPropertyName = manyPropertyName;
    this.manyPropEl = manyPropEl;
  }
  
  public String getSelectSql()
  {
    return this.selectSql;
  }
  
  public void setSelectSql(String selectSql)
  {
    this.selectSql = selectSql;
  }
  
  public String getFromSql()
  {
    return this.fromSql;
  }
  
  public void setFromSql(String fromSql)
  {
    this.fromSql = fromSql;
  }
  
  public String getInheritanceWhereSql()
  {
    return this.inheritanceWhereSql;
  }
  
  public void setInheritanceWhereSql(String whereSql)
  {
    this.inheritanceWhereSql = whereSql;
  }
  
  public void setSummary(String summary)
  {
    this.summary = summary;
  }
  
  public String getSummary()
  {
    return this.summary;
  }
  
  public SqlTreeNode getRootNode()
  {
    return this.rootNode;
  }
  
  public void setRootNode(SqlTreeNode rootNode)
  {
    this.rootNode = rootNode;
  }
  
  public BeanPropertyAssocMany<?> getManyProperty()
  {
    return this.manyProperty;
  }
  
  public String getManyPropertyName()
  {
    return this.manyPropertyName;
  }
  
  public ElPropertyValue getManyPropertyEl()
  {
    return this.manyPropEl;
  }
  
  public boolean isManyIncluded()
  {
    return this.manyProperty != null;
  }
  
  public BeanProperty[] getEncryptedProps()
  {
    return this.encryptedProps;
  }
  
  public void setEncryptedProps(BeanProperty[] encryptedProps)
  {
    this.encryptedProps = encryptedProps;
  }
}
