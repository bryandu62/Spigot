package com.avaje.ebean;

import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.text.csv.CsvReader;
import com.avaje.ebean.text.json.JsonContext;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.OptimisticLockException;

public abstract interface EbeanServer
{
  public abstract AdminLogging getAdminLogging();
  
  public abstract AdminAutofetch getAdminAutofetch();
  
  public abstract String getName();
  
  public abstract ExpressionFactory getExpressionFactory();
  
  public abstract BeanState getBeanState(Object paramObject);
  
  public abstract Object getBeanId(Object paramObject);
  
  public abstract Map<String, ValuePair> diff(Object paramObject1, Object paramObject2);
  
  public abstract InvalidValue validate(Object paramObject);
  
  public abstract InvalidValue[] validate(Object paramObject1, String paramString, Object paramObject2);
  
  public abstract <T> T createEntityBean(Class<T> paramClass);
  
  public abstract ObjectInputStream createProxyObjectInputStream(InputStream paramInputStream);
  
  public abstract <T> CsvReader<T> createCsvReader(Class<T> paramClass);
  
  public abstract <T> Query<T> createNamedQuery(Class<T> paramClass, String paramString);
  
  public abstract <T> Query<T> createQuery(Class<T> paramClass, String paramString);
  
  public abstract <T> Query<T> createQuery(Class<T> paramClass);
  
  public abstract <T> Query<T> find(Class<T> paramClass);
  
  public abstract Object nextId(Class<?> paramClass);
  
  public abstract <T> Filter<T> filter(Class<T> paramClass);
  
  public abstract <T> void sort(List<T> paramList, String paramString);
  
  public abstract <T> Update<T> createNamedUpdate(Class<T> paramClass, String paramString);
  
  public abstract <T> Update<T> createUpdate(Class<T> paramClass, String paramString);
  
  public abstract SqlQuery createSqlQuery(String paramString);
  
  public abstract SqlQuery createNamedSqlQuery(String paramString);
  
  public abstract SqlUpdate createSqlUpdate(String paramString);
  
  public abstract CallableSql createCallableSql(String paramString);
  
  public abstract SqlUpdate createNamedSqlUpdate(String paramString);
  
  public abstract Transaction createTransaction();
  
  public abstract Transaction createTransaction(TxIsolation paramTxIsolation);
  
  public abstract Transaction beginTransaction();
  
  public abstract Transaction beginTransaction(TxIsolation paramTxIsolation);
  
  public abstract Transaction currentTransaction();
  
  public abstract void commitTransaction();
  
  public abstract void rollbackTransaction();
  
  public abstract void endTransaction();
  
  public abstract void logComment(String paramString);
  
  public abstract void refresh(Object paramObject);
  
  public abstract void refreshMany(Object paramObject, String paramString);
  
  public abstract <T> T find(Class<T> paramClass, Object paramObject);
  
  public abstract <T> T getReference(Class<T> paramClass, Object paramObject);
  
