package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebean.CallableSql;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.Update;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollection.ModifyListenMode;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.config.ldap.LdapContextFactory;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.SpiUpdate;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.server.core.ConcurrencyMode;
import com.avaje.ebeaninternal.server.core.Message;
import com.avaje.ebeaninternal.server.core.PersistRequest.Type;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.core.PersistRequestCallableSql;
import com.avaje.ebeaninternal.server.core.PersistRequestOrmUpdate;
import com.avaje.ebeaninternal.server.core.PersistRequestUpdateSql;
import com.avaje.ebeaninternal.server.core.Persister;
import com.avaje.ebeaninternal.server.core.PstmtBatch;
import com.avaje.ebeaninternal.server.deploy.BeanCascadeInfo;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import com.avaje.ebeaninternal.server.deploy.BeanManager;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.deploy.IntersectionRow;
import com.avaje.ebeaninternal.server.deploy.ManyType;
import com.avaje.ebeaninternal.server.ldap.DefaultLdapPersister;
import com.avaje.ebeaninternal.server.ldap.LdapPersistBeanRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public final class DefaultPersister
  implements Persister
{
  private static final Logger logger = Logger.getLogger(DefaultPersister.class.getName());
  private final PersistExecute persistExecute;
  private final DefaultLdapPersister ldapPersister;
  private final SpiEbeanServer server;
  private final BeanDescriptorManager beanDescriptorManager;
  private final boolean defaultUpdateNullProperties;
  private final boolean defaultDeleteMissingChildren;
  
  public DefaultPersister(SpiEbeanServer server, boolean validate, Binder binder, BeanDescriptorManager descMgr, PstmtBatch pstmtBatch, LdapContextFactory contextFactory)
  {
    this.server = server;
    this.beanDescriptorManager = descMgr;
    
    this.persistExecute = new DefaultPersistExecute(validate, binder, pstmtBatch);
    this.ldapPersister = new DefaultLdapPersister(contextFactory);
    
    this.defaultUpdateNullProperties = server.isDefaultUpdateNullProperties();
    this.defaultDeleteMissingChildren = server.isDefaultDeleteMissingChildren();
  }
  
  public int executeCallable(CallableSql callSql, Transaction t)
  {
    PersistRequestCallableSql request = new PersistRequestCallableSql(this.server, callSql, (SpiTransaction)t, this.persistExecute);
    try
    {
      request.initTransIfRequired();
      int rc = request.executeOrQueue();
      request.commitTransIfRequired();
      return rc;
    }
    catch (RuntimeException e)
    {
      request.rollbackTransIfRequired();
      throw e;
    }
  }
  
  public int executeOrmUpdate(Update<?> update, Transaction t)
  {
    SpiUpdate<?> ormUpdate = (SpiUpdate)update;
    
    BeanManager<?> mgr = this.beanDescriptorManager.getBeanManager(ormUpdate.getBeanType());
    if (mgr == null)
    {
      String msg = "No BeanManager found for type [" + ormUpdate.getBeanType() + "]. Is it an entity?";
      throw new PersistenceException(msg);
    }
    PersistRequestOrmUpdate request = new PersistRequestOrmUpdate(this.server, mgr, ormUpdate, (SpiTransaction)t, this.persistExecute);
    try
    {
      request.initTransIfRequired();
      int rc = request.executeOrQueue();
      request.commitTransIfRequired();
      return rc;
    }
    catch (RuntimeException e)
    {
      request.rollbackTransIfRequired();
      throw e;
    }
  }
  
  public int executeSqlUpdate(SqlUpdate updSql, Transaction t)
  {
    PersistRequestUpdateSql request = new PersistRequestUpdateSql(this.server, updSql, (SpiTransaction)t, this.persistExecute);
    try
    {
      request.initTransIfRequired();
      int rc = request.executeOrQueue();
      request.commitTransIfRequired();
      return rc;
    }
    catch (RuntimeException e)
    {
      request.rollbackTransIfRequired();
      throw e;
    }
  }
  
  private void deleteRecurse(Object detailBean, Transaction t)
  {
    this.server.delete(detailBean, t);
  }
  
  public void forceUpdate(Object bean, Set<String> updateProps, Transaction t, boolean deleteMissingChildren, boolean updateNullProperties)
  {
    if (bean == null) {
      throw new NullPointerException(Message.msg("bean.isnull"));
    }
    if (updateProps == null) {
      if ((bean instanceof EntityBean))
      {
        EntityBeanIntercept ebi = ((EntityBean)bean)._ebean_getIntercept();
        if ((ebi.isDirty()) || (ebi.isLoaded()))
        {
          PersistRequestBean<?> req = createRequest(bean, t, null);
          try
          {
            req.initTransIfRequired();
            update(req);
            req.commitTransIfRequired();
            
            return;
          }
          catch (RuntimeException ex)
          {
            req.rollbackTransIfRequired();
            throw ex;
          }
        }
        if (ebi.isReference()) {
          return;
        }
        updateProps = ebi.getLoadedProps();
      }
    }
    BeanManager<?> mgr = getBeanManager(bean);
    if (mgr == null) {
      throw new PersistenceException(errNotRegistered(bean.getClass()));
    }
    forceUpdateStateless(bean, t, null, mgr, updateProps, deleteMissingChildren, updateNullProperties);
  }
  
  private void forceUpdateStateless(Object bean, Transaction t, Object parentBean, BeanManager<?> mgr, Set<String> updateProps, boolean deleteMissingChildren, boolean updateNullProperties)
  {
    BeanDescriptor<?> descriptor = mgr.getBeanDescriptor();
    
    ConcurrencyMode mode = descriptor.determineConcurrencyMode(bean);
    if (updateProps == null)
    {
      updateProps = updateNullProperties ? null : descriptor.determineLoadedProperties(bean);
    }
    else if (updateProps.isEmpty())
    {
      updateProps = null;
    }
    else if (ConcurrencyMode.VERSION.equals(mode))
    {
      String verName = descriptor.firstVersionProperty().getName();
      if (!updateProps.contains(verName))
      {
        updateProps = new HashSet(updateProps);
        updateProps.add(verName);
      }
    }
    PersistRequestBean<?> req;
    PersistRequestBean<?> req;
    if (descriptor.isLdapEntityType())
    {
      req = new LdapPersistBeanRequest(this.server, bean, parentBean, mgr, this.ldapPersister, updateProps, mode);
    }
    else
    {
      req = new PersistRequestBean(this.server, bean, parentBean, mgr, (SpiTransaction)t, this.persistExecute, updateProps, mode);
      req.setStatelessUpdate(true, deleteMissingChildren, updateNullProperties);
    }
    try
    {
      req.initTransIfRequired();
      update(req);
      req.commitTransIfRequired();
    }
    catch (RuntimeException ex)
    {
      req.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public void save(Object bean, Transaction t)
  {
    saveRecurse(bean, t, null);
  }
  
  public void forceInsert(Object bean, Transaction t)
  {
    PersistRequestBean<?> req = createRequest(bean, t, null);
    try
    {
      req.initTransIfRequired();
      insert(req);
      req.commitTransIfRequired();
    }
    catch (RuntimeException ex)
    {
      req.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  private void saveRecurse(Object bean, Transaction t, Object parentBean)
  {
    if (bean == null) {
      throw new NullPointerException(Message.msg("bean.isnull"));
    }
    if (!(bean instanceof EntityBean))
    {
      saveVanillaRecurse(bean, t, parentBean);
      return;
    }
    PersistRequestBean<?> req = createRequest(bean, t, parentBean);
    try
    {
      req.initTransIfRequired();
      saveEnhanced(req);
      req.commitTransIfRequired();
    }
    catch (RuntimeException ex)
    {
      req.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  private void saveEnhanced(PersistRequestBean<?> request)
  {
    EntityBeanIntercept intercept = request.getEntityBeanIntercept();
    if (intercept.isReference())
    {
      if (request.isPersistCascade())
      {
        intercept.setLoaded();
        saveAssocMany(false, request);
        intercept.setReference();
      }
    }
    else if (intercept.isLoaded()) {
      update(request);
    } else {
      insert(request);
    }
  }
  
  private void saveVanillaRecurse(Object bean, Transaction t, Object parentBean)
  {
    BeanManager<?> mgr = getBeanManager(bean);
    if (mgr == null) {
      throw new RuntimeException("No Mgr found for " + bean + " " + bean.getClass());
    }
    if (mgr.getBeanDescriptor().isVanillaInsert(bean)) {
      saveVanillaInsert(bean, t, parentBean, mgr);
    } else {
      forceUpdateStateless(bean, t, parentBean, mgr, null, this.defaultDeleteMissingChildren, this.defaultUpdateNullProperties);
    }
  }
  
  private void saveVanillaInsert(Object bean, Transaction t, Object parentBean, BeanManager<?> mgr)
  {
    PersistRequestBean<?> req = createRequest(bean, t, parentBean, mgr);
    try
    {
      req.initTransIfRequired();
      insert(req);
      req.commitTransIfRequired();
    }
    catch (RuntimeException ex)
    {
      req.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  private void insert(PersistRequestBean<?> request)
  {
    if (request.isRegisteredBean()) {
      return;
    }
    try
    {
      request.setType(PersistRequest.Type.INSERT);
      if (request.isPersistCascade()) {
        saveAssocOne(request);
      }
      setIdGenValue(request);
      request.executeOrQueue();
      if (request.isPersistCascade()) {
        saveAssocMany(true, request);
      }
    }
    finally
    {
      request.unRegisterBean();
    }
  }
  
  private void update(PersistRequestBean<?> request)
  {
    if (request.isRegisteredBean()) {
      return;
    }
    try
    {
      request.setType(PersistRequest.Type.UPDATE);
      if (request.isPersistCascade()) {
        saveAssocOne(request);
      }
      if (request.isDirty()) {
        request.executeOrQueue();
      } else if (logger.isLoggable(Level.FINE)) {
        logger.fine(Message.msg("persist.update.skipped", request.getBean()));
      }
      if (request.isPersistCascade()) {
        saveAssocMany(false, request);
      }
    }
    finally
    {
      request.unRegisterBean();
    }
  }
  
  public void delete(Object bean, Transaction t)
  {
    PersistRequestBean<?> req = createRequest(bean, t, null);
    if (req.isRegisteredForDeleteBean())
    {
      if (logger.isLoggable(Level.FINE)) {
        logger.fine("skipping delete on alreadyRegistered " + bean);
      }
      return;
    }
    req.setType(PersistRequest.Type.DELETE);
    try
    {
      req.initTransIfRequired();
      delete(req);
      req.commitTransIfRequired();
    }
    catch (RuntimeException ex)
    {
      req.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  private void deleteList(List<?> beanList, Transaction t)
  {
    for (int i = 0; i < beanList.size(); i++)
    {
      Object bean = beanList.get(i);
      delete(bean, t);
    }
  }
  
  public void deleteMany(Class<?> beanType, Collection<?> ids, Transaction transaction)
  {
    if ((ids == null) || (ids.size() == 0)) {
      return;
    }
    BeanDescriptor<?> descriptor = this.beanDescriptorManager.getBeanDescriptor(beanType);
    
    ArrayList<Object> idList = new ArrayList(ids.size());
    for (Object id : ids) {
      idList.add(descriptor.convertId(id));
    }
    delete(descriptor, null, idList, transaction);
  }
  
  public int delete(Class<?> beanType, Object id, Transaction transaction)
  {
    BeanDescriptor<?> descriptor = this.beanDescriptorManager.getBeanDescriptor(beanType);
    
    id = descriptor.convertId(id);
    return delete(descriptor, id, null, transaction);
  }
  
  private int delete(BeanDescriptor<?> descriptor, Object id, List<Object> idList, Transaction transaction)
  {
    SpiTransaction t = (SpiTransaction)transaction;
    if (t.isPersistCascade())
    {
      BeanPropertyAssocOne<?>[] propImportDelete = descriptor.propertiesOneImportedDelete();
      if (propImportDelete.length > 0)
      {
        Query<?> q = deleteRequiresQuery(descriptor, propImportDelete);
        if (idList != null)
        {
          q.where().idIn(idList);
          if (t.isLogSummary()) {
            t.logInternal("-- DeleteById of " + descriptor.getName() + " ids[" + idList + "] requires fetch of foreign key values");
          }
          List<?> beanList = this.server.findList(q, t);
          deleteList(beanList, t);
          return beanList.size();
        }
        q.where().idEq(id);
        if (t.isLogSummary()) {
          t.logInternal("-- DeleteById of " + descriptor.getName() + " id[" + id + "] requires fetch of foreign key values");
        }
        Object bean = this.server.findUnique(q, t);
        if (bean == null) {
          return 0;
        }
        delete(bean, t);
        return 1;
      }
    }
    if (t.isPersistCascade())
    {
      BeanPropertyAssocOne<?>[] expOnes = descriptor.propertiesOneExportedDelete();
      for (int i = 0; i < expOnes.length; i++)
      {
        BeanDescriptor<?> targetDesc = expOnes[i].getTargetDescriptor();
        if ((targetDesc.isDeleteRecurseSkippable()) && (!targetDesc.isUsingL2Cache()))
        {
          SqlUpdate sqlDelete = expOnes[i].deleteByParentId(id, idList);
          executeSqlUpdate(sqlDelete, t);
        }
        else
        {
          List<Object> childIds = expOnes[i].findIdsByParentId(id, idList, t);
          delete(targetDesc, null, childIds, t);
        }
      }
      BeanPropertyAssocMany<?>[] manys = descriptor.propertiesManyDelete();
      for (int i = 0; i < manys.length; i++)
      {
        BeanDescriptor<?> targetDesc = manys[i].getTargetDescriptor();
        if ((targetDesc.isDeleteRecurseSkippable()) && (!targetDesc.isUsingL2Cache()))
        {
          SqlUpdate sqlDelete = manys[i].deleteByParentId(id, idList);
          executeSqlUpdate(sqlDelete, t);
        }
        else
        {
          List<Object> childIds = manys[i].findIdsByParentId(id, idList, t, null);
          delete(targetDesc, null, childIds, t);
        }
      }
    }
    BeanPropertyAssocMany<?>[] manys = descriptor.propertiesManyToMany();
    for (int i = 0; i < manys.length; i++)
    {
      SqlUpdate sqlDelete = manys[i].deleteByParentId(id, idList);
      if (t.isLogSummary()) {
        t.logInternal("-- Deleting intersection table entries: " + manys[i].getFullBeanName());
      }
      executeSqlUpdate(sqlDelete, t);
    }
    SqlUpdate deleteById = descriptor.deleteById(id, idList);
    if (t.isLogSummary()) {
      t.logInternal("-- Deleting " + descriptor.getName() + " Ids" + idList);
    }
    deleteById.setAutoTableMod(false);
    if (idList != null) {
      t.getEvent().addDeleteByIdList(descriptor, idList);
    } else {
      t.getEvent().addDeleteById(descriptor, id);
    }
    return executeSqlUpdate(deleteById, t);
  }
  
  private Query<?> deleteRequiresQuery(BeanDescriptor<?> desc, BeanPropertyAssocOne<?>[] propImportDelete)
  {
    Query<?> q = this.server.createQuery(desc.getBeanType());
    StringBuilder sb = new StringBuilder(30);
    for (int i = 0; i < propImportDelete.length; i++) {
      sb.append(propImportDelete[i].getName()).append(",");
    }
    q.setAutofetch(false);
    q.select(sb.toString());
    return q;
  }
  
  private void delete(PersistRequestBean<?> request)
  {
    DeleteUnloadedForeignKeys unloadedForeignKeys = null;
    if (request.isPersistCascade())
    {
      request.registerDeleteBean();
      deleteAssocMany(request);
      request.unregisterDeleteBean();
      
      unloadedForeignKeys = getDeleteUnloadedForeignKeys(request);
      if (unloadedForeignKeys != null) {
        unloadedForeignKeys.queryForeignKeys();
      }
    }
    request.executeOrQueue();
    if (request.isPersistCascade())
    {
      deleteAssocOne(request);
      if (unloadedForeignKeys != null) {
        unloadedForeignKeys.deleteCascade();
      }
    }
  }
  
  private void saveAssocMany(boolean insertedParent, PersistRequestBean<?> request)
  {
    Object parentBean = request.getBean();
    BeanDescriptor<?> desc = request.getBeanDescriptor();
    SpiTransaction t = request.getTransaction();
    
    BeanPropertyAssocOne<?>[] expOnes = desc.propertiesOneExportedSave();
    for (int i = 0; i < expOnes.length; i++)
    {
      BeanPropertyAssocOne<?> prop = expOnes[i];
      if (request.isLoadedProperty(prop))
      {
        Object detailBean = prop.getValue(parentBean);
        if ((detailBean != null) && 
          (!prop.isSaveRecurseSkippable(detailBean)))
        {
          t.depth(1);
          saveRecurse(detailBean, t, parentBean);
          t.depth(-1);
        }
      }
    }
    BeanPropertyAssocMany<?>[] manys = desc.propertiesManySave();
    for (int i = 0; i < manys.length; i++) {
      saveMany(new SaveManyPropRequest(insertedParent, manys[i], parentBean, request, null));
    }
  }
  
  private static class SaveManyPropRequest
  {
    private final boolean insertedParent;
    private final BeanPropertyAssocMany<?> many;
    private final Object parentBean;
    private final SpiTransaction t;
    private final boolean cascade;
    private final boolean statelessUpdate;
    private final boolean deleteMissingChildren;
    private final boolean updateNullProperties;
    
    private SaveManyPropRequest(boolean insertedParent, BeanPropertyAssocMany<?> many, Object parentBean, PersistRequestBean<?> request)
    {
      this.insertedParent = insertedParent;
      this.many = many;
      this.cascade = many.getCascadeInfo().isSave();
      this.parentBean = parentBean;
      this.t = request.getTransaction();
      this.statelessUpdate = request.isStatelessUpdate();
      this.deleteMissingChildren = request.isDeleteMissingChildren();
      this.updateNullProperties = request.isUpdateNullProperties();
    }
    
    private SaveManyPropRequest(BeanPropertyAssocMany<?> many, Object parentBean, SpiTransaction t)
    {
      this.insertedParent = false;
      this.many = many;
      this.parentBean = parentBean;
      this.t = t;
      this.cascade = true;
      this.statelessUpdate = false;
      this.deleteMissingChildren = false;
      this.updateNullProperties = false;
    }
    
    private Object getValueUnderlying()
    {
      return this.many.getValueUnderlying(this.parentBean);
    }
    
    private boolean isModifyListenMode()
    {
      return BeanCollection.ModifyListenMode.REMOVALS.equals(this.many.getModifyListenMode());
    }
    
    private boolean isStatelessUpdate()
    {
      return this.statelessUpdate;
    }
    
    private boolean isDeleteMissingChildren()
    {
      return this.deleteMissingChildren;
    }
    
    private boolean isUpdateNullProperties()
    {
      return this.updateNullProperties;
    }
    
    private boolean isInsertedParent()
    {
      return this.insertedParent;
    }
    
    private BeanPropertyAssocMany<?> getMany()
    {
      return this.many;
    }
    
    private Object getParentBean()
    {
      return this.parentBean;
    }
    
    private SpiTransaction getTransaction()
    {
      return this.t;
    }
    
    private boolean isCascade()
    {
      return this.cascade;
    }
  }
  
  private void saveMany(SaveManyPropRequest saveMany)
  {
    if (saveMany.getMany().isManyToMany())
    {
      if (saveMany.isCascade())
      {
        saveAssocManyDetails(saveMany, false, saveMany.isUpdateNullProperties());
        
        saveAssocManyIntersection(saveMany, saveMany.isDeleteMissingChildren());
      }
    }
    else
    {
      if (saveMany.isCascade()) {
        saveAssocManyDetails(saveMany, saveMany.isDeleteMissingChildren(), saveMany.isUpdateNullProperties());
      }
      if (saveMany.isModifyListenMode()) {
        removeAssocManyPrivateOwned(saveMany);
      }
    }
  }
  
  private void removeAssocManyPrivateOwned(SaveManyPropRequest saveMany)
  {
    Object details = saveMany.getValueUnderlying();
    if ((details instanceof BeanCollection))
    {
      BeanCollection<?> c = (BeanCollection)details;
      Set<?> modifyRemovals = c.getModifyRemovals();
      if ((modifyRemovals != null) && (!modifyRemovals.isEmpty()))
      {
        SpiTransaction t = saveMany.getTransaction();
        
        t.depth(1);
        for (Object removedBean : modifyRemovals) {
          deleteRecurse(removedBean, t);
        }
        t.depth(-1);
      }
    }
  }
  
  private void saveAssocManyDetails(SaveManyPropRequest saveMany, boolean deleteMissingChildren, boolean updateNullProperties)
  {
    BeanPropertyAssocMany<?> prop = saveMany.getMany();
    
    Object details = saveMany.getValueUnderlying();
    
    Collection<?> collection = getDetailsIterator(details);
    if (collection == null) {
      return;
    }
    if (saveMany.isInsertedParent()) {
      prop.getTargetDescriptor().preAllocateIds(collection.size());
    }
    BeanDescriptor<?> targetDescriptor = prop.getTargetDescriptor();
    ArrayList<Object> detailIds = null;
    if (deleteMissingChildren) {
      detailIds = new ArrayList();
    }
    SpiTransaction t = saveMany.getTransaction();
    t.depth(1);
    
    boolean isMap = ManyType.JAVA_MAP.equals(prop.getManyType());
    Object parentBean = saveMany.getParentBean();
    Object mapKeyValue = null;
    
    boolean saveSkippable = prop.isSaveRecurseSkippable();
    boolean skipSavingThisBean = false;
    for (Object detailBean : collection)
    {
      if (isMap)
      {
        Map.Entry<?, ?> entry = (Map.Entry)detailBean;
        mapKeyValue = entry.getKey();
        detailBean = entry.getValue();
      }
      if (prop.isManyToMany())
      {
        if ((detailBean instanceof EntityBean)) {
          skipSavingThisBean = ((EntityBean)detailBean)._ebean_getIntercept().isReference();
        }
      }
      else if ((detailBean instanceof EntityBean))
      {
        EntityBeanIntercept ebi = ((EntityBean)detailBean)._ebean_getIntercept();
        if (ebi.isNewOrDirty()) {
          prop.setJoinValuesToChild(parentBean, detailBean, mapKeyValue);
        } else if (ebi.isReference()) {
          skipSavingThisBean = true;
        } else {
          skipSavingThisBean = saveSkippable;
        }
      }
      else
      {
        prop.setJoinValuesToChild(parentBean, detailBean, mapKeyValue);
      }
      if (skipSavingThisBean) {
        skipSavingThisBean = false;
      } else if (!saveMany.isStatelessUpdate()) {
        saveRecurse(detailBean, t, parentBean);
      } else if (targetDescriptor.isStatelessUpdate(detailBean)) {
        forceUpdate(detailBean, null, t, deleteMissingChildren, updateNullProperties);
      } else {
        forceInsert(detailBean, t);
      }
      if (detailIds != null)
      {
        Object id = targetDescriptor.getId(detailBean);
        if (!DmlUtil.isNullOrZero(id)) {
          detailIds.add(id);
        }
      }
    }
    if (detailIds != null) {
      deleteManyDetails(t, prop.getBeanDescriptor(), parentBean, prop, detailIds);
    }
    t.depth(-1);
  }
  
  public int deleteManyToManyAssociations(Object ownerBean, String propertyName, Transaction t)
  {
    BeanDescriptor<?> descriptor = this.beanDescriptorManager.getBeanDescriptor(ownerBean.getClass());
    BeanPropertyAssocMany<?> prop = (BeanPropertyAssocMany)descriptor.getBeanProperty(propertyName);
    return deleteAssocManyIntersection(ownerBean, prop, t);
  }
  
  public void saveManyToManyAssociations(Object ownerBean, String propertyName, Transaction t)
  {
    BeanDescriptor<?> descriptor = this.beanDescriptorManager.getBeanDescriptor(ownerBean.getClass());
    BeanPropertyAssocMany<?> prop = (BeanPropertyAssocMany)descriptor.getBeanProperty(propertyName);
    
    saveAssocManyIntersection(new SaveManyPropRequest(prop, ownerBean, (SpiTransaction)t, null), false);
  }
  
  public void saveAssociation(Object parentBean, String propertyName, Transaction t)
  {
    BeanDescriptor<?> descriptor = this.beanDescriptorManager.getBeanDescriptor(parentBean.getClass());
    SpiTransaction trans = (SpiTransaction)t;
    
    BeanProperty prop = descriptor.getBeanProperty(propertyName);
    if (prop == null)
    {
      String msg = "Could not find property [" + propertyName + "] on bean " + parentBean.getClass();
      throw new PersistenceException(msg);
    }
    if ((prop instanceof BeanPropertyAssocMany))
    {
      BeanPropertyAssocMany<?> manyProp = (BeanPropertyAssocMany)prop;
      saveMany(new SaveManyPropRequest(manyProp, parentBean, (SpiTransaction)t, null));
    }
    else if ((prop instanceof BeanPropertyAssocOne))
    {
      BeanPropertyAssocOne<?> oneProp = (BeanPropertyAssocOne)prop;
      Object assocBean = oneProp.getValue(parentBean);
      
      int depth = oneProp.isOneToOneExported() ? 1 : -1;
      int revertDepth = -1 * depth;
      
      trans.depth(depth);
      saveRecurse(assocBean, t, parentBean);
      trans.depth(revertDepth);
    }
    else
    {
      String msg = "Expecting [" + prop.getFullBeanName() + "] to be a OneToMany, OneToOne, ManyToOne or ManyToMany property?";
      throw new PersistenceException(msg);
    }
  }
  
  private void saveAssocManyIntersection(SaveManyPropRequest saveManyPropRequest, boolean deleteMissingChildren)
  {
    BeanPropertyAssocMany<?> prop = saveManyPropRequest.getMany();
    Object value = prop.getValueUnderlying(saveManyPropRequest.getParentBean());
    if (value == null) {
      return;
    }
    SpiTransaction t = saveManyPropRequest.getTransaction();
    Collection<?> additions = null;
    Collection<?> deletions = null;
    
    boolean vanillaCollection = !(value instanceof BeanCollection);
    if ((vanillaCollection) || (deleteMissingChildren)) {
      deleteAssocManyIntersection(saveManyPropRequest.getParentBean(), prop, t);
    }
    if ((saveManyPropRequest.isInsertedParent()) || (vanillaCollection) || (deleteMissingChildren))
    {
      if ((value instanceof Map))
      {
        additions = ((Map)value).values();
      }
      else if ((value instanceof Collection))
      {
        additions = (Collection)value;
      }
      else
      {
        String msg = "Unhandled ManyToMany type " + value.getClass().getName() + " for " + prop.getFullBeanName();
        throw new PersistenceException(msg);
      }
      if (!vanillaCollection) {
        ((BeanCollection)value).modifyReset();
      }
    }
    else
    {
      BeanCollection<?> manyValue = (BeanCollection)value;
      additions = manyValue.getModifyAdditions();
      deletions = manyValue.getModifyRemovals();
      
      manyValue.modifyReset();
    }
    t.depth(1);
    if ((additions != null) && (!additions.isEmpty())) {
      for (Object otherBean : additions) {
        if ((deletions != null) && (deletions.remove(otherBean)))
        {
          String m = "Inserting and Deleting same object? " + otherBean;
          if (t.isLogSummary()) {
            t.logInternal(m);
          }
          logger.log(Level.WARNING, m);
        }
        else
        {
          if (!prop.hasImportedId(otherBean))
          {
            String msg = "ManyToMany bean " + otherBean + " does not have an Id value.";
            throw new PersistenceException(msg);
          }
          IntersectionRow intRow = prop.buildManyToManyMapBean(saveManyPropRequest.getParentBean(), otherBean);
          SqlUpdate sqlInsert = intRow.createInsert(this.server);
          executeSqlUpdate(sqlInsert, t);
        }
      }
    }
    if ((deletions != null) && (!deletions.isEmpty())) {
      for (Object otherDelete : deletions)
      {
        IntersectionRow intRow = prop.buildManyToManyMapBean(saveManyPropRequest.getParentBean(), otherDelete);
        SqlUpdate sqlDelete = intRow.createDelete(this.server);
        executeSqlUpdate(sqlDelete, t);
      }
    }
    t.depth(-1);
  }
  
  private int deleteAssocManyIntersection(Object bean, BeanPropertyAssocMany<?> many, Transaction t)
  {
    IntersectionRow intRow = many.buildManyToManyDeleteChildren(bean);
    SqlUpdate sqlDelete = intRow.createDeleteChildren(this.server);
    
    return executeSqlUpdate(sqlDelete, t);
  }
  
  private void deleteAssocMany(PersistRequestBean<?> request)
  {
    SpiTransaction t = request.getTransaction();
    t.depth(-1);
    
    BeanDescriptor<?> desc = request.getBeanDescriptor();
    Object parentBean = request.getBean();
    
    BeanPropertyAssocOne<?>[] expOnes = desc.propertiesOneExportedDelete();
    if (expOnes.length > 0)
    {
      DeleteUnloadedForeignKeys unloaded = null;
      for (int i = 0; i < expOnes.length; i++)
      {
        BeanPropertyAssocOne<?> prop = expOnes[i];
        if (request.isLoadedProperty(prop))
        {
          Object detailBean = prop.getValue(parentBean);
          if (detailBean != null) {
            deleteRecurse(detailBean, t);
          }
        }
        else
        {
          if (unloaded == null) {
            unloaded = new DeleteUnloadedForeignKeys(this.server, request);
          }
          unloaded.add(prop);
        }
      }
      if (unloaded != null)
      {
        unloaded.queryForeignKeys();
        unloaded.deleteCascade();
      }
    }
    BeanPropertyAssocMany<?>[] manys = desc.propertiesManyDelete();
    for (int i = 0; i < manys.length; i++) {
      if (manys[i].isManyToMany())
      {
        deleteAssocManyIntersection(parentBean, manys[i], t);
      }
      else
      {
        if (BeanCollection.ModifyListenMode.REMOVALS.equals(manys[i].getModifyListenMode()))
        {
          Object details = manys[i].getValueUnderlying(parentBean);
          if ((details instanceof BeanCollection))
          {
            Set<?> modifyRemovals = ((BeanCollection)details).getModifyRemovals();
            if ((modifyRemovals != null) && (!modifyRemovals.isEmpty())) {
              for (Object detailBean : modifyRemovals) {
                if (manys[i].hasId(detailBean)) {
                  deleteRecurse(detailBean, t);
                }
              }
            }
          }
        }
        deleteManyDetails(t, desc, parentBean, manys[i], null);
      }
    }
    t.depth(1);
  }
  
  private void deleteManyDetails(SpiTransaction t, BeanDescriptor<?> desc, Object parentBean, BeanPropertyAssocMany<?> many, ArrayList<Object> excludeDetailIds)
  {
    if (many.getCascadeInfo().isDelete())
    {
      BeanDescriptor<?> targetDesc = many.getTargetDescriptor();
      if ((targetDesc.isDeleteRecurseSkippable()) && (!targetDesc.isUsingL2Cache()))
      {
        IntersectionRow intRow = many.buildManyDeleteChildren(parentBean, excludeDetailIds);
        SqlUpdate sqlDelete = intRow.createDelete(this.server);
        executeSqlUpdate(sqlDelete, t);
      }
      else
      {
        Object parentId = desc.getId(parentBean);
        List<Object> idsByParentId = many.findIdsByParentId(parentId, null, t, excludeDetailIds);
        if (!idsByParentId.isEmpty()) {
          delete(targetDesc, null, idsByParentId, t);
        }
      }
    }
  }
  
  private void saveAssocOne(PersistRequestBean<?> request)
  {
    BeanDescriptor<?> desc = request.getBeanDescriptor();
    
    BeanPropertyAssocOne<?>[] ones = desc.propertiesOneImportedSave();
    for (int i = 0; i < ones.length; i++)
    {
      BeanPropertyAssocOne<?> prop = ones[i];
      if (request.isLoadedProperty(prop))
      {
        Object detailBean = prop.getValue(request.getBean());
        if ((detailBean != null) && 
          (!isReference(detailBean))) {
          if (!request.isParent(detailBean)) {
            if (!prop.isSaveRecurseSkippable(detailBean))
            {
              SpiTransaction t = request.getTransaction();
              t.depth(-1);
              saveRecurse(detailBean, t, null);
              t.depth(1);
            }
          }
        }
      }
    }
  }
  
  private boolean isReference(Object bean)
  {
    return ((bean instanceof EntityBean)) && (((EntityBean)bean)._ebean_getIntercept().isReference());
  }
  
  private DeleteUnloadedForeignKeys getDeleteUnloadedForeignKeys(PersistRequestBean<?> request)
  {
    DeleteUnloadedForeignKeys fkeys = null;
    
    BeanPropertyAssocOne<?>[] ones = request.getBeanDescriptor().propertiesOneImportedDelete();
    for (int i = 0; i < ones.length; i++) {
      if (!request.isLoadedProperty(ones[i]))
      {
        if (fkeys == null) {
          fkeys = new DeleteUnloadedForeignKeys(this.server, request);
        }
        fkeys.add(ones[i]);
      }
    }
    return fkeys;
  }
  
  private void deleteAssocOne(PersistRequestBean<?> request)
  {
    BeanDescriptor<?> desc = request.getBeanDescriptor();
    BeanPropertyAssocOne<?>[] ones = desc.propertiesOneImportedDelete();
    for (int i = 0; i < ones.length; i++)
    {
      BeanPropertyAssocOne<?> prop = ones[i];
      if (request.isLoadedProperty(prop))
      {
        Object detailBean = prop.getValue(request.getBean());
        if ((detailBean != null) && (prop.hasId(detailBean))) {
          deleteRecurse(detailBean, request.getTransaction());
        }
      }
    }
  }
  
  private void setIdGenValue(PersistRequestBean<?> request)
  {
    BeanDescriptor<?> desc = request.getBeanDescriptor();
    if (!desc.isUseIdGenerator()) {
      return;
    }
    BeanProperty idProp = desc.getSingleIdProperty();
    if ((idProp == null) || (idProp.isEmbedded())) {
      return;
    }
    Object bean = request.getBean();
    Object uid = idProp.getValue(bean);
    if (DmlUtil.isNullOrZero(uid))
    {
      Object nextId = desc.nextId(request.getTransaction());
      
      desc.convertSetId(nextId, bean);
    }
  }
  
  private Collection<?> getDetailsIterator(Object o)
  {
    if (o == null) {
      return null;
    }
    if ((o instanceof BeanCollection))
    {
      BeanCollection<?> bc = (BeanCollection)o;
      if (!bc.isPopulated()) {
        return null;
      }
      return bc.getActualDetails();
    }
    if ((o instanceof Map)) {
      return ((Map)o).entrySet();
    }
    if ((o instanceof Collection)) {
      return (Collection)o;
    }
    String m = "expecting a Map or Collection but got [" + o.getClass().getName() + "]";
    throw new PersistenceException(m);
  }
  
  private <T> PersistRequestBean<T> createRequest(T bean, Transaction t, Object parentBean)
  {
    BeanManager<T> mgr = getBeanManager(bean);
    if (mgr == null) {
      throw new PersistenceException(errNotRegistered(bean.getClass()));
    }
    return createRequest(bean, t, parentBean, mgr);
  }
  
  private String errNotRegistered(Class<?> beanClass)
  {
    String msg = "The type [" + beanClass + "] is not a registered entity?";
    msg = msg + " If you don't explicitly list the entity classes to use Ebean will search for them in the classpath.";
    msg = msg + " If the entity is in a Jar check the ebean.search.jars property in ebean.properties file or check ServerConfig.addJar().";
    return msg;
  }
  
  private PersistRequestBean<?> createRequest(Object bean, Transaction t, Object parentBean, BeanManager<?> mgr)
  {
    if (mgr.isLdapEntityType()) {
      return new LdapPersistBeanRequest(this.server, bean, parentBean, mgr, this.ldapPersister);
    }
    return new PersistRequestBean(this.server, bean, parentBean, mgr, (SpiTransaction)t, this.persistExecute);
  }
  
  private <T> BeanManager<T> getBeanManager(T bean)
  {
    return this.beanDescriptorManager.getBeanManager(bean.getClass());
  }
}
