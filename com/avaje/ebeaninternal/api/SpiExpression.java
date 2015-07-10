package com.avaje.ebeaninternal.api;

import com.avaje.ebean.Expression;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;

public abstract interface SpiExpression
  extends Expression
{
  public abstract void containsMany(BeanDescriptor<?> paramBeanDescriptor, ManyWhereJoins paramManyWhereJoins);
  
  public abstract int queryAutoFetchHash();
  
  public abstract int queryPlanHash(BeanQueryRequest<?> paramBeanQueryRequest);
  
  public abstract int queryBindHash();
  
  public abstract void addSql(SpiExpressionRequest paramSpiExpressionRequest);
  
  public abstract void addBindValues(SpiExpressionRequest paramSpiExpressionRequest);
}
