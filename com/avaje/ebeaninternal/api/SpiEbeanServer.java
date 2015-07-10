package com.avaje.ebeaninternal.api;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.TxScope;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.bean.BeanLoader;
import com.avaje.ebean.bean.CallStack;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import com.avaje.ebeaninternal.server.core.PstmtBatch;
import com.avaje.ebeaninternal.server.core.SpiOrmQueryRequest;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.query.CQuery;
import com.avaje.ebeaninternal.server.query.CQueryEngine;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import java.lang.reflect.Type;
import java.util.List;

public abstract interface SpiEbeanServer
  extends EbeanServer, BeanLoader, BeanCollectionLoader
{
  public abstract boolean isDefaultDeleteMissingChildren();
  
  public abstract boolean isDefaultUpdateNullProperties();
  
  public abstract boolean isVanillaMode();
  
  public abstract DatabasePlatform getDatabasePlatform();
  
  public abstract PstmtBatch getPstmtBatch();
  
  public abstract CallStack createCallStack();
  
  public abstract DdlGenerator getDdlGenerator();
  
  public abstract AutoFetchManager getAutoFetchManager();
  
  public abstract void clearQueryStatistics();
  
  public abstract List<BeanDescriptor<?>> getBeanDescriptors();
  
  public abstract <T> BeanDescriptor<T> getBeanDescriptor(Class<T> paramClass);
  
  public abstract BeanDescriptor<?> getBeanDescriptorById(String paramString);
  
  public abstract List<BeanDescriptor<?>> getBeanDescriptors(String paramString);
  
  public abstract void externalModification(TransactionEventTable paramTransactionEventTable);
  
  public abstract SpiTransaction createServerTransaction(boolean paramBoolean, int paramInt);
  
  public abstract SpiTransaction getCurrentServerTransaction();
  
  public abstract ScopeTrans createScopeTrans(TxScope paramTxScope);
  
  public abstract SpiTransaction createQueryTransaction();
  
  public abstract void remoteTransactionEvent(RemoteTransactionEvent paramRemoteTransactionEvent);
  
  public abstract <T> SpiOrmQueryRequest<T> createQueryRequest(BeanDescriptor<T> paramBeanDescriptor, SpiQuery<T> paramSpiQuery, Transaction paramTransaction);
  
  public abstract <T> CQuery<T> compileQuery(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract CQueryEngine getQueryEngine();
  
  public abstract <T> List<Object> findIdsWithCopy(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> int findRowCountWithCopy(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract void loadBean(LoadBeanRequest paramLoadBeanRequest);
  
  public abstract void loadMany(LoadManyRequest paramLoadManyRequest);
  
  public abstract int getLazyLoadBatchSize();
  
  public abstract boolean isSupportedType(Type paramType);
}
