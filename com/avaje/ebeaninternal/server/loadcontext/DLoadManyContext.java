package com.avaje.ebeaninternal.server.loadcontext;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.api.LoadManyContext;
import com.avaje.ebeaninternal.api.LoadManyRequest;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import java.util.List;

public class DLoadManyContext
  implements LoadManyContext, BeanCollectionLoader
{
  protected final DLoadContext parent;
  protected final String fullPath;
  private final BeanDescriptor<?> desc;
  private final BeanPropertyAssocMany<?> property;
  private final String path;
  private final int batchSize;
  private final OrmQueryProperties queryProps;
  private final DLoadList<BeanCollection<?>> weakList;
  
  public DLoadManyContext(DLoadContext parent, BeanPropertyAssocMany<?> p, String path, int batchSize, OrmQueryProperties queryProps, DLoadList<BeanCollection<?>> weakList)
  {
    this.parent = parent;
    this.property = p;
    this.desc = p.getBeanDescriptor();
    this.path = path;
    this.batchSize = batchSize;
    this.queryProps = queryProps;
    this.weakList = weakList;
    if (parent.getRelativePath() == null) {
      this.fullPath = path;
    } else {
      this.fullPath = (parent.getRelativePath() + "." + path);
    }
  }
  
  public void configureQuery(SpiQuery<?> query)
  {
    if (this.parent.isReadOnly() != null) {
      query.setReadOnly(this.parent.isReadOnly().booleanValue());
    }
    query.setParentNode(getObjectGraphNode());
    if (this.queryProps != null) {
      this.queryProps.configureManyQuery(query);
    }
    if (this.parent.isUseAutofetchManager()) {
      query.setAutofetch(true);
    }
  }
  
  public ObjectGraphNode getObjectGraphNode()
  {
    int pos = this.path.lastIndexOf('.');
    if (pos == -1) {
      return this.parent.getObjectGraphNode(null);
    }
    String parentPath = this.path.substring(0, pos);
    return this.parent.getObjectGraphNode(parentPath);
  }
  
  public String getFullPath()
  {
    return this.fullPath;
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.parent.getPersistenceContext();
  }
  
  public int getBatchSize()
  {
    return this.batchSize;
  }
  
  public BeanPropertyAssocMany<?> getBeanProperty()
  {
    return this.property;
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.desc;
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public String getName()
  {
    return this.parent.getEbeanServer().getName();
  }
  
  public void register(BeanCollection<?> bc)
  {
    int pos = this.weakList.add(bc);
    bc.setLoader(pos, this);
  }
  
  public void loadMany(BeanCollection<?> bc, boolean onlyIds)
  {
    int position = bc.getLoaderIndex();
    LoadManyRequest req;
    synchronized (this.weakList)
    {
      boolean hitCache = (this.desc.isBeanCaching()) && (!onlyIds) && (!this.parent.isExcludeBeanCache());
      if (hitCache)
      {
        Object ownerBean = bc.getOwnerBean();
        BeanDescriptor<? extends Object> parentDesc = this.desc.getBeanDescriptor(ownerBean.getClass());
        Object parentId = parentDesc.getId(ownerBean);
        if (parentDesc.cacheLoadMany(this.property, bc, parentId, this.parent.isReadOnly(), false))
        {
          this.weakList.removeEntry(position);
          return;
        }
      }
      List<BeanCollection<?>> loadBatch = this.weakList.getLoadBatch(position, this.batchSize);
      req = new LoadManyRequest(this, loadBatch, null, this.batchSize, true, onlyIds, hitCache);
    }
    this.parent.getEbeanServer().loadMany(req);
  }
  
  public void loadSecondaryQuery(OrmQueryRequest<?> parentRequest, int requestedBatchSize, boolean all)
  {
    for (;;)
    {
      LoadManyRequest req;
      synchronized (this.weakList)
      {
        List<BeanCollection<?>> batch = this.weakList.getNextBatch(requestedBatchSize);
        if (batch.size() == 0) {
          return;
        }
        req = new LoadManyRequest(this, batch, parentRequest.getTransaction(), requestedBatchSize, false, false, false);
      }
      this.parent.getEbeanServer().loadMany(req);
      if (!all) {
        break;
      }
    }
  }
}
