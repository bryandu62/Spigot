package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.ExampleExpression;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Junction;
import com.avaje.ebean.LikeType;
import com.avaje.ebean.Query;
import com.avaje.ebeaninternal.api.SpiExpressionFactory;
import com.avaje.ebeaninternal.api.SpiQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DefaultExpressionFactory
  implements SpiExpressionFactory
{
  private static final Object[] EMPTY_ARRAY = new Object[0];
  private final FilterExprPath prefix;
  
  public DefaultExpressionFactory()
  {
    this(null);
  }
  
  public DefaultExpressionFactory(FilterExprPath prefix)
  {
    this.prefix = prefix;
  }
  
  public ExpressionFactory createExpressionFactory(FilterExprPath prefix)
  {
    return new DefaultExpressionFactory(prefix);
  }
  
  public String getLang()
  {
    return "sql";
  }
  
  public Expression eq(String propertyName, Object value)
  {
    if (value == null) {
      return isNull(propertyName);
    }
    return new SimpleExpression(this.prefix, propertyName, SimpleExpression.Op.EQ, value);
  }
  
  public Expression ne(String propertyName, Object value)
  {
    if (value == null) {
      return isNotNull(propertyName);
    }
    return new SimpleExpression(this.prefix, propertyName, SimpleExpression.Op.NOT_EQ, value);
  }
  
  public Expression ieq(String propertyName, String value)
  {
    if (value == null) {
      return isNull(propertyName);
    }
    return new CaseInsensitiveEqualExpression(this.prefix, propertyName, value);
  }
  
  public Expression between(String propertyName, Object value1, Object value2)
  {
    return new BetweenExpression(this.prefix, propertyName, value1, value2);
  }
  
  public Expression betweenProperties(String lowProperty, String highProperty, Object value)
  {
    return new BetweenPropertyExpression(this.prefix, lowProperty, highProperty, value);
  }
  
  public Expression gt(String propertyName, Object value)
  {
    return new SimpleExpression(this.prefix, propertyName, SimpleExpression.Op.GT, value);
  }
  
  public Expression ge(String propertyName, Object value)
  {
    return new SimpleExpression(this.prefix, propertyName, SimpleExpression.Op.GT_EQ, value);
  }
  
  public Expression lt(String propertyName, Object value)
  {
    return new SimpleExpression(this.prefix, propertyName, SimpleExpression.Op.LT, value);
  }
  
  public Expression le(String propertyName, Object value)
  {
    return new SimpleExpression(this.prefix, propertyName, SimpleExpression.Op.LT_EQ, value);
  }
  
  public Expression isNull(String propertyName)
  {
    return new NullExpression(this.prefix, propertyName, false);
  }
  
  public Expression isNotNull(String propertyName)
  {
    return new NullExpression(this.prefix, propertyName, true);
  }
  
  public ExampleExpression iexampleLike(Object example)
  {
    return new DefaultExampleExpression(this.prefix, example, true, LikeType.RAW);
  }
  
  public ExampleExpression exampleLike(Object example)
  {
    return new DefaultExampleExpression(this.prefix, example, false, LikeType.RAW);
  }
  
  public ExampleExpression exampleLike(Object example, boolean caseInsensitive, LikeType likeType)
  {
    return new DefaultExampleExpression(this.prefix, example, caseInsensitive, likeType);
  }
  
  public Expression like(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, false, LikeType.RAW);
  }
  
  public Expression ilike(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, true, LikeType.RAW);
  }
  
  public Expression startsWith(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, false, LikeType.STARTS_WITH);
  }
  
  public Expression istartsWith(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, true, LikeType.STARTS_WITH);
  }
  
  public Expression endsWith(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, false, LikeType.ENDS_WITH);
  }
  
  public Expression iendsWith(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, true, LikeType.ENDS_WITH);
  }
  
  public Expression contains(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, false, LikeType.CONTAINS);
  }
  
  public Expression icontains(String propertyName, String value)
  {
    return new LikeExpression(this.prefix, propertyName, value, true, LikeType.CONTAINS);
  }
  
  public Expression in(String propertyName, Object[] values)
  {
    return new InExpression(this.prefix, propertyName, values);
  }
  
  public Expression in(String propertyName, Query<?> subQuery)
  {
    return new InQueryExpression(this.prefix, propertyName, (SpiQuery)subQuery);
  }
  
  public Expression in(String propertyName, Collection<?> values)
  {
    return new InExpression(this.prefix, propertyName, values);
  }
  
  public Expression idEq(Object value)
  {
    return new IdExpression(value);
  }
  
  public Expression idIn(List<?> idList)
  {
    return new IdInExpression(idList);
  }
  
  public Expression allEq(Map<String, Object> propertyMap)
  {
    return new AllEqualsExpression(this.prefix, propertyMap);
  }
  
  public Expression raw(String raw, Object value)
  {
    return new RawExpression(raw, new Object[] { value });
  }
  
  public Expression raw(String raw, Object[] values)
  {
    return new RawExpression(raw, values);
  }
  
  public Expression raw(String raw)
  {
    return new RawExpression(raw, EMPTY_ARRAY);
  }
  
  public Expression and(Expression expOne, Expression expTwo)
  {
    return new LogicExpression.And(expOne, expTwo);
  }
  
  public Expression or(Expression expOne, Expression expTwo)
  {
    return new LogicExpression.Or(expOne, expTwo);
  }
  
  public Expression not(Expression exp)
  {
    return new NotExpression(exp);
  }
  
  public <T> Junction<T> conjunction(Query<T> query)
  {
    return new JunctionExpression.Conjunction(query, query.where());
  }
  
  public <T> Junction<T> disjunction(Query<T> query)
  {
    return new JunctionExpression.Disjunction(query, query.where());
  }
  
  public <T> Junction<T> conjunction(Query<T> query, ExpressionList<T> parent)
  {
    return new JunctionExpression.Conjunction(query, parent);
  }
  
  public <T> Junction<T> disjunction(Query<T> query, ExpressionList<T> parent)
  {
    return new JunctionExpression.Disjunction(query, parent);
  }
}