  public abstract <T> int findRowCount(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> List<Object> findIds(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> QueryIterator<T> findIterate(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> void findVisit(Query<T> paramQuery, QueryResultVisitor<T> paramQueryResultVisitor, Transaction paramTransaction);
  
  public abstract <T> List<T> findList(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> FutureRowCount<T> findFutureRowCount(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> FutureIds<T> findFutureIds(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> FutureList<T> findFutureList(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract SqlFutureList findFutureList(SqlQuery paramSqlQuery, Transaction paramTransaction);
  
  public abstract <T> PagingList<T> findPagingList(Query<T> paramQuery, Transaction paramTransaction, int paramInt);
  
  public abstract <T> Set<T> findSet(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> Map<?, T> findMap(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract <T> T findUnique(Query<T> paramQuery, Transaction paramTransaction);
  
  public abstract List<SqlRow> findList(SqlQuery paramSqlQuery, Transaction paramTransaction);
  
  public abstract Set<SqlRow> findSet(SqlQuery paramSqlQuery, Transaction paramTransaction);
  
  public abstract Map<?, SqlRow> findMap(SqlQuery paramSqlQuery, Transaction paramTransaction);
  
  public abstract SqlRow findUnique(SqlQuery paramSqlQuery, Transaction paramTransaction);
  
  public abstract void save(Object paramObject)
    throws OptimisticLockException;
  
  public abstract int save(Iterator<?> paramIterator)
    throws OptimisticLockException;
  
  public abstract int save(Collection<?> paramCollection)
    throws OptimisticLockException;
  
  public abstract void delete(Object paramObject)
    throws OptimisticLockException;
  
  public abstract int delete(Iterator<?> paramIterator)
    throws OptimisticLockException;
  
  public abstract int delete(Collection<?> paramCollection)
    throws OptimisticLockException;
  
  public abstract int delete(Class<?> paramClass, Object paramObject);
  
  public abstract int delete(Class<?> paramClass, Object paramObject, Transaction paramTransaction);
  
  public abstract void delete(Class<?> paramClass, Collection<?> paramCollection);
  
  public abstract void delete(Class<?> paramClass, Collection<?> paramCollection, Transaction paramTransaction);
  
  public abstract int execute(SqlUpdate paramSqlUpdate);
  
  public abstract int execute(Update<?> paramUpdate);
  
  public abstract int execute(Update<?> paramUpdate, Transaction paramTransaction);
  
  public abstract int execute(CallableSql paramCallableSql);
  
  public abstract void externalModification(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
  
  public abstract <T> T find(Class<T> paramClass, Object paramObject, Transaction paramTransaction);
  
  public abstract void save(Object paramObject, Transaction paramTransaction)
    throws OptimisticLockException;
  
  public abstract int save(Iterator<?> paramIterator, Transaction paramTransaction)
    throws OptimisticLockException;
  
  public abstract void update(Object paramObject);
  
  public abstract void update(Object paramObject, Transaction paramTransaction);
  
  public abstract void update(Object paramObject, Set<String> paramSet);
  
  public abstract void update(Object paramObject, Set<String> paramSet, Transaction paramTransaction);
  
  public abstract void update(Object paramObject, Set<String> paramSet, Transaction paramTransaction, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void insert(Object paramObject);
  
  public abstract void insert(Object paramObject, Transaction paramTransaction);
  
  public abstract int deleteManyToManyAssociations(Object paramObject, String paramString);
  
  public abstract int deleteManyToManyAssociations(Object paramObject, String paramString, Transaction paramTransaction);
  
  public abstract void saveManyToManyAssociations(Object paramObject, String paramString);
  
  public abstract void saveManyToManyAssociations(Object paramObject, String paramString, Transaction paramTransaction);
  
  public abstract void saveAssociation(Object paramObject, String paramString);
  
  public abstract void saveAssociation(Object paramObject, String paramString, Transaction paramTransaction);
  
  public abstract void delete(Object paramObject, Transaction paramTransaction)
    throws OptimisticLockException;
  
  public abstract int delete(Iterator<?> paramIterator, Transaction paramTransaction)
    throws OptimisticLockException;
  
  public abstract int execute(SqlUpdate paramSqlUpdate, Transaction paramTransaction);
  
  public abstract int execute(CallableSql paramCallableSql, Transaction paramTransaction);
  
  public abstract void execute(TxScope paramTxScope, TxRunnable paramTxRunnable);
  
  public abstract void execute(TxRunnable paramTxRunnable);
  
  public abstract <T> T execute(TxScope paramTxScope, TxCallable<T> paramTxCallable);
  
  public abstract <T> T execute(TxCallable<T> paramTxCallable);
  
  public abstract ServerCacheManager getServerCacheManager();
  
  public abstract BackgroundExecutor getBackgroundExecutor();
  
  public abstract void runCacheWarming();
  
  public abstract void runCacheWarming(Class<?> paramClass);
  
  public abstract JsonContext createJsonContext();
}
