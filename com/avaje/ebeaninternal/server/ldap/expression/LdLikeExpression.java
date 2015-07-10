package com.avaje.ebeaninternal.server.ldap.expression;

import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;

class LdLikeExpression
  extends LdAbstractExpression
{
  private static final long serialVersionUID = 4091359751840929076L;
  private final String value;
  
  public LdLikeExpression(String propertyName, String value)
  {
    super(propertyName);
    this.value = value;
  }
  
  public String getPropertyName()
  {
    return this.propertyName;
  }
  
  public void addBindValues(SpiExpressionRequest request) {}
  
  public void addSql(SpiExpressionRequest request)
  {
    String escapedValue;
    String escapedValue;
    if (this.value == null) {
      escapedValue = "*";
    } else {
      escapedValue = LdEscape.forLike(this.value);
    }
    String parsed = request.parseDeploy(this.propertyName);
    
    request.append("(").append(parsed).append("=").append(escapedValue).append(")");
  }
  
  public int queryAutoFetchHash()
  {
    int hc = LdLikeExpression.class.getName().hashCode();
    hc = hc * 31 + this.propertyName.hashCode();
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
