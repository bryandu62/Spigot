package com.avaje.ebeaninternal.server.el;

import com.avaje.ebean.Filter;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class ElFilter<T>
  implements Filter<T>
{
  private final BeanDescriptor<T> beanDescriptor;
  private ArrayList<ElMatcher<T>> matches = new ArrayList();
  private int maxRows;
  private String sortByClause;
  
  public ElFilter(BeanDescriptor<T> beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
  }
  
  private Object convertValue(String propertyName, Object value)
  {
    ElPropertyValue elGetValue = this.beanDescriptor.getElGetValue(propertyName);
    return elGetValue.elConvertType(value);
  }
  
  private ElComparator<T> getElComparator(String propertyName)
  {
    return this.beanDescriptor.getElComparator(propertyName);
  }
  
  private ElPropertyValue getElGetValue(String propertyName)
  {
    return this.beanDescriptor.getElGetValue(propertyName);
  }
  
  public Filter<T> sort(String sortByClause)
  {
    this.sortByClause = sortByClause;
    return this;
  }
  
  protected boolean isMatch(T bean)
  {
    for (int i = 0; i < this.matches.size(); i++)
    {
      ElMatcher<T> matcher = (ElMatcher)this.matches.get(i);
      if (!matcher.isMatch(bean)) {
        return false;
      }
    }
    return true;
  }
  
  public Filter<T> in(String propertyName, Set<?> matchingValues)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    
    this.matches.add(new ElMatchBuilder.InSet(matchingValues, elGetValue));
    return this;
  }
  
  public Filter<T> eq(String propertyName, Object value)
  {
    value = convertValue(propertyName, value);
    ElComparator<T> comparator = getElComparator(propertyName);
    
    this.matches.add(new ElMatchBuilder.Eq(value, comparator));
    return this;
  }
  
  public Filter<T> ne(String propertyName, Object value)
  {
    value = convertValue(propertyName, value);
    ElComparator<T> comparator = getElComparator(propertyName);
    
    this.matches.add(new ElMatchBuilder.Ne(value, comparator));
    return this;
  }
  
  public Filter<T> between(String propertyName, Object min, Object max)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    min = elGetValue.elConvertType(min);
    max = elGetValue.elConvertType(max);
    
    ElComparator<T> elComparator = getElComparator(propertyName);
    
    this.matches.add(new ElMatchBuilder.Between(min, max, elComparator));
    return this;
  }
  
  public Filter<T> gt(String propertyName, Object value)
  {
    value = convertValue(propertyName, value);
    ElComparator<T> comparator = getElComparator(propertyName);
    
    this.matches.add(new ElMatchBuilder.Gt(value, comparator));
    return this;
  }
  
  public Filter<T> ge(String propertyName, Object value)
  {
    value = convertValue(propertyName, value);
    ElComparator<T> comparator = getElComparator(propertyName);
    
    this.matches.add(new ElMatchBuilder.Ge(value, comparator));
    return this;
  }
  
  public Filter<T> ieq(String propertyName, String value)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    
    this.matches.add(new ElMatchBuilder.Ieq(elGetValue, value));
    return this;
  }
  
  public Filter<T> isNotNull(String propertyName)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    
    this.matches.add(new ElMatchBuilder.IsNotNull(elGetValue));
    return this;
  }
  
  public Filter<T> isNull(String propertyName)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    
    this.matches.add(new ElMatchBuilder.IsNull(elGetValue));
    return this;
  }
  
  public Filter<T> le(String propertyName, Object value)
  {
    value = convertValue(propertyName, value);
    ElComparator<T> comparator = getElComparator(propertyName);
    
    this.matches.add(new ElMatchBuilder.Le(value, comparator));
    return this;
  }
  
  public Filter<T> lt(String propertyName, Object value)
  {
    value = convertValue(propertyName, value);
    ElComparator<T> comparator = getElComparator(propertyName);
    
    this.matches.add(new ElMatchBuilder.Lt(value, comparator));
    return this;
  }
  
  public Filter<T> regex(String propertyName, String regEx)
  {
    return regex(propertyName, regEx, 0);
  }
  
  public Filter<T> regex(String propertyName, String regEx, int options)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    
    this.matches.add(new ElMatchBuilder.RegularExpr(elGetValue, regEx, options));
    return this;
  }
  
  public Filter<T> contains(String propertyName, String value)
  {
    String quote = ".*" + Pattern.quote(value) + ".*";
    
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    this.matches.add(new ElMatchBuilder.RegularExpr(elGetValue, quote, 0));
    return this;
  }
  
  public Filter<T> icontains(String propertyName, String value)
  {
    String quote = ".*" + Pattern.quote(value) + ".*";
    
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    this.matches.add(new ElMatchBuilder.RegularExpr(elGetValue, quote, 2));
    return this;
  }
  
  public Filter<T> endsWith(String propertyName, String value)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    this.matches.add(new ElMatchBuilder.EndsWith(elGetValue, value));
    return this;
  }
  
  public Filter<T> startsWith(String propertyName, String value)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    this.matches.add(new ElMatchBuilder.StartsWith(elGetValue, value));
    return this;
  }
  
  public Filter<T> iendsWith(String propertyName, String value)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    this.matches.add(new ElMatchBuilder.IEndsWith(elGetValue, value));
    return this;
  }
  
  public Filter<T> istartsWith(String propertyName, String value)
  {
    ElPropertyValue elGetValue = getElGetValue(propertyName);
    this.matches.add(new ElMatchBuilder.IStartsWith(elGetValue, value));
    return this;
  }
  
  public Filter<T> maxRows(int maxRows)
  {
    this.maxRows = maxRows;
    return this;
  }
  
  public List<T> filter(List<T> list)
  {
    if (this.sortByClause != null)
    {
      list = new ArrayList(list);
      this.beanDescriptor.sort(list, this.sortByClause);
    }
    ArrayList<T> filterList = new ArrayList();
    for (int i = 0; i < list.size(); i++)
    {
      T t = list.get(i);
      if (isMatch(t))
      {
        filterList.add(t);
        if ((this.maxRows > 0) && (filterList.size() >= this.maxRows)) {
          break;
        }
      }
    }
    return filterList;
  }
}
