package com.avaje.ebeaninternal.server.querydefn;

import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.OrderBy;
import com.avaje.ebean.OrderBy.Property;
import com.avaje.ebean.Query;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.SpiExpressionFactory;
import com.avaje.ebeaninternal.api.SpiExpressionList;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.expression.FilterExprPath;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.query.SplitName;
import com.avaje.ebeaninternal.util.FilterExpressionList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OrmQueryProperties
  implements Serializable
{
  private static final long serialVersionUID = -8785582703966455658L;
  private String parentPath;
  private String path;
  private String properties;
  private String trimmedProperties;
  private int queryFetchBatch = -1;
  private boolean queryFetchAll;
  private int lazyFetchBatch = -1;
  private FetchConfig fetchConfig;
  private boolean cache;
  private boolean readOnly;
  private Set<String> included;
  private Set<String> includedBeanJoin;
  private Set<String> secondaryQueryJoins;
  private List<OrmQueryProperties> secondaryChildren;
  private OrderBy orderBy;
  private SpiExpressionList filterMany;
  
  public OrmQueryProperties(String path)
  {
    this.path = path;
    this.parentPath = SplitName.parent(path);
  }
  
  public OrmQueryProperties()
  {
    this(null);
  }
  
  public OrmQueryProperties(String path, String properties)
  {
    this(path);
    setProperties(properties);
  }
  
  public void addSecJoinOrderProperty(OrderBy.Property orderProp)
  {
    if (this.orderBy == null) {
      this.orderBy = new OrderBy();
    }
    this.orderBy.add(orderProp);
  }
  
  public void setFetchConfig(FetchConfig fetchConfig)
  {
    if (fetchConfig != null)
    {
      this.fetchConfig = fetchConfig;
      this.lazyFetchBatch = fetchConfig.getLazyBatchSize();
      this.queryFetchBatch = fetchConfig.getQueryBatchSize();
      this.queryFetchAll = fetchConfig.isQueryAll();
    }
  }
  
  public FetchConfig getFetchConfig()
  {
    return this.fetchConfig;
  }
  
  public void setProperties(String properties)
  {
    this.properties = properties;
    this.trimmedProperties = properties;
    parseProperties();
    if (!isAllProperties())
    {
      Set<String> parsed = parseIncluded(this.trimmedProperties);
      if (parsed.contains("*")) {
        this.included = null;
      } else {
        this.included = parsed;
      }
    }
    else
    {
      this.included = null;
    }
  }
  
  private boolean isAllProperties()
  {
    return (this.trimmedProperties == null) || (this.trimmedProperties.length() == 0) || ("*".equals(this.trimmedProperties));
  }
  
  public <T> SpiExpressionList<T> filterMany(Query<T> rootQuery)
  {
    if (this.filterMany == null)
    {
      FilterExprPath exprPath = new FilterExprPath(this.path);
      SpiExpressionFactory queryEf = (SpiExpressionFactory)rootQuery.getExpressionFactory();
      ExpressionFactory filterEf = queryEf.createExpressionFactory(exprPath);
      this.filterMany = new FilterExpressionList(exprPath, filterEf, rootQuery);
      
      this.queryFetchAll = true;
      this.queryFetchBatch = 100;
      this.lazyFetchBatch = 100;
    }
    return this.filterMany;
  }
  
  public SpiExpressionList<?> getFilterManyTrimPath(int trimPath)
  {
    if (this.filterMany == null) {
      return null;
    }
    this.filterMany.trimPath(trimPath);
    return this.filterMany;
  }
  
  public SpiExpressionList<?> getFilterMany()
  {
    return this.filterMany;
  }
  
  public void setFilterMany(SpiExpressionList<?> filterMany)
  {
    this.filterMany = filterMany;
  }
  
  public void setDefaultProperties(String properties, Set<String> included)
  {
    this.properties = properties;
    this.trimmedProperties = properties;
    this.included = included;
  }
  
  public void setTunedProperties(OrmQueryProperties tunedProperties)
  {
    this.properties = tunedProperties.properties;
    this.trimmedProperties = tunedProperties.trimmedProperties;
    this.included = tunedProperties.included;
  }
  
  public void configureBeanQuery(SpiQuery<?> query)
  {
    if ((this.trimmedProperties != null) && (this.trimmedProperties.length() > 0))
    {
      query.select(this.trimmedProperties);
      if (this.filterMany != null) {
        query.setFilterMany(this.path, this.filterMany);
      }
    }
    if (this.secondaryChildren != null)
    {
      int trimPath = this.path.length() + 1;
      for (int i = 0; i < this.secondaryChildren.size(); i++)
      {
        OrmQueryProperties p = (OrmQueryProperties)this.secondaryChildren.get(i);
        String path = p.getPath();
        path = path.substring(trimPath);
        query.fetch(path, p.getProperties(), p.getFetchConfig());
        query.setFilterMany(path, p.getFilterManyTrimPath(trimPath));
      }
    }
    if (this.orderBy != null)
    {
      List<OrderBy.Property> orderByProps = this.orderBy.getProperties();
      for (int i = 0; i < orderByProps.size(); i++) {
        ((OrderBy.Property)orderByProps.get(i)).trim(this.path);
      }
      query.setOrder(this.orderBy);
    }
  }
  
  public void configureManyQuery(SpiQuery<?> query)
  {
    if ((this.trimmedProperties != null) && (this.trimmedProperties.length() > 0)) {
      query.fetch(query.getLazyLoadManyPath(), this.trimmedProperties);
    }
    if (this.filterMany != null) {
      query.setFilterMany(this.path, this.filterMany);
    }
    if (this.secondaryChildren != null)
    {
      int trimlen = this.path.length() - query.getLazyLoadManyPath().length();
      for (int i = 0; i < this.secondaryChildren.size(); i++)
      {
        OrmQueryProperties p = (OrmQueryProperties)this.secondaryChildren.get(i);
        String path = p.getPath();
        path = path.substring(trimlen);
        query.fetch(path, p.getProperties(), p.getFetchConfig());
        query.setFilterMany(path, p.getFilterManyTrimPath(trimlen));
      }
    }
    if (this.orderBy != null) {
      query.setOrder(this.orderBy);
    }
  }
  
  public OrmQueryProperties copy()
  {
    OrmQueryProperties copy = new OrmQueryProperties();
    copy.parentPath = this.parentPath;
    copy.path = this.path;
    copy.properties = this.properties;
    copy.cache = this.cache;
    copy.readOnly = this.readOnly;
    copy.queryFetchAll = this.queryFetchAll;
    copy.queryFetchBatch = this.queryFetchBatch;
    copy.lazyFetchBatch = this.lazyFetchBatch;
    copy.filterMany = this.filterMany;
    if (this.included != null) {
      copy.included = new HashSet(this.included);
    }
    if (this.includedBeanJoin != null) {
      copy.includedBeanJoin = new HashSet(this.includedBeanJoin);
    }
    return copy;
  }
  
  public boolean hasSelectClause()
  {
    if ("*".equals(this.trimmedProperties)) {
      return true;
    }
    return this.included != null;
  }
  
  public String toString()
  {
    String s = "";
    if (this.path != null) {
      s = s + this.path + " ";
    }
    if (this.properties != null) {
      s = s + "(" + this.properties + ") ";
    } else if (this.included != null) {
      s = s + "(" + this.included + ") ";
    }
    return s;
  }
  
  public boolean isChild(OrmQueryProperties possibleChild)
  {
    return possibleChild.getPath().startsWith(this.path + ".");
  }
  
  public void add(OrmQueryProperties child)
  {
    if (this.secondaryChildren == null) {
      this.secondaryChildren = new ArrayList();
    }
    this.secondaryChildren.add(child);
  }
  
  public int autofetchPlanHash()
  {
    int hc = this.path != null ? this.path.hashCode() : 1;
    if (this.properties != null) {
      hc = hc * 31 + this.properties.hashCode();
    } else {
      hc = hc * 31 + (this.included != null ? this.included.hashCode() : 1);
    }
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    int hc = this.path != null ? this.path.hashCode() : 1;
    if (this.properties != null) {
      hc = hc * 31 + this.properties.hashCode();
    } else {
      hc = hc * 31 + (this.included != null ? this.included.hashCode() : 1);
    }
    hc = hc * 31 + (this.filterMany != null ? this.filterMany.queryPlanHash(request) : 1);
    
    return hc;
  }
  
  public String getProperties()
  {
    return this.properties;
  }
  
  public boolean hasProperties()
  {
    return (this.properties != null) || (this.included != null);
  }
  
  public boolean isIncludedBeanJoin(String propertyName)
  {
    if (this.includedBeanJoin == null) {
      return false;
    }
    return this.includedBeanJoin.contains(propertyName);
  }
  
  public void includeBeanJoin(String propertyName)
  {
    if (this.includedBeanJoin == null) {
      this.includedBeanJoin = new HashSet();
    }
    this.includedBeanJoin.add(propertyName);
  }
  
  public boolean allProperties()
  {
    return this.included == null;
  }
  
  public Iterator<String> getSelectProperties()
  {
    if (this.secondaryQueryJoins == null) {
      return this.included.iterator();
    }
    LinkedHashSet<String> temp = new LinkedHashSet(this.secondaryQueryJoins.size() + this.included.size());
    temp.addAll(this.included);
    temp.addAll(this.secondaryQueryJoins);
    return temp.iterator();
  }
  
  public void addSecondaryQueryJoin(String property)
  {
    if (this.secondaryQueryJoins == null) {
      this.secondaryQueryJoins = new HashSet(4);
    }
    this.secondaryQueryJoins.add(property);
  }
  
  public Set<String> getAllIncludedProperties()
  {
    if (this.included == null) {
      return null;
    }
    if ((this.includedBeanJoin == null) && (this.secondaryQueryJoins == null)) {
      return new LinkedHashSet(this.included);
    }
    LinkedHashSet<String> s = new LinkedHashSet(2 * (this.included.size() + 5));
    if (this.included != null) {
      s.addAll(this.included);
    }
    if (this.includedBeanJoin != null) {
      s.addAll(this.includedBeanJoin);
    }
    if (this.secondaryQueryJoins != null) {
      s.addAll(this.secondaryQueryJoins);
    }
    return s;
  }
  
  public boolean isIncluded(String propName)
  {
    if ((this.includedBeanJoin != null) && (this.includedBeanJoin.contains(propName))) {
      return false;
    }
    if (this.included == null) {
      return true;
    }
    return this.included.contains(propName);
  }
  
  public OrmQueryProperties setQueryFetchBatch(int queryFetchBatch)
  {
    this.queryFetchBatch = queryFetchBatch;
    return this;
  }
  
  public OrmQueryProperties setQueryFetchAll(boolean queryFetchAll)
  {
    this.queryFetchAll = queryFetchAll;
    return this;
  }
  
  public OrmQueryProperties setQueryFetch(int batch, boolean queryFetchAll)
  {
    this.queryFetchBatch = batch;
    this.queryFetchAll = queryFetchAll;
    return this;
  }
  
  public OrmQueryProperties setLazyFetchBatch(int lazyFetchBatch)
  {
    this.lazyFetchBatch = lazyFetchBatch;
    return this;
  }
  
  public boolean isFetchJoin()
  {
    return (!isQueryFetch()) && (!isLazyFetch());
  }
  
  public boolean isQueryFetch()
  {
    return this.queryFetchBatch > -1;
  }
  
  public int getQueryFetchBatch()
  {
    return this.queryFetchBatch;
  }
  
  public boolean isQueryFetchAll()
  {
    return this.queryFetchAll;
  }
  
  public boolean isLazyFetch()
  {
    return this.lazyFetchBatch > -1;
  }
  
  public int getLazyFetchBatch()
  {
    return this.lazyFetchBatch;
  }
  
  public boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public boolean isCache()
  {
    return this.cache;
  }
  
  public String getParentPath()
  {
    return this.parentPath;
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  private void parseProperties()
  {
    if (this.trimmedProperties == null) {
      return;
    }
    int pos = this.trimmedProperties.indexOf("+readonly");
    if (pos > -1)
    {
      this.trimmedProperties = StringHelper.replaceString(this.trimmedProperties, "+readonly", "");
      this.readOnly = true;
    }
    pos = this.trimmedProperties.indexOf("+cache");
    if (pos > -1)
    {
      this.trimmedProperties = StringHelper.replaceString(this.trimmedProperties, "+cache", "");
      this.cache = true;
    }
    pos = this.trimmedProperties.indexOf("+query");
    if (pos > -1) {
      this.queryFetchBatch = parseBatchHint(pos, "+query");
    }
    pos = this.trimmedProperties.indexOf("+lazy");
    if (pos > -1) {
      this.lazyFetchBatch = parseBatchHint(pos, "+lazy");
    }
    this.trimmedProperties = this.trimmedProperties.trim();
    while (this.trimmedProperties.startsWith(",")) {
      this.trimmedProperties = this.trimmedProperties.substring(1).trim();
    }
  }
  
  private int parseBatchHint(int pos, String option)
  {
    int startPos = pos + option.length();
    
    int endPos = findEndPos(startPos, this.trimmedProperties);
    if (endPos == -1)
    {
      this.trimmedProperties = StringHelper.replaceString(this.trimmedProperties, option, "");
      return 0;
    }
    String batchParam = this.trimmedProperties.substring(startPos + 1, endPos);
    if (endPos + 1 >= this.trimmedProperties.length()) {
      this.trimmedProperties = this.trimmedProperties.substring(0, pos);
    } else {
      this.trimmedProperties = (this.trimmedProperties.substring(0, pos) + this.trimmedProperties.substring(endPos + 1));
    }
    return Integer.parseInt(batchParam);
  }
  
  private int findEndPos(int pos, String props)
  {
    if ((pos < props.length()) && 
      (props.charAt(pos) == '('))
    {
      int endPara = props.indexOf(')', pos + 1);
      if (endPara == -1)
      {
        String m = "Error could not find ')' in " + props + " after position " + pos;
        throw new RuntimeException(m);
      }
      return endPara;
    }
    return -1;
  }
  
  private static Set<String> parseIncluded(String rawList)
  {
    String[] res = rawList.split(",");
    
    LinkedHashSet<String> set = new LinkedHashSet(res.length + 3);
    
    String temp = null;
    for (int i = 0; i < res.length; i++)
    {
      temp = res[i].trim();
      if (temp.length() > 0) {
        set.add(temp);
      }
    }
    if (set.isEmpty()) {
      return null;
    }
    return Collections.unmodifiableSet(set);
  }
}
