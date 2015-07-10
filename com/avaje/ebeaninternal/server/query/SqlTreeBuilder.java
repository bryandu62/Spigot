package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.InheritInfo;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlTreeBuilder
{
  private static final Logger logger = Logger.getLogger(SqlTreeBuilder.class.getName());
  private final SpiQuery<?> query;
  private final BeanDescriptor<?> desc;
  private final OrmQueryDetail queryDetail;
  private final StringBuilder summary = new StringBuilder();
  private final CQueryPredicates predicates;
  private final boolean subQuery;
  private BeanPropertyAssocMany<?> manyProperty;
  private String manyPropertyName;
  private final SqlTreeAlias alias;
  private final DefaultDbSqlContext ctx;
  private final HashSet<String> selectIncludes = new HashSet();
  private final ManyWhereJoins manyWhereJoins;
  private final TableJoin includeJoin;
  private final boolean rawSql;
  
  public SqlTreeBuilder(OrmQueryRequest<?> request, CQueryPredicates predicates, OrmQueryDetail queryDetail)
  {
    this.rawSql = true;
    this.desc = request.getBeanDescriptor();
    this.query = null;
    this.subQuery = false;
    this.queryDetail = queryDetail;
    this.predicates = predicates;
    
    this.includeJoin = null;
    this.manyWhereJoins = null;
    this.alias = null;
    this.ctx = null;
  }
  
  public SqlTreeBuilder(String tableAliasPlaceHolder, String columnAliasPrefix, OrmQueryRequest<?> request, CQueryPredicates predicates)
  {
    this.rawSql = false;
    this.desc = request.getBeanDescriptor();
    this.query = request.getQuery();
    
    this.subQuery = SpiQuery.Type.SUBQUERY.equals(this.query.getType());
    this.includeJoin = this.query.getIncludeTableJoin();
    this.manyWhereJoins = this.query.getManyWhereJoins();
    this.queryDetail = this.query.getDetail();
    
    this.predicates = predicates;
    this.alias = new SqlTreeAlias(request.getBeanDescriptor().getBaseTableAlias());
    this.ctx = new DefaultDbSqlContext(this.alias, tableAliasPlaceHolder, columnAliasPrefix, !this.subQuery);
  }
  
  public SqlTree build()
  {
    SqlTree sqlTree = new SqlTree();
    
    this.summary.append(this.desc.getName());
    
    buildRoot(this.desc, sqlTree);
    
    SqlTreeNode rootNode = sqlTree.getRootNode();
    if (!this.rawSql)
    {
      sqlTree.setSelectSql(buildSelectClause(rootNode));
      sqlTree.setFromSql(buildFromClause(rootNode));
      sqlTree.setInheritanceWhereSql(buildWhereClause(rootNode));
      sqlTree.setEncryptedProps(this.ctx.getEncryptedProps());
    }
    sqlTree.setIncludes(this.queryDetail.getIncludes());
    sqlTree.setSummary(this.summary.toString());
    if (this.manyPropertyName != null)
    {
      ElPropertyValue manyPropEl = this.desc.getElGetValue(this.manyPropertyName);
      sqlTree.setManyProperty(this.manyProperty, this.manyPropertyName, manyPropEl);
    }
    return sqlTree;
  }
  
  private String buildSelectClause(SqlTreeNode rootNode)
  {
    if (this.rawSql) {
      return "Not Used";
    }
    rootNode.appendSelect(this.ctx, this.subQuery);
    
    String selectSql = this.ctx.getContent();
    if (selectSql.length() >= ", ".length()) {
      selectSql = selectSql.substring(", ".length());
    }
    return selectSql;
  }
  
  private String buildWhereClause(SqlTreeNode rootNode)
  {
    if (this.rawSql) {
      return "Not Used";
    }
    rootNode.appendWhere(this.ctx);
    return this.ctx.getContent();
  }
  
  private String buildFromClause(SqlTreeNode rootNode)
  {
    if (this.rawSql) {
      return "Not Used";
    }
    rootNode.appendFrom(this.ctx, false);
    return this.ctx.getContent();
  }
  
  private void buildRoot(BeanDescriptor<?> desc, SqlTree sqlTree)
  {
    SqlTreeNode selectRoot = buildSelectChain(null, null, desc, null);
    sqlTree.setRootNode(selectRoot);
    if (!this.rawSql)
    {
      this.alias.addJoin(this.queryDetail.getIncludes(), desc);
      this.alias.addJoin(this.predicates.getPredicateIncludes(), desc);
      this.alias.addManyWhereJoins(this.manyWhereJoins.getJoins());
      
      this.alias.buildAlias();
      
      this.predicates.parseTableAlias(this.alias);
    }
  }
  
  private SqlTreeNode buildSelectChain(String prefix, BeanPropertyAssoc<?> prop, BeanDescriptor<?> desc, List<SqlTreeNode> joinList)
  {
    List<SqlTreeNode> myJoinList = new ArrayList();
    
    BeanPropertyAssocOne<?>[] ones = desc.propertiesOne();
    for (int i = 0; i < ones.length; i++)
    {
      String propPrefix = SplitName.add(prefix, ones[i].getName());
      if (isIncludeBean(propPrefix, ones[i]))
      {
        this.selectIncludes.add(propPrefix);
        buildSelectChain(propPrefix, ones[i], ones[i].getTargetDescriptor(), myJoinList);
      }
    }
    BeanPropertyAssocMany<?>[] manys = desc.propertiesMany();
    for (int i = 0; i < manys.length; i++)
    {
      String propPrefix = SplitName.add(prefix, manys[i].getName());
      if (isIncludeMany(prefix, propPrefix, manys[i]))
      {
        this.selectIncludes.add(propPrefix);
        buildSelectChain(propPrefix, manys[i], manys[i].getTargetDescriptor(), myJoinList);
      }
    }
    if ((prefix == null) && (!this.rawSql)) {
      addManyWhereJoins(myJoinList);
    }
    SqlTreeNode selectNode = buildNode(prefix, prop, desc, myJoinList);
    if (joinList != null) {
      joinList.add(selectNode);
    }
    return selectNode;
  }
  
  private void addManyWhereJoins(List<SqlTreeNode> myJoinList)
  {
    Set<String> includes = this.manyWhereJoins.getJoins();
    for (String joinProp : includes)
    {
      BeanPropertyAssoc<?> beanProperty = (BeanPropertyAssoc)this.desc.getBeanPropertyFromPath(joinProp);
      SqlTreeNodeManyWhereJoin nodeJoin = new SqlTreeNodeManyWhereJoin(joinProp, beanProperty);
      myJoinList.add(nodeJoin);
    }
  }
  
  private SqlTreeNode buildNode(String prefix, BeanPropertyAssoc<?> prop, BeanDescriptor<?> desc, List<SqlTreeNode> myList)
  {
    OrmQueryProperties queryProps = this.queryDetail.getChunk(prefix, false);
    
    SqlTreeProperties props = getBaseSelect(desc, queryProps);
    if (prefix == null)
    {
      buildExtraJoins(desc, myList);
      return new SqlTreeNodeRoot(desc, props, myList, !this.subQuery, this.includeJoin);
    }
    if ((prop instanceof BeanPropertyAssocMany)) {
      return new SqlTreeNodeManyRoot(prefix, (BeanPropertyAssocMany)prop, props, myList);
    }
    return new SqlTreeNodeBean(prefix, prop, props, myList, true);
  }
  
  private void buildExtraJoins(BeanDescriptor<?> desc, List<SqlTreeNode> myList)
  {
    if (this.rawSql) {
      return;
    }
    Set<String> predicateIncludes = this.predicates.getPredicateIncludes();
    if (predicateIncludes == null) {
      return;
    }
    predicateIncludes.removeAll(this.manyWhereJoins.getJoins());
    
    IncludesDistiller extraJoinDistill = new IncludesDistiller(desc, this.selectIncludes, predicateIncludes, null);
    
    Collection<SqlTreeNodeExtraJoin> extraJoins = extraJoinDistill.getExtraJoinRootNodes();
    if (extraJoins.isEmpty()) {
      return;
    }
    Iterator<SqlTreeNodeExtraJoin> it = extraJoins.iterator();
    while (it.hasNext())
    {
      SqlTreeNodeExtraJoin extraJoin = (SqlTreeNodeExtraJoin)it.next();
      myList.add(extraJoin);
      if (extraJoin.isManyJoin()) {
        this.query.setDistinct(true);
      }
    }
  }
  
  private void addPropertyToSubQuery(SqlTreeProperties selectProps, BeanDescriptor<?> desc, OrmQueryProperties queryProps, String propName)
  {
    BeanProperty p = desc.findBeanProperty(propName);
    if (p == null)
    {
      logger.log(Level.SEVERE, "property [" + propName + "]not found on " + desc + " for query - excluding it.");
    }
    else if (((p instanceof BeanPropertyAssoc)) && (p.isEmbedded()))
    {
      int pos = propName.indexOf(".");
      if (pos > -1)
      {
        String name = propName.substring(pos + 1);
        p = ((BeanPropertyAssoc)p).getTargetDescriptor().findBeanProperty(name);
      }
    }
    selectProps.add(p);
  }
  
  private void addProperty(SqlTreeProperties selectProps, BeanDescriptor<?> desc, OrmQueryProperties queryProps, String propName)
  {
    if (this.subQuery)
    {
      addPropertyToSubQuery(selectProps, desc, queryProps, propName);
      return;
    }
    int basePos = propName.indexOf('.');
    if (basePos > -1)
    {
      String baseName = propName.substring(0, basePos);
      if (!selectProps.containsProperty(baseName))
      {
        BeanProperty p = desc.findBeanProperty(baseName);
        if (p == null)
        {
          String m = "property [" + propName + "] not found on " + desc + " for query - excluding it.";
          logger.log(Level.SEVERE, m);
        }
        else if (p.isEmbedded())
        {
          selectProps.add(p);
          
          selectProps.getIncludedProperties().add(baseName);
        }
        else
        {
          String m = "property [" + p.getFullBeanName() + "] expected to be an embedded bean for query - excluding it.";
          
          logger.log(Level.SEVERE, m);
        }
      }
    }
    else
    {
      BeanProperty p = desc.findBeanProperty(propName);
      if (p == null) {
        logger.log(Level.SEVERE, "property [" + propName + "] not found on " + desc + " for query - excluding it.");
      } else if (!p.isId()) {
        if ((p instanceof BeanPropertyAssoc))
        {
          if (!queryProps.isIncludedBeanJoin(p.getName())) {
            selectProps.add(p);
          }
        }
        else {
          selectProps.add(p);
        }
      }
    }
  }
  
  private SqlTreeProperties getBaseSelectPartial(BeanDescriptor<?> desc, OrmQueryProperties queryProps)
  {
    SqlTreeProperties selectProps = new SqlTreeProperties();
    selectProps.setReadOnly(queryProps.isReadOnly());
    selectProps.setIncludedProperties(queryProps.getAllIncludedProperties());
    
    Iterator<String> it = queryProps.getSelectProperties();
    while (it.hasNext())
    {
      String propName = (String)it.next();
      if (propName.length() > 0) {
        addProperty(selectProps, desc, queryProps, propName);
      }
    }
    return selectProps;
  }
  
  private SqlTreeProperties getBaseSelect(BeanDescriptor<?> desc, OrmQueryProperties queryProps)
  {
    boolean partial = (queryProps != null) && (!queryProps.allProperties());
    if (partial) {
      return getBaseSelectPartial(desc, queryProps);
    }
    SqlTreeProperties selectProps = new SqlTreeProperties();
    
    selectProps.add(desc.propertiesBaseScalar());
    selectProps.add(desc.propertiesBaseCompound());
    selectProps.add(desc.propertiesEmbedded());
    
    BeanPropertyAssocOne<?>[] propertiesOne = desc.propertiesOne();
    for (int i = 0; i < propertiesOne.length; i++) {
      if ((queryProps == null) || (!queryProps.isIncludedBeanJoin(propertiesOne[i].getName()))) {
        selectProps.add(propertiesOne[i]);
      }
    }
    selectProps.setTableJoins(desc.tableJoins());
    
    InheritInfo inheritInfo = desc.getInheritInfo();
    if (inheritInfo != null) {
      inheritInfo.addChildrenProperties(selectProps);
    }
    return selectProps;
  }
  
  private boolean isIncludeMany(String prefix, String propName, BeanPropertyAssocMany<?> manyProp)
  {
    if (this.queryDetail.isJoinsEmpty()) {
      return false;
    }
    if (this.queryDetail.includes(propName))
    {
      if (this.manyProperty != null)
      {
        if (logger.isLoggable(Level.FINE))
        {
          String msg = "Not joining [" + propName + "] as already joined to a Many[" + this.manyProperty + "].";
          logger.fine(msg);
        }
        return false;
      }
      this.manyProperty = manyProp;
      this.manyPropertyName = propName;
      this.summary.append(" +many:").append(propName);
      return true;
    }
    return false;
  }
  
  private boolean isIncludeBean(String prefix, BeanPropertyAssocOne<?> prop)
  {
    if (this.queryDetail.includes(prefix))
    {
      this.summary.append(", ").append(prefix);
      String[] splitNames = SplitName.split(prefix);
      this.queryDetail.includeBeanJoin(splitNames[0], splitNames[1]);
      return true;
    }
    return false;
  }
  
  private static class IncludesDistiller
  {
    private final Set<String> selectIncludes;
    private final Set<String> predicateIncludes;
    private final Map<String, SqlTreeNodeExtraJoin> joinRegister = new HashMap();
    private final Map<String, SqlTreeNodeExtraJoin> rootRegister = new HashMap();
    private final BeanDescriptor<?> desc;
    
    private IncludesDistiller(BeanDescriptor<?> desc, Set<String> selectIncludes, Set<String> predicateIncludes)
    {
      this.desc = desc;
      this.selectIncludes = selectIncludes;
      this.predicateIncludes = predicateIncludes;
    }
    
    private Collection<SqlTreeNodeExtraJoin> getExtraJoinRootNodes()
    {
      String[] extras = findExtras();
      if (extras.length == 0) {
        return this.rootRegister.values();
      }
      Arrays.sort(extras);
      for (int i = 0; i < extras.length; i++) {
        createExtraJoin(extras[i]);
      }
      return this.rootRegister.values();
    }
    
    private void createExtraJoin(String includeProp)
    {
      SqlTreeNodeExtraJoin extraJoin = createJoinLeaf(includeProp);
      if (extraJoin != null)
      {
        SqlTreeNodeExtraJoin root = findExtraJoinRoot(includeProp, extraJoin);
        
        this.rootRegister.put(root.getName(), root);
      }
    }
    
    private SqlTreeNodeExtraJoin createJoinLeaf(String propertyName)
    {
      ElPropertyValue elGetValue = this.desc.getElGetValue(propertyName);
      if (elGetValue == null) {
        return null;
      }
      BeanProperty beanProperty = elGetValue.getBeanProperty();
      if ((beanProperty instanceof BeanPropertyAssoc))
      {
        BeanPropertyAssoc<?> assocProp = (BeanPropertyAssoc)beanProperty;
        if (assocProp.isEmbedded()) {
          return null;
        }
        SqlTreeNodeExtraJoin extraJoin = new SqlTreeNodeExtraJoin(propertyName, assocProp);
        this.joinRegister.put(propertyName, extraJoin);
        return extraJoin;
      }
      return null;
    }
    
    private SqlTreeNodeExtraJoin findExtraJoinRoot(String includeProp, SqlTreeNodeExtraJoin childJoin)
    {
      int dotPos = includeProp.lastIndexOf('.');
      if (dotPos == -1) {
        return childJoin;
      }
      String parentPropertyName = includeProp.substring(0, dotPos);
      if (this.selectIncludes.contains(parentPropertyName)) {
        return childJoin;
      }
      SqlTreeNodeExtraJoin parentJoin = (SqlTreeNodeExtraJoin)this.joinRegister.get(parentPropertyName);
      if (parentJoin == null) {
        parentJoin = createJoinLeaf(parentPropertyName);
      }
      parentJoin.addChild(childJoin);
      return findExtraJoinRoot(parentPropertyName, parentJoin);
    }
    
    private String[] findExtras()
    {
      List<String> extras = new ArrayList();
      for (String predProp : this.predicateIncludes) {
        if (!this.selectIncludes.contains(predProp)) {
          extras.add(predProp);
        }
      }
      return (String[])extras.toArray(new String[extras.size()]);
    }
  }
}
