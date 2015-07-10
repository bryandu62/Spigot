package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.CallableSql;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.Update;
import java.util.Collection;
import java.util.Set;

public abstract interface Persister
{
  public abstract void forceUpdate(Object paramObject, Set<String> paramSet, Transaction paramTransaction, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void forceInsert(Object paramObject, Transaction paramTransaction);
  
  public abstract void save(Object paramObject, Transaction paramTransaction);
  
  public abstract void saveManyToManyAssociations(Object paramObject, String paramString, Transaction paramTransaction);
  
  public abstract void saveAssociation(Object paramObject, String paramString, Transaction paramTransaction);
  
  public abstract int deleteManyToManyAssociations(Object paramObject, String paramString, Transaction paramTransaction);
  
  public abstract int delete(Class<?> paramClass, Object paramObject, Transaction paramTransaction);
  
  public abstract void delete(Object paramObject, Transaction paramTransaction);
  
  public abstract void deleteMany(Class<?> paramClass, Collection<?> paramCollection, Transaction paramTransaction);
  
  public abstract int executeOrmUpdate(Update<?> paramUpdate, Transaction paramTransaction);
  
  public abstract int executeSqlUpdate(SqlUpdate paramSqlUpdate, Transaction paramTransaction);
  
  public abstract int executeCallable(CallableSql paramCallableSql, Transaction paramTransaction);
}
