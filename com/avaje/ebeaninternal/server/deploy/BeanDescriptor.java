package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.Query.UseIndex;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.dbplatform.IdGenerator;
import com.avaje.ebean.config.dbplatform.IdType;
import com.avaje.ebean.event.BeanFinder;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebean.event.BeanPersistListener;
import com.avaje.ebean.event.BeanQueryAdapter;
import com.avaje.ebean.text.TextException;
import com.avaje.ebean.text.json.JsonWriteBeanVisitor;
import com.avaje.ebean.validation.factory.Validator;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiUpdatePlan;
import com.avaje.ebeaninternal.api.TransactionEventTable.TableIUD;
import com.avaje.ebeaninternal.server.cache.CachedBeanData;
import com.avaje.ebeaninternal.server.cache.CachedBeanDataFromBean;
import com.avaje.ebeaninternal.server.cache.CachedBeanDataToBean;
import com.avaje.ebeaninternal.server.cache.CachedBeanDataUpdate;
import com.avaje.ebeaninternal.server.cache.CachedManyIds;
import com.avaje.ebeaninternal.server.core.CacheOptions;
import com.avaje.ebeaninternal.server.core.ConcurrencyMode;
import com.avaje.ebeaninternal.server.core.DefaultSqlUpdate;
import com.avaje.ebeaninternal.server.core.InternString;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanPropertyLists;
import com.avaje.ebeaninternal.server.el.ElComparator;
import com.avaje.ebeaninternal.server.el.ElComparatorCompound;
import com.avaje.ebeaninternal.server.el.ElComparatorProperty;
import com.avaje.ebeaninternal.server.el.ElPropertyChainBuilder;
import com.avaje.ebeaninternal.server.el.ElPropertyDeploy;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.ldap.LdapPersistenceException;
import com.avaje.ebeaninternal.server.persist.DmlUtil;
import com.avaje.ebeaninternal.server.query.CQueryPlan;
import com.avaje.ebeaninternal.server.query.SplitName;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.reflect.BeanReflect;
import com.avaje.ebeaninternal.server.text.json.ReadJsonContext;
import com.avaje.ebeaninternal.server.text.json.ReadJsonContext.ReadBeanState;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext.WriteBeanState;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.type.TypeManager;
import com.avaje.ebeaninternal.util.SortByClause;
import com.avaje.ebeaninternal.util.SortByClause.Property;
import com.avaje.ebeaninternal.util.SortByClauseParser;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import javax.persistence.PersistenceException;

public class BeanDescriptor<T>
{
  private static final Logger logger = Logger.getLogger(BeanDescriptor.class.getName());
  private final ConcurrentHashMap<Integer, SpiUpdatePlan> updatePlanCache = new ConcurrentHashMap();
  private final ConcurrentHashMap<Integer, CQueryPlan> queryPlanCache = new ConcurrentHashMap();
  private final ConcurrentHashMap<String, ElPropertyValue> elGetCache = new ConcurrentHashMap();
  private final ConcurrentHashMap<String, ElComparator<T>> comparatorCache = new ConcurrentHashMap();
  private final ConcurrentHashMap<String, BeanFkeyProperty> fkeyMap = new ConcurrentHashMap();
  private final String serverName;
  private final EntityType entityType;
  private final IdType idType;
  private final IdGenerator idGenerator;
  private final String sequenceName;
  private final String ldapBaseDn;
  private final String[] ldapObjectclasses;
  private final String selectLastInsertedId;
  private final boolean autoFetchTunable;
  private final boolean cacheSharableBeans;
  private final String lazyFetchIncludes;
  private final ConcurrencyMode concurrencyMode;
  private final String[] dependantTables;
  private final CompoundUniqueContraint[] compoundUniqueConstraints;
  private final Map<String, String> extraAttrMap;
  private final String baseTable;
  private final BeanReflect beanReflect;
  private final LinkedHashMap<String, BeanProperty> propMap;
  private final LinkedHashMap<String, BeanProperty> propMapByDbColumn;
  private final Class<T> beanType;
  private final BeanDescriptorMap owner;
  private final Class<?> factoryType;
  private final boolean enhancedBean;
  private volatile BeanPersistController persistController;
  private volatile BeanPersistListener<T> persistListener;
  private volatile BeanQueryAdapter queryAdapter;
  private final BeanFinder<T> beanFinder;
  private final TableJoin[] derivedTableJoins;
  private final InheritInfo inheritInfo;
  private final BeanProperty[] propertiesId;
  private final BeanProperty[] propertiesVersion;
  private final BeanProperty propertiesNaturalKey;
  private final BeanProperty[] propertiesLocal;
  private final BeanPropertyAssocOne<?> unidirectional;
  private final int namesOfManyPropsHash;
  private final Set<String> namesOfManyProps;
  private final BeanProperty[] propertiesNonMany;
  private final BeanPropertyAssocMany<?>[] propertiesMany;
  private final BeanPropertyAssocMany<?>[] propertiesManySave;
  private final BeanPropertyAssocMany<?>[] propertiesManyDelete;
  private final BeanPropertyAssocMany<?>[] propertiesManyToMany;
  private final BeanPropertyAssocOne<?>[] propertiesOne;
  private final BeanPropertyAssocOne<?>[] propertiesOneImported;
  private final BeanPropertyAssocOne<?>[] propertiesOneImportedSave;
  private final BeanPropertyAssocOne<?>[] propertiesOneImportedDelete;
  private final BeanPropertyAssocOne<?>[] propertiesOneExported;
  private final BeanPropertyAssocOne<?>[] propertiesOneExportedSave;
  private final BeanPropertyAssocOne<?>[] propertiesOneExportedDelete;
  private final BeanPropertyAssocOne<?>[] propertiesEmbedded;
  private final BeanProperty[] propertiesBaseScalar;
  private final BeanPropertyCompound[] propertiesBaseCompound;
  private final BeanProperty[] propertiesTransient;
  final BeanProperty[] propertiesNonTransient;
  private final BeanProperty propertyFirstVersion;
  private final BeanProperty propertySingleId;
  private final String fullName;
  private final Map<String, DeployNamedQuery> namedQueries;
  private final Map<String, DeployNamedUpdate> namedUpdates;
  private final boolean hasLocalValidation;
  private final boolean hasCascadeValidation;
  private final BeanProperty[] propertiesValidationLocal;
  private final BeanProperty[] propertiesValidationCascade;
  private final Validator[] beanValidators;
  private boolean saveRecurseSkippable;
  private boolean deleteRecurseSkippable;
  private final TypeManager typeManager;
  private final IdBinder idBinder;
  private String idBinderInLHSSql;
  private String idBinderIdSql;
  private String deleteByIdSql;
  private String deleteByIdInSql;
  private final String name;
  private final String baseTableAlias;
  private final boolean updateChangesOnly;
  private final ServerCacheManager cacheManager;
  private final CacheOptions cacheOptions;
  private final String defaultSelectClause;
  private final Set<String> defaultSelectClauseSet;
  private final String[] defaultSelectDbArray;
  private final String descriptorId;
  private final Query.UseIndex useIndex;
  private SpiEbeanServer ebeanServer;
  private ServerCache beanCache;
  private ServerCache naturalKeyCache;
  private ServerCache queryCache;
  
  public static enum EntityType
  {
    ORM,  EMBEDDED,  SQL,  META,  LDAP,  XMLELEMENT;
    
    private EntityType() {}
  }
  
