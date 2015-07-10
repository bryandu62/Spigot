package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.el.ElPropertyDeploy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class SqlTreeAlias
{
  private int counter;
  private int manyWhereCounter;
  private TreeSet<String> joinProps = new TreeSet();
  private HashSet<String> embeddedPropertyJoins;
  private TreeSet<String> manyWhereJoinProps = new TreeSet();
  private HashMap<String, String> aliasMap = new HashMap();
  private HashMap<String, String> manyWhereAliasMap = new HashMap();
  private final String rootTableAlias;
  
  public SqlTreeAlias(String rootTableAlias)
  {
    this.rootTableAlias = rootTableAlias;
  }
  
  public void addManyWhereJoins(Set<String> manyWhereJoins)
  {
    if (manyWhereJoins != null) {
      for (String include : manyWhereJoins) {
        addPropertyJoin(include, this.manyWhereJoinProps);
      }
    }
  }
  
  private void addEmbeddedPropertyJoin(String embProp)
  {
    if (this.embeddedPropertyJoins == null) {
      this.embeddedPropertyJoins = new HashSet();
    }
    this.embeddedPropertyJoins.add(embProp);
  }
  
  public void addJoin(Set<String> propJoins, BeanDescriptor<?> desc)
  {
    if (propJoins != null) {
      for (String propJoin : propJoins)
      {
        ElPropertyDeploy elProp = desc.getElPropertyDeploy(propJoin);
        if ((elProp != null) && (elProp.getBeanProperty().isEmbedded()))
        {
          String[] split = SplitName.split(propJoin);
          addPropertyJoin(split[0], this.joinProps);
          addEmbeddedPropertyJoin(propJoin);
        }
        else
        {
          addPropertyJoin(propJoin, this.joinProps);
        }
      }
    }
  }
  
  private void addPropertyJoin(String include, TreeSet<String> set)
  {
    if (set.add(include))
    {
      String[] split = SplitName.split(include);
      if (split[0] != null) {
        addPropertyJoin(split[0], set);
      }
    }
  }
  
  public void buildAlias()
  {
    Iterator<String> i = this.joinProps.iterator();
    while (i.hasNext()) {
      calcAlias((String)i.next());
    }
    i = this.manyWhereJoinProps.iterator();
    while (i.hasNext()) {
      calcAliasManyWhere((String)i.next());
    }
    mapEmbeddedPropertyAlias();
  }
  
  private void mapEmbeddedPropertyAlias()
  {
    if (this.embeddedPropertyJoins != null) {
      for (String propJoin : this.embeddedPropertyJoins)
      {
        String[] split = SplitName.split(propJoin);
        
        String alias = getTableAlias(split[0]);
        this.aliasMap.put(propJoin, alias);
      }
    }
  }
  
  private String calcAlias(String prefix)
  {
    String alias = nextTableAlias();
    this.aliasMap.put(prefix, alias);
    return alias;
  }
  
  private String calcAliasManyWhere(String prefix)
  {
    String alias = nextManyWhereTableAlias();
    this.manyWhereAliasMap.put(prefix, alias);
    return alias;
  }
  
  public String getTableAlias(String prefix)
  {
    if (prefix == null) {
      return this.rootTableAlias;
    }
    String s = (String)this.aliasMap.get(prefix);
    if (s == null) {
      return calcAlias(prefix);
    }
    return s;
  }
  
  public String getTableAliasManyWhere(String prefix)
  {
    if (prefix == null) {
      return this.rootTableAlias;
    }
    String s = (String)this.manyWhereAliasMap.get(prefix);
    if (s == null) {
      s = (String)this.aliasMap.get(prefix);
    }
    if (s == null)
    {
      String msg = "Could not determine table alias for [" + prefix + "] manyMap[" + this.manyWhereAliasMap + "] aliasMap[" + this.aliasMap + "]";
      
      throw new RuntimeException(msg);
    }
    return s;
  }
  
  public String parseWhere(String clause)
  {
    clause = parseRootAlias(clause);
    clause = parseAliasMap(clause, this.manyWhereAliasMap);
    return parseAliasMap(clause, this.aliasMap);
  }
  
  public String parse(String clause)
  {
    clause = parseRootAlias(clause);
    return parseAliasMap(clause, this.aliasMap);
  }
  
  private String parseRootAlias(String clause)
  {
    if (this.rootTableAlias == null) {
      return clause.replace("${}", "");
    }
    return clause.replace("${}", this.rootTableAlias + ".");
  }
  
  private String parseAliasMap(String clause, HashMap<String, String> parseAliasMap)
  {
    Iterator<Map.Entry<String, String>> i = parseAliasMap.entrySet().iterator();
    while (i.hasNext())
    {
      Map.Entry<String, String> e = (Map.Entry)i.next();
      String k = "${" + (String)e.getKey() + "}";
      clause = clause.replace(k, (String)e.getValue() + ".");
    }
    return clause;
  }
  
  private String nextTableAlias()
  {
    return "t" + ++this.counter;
  }
  
  private String nextManyWhereTableAlias()
  {
    return "u" + ++this.manyWhereCounter;
  }
}
