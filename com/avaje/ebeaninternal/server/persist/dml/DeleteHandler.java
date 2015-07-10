package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebeaninternal.api.DerivedRelationshipData;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.type.DataBind;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import javax.persistence.OptimisticLockException;

public class DeleteHandler
  extends DmlHandler
{
  private final DeleteMeta meta;
  
  public DeleteHandler(PersistRequestBean<?> persist, DeleteMeta meta)
  {
    super(persist, meta.isEmptyStringAsNull());
    this.meta = meta;
  }
  
  public void bind()
    throws SQLException
  {
    this.sql = this.meta.getSql(this.persistRequest);
    
    SpiTransaction t = this.persistRequest.getTransaction();
    boolean isBatch = t.isBatchThisRequest();
    PreparedStatement pstmt;
    PreparedStatement pstmt;
    if (isBatch)
    {
      pstmt = getPstmt(t, this.sql, this.persistRequest, false);
    }
    else
    {
      logSql(this.sql);
      pstmt = getPstmt(t, this.sql, false);
    }
    this.dataBind = new DataBind(pstmt);
    
    bindLogAppend("Binding Delete [");
    bindLogAppend(this.meta.getTableName());
    bindLogAppend("] where[");
    
    this.meta.bind(this.persistRequest, this);
    
    bindLogAppend("]");
    
    logBinding();
  }
  
  public void execute()
    throws SQLException, OptimisticLockException
  {
    int rowCount = this.dataBind.executeUpdate();
    checkRowCount(rowCount);
  }
  
  public boolean isIncluded(BeanProperty prop)
  {
    return (prop.isDbUpdatable()) && (super.isIncluded(prop));
  }
  
  public boolean isIncludedWhere(BeanProperty prop)
  {
    return (prop.isDbUpdatable()) && ((this.loadedProps == null) || (this.loadedProps.contains(prop.getName())));
  }
  
  public void registerDerivedRelationship(DerivedRelationshipData assocBean)
  {
    throw new RuntimeException("Never called on delete");
  }
}