  public BeanDescriptor(BeanDescriptorMap owner, TypeManager typeManager, DeployBeanDescriptor<T> deploy, String descriptorId)
  {
    this.owner = owner;
    this.cacheManager = owner.getCacheManager();
    this.serverName = owner.getServerName();
    this.entityType = deploy.getEntityType();
    this.name = InternString.intern(deploy.getName());
    this.baseTableAlias = "t0";
    this.fullName = InternString.intern(deploy.getFullName());
    this.descriptorId = descriptorId;
    
    this.useIndex = deploy.getUseIndex();
    this.typeManager = typeManager;
    this.beanType = deploy.getBeanType();
    this.factoryType = deploy.getFactoryType();
    this.enhancedBean = this.beanType.equals(this.factoryType);
    this.namedQueries = deploy.getNamedQueries();
    this.namedUpdates = deploy.getNamedUpdates();
    
    this.inheritInfo = deploy.getInheritInfo();
    
    this.beanFinder = deploy.getBeanFinder();
    this.persistController = deploy.getPersistController();
    this.persistListener = deploy.getPersistListener();
    this.queryAdapter = deploy.getQueryAdapter();
    this.cacheOptions = deploy.getCacheOptions();
    
    this.defaultSelectClause = deploy.getDefaultSelectClause();
    this.defaultSelectClauseSet = deploy.parseDefaultSelectClause(this.defaultSelectClause);
    this.defaultSelectDbArray = deploy.getDefaultSelectDbArray(this.defaultSelectClauseSet);
    
    this.idType = deploy.getIdType();
    this.idGenerator = deploy.getIdGenerator();
    this.ldapBaseDn = deploy.getLdapBaseDn();
    this.ldapObjectclasses = deploy.getLdapObjectclasses();
    this.sequenceName = deploy.getSequenceName();
    this.selectLastInsertedId = deploy.getSelectLastInsertedId();
    this.lazyFetchIncludes = InternString.intern(deploy.getLazyFetchIncludes());
    this.concurrencyMode = deploy.getConcurrencyMode();
    this.updateChangesOnly = deploy.isUpdateChangesOnly();
    
    this.dependantTables = deploy.getDependantTables();
    this.compoundUniqueConstraints = deploy.getCompoundUniqueConstraints();
    
    this.extraAttrMap = deploy.getExtraAttributeMap();
    
    this.baseTable = InternString.intern(deploy.getBaseTable());
    
    this.beanReflect = deploy.getBeanReflect();
    
    this.autoFetchTunable = ((EntityType.ORM.equals(this.entityType)) && (this.beanFinder == null));
    
    DeployBeanPropertyLists listHelper = new DeployBeanPropertyLists(owner, this, deploy);
    
    this.propMap = listHelper.getPropertyMap();
    this.propMapByDbColumn = getReverseMap(this.propMap);
    this.propertiesTransient = listHelper.getTransients();
    this.propertiesNonTransient = listHelper.getNonTransients();
    this.propertiesBaseScalar = listHelper.getBaseScalar();
    this.propertiesBaseCompound = listHelper.getBaseCompound();
    this.propertiesId = listHelper.getId();
    this.propertiesNaturalKey = listHelper.getNaturalKey();
    this.propertiesVersion = listHelper.getVersion();
    this.propertiesEmbedded = listHelper.getEmbedded();
    this.propertiesLocal = listHelper.getLocal();
    this.unidirectional = listHelper.getUnidirectional();
    this.propertiesOne = listHelper.getOnes();
    this.propertiesOneExported = listHelper.getOneExported();
    this.propertiesOneExportedSave = listHelper.getOneExportedSave();
    this.propertiesOneExportedDelete = listHelper.getOneExportedDelete();
    this.propertiesOneImported = listHelper.getOneImported();
    this.propertiesOneImportedSave = listHelper.getOneImportedSave();
    this.propertiesOneImportedDelete = listHelper.getOneImportedDelete();
    
    this.propertiesMany = listHelper.getMany();
    this.propertiesNonMany = listHelper.getNonMany();
    this.propertiesManySave = listHelper.getManySave();
    this.propertiesManyDelete = listHelper.getManyDelete();
    this.propertiesManyToMany = listHelper.getManyToMany();
    boolean noRelationships = this.propertiesOne.length + this.propertiesMany.length == 0;
    this.cacheSharableBeans = ((noRelationships) && (this.cacheOptions.isReadOnly()));
    
    this.namesOfManyProps = deriveManyPropNames();
    this.namesOfManyPropsHash = this.namesOfManyProps.hashCode();
    
    this.derivedTableJoins = listHelper.getTableJoin();
    this.propertyFirstVersion = listHelper.getFirstVersion();
    if (this.propertiesId.length == 1) {
      this.propertySingleId = this.propertiesId[0];
    } else {
      this.propertySingleId = null;
    }
    this.saveRecurseSkippable = (0 == this.propertiesOneExportedSave.length + this.propertiesOneImportedSave.length + this.propertiesManySave.length);
    
    this.deleteRecurseSkippable = (0 == this.propertiesOneExportedDelete.length + this.propertiesOneImportedDelete.length + this.propertiesManyDelete.length);
    
    this.propertiesValidationLocal = listHelper.getPropertiesWithValidators(false);
    this.propertiesValidationCascade = listHelper.getPropertiesWithValidators(true);
    this.beanValidators = listHelper.getBeanValidators();
    this.hasLocalValidation = ((this.propertiesValidationLocal.length > 0) || (this.beanValidators.length > 0));
    this.hasCascadeValidation = ((this.propertiesValidationCascade.length > 0) || (this.beanValidators.length > 0));
    
    this.idBinder = owner.createIdBinder(this.propertiesId);
  }
  
  private LinkedHashMap<String, BeanProperty> getReverseMap(LinkedHashMap<String, BeanProperty> propMap)
  {
    LinkedHashMap<String, BeanProperty> revMap = new LinkedHashMap(propMap.size() * 2);
    for (BeanProperty prop : propMap.values()) {
      if (prop.getDbColumn() != null) {
        revMap.put(prop.getDbColumn(), prop);
      }
    }
    return revMap;
  }
  
  public void setEbeanServer(SpiEbeanServer ebeanServer)
  {
    this.ebeanServer = ebeanServer;
    for (int i = 0; i < this.propertiesMany.length; i++) {
      this.propertiesMany[i].setLoader(ebeanServer);
    }
  }
  
  public ConcurrencyMode determineConcurrencyMode(Object bean)
  {
    if (this.propertyFirstVersion == null) {
      return ConcurrencyMode.NONE;
    }
    Object v = this.propertyFirstVersion.getValue(bean);
    return v == null ? ConcurrencyMode.NONE : ConcurrencyMode.VERSION;
  }
  
  public Set<String> getDirtyEmbeddedProperties(Object bean)
  {
    HashSet<String> dirtyProperties = null;
    for (int i = 0; i < this.propertiesEmbedded.length; i++)
    {
      Object embValue = this.propertiesEmbedded[i].getValue(bean);
      if ((embValue instanceof EntityBean))
      {
        if (((EntityBean)embValue)._ebean_getIntercept().isDirty())
        {
          if (dirtyProperties == null) {
            dirtyProperties = new HashSet();
          }
          dirtyProperties.add(this.propertiesEmbedded[i].getName());
        }
      }
      else
      {
        if (dirtyProperties == null) {
          dirtyProperties = new HashSet();
        }
        dirtyProperties.add(this.propertiesEmbedded[i].getName());
      }
    }
    return dirtyProperties;
  }
  
  public Set<String> determineLoadedProperties(Object bean)
  {
    HashSet<String> nonNullProps = new HashSet();
    for (int j = 0; j < this.propertiesId.length; j++) {
      if (this.propertiesId[j].getValue(bean) != null) {
        nonNullProps.add(this.propertiesId[j].getName());
      }
    }
    for (int i = 0; i < this.propertiesNonTransient.length; i++) {
      if (this.propertiesNonTransient[i].getValue(bean) != null) {
        nonNullProps.add(this.propertiesNonTransient[i].getName());
      }
    }
    return nonNullProps;
  }
  
