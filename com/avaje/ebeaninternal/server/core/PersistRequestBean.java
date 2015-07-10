package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.ValidationException;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebean.event.BeanPersistListener;
import com.avaje.ebean.event.BeanPersistRequest;
import com.avaje.ebeaninternal.api.DerivedRelationshipData;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanManager;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.persist.BatchControl;
import com.avaje.ebeaninternal.server.persist.PersistExecute;
import com.avaje.ebeaninternal.server.persist.dml.GenerateDmlRequest;
import com.avaje.ebeaninternal.server.transaction.BeanDelta;
import com.avaje.ebeaninternal.server.transaction.BeanPersistIdMap;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import javax.persistence.OptimisticLockException;

public class PersistRequestBean<T>
  extends PersistRequest
  implements BeanPersistRequest<T>
{
  protected final BeanManager<T> beanManager;
  protected final BeanDescriptor<T> beanDescriptor;
  protected final BeanPersistListener<T> beanPersistListener;
  protected final BeanPersistController controller;
  protected final EntityBeanIntercept intercept;
  protected final Object parentBean;
  protected final boolean isDirty;
  protected final boolean vanilla;
  protected final T bean;
  protected T oldValues;
  protected ConcurrencyMode concurrencyMode;
  protected final Set<String> loadedProps;
  protected Object idValue;
  protected Integer beanHash;
  protected Integer beanIdentityHash;
  protected final Set<String> changedProps;
  protected boolean notifyCache;
  private boolean statelessUpdate;
  private boolean deleteMissingChildren;
  private boolean updateNullProperties;
  
  public PersistRequestBean(SpiEbeanServer server, T bean, Object parentBean, BeanManager<T> mgr, SpiTransaction t, PersistExecute persistExecute, Set<String> updateProps, ConcurrencyMode concurrencyMode)
  {
    super(server, t, persistExecute);
    this.beanManager = mgr;
    this.beanDescriptor = mgr.getBeanDescriptor();
    this.beanPersistListener = this.beanDescriptor.getPersistListener();
    this.bean = bean;
    this.parentBean = parentBean;
    
    this.controller = this.beanDescriptor.getPersistController();
    this.concurrencyMode = this.beanDescriptor.getConcurrencyMode();
    
    this.concurrencyMode = concurrencyMode;
    this.loadedProps = updateProps;
    this.changedProps = updateProps;
    
    this.vanilla = true;
    this.isDirty = true;
    this.oldValues = bean;
    if ((bean instanceof EntityBean)) {
      this.intercept = ((EntityBean)bean)._ebean_getIntercept();
    } else {
      this.intercept = null;
    }
  }
  
  public PersistRequestBean(SpiEbeanServer server, T bean, Object parentBean, BeanManager<T> mgr, SpiTransaction t, PersistExecute persistExecute)
  {
    super(server, t, persistExecute);
    this.beanManager = mgr;
    this.beanDescriptor = mgr.getBeanDescriptor();
    this.beanPersistListener = this.beanDescriptor.getPersistListener();
    this.bean = bean;
    this.parentBean = parentBean;
    
    this.controller = this.beanDescriptor.getPersistController();
    this.concurrencyMode = this.beanDescriptor.getConcurrencyMode();
    if ((bean instanceof EntityBean))
    {
      this.intercept = ((EntityBean)bean)._ebean_getIntercept();
      if (this.intercept.isReference()) {
        this.concurrencyMode = ConcurrencyMode.NONE;
      }
      this.isDirty = this.intercept.isDirty();
      if (!this.isDirty)
      {
        this.changedProps = this.intercept.getChangedProps();
      }
      else
      {
        Set<String> beanChangedProps = this.intercept.getChangedProps();
        Set<String> dirtyEmbedded = this.beanDescriptor.getDirtyEmbeddedProperties(bean);
        this.changedProps = mergeChangedProperties(beanChangedProps, dirtyEmbedded);
      }
      this.loadedProps = this.intercept.getLoadedProps();
      this.oldValues = this.intercept.getOldValues();
      this.vanilla = false;
    }
    else
    {
      this.vanilla = true;
      this.isDirty = true;
      this.loadedProps = null;
      this.changedProps = null;
      this.intercept = null;
      if (this.concurrencyMode.equals(ConcurrencyMode.ALL)) {
        this.concurrencyMode = ConcurrencyMode.NONE;
      }
    }
  }
  
  private Set<String> mergeChangedProperties(Set<String> beanChangedProps, Set<String> embChanged)
  {
    if (embChanged == null) {
      return beanChangedProps;
    }
    if (beanChangedProps == null) {
      return embChanged;
    }
    beanChangedProps.addAll(embChanged);
    return beanChangedProps;
  }
  
  public boolean isNotify(TransactionEvent txnEvent)
  {
    return (this.notifyCache) || (isNotifyPersistListener());
  }
  
  public boolean isNotifyCache()
  {
    return this.notifyCache;
  }
  
  public boolean isNotifyPersistListener()
  {
    return this.beanPersistListener != null;
  }
  
  public void notifyCache()
  {
    if (this.notifyCache) {
      switch (this.type)
      {
      case INSERT: 
        this.beanDescriptor.cacheInsert(this.idValue, this);
        break;
      case UPDATE: 
        this.beanDescriptor.cacheUpdate(this.idValue, this);
        break;
      case DELETE: 
        this.beanDescriptor.cacheDelete(this.idValue, this);
        break;
      default: 
        throw new IllegalStateException("Invalid type " + this.type);
      }
    }
  }
  
  public void addToPersistMap(BeanPersistIdMap beanPersistMap)
  {
    beanPersistMap.add(this.beanDescriptor, this.type, this.idValue);
  }
  
  public boolean notifyLocalPersistListener()
  {
    if (this.beanPersistListener == null) {
      return false;
    }
    switch (this.type)
    {
    case INSERT: 
      return this.beanPersistListener.inserted(this.bean);
    case UPDATE: 
      return this.beanPersistListener.updated(this.bean, getUpdatedProperties());
    case DELETE: 
      return this.beanPersistListener.deleted(this.bean);
    }
    return false;
  }
  
  public boolean isParent(Object o)
  {
    return o == this.parentBean;
  }
  
  public boolean isRegisteredBean()
  {
    return this.transaction.isRegisteredBean(this.bean);
  }
  
  public void unRegisterBean()
  {
    this.transaction.unregisterBean(this.bean);
  }
  
  private Integer getBeanHash()
  {
    if (this.beanHash == null)
    {
      Object id = this.beanDescriptor.getId(this.bean);
      int hc = 31 * this.bean.getClass().getName().hashCode();
      if (id != null) {
        hc += id.hashCode();
      }
      this.beanHash = Integer.valueOf(hc);
    }
    return this.beanHash;
  }
  
  public void registerDeleteBean()
  {
    Integer hash = getBeanHash();
    this.transaction.registerDeleteBean(hash);
  }
  
  public void unregisterDeleteBean()
  {
    Integer hash = getBeanHash();
    this.transaction.unregisterDeleteBean(hash);
  }
  
  public boolean isRegisteredForDeleteBean()
  {
    if (this.transaction == null) {
      return false;
    }
    Integer hash = getBeanHash();
    return this.transaction.isRegisteredDeleteBean(hash);
  }
  
  public void setType(PersistRequest.Type type)
  {
    this.type = type;
    this.notifyCache = this.beanDescriptor.isCacheNotify();
    if (((type == PersistRequest.Type.DELETE) || (type == PersistRequest.Type.UPDATE)) && 
      (this.oldValues == null)) {
      this.oldValues = this.bean;
    }
  }
  
  public BeanManager<T> getBeanManager()
  {
    return this.beanManager;
  }
  
  public BeanDescriptor<T> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public boolean isStatelessUpdate()
  {
    return this.statelessUpdate;
  }
  
  public boolean isDeleteMissingChildren()
  {
    return this.deleteMissingChildren;
  }
  
  public boolean isUpdateNullProperties()
  {
    return this.updateNullProperties;
  }
  
  public void setStatelessUpdate(boolean statelessUpdate, boolean deleteMissingChildren, boolean updateNullProperties)
  {
    this.statelessUpdate = statelessUpdate;
    this.deleteMissingChildren = deleteMissingChildren;
    this.updateNullProperties = updateNullProperties;
  }
  
  public boolean isDirty()
  {
    return this.isDirty;
  }
  
  public ConcurrencyMode getConcurrencyMode()
  {
    return this.concurrencyMode;
  }
  
  public void setLoadedProps(Set<String> additionalProps)
  {
    if (this.intercept != null) {
      this.intercept.setLoadedProps(additionalProps);
    }
  }
  
  public Set<String> getLoadedProperties()
  {
    return this.loadedProps;
  }
  
  public String getFullName()
  {
    return this.beanDescriptor.getFullName();
  }
  
  public T getBean()
  {
    return (T)this.bean;
  }
  
  public Object getBeanId()
  {
    return this.beanDescriptor.getId(this.bean);
  }
  
  public BeanDelta createDeltaBean()
  {
    return new BeanDelta(this.beanDescriptor, getBeanId());
  }
  
  public T getOldValues()
  {
    return (T)this.oldValues;
  }
  
  public Object getParentBean()
  {
    return this.parentBean;
  }
  
  public BeanPersistController getBeanController()
  {
    return this.controller;
  }
  
  public EntityBeanIntercept getEntityBeanIntercept()
  {
    return this.intercept;
  }
  
  public void validate()
  {
    InvalidValue errs = this.beanDescriptor.validate(false, this.bean);
    if (errs != null) {
      throw new ValidationException(errs);
    }
  }
  
  public boolean isLoadedProperty(BeanProperty prop)
  {
    if (this.loadedProps == null) {
      return true;
    }
    return this.loadedProps.contains(prop.getName());
  }
  
  public int executeNow()
  {
    switch (this.type)
    {
    case INSERT: 
      this.persistExecute.executeInsertBean(this);
      return -1;
    case UPDATE: 
      this.persistExecute.executeUpdateBean(this);
      return -1;
    case DELETE: 
      this.persistExecute.executeDeleteBean(this);
      return -1;
    }
    throw new RuntimeException("Invalid type " + this.type);
  }
  
  public int executeOrQueue()
  {
    boolean batch = this.transaction.isBatchThisRequest();
    
    BatchControl control = this.transaction.getBatchControl();
    if (control != null) {
      return control.executeOrQueue(this, batch);
    }
    if (batch)
    {
      control = this.persistExecute.createBatchControl(this.transaction);
      return control.executeOrQueue(this, batch);
    }
    return executeNow();
  }
  
  public void setGeneratedKey(Object idValue)
  {
    if (idValue != null)
    {
      idValue = this.beanDescriptor.convertSetId(idValue, this.bean);
      
      this.idValue = idValue;
    }
  }
  
  public void setBoundId(Object idValue)
  {
    this.idValue = idValue;
  }
  
  public final void checkRowCount(int rowCount)
    throws SQLException
  {
    if (rowCount != 1)
    {
      String m = Message.msg("persist.conc2", "" + rowCount);
      throw new OptimisticLockException(m, null, this.bean);
    }
  }
  
  public void postExecute()
    throws SQLException
  {
    if (this.controller != null) {
      controllerPost();
    }
    if (this.intercept != null) {
      this.intercept.setLoaded();
    }
    addEvent();
    if (isLogSummary()) {
      logSummary();
    }
  }
  
  private void controllerPost()
  {
    switch (this.type)
    {
    case INSERT: 
      this.controller.postInsert(this);
      break;
    case UPDATE: 
      this.controller.postUpdate(this);
      break;
    case DELETE: 
      this.controller.postDelete(this);
      break;
    }
  }
  
  private void logSummary()
  {
    String name = this.beanDescriptor.getName();
    switch (this.type)
    {
    case INSERT: 
      this.transaction.logInternal("Inserted [" + name + "] [" + this.idValue + "]");
      break;
    case UPDATE: 
      this.transaction.logInternal("Updated [" + name + "] [" + this.idValue + "]");
      break;
    case DELETE: 
      this.transaction.logInternal("Deleted [" + name + "] [" + this.idValue + "]");
      break;
    }
  }
  
  private void addEvent()
  {
    TransactionEvent event = this.transaction.getEvent();
    if (event != null) {
      event.add(this);
    }
  }
  
  public ConcurrencyMode determineConcurrencyMode()
  {
    if (this.loadedProps != null) {
      if (this.concurrencyMode.equals(ConcurrencyMode.VERSION))
      {
        BeanProperty prop = this.beanDescriptor.firstVersionProperty();
        if ((prop == null) || (!this.loadedProps.contains(prop.getName()))) {
          this.concurrencyMode = ConcurrencyMode.ALL;
        }
      }
    }
    return this.concurrencyMode;
  }
  
  public boolean isDynamicUpdateSql()
  {
    return ((!this.vanilla) && (this.beanDescriptor.isUpdateChangesOnly())) || (this.loadedProps != null);
  }
  
  public GenerateDmlRequest createGenerateDmlRequest(boolean emptyStringAsNull)
  {
    if (this.beanDescriptor.isUpdateChangesOnly()) {
      return new GenerateDmlRequest(emptyStringAsNull, this.changedProps, this.loadedProps, this.oldValues);
    }
    return new GenerateDmlRequest(emptyStringAsNull, this.loadedProps, this.loadedProps, this.oldValues);
  }
  
  public Set<String> getUpdatedProperties()
  {
    if (this.changedProps != null) {
      return this.changedProps;
    }
    return this.loadedProps;
  }
  
  public boolean hasChanged(BeanProperty prop)
  {
    return this.changedProps.contains(prop.getName());
  }
  
  public List<DerivedRelationshipData> getDerivedRelationships()
  {
    return this.transaction.getDerivedRelationship(this.bean);
  }
}
