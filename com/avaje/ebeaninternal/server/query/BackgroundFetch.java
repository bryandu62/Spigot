package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebeaninternal.api.SpiTransaction;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackgroundFetch
  implements Callable<Integer>
{
  private static final Logger logger = Logger.getLogger(BackgroundFetch.class.getName());
  private final CQuery<?> cquery;
  private final SpiTransaction transaction;
  
  public BackgroundFetch(CQuery<?> cquery)
  {
    this.cquery = cquery;
    this.transaction = cquery.getTransaction();
  }
  
  public Integer call()
  {
    try
    {
      BeanCollection<?> bc = this.cquery.continueFetchingInBackground();
      
      return Integer.valueOf(bc.size());
    }
    catch (Exception e)
    {
      Integer localInteger;
      logger.log(Level.SEVERE, null, e);
      return Integer.valueOf(0);
    }
    finally
    {
      try
      {
        this.cquery.close();
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
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("BackgroundFetch ").append(this.cquery);
    return sb.toString();
  }
}
