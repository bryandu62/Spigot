package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.api.SpiQuery.Mode;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssoc;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.DbSqlContext;
import com.avaje.ebeaninternal.server.deploy.InheritInfo;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SqlTreeNodeBean
  implements SqlTreeNode
{
  private static final SqlTreeNode[] NO_CHILDREN = new SqlTreeNode[0];
  final BeanDescriptor<?> desc;
  final IdBinder idBinder;
  final SqlTreeNode[] children;
  final boolean readOnlyLeaf;
  final boolean partialObject;
  final Set<String> partialProps;
  final int partialHash;
  final BeanProperty[] properties;
  final String extraWhere;
  final BeanPropertyAssoc<?> nodeBeanProp;
  final TableJoin[] tableJoins;
  final boolean readId;
  final boolean disableLazyLoad;
  final InheritInfo inheritInfo;
  final String prefix;
  final Set<String> includedProps;
  final Map<String, String> pathMap;
  
  public SqlTreeNodeBean(String prefix, BeanPropertyAssoc<?> beanProp, SqlTreeProperties props, List<SqlTreeNode> myChildren, boolean withId)
  {
    this(prefix, beanProp, beanProp.getTargetDescriptor(), props, myChildren, withId);
  }
  
  public SqlTreeNodeBean(String prefix, BeanPropertyAssoc<?> beanProp, BeanDescriptor<?> desc, SqlTreeProperties props, List<SqlTreeNode> myChildren, boolean withId)
  {
    this.prefix = prefix;
    this.nodeBeanProp = beanProp;
    this.desc = desc;
    this.inheritInfo = desc.getInheritInfo();
    this.extraWhere = (beanProp == null ? null : beanProp.getExtraWhere());
    
    this.idBinder = desc.getIdBinder();
    
    this.readId = ((withId) && (desc.propertiesId().length > 0));
    this.disableLazyLoad = ((!this.readId) || (desc.isSqlSelectBased()));
    
    this.tableJoins = props.getTableJoins();
    
    this.partialObject = props.isPartialObject();
    this.partialProps = props.getIncludedProperties();
    this.partialHash = (this.partialObject ? this.partialProps.hashCode() : 0);
    
    this.readOnlyLeaf = props.isReadOnly();
    
    this.properties = props.getProps();
    if (this.partialObject) {
      this.includedProps = LoadedPropertiesCache.get(this.partialHash, this.partialProps, desc);
    } else {
      this.includedProps = null;
    }
    if (myChildren == null) {
      this.children = NO_CHILDREN;
    } else {
      this.children = ((SqlTreeNode[])myChildren.toArray(new SqlTreeNode[myChildren.size()]));
    }
    this.pathMap = createPathMap(prefix, desc);
  }
  
  private Map<String, String> createPathMap(String prefix, BeanDescriptor<?> desc)
  {
    BeanPropertyAssocMany<?>[] manys = desc.propertiesMany();
    
    HashMap<String, String> m = new HashMap();
    for (int i = 0; i < manys.length; i++)
    {
      String name = manys[i].getName();
      m.put(name, getPath(prefix, name));
    }
    return m;
  }
  
  private String getPath(String prefix, String propertyName)
  {
    if (prefix == null) {
      return propertyName;
    }
    return prefix + "." + propertyName;
  }
  
  protected void postLoad(DbReadContext cquery, Object loadedBean, Object id) {}
  
  public void buildSelectExpressionChain(List<String> selectChain)
  {
    if (this.readId) {
      this.idBinder.buildSelectExpressionChain(this.prefix, selectChain);
    }
    int i = 0;
    for (int x = this.properties.length; i < x; i++) {
      this.properties[i].buildSelectExpressionChain(this.prefix, selectChain);
    }
    for (int i = 0; i < this.children.length; i++) {
      this.children[i].buildSelectExpressionChain(selectChain);
    }
  }
  
  public void load(DbReadContext ctx, Object parentBean)
    throws SQLException
  {
    Object contextBean = null;
    BeanDescriptor<?> localDesc;
    Class<?> localType;
    BeanDescriptor<?> localDesc;
    Object localBean;
    IdBinder localIdBinder;
    if (this.inheritInfo != null)
    {
      InheritInfo localInfo = this.inheritInfo.readType(ctx);
      BeanDescriptor<?> localDesc;
      if (localInfo == null)
      {
        IdBinder localIdBinder = this.idBinder;
        Object localBean = null;
        Class<?> localType = null;
        localDesc = this.desc;
      }
      else
      {
        Object localBean = localInfo.createBean(ctx.isVanillaMode());
        Class<?> localType = localInfo.getType();
        IdBinder localIdBinder = localInfo.getIdBinder();
        localDesc = localInfo.getBeanDescriptor();
      }
    }
    else
    {
      localType = null;
      localDesc = this.desc;
      localBean = this.desc.createBean(ctx.isVanillaMode());
      localIdBinder = this.idBinder;
    }
    SpiQuery.Mode queryMode = ctx.getQueryMode();
    
    PersistenceContext persistenceContext = ctx.getPersistenceContext();
    
    Object id = null;
    if (this.readId)
    {
      id = localIdBinder.readSet(ctx, localBean);
      if (id == null)
      {
        localBean = null;
      }
      else
      {
        contextBean = persistenceContext.putIfAbsent(id, localBean);
        if (contextBean == null)
        {
          contextBean = localBean;
        }
        else if (queryMode.isLoadContextBean())
        {
          localBean = contextBean;
          if ((localBean instanceof EntityBean)) {
            ((EntityBean)localBean)._ebean_getIntercept().setIntercepting(false);
          }
        }
        else
        {
          localBean = null;
        }
      }
    }
    ctx.setCurrentPrefix(this.prefix, this.pathMap);
    
    ctx.propagateState(localBean);
    
    SqlBeanLoad sqlBeanLoad = new SqlBeanLoad(ctx, localType, localBean, queryMode);
    if (this.inheritInfo == null)
    {
      int i = 0;
      for (int x = this.properties.length; i < x; i++) {
        this.properties[i].load(sqlBeanLoad);
      }
    }
    else
    {
      int i = 0;
      for (int x = this.properties.length; i < x; i++)
      {
        BeanProperty p = localDesc.getBeanProperty(this.properties[i].getName());
        if (p != null) {
          p.load(sqlBeanLoad);
        } else {
          this.properties[i].loadIgnore(ctx);
        }
      }
    }
    int i = 0;
    for (int x = this.tableJoins.length; i < x; i++) {
      this.tableJoins[i].load(sqlBeanLoad);
    }
    boolean lazyLoadMany = false;
    if ((localBean == null) && (queryMode.equals(SpiQuery.Mode.LAZYLOAD_MANY)))
    {
      localBean = contextBean;
      lazyLoadMany = true;
    }
    for (int i = 0; i < this.children.length; i++) {
      this.children[i].load(ctx, localBean);
    }
    if (!lazyLoadMany) {
      if (localBean != null)
      {
        ctx.setCurrentPrefix(this.prefix, this.pathMap);
        if (!ctx.isVanillaMode()) {
          createListProxies(localDesc, ctx, localBean);
        }
        localDesc.postLoad(localBean, this.includedProps);
        if ((localBean instanceof EntityBean))
        {
          EntityBeanIntercept ebi = ((EntityBean)localBean)._ebean_getIntercept();
          ebi.setPersistenceContext(persistenceContext);
          ebi.setLoadedProps(this.includedProps);
          if (SpiQuery.Mode.LAZYLOAD_BEAN.equals(queryMode)) {
            ebi.setLoadedLazy();
          } else {
            ebi.setLoaded();
          }
          if (this.partialObject) {
            ctx.register(null, ebi);
          }
          if (this.disableLazyLoad) {
            ebi.setDisableLazyLoad(true);
          }
          if (ctx.isAutoFetchProfiling()) {
            ctx.profileBean(ebi, this.prefix);
          }
        }
      }
    }
    if ((parentBean != null) && (contextBean != null)) {
      this.nodeBeanProp.setValue(parentBean, contextBean);
    }
    if (!this.readId) {
      postLoad(ctx, localBean, id);
    } else {
      postLoad(ctx, contextBean, id);
    }
  }
  
  private void createListProxies(BeanDescriptor<?> localDesc, DbReadContext ctx, Object localBean)
  {
    BeanPropertyAssocMany<?> fetchedMany = ctx.getManyProperty();
    
    BeanPropertyAssocMany<?>[] manys = localDesc.propertiesMany();
    for (int i = 0; i < manys.length; i++) {
      if ((fetchedMany == null) || (!fetchedMany.equals(manys[i])))
      {
        BeanCollection<?> ref = manys[i].createReferenceIfNull(localBean);
        if (ref != null) {
          ctx.register(manys[i].getName(), ref);
        }
      }
    }
  }
  
  public void appendSelect(DbSqlContext ctx, boolean subQuery)
  {
    ctx.pushJoin(this.prefix);
    ctx.pushTableAlias(this.prefix);
    if (this.nodeBeanProp != null) {
      ctx.append('\n').append("        ");
    }
    if ((!subQuery) && (this.inheritInfo != null)) {
      ctx.appendColumn(this.inheritInfo.getDiscriminatorColumn());
    }
    if (this.readId) {
      appendSelect(ctx, false, this.idBinder.getProperties());
    }
    appendSelect(ctx, subQuery, this.properties);
    appendSelectTableJoins(ctx);
    for (int i = 0; i < this.children.length; i++) {
      this.children[i].appendSelect(ctx, subQuery);
    }
    ctx.popTableAlias();
    ctx.popJoin();
  }
  
  private void appendSelectTableJoins(DbSqlContext ctx)
  {
    String baseAlias = ctx.getTableAlias(this.prefix);
    for (int i = 0; i < this.tableJoins.length; i++)
    {
      TableJoin join = this.tableJoins[i];
      
      String alias = baseAlias + i;
      
      ctx.pushSecondaryTableAlias(alias);
      join.appendSelect(ctx, false);
      ctx.popTableAlias();
    }
  }
  
  private void appendSelect(DbSqlContext ctx, boolean subQuery, BeanProperty[] props)
  {
    for (int i = 0; i < props.length; i++) {
      props[i].appendSelect(ctx, subQuery);
    }
  }
  
  public void appendWhere(DbSqlContext ctx)
  {
    if ((this.inheritInfo != null) && 
      (!this.inheritInfo.isRoot()))
    {
      if (ctx.length() > 0) {
        ctx.append(" and");
      }
      ctx.append(" ").append(ctx.getTableAlias(this.prefix)).append(".");
      ctx.append(this.inheritInfo.getWhere()).append(" ");
    }
    if (this.extraWhere != null)
    {
      if (ctx.length() > 0) {
        ctx.append(" and");
      }
      String ta = ctx.getTableAlias(this.prefix);
      String ew = StringHelper.replaceString(this.extraWhere, "${ta}", ta);
      ctx.append(" ").append(ew).append(" ");
    }
    for (int i = 0; i < this.children.length; i++) {
      this.children[i].appendWhere(ctx);
    }
  }
  
  public void appendFrom(DbSqlContext ctx, boolean forceOuterJoin)
  {
    ctx.pushJoin(this.prefix);
    ctx.pushTableAlias(this.prefix);
    
    forceOuterJoin = appendFromBaseTable(ctx, forceOuterJoin);
    for (int i = 0; i < this.properties.length; i++) {
      this.properties[i].appendFrom(ctx, forceOuterJoin);
    }
    for (int i = 0; i < this.children.length; i++) {
      this.children[i].appendFrom(ctx, forceOuterJoin);
    }
    ctx.popTableAlias();
    ctx.popJoin();
  }
  
  public boolean appendFromBaseTable(DbSqlContext ctx, boolean forceOuterJoin)
  {
    if ((this.nodeBeanProp instanceof BeanPropertyAssocMany))
    {
      BeanPropertyAssocMany<?> manyProp = (BeanPropertyAssocMany)this.nodeBeanProp;
      if (manyProp.isManyToMany())
      {
        String alias = ctx.getTableAlias(this.prefix);
        String[] split = SplitName.split(this.prefix);
        String parentAlias = ctx.getTableAlias(split[0]);
        String alias2 = alias + "z_";
        
        TableJoin manyToManyJoin = manyProp.getIntersectionTableJoin();
        manyToManyJoin.addJoin(forceOuterJoin, parentAlias, alias2, ctx);
        
        return this.nodeBeanProp.addJoin(forceOuterJoin, alias2, alias, ctx);
      }
    }
    return this.nodeBeanProp.addJoin(forceOuterJoin, this.prefix, ctx);
  }
  
  public String toString()
  {
    return "SqlTreeNodeBean: " + this.desc;
  }
}
