package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.el.ElPropertyDeploy;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class AllEqualsExpression
  implements SpiExpression
{
  private static final long serialVersionUID = -8691773558205937025L;
  private final Map<String, Object> propMap;
  private final FilterExprPath pathPrefix;
  
  AllEqualsExpression(FilterExprPath pathPrefix, Map<String, Object> propMap)
  {
    this.pathPrefix = pathPrefix;
    this.propMap = propMap;
  }
  
  protected String name(String propName)
  {
    if (this.pathPrefix == null) {
      return propName;
    }
    String path = this.pathPrefix.getPath();
    if ((path == null) || (path.length() == 0)) {
      return propName;
    }
    return path + "." + propName;
  }
  
  public void containsMany(BeanDescriptor<?> desc, ManyWhereJoins manyWhereJoin)
  {
    if (this.propMap != null)
    {
      Iterator<String> it = this.propMap.keySet().iterator();
      while (it.hasNext())
      {
        String propertyName = (String)it.next();
        ElPropertyDeploy elProp = desc.getElPropertyDeploy(name(propertyName));
        if ((elProp != null) && (elProp.containsMany())) {
          manyWhereJoin.add(elProp);
        }
      }
    }
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    if (this.propMap.isEmpty()) {
      return;
    }
    Iterator<Object> it = this.propMap.values().iterator();
    while (it.hasNext())
    {
      Object value = it.next();
      if (value != null) {
        request.addBindValue(value);
      }
    }
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    if (this.propMap.isEmpty()) {
      return;
    }
    request.append("(");
    
    Set<Map.Entry<String, Object>> entries = this.propMap.entrySet();
    Iterator<Map.Entry<String, Object>> it = entries.iterator();
    
    int count = 0;
    while (it.hasNext())
    {
      Map.Entry<String, Object> entry = (Map.Entry)it.next();
      Object value = entry.getValue();
      String propName = (String)entry.getKey();
      if (count > 0) {
        request.append("and ");
      }
      request.append(name(propName));
      if (value == null) {
        request.append(" is null ");
      } else {
        request.append(" = ? ");
      }
      count++;
    }
    request.append(")");
  }
  
  public int queryAutoFetchHash()
  {
    int hc = AllEqualsExpression.class.getName().hashCode();
    Set<Map.Entry<String, Object>> entries = this.propMap.entrySet();
    Iterator<Map.Entry<String, Object>> it = entries.iterator();
    while (it.hasNext())
    {
      Map.Entry<String, Object> entry = (Map.Entry)it.next();
      Object value = entry.getValue();
      String propName = (String)entry.getKey();
      
      hc = hc * 31 + propName.hashCode();
      hc = hc * 31 + (value == null ? 0 : 1);
    }
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    return queryAutoFetchHash();
  }
  
  public int queryBindHash()
  {
    return queryAutoFetchHash();
  }
}
