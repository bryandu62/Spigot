package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.ExampleExpression;
import com.avaje.ebean.LikeType;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.util.ArrayList;
import java.util.Iterator;

public class DefaultExampleExpression
  implements SpiExpression, ExampleExpression
{
  private static final long serialVersionUID = 1L;
  private final Object entity;
  private boolean caseInsensitive;
  private LikeType likeType;
  private boolean includeZeros;
  private ArrayList<SpiExpression> list;
  private final FilterExprPath pathPrefix;
  
  public DefaultExampleExpression(FilterExprPath pathPrefix, Object entity, boolean caseInsensitive, LikeType likeType)
  {
    this.pathPrefix = pathPrefix;
    this.entity = entity;
    this.caseInsensitive = caseInsensitive;
    this.likeType = likeType;
  }
  
  public void containsMany(BeanDescriptor<?> desc, ManyWhereJoins whereManyJoins)
  {
    if (this.list != null) {
      for (int i = 0; i < this.list.size(); i++) {
        ((SpiExpression)this.list.get(i)).containsMany(desc, whereManyJoins);
      }
    }
  }
  
  public ExampleExpression includeZeros()
  {
    this.includeZeros = true;
    return this;
  }
  
  public ExampleExpression caseInsensitive()
  {
    this.caseInsensitive = true;
    return this;
  }
  
  public ExampleExpression useStartsWith()
  {
    this.likeType = LikeType.STARTS_WITH;
    return this;
  }
  
  public ExampleExpression useContains()
  {
    this.likeType = LikeType.CONTAINS;
    return this;
  }
  
  public ExampleExpression useEndsWith()
  {
    this.likeType = LikeType.ENDS_WITH;
    return this;
  }
  
  public ExampleExpression useEqualTo()
  {
    this.likeType = LikeType.EQUAL_TO;
    return this;
  }
  
  public String getPropertyName()
  {
    return null;
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    for (int i = 0; i < this.list.size(); i++)
    {
      SpiExpression item = (SpiExpression)this.list.get(i);
      item.addBindValues(request);
    }
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    if (!this.list.isEmpty())
    {
      request.append("(");
      for (int i = 0; i < this.list.size(); i++)
      {
        SpiExpression item = (SpiExpression)this.list.get(i);
        if (i > 0) {
          request.append(" and ");
        }
        item.addSql(request);
      }
      request.append(") ");
    }
  }
  
  public int queryAutoFetchHash()
  {
    return DefaultExampleExpression.class.getName().hashCode();
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    this.list = buildExpressions(request);
    
    int hc = DefaultExampleExpression.class.getName().hashCode();
    for (int i = 0; i < this.list.size(); i++) {
      hc = hc * 31 + ((SpiExpression)this.list.get(i)).queryPlanHash(request);
    }
    return hc;
  }
  
  public int queryBindHash()
  {
    int hc = DefaultExampleExpression.class.getName().hashCode();
    for (int i = 0; i < this.list.size(); i++) {
      hc = hc * 31 + ((SpiExpression)this.list.get(i)).queryBindHash();
    }
    return hc;
  }
  
  private ArrayList<SpiExpression> buildExpressions(BeanQueryRequest<?> request)
  {
    ArrayList<SpiExpression> list = new ArrayList();
    
    OrmQueryRequest<?> r = (OrmQueryRequest)request;
    BeanDescriptor<?> beanDescriptor = r.getBeanDescriptor();
    
    Iterator<BeanProperty> propIter = beanDescriptor.propertiesAll();
    while (propIter.hasNext())
    {
      BeanProperty beanProperty = (BeanProperty)propIter.next();
      String propName = beanProperty.getName();
      Object value = beanProperty.getValue(this.entity);
      if ((beanProperty.isScalar()) && (value != null)) {
        if ((value instanceof String)) {
          list.add(new LikeExpression(this.pathPrefix, propName, (String)value, this.caseInsensitive, this.likeType));
        } else if ((this.includeZeros) || (!isZero(value))) {
          list.add(new SimpleExpression(this.pathPrefix, propName, SimpleExpression.Op.EQ, value));
        }
      }
    }
    return list;
  }
  
  private boolean isZero(Object value)
  {
    if ((value instanceof Number))
    {
      Number num = (Number)value;
      double doubleValue = num.doubleValue();
      if (doubleValue == 0.0D) {
        return true;
      }
    }
    return false;
  }
}
