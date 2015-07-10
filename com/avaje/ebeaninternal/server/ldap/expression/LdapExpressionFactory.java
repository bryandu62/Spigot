package com.avaje.ebeaninternal.server.ldap.expression;

import com.avaje.ebean.ExampleExpression;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Junction;
import com.avaje.ebean.LikeType;
import com.avaje.ebean.Query;
import com.avaje.ebeaninternal.server.ldap.LdapPersistenceException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LdapExpressionFactory
  implements ExpressionFactory
{
  public String getLang()
  {
    return "ldap";
  }
  
  public ExpressionFactory createExpressionFactory(String path)
  {
    return new LdapExpressionFactory();
  }
  
  public Expression allEq(Map<String, Object> propertyMap)
  {
    Junction conjunction = new LdJunctionExpression.Conjunction(this);
    
    Iterator<Map.Entry<String, Object>> it = propertyMap.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<String, Object> entry = (Map.Entry)it.next();
      conjunction.add(eq((String)entry.getKey(), entry.getValue()));
    }
    return conjunction;
  }
  
  public Expression and(Expression expOne, Expression expTwo)
  {
    return new LdLogicExpression.And(expOne, expTwo);
  }
  
  public Expression between(String propertyName, Object value1, Object value2)
  {
    Expression e1 = gt(propertyName, value1);
    Expression e2 = lt(propertyName, value2);
    return and(e1, e2);
  }
  
  public Expression betweenProperties(String lowProperty, String highProperty, Object value)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public Expression contains(String propertyName, String value)
  {
    if (!value.endsWith("*")) {
      value = "*" + value + "*";
    }
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.EQ, value);
  }
  
  public <T> Junction<T> conjunction(Query<T> query)
  {
    return new LdJunctionExpression.Conjunction(query, query.where());
  }
  
  public <T> Junction<T> disjunction(Query<T> query)
  {
    return new LdJunctionExpression.Disjunction(query, query.where());
  }
  
  public <T> Junction<T> conjunction(Query<T> query, ExpressionList<T> parent)
  {
    return new LdJunctionExpression.Conjunction(query, parent);
  }
  
  public <T> Junction<T> disjunction(Query<T> query, ExpressionList<T> parent)
  {
    return new LdJunctionExpression.Disjunction(query, parent);
  }
  
  public Expression endsWith(String propertyName, String value)
  {
    if (!value.startsWith("*")) {
      value = "*" + value;
    }
    return new LdLikeExpression(propertyName, value);
  }
  
  public Expression eq(String propertyName, Object value)
  {
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.EQ, value);
  }
  
  public Expression lucene(String propertyName, String value)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public Expression lucene(String value)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public ExampleExpression exampleLike(Object example, boolean caseInsensitive, LikeType likeType)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public ExampleExpression exampleLike(Object example)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public Expression ge(String propertyName, Object value)
  {
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.GT_EQ, value);
  }
  
  public Expression gt(String propertyName, Object value)
  {
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.GT, value);
  }
  
  public Expression icontains(String propertyName, String value)
  {
    if (!value.endsWith("*")) {
      value = "*" + value + "*";
    }
    return new LdLikeExpression(propertyName, value);
  }
  
  public Expression idEq(Object value)
  {
    return null;
  }
  
  public Expression idIn(List<?> idList)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public Expression iendsWith(String propertyName, String value)
  {
    if (!value.startsWith("*")) {
      value = "*" + value;
    }
    return new LdLikeExpression(propertyName, value);
  }
  
  public Expression ieq(String propertyName, String value)
  {
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.EQ, value);
  }
  
  public ExampleExpression iexampleLike(Object example)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public Expression ilike(String propertyName, String value)
  {
    return new LdLikeExpression(propertyName, value);
  }
  
  public Expression in(String propertyName, Collection<?> values)
  {
    if ((values == null) || (values.isEmpty())) {
      throw new LdapPersistenceException("collection can't be empty for Ldap");
    }
    Junction disjunction = new LdJunctionExpression.Disjunction(this);
    for (Object v : values) {
      disjunction.add(eq(propertyName, v));
    }
    return disjunction;
  }
  
  public Expression in(String propertyName, Object[] values)
  {
    if ((values == null) || (values.length == 0)) {
      throw new LdapPersistenceException("values can't be empty for Ldap");
    }
    Junction disjunction = new LdJunctionExpression.Disjunction(this);
    for (Object v : values) {
      disjunction.add(eq(propertyName, v));
    }
    return disjunction;
  }
  
  public Expression in(String propertyName, Query<?> subQuery)
  {
    throw new RuntimeException("Not Implemented");
  }
  
  public Expression isNotNull(String propertyName)
  {
    return new LdPresentExpression(propertyName);
  }
  
  public Expression isNull(String propertyName)
  {
    LdPresentExpression exp = new LdPresentExpression(propertyName);
    return new LdNotExpression(exp);
  }
  
  public Expression istartsWith(String propertyName, String value)
  {
    if (!value.endsWith("*")) {
      value = value + "*";
    }
    return new LdLikeExpression(propertyName, value);
  }
  
  public Expression le(String propertyName, Object value)
  {
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.LT_EQ, value);
  }
  
  public Expression like(String propertyName, String value)
  {
    return new LdLikeExpression(propertyName, value);
  }
  
  public Expression lt(String propertyName, Object value)
  {
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.LT, value);
  }
  
  public Expression ne(String propertyName, Object value)
  {
    return new LdSimpleExpression(propertyName, LdSimpleExpression.Op.NOT_EQ, value);
  }
  
  public Expression not(Expression exp)
  {
    return new LdNotExpression(exp);
  }
  
  public Expression or(Expression expOne, Expression expTwo)
  {
    return new LdLogicExpression.Or(expOne, expTwo);
  }
  
  public Expression raw(String raw, Object value)
  {
    if (value != null) {
      return new LdRawExpression(raw, new Object[] { value });
    }
    return new LdRawExpression(raw, null);
  }
  
  public Expression raw(String raw, Object[] values)
  {
    return new LdRawExpression(raw, values);
  }
  
  public Expression raw(String raw)
  {
    return new LdRawExpression(raw, null);
  }
  
  public Expression startsWith(String propertyName, String value)
  {
    if (!value.endsWith("*")) {
      value = value + "*";
    }
    return new LdLikeExpression(propertyName, value);
  }
}
