package com.avaje.ebeaninternal.server.querydefn;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlFutureList;
import com.avaje.ebean.SqlQueryListener;
import com.avaje.ebean.SqlRow;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.SpiSqlQuery;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.PersistenceException;

public class DefaultRelationalQuery
  implements SpiSqlQuery
{
  private static final long serialVersionUID = -1098305779779591068L;
  private transient EbeanServer server;
  private transient SqlQueryListener queryListener;
  private String query;
  private int firstRow;
  private int maxRows;
  private int timeout;
  private boolean futureFetch;
  private boolean cancelled;
  private transient PreparedStatement pstmt;
  private int backgroundFetchAfter;
  private int bufferFetchSizeHint;
  private String mapKey;
  private BindParams bindParams = new BindParams();
  
  public DefaultRelationalQuery(EbeanServer server, String query)
  {
    this.server = server;
    this.query = query;
  }
  
  public DefaultRelationalQuery setQuery(String query)
  {
    this.query = query;
    return this;
  }
  
  public List<SqlRow> findList()
  {
    return this.server.findList(this, null);
  }
  
  public Set<SqlRow> findSet()
  {
    return this.server.findSet(this, null);
  }
  
  public Map<?, SqlRow> findMap()
  {
    return this.server.findMap(this, null);
  }
  
  public SqlRow findUnique()
  {
    return this.server.findUnique(this, null);
  }
  
  public SqlFutureList findFutureList()
  {
    return this.server.findFutureList(this, null);
  }
  
  public DefaultRelationalQuery setParameter(int position, Object value)
  {
    this.bindParams.setParameter(position, value);
    return this;
  }
  
  public DefaultRelationalQuery setParameter(String paramName, Object value)
  {
    this.bindParams.setParameter(paramName, value);
    return this;
  }
  
  public SqlQueryListener getListener()
  {
    return this.queryListener;
  }
  
  public DefaultRelationalQuery setListener(SqlQueryListener queryListener)
  {
    this.queryListener = queryListener;
    return this;
  }
  
  public String toString()
  {
    return "SqlQuery [" + this.query + "]";
  }
  
  public int getFirstRow()
  {
    return this.firstRow;
  }
  
  public DefaultRelationalQuery setFirstRow(int firstRow)
  {
    this.firstRow = firstRow;
    return this;
  }
  
  public int getMaxRows()
  {
    return this.maxRows;
  }
  
  public DefaultRelationalQuery setMaxRows(int maxRows)
  {
    this.maxRows = maxRows;
    return this;
  }
  
  public String getMapKey()
  {
    return this.mapKey;
  }
  
  public DefaultRelationalQuery setMapKey(String mapKey)
  {
    this.mapKey = mapKey;
    return this;
  }
  
  public int getBackgroundFetchAfter()
  {
    return this.backgroundFetchAfter;
  }
  
  public DefaultRelationalQuery setBackgroundFetchAfter(int backgroundFetchAfter)
  {
    this.backgroundFetchAfter = backgroundFetchAfter;
    return this;
  }
  
  public int getTimeout()
  {
    return this.timeout;
  }
  
  public DefaultRelationalQuery setTimeout(int secs)
  {
    this.timeout = secs;
    return this;
  }
  
  public BindParams getBindParams()
  {
    return this.bindParams;
  }
  
  public DefaultRelationalQuery setBufferFetchSizeHint(int bufferFetchSizeHint)
  {
    this.bufferFetchSizeHint = bufferFetchSizeHint;
    return this;
  }
  
  public int getBufferFetchSizeHint()
  {
    return this.bufferFetchSizeHint;
  }
  
  public String getQuery()
  {
    return this.query;
  }
  
  public boolean isFutureFetch()
  {
    return this.futureFetch;
  }
  
  public void setFutureFetch(boolean futureFetch)
  {
    this.futureFetch = futureFetch;
  }
  
  public void setPreparedStatement(PreparedStatement pstmt)
  {
    synchronized (this)
    {
      this.pstmt = pstmt;
    }
  }
  
  public void cancel()
  {
    synchronized (this)
    {
      this.cancelled = true;
      if (this.pstmt != null) {
        try
        {
          this.pstmt.cancel();
        }
        catch (SQLException e)
        {
          String msg = "Error cancelling query";
          throw new PersistenceException(msg, e);
        }
      }
    }
  }
  
  /* Error */
  public boolean isCancelled()
  {
    // Byte code:
    //   0: aload_0
    //   1: dup
    //   2: astore_1
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield 158	com/avaje/ebeaninternal/server/querydefn/DefaultRelationalQuery:cancelled	Z
    //   8: aload_1
    //   9: monitorexit
    //   10: ireturn
    //   11: astore_2
    //   12: aload_1
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Line number table:
    //   Java source line #222	-> byte code offset #0
    //   Java source line #223	-> byte code offset #4
    //   Java source line #224	-> byte code offset #11
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	16	0	this	DefaultRelationalQuery
    //   2	11	1	Ljava/lang/Object;	Object
    //   11	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   4	10	11	finally
    //   11	14	11	finally
  }
}
