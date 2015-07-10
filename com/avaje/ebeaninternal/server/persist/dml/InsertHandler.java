package com.avaje.ebeaninternal.server.persist.dml;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.DerivedRelationshipData;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.Message;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.persist.DmlUtil;
import com.avaje.ebeaninternal.server.type.DataBind;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

public class InsertHandler
  extends DmlHandler
{
  private final InsertMeta meta;
  private final boolean concatinatedKey;
  private boolean useGeneratedKeys;
  private String selectLastInsertedId;
  
  public InsertHandler(PersistRequestBean<?> persist, InsertMeta meta)
  {
    super(persist, meta.isEmptyStringToNull());
    this.meta = meta;
    this.concatinatedKey = meta.isConcatinatedKey();
  }
  
  public boolean isIncluded(BeanProperty prop)
  {
    return (prop.isDbInsertable()) && (super.isIncluded(prop));
  }
  
  public void bind()
    throws SQLException
  {
    BeanDescriptor<?> desc = this.persistRequest.getBeanDescriptor();
    Object bean = this.persistRequest.getBean();
    
    Object idValue = desc.getId(bean);
    
    boolean withId = !DmlUtil.isNullOrZero(idValue);
    if (!withId) {
      if (this.concatinatedKey) {
        withId = this.meta.deriveConcatenatedId(this.persistRequest);
      } else if (this.meta.supportsGetGeneratedKeys()) {
        this.useGeneratedKeys = true;
      } else {
        this.selectLastInsertedId = this.meta.getSelectLastInsertedId();
      }
    }
    SpiTransaction t = this.persistRequest.getTransaction();
    boolean isBatch = t.isBatchThisRequest();
    
    this.sql = this.meta.getSql(withId);
    PreparedStatement pstmt;
    PreparedStatement pstmt;
    if (isBatch)
    {
      pstmt = getPstmt(t, this.sql, this.persistRequest, this.useGeneratedKeys);
    }
    else
    {
      logSql(this.sql);
      pstmt = getPstmt(t, this.sql, this.useGeneratedKeys);
    }
    this.dataBind = new DataBind(pstmt);
    
    bindLogAppend("Binding Insert [");
    bindLogAppend(desc.getBaseTable());
    bindLogAppend("]  set[");
    
    this.meta.bind(this, bean, withId);
    
    bindLogAppend("]");
    logBinding();
  }
  
  protected PreparedStatement getPstmt(SpiTransaction t, String sql, boolean useGeneratedKeys)
    throws SQLException
  {
    Connection conn = t.getInternalConnection();
    if (useGeneratedKeys) {
      return conn.prepareStatement(sql, 1);
    }
    return conn.prepareStatement(sql);
  }
  
  public void execute()
    throws SQLException, OptimisticLockException
  {
    int rc = this.dataBind.executeUpdate();
    if (this.useGeneratedKeys) {
      getGeneratedKeys();
    } else if (this.selectLastInsertedId != null) {
      fetchGeneratedKeyUsingSelect();
    }
    checkRowCount(rc);
    setAdditionalProperties();
    executeDerivedRelationships();
  }
  
  protected void executeDerivedRelationships()
  {
    List<DerivedRelationshipData> derivedRelationships = this.persistRequest.getDerivedRelationships();
    if (derivedRelationships != null) {
      for (int i = 0; i < derivedRelationships.size(); i++)
      {
        DerivedRelationshipData derivedRelationshipData = (DerivedRelationshipData)derivedRelationships.get(i);
        
        EbeanServer ebeanServer = this.persistRequest.getEbeanServer();
        HashSet<String> updateProps = new HashSet();
        updateProps.add(derivedRelationshipData.getLogicalName());
        ebeanServer.update(derivedRelationshipData.getBean(), updateProps, this.transaction, false, true);
      }
    }
  }
  
  private void getGeneratedKeys()
    throws SQLException
  {
    ResultSet rset = this.dataBind.getPstmt().getGeneratedKeys();
    try
    {
      if (rset.next())
      {
        Object idValue = rset.getObject(1);
        if (idValue != null) {
          this.persistRequest.setGeneratedKey(idValue);
        }
      }
      else
      {
        throw new PersistenceException(Message.msg("persist.autoinc.norows"));
      }
      String msg;
      String msg;
      return;
    }
    finally
    {
      try
      {
        rset.close();
      }
      catch (SQLException ex)
      {
        msg = "Error closing rset for returning generatedKeys?";
        logger.log(Level.WARNING, msg, ex);
      }
    }
  }
  
  private void fetchGeneratedKeyUsingSelect()
    throws SQLException
  {
    Connection conn = this.transaction.getConnection();
    
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try
    {
      stmt = conn.prepareStatement(this.selectLastInsertedId);
      rset = stmt.executeQuery();
      if (rset.next())
      {
        Object idValue = rset.getObject(1);
        if (idValue != null) {
          this.persistRequest.setGeneratedKey(idValue);
        }
      }
      else
      {
        throw new PersistenceException(Message.msg("persist.autoinc.norows"));
      }
      String msg;
      String msg;
      String msg;
      String msg;
      return;
    }
    finally
    {
      try
      {
        if (rset != null) {
          rset.close();
        }
      }
      catch (SQLException ex)
      {
        msg = "Error closing rset for fetchGeneratedKeyUsingSelect?";
        logger.log(Level.WARNING, msg, ex);
      }
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException ex)
      {
        msg = "Error closing stmt for fetchGeneratedKeyUsingSelect?";
        logger.log(Level.WARNING, msg, ex);
      }
    }
  }
  
  public void registerDerivedRelationship(DerivedRelationshipData derivedRelationship)
  {
    this.persistRequest.getTransaction().registerDerivedRelationship(derivedRelationship);
  }
}
