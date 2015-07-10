package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.QueryIterator;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import java.sql.SQLException;
import javax.persistence.PersistenceException;

class CQueryIteratorSimple<T>
  implements QueryIterator<T>
{
  private final CQuery<T> cquery;
  private final OrmQueryRequest<T> request;
  
  CQueryIteratorSimple(CQuery<T> cquery, OrmQueryRequest<T> request)
  {
    this.cquery = cquery;
    this.request = request;
  }
  
  public boolean hasNext()
  {
    try
    {
      return this.cquery.hasNextBean(true);
    }
    catch (SQLException e)
    {
      throw this.cquery.createPersistenceException(e);
    }
  }
  
  public T next()
  {
    return (T)this.cquery.getLoadedBean();
  }
  
  public void close()
  {
    this.cquery.updateExecutionStatistics();
    this.cquery.close();
    this.request.endTransIfRequired();
  }
  
  public void remove()
  {
    throw new PersistenceException("Remove not allowed");
  }
}
