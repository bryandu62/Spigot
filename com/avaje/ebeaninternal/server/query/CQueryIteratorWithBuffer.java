package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.QueryIterator;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.persistence.PersistenceException;

class CQueryIteratorWithBuffer<T>
  implements QueryIterator<T>
{
  private final CQuery<T> cquery;
  private final int bufferSize;
  private final OrmQueryRequest<T> request;
  private final ArrayList<T> buffer;
  private boolean moreToLoad = true;
  
  CQueryIteratorWithBuffer(CQuery<T> cquery, OrmQueryRequest<T> request, int bufferSize)
  {
    this.cquery = cquery;
    this.request = request;
    this.bufferSize = bufferSize;
    this.buffer = new ArrayList(bufferSize);
  }
  
  public boolean hasNext()
  {
    try
    {
      if ((this.buffer.isEmpty()) && (this.moreToLoad))
      {
        int i = -1;
        while (this.moreToLoad)
        {
          i++;
          if (i >= this.bufferSize) {
            break;
          }
          if (this.cquery.hasNextBean(true)) {
            this.buffer.add(this.cquery.getLoadedBean());
          } else {
            this.moreToLoad = false;
          }
        }
        this.request.executeSecondaryQueries(this.bufferSize);
      }
      return !this.buffer.isEmpty();
    }
    catch (SQLException e)
    {
      throw this.cquery.createPersistenceException(e);
    }
  }
  
  public T next()
  {
    return (T)this.buffer.remove(0);
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
