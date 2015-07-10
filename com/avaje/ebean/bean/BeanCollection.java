package com.avaje.ebean.bean;

import com.avaje.ebean.ExpressionList;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract interface BeanCollection<E>
  extends Serializable
{
  public abstract Object getOwnerBean();
  
  public abstract String getPropertyName();
  
  public abstract int getLoaderIndex();
  
  public abstract boolean checkEmptyLazyLoad();
  
  public abstract ExpressionList<?> getFilterMany();
  
  public abstract void setFilterMany(ExpressionList<?> paramExpressionList);
  
  public abstract void setBackgroundFetch(Future<Integer> paramFuture);
  
  public abstract void backgroundFetchWait(long paramLong, TimeUnit paramTimeUnit);
  
  public abstract void backgroundFetchWait();
  
  public abstract void setBeanCollectionTouched(BeanCollectionTouched paramBeanCollectionTouched);
  
  public abstract void setLoader(int paramInt, BeanCollectionLoader paramBeanCollectionLoader);
  
  public abstract void setReadOnly(boolean paramBoolean);
  
  public abstract boolean isReadOnly();
  
  public abstract void internalAdd(Object paramObject);
  
  public abstract Object getActualCollection();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract Collection<E> getActualDetails();
  
  public abstract boolean hasMoreRows();
  
  public abstract void setHasMoreRows(boolean paramBoolean);
  
  public abstract boolean isFinishedFetch();
  
  public abstract void setFinishedFetch(boolean paramBoolean);
  
  public abstract boolean isPopulated();
  
  public abstract boolean isReference();
  
  public abstract void setModifyListening(ModifyListenMode paramModifyListenMode);
  
  public abstract void modifyAddition(E paramE);
  
  public abstract void modifyRemoval(Object paramObject);
  
  public abstract Set<E> getModifyAdditions();
  
  public abstract Set<E> getModifyRemovals();
  
  public abstract void modifyReset();
  
  public static enum ModifyListenMode
  {
    NONE,  REMOVALS,  ALL;
    
    private ModifyListenMode() {}
  }
}
