package com.avaje.ebean.common;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollection.ModifyListenMode;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.bean.BeanCollectionTouched;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.persistence.PersistenceException;

public abstract class AbstractBeanCollection<E>
  implements BeanCollection<E>
{
  private static final long serialVersionUID = 3365725236140187588L;
  protected boolean readOnly;
  protected transient BeanCollectionLoader loader;
  protected transient ExpressionList<?> filterMany;
  protected int loaderIndex;
  protected String ebeanServerName;
  protected transient BeanCollectionTouched beanCollectionTouched;
  protected transient Future<Integer> fetchFuture;
  protected final Object ownerBean;
  protected final String propertyName;
  protected boolean finishedFetch = true;
  protected boolean hasMoreRows;
  protected ModifyHolder<E> modifyHolder;
  protected BeanCollection.ModifyListenMode modifyListenMode;
  protected boolean modifyAddListening;
  protected boolean modifyRemoveListening;
  protected boolean modifyListening;
  
  public AbstractBeanCollection()
  {
    this.ownerBean = null;
    this.propertyName = null;
  }
  
  public AbstractBeanCollection(BeanCollectionLoader loader, Object ownerBean, String propertyName)
  {
    this.loader = loader;
    this.ebeanServerName = loader.getName();
    this.ownerBean = ownerBean;
    this.propertyName = propertyName;
    if ((ownerBean instanceof EntityBean))
    {
      EntityBeanIntercept ebi = ((EntityBean)ownerBean)._ebean_getIntercept();
      this.readOnly = ebi.isReadOnly();
    }
  }
  
  public Object getOwnerBean()
  {
    return this.ownerBean;
  }
  
  public String getPropertyName()
  {
    return this.propertyName;
  }
  
  public int getLoaderIndex()
  {
    return this.loaderIndex;
  }
  
  public ExpressionList<?> getFilterMany()
  {
    return this.filterMany;
  }
  
  public void setFilterMany(ExpressionList<?> filterMany)
  {
    this.filterMany = filterMany;
  }
  
  protected void lazyLoadCollection(boolean onlyIds)
  {
    if (this.loader == null) {
      this.loader = ((BeanCollectionLoader)Ebean.getServer(this.ebeanServerName));
    }
    if (this.loader == null)
    {
      String msg = "Lazy loading but LazyLoadEbeanServer is null? The LazyLoadEbeanServer needs to be set after deserialization to support lazy loading.";
      
      throw new PersistenceException(msg);
    }
    this.loader.loadMany(this, onlyIds);
    checkEmptyLazyLoad();
  }
  
  protected void touched()
  {
    if (this.beanCollectionTouched != null)
    {
      this.beanCollectionTouched.notifyTouched(this);
      this.beanCollectionTouched = null;
    }
  }
  
  public void setBeanCollectionTouched(BeanCollectionTouched notify)
  {
    this.beanCollectionTouched = notify;
  }
  
  public void setLoader(int beanLoaderIndex, BeanCollectionLoader loader)
  {
    this.loaderIndex = beanLoaderIndex;
    this.loader = loader;
    this.ebeanServerName = loader.getName();
  }
  
  public boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }
  
  public boolean hasMoreRows()
  {
    return this.hasMoreRows;
  }
  
  public void setHasMoreRows(boolean hasMoreRows)
  {
    this.hasMoreRows = hasMoreRows;
  }
  
  public boolean isFinishedFetch()
  {
    return this.finishedFetch;
  }
  
  public void setFinishedFetch(boolean finishedFetch)
  {
    this.finishedFetch = finishedFetch;
  }
  
  public void setBackgroundFetch(Future<Integer> fetchFuture)
  {
    this.fetchFuture = fetchFuture;
  }
  
  public void backgroundFetchWait(long wait, TimeUnit timeUnit)
  {
    if (this.fetchFuture != null) {
      try
      {
        this.fetchFuture.get(wait, timeUnit);
      }
      catch (Exception e)
      {
        throw new PersistenceException(e);
      }
    }
  }
  
  public void backgroundFetchWait()
  {
    if (this.fetchFuture != null) {
      try
      {
        this.fetchFuture.get();
      }
      catch (Exception e)
      {
        throw new PersistenceException(e);
      }
    }
  }
  
  protected void checkReadOnly()
  {
    if (this.readOnly)
    {
      String msg = "This collection is in ReadOnly mode";
      throw new IllegalStateException(msg);
    }
  }
  
  public void setModifyListening(BeanCollection.ModifyListenMode mode)
  {
    this.modifyListenMode = mode;
    this.modifyAddListening = BeanCollection.ModifyListenMode.ALL.equals(mode);
    this.modifyRemoveListening = ((this.modifyAddListening) || (BeanCollection.ModifyListenMode.REMOVALS.equals(mode)));
    this.modifyListening = ((this.modifyRemoveListening) || (this.modifyAddListening));
    if (this.modifyListening) {
      this.modifyHolder = null;
    }
  }
  
  public BeanCollection.ModifyListenMode getModifyListenMode()
  {
    return this.modifyListenMode;
  }
  
  protected ModifyHolder<E> getModifyHolder()
  {
    if (this.modifyHolder == null) {
      this.modifyHolder = new ModifyHolder();
    }
    return this.modifyHolder;
  }
  
  public void modifyAddition(E bean)
  {
    if (this.modifyAddListening) {
      getModifyHolder().modifyAddition(bean);
    }
  }
  
  public void modifyRemoval(Object bean)
  {
    if (this.modifyRemoveListening) {
      getModifyHolder().modifyRemoval(bean);
    }
  }
  
  public void modifyReset()
  {
    if (this.modifyHolder != null) {
      this.modifyHolder.reset();
    }
  }
  
  public Set<E> getModifyAdditions()
  {
    if (this.modifyHolder == null) {
      return null;
    }
    return this.modifyHolder.getModifyAdditions();
  }
  
  public Set<E> getModifyRemovals()
  {
    if (this.modifyHolder == null) {
      return null;
    }
    return this.modifyHolder.getModifyRemovals();
  }
}
