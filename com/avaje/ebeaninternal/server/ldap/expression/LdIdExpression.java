package com.avaje.ebeaninternal.server.ldap.expression;

import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.util.DefaultExpressionRequest;

class LdIdExpression
  extends LdAbstractExpression
  implements SpiExpression
{
  private static final long serialVersionUID = -3065936341718489843L;
  private final Object value;
  
  LdIdExpression(Object value)
  {
    super(null);
    this.value = value;
  }
  
  public void containsMany(BeanDescriptor<?> desc, ManyWhereJoins manyWhereJoin) {}
  
  public void addBindValues(SpiExpressionRequest request)
  {
    DefaultExpressionRequest r = (DefaultExpressionRequest)request;
    BeanProperty[] propertiesId = r.getBeanDescriptor().propertiesId();
    if (propertiesId.length > 1) {
      throw new RuntimeException("Only single Id property is supported for LDAP");
    }
    request.addBindValue(this.value);
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    DefaultExpressionRequest r = (DefaultExpressionRequest)request;
    BeanProperty[] propertiesId = r.getBeanDescriptor().propertiesId();
    if (propertiesId.length > 1) {
      throw new RuntimeException("Only single Id property is supported for LDAP");
    }
    String ldapProp = propertiesId[0].getDbColumn();
    request.append(ldapProp).append("=").append(nextParam(request));
  }
  
  public int queryAutoFetchHash()
  {
    return LdIdExpression.class.getName().hashCode();
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
