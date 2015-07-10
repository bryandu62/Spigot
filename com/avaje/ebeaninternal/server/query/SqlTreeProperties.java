package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SqlTreeProperties
{
  Set<String> includedProps;
  boolean readOnly;
  boolean includeId = true;
  TableJoin[] tableJoins = new TableJoin[0];
  List<BeanProperty> propsList = new ArrayList();
  LinkedHashSet<String> propNames = new LinkedHashSet();
  
  public boolean containsProperty(String propName)
  {
    return this.propNames.contains(propName);
  }
  
  public void add(BeanProperty[] props)
  {
    for (BeanProperty beanProperty : props) {
      this.propsList.add(beanProperty);
    }
  }
  
  public void add(BeanProperty prop)
  {
    this.propsList.add(prop);
    this.propNames.add(prop.getName());
  }
  
  public BeanProperty[] getProps()
  {
    return (BeanProperty[])this.propsList.toArray(new BeanProperty[this.propsList.size()]);
  }
  
  public boolean isIncludeId()
  {
    return this.includeId;
  }
  
  public void setIncludeId(boolean includeId)
  {
    this.includeId = includeId;
  }
  
  public boolean isPartialObject()
  {
    return this.includedProps != null;
  }
  
  public Set<String> getIncludedProperties()
  {
    return this.includedProps;
  }
  
  public void setIncludedProperties(Set<String> includedProps)
  {
    this.includedProps = includedProps;
  }
  
  public boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }
  
  public TableJoin[] getTableJoins()
  {
    return this.tableJoins;
  }
  
  public void setTableJoins(TableJoin[] tableJoins)
  {
    this.tableJoins = tableJoins;
  }
}
