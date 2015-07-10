package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;
import com.avaje.ebeaninternal.api.Monitor;
import com.avaje.ebeaninternal.api.SpiQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.persistence.PersistenceException;

public class LimitOffsetPagingQuery<T>
  implements PagingList<T>
{
  private transient EbeanServer server;
  private final SpiQuery<T> query;
  private final List<LimitOffsetPage<T>> pages = new ArrayList();
  private final Monitor monitor = new Monitor();
  private final int pageSize;
  private boolean fetchAhead = true;
  private Future<Integer> futureRowCount;
  
  public LimitOffsetPagingQuery(EbeanServer server, SpiQuery<T> query, int pageSize)
  {
    this.query = query;
    this.pageSize = pageSize;
    this.server = server;
  }
  
  public EbeanServer getServer()
  {
    return this.server;
  }
  
  public void setServer(EbeanServer server)
  {
    this.server = server;
  }
  
  public SpiQuery<T> getSpiQuery()
  {
    return this.query;
  }
  
  public PagingList<T> setFetchAhead(boolean fetchAhead)
  {
    this.fetchAhead = fetchAhead;
    return this;
  }
  
  public List<T> getAsList()
  {
    return new LimitOffsetList(this);
  }
  
  public Future<Integer> getFutureRowCount()
  {
    synchronized (this.monitor)
    {
      if (this.futureRowCount == null) {
        this.futureRowCount = this.server.findFutureRowCount(this.query, null);
      }
      return this.futureRowCount;
    }
  }
  
  private LimitOffsetPage<T> internalGetPage(int i)
  {
    synchronized (this.monitor)
    {
      int ps = this.pages.size();
      if (ps <= i) {
        for (int j = ps; j <= i; j++)
        {
          LimitOffsetPage<T> p = new LimitOffsetPage(j, this);
          this.pages.add(p);
        }
      }
      return (LimitOffsetPage)this.pages.get(i);
    }
  }
  
  protected void fetchAheadIfRequired(int pageIndex)
  {
    synchronized (this.monitor)
    {
      if (this.fetchAhead)
      {
        LimitOffsetPage<T> nextPage = internalGetPage(pageIndex + 1);
        nextPage.getFutureList();
      }
    }
  }
  
  public void refresh()
  {
    synchronized (this.monitor)
    {
      this.futureRowCount = null;
      this.pages.clear();
    }
  }
  
  public Page<T> getPage(int i)
  {
    return internalGetPage(i);
  }
  
  protected boolean hasNext(int position)
  {
    return position < getTotalRowCount();
  }
  
  protected T get(int rowIndex)
  {
    int pg = rowIndex / this.pageSize;
    int offset = rowIndex % this.pageSize;
    
    Page<T> page = getPage(pg);
    return (T)page.getList().get(offset);
  }
  
  public int getTotalPageCount()
  {
    int rowCount = getTotalRowCount();
    if (rowCount == 0) {
      return 0;
    }
    return (rowCount - 1) / this.pageSize + 1;
  }
  
  public int getPageSize()
  {
    return this.pageSize;
  }
  
  public int getTotalRowCount()
  {
    try
    {
      return ((Integer)getFutureRowCount().get()).intValue();
    }
    catch (Exception e)
    {
      throw new PersistenceException(e);
    }
  }
}
