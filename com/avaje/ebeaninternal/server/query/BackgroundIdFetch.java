package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.api.BeanIdList;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.type.DataReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackgroundIdFetch
  implements Callable<Integer>
{
  private static final Logger logger = Logger.getLogger(BackgroundIdFetch.class.getName());
  private final ResultSet rset;
  private final PreparedStatement pstmt;
  private final SpiTransaction transaction;
  private final DbReadContext ctx;
  private final BeanDescriptor<?> beanDescriptor;
  private final BeanIdList idList;
  
  public BackgroundIdFetch(SpiTransaction transaction, ResultSet rset, PreparedStatement pstmt, DbReadContext ctx, BeanDescriptor<?> beanDescriptor, BeanIdList idList)
  {
    this.ctx = ctx;
    this.transaction = transaction;
    this.rset = rset;
    this.pstmt = pstmt;
    this.beanDescriptor = beanDescriptor;
    this.idList = idList;
  }
  
  public Integer call()
  {
    try
    {
      int startSize = this.idList.getIdList().size();
      rowsRead = 0;
      Object idValue;
      while (this.rset.next())
      {
        idValue = this.beanDescriptor.getIdBinder().read(this.ctx);
        this.idList.add(idValue);
        this.ctx.getDataReader().resetColumnPosition();
        rowsRead++;
      }
      if (logger.isLoggable(Level.INFO)) {
        logger.info("BG FetchIds read:" + rowsRead + " total:" + (startSize + rowsRead));
      }
      return Integer.valueOf(rowsRead);
    }
    catch (Exception e)
    {
      int rowsRead;
      logger.log(Level.SEVERE, null, e);
      return Integer.valueOf(0);
    }
    finally
    {
      try
      {
        close();
      }
      catch (Exception e)
      {
        logger.log(Level.SEVERE, null, e);
      }
      try
      {
        this.transaction.rollback();
      }
      catch (Exception e)
      {
        logger.log(Level.SEVERE, null, e);
      }
    }
  }
  
  private void close()
  {
    try
    {
      if (this.rset != null) {
        this.rset.close();
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, null, e);
    }
    try
    {
      if (this.pstmt != null) {
        this.pstmt.close();
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, null, e);
    }
  }
}
