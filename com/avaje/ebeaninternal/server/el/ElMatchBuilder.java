package com.avaje.ebeaninternal.server.el;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ElMatchBuilder
{
  static class RegularExpr<T>
    implements ElMatcher<T>
  {
    final ElPropertyValue elGetValue;
    final String value;
    final Pattern pattern;
    
    RegularExpr(ElPropertyValue elGetValue, String value, int options)
    {
      this.elGetValue = elGetValue;
      this.value = value;
      this.pattern = Pattern.compile(value, options);
    }
    
    public boolean isMatch(T bean)
    {
      String v = (String)this.elGetValue.elGetValue(bean);
      return this.pattern.matcher(v).matches();
    }
  }
  
  static abstract class BaseString<T>
    implements ElMatcher<T>
  {
    final ElPropertyValue elGetValue;
    final String value;
    
    public BaseString(ElPropertyValue elGetValue, String value)
    {
      this.elGetValue = elGetValue;
      this.value = value;
    }
    
    public abstract boolean isMatch(T paramT);
  }
  
  static class Ieq<T>
    extends ElMatchBuilder.BaseString<T>
  {
    Ieq(ElPropertyValue elGetValue, String value)
    {
      super(value);
    }
    
    public boolean isMatch(T bean)
    {
      String v = (String)this.elGetValue.elGetValue(bean);
      return this.value.equalsIgnoreCase(v);
    }
  }
  
  static class IStartsWith<T>
    implements ElMatcher<T>
  {
    final ElPropertyValue elGetValue;
    final CharMatch charMatch;
    
    IStartsWith(ElPropertyValue elGetValue, String value)
    {
      this.elGetValue = elGetValue;
      this.charMatch = new CharMatch(value);
    }
    
    public boolean isMatch(T bean)
    {
      String v = (String)this.elGetValue.elGetValue(bean);
      return this.charMatch.startsWith(v);
    }
  }
  
  static class IEndsWith<T>
    implements ElMatcher<T>
  {
    final ElPropertyValue elGetValue;
    final CharMatch charMatch;
    
    IEndsWith(ElPropertyValue elGetValue, String value)
    {
      this.elGetValue = elGetValue;
      this.charMatch = new CharMatch(value);
    }
    
    public boolean isMatch(T bean)
    {
      String v = (String)this.elGetValue.elGetValue(bean);
      return this.charMatch.endsWith(v);
    }
  }
  
  static class StartsWith<T>
    extends ElMatchBuilder.BaseString<T>
  {
    StartsWith(ElPropertyValue elGetValue, String value)
    {
      super(value);
    }
    
    public boolean isMatch(T bean)
    {
      String v = (String)this.elGetValue.elGetValue(bean);
      return this.value.startsWith(v);
    }
  }
  
  static class EndsWith<T>
    extends ElMatchBuilder.BaseString<T>
  {
    EndsWith(ElPropertyValue elGetValue, String value)
    {
      super(value);
    }
    
    public boolean isMatch(T bean)
    {
      String v = (String)this.elGetValue.elGetValue(bean);
      return this.value.endsWith(v);
    }
  }
  
  static class IsNull<T>
    implements ElMatcher<T>
  {
    final ElPropertyValue elGetValue;
    
    public IsNull(ElPropertyValue elGetValue)
    {
      this.elGetValue = elGetValue;
    }
    
    public boolean isMatch(T bean)
    {
      return null == this.elGetValue.elGetValue(bean);
    }
  }
  
  static class IsNotNull<T>
    implements ElMatcher<T>
  {
    final ElPropertyValue elGetValue;
    
    public IsNotNull(ElPropertyValue elGetValue)
    {
      this.elGetValue = elGetValue;
    }
    
    public boolean isMatch(T bean)
    {
      return null != this.elGetValue.elGetValue(bean);
    }
  }
  
  static abstract class Base<T>
    implements ElMatcher<T>
  {
    final Object filterValue;
    final ElComparator<T> comparator;
    
    public Base(Object filterValue, ElComparator<T> comparator)
    {
      this.filterValue = filterValue;
      this.comparator = comparator;
    }
    
    public abstract boolean isMatch(T paramT);
  }
  
  static class InSet<T>
    implements ElMatcher<T>
  {
    final Set<?> set;
    final ElPropertyValue elGetValue;
    
    public InSet(Set<?> set, ElPropertyValue elGetValue)
    {
      this.set = new HashSet(set);
      this.elGetValue = elGetValue;
    }
    
    public boolean isMatch(T bean)
    {
      Object value = this.elGetValue.elGetValue(bean);
      if (value == null) {
        return false;
      }
      return this.set.contains(value);
    }
  }
  
  static class Eq<T>
    extends ElMatchBuilder.Base<T>
  {
    public Eq(Object filterValue, ElComparator<T> comparator)
    {
      super(comparator);
    }
    
    public boolean isMatch(T value)
    {
      return this.comparator.compareValue(this.filterValue, value) == 0;
    }
  }
  
  static class Ne<T>
    extends ElMatchBuilder.Base<T>
  {
    public Ne(Object filterValue, ElComparator<T> comparator)
    {
      super(comparator);
    }
    
    public boolean isMatch(T value)
    {
      return this.comparator.compareValue(this.filterValue, value) != 0;
    }
  }
  
  static class Between<T>
    implements ElMatcher<T>
  {
    final Object min;
    final Object max;
    final ElComparator<T> comparator;
    
    Between(Object min, Object max, ElComparator<T> comparator)
    {
      this.min = min;
      this.max = max;
      this.comparator = comparator;
    }
    
    public boolean isMatch(T value)
    {
      return (this.comparator.compareValue(this.min, value) <= 0) && (this.comparator.compareValue(this.max, value) >= 0);
    }
  }
  
  static class Gt<T>
    extends ElMatchBuilder.Base<T>
  {
    Gt(Object filterValue, ElComparator<T> comparator)
    {
      super(comparator);
    }
    
    public boolean isMatch(T value)
    {
      return this.comparator.compareValue(this.filterValue, value) == -1;
    }
  }
  
  static class Ge<T>
    extends ElMatchBuilder.Base<T>
  {
    Ge(Object filterValue, ElComparator<T> comparator)
    {
      super(comparator);
    }
    
    public boolean isMatch(T value)
    {
      return this.comparator.compareValue(this.filterValue, value) >= 0;
    }
  }
  
  static class Le<T>
    extends ElMatchBuilder.Base<T>
  {
    Le(Object filterValue, ElComparator<T> comparator)
    {
      super(comparator);
    }
    
    public boolean isMatch(T value)
    {
      return this.comparator.compareValue(this.filterValue, value) <= 0;
    }
  }
  
  static class Lt<T>
    extends ElMatchBuilder.Base<T>
  {
    Lt(Object filterValue, ElComparator<T> comparator)
    {
      super(comparator);
    }
    
    public boolean isMatch(T value)
    {
      return this.comparator.compareValue(this.filterValue, value) == 1;
    }
  }
}
