package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.PersistRequest;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.persist.BeanPersister;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public final class DmlBeanPersister
  implements BeanPersister
{
  private static final Logger logger = Logger.getLogger(DmlBeanPersister.class.getName());
  private final UpdateMeta updateMeta;
  private final InsertMeta insertMeta;
  private final DeleteMeta deleteMeta;
  
  public DmlBeanPersister(UpdateMeta updateMeta, InsertMeta insertMeta, DeleteMeta deleteMeta)
  {
    this.updateMeta = updateMeta;
    this.insertMeta = insertMeta;
    this.deleteMeta = deleteMeta;
  }
  
  public void delete(PersistRequestBean<?> request)
  {
    DeleteHandler delete = new DeleteHandler(request, this.deleteMeta);
    execute(request, delete);
  }
  
  public void insert(PersistRequestBean<?> request)
  {
    InsertHandler insert = new InsertHandler(request, this.insertMeta);
    execute(request, insert);
  }
  
  public void update(PersistRequestBean<?> request)
  {
    UpdateHandler update = new UpdateHandler(request, this.updateMeta);
    execute(request, update);
  }
  
  private void execute(PersistRequest request, PersistHandler handler)
  {
    SpiTransaction trans = request.getTransaction();
    boolean batchThisRequest = trans.isBatchThisRequest();
    try
    {
      handler.bind();
      if (batchThisRequest) {
        handler.addBatch();
      } else {
        handler.execute();
      }
      String errMsg;
      String msg;
      return;
    }
    catch (SQLException e)
    {
      errMsg = StringHelper.replaceStringMulti(e.getMessage(), new String[] { "\r", "\n" }, "\\n ");
      msg = "ERROR executing DML bindLog[" + handler.getBindLog() + "] error[" + errMsg + "]";
      if (request.getTransaction().isLogSummary()) {
        request.getTransaction().logInternal(msg);
      }
      throw new PersistenceException(msg, e);
    }
    finally
    {
      if ((!batchThisRequest) && (handler != null)) {
        try
        {
          handler.close();
        }
        catch (SQLException e)
        {
          logger.log(Level.SEVERE, null, e);
        }
      }
    }
  }
}