  public SpiEbeanServer getEbeanServer()
  {
    return this.ebeanServer;
  }
  
  public EntityType getEntityType()
  {
    return this.entityType;
  }
  
  public Query.UseIndex getUseIndex()
  {
    return this.useIndex;
  }
  
  public void initialiseId()
  {
    if (logger.isLoggable(Level.FINER)) {
      logger.finer("BeanDescriptor initialise " + this.fullName);
    }
    if (this.inheritInfo != null) {
      this.inheritInfo.setDescriptor(this);
    }
    if (isEmbedded())
    {
      Iterator<BeanProperty> it = propertiesAll();
      while (it.hasNext())
      {
        BeanProperty prop = (BeanProperty)it.next();
        prop.initialise();
      }
    }
    else
    {
      BeanProperty[] idProps = propertiesId();
      for (int i = 0; i < idProps.length; i++) {
        idProps[i].initialise();
      }
    }
  }
  
  public void initialiseOther()
  {
    if (!isEmbedded())
    {
      Iterator<BeanProperty> it = propertiesAll();
      while (it.hasNext())
      {
        BeanProperty prop = (BeanProperty)it.next();
        if (!prop.isId()) {
          prop.initialise();
        }
      }
    }
    if (this.unidirectional != null) {
      this.unidirectional.initialise();
    }
    this.idBinder.initialise();
    this.idBinderInLHSSql = this.idBinder.getBindIdInSql(this.baseTableAlias);
    this.idBinderIdSql = this.idBinder.getBindIdSql(this.baseTableAlias);
    String idBinderInLHSSqlNoAlias = this.idBinder.getBindIdInSql(null);
    String idEqualsSql = this.idBinder.getBindIdSql(null);
    
    this.deleteByIdSql = ("delete from " + this.baseTable + " where " + idEqualsSql);
    this.deleteByIdInSql = ("delete from " + this.baseTable + " where " + idBinderInLHSSqlNoAlias + " ");
    if (!isEmbedded()) {
      for (DeployNamedUpdate namedUpdate : this.namedUpdates.values())
      {
        DeployUpdateParser parser = new DeployUpdateParser(this);
        namedUpdate.initialise(parser);
      }
    }
  }
  
  public void initInheritInfo()
  {
    if (this.inheritInfo != null)
    {
      if (this.saveRecurseSkippable) {
        this.saveRecurseSkippable = this.inheritInfo.isSaveRecurseSkippable();
      }
      if (this.deleteRecurseSkippable) {
        this.deleteRecurseSkippable = this.inheritInfo.isDeleteRecurseSkippable();
      }
    }
  }
  
  public void cacheInitialise()
  {
    if (this.cacheOptions.isUseNaturalKeyCache()) {
      this.naturalKeyCache = this.cacheManager.getNaturalKeyCache(this.beanType);
    }
    if (this.cacheOptions.isUseCache()) {
      this.beanCache = this.cacheManager.getBeanCache(this.beanType);
    }
  }
  
  protected boolean hasInheritance()
  {
    return this.inheritInfo != null;
  }
  
  protected boolean isDynamicSubclass()
  {
    return !this.beanType.equals(this.factoryType);
  }
  
  public void setLdapObjectClasses(Attributes attributes)
  {
    if (this.ldapObjectclasses != null)
    {
      BasicAttribute ocAttrs = new BasicAttribute("objectclass");
      for (int i = 0; i < this.ldapObjectclasses.length; i++) {
        ocAttrs.add(this.ldapObjectclasses[i]);
      }
      attributes.put(ocAttrs);
    }
  }
  
  public Attributes createAttributes()
  {
    Attributes attrs = new BasicAttributes(true);
    setLdapObjectClasses(attrs);
    return attrs;
  }
  
  public String getLdapBaseDn()
  {
    return this.ldapBaseDn;
  }
  
  public LdapName createLdapNameById(Object id)
    throws InvalidNameException
  {
    LdapName baseDn = new LdapName(this.ldapBaseDn);
    this.idBinder.createLdapNameById(baseDn, id);
    return baseDn;
  }
  
  public LdapName createLdapName(Object bean)
  {
    try
    {
      LdapName name = new LdapName(this.ldapBaseDn);
      if (bean != null) {
        this.idBinder.createLdapNameByBean(name, bean);
      }
      return name;
    }
    catch (InvalidNameException e)
    {
      throw new LdapPersistenceException(e);
    }
  }
  
  public SqlUpdate deleteById(Object id, List<Object> idList)
  {
    if (id != null) {
      return deleteById(id);
    }
    return deleteByIdList(idList);
  }
  
  private SqlUpdate deleteByIdList(List<Object> idList)
  {
    StringBuilder sb = new StringBuilder(this.deleteByIdInSql);
    String inClause = this.idBinder.getIdInValueExprDelete(idList.size());
    sb.append(inClause);
    
    DefaultSqlUpdate delete = new DefaultSqlUpdate(sb.toString());
    for (int i = 0; i < idList.size(); i++) {
      this.idBinder.bindId(delete, idList.get(i));
    }
    return delete;
  }
  
  private SqlUpdate deleteById(Object id)
  {
    DefaultSqlUpdate sqlDelete = new DefaultSqlUpdate(this.deleteByIdSql);
    
    Object[] bindValues = this.idBinder.getBindValues(id);
    for (int i = 0; i < bindValues.length; i++) {
      sqlDelete.addParameter(bindValues[i]);
    }
    return sqlDelete;
  }
  
  public void add(BeanFkeyProperty fkey)
  {
    this.fkeyMap.put(fkey.getName(), fkey);
  }
  
  public void initialiseFkeys()
  {
    for (int i = 0; i < this.propertiesOneImported.length; i++) {
      this.propertiesOneImported[i].addFkey();
    }
  }
  
  public boolean calculateUseCache(Boolean queryUseCache)
  {
    return queryUseCache != null ? queryUseCache.booleanValue() : isBeanCaching();
  }
  
  public boolean calculateUseNaturalKeyCache(Boolean queryUseCache)
  {
    return queryUseCache != null ? queryUseCache.booleanValue() : isBeanCaching();
  }
  
  public CacheOptions getCacheOptions()
  {
    return this.cacheOptions;
  }
  
  public EncryptKey getEncryptKey(BeanProperty p)
  {
    return this.owner.getEncryptKey(this.baseTable, p.getDbColumn());
  }
  
  public EncryptKey getEncryptKey(String tableName, String columnName)
  {
    return this.owner.getEncryptKey(tableName, columnName);
  }
  
  public void runCacheWarming()
  {
    if (this.cacheOptions == null) {
      return;
    }
    String warmingQuery = this.cacheOptions.getWarmingQuery();
    if ((warmingQuery != null) && (warmingQuery.trim().length() > 0))
    {
      Query<T> query = this.ebeanServer.createQuery(this.beanType, warmingQuery);
      query.setUseCache(true);
      query.setReadOnly(true);
      query.setLoadBeanCache(true);
      List<T> list = query.findList();
      if (logger.isLoggable(Level.INFO))
      {
        String msg = "Loaded " + this.beanType + " cache with [" + list.size() + "] beans";
        logger.info(msg);
      }
    }
  }
  
  public boolean hasDefaultSelectClause()
  {
    return this.defaultSelectClause != null;
  }
  
  public String getDefaultSelectClause()
  {
    return this.defaultSelectClause;
  }
  
