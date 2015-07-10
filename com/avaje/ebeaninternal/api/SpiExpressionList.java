package com.avaje.ebeaninternal.api;

import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.ArrayList;
import java.util.List;

public abstract interface SpiExpressionList<T>
  extends ExpressionList<T>
{
  public abstract List<SpiExpression> getUnderlyingList();
  
  public abstract void trimPath(int paramInt);
  
  public abstract void setExpressionFactory(ExpressionFactory paramExpressionFactory);
  
  public abstract void containsMany(BeanDescriptor<?> paramBeanDescriptor, ManyWhereJoins paramManyWhereJoins);
  
  public abstract boolean isEmpty();
  
  public abstract String buildSql(SpiExpressionRequest paramSpiExpressionRequest);
  
  public abstract ArrayList<Object> buildBindValues(SpiExpressionRequest paramSpiExpressionRequest);
  
  public abstract int queryPlanHash(BeanQueryRequest<?> paramBeanQueryRequest);
}
