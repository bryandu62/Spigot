package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebeaninternal.api.DerivedRelationshipData;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.SpiUpdatePlan;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.type.DataBind;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import javax.persistence.OptimisticLockException;

public class UpdateHandler
  extends DmlHandler
{
  private final UpdateMeta meta;
  private Set<String> updatedProperties;
  private boolean emptySetClause;
  
  public UpdateHandler(PersistRequestBean<?> persist, UpdateMeta meta)
  {
    super(persist, meta.isEmptyStringAsNull());
    this.meta = meta;
  }
  
  public void bind()
    throws SQLException
  {
    SpiUpdatePlan updatePlan = this.meta.getUpdatePlan(this.persistRequest);
    if (updatePlan.isEmptySetClause())
    {
      this.emptySetClause = true;
      return;
    }
    this.updatedProperties = updatePlan.getProperties();
    
    this.sql = updatePlan.getSql();
    
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
    
    bindLogAppend("Binding Update [");
    bindLogAppend(this.meta.getTableName());
    bindLogAppend("] ");
    
    this.meta.bind(this.persistRequest, this, updatePlan);
    
    setUpdateGenValues();
    
    bindLogAppend("]");
    logBinding();
  }
  
  public void addBatch()
    throws SQLException
  {
    if (!this.emptySetClause) {
      super.addBatch();
    }
  }
  
  public void execute()
    throws SQLException, OptimisticLockException
  {
    if (!this.emptySetClause)
    {
      int rowCount = this.dataBind.executeUpdate();
      checkRowCount(rowCount);
      setAdditionalProperties();
    }
  }
  
  public boolean isIncluded(BeanProperty prop)
  {
    return (prop.isDbUpdatable()) && ((this.updatedProperties == null) || (this.updatedProperties.contains(prop.getName())));
  }
  
  public void registerDerivedRelationship(DerivedRelationshipData derivedRelationship)
  {
    this.persistRequest.getTransaction().registerDerivedRelationship(derivedRelationship);
  }
}
