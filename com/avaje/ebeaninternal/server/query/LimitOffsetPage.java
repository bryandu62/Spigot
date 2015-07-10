package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.FutureList;
import com.avaje.ebean.Page;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionTouched;
import com.avaje.ebeaninternal.api.SpiQuery;
import java.util.List;
import javax.persistence.PersistenceException;

public class LimitOffsetPage<T>
  implements Page<T>, BeanCollectionTouched
{
  private final int pageIndex;
  private final LimitOffsetPagingQuery<T> owner;
  private FutureList<T> futureList;
  
  public LimitOffsetPage(int pageIndex, LimitOffsetPagingQuery<T> owner)
  {
    this.pageIndex = pageIndex;
    this.owner = owner;
  }
  
  public FutureList<T> getFutureList()
  {
    if (this.futureList == null)
    {
      SpiQuery<T> originalQuery = this.owner.getSpiQuery();
      SpiQuery<T> copy = originalQuery.copy();
      copy.setPersistenceContext(originalQuery.getPersistenceContext());
      
      int pageSize = this.owner.getPageSize();
      copy.setFirstRow(this.pageIndex * pageSize);
      copy.setMaxRows(pageSize);
      copy.setBeanCollectionTouched(this);
      this.futureList = this.owner.getServer().findFutureList(copy, null);
    }
    return this.futureList;
  }
  
  public void notifyTouched(BeanCollection<?> c)
  {
    if (c.hasMoreRows()) {
      this.owner.fetchAheadIfRequired(this.pageIndex);
    }
  }
  
  public List<T> getList()
  {
    try
    {
      return (List)getFutureList().get();
    }
    catch (Exception e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public boolean hasNext()
  {
    return ((BeanCollection)getList()).hasMoreRows();
  }
  
  public boolean hasPrev()
  {
    return this.pageIndex > 0;
  }
  
  public Page<T> next()
  {
    return this.owner.getPage(this.pageIndex + 1);
  }
  
  public Page<T> prev()
  {
    return this.owner.getPage(this.pageIndex - 1);
  }
  
  public int getPageIndex()
  {
    return this.pageIndex;
  }
  
  public int getTotalPageCount()
  {
    return this.owner.getTotalPageCount();
  }
  
  public int getTotalRowCount()
  {
    return this.owner.getTotalRowCount();
  }
  
  public String getDisplayXtoYofZ(String to, String of)
  {
    int first = this.pageIndex * this.owner.getPageSize() + 1;
    int last = first + getList().size() - 1;
    int total = getTotalRowCount();
    
    return first + to + last + of + total;
  }
}
