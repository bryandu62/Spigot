package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.util.DefaultExpressionRequest;
import java.util.List;

public class IdInExpression
  implements SpiExpression
{
  private static final long serialVersionUID = 1L;
  private final List<?> idList;
  
  public IdInExpression(List<?> idList)
  {
    this.idList = idList;
  }
  
  public void containsMany(BeanDescriptor<?> desc, ManyWhereJoins manyWhereJoin) {}
  
  public void addBindValues(SpiExpressionRequest request)
  {
    DefaultExpressionRequest r = (DefaultExpressionRequest)request;
    BeanDescriptor<?> descriptor = r.getBeanDescriptor();
    IdBinder idBinder = descriptor.getIdBinder();
    for (int i = 0; i < this.idList.size(); i++) {
      idBinder.addIdInBindValue(request, this.idList.get(i));
    }
  }
  
  public void addSqlNoAlias(SpiExpressionRequest request)
  {
    DefaultExpressionRequest r = (DefaultExpressionRequest)request;
    BeanDescriptor<?> descriptor = r.getBeanDescriptor();
    IdBinder idBinder = descriptor.getIdBinder();
    
    request.append(descriptor.getIdBinder().getBindIdInSql(null));
    String inClause = idBinder.getIdInValueExpr(this.idList.size());
    request.append(inClause);
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    DefaultExpressionRequest r = (DefaultExpressionRequest)request;
    BeanDescriptor<?> descriptor = r.getBeanDescriptor();
    IdBinder idBinder = descriptor.getIdBinder();
    
    request.append(descriptor.getIdBinderInLHSSql());
    String inClause = idBinder.getIdInValueExpr(this.idList.size());
    request.append(inClause);
  }
  
  public int queryAutoFetchHash()
  {
    int hc = IdInExpression.class.getName().hashCode();
    hc = hc * 31 + this.idList.size();
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    return queryAutoFetchHash();
  }
  
  public int queryBindHash()
  {
    return this.idList.hashCode();
  }
}
