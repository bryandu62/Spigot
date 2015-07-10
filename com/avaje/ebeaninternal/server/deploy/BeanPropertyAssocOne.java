package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.server.core.DefaultSqlUpdate;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.deploy.id.ImportedId;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.el.ElPropertyChainBuilder;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.query.SplitName;
import com.avaje.ebeaninternal.server.query.SqlBeanLoad;
import com.avaje.ebeaninternal.server.text.json.ReadJsonContext;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext;
import com.avaje.ebeaninternal.server.type.DataReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.PersistenceException;

public class BeanPropertyAssocOne<T>
  extends BeanPropertyAssoc<T>
{
  private final boolean oneToOne;
  private final boolean oneToOneExported;
  private final boolean embeddedVersion;
  private final boolean importedPrimaryKey;
  private final BeanPropertyAssocOne<T>.LocalHelp localHelp;
  private final BeanProperty[] embeddedProps;
  private final HashMap<String, BeanProperty> embeddedPropsMap;
  private ImportedId importedId;
  private ExportedProperty[] exportedProperties;
  private String deleteByParentIdSql;
  private String deleteByParentIdInSql;
  BeanPropertyAssocMany<?> relationshipProperty;
  
  public BeanPropertyAssocOne(BeanDescriptorMap owner, DeployBeanPropertyAssocOne<T> deploy)
  {
    this(owner, null, deploy);
  }
  
  public BeanPropertyAssocOne(BeanDescriptorMap owner, BeanDescriptor<?> descriptor, DeployBeanPropertyAssocOne<T> deploy)
  {
    super(owner, descriptor, deploy);
    
    this.importedPrimaryKey = deploy.isImportedPrimaryKey();
    this.oneToOne = deploy.isOneToOne();
    this.oneToOneExported = deploy.isOneToOneExported();
    if (this.embedded)
    {
      BeanEmbeddedMeta overrideMeta = BeanEmbeddedMetaFactory.create(owner, deploy, descriptor);
      this.embeddedProps = overrideMeta.getProperties();
      if (this.id) {
        this.embeddedVersion = false;
      } else {
        this.embeddedVersion = overrideMeta.isEmbeddedVersion();
      }
      this.embeddedPropsMap = new HashMap();
      for (int i = 0; i < this.embeddedProps.length; i++) {
        this.embeddedPropsMap.put(this.embeddedProps[i].getName(), this.embeddedProps[i]);
      }
    }
    else
    {
      this.embeddedProps = null;
      this.embeddedPropsMap = null;
      this.embeddedVersion = false;
    }
    this.localHelp = createHelp(this.embedded, this.oneToOneExported);
  }
  
  public void initialise()
  {
    super.initialise();
    if ((!this.isTransient) && 
      (!this.embedded)) {
      if (!this.oneToOneExported)
      {
        this.importedId = createImportedId(this, this.targetDescriptor, this.tableJoin);
      }
      else
      {
        this.exportedProperties = createExported();
        
        String delStmt = "delete from " + this.targetDescriptor.getBaseTable() + " where ";
        
        this.deleteByParentIdSql = (delStmt + deriveWhereParentIdSql(false));
        this.deleteByParentIdInSql = (delStmt + deriveWhereParentIdSql(true));
      }
    }
  }
  
  public void setRelationshipProperty(BeanPropertyAssocMany<?> relationshipProperty)
  {
    this.relationshipProperty = relationshipProperty;
  }
  
  public BeanPropertyAssocMany<?> getRelationshipProperty()
  {
    return this.relationshipProperty;
  }
  
  public void cacheClear()
  {
    if ((this.targetDescriptor.isBeanCaching()) && (this.relationshipProperty != null)) {
      this.targetDescriptor.cacheClearCachedManyIds(this.relationshipProperty.getName());
    }
  }
  
  public void cacheDelete(boolean clearOnNull, Object bean)
  {
    if ((this.targetDescriptor.isBeanCaching()) && (this.relationshipProperty != null))
    {
      Object assocBean = getValue(bean);
      if (assocBean != null)
      {
        Object parentId = this.targetDescriptor.getId(assocBean);
        if (parentId != null)
        {
          this.targetDescriptor.cacheRemoveCachedManyIds(parentId, this.relationshipProperty.getName());
          return;
        }
      }
      if (clearOnNull) {
        this.targetDescriptor.cacheClearCachedManyIds(this.relationshipProperty.getName());
      }
    }
  }
  
  public ElPropertyValue buildElPropertyValue(String propName, String remainder, ElPropertyChainBuilder chain, boolean propertyDeploy)
  {
    if (this.embedded)
    {
      BeanProperty embProp = (BeanProperty)this.embeddedPropsMap.get(remainder);
      if (embProp == null)
      {
        String msg = "Embedded Property " + remainder + " not found in " + getFullBeanName();
        throw new PersistenceException(msg);
      }
      if (chain == null) {
        chain = new ElPropertyChainBuilder(true, propName);
      }
      chain.add(this);
      return chain.add(embProp).build();
    }
    return createElPropertyValue(propName, remainder, chain, propertyDeploy);
  }
  
  public String getElPlaceholder(boolean encrypted)
  {
    return encrypted ? this.elPlaceHolderEncrypted : this.elPlaceHolder;
  }
  
  public SqlUpdate deleteByParentId(Object parentId, List<Object> parentIdist)
  {
    if (parentId != null) {
      return deleteByParentId(parentId);
    }
    return deleteByParentIdList(parentIdist);
  }
  
  private SqlUpdate deleteByParentIdList(List<Object> parentIdist)
  {
    StringBuilder sb = new StringBuilder(100);
    sb.append(this.deleteByParentIdInSql);
    
    String inClause = this.targetIdBinder.getIdInValueExpr(parentIdist.size());
    sb.append(inClause);
    
    DefaultSqlUpdate delete = new DefaultSqlUpdate(sb.toString());
    for (int i = 0; i < parentIdist.size(); i++) {
      this.targetIdBinder.bindId(delete, parentIdist.get(i));
    }
    return delete;
  }
  
  private SqlUpdate deleteByParentId(Object parentId)
  {
    DefaultSqlUpdate delete = new DefaultSqlUpdate(this.deleteByParentIdSql);
    if (this.exportedProperties.length == 1) {
      delete.addParameter(parentId);
    } else {
      this.targetDescriptor.getIdBinder().bindId(delete, parentId);
    }
    return delete;
  }
  
  public List<Object> findIdsByParentId(Object parentId, List<Object> parentIdist, Transaction t)
  {
    if (parentId != null) {
      return findIdsByParentId(parentId, t);
    }
    return findIdsByParentIdList(parentIdist, t);
  }
  
  private List<Object> findIdsByParentId(Object parentId, Transaction t)
  {
    String rawWhere = deriveWhereParentIdSql(false);
    
    EbeanServer server = getBeanDescriptor().getEbeanServer();
    Query<?> q = server.find(getPropertyType()).where().raw(rawWhere).query();
    
    bindWhereParendId(q, parentId);
    return server.findIds(q, t);
  }
  
  private List<Object> findIdsByParentIdList(List<Object> parentIdist, Transaction t)
  {
    String rawWhere = deriveWhereParentIdSql(true);
    String inClause = this.targetIdBinder.getIdInValueExpr(parentIdist.size());
    
    String expr = rawWhere + inClause;
    
    EbeanServer server = getBeanDescriptor().getEbeanServer();
    Query<?> q = (Query)server.find(getPropertyType()).where().raw(expr);
    for (int i = 0; i < parentIdist.size(); i++) {
      bindWhereParendId(q, parentIdist.get(i));
    }
    return server.findIds(q, t);
  }
  
  private void bindWhereParendId(Query<?> q, Object parentId)
  {
    if (this.exportedProperties.length == 1)
    {
      q.setParameter(1, parentId);
    }
    else
    {
      int pos = 1;
      for (int i = 0; i < this.exportedProperties.length; i++)
      {
        Object embVal = this.exportedProperties[i].getValue(parentId);
        q.setParameter(pos++, embVal);
      }
    }
  }
  
  public void addFkey()
  {
    if (this.importedId != null) {
      this.importedId.addFkeys(this.name);
    }
  }
  
  public boolean isValueLoaded(Object value)
  {
    if ((value instanceof EntityBean)) {
      return ((EntityBean)value)._ebean_getIntercept().isLoaded();
    }
    return true;
  }
  
  public InvalidValue validateCascade(Object value)
  {
    BeanDescriptor<?> target = getTargetDescriptor();
    return target.validate(true, value);
  }
  
  private boolean hasChangedEmbedded(Object bean, Object oldValues)
  {
    Object embValue = getValue(oldValues);
    if ((embValue instanceof EntityBean)) {
      return ((EntityBean)embValue)._ebean_getIntercept().isNewOrDirty();
    }
    if (embValue == null) {
      return getValue(bean) != null;
    }
    return false;
  }
  
  public boolean hasChanged(Object bean, Object oldValues)
  {
    if (this.embedded) {
      return hasChangedEmbedded(bean, oldValues);
    }
    Object value = getValue(bean);
    Object oldVal = getValue(oldValues);
    if (this.oneToOneExported) {
      return false;
    }
    if (value == null) {
      return oldVal != null;
    }
    if (oldValues == null) {
      return true;
    }
    return this.importedId.hasChanged(value, oldVal);
  }
  
  public BeanProperty[] getProperties()
  {
    return this.embeddedProps;
  }
  
  public void buildSelectExpressionChain(String prefix, List<String> selectChain)
  {
    prefix = SplitName.add(prefix, this.name);
    if (!this.embedded) {
      this.targetIdBinder.buildSelectExpressionChain(prefix, selectChain);
    } else {
      for (int i = 0; i < this.embeddedProps.length; i++) {
        this.embeddedProps[i].buildSelectExpressionChain(prefix, selectChain);
      }
    }
  }
  
  public boolean isOneToOne()
  {
    return this.oneToOne;
  }
  
  public boolean isOneToOneExported()
  {
    return this.oneToOneExported;
  }
  
  public boolean isEmbeddedVersion()
  {
    return this.embeddedVersion;
  }
  
  public boolean isImportedPrimaryKey()
  {
    return this.importedPrimaryKey;
  }
  
  public Class<?> getTargetType()
  {
    return getPropertyType();
  }
  
  public Object getCacheDataValue(Object bean)
  {
    if (this.embedded) {
      throw new RuntimeException();
    }
    Object ap = getValue(bean);
    if (ap == null) {
      return null;
    }
    return this.targetDescriptor.getId(ap);
  }
  
  public void setCacheDataValue(Object bean, Object cacheData, Object oldValues, boolean readOnly)
  {
    if (cacheData != null)
    {
      if (this.embedded) {
        throw new RuntimeException();
      }
      boolean vanillaMode = false;
      T ref = this.targetDescriptor.createReference(vanillaMode, Boolean.FALSE, cacheData, null);
      setValue(bean, ref);
      if (oldValues != null) {
        setValue(oldValues, ref);
      }
      if ((readOnly) && (!vanillaMode)) {
        ((EntityBean)ref)._ebean_intercept().setReadOnly(true);
      }
    }
  }
  
  public Object[] getAssocOneIdValues(Object bean)
  {
    return this.targetDescriptor.getIdBinder().getIdValues(bean);
  }
  
  public String getAssocOneIdExpr(String prefix, String operator)
  {
    return this.targetDescriptor.getIdBinder().getAssocOneIdExpr(prefix, operator);
  }
  
  public String getAssocIdInValueExpr(int size)
  {
    return this.targetDescriptor.getIdBinder().getIdInValueExpr(size);
  }
  
  public String getAssocIdInExpr(String prefix)
  {
    return this.targetDescriptor.getIdBinder().getAssocIdInExpr(prefix);
  }
  
  public boolean isAssocId()
  {
    return !this.embedded;
  }
  
  public boolean isAssocProperty()
  {
    return !this.embedded;
  }
  
  public Object createEmbeddedId()
  {
    return getTargetDescriptor().createVanillaBean();
  }
  
  public Object createEmptyReference()
  {
    return this.targetDescriptor.createEntityBean();
  }
  
  public void elSetReference(Object bean)
  {
    Object value = getValueIntercept(bean);
    if (value != null) {
      ((EntityBean)value)._ebean_getIntercept().setReference();
    }
  }
  
  public Object elGetReference(Object bean)
  {
    Object value = getValueIntercept(bean);
    if (value == null)
    {
      value = this.targetDescriptor.createEntityBean();
      setValueIntercept(bean, value);
    }
    return value;
  }
  
  public ImportedId getImportedId()
  {
    return this.importedId;
  }
  
  private String deriveWhereParentIdSql(boolean inClause)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.exportedProperties.length; i++)
    {
      String fkColumn = this.exportedProperties[i].getForeignDbColumn();
      if (i > 0)
      {
        String s = inClause ? "," : " and ";
        sb.append(s);
      }
      sb.append(fkColumn);
      if (!inClause) {
        sb.append("=? ");
      }
    }
    return sb.toString();
  }
  
  private ExportedProperty[] createExported()
  {
    BeanProperty[] uids = this.descriptor.propertiesId();
    
    ArrayList<ExportedProperty> list = new ArrayList();
    if ((uids.length == 1) && (uids[0].isEmbedded()))
    {
      BeanPropertyAssocOne<?> one = (BeanPropertyAssocOne)uids[0];
      BeanDescriptor<?> targetDesc = one.getTargetDescriptor();
      BeanProperty[] emIds = targetDesc.propertiesBaseScalar();
      try
      {
        for (int i = 0; i < emIds.length; i++)
        {
          ExportedProperty expProp = findMatch(true, emIds[i]);
          list.add(expProp);
        }
      }
      catch (PersistenceException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      for (int i = 0; i < uids.length; i++)
      {
        ExportedProperty expProp = findMatch(false, uids[i]);
        list.add(expProp);
      }
    }
    return (ExportedProperty[])list.toArray(new ExportedProperty[list.size()]);
  }
  
  private ExportedProperty findMatch(boolean embeddedProp, BeanProperty prop)
  {
    String matchColumn = prop.getDbColumn();
    
    String searchTable = this.tableJoin.getTable();
    TableJoinColumn[] columns = this.tableJoin.columns();
    for (int i = 0; i < columns.length; i++)
    {
      String matchTo = columns[i].getLocalDbColumn();
      if (matchColumn.equalsIgnoreCase(matchTo))
      {
        String foreignCol = columns[i].getForeignDbColumn();
        return new ExportedProperty(embeddedProp, foreignCol, prop);
      }
    }
    String msg = "Error with the Join on [" + getFullBeanName() + "]. Could not find the matching foreign key for [" + matchColumn + "] in table[" + searchTable + "]?" + " Perhaps using a @JoinColumn with the name/referencedColumnName attributes swapped?";
    
    throw new PersistenceException(msg);
  }
  
  public void appendSelect(DbSqlContext ctx, boolean subQuery)
  {
    if (!this.isTransient) {
      this.localHelp.appendSelect(ctx, subQuery);
    }
  }
  
  public void appendFrom(DbSqlContext ctx, boolean forceOuterJoin)
  {
    if (!this.isTransient) {
      this.localHelp.appendFrom(ctx, forceOuterJoin);
    }
  }
  
  public Object readSet(DbReadContext ctx, Object bean, Class<?> type)
    throws SQLException
  {
    boolean assignable = (type == null) || (this.owningType.isAssignableFrom(type));
    return this.localHelp.readSet(ctx, bean, assignable);
  }
  
  public Object read(DbReadContext ctx)
    throws SQLException
  {
    return this.localHelp.read(ctx);
  }
  
  public void loadIgnore(DbReadContext ctx)
  {
    this.localHelp.loadIgnore(ctx);
  }
  
  public void load(SqlBeanLoad sqlBeanLoad)
    throws SQLException
  {
    Object dbVal = sqlBeanLoad.load(this);
    if ((this.embedded) && (sqlBeanLoad.isLazyLoad()) && 
      ((dbVal instanceof EntityBean))) {
      ((EntityBean)dbVal)._ebean_getIntercept().setLoaded();
    }
  }
  
  private BeanPropertyAssocOne<T>.LocalHelp createHelp(boolean embedded, boolean oneToOneExported)
  {
    if (embedded) {
      return new Embedded(null);
    }
    if (oneToOneExported) {
      return new ReferenceExported(null);
    }
    return new Reference(this);
  }
  
  private abstract class LocalHelp
  {
    private LocalHelp() {}
    
    abstract void loadIgnore(DbReadContext paramDbReadContext);
    
    abstract Object read(DbReadContext paramDbReadContext)
      throws SQLException;
    
    abstract Object readSet(DbReadContext paramDbReadContext, Object paramObject, boolean paramBoolean)
      throws SQLException;
    
    abstract void appendSelect(DbSqlContext paramDbSqlContext, boolean paramBoolean);
    
    abstract void appendFrom(DbSqlContext paramDbSqlContext, boolean paramBoolean);
  }
  
  private final class Embedded
    extends BeanPropertyAssocOne.LocalHelp
  {
    private Embedded()
    {
      super(null);
    }
    
    void loadIgnore(DbReadContext ctx)
    {
      for (int i = 0; i < BeanPropertyAssocOne.this.embeddedProps.length; i++) {
        BeanPropertyAssocOne.this.embeddedProps[i].loadIgnore(ctx);
      }
    }
    
    Object readSet(DbReadContext ctx, Object bean, boolean assignable)
      throws SQLException
    {
      Object dbVal = read(ctx);
      if ((bean != null) && (assignable))
      {
        BeanPropertyAssocOne.this.setValue(bean, dbVal);
        ctx.propagateState(dbVal);
        return dbVal;
      }
      return null;
    }
    
    Object read(DbReadContext ctx)
      throws SQLException
    {
      EntityBean embeddedBean = BeanPropertyAssocOne.this.targetDescriptor.createEntityBean();
      
      boolean notNull = false;
      for (int i = 0; i < BeanPropertyAssocOne.this.embeddedProps.length; i++)
      {
        Object value = BeanPropertyAssocOne.this.embeddedProps[i].readSet(ctx, embeddedBean, null);
        if (value != null) {
          notNull = true;
        }
      }
      if (notNull)
      {
        ctx.propagateState(embeddedBean);
        return embeddedBean;
      }
      return null;
    }
    
    void appendFrom(DbSqlContext ctx, boolean forceOuterJoin) {}
    
    void appendSelect(DbSqlContext ctx, boolean subQuery)
    {
      for (int i = 0; i < BeanPropertyAssocOne.this.embeddedProps.length; i++) {
        BeanPropertyAssocOne.this.embeddedProps[i].appendSelect(ctx, subQuery);
      }
    }
  }
  
  private final class Reference
    extends BeanPropertyAssocOne.LocalHelp
  {
    Reference()
    {
      super(null);
    }
    
    void loadIgnore(DbReadContext ctx)
    {
      BeanPropertyAssocOne.this.targetIdBinder.loadIgnore(ctx);
      if (BeanPropertyAssocOne.this.targetInheritInfo != null) {
        ctx.getDataReader().incrementPos(1);
      }
    }
    
    Object readSet(DbReadContext ctx, Object bean, boolean assignable)
      throws SQLException
    {
      Object val = read(ctx);
      if ((bean != null) && (assignable))
      {
        BeanPropertyAssocOne.this.setValue(bean, val);
        ctx.propagateState(val);
      }
      return val;
    }
    
    Object read(DbReadContext ctx)
      throws SQLException
    {
      BeanDescriptor<?> rowDescriptor = null;
      Class<?> rowType = BeanPropertyAssocOne.this.targetType;
      if (BeanPropertyAssocOne.this.targetInheritInfo != null)
      {
        InheritInfo rowInheritInfo = BeanPropertyAssocOne.this.targetInheritInfo.readType(ctx);
        if (rowInheritInfo != null)
        {
          rowType = rowInheritInfo.getType();
          rowDescriptor = rowInheritInfo.getBeanDescriptor();
        }
      }
      Object id = BeanPropertyAssocOne.this.targetIdBinder.read(ctx);
      if (id == null) {
        return null;
      }
      Object existing = ctx.getPersistenceContext().get(rowType, id);
      if (existing != null) {
        return existing;
      }
      Object parent = null;
      boolean vanillaMode = ctx.isVanillaMode();
      
      Boolean readOnly = ctx.isReadOnly();
      Object ref;
      Object ref;
      if (BeanPropertyAssocOne.this.targetInheritInfo != null) {
        ref = rowDescriptor.createReference(vanillaMode, readOnly, id, parent);
      } else {
        ref = BeanPropertyAssocOne.this.targetDescriptor.createReference(vanillaMode, readOnly, id, parent);
      }
      Object existingBean = ctx.getPersistenceContext().putIfAbsent(id, ref);
      if (existingBean != null)
      {
        ref = existingBean;
      }
      else if (!vanillaMode)
      {
        EntityBeanIntercept ebi = ((EntityBean)ref)._ebean_getIntercept();
        if (Boolean.TRUE.equals(ctx.isReadOnly())) {
          ebi.setReadOnly(true);
        }
        ctx.register(BeanPropertyAssocOne.this.name, ebi);
      }
      return ref;
    }
    
    void appendFrom(DbSqlContext ctx, boolean forceOuterJoin)
    {
      if (BeanPropertyAssocOne.this.targetInheritInfo != null)
      {
        String relativePrefix = ctx.getRelativePrefix(BeanPropertyAssocOne.this.name);
        BeanPropertyAssocOne.this.tableJoin.addJoin(forceOuterJoin, relativePrefix, ctx);
      }
    }
    
    void appendSelect(DbSqlContext ctx, boolean subQuery)
    {
      if ((!subQuery) && (BeanPropertyAssocOne.this.targetInheritInfo != null))
      {
        String relativePrefix = ctx.getRelativePrefix(BeanPropertyAssocOne.this.getName());
        String tableAlias = ctx.getTableAlias(relativePrefix);
        ctx.appendColumn(tableAlias, BeanPropertyAssocOne.this.targetInheritInfo.getDiscriminatorColumn());
      }
      BeanPropertyAssocOne.this.importedId.sqlAppend(ctx);
    }
  }
  
  private final class ReferenceExported
    extends BeanPropertyAssocOne.LocalHelp
  {
    private ReferenceExported()
    {
      super(null);
    }
    
    void loadIgnore(DbReadContext ctx)
    {
      BeanPropertyAssocOne.this.targetDescriptor.getIdBinder().loadIgnore(ctx);
    }
    
    Object readSet(DbReadContext ctx, Object bean, boolean assignable)
      throws SQLException
    {
      Object dbVal = read(ctx);
      if ((bean != null) && (assignable))
      {
        BeanPropertyAssocOne.this.setValue(bean, dbVal);
        ctx.propagateState(dbVal);
      }
      return dbVal;
    }
    
    Object read(DbReadContext ctx)
      throws SQLException
    {
      IdBinder idBinder = BeanPropertyAssocOne.this.targetDescriptor.getIdBinder();
      Object id = idBinder.read(ctx);
      if (id == null) {
        return null;
      }
      PersistenceContext persistCtx = ctx.getPersistenceContext();
      Object existing = persistCtx.get(BeanPropertyAssocOne.this.targetType, id);
      if (existing != null) {
        return existing;
      }
      boolean vanillaMode = ctx.isVanillaMode();
      Object parent = null;
      Object ref = BeanPropertyAssocOne.this.targetDescriptor.createReference(vanillaMode, ctx.isReadOnly(), id, parent);
      if (!vanillaMode)
      {
        EntityBeanIntercept ebi = ((EntityBean)ref)._ebean_getIntercept();
        if (Boolean.TRUE.equals(ctx.isReadOnly())) {
          ebi.setReadOnly(true);
        }
        persistCtx.put(id, ref);
        ctx.register(BeanPropertyAssocOne.this.name, ebi);
      }
      return ref;
    }
    
    void appendSelect(DbSqlContext ctx, boolean subQuery)
    {
      String relativePrefix = ctx.getRelativePrefix(BeanPropertyAssocOne.this.getName());
      ctx.pushTableAlias(relativePrefix);
      
      IdBinder idBinder = BeanPropertyAssocOne.this.targetDescriptor.getIdBinder();
      idBinder.appendSelect(ctx, subQuery);
      
      ctx.popTableAlias();
    }
    
    void appendFrom(DbSqlContext ctx, boolean forceOuterJoin)
    {
      String relativePrefix = ctx.getRelativePrefix(BeanPropertyAssocOne.this.getName());
      BeanPropertyAssocOne.this.tableJoin.addJoin(forceOuterJoin, relativePrefix, ctx);
    }
  }
  
  public void jsonWrite(WriteJsonContext ctx, Object bean)
  {
    Object value = getValueIntercept(bean);
    if (value == null)
    {
      ctx.beginAssocOneIsNull(this.name);
    }
    else if (!ctx.isParentBean(value))
    {
      ctx.pushParentBean(bean);
      ctx.beginAssocOne(this.name);
      BeanDescriptor<?> refDesc = this.descriptor.getBeanDescriptor(value.getClass());
      refDesc.jsonWrite(ctx, value);
      ctx.endAssocOne();
      ctx.popParentBean();
    }
  }
  
  public void jsonRead(ReadJsonContext ctx, Object bean)
  {
    T assocBean = this.targetDescriptor.jsonReadBean(ctx, this.name);
    setValue(bean, assocBean);
  }
}
