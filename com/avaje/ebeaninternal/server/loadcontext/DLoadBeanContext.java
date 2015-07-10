package com.avaje.ebeaninternal.server.loadcontext;

import com.avaje.ebean.bean.BeanLoader;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.api.LoadBeanContext;
import com.avaje.ebeaninternal.api.LoadBeanRequest;
import com.avaje.ebeaninternal.api.LoadContext;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DLoadBeanContext
  implements LoadBeanContext, BeanLoader
{
  private static final Logger logger = Logger.getLogger(DLoadBeanContext.class.getName());
  protected final DLoadContext parent;
  protected final BeanDescriptor<?> desc;
  protected final String path;
  protected final String fullPath;
  private final DLoadList<EntityBeanIntercept> weakList;
  private final OrmQueryProperties queryProps;
  private int batchSize;
  
  public DLoadBeanContext(DLoadContext parent, BeanDescriptor<?> desc, String path, int batchSize, OrmQueryProperties queryProps, DLoadList<EntityBeanIntercept> weakList)
  {
    this.parent = parent;
    this.desc = desc;
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
  
  public void configureQuery(SpiQuery<?> query, String lazyLoadProperty)
  {
    if (this.parent.isReadOnly() != null) {
      query.setReadOnly(this.parent.isReadOnly().booleanValue());
    }
    query.setParentNode(getObjectGraphNode());
    query.setLazyLoadProperty(lazyLoadProperty);
    if (this.queryProps != null) {
      this.queryProps.configureBeanQuery(query);
    }
    if (this.parent.isUseAutofetchManager()) {
      query.setAutofetch(true);
    }
  }
  
  public String getFullPath()
  {
    return this.fullPath;
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.parent.getPersistenceContext();
  }
  
  public OrmQueryProperties getQueryProps()
  {
    return this.queryProps;
  }
  
  public ObjectGraphNode getObjectGraphNode()
  {
    return this.parent.getObjectGraphNode(this.path);
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public String getName()
  {
    return this.parent.getEbeanServer().getName();
  }
  
  public int getBatchSize()
  {
    return this.batchSize;
  }
  
  public void setBatchSize(int batchSize)
  {
    this.batchSize = batchSize;
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.desc;
  }
  
  public LoadContext getGraphContext()
  {
    return this.parent;
  }
  
  public void register(EntityBeanIntercept ebi)
  {
    int pos = this.weakList.add(ebi);
    ebi.setBeanLoader(pos, this, this.parent.getPersistenceContext());
  }
  
  private boolean loadBeanFromCache(EntityBeanIntercept ebi, int position)
  {
    if (!this.desc.loadFromCache(ebi)) {
      return false;
    }
    this.weakList.removeEntry(position);
    if (logger.isLoggable(Level.FINEST)) {
      logger.log(Level.FINEST, "Loading path:" + this.fullPath + " - bean loaded from L2 cache, position[" + position + "]");
    }
    return true;
  }
  
  public void loadBean(EntityBeanIntercept ebi)
  {
    if (this.desc.lazyLoadMany(ebi)) {
      return;
    }
    int position = ebi.getBeanLoaderIndex();
    boolean hitCache = (!this.parent.isExcludeBeanCache()) && (this.desc.isBeanCaching());
    if ((hitCache) && (loadBeanFromCache(ebi, position))) {
      return;
    }
    List<EntityBeanIntercept> batch = null;
    try
    {
      batch = this.weakList.getLoadBatch(position, this.batchSize);
    }
    catch (IllegalStateException e)
    {
      logger.log(Level.SEVERE, "type[" + this.desc.getFullName() + "] fullPath[" + this.fullPath + "] batchSize[" + this.batchSize + "]", e);
    }
    if ((hitCache) && (this.batchSize > 1)) {
      batch = loadBeanCheckBatch(batch);
    }
    if (logger.isLoggable(Level.FINER)) {
      for (int i = 0; i < batch.size(); i++)
      {
        EntityBeanIntercept entityBeanIntercept = (EntityBeanIntercept)batch.get(i);
        EntityBean owner = entityBeanIntercept.getOwner();
        Object id = this.desc.getId(owner);
        
        logger.finer("LoadBean type[" + owner.getClass().getName() + "] fullPath[" + this.fullPath + "] id[" + id + "] batchIndex[" + i + "] beanLoaderIndex[" + entityBeanIntercept.getBeanLoaderIndex() + "]");
      }
    }
    LoadBeanRequest req = new LoadBeanRequest(this, batch, null, this.batchSize, true, ebi.getLazyLoadProperty(), hitCache);
    this.parent.getEbeanServer().loadBean(req);
  }
  
  private List<EntityBeanIntercept> loadBeanCheckBatch(List<EntityBeanIntercept> batch)
  {
    List<EntityBeanIntercept> actualLoadBatch = new ArrayList(this.batchSize);
    List<EntityBeanIntercept> batchToCheck = batch;
    
    int loadedFromCache = 0;
    for (;;)
    {
      for (int i = 0; i < batchToCheck.size(); i++) {
        if (!this.desc.loadFromCache((EntityBeanIntercept)batchToCheck.get(i)))
        {
          actualLoadBatch.add(batchToCheck.get(i));
        }
        else
        {
          loadedFromCache++;
          if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "Loading path:" + this.fullPath + " - bean loaded from L2 cache(batch)");
          }
        }
      }
      if (batchToCheck.isEmpty()) {
        break;
      }
      int more = this.batchSize - actualLoadBatch.size();
      if ((more <= 0) || (loadedFromCache > 500)) {
        break;
      }
      batchToCheck = this.weakList.getNextBatch(more);
    }
    return actualLoadBatch;
  }
  
  public void loadSecondaryQuery(OrmQueryRequest<?> parentRequest, int requestedBatchSize, boolean all)
  {
    synchronized (this)
    {
      for (;;)
      {
        List<EntityBeanIntercept> batch = this.weakList.getNextBatch(requestedBatchSize);
        if (batch.size() == 0)
        {
          if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "Loading path:" + this.fullPath + " - no more beans to load");
          }
          return;
        }
        boolean loadCache = false;
        LoadBeanRequest req = new LoadBeanRequest(this, batch, parentRequest.getTransaction(), requestedBatchSize, false, null, loadCache);
        if (logger.isLoggable(Level.FINEST)) {
          logger.log(Level.FINEST, "Loading path:" + this.fullPath + " - secondary query batch load [" + batch.size() + "] beans");
        }
        this.parent.getEbeanServer().loadBean(req);
        if (!all) {
          break;
        }
      }
    }
  }
}
