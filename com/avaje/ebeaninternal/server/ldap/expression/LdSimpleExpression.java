package com.avaje.ebeaninternal.server.ldap.expression;

import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.type.ScalarType;

class LdSimpleExpression
  extends LdAbstractExpression
{
  private static final long serialVersionUID = 4091359751840929075L;
  private final Op type;
  private final Object value;
  
  static enum Op
  {
    EQ,  NOT_EQ,  LT,  LT_EQ,  GT,  GT_EQ;
    
    private Op() {}
  }
  
  public LdSimpleExpression(String propertyName, Op type, Object value)
  {
    super(propertyName);
    this.type = type;
    this.value = value;
  }
  
  public String getPropertyName()
  {
    return this.propertyName;
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    ElPropertyValue prop = getElProp(request);
    if (prop != null)
    {
      if (prop.isAssocId())
      {
        Object[] ids = prop.getAssocOneIdValues(this.value);
        if (ids != null) {
          for (int i = 0; i < ids.length; i++) {
            request.addBindValue(ids[i]);
          }
        }
        return;
      }
      ScalarType<?> scalarType = prop.getBeanProperty().getScalarType();
      Object v = scalarType.toJdbcType(this.value);
      request.addBindValue(v);
    }
    else
    {
      request.addBindValue(this.value);
    }
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    ElPropertyValue prop = getElProp(request);
    if ((prop != null) && 
      (prop.isAssocId()))
    {
      String rawExpr = prop.getAssocOneIdExpr(this.propertyName, this.type.toString());
      String parsed = request.parseDeploy(rawExpr);
      request.append(parsed);
      return;
    }
    String parsed = request.parseDeploy(this.propertyName);
    
    request.append("(").append(parsed).append("").append(this.type.toString()).append(nextParam(request)).append(")");
  }
  
  public int queryAutoFetchHash()
  {
    int hc = LdSimpleExpression.class.getName().hashCode();
    hc = hc * 31 + this.propertyName.hashCode();
    hc = hc * 31 + this.type.name().hashCode();
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
