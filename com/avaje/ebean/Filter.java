package com.avaje.ebean;

import java.util.List;
import java.util.Set;

public abstract interface Filter<T>
{
  public abstract Filter<T> sort(String paramString);
  
  public abstract Filter<T> maxRows(int paramInt);
  
  public abstract Filter<T> eq(String paramString, Object paramObject);
  
  public abstract Filter<T> ne(String paramString, Object paramObject);
  
  public abstract Filter<T> ieq(String paramString1, String paramString2);
  
  public abstract Filter<T> between(String paramString, Object paramObject1, Object paramObject2);
  
  public abstract Filter<T> gt(String paramString, Object paramObject);
  
  public abstract Filter<T> ge(String paramString, Object paramObject);
  
  public abstract Filter<T> lt(String paramString, Object paramObject);
  
  public abstract Filter<T> le(String paramString, Object paramObject);
  
  public abstract Filter<T> isNull(String paramString);
  
  public abstract Filter<T> isNotNull(String paramString);
  
  public abstract Filter<T> startsWith(String paramString1, String paramString2);
  
  public abstract Filter<T> istartsWith(String paramString1, String paramString2);
  
  public abstract Filter<T> endsWith(String paramString1, String paramString2);
  
  public abstract Filter<T> iendsWith(String paramString1, String paramString2);
  
  public abstract Filter<T> contains(String paramString1, String paramString2);
  
  public abstract Filter<T> icontains(String paramString1, String paramString2);
  
  public abstract Filter<T> in(String paramString, Set<?> paramSet);
  
  public abstract List<T> filter(List<T> paramList);
}
