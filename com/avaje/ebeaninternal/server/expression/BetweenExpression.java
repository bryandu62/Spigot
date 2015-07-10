package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;

class BetweenExpression
  extends AbstractExpression
{
  private static final long serialVersionUID = 2078918165221454910L;
  private static final String BETWEEN = " between ";
  private final Object valueHigh;
  private final Object valueLow;
  
  BetweenExpression(FilterExprPath pathPrefix, String propertyName, Object valLo, Object valHigh)
  {
    super(pathPrefix, propertyName);
    this.valueLow = valLo;
    this.valueHigh = valHigh;
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    request.addBindValue(this.valueLow);
    request.addBindValue(this.valueHigh);
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    request.append(getPropertyName()).append(" between ").append(" ? and ? ");
  }
  
  public int queryAutoFetchHash()
  {
    int hc = BetweenExpression.class.getName().hashCode();
    hc = hc * 31 + this.propName.hashCode();
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    return queryAutoFetchHash();
  }
  
  public int queryBindHash()
  {
    int hc = this.valueLow.hashCode();
    hc = hc * 31 + this.valueHigh.hashCode();
    return hc;
  }
}
