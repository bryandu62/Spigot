package com.avaje.ebeaninternal.api;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.OrderBy;
import com.avaje.ebean.Query;
import com.avaje.ebean.QueryListener;
import com.avaje.ebean.bean.BeanCollectionTouched;
import com.avaje.ebean.bean.CallStack;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import com.avaje.ebeaninternal.server.query.CancelableQuery;
import com.avaje.ebeaninternal.server.querydefn.NaturalKeyBindParam;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import java.util.ArrayList;
import java.util.List;

public abstract interface SpiQuery<T>
  extends Query<T>
{
  public abstract void setTotalHits(int paramInt);
  
  public abstract boolean selectAllForLazyLoadProperty();
  
  public abstract void setMode(Mode paramMode);
  
  public abstract Mode getMode();
  
  public abstract BeanCollectionTouched getBeanCollectionTouched();
  
  public abstract void setBeanCollectionTouched(BeanCollectionTouched paramBeanCollectionTouched);
  
  public abstract void setIdList(List<Object> paramList);
  
  public abstract List<Object> getIdList();
  
  public abstract SpiQuery<T> copy();
  
  public abstract Type getType();
  
  public abstract void setType(Type paramType);
  
  public abstract String getLoadDescription();
  
  public abstract String getLoadMode();
  
  public abstract void setLoadDescription(String paramString1, String paramString2);
  
  public abstract void setBeanDescriptor(BeanDescriptor<?> paramBeanDescriptor);
  
  public abstract boolean initManyWhereJoins();
  
  public abstract ManyWhereJoins getManyWhereJoins();
  
  public abstract void convertWhereNaturalKeyToId(Object paramObject);
  
  public abstract NaturalKeyBindParam getNaturalKeyBindParam();
  
  public abstract void setSelectId();
  
  public abstract void setFilterMany(String paramString, ExpressionList<?> paramExpressionList);
  
  public abstract List<OrmQueryProperties> removeQueryJoins();
  
  public abstract List<OrmQueryProperties> removeLazyJoins();
  
  public abstract void setLazyLoadManyPath(String paramString);
  
  public abstract void convertManyFetchJoinsToQueryJoins(boolean paramBoolean, int paramInt);
  
  public abstract PersistenceContext getPersistenceContext();
  
  public abstract void setPersistenceContext(PersistenceContext paramPersistenceContext);
  
  public abstract boolean isDetailEmpty();
  
  public abstract Boolean isAutofetch();
  
  public abstract Boolean isForUpdate();
  
  public abstract AutoFetchManager getAutoFetchManager();
  
  public abstract void setAutoFetchManager(AutoFetchManager paramAutoFetchManager);
  
  public abstract ObjectGraphNode setOrigin(CallStack paramCallStack);
  
  public abstract void setParentNode(ObjectGraphNode paramObjectGraphNode);
  
  public abstract void setLazyLoadProperty(String paramString);
  
  public abstract String getLazyLoadProperty();
  
  public abstract String getLazyLoadManyPath();
  
  public abstract ObjectGraphNode getParentNode();
  
  public abstract boolean isUsageProfiling();
  
  public abstract void setUsageProfiling(boolean paramBoolean);
  
  public abstract String getName();
  
  public abstract int queryAutofetchHash();
  
  public abstract int queryPlanHash(BeanQueryRequest<?> paramBeanQueryRequest);
  
  public abstract int queryBindHash();
  
  public abstract int queryHash();
  
  public abstract boolean isSqlSelect();
  
  public abstract boolean isRawSql();
  
  public abstract OrderBy<T> getOrderBy();
  
  public abstract String getAdditionalWhere();
  
  public abstract SpiExpressionList<T> getWhereExpressions();
  
  public abstract SpiExpressionList<T> getHavingExpressions();
  
  public abstract String getAdditionalHaving();
  
  public abstract boolean hasMaxRowsOrFirstRow();
  
  public abstract Boolean isUseBeanCache();
  
  public abstract boolean isUseQueryCache();
  
  public abstract boolean isLoadBeanCache();
  
  public abstract Boolean isReadOnly();
  
  public abstract void contextAdd(EntityBean paramEntityBean);
  
  public abstract Class<T> getBeanType();
  
  public abstract int getTimeout();
  
  public abstract ArrayList<EntityBean> getContextAdditions();
  
  public abstract BindParams getBindParams();
  
  public abstract String getQuery();
  
  public abstract void setDetail(OrmQueryDetail paramOrmQueryDetail);
  
  public abstract boolean tuneFetchProperties(OrmQueryDetail paramOrmQueryDetail);
  
  public abstract void setAutoFetchTuned(boolean paramBoolean);
  
  public abstract OrmQueryDetail getDetail();
  
  public abstract TableJoin getIncludeTableJoin();
  
  public abstract void setIncludeTableJoin(TableJoin paramTableJoin);
  
  public abstract String getMapKey();
  
  public abstract int getBackgroundFetchAfter();
  
  public abstract int getMaxRows();
  
  public abstract int getFirstRow();
  
  public abstract boolean isDistinct();
  
  public abstract boolean isVanillaMode(boolean paramBoolean);
  
  public abstract void setDefaultSelectClause();
  
  public abstract String getRawWhereClause();
  
  public abstract Object getId();
  
  public abstract QueryListener<T> getListener();
  
  public abstract boolean createOwnTransaction();
  
  public abstract void setGeneratedSql(String paramString);
  
  public abstract int getBufferFetchSizeHint();
  
  public abstract boolean isFutureFetch();
  
  public abstract void setFutureFetch(boolean paramBoolean);
  
  public abstract void setCancelableQuery(CancelableQuery paramCancelableQuery);
  
  public abstract boolean isCancelled();
  
  public static enum Mode
  {
    NORMAL(false),  LAZYLOAD_MANY(false),  LAZYLOAD_BEAN(true),  REFRESH_BEAN(true);
    
    private final boolean loadContextBean;
    
    private Mode(boolean loadContextBean)
    {
      this.loadContextBean = loadContextBean;
    }
    
    public boolean isLoadContextBean()
    {
      return this.loadContextBean;
    }
  }
  
  public static enum Type
  {
    BEAN,  LIST,  SET,  MAP,  ID_LIST,  ROWCOUNT,  SUBQUERY;
    
    private Type() {}
  }
}
