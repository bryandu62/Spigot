package com.avaje.ebeaninternal.server.ldap.expression;

import com.avaje.ebean.Expression;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;

abstract class LdLogicExpression
  implements SpiExpression
{
  private static final long serialVersionUID = 616860781960645251L;
  static final String AND = "&";
  static final String OR = "|";
  private final SpiExpression expOne;
  private final SpiExpression expTwo;
  private final String joinType;
  
  static class And
    extends LdLogicExpression
  {
    private static final long serialVersionUID = -3832889676798526445L;
    
    And(Expression expOne, Expression expTwo)
    {
      super(expOne, expTwo);
    }
  }
  
  static class Or
    extends LdLogicExpression
  {
    private static final long serialVersionUID = -6871993143194094810L;
    
    Or(Expression expOne, Expression expTwo)
    {
      super(expOne, expTwo);
    }
  }
  
  LdLogicExpression(String joinType, Expression expOne, Expression expTwo)
  {
    this.joinType = joinType;
    this.expOne = ((SpiExpression)expOne);
    this.expTwo = ((SpiExpression)expTwo);
  }
  
  public void containsMany(BeanDescriptor<?> desc, ManyWhereJoins manyWhereJoin)
  {
    this.expOne.containsMany(desc, manyWhereJoin);
    this.expTwo.containsMany(desc, manyWhereJoin);
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    this.expOne.addBindValues(request);
    this.expTwo.addBindValues(request);
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    request.append("(");
    request.append(this.joinType);
    this.expOne.addSql(request);
    this.expTwo.addSql(request);
    request.append(") ");
  }
  
  public int queryAutoFetchHash()
  {
    int hc = LdLogicExpression.class.getName().hashCode() + this.joinType.hashCode();
    hc = hc * 31 + this.expOne.queryAutoFetchHash();
    hc = hc * 31 + this.expTwo.queryAutoFetchHash();
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    int hc = LdLogicExpression.class.getName().hashCode() + this.joinType.hashCode();
    hc = hc * 31 + this.expOne.queryPlanHash(request);
    hc = hc * 31 + this.expTwo.queryPlanHash(request);
    return hc;
  }
  
  public int queryBindHash()
  {
    int hc = this.expOne.queryBindHash();
    hc = hc * 31 + this.expTwo.queryBindHash();
    return hc;
  }
}
