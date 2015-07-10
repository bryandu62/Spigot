package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.core.PersistRequestCallableSql;
import com.avaje.ebeaninternal.server.core.PersistRequestOrmUpdate;
import com.avaje.ebeaninternal.server.core.PersistRequestUpdateSql;
import com.avaje.ebeaninternal.server.core.PstmtBatch;
import com.avaje.ebeaninternal.server.deploy.BeanManager;

public final class DefaultPersistExecute
  implements PersistExecute
{
  private final ExeCallableSql exeCallableSql;
  private final ExeUpdateSql exeUpdateSql;
  private final ExeOrmUpdate exeOrmUpdate;
  private final int defaultBatchSize;
  private final boolean defaultBatchGenKeys;
  private final boolean validate;
  
  public DefaultPersistExecute(boolean validate, Binder binder, PstmtBatch pstmtBatch)
  {
    this.validate = validate;
    this.exeOrmUpdate = new ExeOrmUpdate(binder, pstmtBatch);
    this.exeUpdateSql = new ExeUpdateSql(binder, pstmtBatch);
    this.exeCallableSql = new ExeCallableSql(binder, pstmtBatch);
    
    this.defaultBatchGenKeys = GlobalProperties.getBoolean("batch.getgeneratedkeys", true);
    this.defaultBatchSize = GlobalProperties.getInt("batch.size", 20);
  }
  
  public BatchControl createBatchControl(SpiTransaction t)
  {
    return new BatchControl(t, this.defaultBatchSize, this.defaultBatchGenKeys);
  }
  
  public <T> void executeInsertBean(PersistRequestBean<T> request)
  {
    BeanManager<T> mgr = request.getBeanManager();
    BeanPersister persister = mgr.getBeanPersister();
    
    BeanPersistController controller = request.getBeanController();
    if ((controller == null) || (controller.preInsert(request)))
    {
      if (this.validate) {
        request.validate();
      }
      persister.insert(request);
    }
  }
  
  public <T> void executeUpdateBean(PersistRequestBean<T> request)
  {
    BeanManager<T> mgr = request.getBeanManager();
    BeanPersister persister = mgr.getBeanPersister();
    
    BeanPersistController controller = request.getBeanController();
    if ((controller == null) || (controller.preUpdate(request)))
    {
      if (this.validate) {
        request.validate();
      }
      persister.update(request);
    }
  }
  
  public <T> void executeDeleteBean(PersistRequestBean<T> request)
  {
    BeanManager<T> mgr = request.getBeanManager();
    BeanPersister persister = mgr.getBeanPersister();
    
    BeanPersistController controller = request.getBeanController();
    if ((controller == null) || (controller.preDelete(request))) {
      persister.delete(request);
    }
  }
  
  public int executeOrmUpdate(PersistRequestOrmUpdate request)
  {
    return this.exeOrmUpdate.execute(request);
  }
  
  public int executeSqlUpdate(PersistRequestUpdateSql request)
  {
    return this.exeUpdateSql.execute(request);
  }
  
  public int executeSqlCallable(PersistRequestCallableSql request)
  {
    return this.exeCallableSql.execute(request);
  }
}
