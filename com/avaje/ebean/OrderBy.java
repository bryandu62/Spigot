package com.avaje.ebean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class OrderBy<T>
  implements Serializable
{
  private static final long serialVersionUID = 9157089257745730539L;
  private transient Query<T> query;
  private List<Property> list;
  
  public OrderBy()
  {
    this.list = new ArrayList(2);
  }
  
  public OrderBy(String orderByClause)
  {
    this(null, orderByClause);
  }
  
  public OrderBy(Query<T> query, String orderByClause)
  {
    this.query = query;
    this.list = new ArrayList(2);
    parse(orderByClause);
  }
  
  public void reverse()
  {
    for (int i = 0; i < this.list.size(); i++) {
      ((Property)this.list.get(i)).reverse();
    }
  }
  
  public Query<T> asc(String propertyName)
  {
    this.list.add(new Property(propertyName, true));
    return this.query;
  }
  
  public Query<T> desc(String propertyName)
  {
    this.list.add(new Property(propertyName, false));
    return this.query;
  }
  
  public List<Property> getProperties()
  {
    return this.list;
  }
  
  public boolean isEmpty()
  {
    return this.list.isEmpty();
  }
  
  public Query<T> getQuery()
  {
    return this.query;
  }
  
  public void setQuery(Query<T> query)
  {
    this.query = query;
  }
  
  public OrderBy<T> copy()
  {
    OrderBy<T> copy = new OrderBy();
    for (int i = 0; i < this.list.size(); i++) {
      copy.add(((Property)this.list.get(i)).copy());
    }
    return copy;
  }
  
  public void add(Property p)
  {
    this.list.add(p);
  }
  
  public String toString()
  {
    return this.list.toString();
  }
  
  public String toStringFormat()
  {
    if (this.list.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.list.size(); i++)
    {
      Property property = (Property)this.list.get(i);
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(property.toStringFormat());
    }
    return sb.toString();
  }
  
  public boolean equals(Object obj)
  {
    if ((obj instanceof OrderBy))
    {
      if (obj == this) {
        return true;
      }
      OrderBy<?> other = (OrderBy)obj;
      return hashCode() == other.hashCode();
    }
    return false;
  }
  
  public int hashCode()
  {
    return hash();
  }
  
  public int hash()
  {
    int hc = OrderBy.class.getName().hashCode();
    for (int i = 0; i < this.list.size(); i++) {
      hc = hc * 31 + ((Property)this.list.get(i)).hash();
    }
    return hc;
  }
  
  public static final class Property
    implements Serializable
  {
    private static final long serialVersionUID = 1546009780322478077L;
    private String property;
    private boolean ascending;
    
    public Property(String property, boolean ascending)
    {
      this.property = property;
      this.ascending = ascending;
    }
    
    protected int hash()
    {
      int hc = this.property.hashCode();
      hc = hc * 31 + (this.ascending ? 0 : 1);
      return hc;
    }
    
    public String toString()
    {
      return toStringFormat();
    }
    
    public String toStringFormat()
    {
      if (this.ascending) {
        return this.property;
      }
      return this.property + " desc";
    }
    
    public void reverse()
    {
      this.ascending = (!this.ascending);
    }
    
    public void trim(String pathPrefix)
    {
      this.property = this.property.substring(pathPrefix.length() + 1);
    }
    
    public Property copy()
    {
      return new Property(this.property, this.ascending);
    }
    
    public String getProperty()
    {
      return this.property;
    }
    
    public void setProperty(String property)
    {
      this.property = property;
    }
    
    public boolean isAscending()
    {
      return this.ascending;
    }
    
    public void setAscending(boolean ascending)
    {
      this.ascending = ascending;
    }
  }
  
  private void parse(String orderByClause)
  {
    if (orderByClause == null) {
      return;
    }
    String[] chunks = orderByClause.split(",");
    for (int i = 0; i < chunks.length; i++)
    {
      String[] pairs = chunks[i].split(" ");
      Property p = parseProperty(pairs);
      if (p != null) {
        this.list.add(p);
      }
    }
  }
  
  private Property parseProperty(String[] pairs)
  {
    if (pairs.length == 0) {
      return null;
    }
    ArrayList<String> wordList = new ArrayList(pairs.length);
    for (int i = 0; i < pairs.length; i++) {
      if (!isEmptyString(pairs[i])) {
        wordList.add(pairs[i]);
      }
    }
    if (wordList.isEmpty()) {
      return null;
    }
    if (wordList.size() == 1) {
      return new Property((String)wordList.get(0), true);
    }
    if (wordList.size() == 2)
    {
      boolean asc = isAscending((String)wordList.get(1));
      return new Property((String)wordList.get(0), asc);
    }
    String m = "Expecting a max of 2 words in [" + Arrays.toString(pairs) + "] but got " + wordList.size();
    
    throw new RuntimeException(m);
  }
  
  private boolean isAscending(String s)
  {
    s = s.toLowerCase();
    if (s.startsWith("asc")) {
      return true;
    }
    if (s.startsWith("desc")) {
      return false;
    }
    String m = "Expecting [" + s + "] to be asc or desc?";
    throw new RuntimeException(m);
  }
  
  private boolean isEmptyString(String s)
  {
    return (s == null) || (s.length() == 0);
  }
}
