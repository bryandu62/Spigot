package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.el.ElPropertyDeploy;

class BetweenPropertyExpression
  implements SpiExpression
{
  private static final long serialVersionUID = 2078918165221454910L;
  private static final String BETWEEN = " between ";
  private final FilterExprPath pathPrefix;
  private final String lowProperty;
  private final String highProperty;
  private final Object value;
  
  BetweenPropertyExpression(FilterExprPath pathPrefix, String lowProperty, String highProperty, Object value)
  {
    this.pathPrefix = pathPrefix;
    this.lowProperty = lowProperty;
    this.highProperty = highProperty;
    this.value = value;
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
    ElPropertyDeploy elProp = desc.getElPropertyDeploy(name(this.lowProperty));
    if ((elProp != null) && (elProp.containsMany())) {
      manyWhereJoin.add(elProp);
    }
    elProp = desc.getElPropertyDeploy(name(this.highProperty));
    if ((elProp != null) && (elProp.containsMany())) {
      manyWhereJoin.add(elProp);
    }
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    request.addBindValue(this.value);
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    request.append(" ? ").append(" between ").append(name(this.lowProperty)).append(" and ").append(name(this.highProperty));
  }
  
  public int queryAutoFetchHash()
  {
    int hc = BetweenPropertyExpression.class.getName().hashCode();
    hc = hc * 31 + this.lowProperty.hashCode();
    hc = hc * 31 + this.highProperty.hashCode();
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    return queryAutoFetchHash();
  }
  
  public int queryBindHash()
  {
    return this.value.hashCode();
  }
}