  public Set<String> getDefaultSelectClauseSet()
  {
    return this.defaultSelectClauseSet;
  }
  
  public String[] getDefaultSelectDbArray()
  {
    return this.defaultSelectDbArray;
  }
  
  public boolean isInheritanceRoot()
  {
    return (this.inheritInfo == null) || (this.inheritInfo.isRoot());
  }
  
  public boolean isQueryCaching()
  {
    return this.queryCache != null;
  }
  
  public boolean isBeanCaching()
  {
    return this.beanCache != null;
  }
  
  public boolean cacheIsUseManyId()
  {
    return isBeanCaching();
  }
  
  public boolean isCacheNotify()
  {
    if ((isBeanCaching()) || (isQueryCaching())) {
      return true;
    }
    for (int i = 0; i < this.propertiesOneImported.length; i++) {
      if (this.propertiesOneImported[i].getTargetDescriptor().isBeanCaching()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isUsingL2Cache()
  {
    return isBeanCaching();
  }
  
  public void cacheNotify(TransactionEventTable.TableIUD tableIUD)
  {
    if (tableIUD.isUpdateOrDelete()) {
      cacheClear();
    }
    queryCacheClear();
  }
  
  public void queryCacheClear()
  {
    if (this.queryCache != null) {
      this.queryCache.clear();
    }
  }
  
  public BeanCollection<T> queryCacheGet(Object id)
  {
    if (this.queryCache == null) {
      return null;
    }
    return (BeanCollection)this.queryCache.get(id);
  }
  
  public void queryCachePut(Object id, BeanCollection<T> query)
  {
    if (this.queryCache == null) {
      this.queryCache = this.cacheManager.getQueryCache(this.beanType);
    }
    this.queryCache.put(id, query);
  }
  
  private ServerCache getBeanCache()
  {
    if (this.beanCache == null) {
      this.beanCache = this.cacheManager.getBeanCache(this.beanType);
    }
    return this.beanCache;
  }
  
  public void cacheClear()
  {
    if (this.beanCache != null) {
      this.beanCache.clear();
    }
  }
  
  public void cachePutBeanData(Object bean)
  {
    CachedBeanData beanData = CachedBeanDataFromBean.extract(this, bean);
    
    Object id = getId(bean);
    getBeanCache().put(id, beanData);
    if ((beanData.isNaturalKeyUpdate()) && (this.naturalKeyCache != null))
    {
      Object naturalKey = beanData.getNaturalKey();
      if (naturalKey != null) {
        this.naturalKeyCache.put(naturalKey, id);
      }
    }
  }
  
  public boolean cacheLoadMany(BeanPropertyAssocMany<?> many, BeanCollection<?> bc, Object parentId, Boolean readOnly, boolean vanilla)
  {
    CachedManyIds ids = cacheGetCachedManyIds(parentId, many.getName());
    if (ids == null) {
      return false;
    }
    Object ownerBean = bc.getOwnerBean();
    EntityBeanIntercept ebi = ((EntityBean)ownerBean)._ebean_getIntercept();
    PersistenceContext persistenceContext = ebi.getPersistenceContext();
    
    BeanDescriptor<?> targetDescriptor = many.getTargetDescriptor();
    
    List<Object> idList = ids.getIdList();
    bc.checkEmptyLazyLoad();
    for (int i = 0; i < idList.size(); i++)
    {
      Object id = idList.get(i);
      Object refBean = targetDescriptor.createReference(vanilla, readOnly, id, null);
      EntityBeanIntercept refEbi = ((EntityBean)refBean)._ebean_getIntercept();
      
      many.add(bc, refBean);
      persistenceContext.put(id, refBean);
      refEbi.setPersistenceContext(persistenceContext);
    }
    return true;
  }
  
  public void cachePutMany(BeanPropertyAssocMany<?> many, BeanCollection<?> bc, Object parentId)
  {
    BeanDescriptor<?> targetDescriptor = many.getTargetDescriptor();
    Collection<?> actualDetails = bc.getActualDetails();
    ArrayList<Object> idList = new ArrayList();
    for (Object bean : actualDetails)
    {
      Object id = targetDescriptor.getId(bean);
      idList.add(id);
    }
    CachedManyIds ids = new CachedManyIds(idList);
    cachePutCachedManyIds(parentId, many.getName(), ids);
  }
  
  public void cacheRemoveCachedManyIds(Object parentId, String propertyName)
  {
    ServerCache collectionIdsCache = this.cacheManager.getCollectionIdsCache(this.beanType, propertyName);
    collectionIdsCache.remove(parentId);
  }
  
  public void cacheClearCachedManyIds(String propertyName)
  {
    ServerCache collectionIdsCache = this.cacheManager.getCollectionIdsCache(this.beanType, propertyName);
    collectionIdsCache.clear();
  }
  
  public CachedManyIds cacheGetCachedManyIds(Object parentId, String propertyName)
  {
    ServerCache collectionIdsCache = this.cacheManager.getCollectionIdsCache(this.beanType, propertyName);
    return (CachedManyIds)collectionIdsCache.get(parentId);
  }
  
  public void cachePutCachedManyIds(Object parentId, String propertyName, CachedManyIds ids)
  {
    ServerCache collectionIdsCache = this.cacheManager.getCollectionIdsCache(this.beanType, propertyName);
    collectionIdsCache.put(parentId, ids);
  }
  
  public T cacheGetBean(Object id, boolean vanilla, Boolean readOnly)
  {
    CachedBeanData d = (CachedBeanData)getBeanCache().get(id);
    if (d == null) {
      return null;
    }
    if ((this.cacheSharableBeans) && (!vanilla) && (!Boolean.FALSE.equals(readOnly)))
    {
      Object bean = d.getSharableBean();
      if (bean != null) {
        return (T)bean;
      }
    }
    T bean = createBean(vanilla);
    convertSetId(id, bean);
    if ((!vanilla) && (Boolean.TRUE.equals(readOnly))) {
      ((EntityBean)bean)._ebean_getIntercept().setReadOnly(true);
    }
    CachedBeanDataToBean.load(this, bean, d);
    return bean;
  }
  
  public boolean cacheIsNaturalKey(String propName)
  {
    return (propName != null) && (propName.equals(this.cacheOptions.getNaturalKey()));
  }
  
  public Object cacheGetNaturalKeyId(Object uniqueKeyValue)
  {
    if (this.naturalKeyCache != null) {
      return this.naturalKeyCache.get(uniqueKeyValue);
    }
    return null;
  }
  
  public void cacheRemove(Object id)
  {
    if (this.beanCache != null) {
      this.beanCache.remove(id);
    }
    for (int i = 0; i < this.propertiesOneImported.length; i++) {
      this.propertiesOneImported[i].cacheClear();
    }
  }
  
  public void cacheDelete(Object id, PersistRequestBean<T> deleteRequest)
  {
    if (this.beanCache != null) {
      this.beanCache.remove(id);
    }
    for (int i = 0; i < this.propertiesOneImported.length; i++)
    {
      BeanPropertyAssocMany<?> many = this.propertiesOneImported[i].getRelationshipProperty();
      if (many != null) {
        this.propertiesOneImported[i].cacheDelete(true, deleteRequest);
      }
    }
  }
  
  public void cacheInsert(Object id, PersistRequestBean<T> insertRequest)
  {
    if (this.queryCache != null) {
      this.queryCache.clear();
    }
    for (int i = 0; i < this.propertiesOneImported.length; i++) {
      this.propertiesOneImported[i].cacheDelete(false, insertRequest.getBean());
    }
  }
  
  public void cacheUpdate(Object id, PersistRequestBean<T> updateRequest)
  {
    ServerCache cache = getBeanCache();
    CachedBeanData cd = (CachedBeanData)cache.get(id);
    if (cd != null)
    {
      CachedBeanData newCd = CachedBeanDataUpdate.update(this, cd, updateRequest);
      cache.put(id, newCd);
      if ((newCd.isNaturalKeyUpdate()) && (this.naturalKeyCache != null))
      {
        Object oldKey = this.propertiesNaturalKey.getValue(updateRequest.getOldValues());
        Object newKey = this.propertiesNaturalKey.getValue(updateRequest.getBean());
        if (oldKey != null) {
          this.naturalKeyCache.remove(oldKey);
        }
        if (newKey != null) {
          this.naturalKeyCache.put(newKey, id);
        }
      }
    }
  }
  
  public String getBaseTableAlias()
  {
    return this.baseTableAlias;
  }
  
  public boolean loadFromCache(EntityBeanIntercept ebi)
  {
    Object bean = ebi.getOwner();
    Object id = getId(bean);
    
    return loadFromCache(bean, ebi, id);
  }
  
  public boolean loadFromCache(Object bean, EntityBeanIntercept ebi, Object id)
  {
    CachedBeanData cacheData = (CachedBeanData)getBeanCache().get(id);
    if (cacheData == null) {
      return false;
    }
    String lazyLoadProperty = ebi.getLazyLoadProperty();
    if ((lazyLoadProperty != null) && (!cacheData.containsProperty(lazyLoadProperty))) {
      return false;
    }
    CachedBeanDataToBean.load(this, bean, ebi, cacheData);
    return true;
  }
  
  public void preAllocateIds(int batchSize)
  {
    if (this.idGenerator != null) {
      this.idGenerator.preAllocateIds(batchSize);
    }
  }
  
  public Object nextId(Transaction t)
  {
    if (this.idGenerator != null) {
      return this.idGenerator.nextId(t);
    }
    return null;
  }
  
  public DeployPropertyParser createDeployPropertyParser()
  {
    return new DeployPropertyParser(this);
  }
  
  public String convertOrmUpdateToSql(String ormUpdateStatement)
  {
    return new DeployUpdateParser(this).parse(ormUpdateStatement);
  }
  
  public void clearQueryStatistics()
  {
    Iterator<CQueryPlan> it = this.queryPlanCache.values().iterator();
    while (it.hasNext())
    {
      CQueryPlan queryPlan = (CQueryPlan)it.next();
      queryPlan.resetStatistics();
    }
  }
  
  public void postLoad(Object bean, Set<String> includedProperties)
  {
    BeanPersistController c = this.persistController;
    if (c != null) {
      c.postLoad(bean, includedProperties);
    }
  }
  
  public Iterator<CQueryPlan> queryPlans()
  {
    return this.queryPlanCache.values().iterator();
  }
  
  public CQueryPlan getQueryPlan(Integer key)
  {
    return (CQueryPlan)this.queryPlanCache.get(key);
  }
  
  public void putQueryPlan(Integer key, CQueryPlan plan)
  {
    this.queryPlanCache.put(key, plan);
  }
  
  public SpiUpdatePlan getUpdatePlan(Integer key)
  {
    return (SpiUpdatePlan)this.updatePlanCache.get(key);
  }
  
  public void putUpdatePlan(Integer key, SpiUpdatePlan plan)
  {
    this.updatePlanCache.put(key, plan);
  }
  
  public TypeManager getTypeManager()
  {
    return this.typeManager;
  }
  
  public boolean isUpdateChangesOnly()
  {
    return this.updateChangesOnly;
  }
  
  public boolean isSaveRecurseSkippable()
  {
    return this.saveRecurseSkippable;
  }
  
  public boolean isDeleteRecurseSkippable()
  {
    return this.deleteRecurseSkippable;
  }
  
  public boolean hasLocalValidation()
  {
    return this.hasLocalValidation;
  }
  
  public boolean hasCascadeValidation()
  {
    return this.hasCascadeValidation;
  }
  
  public InvalidValue validate(boolean cascade, Object bean)
  {
    if (!this.hasCascadeValidation) {
      return null;
    }
    List<InvalidValue> errList = null;
    
    Set<String> loadedProps = null;
    if ((bean instanceof EntityBean))
    {
      EntityBeanIntercept ebi = ((EntityBean)bean)._ebean_getIntercept();
      loadedProps = ebi.getLoadedProps();
    }
    if (loadedProps != null)
    {
      Iterator<String> propIt = loadedProps.iterator();
      while (propIt.hasNext())
      {
        String propName = (String)propIt.next();
        BeanProperty property = getBeanProperty(propName);
        if ((property != null) && (property.hasValidationRules(cascade)))
        {
          Object value = property.getValue(bean);
          List<InvalidValue> errs = property.validate(cascade, value);
          if (errs != null)
          {
            if (errList == null) {
              errList = new ArrayList();
            }
            errList.addAll(errs);
          }
        }
      }
    }
    else
    {
      BeanProperty[] props = cascade ? this.propertiesValidationCascade : this.propertiesValidationLocal;
      for (int i = 0; i < props.length; i++)
      {
        BeanProperty prop = props[i];
        Object value = prop.getValue(bean);
        List<InvalidValue> errs = prop.validate(cascade, value);
        if (errs != null)
        {
          if (errList == null) {
            errList = new ArrayList();
          }
          errList.addAll(errs);
        }
      }
    }
    for (int i = 0; i < this.beanValidators.length; i++) {
      if (!this.beanValidators[i].isValid(bean))
      {
        if (errList == null) {
          errList = new ArrayList();
        }
        Validator v = this.beanValidators[i];
        errList.add(new InvalidValue(v.getKey(), v.getAttributes(), getFullName(), null, bean));
      }
    }
    if (errList == null) {
      return null;
    }
    return new InvalidValue(null, getFullName(), bean, InvalidValue.toArray(errList));
  }
  
  public BeanPropertyAssocMany<?> getManyProperty(SpiQuery<?> query)
  {
    OrmQueryDetail detail = query.getDetail();
    for (int i = 0; i < this.propertiesMany.length; i++) {
      if (detail.includes(this.propertiesMany[i].getName())) {
        return this.propertiesMany[i];
      }
    }
    return null;
  }
  
  public IdBinder getIdBinder()
  {
    return this.idBinder;
  }
  
  public String getIdBinderIdSql()
  {
    return this.idBinderIdSql;
  }
  
  public String getIdBinderInLHSSql()
  {
    return this.idBinderInLHSSql;
  }
  
  public void bindId(DataBind dataBind, Object idValue)
    throws SQLException
  {
    this.idBinder.bindId(dataBind, idValue);
  }
  
  public Object[] getBindIdValues(Object idValue)
  {
    return this.idBinder.getBindValues(idValue);
  }
  
  public DeployNamedQuery getNamedQuery(String name)
  {
    return (DeployNamedQuery)this.namedQueries.get(name);
  }
  
  public DeployNamedQuery addNamedQuery(DeployNamedQuery deployNamedQuery)
  {
    return (DeployNamedQuery)this.namedQueries.put(deployNamedQuery.getName(), deployNamedQuery);
  }
  
  public DeployNamedUpdate getNamedUpdate(String name)
  {
    return (DeployNamedUpdate)this.namedUpdates.get(name);
  }
  
  public Object createBean(boolean vanillaMode)
  {
    return vanillaMode ? createVanillaBean() : createEntityBean();
  }
  
  public Object createVanillaBean()
  {
    return this.beanReflect.createVanillaBean();
  }
  
  public EntityBean createEntityBean()
  {
    try
    {
      return (EntityBean)this.beanReflect.createEntityBean();
    }
    catch (Exception ex)
    {
      throw new PersistenceException(ex);
    }
  }
  
  public T createReference(boolean vanillaMode, Boolean readOnly, Object id, Object parent)
  {
    if ((this.cacheSharableBeans) && (!vanillaMode) && (!Boolean.FALSE.equals(readOnly)))
    {
      CachedBeanData d = (CachedBeanData)getBeanCache().get(id);
      if (d != null)
      {
        Object shareableBean = d.getSharableBean();
        if (shareableBean != null) {
          return (T)shareableBean;
        }
      }
    }
    try
    {
      Object bean = createBean(vanillaMode);
      
      convertSetId(id, bean);
      if (!vanillaMode)
      {
        EntityBean eb = (EntityBean)bean;
        
        EntityBeanIntercept ebi = eb._ebean_getIntercept();
        ebi.setBeanLoaderByServerName(this.ebeanServer.getName());
        if (parent != null) {
          ebi.setParentBean(parent);
        }
        ebi.setReference();
      }
      return (T)bean;
    }
    catch (Exception ex)
    {
      throw new PersistenceException(ex);
    }
  }
  
  public BeanProperty getBeanPropertyFromDbColumn(String dbColumn)
  {
    return (BeanProperty)this.propMapByDbColumn.get(dbColumn);
  }
  
  public BeanProperty getBeanPropertyFromPath(String path)
  {
    String[] split = SplitName.splitBegin(path);
    if (split[1] == null) {
      return _findBeanProperty(split[0]);
    }
    BeanPropertyAssoc<?> assocProp = (BeanPropertyAssoc)_findBeanProperty(split[0]);
    BeanDescriptor<?> targetDesc = assocProp.getTargetDescriptor();
    
    return targetDesc.getBeanPropertyFromPath(split[1]);
  }
  
  public BeanDescriptor<?> getBeanDescriptor(String path)
  {
    if (path == null) {
      return this;
    }
    String[] splitBegin = SplitName.splitBegin(path);
    
    BeanProperty beanProperty = (BeanProperty)this.propMap.get(splitBegin[0]);
    if ((beanProperty instanceof BeanPropertyAssoc))
    {
      BeanPropertyAssoc<?> assocProp = (BeanPropertyAssoc)beanProperty;
      return assocProp.getTargetDescriptor().getBeanDescriptor(splitBegin[1]);
    }
    throw new RuntimeException("Error getting BeanDescriptor for path " + path + " from " + getFullName());
  }
  
  public <U> BeanDescriptor<U> getBeanDescriptor(Class<U> otherType)
  {
    return this.owner.getBeanDescriptor(otherType);
  }
  
  public BeanPropertyAssocOne<?> getUnidirectional()
  {
    if (this.unidirectional != null) {
      return this.unidirectional;
    }
    if ((this.inheritInfo != null) && (!this.inheritInfo.isRoot())) {
      return this.inheritInfo.getParent().getBeanDescriptor().getUnidirectional();
    }
    return null;
  }
  
  public Object getValue(Object bean, String property)
  {
    return getBeanProperty(property).getValue(bean);
  }
  
  public boolean isUseIdGenerator()
  {
    return this.idGenerator != null;
  }
  
  public String getDescriptorId()
  {
    return this.descriptorId;
  }
  
  public Class<T> getBeanType()
  {
    return this.beanType;
  }
  
  public Class<?> getFactoryType()
  {
    return this.factoryType;
  }
  
  public String getFullName()
  {
    return this.fullName;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String toString()
  {
    return this.fullName;
  }
  
  public Object getId(Object bean)
  {
    if (this.propertySingleId != null)
    {
      if ((this.inheritInfo != null) && (!this.enhancedBean)) {
        return this.propertySingleId.getValueViaReflection(bean);
      }
      return this.propertySingleId.getValue(bean);
    }
    LinkedHashMap<String, Object> idMap = new LinkedHashMap();
    for (int i = 0; i < this.propertiesId.length; i++)
    {
      Object value = this.propertiesId[i].getValue(bean);
      idMap.put(this.propertiesId[i].getName(), value);
    }
    return idMap;
  }
  
  public boolean isComplexId()
  {
    return this.idBinder.isComplexId();
  }
  
  public String getDefaultOrderBy()
  {
    return this.idBinder.getDefaultOrderBy();
  }
  
  public Object convertId(Object idValue)
  {
    return this.idBinder.convertSetId(idValue, null);
  }
  
  public Object convertSetId(Object idValue, Object bean)
  {
    return this.idBinder.convertSetId(idValue, bean);
  }
  
  public BeanProperty getBeanProperty(String propName)
  {
    return (BeanProperty)this.propMap.get(propName);
  }
  
  public void sort(List<T> list, String sortByClause)
  {
    ElComparator<T> comparator = getElComparator(sortByClause);
    Collections.sort(list, comparator);
  }
  
  public ElComparator<T> getElComparator(String propNameOrSortBy)
  {
    ElComparator<T> c = (ElComparator)this.comparatorCache.get(propNameOrSortBy);
    if (c == null)
    {
      c = createComparator(propNameOrSortBy);
      this.comparatorCache.put(propNameOrSortBy, c);
    }
    return c;
  }
  
  public boolean lazyLoadMany(EntityBeanIntercept ebi)
  {
    String lazyLoadProperty = ebi.getLazyLoadProperty();
    BeanProperty lazyLoadBeanProp = getBeanProperty(lazyLoadProperty);
    if ((lazyLoadBeanProp instanceof BeanPropertyAssocMany))
    {
      BeanPropertyAssocMany<?> manyProp = (BeanPropertyAssocMany)lazyLoadBeanProp;
      manyProp.createReference(ebi.getOwner());
      Set<String> loadedProps = ebi.getLoadedProps();
      HashSet<String> newLoadedProps = new HashSet();
      if (loadedProps != null) {
        newLoadedProps.addAll(loadedProps);
      }
      newLoadedProps.add(lazyLoadProperty);
      ebi.setLoadedProps(newLoadedProps);
      ebi.setLoadedLazy();
      return true;
    }
    return false;
  }
  
  private ElComparator<T> createComparator(String sortByClause)
  {
    SortByClause sortBy = SortByClauseParser.parse(sortByClause);
    if (sortBy.size() == 1) {
      return createPropertyComparator((SortByClause.Property)sortBy.getProperties().get(0));
    }
    ElComparator<T>[] comparators = new ElComparator[sortBy.size()];
    
    List<SortByClause.Property> sortProps = sortBy.getProperties();
    for (int i = 0; i < sortProps.size(); i++)
    {
      SortByClause.Property sortProperty = (SortByClause.Property)sortProps.get(i);
      comparators[i] = createPropertyComparator(sortProperty);
    }
    return new ElComparatorCompound(comparators);
  }
  
  private ElComparator<T> createPropertyComparator(SortByClause.Property sortProp)
  {
    ElPropertyValue elGetValue = getElGetValue(sortProp.getName());
    
    Boolean nullsHigh = sortProp.getNullsHigh();
    if (nullsHigh == null) {
      nullsHigh = Boolean.TRUE;
    }
    return new ElComparatorProperty(elGetValue, sortProp.isAscending(), nullsHigh.booleanValue());
  }
  
  public ElPropertyValue getElGetValue(String propName)
  {
    return getElPropertyValue(propName, false);
  }
  
  public ElPropertyDeploy getElPropertyDeploy(String propName)
  {
    ElPropertyDeploy fk = (ElPropertyDeploy)this.fkeyMap.get(propName);
    if (fk != null) {
      return fk;
    }
    return getElPropertyValue(propName, true);
  }
  
  private ElPropertyValue getElPropertyValue(String propName, boolean propertyDeploy)
  {
    ElPropertyValue elGetValue = (ElPropertyValue)this.elGetCache.get(propName);
    if (elGetValue == null)
    {
      elGetValue = buildElGetValue(propName, null, propertyDeploy);
      if (elGetValue == null) {
        return null;
      }
      if ((elGetValue instanceof BeanFkeyProperty)) {
        this.fkeyMap.put(propName, (BeanFkeyProperty)elGetValue);
      } else {
        this.elGetCache.put(propName, elGetValue);
      }
    }
    return elGetValue;
  }
  
  protected ElPropertyValue buildElGetValue(String propName, ElPropertyChainBuilder chain, boolean propertyDeploy)
  {
    if ((propertyDeploy) && (chain != null))
    {
      BeanFkeyProperty fk = (BeanFkeyProperty)this.fkeyMap.get(propName);
      if (fk != null) {
        return fk.create(chain.getExpression());
      }
    }
    int basePos = propName.indexOf('.');
    if (basePos > -1)
    {
      String baseName = propName.substring(0, basePos);
      String remainder = propName.substring(basePos + 1);
      
      BeanProperty assocProp = _findBeanProperty(baseName);
      if (assocProp == null) {
        return null;
      }
      return assocProp.buildElPropertyValue(propName, remainder, chain, propertyDeploy);
    }
    BeanProperty property = _findBeanProperty(propName);
    if (chain == null) {
      return property;
    }
    if (property == null) {
      throw new PersistenceException("No property found for [" + propName + "] in expression " + chain.getExpression());
    }
    if (property.containsMany()) {
      chain.setContainsMany(true);
    }
    return chain.add(property).build();
  }
  
  public BeanProperty findBeanProperty(String propName)
  {
    int basePos = propName.indexOf('.');
    if (basePos > -1)
    {
      String baseName = propName.substring(0, basePos);
      return _findBeanProperty(baseName);
    }
    return _findBeanProperty(propName);
  }
  
  private BeanProperty _findBeanProperty(String propName)
  {
    BeanProperty prop = (BeanProperty)this.propMap.get(propName);
    if ((prop == null) && (this.inheritInfo != null)) {
      return this.inheritInfo.findSubTypeProperty(propName);
    }
    return prop;
  }
  
  protected Object getBeanPropertyWithInheritance(Object bean, String propName)
  {
    BeanDescriptor<?> desc = getBeanDescriptor(bean.getClass());
    BeanProperty beanProperty = desc.findBeanProperty(propName);
    return beanProperty.getValue(bean);
  }
  
  public String getServerName()
  {
    return this.serverName;
  }
  
  public boolean isCacheSharableBeans()
  {
    return this.cacheSharableBeans;
  }
  
  public boolean isAutoFetchTunable()
  {
    return this.autoFetchTunable;
  }
  
  public InheritInfo getInheritInfo()
  {
    return this.inheritInfo;
  }
  
  public boolean isEmbedded()
  {
    return EntityType.EMBEDDED.equals(this.entityType);
  }
  
  public boolean isBaseTableType()
  {
    return EntityType.ORM.equals(this.entityType);
  }
  
  public ConcurrencyMode getConcurrencyMode()
  {
    return this.concurrencyMode;
  }
  
  public String[] getDependantTables()
  {
    return this.dependantTables;
  }
  
  public CompoundUniqueContraint[] getCompoundUniqueConstraints()
  {
    return this.compoundUniqueConstraints;
  }
  
  public BeanPersistListener<T> getPersistListener()
  {
    return this.persistListener;
  }
  
  public BeanFinder<T> getBeanFinder()
  {
    return this.beanFinder;
  }
  
  public BeanQueryAdapter getQueryAdapter()
  {
    return this.queryAdapter;
  }
  
  public void deregister(BeanPersistListener<?> listener)
  {
    BeanPersistListener<T> currListener = this.persistListener;
    if (currListener != null)
    {
      BeanPersistListener<T> deregListener = listener;
      if ((currListener instanceof ChainedBeanPersistListener)) {
        this.persistListener = ((ChainedBeanPersistListener)currListener).deregister(deregListener);
      } else if (currListener.equals(deregListener)) {
        this.persistListener = null;
      }
    }
  }
  
  public void deregister(BeanPersistController controller)
  {
    BeanPersistController c = this.persistController;
    if (c != null) {
      if ((c instanceof ChainedBeanPersistController)) {
        this.persistController = ((ChainedBeanPersistController)c).deregister(controller);
      } else if (c.equals(controller)) {
        this.persistController = null;
      }
    }
  }
  
  public void register(BeanPersistListener<?> newPersistListener)
  {
    if (PersistListenerManager.isRegisterFor(this.beanType, newPersistListener))
    {
      BeanPersistListener<T> newListener = newPersistListener;
      
      BeanPersistListener<T> currListener = this.persistListener;
      if (currListener == null) {
        this.persistListener = newListener;
      } else if ((currListener instanceof ChainedBeanPersistListener)) {
        this.persistListener = ((ChainedBeanPersistListener)currListener).register(newListener);
      } else {
        this.persistListener = new ChainedBeanPersistListener(currListener, newListener);
      }
    }
  }
  
  public void register(BeanPersistController newController)
  {
    if (newController.isRegisterFor(this.beanType))
    {
      BeanPersistController c = this.persistController;
      if (c == null) {
        this.persistController = newController;
      } else if ((c instanceof ChainedBeanPersistController)) {
        this.persistController = ((ChainedBeanPersistController)c).register(newController);
      } else {
        this.persistController = new ChainedBeanPersistController(c, newController);
      }
    }
  }
  
  public BeanPersistController getPersistController()
  {
    return this.persistController;
  }
  
  public boolean isSqlSelectBased()
  {
    return EntityType.SQL.equals(this.entityType);
  }
  
  public boolean isLdapEntityType()
  {
    return EntityType.LDAP.equals(this.entityType);
  }
  
  public String getBaseTable()
  {
    return this.baseTable;
  }
  
  public String getExtraAttribute(String key)
  {
    return (String)this.extraAttrMap.get(key);
  }
  
  public IdType getIdType()
  {
    return this.idType;
  }
  
  public String getSequenceName()
  {
    return this.sequenceName;
  }
  
  public String getSelectLastInsertedId()
  {
    return this.selectLastInsertedId;
  }
  
  public IdGenerator getIdGenerator()
  {
    return this.idGenerator;
  }
  
  public String getLazyFetchIncludes()
  {
    return this.lazyFetchIncludes;
  }
  
  public TableJoin[] tableJoins()
  {
    return this.derivedTableJoins;
  }
  
  public Iterator<BeanProperty> propertiesAll()
  {
    return this.propMap.values().iterator();
  }
  
  public BeanProperty[] propertiesId()
  {
    return this.propertiesId;
  }
  
  public BeanProperty[] propertiesNonTransient()
  {
    return this.propertiesNonTransient;
  }
  
  public BeanProperty[] propertiesTransient()
  {
    return this.propertiesTransient;
  }
  
  public BeanProperty getSingleIdProperty()
  {
    return this.propertySingleId;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesEmbedded()
  {
    return this.propertiesEmbedded;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesOne()
  {
    return this.propertiesOne;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesOneImported()
  {
    return this.propertiesOneImported;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesOneImportedSave()
  {
    return this.propertiesOneImportedSave;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesOneImportedDelete()
  {
    return this.propertiesOneImportedDelete;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesOneExported()
  {
    return this.propertiesOneExported;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesOneExportedSave()
  {
    return this.propertiesOneExportedSave;
  }
  
  public BeanPropertyAssocOne<?>[] propertiesOneExportedDelete()
  {
    return this.propertiesOneExportedDelete;
  }
  
  private Set<String> deriveManyPropNames()
  {
    LinkedHashSet<String> names = new LinkedHashSet();
    for (int i = 0; i < this.propertiesMany.length; i++) {
      names.add(this.propertiesMany[i].getName());
    }
    return Collections.unmodifiableSet(names);
  }
  
  public int getNamesOfManyPropsHash()
  {
    return this.namesOfManyPropsHash;
  }
  
  public Set<String> getNamesOfManyProps()
  {
    return this.namesOfManyProps;
  }
  
  public BeanProperty[] propertiesNonMany()
  {
    return this.propertiesNonMany;
  }
  
  public BeanPropertyAssocMany<?>[] propertiesMany()
  {
    return this.propertiesMany;
  }
  
  public BeanPropertyAssocMany<?>[] propertiesManySave()
  {
    return this.propertiesManySave;
  }
  
  public BeanPropertyAssocMany<?>[] propertiesManyDelete()
  {
    return this.propertiesManyDelete;
  }
  
  public BeanPropertyAssocMany<?>[] propertiesManyToMany()
  {
    return this.propertiesManyToMany;
  }
  
  public BeanProperty firstVersionProperty()
  {
    return this.propertyFirstVersion;
  }
  
  public boolean isVanillaInsert(Object bean)
  {
    if (this.propertyFirstVersion == null) {
      return true;
    }
    Object versionValue = this.propertyFirstVersion.getValue(bean);
    return DmlUtil.isNullOrZero(versionValue);
  }
  
  public boolean isStatelessUpdate(Object bean)
  {
    if (this.propertyFirstVersion == null)
    {
      Object versionValue = getId(bean);
      return !DmlUtil.isNullOrZero(versionValue);
    }
    Object versionValue = this.propertyFirstVersion.getValue(bean);
    return !DmlUtil.isNullOrZero(versionValue);
  }
  
  public BeanProperty[] propertiesVersion()
  {
    return this.propertiesVersion;
  }
  
  public BeanProperty[] propertiesBaseScalar()
  {
    return this.propertiesBaseScalar;
  }
  
  public BeanPropertyCompound[] propertiesBaseCompound()
  {
    return this.propertiesBaseCompound;
  }
  
  public BeanProperty[] propertiesLocal()
  {
    return this.propertiesLocal;
  }
  
  public void jsonWrite(WriteJsonContext ctx, Object bean)
  {
    if (bean != null)
    {
      ctx.appendObjectBegin();
      WriteJsonContext.WriteBeanState prevState = ctx.pushBeanState(bean);
      if (this.inheritInfo != null)
      {
        InheritInfo localInheritInfo = this.inheritInfo.readType(bean.getClass());
        String discValue = localInheritInfo.getDiscriminatorStringValue();
        String discColumn = localInheritInfo.getDiscriminatorColumn();
        ctx.appendDiscriminator(discColumn, discValue);
        
        BeanDescriptor<?> localDescriptor = localInheritInfo.getBeanDescriptor();
        localDescriptor.jsonWriteProperties(ctx, bean);
      }
      else
      {
        jsonWriteProperties(ctx, bean);
      }
      ctx.pushPreviousState(prevState);
      ctx.appendObjectEnd();
    }
  }
  
  private void jsonWriteProperties(WriteJsonContext ctx, Object bean)
  {
    boolean referenceBean = ctx.isReferenceBean();
    
    JsonWriteBeanVisitor<T> beanVisitor = ctx.getBeanVisitor();
    
    Set<String> props = ctx.getIncludeProperties();
    boolean explicitAllProps;
    boolean explicitAllProps;
    if (props == null)
    {
      explicitAllProps = false;
    }
    else
    {
      explicitAllProps = props.contains("*");
      if ((explicitAllProps) || (props.isEmpty())) {
        props = null;
      }
    }
    for (int i = 0; i < this.propertiesId.length; i++)
    {
      Object idValue = this.propertiesId[i].getValue(bean);
      if ((idValue != null) && (
        (props == null) || (props.contains(this.propertiesId[i].getName())))) {
        this.propertiesId[i].jsonWrite(ctx, bean);
      }
    }
    if ((!explicitAllProps) && (props == null)) {
      props = ctx.getLoadedProps();
    }
    if (props != null) {
      for (String prop : props)
      {
        BeanProperty p = getBeanProperty(prop);
        if ((p != null) && (!p.isId())) {
          p.jsonWrite(ctx, bean);
        }
      }
    } else if ((explicitAllProps) || (!referenceBean)) {
      for (int j = 0; j < this.propertiesNonTransient.length; j++) {
        this.propertiesNonTransient[j].jsonWrite(ctx, bean);
      }
    }
    if (beanVisitor != null) {
      beanVisitor.visit(bean, ctx);
    }
  }
  
  public T jsonReadBean(ReadJsonContext ctx, String path)
  {
    ReadJsonContext.ReadBeanState beanState = jsonRead(ctx, path);
    if (beanState == null) {
      return null;
    }
    beanState.setLoadedState();
    return (T)beanState.getBean();
  }
  
  public ReadJsonContext.ReadBeanState jsonRead(ReadJsonContext ctx, String path)
  {
    if (!ctx.readObjectBegin()) {
      return null;
    }
    if (this.inheritInfo == null) {
      return jsonReadObject(ctx, path);
    }
    String discColumn = this.inheritInfo.getRoot().getDiscriminatorColumn();
    if (!ctx.readKeyNext())
    {
      String msg = "Error reading inheritance discriminator - expected [" + discColumn + "] but no json key?";
      throw new TextException(msg);
    }
    String propName = ctx.getTokenKey();
    if (!propName.equalsIgnoreCase(discColumn))
    {
      String msg = "Error reading inheritance discriminator - expected [" + discColumn + "] but read [" + propName + "]";
      throw new TextException(msg);
    }
    String discValue = ctx.readScalarValue();
    if (!ctx.readValueNext())
    {
      String msg = "Error reading inheritance discriminator [" + discColumn + "]. Expected more json name values?";
      throw new TextException(msg);
    }
    InheritInfo localInheritInfo = this.inheritInfo.readType(discValue);
    BeanDescriptor<?> localDescriptor = localInheritInfo.getBeanDescriptor();
    return localDescriptor.jsonReadObject(ctx, path);
  }
  
  private ReadJsonContext.ReadBeanState jsonReadObject(ReadJsonContext ctx, String path)
  {
    T bean = createEntityBean();
    ctx.pushBean(bean, path, this);
    while (ctx.readKeyNext())
    {
      String propName = ctx.getTokenKey();
      BeanProperty p = getBeanProperty(propName);
      if (p != null)
      {
        p.jsonRead(ctx, bean);
        ctx.setProperty(propName);
      }
      else
      {
        ctx.readUnmappedJson(propName);
      }
      if (!ctx.readValueNext()) {
        break;
      }
    }
    return ctx.popBeanState();
  }
  
  public void setLoadedProps(EntityBeanIntercept ebi, Set<String> loadedProps)
  {
    if (isLoadedReference(loadedProps)) {
      ebi.setReference();
    } else {
      ebi.setLoadedProps(loadedProps);
    }
  }
  
  public boolean isLoadedReference(Set<String> loadedProps)
  {
    if ((loadedProps != null) && 
      (loadedProps.size() == this.propertiesId.length))
    {
      for (int i = 0; i < this.propertiesId.length; i++) {
        if (!loadedProps.contains(this.propertiesId[i].getName())) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
}
